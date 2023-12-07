package com.davixavier.entidades.compras;

public enum TipoPagamento 
{
	DINHEIRO("Dinheiro"), CARTÃO("Cartão"), BOLETO("Boleto"), CHEQUE("Cheque"), PROMISSÓRIA("Promissória"), 
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
