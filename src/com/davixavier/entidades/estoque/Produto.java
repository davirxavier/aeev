package com.davixavier.entidades.estoque;

public class Produto
{
	private String nome;
	private int id;
	private double preço;
	private double preçoCompra;
	private int quantidade;
	private int codigo;

	public Produto()
	{
		nome = "";
		id = 0;
		preço = 0;
		preçoCompra = 0;
		quantidade = 0;
		codigo = 0;
	}
		
	public Produto(String nome, int id, double preço, double preçoCompra, int quantidade, int codigo)
	{
		this.nome = nome;
		this.id = id;
		this.preço = preço;
		this.preçoCompra = preçoCompra;
		this.quantidade = quantidade;
		this.codigo = codigo;
	}
	
	public boolean equals(Object object)
	{
		if (object instanceof Produto)
		{
			return ((Produto)object).getId() == getId();
		}
		else 
		{
			return super.equals(object);
		}
	}

	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public double getPreço()
	{
		return preço;
	}

	public void setPreço(double preço)
	{
		this.preço = preço;
	}

	public double getPreçoCompra()
	{
		return preçoCompra;
	}

	public void setPreçoCompra(double preçoCompra)
	{
		this.preçoCompra = preçoCompra;
	}

	public int getQuantidade()
	{
		return quantidade;
	}

	public void setQuantidade(int quantidade)
	{
		this.quantidade = quantidade;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
}
