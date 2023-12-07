package com.davixavier.entidades.compras;

import java.sql.Timestamp;

import com.davixavier.entidades.clientes.Cliente;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Venda
{
	private int id;
	private Timestamp data;
	private double preço;
	private String descrição;
	private ObservableList<ProdutoVenda> produtos;
	private Cliente cliente;

	public Venda()
	{
		id = 0;
		data = new Timestamp(0);
		preço = 0;
		descrição = "";
		produtos = FXCollections.observableArrayList();
		
		cliente = new Cliente();
	}
	
	public Venda(int id, Timestamp data, double preço, String descrição, ObservableList<ProdutoVenda> produtos)
	{
		super();
		this.id = id;
		this.data = data;
		this.preço = preço;
		this.descrição = descrição;
		
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
	public double getPreço()
	{
		return preço;
	}
	public void setPreço(double preço)
	{
		this.preço = preço;
	}
	public String getDescrição()
	{
		return descrição;
	}
	public void setDescrição(String descrição)
	{
		this.descrição = descrição;
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
