package com.davixavier.application.dbcache;

import java.sql.Timestamp;

import com.davixavier.database.DBOperationType;

public class UsuáriosCacheKey extends CacheKey
{
	public UsuáriosCacheKey(int idprodutomodificado, Timestamp datamodificação, DBOperationType tipoop)
	{
		super(idprodutomodificado, datamodificação, tipoop);
	}
}
