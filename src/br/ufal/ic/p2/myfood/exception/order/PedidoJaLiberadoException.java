package br.ufal.ic.p2.myfood.exception.order;

public class PedidoJaLiberadoException extends Exception {
	public PedidoJaLiberadoException() {
		super("Pedido ja liberado");
	}
}
