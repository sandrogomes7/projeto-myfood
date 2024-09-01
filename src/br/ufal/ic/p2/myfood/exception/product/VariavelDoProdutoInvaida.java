package br.ufal.ic.p2.myfood.exception.product;

public class VariavelDoProdutoInvaida extends Exception{
    public VariavelDoProdutoInvaida(String tipo) {
        super(tipo + " invalido");
    }
}
