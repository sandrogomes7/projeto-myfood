package br.ufal.ic.p2.myfood.service;

import br.ufal.ic.p2.myfood.exception.*;
import br.ufal.ic.p2.myfood.exception.user.*;
import br.ufal.ic.p2.myfood.model.CompanyOwner;
import br.ufal.ic.p2.myfood.model.User;
import br.ufal.ic.p2.myfood.model.Customer;
import br.ufal.ic.p2.myfood.repository.Data;

import java.util.Map;

public class UserService {
    private final Map<Integer, User> userList;

    public UserService(Data data) {
        this.userList = data.getUserList();
    }

    public String getUserAttribute(int userId, String attribute) throws Exception {
        User user = userList.get(userId);
        if (user != null) {
            switch (attribute) {
                case "nome" -> {
                    return user.getName();
                }
                case "senha" -> {
                    return user.getPassword();
                }
                case "email" -> {
                    return user.getEmail();
                }
                case "endereco" -> {
                    return user.getAddress();
                }
                case "cpf" -> {
                    if (user.canCreateCompany()) {
                        return ((CompanyOwner) user).getCpf();
                    } else {
                        throw new AtributoInvalidoExceptio();
                    }
                }
                default -> throw new AtributoInvalidoExceptio();
            }
        } else {
            throw new UsuarioNaoCadastradoException();
        }
    }

    public void createClientUser(String name, String email, String password, String address) throws Exception {
        validateUserFields(name, email, password, address);

        for (User user : userList.values()) {
            if (user.getEmail().equals(email)) {
                throw new ContaComEmailJaExisteException();
            }
        }

        User user = new Customer(name, email, password, address);
        userList.put(user.getId(), user);
    }

    public void createOwnerUser(String name, String email, String password, String address, String cpf) throws Exception {

        if (cpf == null || cpf.length() != 14) throw new CpfInvalidoException();

        validateUserFields(name, email, password, address);

        for (User user : userList.values()) {
            if (user.getEmail().equals(email)) throw new ContaComEmailJaExisteException();
        }

        User user = new CompanyOwner(name, email, password, address, cpf);
        userList.put(user.getId(), user);
    }

    public int signIn(String email, String password) throws Exception {
        for (User user : userList.values()) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user.getId();
            }
        }
        throw new LoginOuSenhaInvalidosException();
    }

    public void validateUserFields(String name, String email, String password, String address) throws Exception {

        if (name == null || name.isEmpty()) throw new NomeInvalidoException();

        if (email == null || !email.contains("@")) throw new EmailInvalidoException();

        if (password == null || password.isEmpty()) throw new SenhaInvalidaException();

        if (address == null || address.isEmpty()) throw new EnderecoInvalidoException();
    }
}
