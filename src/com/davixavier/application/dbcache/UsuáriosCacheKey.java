package com.davixavier.application.dbcache;

import java.sql.Timestamp;

import com.davixavier.database.DBOperationType;

public class Usu�riosCacheKey extends CacheKey
{
	public Usu�riosCacheKey(int idprodutomodificado, Timestamp datamodifica��o, DBOperationType tipoop)
	{
		super(idprodutomodificado, datamodifica��o, tipoop);
	}
}
