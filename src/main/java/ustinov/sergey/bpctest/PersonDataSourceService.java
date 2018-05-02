package ustinov.sergey.bpctest;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static ustinov.sergey.bpctest.PersonTO.TRANSFORMER;
import static ustinov.sergey.bpctest.SafeGetter.EMPTY;
import static ustinov.sergey.bpctest.SafeGetter.wrap;

// TODO : define transaction isolated levels
// TODO : optimize SQL
// TODO : implement search by children
// TODO : implement entinites
@Transactional
@Repository
public class PersonDataSourceService {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<PersonTO> search(@Nullable Gender gender, @Nonnull String name, @Nonnull String mother, @Nonnull String father, @Nonnull String child, int offset, int limit) {
        List<Object[]> data = entityManager.createNativeQuery(
            "WITH parents AS ( \n" +
            "    SELECT \n" +
            "      id, \n" +
            "      name, \n" +
            "      gender \n" +
            "    FROM person p \n" +
            "      LEFT JOIN person_parent r ON p.id = r.person_id \n" +
            "    WHERE r.person_id IS NULL \n" +
            ") \n" +
            "SELECT person.id, person.name, person.gender, m.id AS mother_id, f.id AS father_id\n" +
            "FROM person \n" +
            "LEFT JOIN ( \n" +
            "  SELECT o.id, r.person_id FROM parents o\n" +
            "  JOIN person_parent r ON o.id = r.parent_id AND o.gender = 'F'\n" +
            "    WHERE (:dontFilterByMotherName OR o.name LIKE :motherName)\n" +
            ") m ON person.id = m.person_id \n" +
            "LEFT JOIN ( \n" +
            "  SELECT o.id, r.person_id FROM parents o \n" +
            "    JOIN person_parent r ON o.id = r.parent_id AND o.gender = 'M'\n" +
            "  WHERE (:dontFilterByFatherName OR o.name LIKE :fatherName)\n" +
            ") f ON person.id = f.person_id\n" +
            "\n" +
            "WHERE (:dontFilterByGender OR person.gender = :gender)\n" +
            "  AND (:dontFilterByName OR person.name LIKE :name)\n" +
            "ORDER BY person.id ASC\n" +
            "LIMIT :lim OFFSET :off"
          )
           .setParameter("dontFilterByGender", gender == null)
           .setParameter("gender", ofNullable(gender).map(Gender::getValue).orElse(EMPTY))
           .setParameter("dontFilterByName", name.isEmpty())
           .setParameter("name", wrap(name))
           .setParameter("dontFilterByMotherName", mother.isEmpty())
           .setParameter("motherName", wrap(mother))
           .setParameter("dontFilterByFatherName", father.isEmpty())
           .setParameter("fatherName", wrap(father))
           .setParameter("lim", limit)
           .setParameter("off", offset)
           .getResultList();


        List<PersonTO> personTOs = data.stream().map(TRANSFORMER)
            .collect(Collectors.toList());

        final Map<Long, PersonTO> parentsTOs = personTOs.stream()
            .filter(PersonTO::isGrandParent)
            .collect(Collectors.toMap(
                PersonTO::getId, Function.identity()
            ));

        // Enrich results
        List<PersonTO> childsTOs = new ArrayList<>(personTOs);
        childsTOs.removeAll(parentsTOs.values());
        childsTOs.forEach(c -> {
            for (Long parentId : c.getParents().values()) {
                ofNullable(parentsTOs.get(parentId)).map(
                    p -> p.getChilds().add(c.getId())
                );
            }
        });
        return personTOs;
    }

    @Nullable
    public byte[] getPhoto(long personId) {
        try {
             byte[] photo = (byte[]) entityManager.createNativeQuery(
                "SELECT data FROM person_photo WHERE person_id = :personId"
            )
            .setParameter("personId", personId)
            .getSingleResult();
            return photo;
        } catch (NoResultException e) {
            return null;
        }
    }
}
