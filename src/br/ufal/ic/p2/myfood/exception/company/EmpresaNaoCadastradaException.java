package br.ufal.ic.p2.myfood.exception.company;

public class EmpresaNaoCadastradaException extends Exception {
    public EmpresaNaoCadastradaException() {
        super("Empresa nao cadastrada");
    }
}
