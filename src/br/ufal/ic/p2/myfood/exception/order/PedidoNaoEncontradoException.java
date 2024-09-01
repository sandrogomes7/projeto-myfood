package br.ufal.ic.p2.myfood.exception.order;

public class PedidoNaoEncontradoException extends Exception {
    public PedidoNaoEncontradoException() {
        super("Pedido nao encontrado");
    }
}
