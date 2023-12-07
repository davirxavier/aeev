package com.davixavier.entidades.clientes;

public class IdentificaçãoFactory
{
	public static Identificável identificávelFromString(String string)
	{
		if (string.length() <= 11)
		{
			return new CPF(string);
		}
		else
		{
			return new CNPJ(string);
		}
	}
}
