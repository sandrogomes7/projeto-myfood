package br.ufal.ic.p2.myfood.exception.delivery;

public class EntregadorAindaEmEntregaException extends Exception {
	public EntregadorAindaEmEntregaException() {
		super("Entregador ainda em entrega");
	}
}
