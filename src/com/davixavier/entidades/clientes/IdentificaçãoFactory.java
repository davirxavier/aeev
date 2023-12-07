package com.davixavier.entidades.clientes;

public class Identifica��oFactory
{
	public static Identific�vel identific�velFromString(String string)
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
