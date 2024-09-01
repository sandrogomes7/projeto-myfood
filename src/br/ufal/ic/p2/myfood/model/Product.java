package br.ufal.ic.p2.myfood.model;


public class Product {
    private static int idCounter = 1;
    private int id;
    private int companyId;
    private String name;
    private float value;
    private String category;

    public Product() {
    }

    public Product(int companyId, String name, float value, String category) {
        this.id = idCounter++;
        this.companyId = companyId;
        this.name = name;
        this.value = value;
        this.category = category;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Product.idCounter = idCounter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
