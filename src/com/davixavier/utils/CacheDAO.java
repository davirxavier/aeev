package com.davixavier.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.davixavier.application.dbcache.CacheKey;
import com.davixavier.database.DBOperationType;

public interface CacheDAO<T extends CacheKey> extends DAO<T>
{
	public List<T> getAll(Connection connection, DBOperationType tipoop) throws SQLException;
	public boolean deleteBatch(List<T> list, Connection connection)  throws SQLException;
}
