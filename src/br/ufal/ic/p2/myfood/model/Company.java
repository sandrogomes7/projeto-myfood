package br.ufal.ic.p2.myfood.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Company {
    private static int idCounter = 1;
    private int id;
    private String companyType;
    private int owner;
    private String name;
    private String address;

    private List<Product> productList;
    private List<Order> orderList;

    public Company() {
    }

    public Company(String companyType, int owner, String name, String address) {
        this.id = idCounter++;
        this.companyType = companyType;
        this.owner = owner;
        this.name = name;
        this.address = address;
        this.productList = new ArrayList<>();
        this.orderList = new ArrayList<>();
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Company.idCounter = idCounter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public void addOrder(Order order) {
        orderList.add(order);
    }

    public void addProducts(Product product) {
        this.productList.add(product);
    }
}

