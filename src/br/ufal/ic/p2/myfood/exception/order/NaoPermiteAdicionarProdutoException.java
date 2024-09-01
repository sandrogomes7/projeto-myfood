package br.ufal.ic.p2.myfood.exception.order;

public class NaoPermiteAdicionarProdutoException extends Exception {
    public NaoPermiteAdicionarProdutoException() {
        super("Nao e possivel adcionar produtos a um pedido fechado");
    }
}
