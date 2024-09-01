package br.ufal.ic.p2.myfood.exception;

public class AtributoNaoExisteException extends Exception {
    public AtributoNaoExisteException() {
        super("Atributo nao existe");
    }
}
