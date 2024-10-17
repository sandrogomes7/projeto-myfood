package br.ufal.ic.p2.myfood.exception.delivery;

public class NaoExisteNadaParaEntregarExeption extends Exception {
    public NaoExisteNadaParaEntregarExeption() {
        super("Nao existe nada para ser entregue com esse id");
    }
}
