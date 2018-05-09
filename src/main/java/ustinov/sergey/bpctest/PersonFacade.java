package ustinov.sergey.bpctest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ustinov.sergey.bpctest.model.enums.Gender;
import ustinov.sergey.bpctest.to.PersonFilterTO;
import ustinov.sergey.bpctest.to.PersonTO;
import ustinov.sergey.bpctest.utils.SafeGetter;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;

/**
 * Facade for performing validations etc.
 */
@Service
public class PersonFacade {
    private Logger log = LoggerFactory.getLogger(getClass());

    private static final int ROWS_PER_PAGE = 10;
    private static final Function<PersonFilterTO, Gender> parseGender = o -> {
        try {
            return ofNullable(o.getGender())
                .map(Gender::valueOf).orElse(null);
        } catch (IllegalArgumentException e) {
            return null;
        }
    };
    private static final Function<String, Long> parseLong = o -> {
        try {
            return ofNullable(o).map(Long::valueOf).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    };
    private static final Predicate<Integer> nonNegative = o -> o >= 0;

    @Autowired
    private PersonDataSourceService personDao;

    @Nullable
    public byte[] getPhoto(@Nullable String id) {
        // TODO : tune logging system
        log.debug("getPhoto: {}", id);

        Long personId = new SafeGetter<>(id).get(parseLong, null);
        return ofNullable(personId)
            .map(personDao::getPhoto).orElse(null);
    }

    public Collection<PersonTO> search(@Nullable PersonFilterTO filter) {
        SafeGetter<PersonFilterTO> s = new SafeGetter<>(filter);

        log.debug("{}", filter);

        return personDao.search(
            s.get(parseGender, null),
            s.get(PersonFilterTO::getName),
            s.get(PersonFilterTO::getMother),
            s.get(PersonFilterTO::getFather),
            s.get(PersonFilterTO::getChild),
            s.get(PersonFilterTO::getOffset, nonNegative, 0),
            s.get(PersonFilterTO::getLimit, nonNegative, ROWS_PER_PAGE)
        );
    }
}