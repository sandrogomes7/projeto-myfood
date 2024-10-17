package br.ufal.ic.p2.myfood.service;

import br.ufal.ic.p2.myfood.exception.*;
import br.ufal.ic.p2.myfood.exception.user.PlacaInvalidaException;
import br.ufal.ic.p2.myfood.exception.user.VeiculoInvalidoException;
import br.ufal.ic.p2.myfood.exception.UsuarioNaoEhEntregadorException;
import br.ufal.ic.p2.myfood.exception.user.*;
import br.ufal.ic.p2.myfood.model.CompanyOwner;
import br.ufal.ic.p2.myfood.model.DeliveryPerson;
import br.ufal.ic.p2.myfood.model.User;
import br.ufal.ic.p2.myfood.model.Customer;
import br.ufal.ic.p2.myfood.repository.Data;
import br.ufal.ic.p2.myfood.util.AttributeTranslation;

import java.util.Map;

public class UserService {
    private final Map<Integer, User> userList;

    private static final String OWNER_QUALIFICATION = "Company Owner";
    private static final String DELIVERY_QUALIFICATION = "Delivery Person";

    public UserService(Data data) {
        this.userList = data.getUserList();
    }

    public String getUserAttribute(int userId, String attribute) throws Exception {
        User user = userList.get(userId);
        if (user == null) throw new UsuarioNaoCadastradoException();
        String englishAttribute = AttributeTranslation.getEnglishAttribute(attribute);

        return switch (englishAttribute) {
            case "name" -> user.getName();
            case "password" -> user.getPassword();
            case "email" -> user.getEmail();
            case "address" -> user.getAddress();
            case "cpf" -> getCpf(user);
            case "vehicle" -> getVehicle(user);
            case "plate" -> getPlate(user);
            default -> throw new AtributoInvalidoException();
        };
    }

    public void createCustomerUser(String name, String email, String password, String address) throws Exception {
        validateUserFields(name, email, password, address);

        User user = new Customer(name, email, password, address);
        userList.put(user.getId(), user);
    }

    public void createOwnerUser(String name, String email, String password, String address, String cpf) throws Exception {
        if (cpf == null || cpf.length() != 14) throw new CpfInvalidoException();

        validateUserFields(name, email, password, address);

        User user = new CompanyOwner(name, email, password, address, cpf);
        userList.put(user.getId(), user);
    }

    public void createDeliveryPerson(String name, String email, String password, String address, String vehicle, String plate) throws Exception {
        if (vehicle == null || vehicle.isEmpty()) throw new VeiculoInvalidoException();
        if (plate == null || plate.isEmpty()) throw new PlacaInvalidaException();

        for (User user : userList.values()) {
            if (user.userQualification().equals(DELIVERY_QUALIFICATION)) {
                DeliveryPerson deliveryPerson = (DeliveryPerson) user;
                if (deliveryPerson.getPlate().equals(plate)) throw new PlacaInvalidaException();
            }
        }

        validateUserFields(name, email, password, address);

        User user = new DeliveryPerson(name, email, password, address, vehicle, plate);
        userList.put(user.getId(), user);
    }

    public int signIn(String email, String password) throws Exception {
        for (User user : userList.values()) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password))
                return user.getId();
        }
        throw new LoginOuSenhaInvalidosException();
    }

    public String getCompanies(int idDeliveryPerson) throws Exception {
        DeliveryPerson user = userList.values().stream()
                .filter(u -> u.userQualification().equals(DELIVERY_QUALIFICATION))
                .map(u -> (DeliveryPerson) u)
                .filter(u -> u.getId() == idDeliveryPerson)
                .findFirst()
                .orElseThrow(UsuarioNaoEhEntregadorException::new);

        return user.companiesInString();
    }

    private void validateUserFields(String name, String email, String password, String address) throws Exception {
        if (name == null || name.isEmpty()) throw new NomeInvalidoException();
        if (email == null || !email.contains("@")) throw new EmailInvalidoException();
        if (password == null || password.isEmpty()) throw new SenhaInvalidaException();
        if (address == null || address.isEmpty()) throw new EnderecoInvalidoException();
        if (userList.values().stream().anyMatch(user -> user.getEmail().equals(email)))
            throw new ContaComEmailJaExisteException();
    }

    private String getCpf(User user) throws AtributoInvalidoException {
        if (user.userQualification().equals(OWNER_QUALIFICATION)) return ((CompanyOwner) user).getCpf();
        else throw new AtributoInvalidoException();
    }

    private String getVehicle(User user) throws AtributoInvalidoException {
        if (user.userQualification().equals(DELIVERY_QUALIFICATION)) return ((DeliveryPerson) user).getVehicle();
        else throw new AtributoInvalidoException();
    }

    private String getPlate(User user) throws AtributoInvalidoException {
        if (user.userQualification().equals(DELIVERY_QUALIFICATION)) return ((DeliveryPerson) user).getPlate();
        else throw new AtributoInvalidoException();
    }

}
