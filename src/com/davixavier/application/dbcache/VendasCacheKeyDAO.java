package com.davixavier.application.dbcache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.davixavier.database.DBOperationType;
import com.davixavier.utils.CacheDAO;
import com.davixavier.utils.Utils;

public class VendasCacheKeyDAO implements CacheDAO<VendasCacheKey>
{
	private static VendasCacheKeyDAO instance;
	
	public VendasCacheKeyDAO()
	{
	}
	
	public static VendasCacheKeyDAO getInstance()
	{
		if (instance == null)
		{
			synchronized (VendasCacheKeyDAO.class) 
			{
				if (instance == null)
				{
					instance = new VendasCacheKeyDAO();
				}
			}
		}
		
		return instance;
	}
	
	public boolean insert(VendasCacheKey cacheKey, Connection connection)
	{
		String query = "INSERT INTO vendas_cache(idvenda, datamodificação, tipoop) "
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
	
	public boolean remove(VendasCacheKey cacheKey, Connection connection)
	{
		String query = "DELETE FROM vendas_cache WHERE idvenda = ?";
		
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
	
	public List<VendasCacheKey> getAll(Connection connection, DBOperationType tipoop)
	{
		List<VendasCacheKey> keys = new ArrayList<VendasCacheKey>();
		
		String query = "SELECT * FROM vendas_cache WHERE tipoop = ?";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			statement.setString(1, tipoop.toString());
			
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next())
			{
				int id = resultSet.getInt("idvenda");
				Timestamp timestamp = resultSet.getTimestamp("datamodificação");
				DBOperationType operationType = DBOperationType.fromString(resultSet.getString("tipoop"));
				
				keys.add(new VendasCacheKey(id, timestamp, operationType));
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
	
	public HashMap<Integer, VendasCacheKey> getAll(Connection connection)
	{
		HashMap<Integer, VendasCacheKey> hashMap = new HashMap<Integer, VendasCacheKey>();
		
		String query = "SELECT * FROM vendas_cache";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next())
			{
				int id = resultSet.getInt("idvenda");
				Timestamp timestamp = resultSet.getTimestamp("datamodificação");
				DBOperationType operationType = DBOperationType.fromString(resultSet.getString("tipoop"));
				
				hashMap.put(id, new VendasCacheKey(id, timestamp, operationType));
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
	
	public boolean exists(VendasCacheKey cacheKey, Connection connection)
	{
		String query = "SELECT * FROM vendas_cache WHERE idvenda = ?";
		
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
	
	public boolean deleteBatch(List<VendasCacheKey> keys, Connection connection)
	{
		if (keys.size() == 0)
			return true;
		
		String query = "DELETE FROM vendas_cache WHERE idvenda in(";
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
	public boolean update(VendasCacheKey object, Connection connection)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
