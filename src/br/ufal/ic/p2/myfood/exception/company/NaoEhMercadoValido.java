package br.ufal.ic.p2.myfood.exception.company;

public class NaoEhMercadoValido extends Exception {
	public NaoEhMercadoValido() {
		super("Nao e um mercado valido");
	}
}
