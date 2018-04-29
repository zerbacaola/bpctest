package ustinov.sergey.bpctest;

import java.util.List;

public class PersonFilterTO {
    private String name;
    private Gender gender;
    private List<String> childrens;
    private String mother;
    private String father;
    private int page;
    private int rowsPerPage;

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

    public List<String> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<String> childrens) {
        this.childrens = childrens;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    @Override
    public String toString() {
        return "PersonFilterTO{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", childrens=" + childrens +
                ", mother='" + mother + '\'' +
                ", father='" + father + '\'' +
                ", page=" + page +
                ", rowsPerPage=" + rowsPerPage +
                '}';
    }
}
