package br.ufal.ic.p2.myfood.exception.company;

public class UsuarioNaoPodeCriarEmpresaException extends Exception {
    public UsuarioNaoPodeCriarEmpresaException() {
        super("Usuario nao pode criar uma empresa");
    }
}
