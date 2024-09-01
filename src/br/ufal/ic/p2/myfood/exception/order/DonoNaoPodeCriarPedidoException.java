package br.ufal.ic.p2.myfood.exception.order;

public class DonoNaoPodeCriarPedidoException extends Exception {
    public DonoNaoPodeCriarPedidoException() {
        super("Dono de empresa nao pode fazer um pedido");
    }
}
