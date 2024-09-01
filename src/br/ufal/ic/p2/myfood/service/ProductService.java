package br.ufal.ic.p2.myfood.service;

import br.ufal.ic.p2.myfood.exception.*;
import br.ufal.ic.p2.myfood.exception.product.*;
import br.ufal.ic.p2.myfood.model.Company;
import br.ufal.ic.p2.myfood.model.Product;
import br.ufal.ic.p2.myfood.repository.Data;

import java.util.Map;

public class ProductService {

    private final Map<Integer, Company> companyList;

    public ProductService(Data data) {
        this.companyList = data.getCompanyList();
    }

    public int createProduct(int idCompany, String nameProduct, float value, String category) throws Exception {
        validateProductFields(nameProduct, value, category);

        Company currentCompany = companyList.get(idCompany);
        validateCompany(currentCompany);

        for (Product product : currentCompany.getProductList()) {
            if (product.getName().equals(nameProduct)) {
                throw new ProdutoComNomeJaExisteException();
            }
        }

        Product product = new Product(idCompany, nameProduct, value, category);
        currentCompany.addProducts(product);

        return product.getId();
    }

    public void editProduct(int idProduct, String nameProduct, float value, String category) throws Exception {
        validateProductFields(nameProduct, value, category);

        Product editableProduct = null;
        for (Company company : companyList.values()) {
            for (Product currentProduct : company.getProductList()) {
                if (currentProduct.getId() == idProduct) {
                    editableProduct = currentProduct;
                }
            }
        }

        if (editableProduct == null) throw new ProdutoNaoCadastradoException();

        editableProduct.setName(nameProduct);
        editableProduct.setValue(value);
        editableProduct.setCategory(category);
    }

    public String getProductAttribute(String name, int idCompany, String attribute) throws Exception {
        if (name == null || name.isEmpty()) throw new VariavelDoProdutoInvaida("Nome");

        if (attribute == null || attribute.isEmpty()) throw new AtributoInvalidoExceptio();

        Company currentCompany = companyList.get(idCompany);
        validateCompany(currentCompany);

        Product product = null;
        for (Product productAux : currentCompany.getProductList()) {
            if (productAux.getName().equals(name)) {
                product = productAux;
            }
        }

        if (product == null) throw new ProdutoNaoEncontradoException();

        return switch (attribute) {
            case "nome" -> product.getName();
            case "valor" -> String.format("%.2f", product.getValue()).replace(",", ".");
            case "empresa" -> companyList.get(product.getCompanyId()).getName();
            case "categoria" -> product.getCategory();
            default -> throw new AtributoNaoExisteException();
        };

    }

    public String listProductsOfCompany(int idCompany) throws Exception {
        Company currentCompany = companyList.get(idCompany);
        validateCompany(currentCompany);

        StringBuilder stringProducts = new StringBuilder("{[");
        for (Product product : currentCompany.getProductList()) {
            stringProducts.append(product.getName()).append(", ");
        }
        if (stringProducts.length() > 2) {
            stringProducts = new StringBuilder(stringProducts.substring(0, stringProducts.length() - 2));
        }
        stringProducts.append("]}");

        return stringProducts.toString();
    }

    public void validateProductFields(String name, float value, String category) throws Exception {
        if (name == null || name.isEmpty()) throw new VariavelDoProdutoInvaida("Nome");

        if (value <= 0) throw new VariavelDoProdutoInvaida("Valor");

        if (category == null || category.isEmpty()) throw new VariavelDoProdutoInvaida("Categoria");
    }

    public void validateCompany(Company company) throws Exception {
        if (company == null) throw new EmpresaNaoEncontradaException();
    }
}
