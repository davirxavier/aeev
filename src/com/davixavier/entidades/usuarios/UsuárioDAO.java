package com.davixavier.entidades.usuarios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.davixavier.application.dbcache.Usu�riosCacheKey;
import com.davixavier.application.dbcache.Usu�riosCacheKeyDAO;
import com.davixavier.database.ConnectionFactory;
import com.davixavier.database.DBOperationType;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.panes.login.Login;
import com.davixavier.utils.AdvancedDAO;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.Utils;

public class Usu�rioDAO implements AdvancedDAO<Usu�rio, Usu�riosCacheKey>
{
	private static Usu�rioDAO instance;
	
	public Usu�rioDAO() 
	{
	}
	
	public static Usu�rioDAO getInstance()
	{
		if (instance == null)
		{
			synchronized (Usu�rioDAO.class) 
			{
				if (instance == null)
				{
					instance = new Usu�rioDAO();
				}
			}
		}
		
		return instance;
	}
	
	public boolean insert(Usu�rio usu�rio, Connection connection) throws SQLException
	{
		String query = "INSERT INTO usu�rios (id, username, password, email, type) VALUES(?, ?, ?, ?, ?) "
			     + "ON DUPLICATE KEY UPDATE username = ?, password = ?, email = ?, type = ?;";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, usu�rio.getId());
			statement.setString(2, usu�rio.getUsername());
			statement.setString(3, usu�rio.getSenhaHash());
			statement.setString(4, usu�rio.getEmail());
			statement.setString(5, usu�rio.getType());
			
			statement.setString(6, usu�rio.getUsername());
			statement.setString(7, usu�rio.getSenhaHash());
			statement.setString(8, usu�rio.getEmail());
			statement.setString(9, usu�rio.getType());
			
			statement.execute();
			
			Usu�riosCacheKeyDAO.getInstance().insert(new Usu�riosCacheKey(usu�rio.getId(), Timestamp.valueOf(LocalDateTime.now()), DBOperationType.INSERT), 
					connection);
			ExecuterServices.requestSync();
			
			return true;
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new SQLException(e);
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}
	
	@Override
	public boolean insertBatch(List<Usu�rio> list, Connection connection) throws SQLException
	{
		String query = "INSERT INTO usu�rios(id, username, password, email, type) VALUES(?, ?, ?, ?, ?) "
				     + "ON DUPLICATE KEY UPDATE username = ?, password = ?, email = ?, type = ?;";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			for (Usu�rio usu�rio : list)
			{
				statement.setInt(1, usu�rio.getId());
				statement.setString(2, usu�rio.getUsername());
				statement.setString(3, usu�rio.getSenhaHash());
				statement.setString(4, usu�rio.getEmail());
				statement.setString(5, usu�rio.getType());
				;
				statement.setString(6, usu�rio.getUsername());
				statement.setString(7, usu�rio.getSenhaHash());
				statement.setString(8, usu�rio.getEmail());
				statement.setString(9, usu�rio.getType());
				
				statement.addBatch();
			}
			
			if (list.size() > 0)
				statement.executeBatch();
			
			return true;
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			
			throw new SQLException(e);
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}
	
	public boolean remove(Usu�rio usu�rio, Connection connection) throws SQLException
	{
		String query = "DELETE FROM usu�rios WHERE id = ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, usu�rio.getId());
			
			statement.execute();
			
			Usu�riosCacheKeyDAO.getInstance().insert(new Usu�riosCacheKey(usu�rio.getId(), Timestamp.valueOf(LocalDateTime.now()), DBOperationType.DELETE), 
					connection);
			ExecuterServices.requestSync();
			
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new SQLException(e);
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}

	public boolean update(Usu�rio usu�rio, Connection connection) throws SQLException
	{
		String query = "UPDATE usu�rios "
				     + "SET username = ?, password = ?, email = ?, type = ? "
				     + "WHERE id = ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setString(1, usu�rio.getUsername());
			statement.setString(2, usu�rio.getSenhaHash());
			statement.setString(3, usu�rio.getEmail());
			statement.setString(4, usu�rio.getType());
			statement.setInt(5, usu�rio.getId());
			
			statement.execute();
			
			Usu�riosCacheKeyDAO.getInstance().insert(new Usu�riosCacheKey(usu�rio.getId(), Timestamp.valueOf(LocalDateTime.now()), DBOperationType.UPDATE), 
					connection);
			ExecuterServices.requestSync();
			
			return true;
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new SQLException(e);
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}
	
	public Usu�rio get(int id, Connection connection) throws SQLException
	{
		String query = "SELECT * FROM usu�rios WHERE id = ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			Usu�rio usu�rio = new Usu�rio();
			usu�rio.setUsername(resultSet.getString("username"));
			usu�rio.setSenhaHash(resultSet.getString("password"));
			usu�rio.setEmail(resultSet.getString("email"));
			usu�rio.setType(resultSet.getString("type"));
			usu�rio.setId(resultSet.getInt("id"));
			
			return usu�rio;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			
			throw new SQLException(e);
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}
	
	public Usu�rio getDefault() throws SQLException
	{
		String query = "SELECT * FROM usu�rios WHERE id = ?";
		
		int id = Login.DEFAULTUSERNUM;
		
		PreparedStatement statement = null;
		try
		{
			statement = ConnectionFactory.getConnection().prepareStatement(query);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			
			Usu�rio usu�rio = new Usu�rio();
			usu�rio.setUsername(resultSet.getString("username"));
			usu�rio.setSenhaHash(resultSet.getString("password"));
			usu�rio.setEmail(resultSet.getString("email"));
			usu�rio.setType(resultSet.getString("type"));
			usu�rio.setId(resultSet.getInt("id"));
			
			return usu�rio;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			
			throw new SQLException(e);
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}
	
	public HashMap<String, Usu�rio> getAll(Connection connection) throws SQLException
	{
		String query = "SELECT * FROM usu�rios";
		HashMap<String, Usu�rio> hashMap = new HashMap<>();
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				Usu�rio usu�rio = new Usu�rio();
				usu�rio.setUsername(resultSet.getString("username"));
				usu�rio.setSenhaHash(resultSet.getString("password"));
				usu�rio.setEmail(resultSet.getString("email"));
				usu�rio.setType(resultSet.getString("type"));
				usu�rio.setId(resultSet.getInt("id"));
				
				hashMap.put(usu�rio.getUsername(), usu�rio);
			}
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			
			throw new SQLException(e);
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
		
		return hashMap;
	}
	
	public boolean search(Usu�rio usu�rio) throws SQLException
	{
		String query = "SELECT * FROM usu�rios WHERE username = ? AND password = ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = ConnectionFactory.getConnection().prepareStatement(query);
			statement.setString(1, usu�rio.getUsername());
			statement.setString(2, usu�rio.getSenhaHash());
			
			ResultSet resultSet = statement.executeQuery();
			
			if (resultSet.next())
				return true;
			else
				return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new SQLException(e);
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}
	
	@Override
	public List<Usu�rio> getAllList(Connection connection, List<Usu�riosCacheKey> keys) throws SQLException
	{
		String query = "SELECT * FROM usu�rios WHERE id IN(";
		for (int i = 0; i < keys.size(); i++)
		{
			query += "?";
			if (i+1 < keys.size())
			{
				query += ",";
			}
		}
		query += ")";
		
		ArrayList<Usu�rio> usu�rios = new ArrayList<Usu�rio>();
		if (keys.size() == 0)
			return usu�rios;
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			for (int i = 0; i < keys.size(); i++)
			{
				statement.setInt(i+1, keys.get(i).getIdModificado());
			}
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				Usu�rio usu�rio = new Usu�rio();
				usu�rio.setUsername(resultSet.getString("username"));
				usu�rio.setSenhaHash(resultSet.getString("password"));
				usu�rio.setEmail(resultSet.getString("email"));
				usu�rio.setType(resultSet.getString("type"));
				usu�rio.setId(resultSet.getInt("id"));
				
				usu�rios.add(usu�rio);
			}
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			
			throw new SQLException(e);
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
		
		return usu�rios;
	}

	@Override
	public List<Usu�rio> getAllList(Connection connection) throws SQLException
	{
		String query = "SELECT * FROM usu�rios";
		
		ArrayList<Usu�rio> usu�rios = new ArrayList<Usu�rio>();
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				Usu�rio usu�rio = new Usu�rio();
				usu�rio.setUsername(resultSet.getString("username"));
				usu�rio.setSenhaHash(resultSet.getString("password"));
				usu�rio.setEmail(resultSet.getString("email"));
				usu�rio.setType(resultSet.getString("type"));
				usu�rio.setId(resultSet.getInt("id"));
				
				usu�rios.add(usu�rio);
			}
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new SQLException(e);
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
		
		return usu�rios;
	}

	@Override
	public boolean updateBatch(List<Usu�rio> list, Connection connection) throws SQLException
	{
		String query = "UPDATE usu�rios SET id = ?, username = ?, password = ?, email = ?, type = ? "
					 + "WHERE id = ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			for (Usu�rio usu�rio : list)
			{
				statement.setInt(1, usu�rio.getId());
				statement.setString(2, usu�rio.getUsername());
				statement.setString(3, usu�rio.getSenhaHash());
				statement.setString(4, usu�rio.getEmail());
				statement.setString(5, usu�rio.getType());
				
				statement.addBatch();
			}
			
			if (list.size() > 0)
				statement.executeBatch();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new SQLException(e);
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
		
		return false;
	}

	@Override
	public boolean removeBatch(List<Usu�rio> list, Connection connection)  throws SQLException
	{
		String query = "DELETE FROM usu�rios WHERE id IN(";
		for (int i = 0; i < list.size(); i++)
		{
			query += "?";
			
			if (i+1 < list.size())
			{
				query += ",";
			}
		}
		query += ")";
		
		PreparedStatement statement = null;
		try 
		{
			if (list.size() == 0)
				return true;
			
			statement = connection.prepareStatement(query);
			for (int i = 0; i < list.size(); i++)
			{
				statement.setInt(i+1, list.get(i).getId());
			}
			
			if (list.size() > 0)
				statement.execute();
			
			return true;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			throw new SQLException(e);
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}

	@Override
	public int getLastIndex(Connection connection) throws SQLException 
	{
		String query = "SELECT MAX(id) AS maxid FROM usu�rios";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			
			return resultSet.getInt("maxid");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new SQLException(e);
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}

	@Override
	public boolean updateId(Usu�rio usu�rio, int newId, Connection connection) throws SQLException 
	{
		String query = "UPDATE usu�rios SET id = ? WHERE id = ?";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, newId);
			statement.setInt(2, usu�rio.getId());
			
			statement.execute();
			
			Usu�riosCacheKeyDAO.getInstance().insert(new Usu�riosCacheKey(newId, Timestamp.valueOf(LocalDateTime.now()), DBOperationType.UPDATE), 
					connection);
			ExecuterServices.requestSync();
			
			return true;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new SQLException(e);
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}
}
