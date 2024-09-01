package br.ufal.ic.p2.myfood.exception.company;

public class EmpresaMesmoNomeELocalException extends Exception {
    public EmpresaMesmoNomeELocalException() {
        super("Proibido cadastrar duas empresas com o mesmo nome e local");
    }
}
