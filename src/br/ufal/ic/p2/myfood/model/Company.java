package br.ufal.ic.p2.myfood.model;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Company {
    private static int idCounter = 1;
    private int id;
    private String companyType;
    private int idOwner;
    private String name;
    private String address;

    private Map<Integer, Product> productList;
    private Map<Integer, Order> orderList;
    private Map<Integer, DeliveryPerson> deliveryPersonList;
    private Map<Integer, Delivery> deliveryList;

    public Company() {
    }

    public Company(String companyType, int idOwner, String name, String address) {
        this.id = idCounter++;
        this.companyType = companyType;
        this.idOwner = idOwner;
        this.name = name;
        this.address = address;
        this.productList = new HashMap<>();
        this.orderList = new HashMap<>();
        this.deliveryPersonList = new HashMap<>();
        this.deliveryList = new HashMap<>();
    }

    // Getters and Setters
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

    public int getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
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

    public Map<Integer, Product> getProductList() {
        return productList;
    }

    public void setProductList(Map<Integer, Product> productList) {
        this.productList = productList;
    }

    public Map<Integer, Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(Map<Integer, Order> orderList) {
        this.orderList = orderList;
    }

    public Map<Integer, DeliveryPerson> getDeliveryPersonList() {
        return deliveryPersonList;
    }

    public void setDeliveryPersonList(Map<Integer, DeliveryPerson> deliveryPersonList) {
        this.deliveryPersonList = deliveryPersonList;
    }

    public Map<Integer, Delivery> getDeliveryList() {
        return deliveryList;
    }

    public void setDeliveryList(Map<Integer, Delivery> deliveryList) {
        this.deliveryList = deliveryList;
    }

    public void addOrder(Order order) {
        orderList.put(order.getIdOrder(), order);
    }

    public void addProducts(Product product) {
        this.productList.put(product.getId(), product);
    }

    public void addDeliveryPerson(DeliveryPerson deliveryPerson) {
        this.deliveryPersonList.put(deliveryPerson.getId(), deliveryPerson);
    }

    public void addDelivery(Delivery delivery) {
        this.deliveryList.put(delivery.getId(), delivery);
    }

    public String productsInString() {
        return productList.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getValue().getName())
                .collect(Collectors.joining(", ", "{[", "]}"));
    }

    public String deliveriesPersonInString() {
        return deliveryPersonList.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getValue().getEmail())
                .collect(Collectors.joining(", ", "{[", "]}"));
    }

}

