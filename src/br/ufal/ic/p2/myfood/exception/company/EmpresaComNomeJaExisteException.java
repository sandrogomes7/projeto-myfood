package br.ufal.ic.p2.myfood.exception.company;

public class EmpresaComNomeJaExisteException extends Exception {
    public EmpresaComNomeJaExisteException() {
        super("Empresa com esse nome ja existe");
    }
}
