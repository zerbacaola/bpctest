package ustinov.sergey.bpctest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

/**
 * Facade for performing validations etc.
 */
@Service
public class PersonFacade {
    @Autowired
    private PersonDataSourceService personDao;

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final int ROWS_PER_PAGE = 5;
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[]{};

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

    public byte[] getPhoto(@Nullable String id) {
        // TODO : tune logging system
        log.debug("getPhoto: {}", id);

        Long personId = new SafeGetter<>(id).get(parseLong, null);
        return personId != null ?
            personDao.getPhoto(personId) : EMPTY_BYTE_ARRAY;
    }

    public List<PersonTO> search(@Nullable PersonFilterTO filter) {
        SafeGetter<PersonFilterTO> s = new SafeGetter<>(filter);

        log.debug("{}", filter);

        return personDao.search(
            s.get(parseGender, null),
            s.get(PersonFilterTO::getName),
            s.get(PersonFilterTO::getMother),
            s.get(PersonFilterTO::getFather),
            s.get(PersonFilterTO::getChild),
            s.get(PersonFilterTO::getPage, 1),
            s.get(PersonFilterTO::getRowsPerPage, ROWS_PER_PAGE)
        );
    }
}