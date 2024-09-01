package br.ufal.ic.p2.myfood.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private List<Order> orderList;

    public Customer() {
    }

    public Customer(String name, String email, String password, String address) {
        super(name, email, password, address);
        orderList = new ArrayList<>();
    }

    @Override
    public boolean canCreateCompany() {
        return false;
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

}