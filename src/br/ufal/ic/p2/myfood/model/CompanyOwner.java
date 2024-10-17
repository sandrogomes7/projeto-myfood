package br.ufal.ic.p2.myfood.model;

import java.util.HashMap;
import java.util.Map;

public class CompanyOwner extends User {
    private String cpf;
    private Map<Integer, Company> companyList;

    public CompanyOwner() {
    }

    public CompanyOwner(String name, String email, String password, String address, String cpf) {
        super(name, email, password, address);
        this.cpf = cpf;
        companyList = new HashMap<>();
    }

    @Override
    public String userQualification() {
        return "Company Owner";
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Map<Integer, Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(Map<Integer, Company> companyList) {
        this.companyList = companyList;
    }
}
