package br.ufal.ic.p2.myfood.exception.order;

public class NaoExistePedidoAbertoException extends Exception {
    public NaoExistePedidoAbertoException() {
        super("Nao existe pedido em aberto");
    }
}
