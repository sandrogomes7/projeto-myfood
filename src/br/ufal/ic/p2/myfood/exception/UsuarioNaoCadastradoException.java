package br.ufal.ic.p2.myfood.exception;

public class UsuarioNaoCadastradoException extends Exception {
    public UsuarioNaoCadastradoException() {
        super("Usuario nao cadastrado.");
    }
}
