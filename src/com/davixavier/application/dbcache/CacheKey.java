package com.davixavier.application.dbcache;

import java.sql.Timestamp;

import com.davixavier.database.DBOperationType;

public class CacheKey 
{
	private int idmodificado;
	private Timestamp datamodificação;
	private DBOperationType tipoop;

	public CacheKey(int idprodutomodificado, Timestamp datamodificação, DBOperationType tipoop)
	{
		super();
		this.idmodificado = idprodutomodificado;
		this.datamodificação = datamodificação;
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

	public Timestamp getDatamodificação()
	{
		return datamodificação;
	}

	public void setDatamodificação(Timestamp datamodificação)
	{
		this.datamodificação = datamodificação;
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
