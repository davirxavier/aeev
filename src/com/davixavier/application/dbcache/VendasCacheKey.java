package com.davixavier.application.dbcache;

import java.sql.Timestamp;

import com.davixavier.database.DBOperationType;

public class VendasCacheKey extends CacheKey
{

	public VendasCacheKey(int idprodutomodificado, Timestamp datamodificação, DBOperationType tipoop)
	{
		super(idprodutomodificado, datamodificação, tipoop);
	}

}
