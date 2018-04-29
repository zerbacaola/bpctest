package ustinov.sergey.bpctest;

import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ustinov.sergey.bpctest.Gender.FEMALE;
import static ustinov.sergey.bpctest.Gender.MALE;

@Repository
public class PersonDataSourceService {

    // TODO : implement persistence
    public List<PersonTO> search(@Nullable Gender gender, @Nonnull String name, @Nonnull String mother, @Nonnull String father, @Nonnull List<String> childrens, int page, int rowsQuantity) {
        List<PersonTO> result = new ArrayList<>();

        PersonTO p1 = new PersonTO(1, "Петр", MALE, new byte[]{0xF, 0xF, 0xF, 0xF});
        PersonTO p2 = new PersonTO(2, "Тамара", FEMALE, new byte[]{0xF, 0xF, 0xF, 0xF});
        PersonTO p3 = new PersonTO(3, "Иван", MALE, new byte[]{0xF, 0xF, 0xF, 0xF});
        PersonTO p4 = new PersonTO(4, "Ольга", FEMALE, new byte[]{0xF, 0xF, 0xF, 0xF});

        p1.getParents().put(MALE, p3.getId());
        p1.getParents().put(FEMALE, p4.getId());

        p2.getParents().put(MALE, p3.getId());

        p3.getChilds().addAll(Arrays.asList(p1.getId(),p2.getId()));
        p4.getChilds().addAll(Arrays.asList(p1.getId()));

        result.addAll(Arrays.asList(
            p1,p2,p3,p4
        ));

        return result;
    }
}
