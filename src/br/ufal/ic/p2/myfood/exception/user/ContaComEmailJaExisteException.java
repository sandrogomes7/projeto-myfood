package br.ufal.ic.p2.myfood.exception.user;

public class ContaComEmailJaExisteException extends Exception{
    public ContaComEmailJaExisteException() {
        super("Conta com esse email ja existe");
    }
}
