package com.davixavier.application.dbcache;

import java.sql.Timestamp;

import com.davixavier.database.DBOperationType;

public class ClientesCacheKey extends CacheKey
{
	public ClientesCacheKey(int idprodutomodificado, Timestamp datamodifica��o, DBOperationType tipoop)
	{
		super(idprodutomodificado, datamodifica��o, tipoop);
	}
}
