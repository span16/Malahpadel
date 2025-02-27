package models;

public class evenement {
    private int id;  // Attribut id de type int

    public evenement(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "evenement{" +
                "id=" + id +
                '}';
    }
}



