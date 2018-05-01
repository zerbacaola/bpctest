package ustinov.sergey.bpctest;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static ustinov.sergey.bpctest.Gender.FEMALE;
import static ustinov.sergey.bpctest.Gender.MALE;

public class PersonTO {

    public static final Function<Object[], PersonTO> TRANSFORMER = array -> {
        final Map<Gender, Long> parents = new HashMap<>();
        PersonTO p = new PersonTO(
            ((Integer) array[0]).longValue(),
            (String) array[1],
            Gender.parse((String) array[2]),
            parents
        );

        ofNullable((Integer) array[3])
            .map(Integer::longValue).ifPresent(id -> parents.put(FEMALE, id));
        ofNullable((Integer) array[4])
            .map(Integer::longValue).ifPresent(id -> parents.put(MALE, id));
        return p;
    };

    private long id;
    private String name;
    private Gender gender;
    private Map<Gender, Long> parents = new HashMap<>();
    private List<Long> childs = new ArrayList<>();

    public PersonTO(long id, String name, Gender gender, Map<Gender, Long> parents) {
        this(id, name, gender);
        this.parents = parents;
    }

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

    public Map<Gender, Long> getParents() {
        return parents;
    }

    public void setParents(Map<Gender, Long> parents) {
        this.parents = parents;
    }

    public List<Long> getChilds() {
        return childs;
    }

    public void setChilds(List<Long> childs) {
        this.childs = childs;
    }

    public boolean isGrandParent() {
        return ofNullable(parents).map(Map::isEmpty).orElse(true);
    }
}
