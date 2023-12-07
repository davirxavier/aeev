package com.davixavier.entidades.compras;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProdutoVenda
{
	private SimpleIntegerProperty id;
	private SimpleStringProperty nome;
	private SimpleIntegerProperty quantidade;
	private SimpleDoubleProperty pre�o;
	
	public ProdutoVenda()
	{
		id = new SimpleIntegerProperty();
		nome = new SimpleStringProperty("");
		quantidade = new SimpleIntegerProperty(0);
		pre�o = new SimpleDoubleProperty(0);
	}
	
	public ProdutoVenda(String nome, int quantidade, double pre�o)
	{
		super();
		this.nome = new SimpleStringProperty(nome);
		this.pre�o = new SimpleDoubleProperty(pre�o);
		this.quantidade = new SimpleIntegerProperty(quantidade);
		this.pre�o = new SimpleDoubleProperty(pre�o);
	}
	
	public ProdutoVenda(int id, String nome, int quantidade, double pre�o)
	{
		super();
		this.nome = new SimpleStringProperty(nome);
		this.pre�o = new SimpleDoubleProperty(pre�o);
		this.quantidade = new SimpleIntegerProperty(quantidade);
		this.pre�o = new SimpleDoubleProperty(pre�o);
		this.id = new SimpleIntegerProperty(id);
	}

	@Override
	public String toString()
	{
		return getNome() + "," + getPre�o() + "," + getQuantidade();
	}
	
	public boolean equals(ProdutoVenda obj) 
	{
		return id.get() == obj.getId();
	}
	
	public static ProdutoVenda parse(String object)
	{
		if (object.isEmpty())
			return null;
		
		ProdutoVenda produtoVenda = new ProdutoVenda();
		
		String[] split = object.split("\\,");
		produtoVenda.setNome(split[0]);
		produtoVenda.setPre�o(Double.parseDouble(split[1]));
		produtoVenda.setQuantidade(Integer.parseInt(split[2]));
		
		return produtoVenda;
	}
	
	public static ObservableList<ProdutoVenda> parseArray(String[] objects)
	{
		ObservableList<ProdutoVenda> produtoVendas = FXCollections.observableArrayList();
		for (int i = 0; i < objects.length; i++)
		{
			produtoVendas.add(parse(objects[i]));
		}
		
		return produtoVendas;
	}
	
	public int getId()
	{
		return id.get();
	}
	public void setId(int id)
	{
		this.id.set(id);
	}
	public String getNome()
	{
		return nome.get();
	}
	public void setNome(String nome)
	{
		this.nome.set(nome);
	}
	public int getQuantidade()
	{
		return quantidade.get();
	}
	public void setQuantidade(int quantidade)
	{
		this.quantidade.set(quantidade);
	}
	public double getPre�o()
	{
		return pre�o.get();
	}
	public void setPre�o(double pre�o)
	{
		this.pre�o.set(pre�o);
	}
	
	public SimpleStringProperty nomeProperty()
	{
		return nome;
	}
	public SimpleIntegerProperty quantidadeProperty()
	{
		return quantidade;
	}
	public SimpleDoubleProperty pre�oProperty()
	{
		return pre�o;
	}
	public SimpleIntegerProperty idProperty()
	{
		return id;
	}
}
