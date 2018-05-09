package ustinov.sergey.bpctest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static ustinov.sergey.bpctest.PersonTO.getTransformer;
import static ustinov.sergey.bpctest.SafeGetter.EMPTY;
import static ustinov.sergey.bpctest.SafeGetter.wrap;

// TODO : define transaction isolated levels
// TODO : optimize SQL
// TODO : implement entinites
@Transactional
@Repository
public class PersonDataSourceService {

    @Value("${db.vendor}")
    private String dbVendor;

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public Collection<PersonTO> search(@Nullable Gender gender, @Nonnull String name, @Nonnull String mother, @Nonnull String father, @Nonnull String child, int offset, int limit) {
        List<Object[]> data = entityManager.createNativeQuery(
                "WITH parents AS (\n" +
                "    SELECT \n" +
                "      id, \n" +
                "      name, \n" +
                "      gender\n" +
                "    FROM person p \n" +
                "      LEFT JOIN person_parent r ON p.id = r.person_id \n" +
                "    WHERE r.person_id IS NULL \n" +
                "),\n" +
                "parents_with_childrens AS (\n" +
                "  SELECT  person.id, person.name, person.gender,\n" +
                "          m.id AS mother_id, m.name AS mother_name,\n" +
                "          f.id AS father_id, f.name AS father_name\n" +
                "  FROM person\n" +
                "  LEFT JOIN (\n" +
                "    SELECT o.id, r.person_id, o.name FROM parents o\n" +
                "    JOIN person_parent r ON o.id = r.parent_id AND o.gender = 'F'\n" +
                "  ) m ON person.id = m.person_id\n" +
                "  LEFT JOIN (\n" +
                "    SELECT o.id, r.person_id, o.name FROM parents o\n" +
                "      JOIN person_parent r ON o.id = r.parent_id AND o.gender = 'M'\n" +
                "  ) f ON person.id = f.person_id\n" +
                "  WHERE (:dontFilterByGender OR person.gender = :gender)\n" +
                "    AND (:dontFilterByName OR LOWER(person.name) LIKE LOWER(:name))\n" +
                "    AND (:dontFilterByMotherName OR LOWER(m.name) LIKE LOWER(:motherName))\n" +
                "    AND (:dontFilterByFatherName OR LOWER(f.name) LIKE LOWER(:fatherName))\n" +
                "  LIMIT :lim OFFSET :off \n" +
                ")\n" +
                "SELECT pc.*, ps.id AS child_id, ps.name AS child_name\n" +
                "  FROM parents_with_childrens pc\n" +
                "  LEFT JOIN person_parent pp ON pp.parent_id = pc.id\n" +
                "  LEFT JOIN person ps ON ps.id = pp.person_id\n " +
                "WHERE (:dontFilterByChildName OR LOWER(ps.name) LIKE LOWER(:childName))\n " +
                "ORDER BY pc.id"
           )
           .setParameter("dontFilterByGender", gender == null)
           .setParameter("gender", ofNullable(gender).map(Gender::getValue).orElse(EMPTY))
           .setParameter("dontFilterByName", name.isEmpty())
           .setParameter("name", wrap(name))
           .setParameter("dontFilterByMotherName", mother.isEmpty())
           .setParameter("motherName", wrap(mother))
           .setParameter("dontFilterByFatherName", father.isEmpty())
           .setParameter("fatherName", wrap(father))
           .setParameter("dontFilterByChildName", child.isEmpty())
           .setParameter("childName", wrap(child))
           .setParameter("lim", limit)
           .setParameter("off", offset)
           .getResultList();

        List<PersonTO> personTOs = data.stream().map(getTransformer(getDbVendor()))
            .collect(Collectors.toList());

        Map<Long, PersonTO> personTOsMapper = personTOs.stream()
            .collect(Collectors.toMap(
                PersonTO::getId, Function.identity(),
                (p1, p2) -> {
                    p1.getChilds().addAll(p2.getChilds());
                    return p1;
                }
            ));

        return personTOsMapper.values();
    }

    @Nullable
    public byte[] getPhoto(long personId) {
        try {
            PersonPhoto photo = entityManager.createQuery(
                "SELECT p FROM PersonPhoto p WHERE p.personId = :personId", PersonPhoto.class
            )
            .setParameter("personId", personId)
            .getSingleResult();
            return photo.getData();
        } catch (NoResultException e) {
            return null;
        }
    }

    private DbVendor getDbVendor() {
        return DbVendor.valueOf(dbVendor);
    }
}
