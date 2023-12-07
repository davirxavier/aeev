package com.davixavier.entidades.clientes;

public class Endere�o
{
	private String rua;
	private String numero;
	private String bairro;
	private String cidade;
	private String estado;
	private String complemento;
	
	public Endere�o(String rua, String numero, String bairro, String cidade, String estado, String complemento)
	{
		super();
		this.rua = rua;
		this.numero = numero;
		this.bairro = bairro;
		this.cidade = cidade;
		this.estado = estado;
		this.complemento = complemento;
	}

	public Endere�o()
	{
		rua = "";
		numero = "";
		bairro = "";
		cidade = "";
		estado = "";
		complemento = "";
	}
	
	public String toString()
	{
		return rua + "," + numero + "," + bairro + "," + cidade + "," + estado + "," + complemento;
	}
	
	public String toFormattedString()
	{
		return numero + ", " + rua + ", " + complemento + ", " + bairro + ", " + cidade + "-" + estado;
	}
	
	public static Endere�o fromString(String string)
	{
		Endere�o endere�o = new Endere�o();
		
		String[] split = string.split("\\,");
		if (split.length < 5)
			return endere�o;
		
		endere�o.setRua(split[0]);
		endere�o.setNumero(split[1]);
		endere�o.setBairro(split[2]);
		endere�o.setCidade(split[3]);
		endere�o.setEstado(split[4]);
		
		if (split.length > 5)
		{
			endere�o.setComplemento(split[5]);
		}
		else
		{
			endere�o.setComplemento("");
		}
		
		return endere�o;
	}
	
	public String getRua()
	{
		return rua;
	}
	public void setRua(String rua)
	{
		this.rua = rua;
	}
	public String getNumero()
	{
		return numero;
	}
	public void setNumero(String numero)
	{
		this.numero = numero;
	}
	public String getBairro()
	{
		return bairro;
	}
	public void setBairro(String bairro)
	{
		this.bairro = bairro;
	}
	public String getCidade()
	{
		return cidade;
	}
	public void setCidade(String cidade)
	{
		this.cidade = cidade;
	}
	public String getEstado()
	{
		return estado;
	}
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	public String getComplemento()
	{
		return complemento;
	}
	public void setComplemento(String complemento)
	{
		this.complemento = complemento;
	}
}
