package br.ufal.ic.p2.myfood.controller;

import br.ufal.ic.p2.myfood.repository.Data;
import br.ufal.ic.p2.myfood.service.*;

public class MyFoodSystem {
    private final Data database = Data.getInstance();

    private final UserService userService = new UserService(database);
    private final CompanyService companyService = new CompanyService(database);
    private final ProductService productService = new ProductService(database);
    private final OrderService orderService = new OrderService(database);
    private final DeliveryService deliveryService = new DeliveryService(database);

    public MyFoodSystem() throws Exception {
    }

    public void resetSystem() throws Exception {
        database.eraseData();
        database.removeAllCompanies();
        database.removeAllUsers();
    }

    public String getUserAttribute(int id, String attribute) throws Exception {
        return userService.getUserAttribute(id, attribute);
    }

    public void createUser(String name, String email, String password, String address) throws Exception {
        userService.createCustomerUser(name, email, password, address);
    }

    public void createUser(String name, String email, String password, String address, String cpf) throws Exception {
        userService.createOwnerUser(name, email, password, address, cpf);
    }

    public void createUser(String name, String email, String password, String address, String vehicle, String licensePlate) throws Exception {
        userService.createDeliveryPerson(name, email, password, address, vehicle, licensePlate);
    }

    public int login(String email, String password) throws Exception {
        return userService.signIn(email, password);
    }

    public void endSystem() throws Exception {
        database.save();
    }

    public int createCompany(String companyType, int ownerId, String companyName, String address, String kitchenType) throws Exception {
        return companyService.createCompanyRestaurant(companyType, ownerId, companyName, address, kitchenType);
    }

    public String getUserCompanies(int ownerId) throws Exception {
        return companyService.listCompaniesByOwner(ownerId);
    }

    public String getCompanyAttribute(int ownerId, String searchedAttribute) throws Exception {
        return companyService.getCompanyAttribute(ownerId, searchedAttribute);
    }

    public int getCompanyId(int companyId, String companyName, int index) throws Exception {
        return companyService.getIdCompany(companyId, companyName, index);
    }

    public int createProduct(int company, String name, float price, String category) throws Exception {
        return productService.createProduct(company, name, price, category);
    }

    public void editProduct(int product, String name, float price, String category) throws Exception {
        productService.editProduct(product, name, price, category);
    }

    public String getProduct(String name, int company, String attribute) throws Exception {
        return productService.getProductAttribute(name, company, attribute);
    }

    public String listProducts(int company) throws Exception {
        return productService.listProductsOfCompany(company);
    }

    public int createOrder(int client, int company) throws Exception {
        return orderService.createOrder(client, company);
    }

    public void addProduct(int orderNumber, int productNumber) throws Exception {
        orderService.addProductToOrder(orderNumber, productNumber);
    }

    public String getOrders(int number, String attribute) throws Exception {
        return orderService.getOrderProductAttribute(number, attribute);
    }

    public void closeOrder(int number) throws Exception {
        orderService.closeOrder(number);
    }

    public void removeProduct(int order, String product) throws Exception {
        orderService.removeProductFromOrder(order, product);
    }

    public int getOrderNumber(int client, int company, int index) throws Exception {
        return orderService.getOrderNumber(client, company, index);
    }

    public int createCompany(String companyType, int ownerId, String companyName, String address, String open, String close, String marketType) throws Exception {
        return companyService.createCompanyMarket(companyType, ownerId, companyName, address, open, close, marketType);
    }

    public void changeMarketHours(int marketId, String open, String close) throws Exception {
        companyService.changeMarketHours(marketId, open, close);
    }

    public int createCompany(String companyType, int ownerId, String companyName, String address, Boolean open24Hours, int employeeCount) throws Exception {
        return companyService.createCompanyPharmacy(companyType, ownerId, companyName, address, open24Hours, employeeCount);
    }

    public void registerDeliveryUser(int company, int deliveryUser) throws Exception {
        companyService.registerDeliveryPerson(company, deliveryUser);
    }

    public String getDeliveryPersons(int company) throws Exception {
        return companyService.getDeliveryPersons(company);
    }

    public String getCompanies(int deliveryPerson) throws Exception {
        return userService.getCompanies(deliveryPerson);
    }

    public void releaseOrder(int order) throws Exception {
        orderService.releaseOrder(order);
    }

    public int getOrder(int deliveryPerson) throws Exception {
        return orderService.getOrder(deliveryPerson);
    }

    public int createDelivery(int order, int deliveryPerson, String destination) throws Exception {
        return deliveryService.createDelivery(order, deliveryPerson, destination);
    }

    public String getDelivery(int deliveryId, String attribute) throws Exception {
        return deliveryService.getDelivery(deliveryId, attribute);
    }

    public int getIdDelivery(int order) throws Exception {
        return deliveryService.getIdDelivery(order);
    }

    public void deliver(int idDelivery) throws Exception {
        deliveryService.deliver(idDelivery);
    }
}
