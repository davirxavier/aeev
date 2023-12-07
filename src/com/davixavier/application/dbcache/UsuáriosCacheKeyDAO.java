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

public class Usu�riosCacheKeyDAO implements CacheDAO<Usu�riosCacheKey>
{
	private static Usu�riosCacheKeyDAO instance;
	
	public Usu�riosCacheKeyDAO() 
	{
	}
	
	public static Usu�riosCacheKeyDAO getInstance()
	{
		if (instance == null)
		{
			synchronized (Usu�riosCacheKeyDAO.class) 
			{
				if (instance == null)
				{
					instance = new Usu�riosCacheKeyDAO();
				}
			}
		}
		
		return instance;
	}
	
	public boolean insert(Usu�riosCacheKey cacheKey, Connection connection)
	{
		String query = "INSERT INTO usu�rios_cache(idusu�rio, datamodifica��o, tipoop) "
				     + "VALUES(?, ?, ?)";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, cacheKey.getIdModificado());
			statement.setTimestamp(2, cacheKey.getDatamodifica��o());
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
	
	public boolean remove(Usu�riosCacheKey cacheKey, Connection connection)
	{
		String query = "DELETE FROM usu�rios_cache WHERE idusu�rio = ?";
		
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
	
	public List<Usu�riosCacheKey> getAll(Connection connection, DBOperationType tipoop)
	{
		List<Usu�riosCacheKey> keys = new ArrayList<Usu�riosCacheKey>();
		
		String query = "SELECT * FROM usu�rios_cache WHERE tipoop = ?";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			statement.setString(1, tipoop.toString());
			
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next())
			{
				int id = resultSet.getInt("idusu�rio");
				Timestamp timestamp = resultSet.getTimestamp("datamodifica��o");
				DBOperationType operationType = DBOperationType.fromString(resultSet.getString("tipoop"));
				
				keys.add(new Usu�riosCacheKey(id, timestamp, operationType));
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
	
	public HashMap<Integer, Usu�riosCacheKey> getAll(Connection connection)
	{
		HashMap<Integer, Usu�riosCacheKey> hashMap = new HashMap<Integer, Usu�riosCacheKey>();
		
		String query = "SELECT * FROM usu�rios_cache";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next())
			{
				int id = resultSet.getInt("idusu�rio");
				Timestamp timestamp = resultSet.getTimestamp("datamodifica��o");
				DBOperationType operationType = DBOperationType.fromString(resultSet.getString("tipoop"));
				
				hashMap.put(id, new Usu�riosCacheKey(id, timestamp, operationType));
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
	
	public boolean exists(Usu�riosCacheKey cacheKey, Connection connection)
	{
		String query = "SELECT * FROM usu�rios_cache WHERE idusu�rio = ?";
		
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
	
	public boolean deleteBatch(List<Usu�riosCacheKey> keys, Connection connection)
	{
		if (keys.size() == 0)
			return true;
		
		String query = "DELETE FROM usu�rios_cache WHERE idusu�rio in(";
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
	public boolean update(Usu�riosCacheKey object, Connection connection)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
