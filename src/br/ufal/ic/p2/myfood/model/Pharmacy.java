package br.ufal.ic.p2.myfood.model;

public class Pharmacy extends Company{
    private Boolean open24Hours;
    private Integer numberOfEmployees;

    public Pharmacy(){
    }

    public Pharmacy(String companyType, int idOwner, String companyName, String address, Boolean open24Hours, Integer numberOfEmployees) {
        super(companyType, idOwner, companyName, address);
        this.open24Hours = open24Hours;
        this.numberOfEmployees = numberOfEmployees;
    }

    public Boolean getOpen24Hours() {
        return open24Hours;
    }

    public void setOpen24Hours(Boolean open24Hours) {
        this.open24Hours = open24Hours;
    }

    public Integer getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(Integer numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }
}
