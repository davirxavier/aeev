package com.davixavier.application;

import java.io.File;

import com.davixavier.application.img.IconsPath;
import com.davixavier.entidades.clientes.Telefone;

import javafx.scene.image.Image;

public class Loja 
{
	private int idusuário;
	private String nome = "AEEV";
	private Telefone telefone1 = new Telefone("0000", "00", "000000000");
	private Telefone telefone2 = new Telefone("0000", "00", "000000000");
	private Image logo;
	private String logoPath;
	
	private static Loja instancia;
	
	private Loja() 
	{
		nome = "AEEV";
		idusuário = 1;
		telefone1 = new Telefone("0000", "00", "000000000");
		telefone2 = new Telefone("0000", "00", "000000000");
		logo = new Image(IconsPath.ICON.getPath());
		logoPath = IconsPath.ICON.getPath();
	}
	
	public static Loja getInstance()
	{
		if (instancia == null)
		{
			synchronized (Loja.class) 
			{
				if (instancia == null)
				{
					instancia = new Loja();
				}
			}
		}
		
		return instancia;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Telefone getTelefone1() {
		return telefone1;
	}

	public void setTelefone1(Telefone telefone1) {
		this.telefone1 = telefone1;
	}

	public Telefone getTelefone2() {
		return telefone2;
	}

	public void setTelefone2(Telefone telefone2) {
		this.telefone2 = telefone2;
	}

	public Image getLogo() {
		return logo;
	}

	public void setLogo(Image logo) {
		this.logo = logo;
	}
	
	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public int getIdusuário() {
		return idusuário;
	}

	public void setIdusuário(int idusuário) {
		this.idusuário = idusuário;
	}
	
}
