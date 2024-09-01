package br.ufal.ic.p2.myfood.service;

import br.ufal.ic.p2.myfood.exception.*;
import br.ufal.ic.p2.myfood.exception.order.*;
import br.ufal.ic.p2.myfood.model.Company;
import br.ufal.ic.p2.myfood.model.Customer;
import br.ufal.ic.p2.myfood.model.Order;
import br.ufal.ic.p2.myfood.model.Product;
import br.ufal.ic.p2.myfood.model.User;
import br.ufal.ic.p2.myfood.repository.Data;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class OrderService {
    private final Map<Integer, Company> companyList;
    private final Map<Integer, User> userList;

    private static final String ABERTO = "aberto";
    private static final String PREPARANDO = "preparando";
    private static final String FECHADO = "fechado";

    public OrderService(Data data) {
        this.companyList = data.getCompanyList();
        this.userList = data.getUserList();
    }

    public int createOrder(int client, int company) throws Exception {
        User user = userList.get(client);
        if (user.canCreateCompany()) throw new DonoNaoPodeCriarPedidoException();

        Customer currentCustomer = (Customer) user;
        Company currentCompany = companyList.get(company);

        validateCompany(currentCompany);

        for (Order orderAux1 : currentCustomer.getOrderList()) {
            if (orderAux1.getCompanyId() == company) {
                for (Order orderAux2 : currentCompany.getOrderList()) {
                    if (orderAux2.getState().equals(ABERTO)) {
                        throw new NaoPermiteDoisPedidoAbertoException();
                    }
                }
            }
        }

        Order order = new Order(client, company);
        currentCompany.addOrder(order);
        currentCustomer.addOrder(order);

        return order.getNumberOrder();
    }

    public int getOrderNumber(int client, int company, int index) throws Exception {
        User user = userList.get(client);
        if (user.canCreateCompany()) throw new DonoNaoPodeCriarPedidoException();

        Customer currentCustomer = (Customer) user;
        Company currentCompany = companyList.get(company);

        validateCompany(currentCompany);

        List<Order> ordersUser = new ArrayList<>();
        for (Order order : currentCustomer.getOrderList()) {
            if (order.getCompanyId() == company) {
                ordersUser.add(order);
            }
        }

        if (ordersUser.isEmpty()) throw new PedidoNaoEncontradoException();

        if (ordersUser.size() <= index) throw new IndiceMaiorQueEsperadoExceptio();

        return ordersUser.get(index).getNumberOrder();
    }

    public void addProductToOrder(int numberOrder, int product) throws Exception {
        Order order = null;
        Company company = null;

        for (Company companyAux : companyList.values()) {
            order = companyAux.getOrderList().stream()
                    .filter(p -> p.getNumberOrder() == numberOrder)
                    .findFirst()
                    .orElse(null);

            if (order != null) {
                company = companyAux;
                break;
            }
        }

        if (order == null) throw new NaoExistePedidoAbertoException();

        if (order.getState().equals(PREPARANDO)) throw new NaoPermiteAdicionarProdutoException();

        if (!order.getState().equals(ABERTO)) throw new PedidoNaoEncontradoException();

        for (Product productAux : company.getProductList()) {
            if (productAux.getId() == product) {
                order.addProduct(productAux);
                return;
            }
        }

        throw new ProdutoNaoPertenceEmpresaException();
    }

    public String getOrderProductAttribute(int numberOrder, String attribute) throws Exception {
        if (attribute == null || attribute.isEmpty()) throw new AtributoInvalidoExceptio();

        Order order = companyList.values().stream()
                .flatMap(company -> company.getOrderList().stream())
                .filter(p -> p.getNumberOrder() == numberOrder)
                .findFirst()
                .orElse(null);

        if (order == null) throw new PedidoNaoEncontradoException();

        return switch (attribute) {
            case "estado" -> order.getState();
            case "valor" -> String.format("%.2f", order.getValue()).replace(",", ".");
            case "cliente" -> userList.get(order.getClientId()).getName();
            case "empresa" -> companyList.get(order.getCompanyId()).getName();
            case "produtos" -> order.productString();

            default -> throw new AtributoNaoExisteException();
        };
    }

    public void closeOrder(int numberOrder) throws Exception {
        Order companyOrder = companyList.values().stream()
                .flatMap(company -> company.getOrderList().stream())
                .filter(order -> order.getNumberOrder() == numberOrder)
                .findFirst()
                .orElse(null);

        Order userOrder = userList.values().stream()
                .filter(user -> !user.canCreateCompany())
                .map(user -> (Customer) user)
                .flatMap(user -> user.getOrderList().stream())
                .filter(order -> order.getNumberOrder() == numberOrder)
                .findFirst()
                .orElse(null);

        if (companyOrder == null || userOrder == null) throw new PedidoNaoEncontradoException();

        companyOrder.setState(PREPARANDO);
        userOrder.setState(PREPARANDO);
    }

    public void removeProductFromOrder(int numberOrder, String nameProduct) throws Exception {
        Order currentOrder = companyList.values().stream()
                .flatMap(company -> company.getOrderList().stream())
                .filter(order -> order.getNumberOrder() == numberOrder)
                .findFirst()
                .orElse(null);

        if (currentOrder == null) throw new PedidoNaoEncontradoException();

        if (nameProduct == null || nameProduct.isEmpty()) throw new ProdutoInvalidoException();

        if (currentOrder.getState().equals(PREPARANDO)) throw new NaoPermiteRemoverEmPedidoFechadoException();

        if (!currentOrder.getState().equals(ABERTO)) throw new PedidoNaoEncontradoException();

        for (Product product : currentOrder.getProductList()) {
            if (product.getName().equals(nameProduct)) {
                currentOrder.removeProduct(product);
                return;
            }
        }

        throw new ProdutoNaoEncontradoException();
    }

    public void validateCompany(Company company) throws Exception {
        if (company == null) throw new EmpresaNaoEncontradaException();
    }
}
