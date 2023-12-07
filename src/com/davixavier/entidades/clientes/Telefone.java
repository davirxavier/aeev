package com.davixavier.entidades.clientes;

public class Telefone
{
	private String pa�s;
	private String ddd;
	private String n�mero;
	
	public Telefone(String pa�s, String ddd, String n�mero)
	{
		super();
		this.pa�s = pa�s;
		this.ddd = ddd;
		this.n�mero = n�mero;
	}

	public Telefone()
	{
		pa�s = "";
		ddd = "";
		n�mero = "";
	}
	
	public String toFormattedString()
	{
		if (n�mero.length() < 5)
		{
			return "";
		}
		return "+" + pa�s + " (" + ddd + ") " + n�mero.substring(0, 5) + "-" + n�mero.substring(5);
	}
	
	public String toString()
	{
		return pa�s + "," + ddd + "," + n�mero;
	}
	
	public static Telefone fromString(String string)
	{
		Telefone telefone = new Telefone();
		String[] split = string.split("\\,");
		
		if (split.length < 3)
			return new Telefone("0000", "00", "000000000");
		
		telefone.setPa�s(split[0]);
		telefone.setDdd(split[1]);
		telefone.setN�mero(split[2]);
		
		return telefone;
	}
	
	public static Telefone fromString(String[] strings)
	{
		return new Telefone(strings[0], strings[1], strings[2]);
	}
	
	public String getPa�s()
	{
		return pa�s;
	}
	public void setPa�s(String pa�s)
	{
		this.pa�s = pa�s;
	}
	public String getDdd()
	{
		return ddd;
	}
	public void setDdd(String ddd)
	{
		this.ddd = ddd;
	}
	public String getN�mero()
	{
		return n�mero;
	}
	public void setN�mero(String n�mero)
	{
		this.n�mero = n�mero;
	}
}
