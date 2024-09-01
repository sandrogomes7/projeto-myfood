package br.ufal.ic.p2.myfood.controller;

import br.ufal.ic.p2.myfood.repository.Data;
import br.ufal.ic.p2.myfood.service.CompanyService;
import br.ufal.ic.p2.myfood.service.OrderService;
import br.ufal.ic.p2.myfood.service.ProductService;
import br.ufal.ic.p2.myfood.service.UserService;

public class MyFoodSystem {
    private final Data database = Data.getInstance();

    private final UserService userService = new UserService(database);
    private final CompanyService companyService = new CompanyService(database);
    private final ProductService productService = new ProductService(database);
    private final OrderService orderService = new OrderService(database);

    public MyFoodSystem() throws Exception {
    }

    public void resetSystem() throws Exception {
        database.eraseData();
        database.removeAllCompanies();
        database.removeAllUsers();
    }

    public String getUserAttribute(int id, String atributo) throws Exception {
        return userService.getUserAttribute(id, atributo);
    }

    public void createUser(String nome, String email, String senha, String endereco) throws Exception {
        userService.createClientUser(nome, email, senha, endereco);
    }

    public void createUser(String nome, String email, String senha, String endereco, String cpf) throws Exception {
        userService.createOwnerUser(nome, email, senha, endereco, cpf);
    }

    public int login(String email, String senha) throws Exception {
        return userService.signIn(email, senha);
    }

    public void endSystem() throws Exception {
        database.save();
    }

    public int createCompany(String tipoEmpresa, int idDono, String nomeEmpresa, String endereco, String tipoCozinha) throws Exception {
        return companyService.createCompany(tipoEmpresa, idDono, nomeEmpresa, endereco, tipoCozinha);
    }

    public String getUserCompanies(int idDono) throws Exception {
        return companyService.listCompaniesByOwner(idDono);
    }

    public String getCompanyAttribute(int idDono, String atributoBuscado) throws Exception {
        return companyService.getCompanyAttribute(idDono, atributoBuscado);
    }

    public int getCompanyId(int idEmpresa, String nomeEmpresa, int indice) throws Exception {
        return companyService.getIdCompany(idEmpresa, nomeEmpresa, indice);
    }

    public int createProduct(int empresa, String nome, float valor, String categoria) throws Exception {
        return productService.createProduct(empresa, nome, valor, categoria);
    }

    public void editProduct(int produto, String nome, float valor, String categoria) throws Exception {
        productService.editProduct(produto, nome, valor, categoria);
    }

    public String getProduct(String nome, int empresa, String atributo) throws Exception {
        return productService.getProductAttribute(nome, empresa, atributo);
    }

    public String listProducts(int empresa) throws Exception {
        return productService.listProductsOfCompany(empresa);
    }

    public int createOrder(int cliente, int empresa) throws Exception {
        return orderService.createOrder(cliente, empresa);
    }

    public void addProduct(int numeroPedido, int numeroProduto) throws Exception {
        orderService.addProductToOrder(numeroPedido, numeroProduto);
    }

    public String getOrders(int numero, String atributo) throws Exception {
        return orderService.getOrderProductAttribute(numero, atributo);
    }

    public void closeOrder(int numero) throws Exception {
        orderService.closeOrder(numero);
    }

    public void removeProduct(int pedido, String produto) throws Exception {
        orderService.removeProductFromOrder(pedido, produto);
    }

    public int getOrderNumber(int cliente, int empresa, int indice) throws Exception {
        return orderService.getOrderNumber(cliente, empresa, indice);
    }
}
