package br.ufal.ic.p2.myfood.service;

import br.ufal.ic.p2.myfood.exception.*;
import br.ufal.ic.p2.myfood.exception.company.*;
import br.ufal.ic.p2.myfood.model.*;
import br.ufal.ic.p2.myfood.repository.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompanyService {
    private final Map<Integer, Company> companyList;
    private final Map<Integer, User> userList;

    public CompanyService(Data data) {
        this.companyList = data.getCompanyList();
        this.userList = data.getUserList();
    }

    public int createCompany(String companyType, int idOwner, String companyName, String address, String cuisineType) throws Exception {
        if (!companyType.equals("restaurante")) throw new TipoEmpresaInvalidoException();

        if (companyName == null || companyName.isEmpty()) throw new NomeInvalidoException();
        if (address == null || address.isEmpty()) throw new EnderecoEmpresaInvalidoException();
        if (cuisineType == null || cuisineType.isEmpty()) throw new TipoCozinhaInvalidoException();

        for (Company company : companyList.values()) {
            if (company.getOwner() != idOwner && company.getName().equalsIgnoreCase(companyName)) {
                throw new EmpresaComNomeJaExisteException();
            }
            if (company.getOwner() == idOwner && company.getAddress().equals(address) && company.getName().equals(companyName)) {
                throw new EmpresaMesmoNomeELocalException();
            }
        }

        User user = userList.get(idOwner);
        if (user == null) throw new UsuarioNaoCadastradoException();
        if (!user.canCreateCompany()) throw new UsuarioNaoPodeCriarEmpresaException();

        int id = -1;
        if (companyType.equals("restaurante")) {
            Restaurant restaurant = new Restaurant(companyType, idOwner, companyName, address, cuisineType);
            id = restaurant.getId();
            companyList.put(restaurant.getId(), restaurant);
        }

        return id;
    }

    public String listCompaniesByOwner(int idOwner) throws Exception {
        User user = userList.get(idOwner);
        if (user == null) throw new UsuarioNaoCadastradoException();
        if (!user.canCreateCompany()) throw new UsuarioNaoPodeCriarEmpresaException();

        StringBuilder stringCompanyUser = new StringBuilder("{[");
        for (Company company : companyList.values()) {
            if (company.getOwner() == idOwner) {
                String name = company.getName();
                String address = company.getAddress();
                if (name != null && !name.isEmpty() && address != null && !address.isEmpty()) {
                    stringCompanyUser.append("[");
                    stringCompanyUser.append(name).append(", ").append(address);
                    stringCompanyUser.append("], ");
                }
            }
        }
        if (stringCompanyUser.length() > 2) {
            stringCompanyUser.setLength(stringCompanyUser.length() - 2);
        }
        stringCompanyUser.append("]}");

        return stringCompanyUser.toString();
    }

    public String getCompanyAttribute(int idCompany, String attribute) throws Exception {
        if (attribute == null) throw new AtributoInvalidoExceptio();

        Company company = companyList.get(idCompany);

        if (company == null) throw new EmpresaNaoCadastradaException();

        return switch (attribute) {
            case "nome" -> company.getName();
            case "dono" -> userList.get(company.getOwner()).getName();
            case "endereco" -> company.getAddress();
            case "tipoCozinha" -> {
                if (company.getCompanyType().equals("restaurante")) {
                    yield ((Restaurant) company).getCuisineType();
                } else {
                    throw new AtributoInvalidoExceptio();
                }
            }
            default -> throw new AtributoInvalidoExceptio();
        };
    }

    public int getIdCompany(int idOwner, String companyName, int index) throws Exception {
        if (companyName == null || companyName.isEmpty()) throw new NomeInvalidoException();

        if (index < 0) throw new IndiceInvalidoException();

        User user = userList.get(idOwner);
        if (user == null) throw new UsuarioNaoCadastradoException();

        List<Company> companiesOwner = new ArrayList<>();
        for (Company company : companyList.values()) {
            if (company.getOwner() == idOwner) {
                if (company.getName().equals(companyName)) {
                    companiesOwner.add(company);
                }
            }
        }
        if (companiesOwner.isEmpty()) throw new NomeEmpresaNaoExisteException();

        if (companiesOwner.size() <= index) throw new IndiceMaiorQueEsperadoExceptio();

        return companiesOwner.get(index).getId();
    }
}
