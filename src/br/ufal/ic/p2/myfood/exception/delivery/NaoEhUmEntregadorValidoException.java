package br.ufal.ic.p2.myfood.exception.delivery;

public class NaoEhUmEntregadorValidoException extends Exception {
    public NaoEhUmEntregadorValidoException() {
        super("Nao e um entregador valido");
    }
}
