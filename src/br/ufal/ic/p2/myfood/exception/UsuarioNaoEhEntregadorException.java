package br.ufal.ic.p2.myfood.exception;

public class UsuarioNaoEhEntregadorException extends Exception {
	public UsuarioNaoEhEntregadorException() {
		super("Usuario nao e um entregador");
	}
}
