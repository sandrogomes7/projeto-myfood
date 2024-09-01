package br.ufal.ic.p2.myfood.repository;

import br.ufal.ic.p2.myfood.model.Company;
import br.ufal.ic.p2.myfood.model.User;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private static volatile Data instance;

    public Map<Integer, User> userList = new HashMap<>();
    public Map<Integer, Company> companyList = new HashMap<>();

    public Data() {
    }

    public static synchronized Data getInstance() throws Exception {
        if (instance == null) {
            instance = Persistence.loadData();
            if (instance == null) instance = new Data();
        }
        return instance;
    }

    public void save() throws Exception {
        Persistence.saveData(this);
    }

    public void eraseData() throws Exception {
        Persistence.eraseData();
    }

    public void removeAllUsers() {
        userList.clear();
    }

    public void removeAllCompanies() {
        companyList.clear();
    }

    public static void setInstance(Data instance) {
        Data.instance = instance;
    }

    public Map<Integer, User> getUserList() {
        return userList;
    }

    public void setUserList(Map<Integer, User> userList) {
        this.userList = userList;
    }

    public Map<Integer, Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(Map<Integer, Company> companyList) {
        this.companyList = companyList;
    }
}
