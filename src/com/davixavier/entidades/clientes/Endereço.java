package com.davixavier.entidades.clientes;

public class Endereço
{
	private String rua;
	private String numero;
	private String bairro;
	private String cidade;
	private String estado;
	private String complemento;
	
	public Endereço(String rua, String numero, String bairro, String cidade, String estado, String complemento)
	{
		super();
		this.rua = rua;
		this.numero = numero;
		this.bairro = bairro;
		this.cidade = cidade;
		this.estado = estado;
		this.complemento = complemento;
	}

	public Endereço()
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
	
	public static Endereço fromString(String string)
	{
		Endereço endereço = new Endereço();
		
		String[] split = string.split("\\,");
		if (split.length < 5)
			return endereço;
		
		endereço.setRua(split[0]);
		endereço.setNumero(split[1]);
		endereço.setBairro(split[2]);
		endereço.setCidade(split[3]);
		endereço.setEstado(split[4]);
		
		if (split.length > 5)
		{
			endereço.setComplemento(split[5]);
		}
		else
		{
			endereço.setComplemento("");
		}
		
		return endereço;
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
