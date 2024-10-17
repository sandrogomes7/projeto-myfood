package br.ufal.ic.p2.myfood.model;

import java.util.HashMap;
import java.util.Map;

public class Customer extends User {
    private Map<Integer, Order> orderList;

    public Customer() {
    }

    public Customer(String name, String email, String password, String address) {
        super(name, email, password, address);
        orderList = new HashMap<>();
    }

    @Override
    public String userQualification() {
        return "Customer";
    }

    public Map<Integer, Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(Map<Integer, Order> orderList) {
        this.orderList = orderList;
    }

    public void addOrder(Order order) {
        orderList.put(order.getIdOrder(), order);
    }

}