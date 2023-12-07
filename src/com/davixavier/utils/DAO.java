package com.davixavier.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface DAO<T>
{
	public boolean insert(T object, Connection connection) throws SQLException;
	public boolean remove(T object, Connection connection) throws SQLException;
	public boolean update(T object, Connection connection) throws SQLException;
	public HashMap<?, T> getAll(Connection connection) throws SQLException;
}
