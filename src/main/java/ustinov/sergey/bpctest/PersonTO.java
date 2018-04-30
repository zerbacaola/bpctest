package ustinov.sergey.bpctest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonTO {

    private long id;
    private String name;
    private Gender gender;
    private Map<Gender, Long> parents = new HashMap<>();
    private List<Long> childs = new ArrayList<>();

    public PersonTO(long id, String name, Gender gender, Map<Gender, Long> parents, List<Long> childs) {
        this(id, name, gender);
        this.parents = parents;
        this.childs = childs;
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
}
