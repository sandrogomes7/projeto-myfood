package br.ufal.ic.p2.myfood.service;

import br.ufal.ic.p2.myfood.exception.*;
import br.ufal.ic.p2.myfood.exception.product.*;
import br.ufal.ic.p2.myfood.model.Company;
import br.ufal.ic.p2.myfood.model.Product;
import br.ufal.ic.p2.myfood.repository.Data;
import br.ufal.ic.p2.myfood.util.AttributeTranslation;

import java.util.Map;

public class ProductService {
    private final Map<Integer, Company> companyList;

    private static final String VARIABLE_NAME = "Nome";
    private static final String VARIABLE_VALUE = "Valor";
    private static final String VARIABLE_CATEGORY = "Categoria";

    public ProductService(Data data) {
        this.companyList = data.getCompanyList();
    }

    public int createProduct(int idCompany, String nameProduct, float value, String category) throws Exception {
        validateProductFields(nameProduct, value, category);

        Company currentCompany = companyList.get(idCompany);
        validateCompany(currentCompany);

        boolean hasProductWithSameName = currentCompany.getProductList().values().stream()
                .anyMatch(product -> product.getName().equals(nameProduct));
        if (hasProductWithSameName) throw new ProdutoComNomeJaExisteException();

        Product product = new Product(idCompany, nameProduct, value, category);
        currentCompany.addProducts(product);

        return product.getId();
    }

    public void editProduct(int idProduct, String nameProduct, float value, String category) throws Exception {
        validateProductFields(nameProduct, value, category);

        Product editableProduct = companyList.values().stream()
                .flatMap(company -> company.getProductList().values().stream())
                .filter(product -> product.getId() == idProduct)
                .findFirst()
                .orElseThrow(ProdutoNaoCadastradoException::new);

        editableProduct.setName(nameProduct);
        editableProduct.setValue(value);
        editableProduct.setCategory(category);
    }

    public String getProductAttribute(String name, int idCompany, String attribute) throws Exception {
        if (name == null || name.isEmpty()) throw new VariavelDoProdutoInvalidaException(VARIABLE_NAME);
        if (attribute == null || attribute.isEmpty()) throw new AtributoInvalidoException();

        Company currentCompany = companyList.get(idCompany);
        validateCompany(currentCompany);

        Product product = currentCompany.getProductList().values().stream()
                .filter(productAux -> productAux.getName().equals(name))
                .findFirst()
                .orElseThrow(ProdutoNaoEncontradoException::new);

        String englishAttribute = AttributeTranslation.getEnglishAttribute(attribute);
        return switch (englishAttribute) {
            case "name" -> product.getName();
            case "value" -> String.format("%.2f", product.getValue()).replace(",", ".");
            case "company" -> companyList.get(product.getIdCompany()).getName();
            case "category" -> product.getCategory();
            default -> throw new AtributoNaoExisteException();
        };
    }

    public String listProductsOfCompany(int idCompany) throws Exception {
        Company currentCompany = companyList.get(idCompany);
        validateCompany(currentCompany);

        return currentCompany.productsInString();
    }

    private void validateProductFields(String name, float value, String category) throws Exception {
        if (name == null || name.isEmpty()) throw new VariavelDoProdutoInvalidaException(VARIABLE_NAME);
        if (value <= 0) throw new VariavelDoProdutoInvalidaException(VARIABLE_VALUE);
        if (category == null || category.isEmpty()) throw new VariavelDoProdutoInvalidaException(VARIABLE_CATEGORY);
    }

    private void validateCompany(Company company) throws Exception {
        if (company == null) throw new EmpresaNaoEncontradaException();
    }
}
