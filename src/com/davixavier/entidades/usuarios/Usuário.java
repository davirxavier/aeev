package com.davixavier.entidades.usuarios;

public class Usu�rio
{
	private String username;
	private String senhaHash;
	private String email;
	private String type;
	private int id;
	
	public Usu�rio()
	{
		username = "";
		senhaHash = "";
		type = "Usu�rio";
		setId(0);
	}
	
	public Usu�rio(String nome, String senhaHash, String type, String email)
	{
		this.username = nome;
		this.senhaHash = senhaHash;
		this.type = type;
		this.email = email;
	}
	
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String nome)
	{
		this.username = nome;
	}
	public String getSenhaHash()
	{
		return senhaHash;
	}
	public void setSenhaHash(String senhaHash)
	{
		this.senhaHash = senhaHash;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
