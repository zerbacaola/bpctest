package ustinov.sergey.bpctest;

public class PersonFilterTO {
    private String name;
    private String gender;
    private String child;
    private String mother;
    private String father;
    private int offset;
    private int limit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
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

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "PersonFilterTO{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", child='" + child + '\'' +
                ", mother='" + mother + '\'' +
                ", father='" + father + '\'' +
                ", offset=" + offset +
                ", limit=" + limit +
                '}';
    }
}
