package com.davixavier.entidades.clientes;

public class Telefone
{
	private String país;
	private String ddd;
	private String número;
	
	public Telefone(String país, String ddd, String número)
	{
		super();
		this.país = país;
		this.ddd = ddd;
		this.número = número;
	}

	public Telefone()
	{
		país = "";
		ddd = "";
		número = "";
	}
	
	public String toFormattedString()
	{
		if (número.length() < 5)
		{
			return "";
		}
		return "+" + país + " (" + ddd + ") " + número.substring(0, 5) + "-" + número.substring(5);
	}
	
	public String toString()
	{
		return país + "," + ddd + "," + número;
	}
	
	public static Telefone fromString(String string)
	{
		Telefone telefone = new Telefone();
		String[] split = string.split("\\,");
		
		if (split.length < 3)
			return new Telefone("0000", "00", "000000000");
		
		telefone.setPaís(split[0]);
		telefone.setDdd(split[1]);
		telefone.setNúmero(split[2]);
		
		return telefone;
	}
	
	public static Telefone fromString(String[] strings)
	{
		return new Telefone(strings[0], strings[1], strings[2]);
	}
	
	public String getPaís()
	{
		return país;
	}
	public void setPaís(String país)
	{
		this.país = país;
	}
	public String getDdd()
	{
		return ddd;
	}
	public void setDdd(String ddd)
	{
		this.ddd = ddd;
	}
	public String getNúmero()
	{
		return número;
	}
	public void setNúmero(String número)
	{
		this.número = número;
	}
}
