package com.davixavier.application.dbcache;

import java.sql.Timestamp;

import com.davixavier.database.DBOperationType;

public class EstoqueCacheKey extends CacheKey
{
	public EstoqueCacheKey(int idprodutomodificado, Timestamp datamodificação, DBOperationType tipoop)
	{
		super(idprodutomodificado, datamodificação, tipoop);
	}
}
