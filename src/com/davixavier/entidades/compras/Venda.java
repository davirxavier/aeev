package com.davixavier.entidades.compras;

import java.sql.Timestamp;

import com.davixavier.entidades.clientes.Cliente;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Venda
{
	private int id;
	private Timestamp data;
	private double pre�o;
	private String descri��o;
	private ObservableList<ProdutoVenda> produtos;
	private Cliente cliente;

	public Venda()
	{
		id = 0;
		data = new Timestamp(0);
		pre�o = 0;
		descri��o = "";
		produtos = FXCollections.observableArrayList();
		
		cliente = new Cliente();
	}
	
	public Venda(int id, Timestamp data, double pre�o, String descri��o, ObservableList<ProdutoVenda> produtos)
	{
		super();
		this.id = id;
		this.data = data;
		this.pre�o = pre�o;
		this.descri��o = descri��o;
		
		this.produtos = produtos;
		
		cliente = new Cliente();
	}
	
	public String getProdutosString()
	{
		String produtos = "";
		for (ProdutoVenda p : getProdutos())
		{
			produtos += p.toString() + ";";
		}
		return produtos;
	}
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public Timestamp getData()
	{
		return data;
	}
	public void setData(Timestamp data)
	{
		this.data = data;
	}
	public double getPre�o()
	{
		return pre�o;
	}
	public void setPre�o(double pre�o)
	{
		this.pre�o = pre�o;
	}
	public String getDescri��o()
	{
		return descri��o;
	}
	public void setDescri��o(String descri��o)
	{
		this.descri��o = descri��o;
	}

	public ObservableList<ProdutoVenda> getProdutos()
	{
		return produtos;
	}

	public void setProdutos(ObservableList<ProdutoVenda> produtos)
	{
		this.produtos = produtos;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}
