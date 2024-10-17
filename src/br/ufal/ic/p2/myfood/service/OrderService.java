package br.ufal.ic.p2.myfood.service;

import br.ufal.ic.p2.myfood.exception.*;
import br.ufal.ic.p2.myfood.exception.order.*;
import br.ufal.ic.p2.myfood.model.*;
import br.ufal.ic.p2.myfood.repository.Data;
import br.ufal.ic.p2.myfood.util.AttributeTranslation;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class OrderService {
    private final Map<Integer, Company> companyList;
    private final Map<Integer, User> userList;

    private static final String OPEN = "open";
    private static final String PREPARING = "preparing";
    private static final String READY = "ready";
    private static final String OWNER_QUALIFICATION = "Company Owner";
    private static final String DELIVERY_QUALIFICATION = "Delivery Person";
    private static final String PHARMACY = "pharmacy";

    public OrderService(Data data) {
        this.companyList = data.getCompanyList();
        this.userList = data.getUserList();
    }

    public int createOrder(int idCustomer, int idCompany) throws Exception {
        User user = userList.get(idCustomer);
        if (user.userQualification().equals(OWNER_QUALIFICATION)) throw new DonoNaoPodeCriarPedidoException();

        Customer currentCustomer = (Customer) user;
        Company currentCompany = companyList.get(idCompany);

        validateCompany(currentCompany);

        boolean hasOpenOrder = currentCustomer.getOrderList().values().stream()
                .anyMatch(order -> order.getIdCompany() == idCompany && order.getState().equals(OPEN));

        if (hasOpenOrder) throw new NaoPermiteDoisPedidoAbertoException();

        Order order = new Order(idCustomer, idCompany);
        currentCompany.addOrder(order);
        currentCustomer.addOrder(order);

        return order.getIdOrder();
    }

    public int getOrderNumber(int idCustomer, int idCompany, int index) throws Exception {
        User user = userList.get(idCustomer);
        if (user.userQualification().equals(OWNER_QUALIFICATION)) throw new DonoNaoPodeCriarPedidoException();

        Customer currentCustomer = (Customer) user;
        Company currentCompany = companyList.get(idCompany);

        validateCompany(currentCompany);

        List<Order> ordersUser = currentCustomer.getOrderList().values().stream()
                .filter(order -> order.getIdCompany() == idCompany)
                .toList();

        if (ordersUser.isEmpty()) throw new PedidoNaoEncontradoException();
        if (ordersUser.size() <= index) throw new IndiceMaiorQueEsperadoException();

        return ordersUser.get(index).getIdOrder();
    }

    public void addProductToOrder(int idOrder, int idProduct) throws Exception {
        Order order = companyList.values().stream()
                .flatMap(company -> company.getOrderList().values().stream())
                .filter(p -> p.getIdOrder() == idOrder)
                .findFirst()
                .orElseThrow(NaoExistePedidoAbertoException::new);

        if (order.getState().equals(PREPARING)) throw new NaoPermiteAdicionarProdutoException();
        if (!order.getState().equals(OPEN)) throw new PedidoNaoEncontradoException();

        Company company = companyList.values().stream()
                .filter(c -> c.getOrderList().containsValue(order))
                .findFirst()
                .orElseThrow(PedidoNaoEncontradoException::new);

        Product productAux = company.getProductList().values().stream()
                .filter(p -> p.getId() == idProduct)
                .findFirst()
                .orElseThrow(ProdutoNaoPertenceEmpresaException::new);

        order.addProduct(productAux);
    }

    public String getOrderProductAttribute(int idOrder, String attribute) throws Exception {
        if (attribute == null || attribute.isEmpty()) throw new AtributoInvalidoException();

        Order companyOrder = findOrder(idOrder, true);

        String englishAttribute = AttributeTranslation.getEnglishAttribute(attribute);
        return switch (englishAttribute) {
            case "state" -> AttributeTranslation.getPortugueseAttribute(companyOrder.getState());
            case "value" -> String.format("%.2f", companyOrder.getValue()).replace(",", ".");
            case "customer" -> userList.get(companyOrder.getIdCustomer()).getName();
            case "company" -> companyList.get(companyOrder.getIdCompany()).getName();
            case "products" -> companyOrder.productInString();
            default -> throw new AtributoNaoExisteException();
        };
    }

    public void closeOrder(int idOrder) throws Exception {
        Order companyOrder = findOrder(idOrder, true);
        Order userOrder = findOrder(idOrder, false);

        companyOrder.setState(PREPARING);
        userOrder.setState(PREPARING);
    }

    public void removeProductFromOrder(int idOrder, String nameProduct) throws Exception {
        if (nameProduct == null || nameProduct.isEmpty()) throw new ProdutoInvalidoException();
        Order companyOrder = findOrder(idOrder, true);

        if (companyOrder.getState().equals(PREPARING)) throw new NaoPermiteRemoverEmPedidoFechadoException();
        if (!companyOrder.getState().equals(OPEN)) throw new PedidoNaoEncontradoException();

        Product productToRemove = companyOrder.getProductList().stream()
                .filter(product -> product.getName().equals(nameProduct))
                .findFirst()
                .orElseThrow(ProdutoNaoEncontradoException::new);

        companyOrder.removeProduct(productToRemove);
    }

    public void releaseOrder(int idOrder) throws Exception {
        Order companyOrder = findOrder(idOrder, true);
        Order userOrder = findOrder(idOrder, false);

        if (companyOrder.getState().equals(READY) || userOrder.getState().equals(READY))
            throw new PedidoJaLiberadoException();
        if (!companyOrder.getState().equals(PREPARING) || !userOrder.getState().equals(PREPARING))
            throw new NaoEhPossivelLiberarException();

        companyOrder.setState(READY);
        userOrder.setState(READY);
    }

    public int getOrder(int idDeliveryPerson) throws Exception {
        DeliveryPerson user = userList.values().stream()
                .filter(u -> u.userQualification().equals(DELIVERY_QUALIFICATION))
                .map(u -> (DeliveryPerson) u)
                .filter(u -> u.getId() == idDeliveryPerson)
                .findFirst()
                .orElseThrow(UsuarioNaoEhEntregadorException::new);

        List<Order> orders = new ArrayList<>();
        for (Company company : user.getCompanyList()) {
            for (Order order : company.getOrderList().values()) {
                if (company.getCompanyType().equals(PHARMACY) && order.getState().equals(READY)) {
                    orders.add(order);
                }
            }
        }

        for (Company company : user.getCompanyList()) {
            for (Order order : company.getOrderList().values()) {
                if (!company.getCompanyType().equals(PHARMACY) && order.getState().equals(READY)) {
                    orders.add(order);
                }
            }
        }

        if (user.getCompanyList().isEmpty()) throw new EntregadorNaoEstarEmNenhumaEmpresaException();
        if (orders.isEmpty()) throw new NaoExistePedidoParaEntregaException();

        return orders.getFirst().getIdOrder();
    }

    private void validateCompany(Company company) throws Exception {
        if (company == null) throw new EmpresaNaoEncontradaException();
    }

    private Order findOrder(int idOrder, boolean isCompanyOrder) throws Exception {
        if (isCompanyOrder) {
           return userList.values().stream()
                    .filter(user -> !user.userQualification().equals(OWNER_QUALIFICATION))
                    .map(user -> (Customer) user)
                    .flatMap(user -> user.getOrderList().values().stream())
                    .filter(o -> o.getIdOrder() == idOrder)
                    .findFirst()
                    .orElseThrow(PedidoNaoEncontradoException::new);
        } else {
            return companyList.values().stream()
                    .flatMap(company -> company.getOrderList().values().stream())
                    .filter(o -> o.getIdOrder() == idOrder)
                    .findFirst()
                    .orElseThrow(PedidoNaoEncontradoException::new);
        }
    }
}
