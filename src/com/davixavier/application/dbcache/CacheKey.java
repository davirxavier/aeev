package com.davixavier.application.dbcache;

import java.sql.Timestamp;

import com.davixavier.database.DBOperationType;

public class CacheKey 
{
	private int idmodificado;
	private Timestamp datamodifica��o;
	private DBOperationType tipoop;

	public CacheKey(int idprodutomodificado, Timestamp datamodifica��o, DBOperationType tipoop)
	{
		super();
		this.idmodificado = idprodutomodificado;
		this.datamodifica��o = datamodifica��o;
		this.tipoop = tipoop;
	}

	public int getIdModificado() 
	{
		return idmodificado;
	}

	public void setIdModificado(int idprodutomodificado) 
	{
		this.idmodificado = idprodutomodificado;
	}

	public Timestamp getDatamodifica��o()
	{
		return datamodifica��o;
	}

	public void setDatamodifica��o(Timestamp datamodifica��o)
	{
		this.datamodifica��o = datamodifica��o;
	}

	public DBOperationType getTipoop()
	{
		return tipoop;
	}

	public void setTipoop(DBOperationType tipoop) 
	{
		this.tipoop = tipoop;
	}
}
