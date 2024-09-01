package br.ufal.ic.p2.myfood.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Order {
    private static int idCounter = 1;
    private int numberOrder;
    private int clientId;
    private int companyId;
    private String state;
    private float value;

    private List<Product> productList;

    private static final String ABERTO = "aberto";

    public Order() {
    }

    public Order(int clientId, int companyId) {
        this.numberOrder = idCounter++;
        this.clientId = clientId;
        this.companyId = companyId;
        this.state = ABERTO;
        this.value = 0;
        this.productList = new ArrayList<>();
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Order.idCounter = idCounter;
    }

    public int getNumberOrder() {
        return numberOrder;
    }

    public void setNumberOrder(int numberOrder) {
        this.numberOrder = numberOrder;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        value = 0;
        for (Product product : productList) {
            value += product.getValue();
        }
    }

    public void addProduct(Product product) {
        productList.add(product);
        value += product.getValue();
    }

    public String productString() {
        StringBuilder stringProduct = new StringBuilder("{[");
        for (Product product : productList) {
            stringProduct.append(product.getName()).append(", ");
        }
        if (stringProduct.length() > 2) {
            stringProduct = new StringBuilder(stringProduct.substring(0, stringProduct.length() - 2));
        }
        stringProduct.append("]}");
        return stringProduct.toString();
    }

    public void removeProduct(Product product) {
        for (Product productAux : productList) {
            if (productAux.equals(product)) {
                productList.remove(productAux);
                value -= productAux.getValue();
                return;
            }
        }
    }
}
