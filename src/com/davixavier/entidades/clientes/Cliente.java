package com.davixavier.entidades.clientes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Cliente
{
	private int id;
	private String nome;
	private ObservableList<Telefone> telefones;
	private Endereço endereço;
	private Identificável cpfCnpj;
	
	private String nomeFantasia;
	
	public Cliente(int id, String nome, String nomeFantasia, ObservableList<Telefone> telefones, Endereço endereço)
	{
		super();
		this.id = id;
		this.nome = nome;
		this.telefones = telefones;
		this.endereço = endereço;
		this.nomeFantasia = nomeFantasia;
	}
	
	public Cliente(int id, String nome, ObservableList<Telefone> telefones, Endereço endereço)
	{
		super();
		this.id = id;
		this.nome = nome;
		this.telefones = telefones;
		this.endereço = endereço;
	}

	public Cliente()
	{
		id = 0;
		nome = "";
		telefones = FXCollections.observableArrayList();
		endereço = new Endereço();
		cpfCnpj = new CPF("");
		nomeFantasia = "";
	}
	
	public static Cliente fromString(String string)
	{
		if (string.isEmpty())
			return null;
		
		String[] split = string.split("\\;");
		
		Cliente cliente = new Cliente();
		cliente.setId(Integer.parseInt(split[0]));
		cliente.setNome(split[1]);
		
		cliente.setEndereço(Endereço.fromString(split[2]));
		
		cliente.setCpfCnpj(IdentificaçãoFactory.identificávelFromString(split[3]));
		
		cliente.getTelefones().add(Telefone.fromString(split[4]));
		
		if (split.length > 5)
			cliente.getTelefones().add(Telefone.fromString(split[5]));
		else
			cliente.getTelefones().add(Telefone.fromString(",,;"));
		
		if (split.length > 6)
			cliente.setNomeFantasia(split[6]);
		else
			cliente.setNomeFantasia("");
		
		return cliente;
	}
	
	public String toString()
	{
		if (cpfCnpj == null || endereço == null || telefones.size() == 0)
			return "";
		
		String ret = "";
		ret += id + ";";
		ret += nome + ";";
		
		ret += endereço.toString() + ";";
		
		ret += cpfCnpj.toString() + ";";
		
		if (telefones.size() > 0)
			ret += telefones.get(0).toString() + ";";
		else
			ret += ",,;";
		
		if (telefones.size() > 1)
			ret += telefones.get(1).toString() + ";";
		else
			ret += ",,;";
		
		ret += nomeFantasia + ";";
		
		return ret;
	}
	
	public static ObservableList<Telefone> telefonesFromString(String string)
	{
		ObservableList<Telefone> telefones = FXCollections.observableArrayList();
		String[] split = string.split(";");
		
		for (String telefone : split)
		{
			telefones.add(Telefone.fromString(telefone));
		}
		
		return telefones;
	}
	
	public String getTelefonesString()
	{
		String telefones = "";
		
		for (Telefone telefone : this.telefones)
		{
			telefones += telefone.toString() + ";";
		}
		
		return telefones;
	}
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getNome()
	{
		return nome;
	}
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	public ObservableList<Telefone> getTelefones()
	{
		return telefones;
	}
	public void setTelefones(ObservableList<Telefone> telefones)
	{
		this.telefones = telefones;
	}
	public Endereço getEndereço()
	{
		return endereço;
	}
	public void setEndereço(Endereço endereço)
	{
		this.endereço = endereço;
	}

	public Identificável getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(Identificável cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}
	
	
}
