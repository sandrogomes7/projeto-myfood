package br.ufal.ic.p2.myfood.model;

public class Restaurant extends Company {
    private String cuisineType;

    public Restaurant() {
    }

    public Restaurant(String companyType, int owner, String name, String address, String cuisineType) {
        super(companyType, owner, name, address);
        this.cuisineType = cuisineType;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }
}
