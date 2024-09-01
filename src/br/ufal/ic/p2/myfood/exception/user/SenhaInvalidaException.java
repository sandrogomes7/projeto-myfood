package br.ufal.ic.p2.myfood.exception.user;

public class SenhaInvalidaException extends Exception {
    public SenhaInvalidaException() {
        super("Senha invalido");
    }
}
