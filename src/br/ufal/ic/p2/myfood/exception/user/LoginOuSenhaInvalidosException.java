package br.ufal.ic.p2.myfood.exception.user;

public class LoginOuSenhaInvalidosException extends Exception {
    public LoginOuSenhaInvalidosException() {
        super("Login ou senha invalidos");
    }
}
