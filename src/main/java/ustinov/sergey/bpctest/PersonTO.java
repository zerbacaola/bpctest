package ustinov.sergey.bpctest;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static ustinov.sergey.bpctest.Gender.FEMALE;
import static ustinov.sergey.bpctest.Gender.MALE;

public class PersonTO {

    public static final Function<Object[], PersonTO> TRANSFORMER = array -> {
        final PersonTO p = new PersonTO(
            ((Integer) array[0]).longValue(),
            (String) array[1],
            Gender.parse((String) array[2])
        );

        final PersonRef mRef = new PersonRef();

        ofNullable((Integer) array[3]).map(Integer::longValue)
            .ifPresent(mRef::setId);
        ofNullable((String) array[4])
            .ifPresent(mRef::setName);

        p.getParents().put(FEMALE, mRef);

        final PersonRef fRef = new PersonRef();

        ofNullable((Integer) array[5]).map(Integer::longValue)
            .ifPresent(fRef::setId);
        ofNullable((String) array[6])
            .ifPresent(fRef::setName);

        final PersonRef ref = new PersonRef();

        p.getParents().put(MALE, fRef);

        ofNullable((Integer) array[7]).map(Integer::longValue)
            .ifPresent(ref::setId);
        ofNullable((String) array[8])
            .ifPresent(ref::setName);

        if (ref.isInitialized()) {
            p.getChilds().add(ref);
        }

        return p;
    };

    private long id;
    private String name;
    private Gender gender;
    private Map<Gender, PersonRef> parents = new HashMap<>();
    private List<PersonRef> childs = new ArrayList<>();

    public PersonTO(long id, String name, Gender gender) {
        this.id = id;
        this.name = name;
        this.gender = gender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Map<Gender, PersonRef> getParents() {
        return parents;
    }

    public void setParents(Map<Gender, PersonRef> parents) {
        this.parents = parents;
    }

    public List<PersonRef> getChilds() {
        return childs;
    }

    public void setChilds(List<PersonRef> childs) {
        this.childs = childs;
    }

    private static class PersonRef {
        private String name;
        private Long id;

        public PersonRef() {}

        public PersonRef(String name, Long id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @JsonIgnore
        public boolean isInitialized() {
            return id != null || name != null;
        }
    }
}
