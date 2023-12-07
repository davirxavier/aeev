package com.davixavier.entidades.compras;

public enum TipoPagamento 
{
	DINHEIRO("Dinheiro"), CART�O("Cart�o"), BOLETO("Boleto"), CHEQUE("Cheque"), PROMISS�RIA("Promiss�ria"), 
	OUTROS("Outros");
	
	private String string;
	
	private TipoPagamento(String string) 
	{
		this.string = string;
	}
	
	public String toString()
	{
		return string;
	}
}
