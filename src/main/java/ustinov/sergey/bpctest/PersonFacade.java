package ustinov.sergey.bpctest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Facade for performing validations etc.
 */
@Service
public class PersonFacade {
    @Autowired
    private PersonDataSourceService personDao;

    private static final int ROWS_PER_PAGE = 5;

    public List<PersonTO> search(PersonFilterTO filter) {
        SafeGetter<PersonFilterTO> s = new SafeGetter<>(filter);

        return personDao.search(
            s.get(PersonFilterTO::getGender, null),
            s.get(PersonFilterTO::getName),
            s.get(PersonFilterTO::getMother),
            s.get(PersonFilterTO::getFather),
            s.get(PersonFilterTO::getChildrens, Collections.emptyList()),
            s.get(PersonFilterTO::getPage, 1),
            s.get(PersonFilterTO::getRowsPerPage, ROWS_PER_PAGE)
        );
    }
}