package br.ufal.ic.p2.myfood.service;

import br.ufal.ic.p2.myfood.exception.AtributoInvalidoException;
import br.ufal.ic.p2.myfood.exception.AtributoNaoExisteException;
import br.ufal.ic.p2.myfood.exception.UsuarioNaoEhUmEntregadorException;
import br.ufal.ic.p2.myfood.exception.delivery.*;
import br.ufal.ic.p2.myfood.exception.PedidoNaoEncontradoException;
import br.ufal.ic.p2.myfood.model.*;
import br.ufal.ic.p2.myfood.repository.Data;
import br.ufal.ic.p2.myfood.util.AttributeTranslation;

import java.util.Map;

public class DeliveryService {
    private final Map<Integer, Company> companyList;
    private final Map<Integer, User> userList;

    private static final String READY = "ready";
    private static final String DELIVERED = "delivered";
    private static final String DELIVERING = "delivering";
    private static final String DELIVERY_QUALIFICATION = "Delivery Person";
    private static final String CUSTOMER_QUALIFICATION = "Customer";

    public DeliveryService(Data data) {
        this.companyList = data.getCompanyList();
        this.userList = data.getUserList();
    }

    public int createDelivery(int idOrder, int idDeliveryPerson, String destination) throws Exception {
        Order order = companyList.values().stream()
                .flatMap(c -> c.getOrderList().values().stream())
                .filter(o -> o.getIdOrder() == idOrder)
                .filter(o -> o.getState().equals(READY))
                .findFirst()
                .orElseThrow(PedidoNaoEstaProntoParaEntregaException::new);

        DeliveryPerson user = userList.values().stream()
                .filter(u -> u.userQualification().equals(DELIVERY_QUALIFICATION))
                .map(u -> (DeliveryPerson) u)
                .filter(u -> u.getId() == idDeliveryPerson)
                .findFirst()
                .orElseThrow(NaoEhUmEntregadorValidoException::new);

        if (user.isBeingDelivered()) throw new EntregadorAindaEmEntregaException();

        if (destination == null || destination.isEmpty()) {
            destination = userList.values().stream()
                    .filter(u -> u.userQualification().equals(CUSTOMER_QUALIFICATION))
                    .map(u -> (Customer) u)
                    .filter(u -> u.getOrderList().values().stream()
                            .anyMatch(o -> o.getIdOrder() == idOrder))
                    .map(Customer::getAddress)
                    .findFirst()
                    .orElseThrow(DestinoInvalidoException::new);
        }

        Delivery delivery = new Delivery(destination, idDeliveryPerson, idOrder);
        Company company = companyList.get(order.getIdCompany());
        company.addDelivery(delivery);

        user.toggleIsBeingDelivered();
        order.setState(DELIVERING);

        return delivery.getId();
    }

    public String getDelivery(int idDeliver, String attribute) throws Exception {
        if (attribute == null || attribute.isEmpty())  throw new AtributoInvalidoException();

        Delivery delivery = companyList.values().stream()
                .flatMap(c -> c.getDeliveryList().values().stream())
                .filter(d -> d.getId() == idDeliver)
                .findFirst()
                .orElseThrow(EntregaNaoEncontradaException::new);

        String englishAttribute = AttributeTranslation.getEnglishAttribute(attribute);
        return switch (englishAttribute) {
            case "customer" -> getCustomerName(delivery);
            case "company" -> getCompanyName(delivery);
            case "order" -> String.valueOf(delivery.getIdOrder());
            case "deliveryPerson" -> getDeliveryPersonName(delivery);
            case "destination" -> delivery.getDestination();
            case "products" -> getProducts(delivery);
            default -> throw new AtributoNaoExisteException();
        };
    }

    public int getIdDelivery(int idOrder) throws Exception {
        return companyList.values().stream()
                .flatMap(c -> c.getDeliveryList().values().stream())
                .filter(d -> d.getIdOrder() == idOrder)
                .map(Delivery::getId)
                .findFirst()
                .orElseThrow(NaoExisteEntregaComEsseIdException::new);
    }

    public void deliver(int idDeliver) throws Exception {
        Delivery delivery = companyList.values().stream()
                .flatMap(c -> c.getDeliveryList().values().stream())
                .filter(d -> d.getId() == idDeliver)
                .findFirst()
                .orElseThrow(NaoExisteNadaParaEntregarExeption::new);

        Order order = companyList.values().stream()
                .flatMap(c -> c.getOrderList().values().stream())
                .filter(o -> o.getIdOrder() == delivery.getIdOrder())
                .findFirst()
                .orElseThrow(PedidoNaoEncontradoException::new);

        order.setState(DELIVERED);

        DeliveryPerson user = userList.values().stream()
                .filter(u -> u.userQualification().equals(DELIVERY_QUALIFICATION))
                .map(u -> (DeliveryPerson) u)
                .filter(u -> u.getId() == delivery.getIdDeliveryPerson())
                .findFirst()
                .orElseThrow(UsuarioNaoEhUmEntregadorException::new);

        user.toggleIsBeingDelivered();
    }

    private String getCustomerName(Delivery delivery) throws AtributoNaoExisteException {
        return userList.values().stream()
                .filter(u -> u.userQualification().equals(CUSTOMER_QUALIFICATION))
                .map(u -> (Customer) u)
                .filter(u -> u.getOrderList().values().stream()
                        .anyMatch(o -> o.getIdOrder() == delivery.getIdOrder()))
                .map(Customer::getName)
                .findFirst()
                .orElseThrow(AtributoNaoExisteException::new);
    }

    private String getCompanyName(Delivery delivery) throws AtributoNaoExisteException {
        return companyList.values().stream()
                .filter(c -> c.getOrderList().values().stream()
                        .anyMatch(o -> o.getIdOrder() == delivery.getIdOrder()))
                .map(Company::getName)
                .findFirst()
                .orElseThrow(AtributoNaoExisteException::new);
    }

    private String getDeliveryPersonName(Delivery delivery) throws AtributoNaoExisteException {
        return userList.values().stream()
                .filter(u -> u.userQualification().equals(DELIVERY_QUALIFICATION))
                .map(u -> (DeliveryPerson) u)
                .filter(u -> u.getId() == delivery.getIdDeliveryPerson())
                .map(DeliveryPerson::getName)
                .findFirst()
                .orElseThrow(AtributoNaoExisteException::new);
    }

    private String getProducts(Delivery delivery) throws AtributoNaoExisteException {
        return companyList.values().stream()
                .flatMap(c -> c.getOrderList().values().stream())
                .filter(o -> o.getIdOrder() == delivery.getIdOrder())
                .map(Order::productInString)
                .findFirst()
                .orElseThrow(AtributoNaoExisteException::new);
    }
}
