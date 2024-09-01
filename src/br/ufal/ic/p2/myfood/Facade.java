package br.ufal.ic.p2.myfood;

import br.ufal.ic.p2.myfood.controller.MyFoodSystem;

public class Facade {
    private final MyFoodSystem sys;

    public Facade() throws Exception {
        this.sys = new MyFoodSystem();
    }

    public void zerarSistema() throws Exception {
        sys.resetSystem();
    }

    public String getAtributoUsuario(int id, String atributo) throws Exception {
        return sys.getUserAttribute(id, atributo);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco) throws Exception {
        sys.createUser(nome, email, senha, endereco);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) throws Exception {
        sys.createUser(nome, email, senha, endereco, cpf);
    }

    public int login(String email, String senha) throws Exception {
        return sys.login(email, senha);
    }

    public void encerrarSistema() throws Exception {
        sys.endSystem();
    }

    public int criarEmpresa(String tipoEmpresa, int idDono, String nomeEmpresa, String endereco, String tipoCozinha) throws Exception {
        return sys.createCompany(tipoEmpresa, idDono, nomeEmpresa, endereco, tipoCozinha);
    }

    public String getEmpresasDoUsuario(int id) throws Exception {
        return sys.getUserCompanies(id);
    }

    public String getAtributoEmpresa(int id, String atributo) throws Exception {
        return sys.getCompanyAttribute(id, atributo);
    }

    public int getIdEmpresa(int id, String nome, int indice) throws Exception {
        return sys.getCompanyId(id, nome, indice);
    }

    public int criarProduto(int empresa, String nome, float valor, String categoria) throws Exception {
        return sys.createProduct(empresa, nome, valor, categoria);
    }

    public void editarProduto(int produto, String nome, float valor, String categoria) throws Exception {
        sys.editProduct(produto, nome, valor, categoria);
    }

    public String getProduto(String nome, int empresa, String atributo) throws Exception {
        return sys.getProduct(nome, empresa, atributo);
    }

    public String listarProdutos(int empresa) throws Exception {
        return sys.listProducts(empresa);
    }

    public int criarPedido(int cliente, int empresa) throws Exception {
        return sys.createOrder(cliente, empresa);
    }

    public void adicionarProduto(int numeroPedido, int numeroProduto) throws Exception {
        sys.addProduct(numeroPedido, numeroProduto);
    }

    public String getPedidos(int numero, String atributo) throws Exception {
        return sys.getOrders(numero, atributo);
    }

    public void fecharPedido(int numero) throws Exception {
        sys.closeOrder(numero);
    }

    public void removerProduto(int pedido, String produto) throws Exception {
        sys.removeProduct(pedido, produto);
    }

    public int getNumeroPedido(int cliente, int empresa, int indice) throws Exception {
        return sys.getOrderNumber(cliente, empresa, indice);
    }
}