package com.davixavier.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.davixavier.application.dbcache.CacheKey;

public interface AdvancedDAO<T, S extends CacheKey> extends DAO<T>
{
	public boolean insertBatch(List<T> list, Connection connection) throws SQLException;
	public List<T> getAllList(Connection connection, List<S> keys) throws SQLException;
	public List<T> getAllList(Connection connection) throws SQLException;
	public boolean updateBatch(List<T> list, Connection connection) throws SQLException;
	public boolean removeBatch(List<T> list, Connection connection) throws SQLException;
	
	public T get(int i, Connection connection) throws SQLException;
	public int getLastIndex(Connection connection) throws SQLException;
	public boolean updateId(T t, int newId, Connection connection) throws SQLException;
}
