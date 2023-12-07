package com.davixavier.application.dbcache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.davixavier.database.ConnectionFactory;
import com.davixavier.database.DBOperationType;
import com.davixavier.utils.CacheDAO;
import com.davixavier.utils.DAO;
import com.davixavier.utils.Utils;

public class ClientesCacheKeyDAO implements CacheDAO<ClientesCacheKey>
{
	private static ClientesCacheKeyDAO instance;
	
	private ClientesCacheKeyDAO() 
	{
	}
	
	public static ClientesCacheKeyDAO getInstance()
	{
		if (instance == null)
		{
			synchronized (ClientesCacheKeyDAO.class) 
			{
				if (instance == null)
				{
					instance = new ClientesCacheKeyDAO();
				}
			}
		}
		
		return instance;
	}
	
	public boolean insert(ClientesCacheKey cacheKey, Connection connection)
	{
		String query = "INSERT INTO clientes_cache(idcliente, datamodificação, tipoop) "
				     + "VALUES(?, ?, ?)";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, cacheKey.getIdModificado());
			statement.setTimestamp(2, cacheKey.getDatamodificação());
			statement.setString(3, cacheKey.getTipoop().toString());
			statement.execute();
			
			return true;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}
	
	public boolean delete(ClientesCacheKey cacheKey, Connection connection)
	{
		String query = "DELETE FROM clientes_cache WHERE idcliente = ?";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, cacheKey.getIdModificado());
			statement.execute();
			
			return true;
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}
	
	public List<ClientesCacheKey> getAll(Connection connection, DBOperationType tipoop)
	{
		List<ClientesCacheKey> keys = new ArrayList<ClientesCacheKey>();
		
		String query = "SELECT * FROM clientes_cache WHERE tipoop = ?";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			statement.setString(1, tipoop.toString());
			
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next())
			{
				int id = resultSet.getInt("idcliente");
				Timestamp timestamp = resultSet.getTimestamp("datamodificação");
				DBOperationType operationType = DBOperationType.fromString(resultSet.getString("tipoop"));
				
				keys.add(new ClientesCacheKey(id, timestamp, operationType));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
		
		return keys;
	}
	
	public HashMap<Integer, ClientesCacheKey> getAll(Connection connection)
	{
		HashMap<Integer, ClientesCacheKey> hashMap = new HashMap<Integer, ClientesCacheKey>();
		
		String query = "SELECT * FROM clientes_cache";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next())
			{
				int id = resultSet.getInt("idcliente");
				Timestamp timestamp = resultSet.getTimestamp("datamodificação");
				DBOperationType operationType = DBOperationType.fromString(resultSet.getString("tipoop"));
				
				hashMap.put(id, new ClientesCacheKey(id, timestamp, operationType));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
		
		return hashMap;
	}
	
	public boolean exists(ClientesCacheKey cacheKey, Connection connection)
	{
		String query = "SELECT * FROM clientes_cache WHERE idcliente = ?";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			
			return statement.executeQuery().next();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}
	
	public boolean deleteBatch(List<ClientesCacheKey> keys, Connection connection)
	{
		if (keys.size() == 0)
			return true;
		
		String query = "DELETE FROM clientes_cache WHERE idcliente in(";
		for (int i = 0; i < keys.size(); i++)
		{
			query += "?";
			if (i+1 < keys.size())
			{
				query += ",";
			}
		}
		query += ")";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			for (int i = 0; i < keys.size(); i++)
			{
				statement.setInt(i+1, keys.get(i).getIdModificado());
			}
			statement.execute();
			
			return true;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return false;
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}

	@Override
	public boolean remove(ClientesCacheKey key, Connection connection) 
	{
		String query = "DELETE FROM clientes_cache WHERE idcliente = ?";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, key.getIdModificado());
			statement.execute();
			
			return true;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			return false;
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}

	@Override
	public boolean update(ClientesCacheKey object, Connection connection)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
