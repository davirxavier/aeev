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
import com.davixavier.utils.Utils;

public class UsuáriosCacheKeyDAO implements CacheDAO<UsuáriosCacheKey>
{
	private static UsuáriosCacheKeyDAO instance;
	
	public UsuáriosCacheKeyDAO() 
	{
	}
	
	public static UsuáriosCacheKeyDAO getInstance()
	{
		if (instance == null)
		{
			synchronized (UsuáriosCacheKeyDAO.class) 
			{
				if (instance == null)
				{
					instance = new UsuáriosCacheKeyDAO();
				}
			}
		}
		
		return instance;
	}
	
	public boolean insert(UsuáriosCacheKey cacheKey, Connection connection)
	{
		String query = "INSERT INTO usuários_cache(idusuário, datamodificação, tipoop) "
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
	
	public boolean remove(UsuáriosCacheKey cacheKey, Connection connection)
	{
		String query = "DELETE FROM usuários_cache WHERE idusuário = ?";
		
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
	
	public List<UsuáriosCacheKey> getAll(Connection connection, DBOperationType tipoop)
	{
		List<UsuáriosCacheKey> keys = new ArrayList<UsuáriosCacheKey>();
		
		String query = "SELECT * FROM usuários_cache WHERE tipoop = ?";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			statement.setString(1, tipoop.toString());
			
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next())
			{
				int id = resultSet.getInt("idusuário");
				Timestamp timestamp = resultSet.getTimestamp("datamodificação");
				DBOperationType operationType = DBOperationType.fromString(resultSet.getString("tipoop"));
				
				keys.add(new UsuáriosCacheKey(id, timestamp, operationType));
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
	
	public HashMap<Integer, UsuáriosCacheKey> getAll(Connection connection)
	{
		HashMap<Integer, UsuáriosCacheKey> hashMap = new HashMap<Integer, UsuáriosCacheKey>();
		
		String query = "SELECT * FROM usuários_cache";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next())
			{
				int id = resultSet.getInt("idusuário");
				Timestamp timestamp = resultSet.getTimestamp("datamodificação");
				DBOperationType operationType = DBOperationType.fromString(resultSet.getString("tipoop"));
				
				hashMap.put(id, new UsuáriosCacheKey(id, timestamp, operationType));
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
	
	public boolean exists(UsuáriosCacheKey cacheKey, Connection connection)
	{
		String query = "SELECT * FROM usuários_cache WHERE idusuário = ?";
		
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
	
	public boolean deleteBatch(List<UsuáriosCacheKey> keys, Connection connection)
	{
		if (keys.size() == 0)
			return true;
		
		String query = "DELETE FROM usuários_cache WHERE idusuário in(";
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
	public boolean update(UsuáriosCacheKey object, Connection connection)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
