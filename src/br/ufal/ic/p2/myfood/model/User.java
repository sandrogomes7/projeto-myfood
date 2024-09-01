package br.ufal.ic.p2.myfood.model;

public abstract class User {
    private static int idCounter = 1;
    private int id;
    private String name;
    private String email;
    private String password;
    private String address;

    public User() {
    }

    public User(String name, String email, String password, String address) {
        this.id = idCounter++;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public abstract boolean canCreateCompany();

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        User.idCounter = idCounter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
