package br.ufal.ic.p2.myfood.exception;

public class ProdutoNaoEncontradoException extends Exception {
    public ProdutoNaoEncontradoException() {
        super("Produto nao encontrado");
    }
}
