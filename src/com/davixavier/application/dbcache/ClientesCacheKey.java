package com.davixavier.application.dbcache;

import java.sql.Timestamp;

import com.davixavier.database.DBOperationType;

public class ClientesCacheKey extends CacheKey
{
	public ClientesCacheKey(int idprodutomodificado, Timestamp datamodificação, DBOperationType tipoop)
	{
		super(idprodutomodificado, datamodificação, tipoop);
	}
}
