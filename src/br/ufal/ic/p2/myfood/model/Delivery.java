package br.ufal.ic.p2.myfood.model;

public class Delivery {
    private static int idCounter = 1;
    private int id;
    private int idOrder;
    private int idDeliveryPerson;
    private String destination;

    public Delivery() {
    }

    public Delivery(String destination, int idDeliveryPerson, int idOrder) {
        this.id = idCounter++;
        this.destination = destination;
        this.idDeliveryPerson = idDeliveryPerson;
        this.idOrder = idOrder;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Delivery.idCounter = idCounter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getIdDeliveryPerson() {
        return idDeliveryPerson;
    }

    public void setIdDeliveryPerson(int idDeliveryPerson) {
        this.idDeliveryPerson = idDeliveryPerson;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
