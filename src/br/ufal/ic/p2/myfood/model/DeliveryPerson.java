package br.ufal.ic.p2.myfood.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DeliveryPerson extends User {
    private String vehicle;
    private String plate;
    private boolean isBeingDelivered;
    private List<Company> companyList;

    public DeliveryPerson() {}

    public DeliveryPerson(String name, String email, String password, String address, String vehicle, String plate) {
        super(name, email, password, address);
        this.vehicle = vehicle;
        this.plate = plate;
        this.isBeingDelivered = false;
        this.companyList = new ArrayList<>();
    }

    @Override
    public String userQualification() {
        return "Delivery Person";
    }

    // Getters and Setters
    public List<Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }

    public boolean isBeingDelivered() {
        return isBeingDelivered;
    }

    public void setBeingDelivered(boolean beingDelivered) {
        isBeingDelivered = beingDelivered;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public void toggleIsBeingDelivered() {
        isBeingDelivered = !isBeingDelivered;
    }

    public void addCompany(Company company) {
        companyList.add(company);
    }

    public String companiesInString() {
        return companyList.stream()
                .sorted(Comparator.comparing(Company::getId))
                .map(company -> String.format("[%s, %s]", company.getName(), company.getAddress()))
                .collect(Collectors.joining(", ", "{[", "]}"));
    }

}
