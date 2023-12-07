package com.davixavier.entidades.estoque;

public class Produto
{
	private String nome;
	private int id;
	private double pre�o;
	private double pre�oCompra;
	private int quantidade;
	private int codigo;

	public Produto()
	{
		nome = "";
		id = 0;
		pre�o = 0;
		pre�oCompra = 0;
		quantidade = 0;
		codigo = 0;
	}
		
	public Produto(String nome, int id, double pre�o, double pre�oCompra, int quantidade, int codigo)
	{
		this.nome = nome;
		this.id = id;
		this.pre�o = pre�o;
		this.pre�oCompra = pre�oCompra;
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

	public double getPre�o()
	{
		return pre�o;
	}

	public void setPre�o(double pre�o)
	{
		this.pre�o = pre�o;
	}

	public double getPre�oCompra()
	{
		return pre�oCompra;
	}

	public void setPre�oCompra(double pre�oCompra)
	{
		this.pre�oCompra = pre�oCompra;
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
