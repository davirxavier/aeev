package com.davixavier.application.dbcache;

import java.sql.Timestamp;

import com.davixavier.database.DBOperationType;

public class VendasCacheKey extends CacheKey
{

	public VendasCacheKey(int idprodutomodificado, Timestamp datamodifica��o, DBOperationType tipoop)
	{
		super(idprodutomodificado, datamodifica��o, tipoop);
	}

}
