package br.ufal.ic.p2.myfood.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
    private static int idCounter = 1;
    private int idOrder;
    private int idCustomer;
    private int idCompany;
    private String state;
    private float value;

    private List<Product> productList;

    private static final String OPEN = "open";

    public Order() {
    }

    public Order(int idCustomer, int idCompany) {
        this.idOrder = idCounter++;
        this.idCustomer = idCustomer;
        this.idCompany = idCompany;
        this.state = OPEN;
        this.value = 0;
        this.productList = new ArrayList<>();
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Order.idCounter = idCounter;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public int getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(int idCompany) {
        this.idCompany = idCompany;
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
    }

    public void addProduct(Product product) {
        productList.add(product);
        value += product.getValue();
    }

    public String productInString() {
        return productList.stream()
                .sorted(Comparator.comparingInt(Product::getId))
                .map(Product::getName)
                .collect(Collectors.joining(", ", "{[", "]}"));
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
