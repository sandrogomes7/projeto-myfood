package br.ufal.ic.p2.myfood.exception;

public class EmpresaNaoEncontradaException extends Exception {
    public EmpresaNaoEncontradaException() {
        super("Empresa nao encontrada");
    }
}
