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

import com.davixavier.application.dbcache.UsuáriosCacheKey;
import com.davixavier.application.dbcache.UsuáriosCacheKeyDAO;
import com.davixavier.database.ConnectionFactory;
import com.davixavier.database.DBOperationType;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.panes.login.Login;
import com.davixavier.utils.AdvancedDAO;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.Utils;

public class UsuárioDAO implements AdvancedDAO<Usuário, UsuáriosCacheKey>
{
	private static UsuárioDAO instance;
	
	public UsuárioDAO() 
	{
	}
	
	public static UsuárioDAO getInstance()
	{
		if (instance == null)
		{
			synchronized (UsuárioDAO.class) 
			{
				if (instance == null)
				{
					instance = new UsuárioDAO();
				}
			}
		}
		
		return instance;
	}
	
	public boolean insert(Usuário usuário, Connection connection) throws SQLException
	{
		String query = "INSERT INTO usuários (id, username, password, email, type) VALUES(?, ?, ?, ?, ?) "
			     + "ON DUPLICATE KEY UPDATE username = ?, password = ?, email = ?, type = ?;";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, usuário.getId());
			statement.setString(2, usuário.getUsername());
			statement.setString(3, usuário.getSenhaHash());
			statement.setString(4, usuário.getEmail());
			statement.setString(5, usuário.getType());
			
			statement.setString(6, usuário.getUsername());
			statement.setString(7, usuário.getSenhaHash());
			statement.setString(8, usuário.getEmail());
			statement.setString(9, usuário.getType());
			
			statement.execute();
			
			UsuáriosCacheKeyDAO.getInstance().insert(new UsuáriosCacheKey(usuário.getId(), Timestamp.valueOf(LocalDateTime.now()), DBOperationType.INSERT), 
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
	public boolean insertBatch(List<Usuário> list, Connection connection) throws SQLException
	{
		String query = "INSERT INTO usuários(id, username, password, email, type) VALUES(?, ?, ?, ?, ?) "
				     + "ON DUPLICATE KEY UPDATE username = ?, password = ?, email = ?, type = ?;";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			for (Usuário usuário : list)
			{
				statement.setInt(1, usuário.getId());
				statement.setString(2, usuário.getUsername());
				statement.setString(3, usuário.getSenhaHash());
				statement.setString(4, usuário.getEmail());
				statement.setString(5, usuário.getType());
				;
				statement.setString(6, usuário.getUsername());
				statement.setString(7, usuário.getSenhaHash());
				statement.setString(8, usuário.getEmail());
				statement.setString(9, usuário.getType());
				
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
	
	public boolean remove(Usuário usuário, Connection connection) throws SQLException
	{
		String query = "DELETE FROM usuários WHERE id = ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, usuário.getId());
			
			statement.execute();
			
			UsuáriosCacheKeyDAO.getInstance().insert(new UsuáriosCacheKey(usuário.getId(), Timestamp.valueOf(LocalDateTime.now()), DBOperationType.DELETE), 
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

	public boolean update(Usuário usuário, Connection connection) throws SQLException
	{
		String query = "UPDATE usuários "
				     + "SET username = ?, password = ?, email = ?, type = ? "
				     + "WHERE id = ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setString(1, usuário.getUsername());
			statement.setString(2, usuário.getSenhaHash());
			statement.setString(3, usuário.getEmail());
			statement.setString(4, usuário.getType());
			statement.setInt(5, usuário.getId());
			
			statement.execute();
			
			UsuáriosCacheKeyDAO.getInstance().insert(new UsuáriosCacheKey(usuário.getId(), Timestamp.valueOf(LocalDateTime.now()), DBOperationType.UPDATE), 
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
	
	public Usuário get(int id, Connection connection) throws SQLException
	{
		String query = "SELECT * FROM usuários WHERE id = ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			Usuário usuário = new Usuário();
			usuário.setUsername(resultSet.getString("username"));
			usuário.setSenhaHash(resultSet.getString("password"));
			usuário.setEmail(resultSet.getString("email"));
			usuário.setType(resultSet.getString("type"));
			usuário.setId(resultSet.getInt("id"));
			
			return usuário;
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
	
	public Usuário getDefault() throws SQLException
	{
		String query = "SELECT * FROM usuários WHERE id = ?";
		
		int id = Login.DEFAULTUSERNUM;
		
		PreparedStatement statement = null;
		try
		{
			statement = ConnectionFactory.getConnection().prepareStatement(query);
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			resultSet.next();
			
			Usuário usuário = new Usuário();
			usuário.setUsername(resultSet.getString("username"));
			usuário.setSenhaHash(resultSet.getString("password"));
			usuário.setEmail(resultSet.getString("email"));
			usuário.setType(resultSet.getString("type"));
			usuário.setId(resultSet.getInt("id"));
			
			return usuário;
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
	
	public HashMap<String, Usuário> getAll(Connection connection) throws SQLException
	{
		String query = "SELECT * FROM usuários";
		HashMap<String, Usuário> hashMap = new HashMap<>();
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				Usuário usuário = new Usuário();
				usuário.setUsername(resultSet.getString("username"));
				usuário.setSenhaHash(resultSet.getString("password"));
				usuário.setEmail(resultSet.getString("email"));
				usuário.setType(resultSet.getString("type"));
				usuário.setId(resultSet.getInt("id"));
				
				hashMap.put(usuário.getUsername(), usuário);
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
	
	public boolean search(Usuário usuário) throws SQLException
	{
		String query = "SELECT * FROM usuários WHERE username = ? AND password = ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = ConnectionFactory.getConnection().prepareStatement(query);
			statement.setString(1, usuário.getUsername());
			statement.setString(2, usuário.getSenhaHash());
			
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
	public List<Usuário> getAllList(Connection connection, List<UsuáriosCacheKey> keys) throws SQLException
	{
		String query = "SELECT * FROM usuários WHERE id IN(";
		for (int i = 0; i < keys.size(); i++)
		{
			query += "?";
			if (i+1 < keys.size())
			{
				query += ",";
			}
		}
		query += ")";
		
		ArrayList<Usuário> usuários = new ArrayList<Usuário>();
		if (keys.size() == 0)
			return usuários;
		
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
				Usuário usuário = new Usuário();
				usuário.setUsername(resultSet.getString("username"));
				usuário.setSenhaHash(resultSet.getString("password"));
				usuário.setEmail(resultSet.getString("email"));
				usuário.setType(resultSet.getString("type"));
				usuário.setId(resultSet.getInt("id"));
				
				usuários.add(usuário);
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
		
		return usuários;
	}

	@Override
	public List<Usuário> getAllList(Connection connection) throws SQLException
	{
		String query = "SELECT * FROM usuários";
		
		ArrayList<Usuário> usuários = new ArrayList<Usuário>();
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				Usuário usuário = new Usuário();
				usuário.setUsername(resultSet.getString("username"));
				usuário.setSenhaHash(resultSet.getString("password"));
				usuário.setEmail(resultSet.getString("email"));
				usuário.setType(resultSet.getString("type"));
				usuário.setId(resultSet.getInt("id"));
				
				usuários.add(usuário);
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
		
		return usuários;
	}

	@Override
	public boolean updateBatch(List<Usuário> list, Connection connection) throws SQLException
	{
		String query = "UPDATE usuários SET id = ?, username = ?, password = ?, email = ?, type = ? "
					 + "WHERE id = ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			for (Usuário usuário : list)
			{
				statement.setInt(1, usuário.getId());
				statement.setString(2, usuário.getUsername());
				statement.setString(3, usuário.getSenhaHash());
				statement.setString(4, usuário.getEmail());
				statement.setString(5, usuário.getType());
				
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
	public boolean removeBatch(List<Usuário> list, Connection connection)  throws SQLException
	{
		String query = "DELETE FROM usuários WHERE id IN(";
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
		String query = "SELECT MAX(id) AS maxid FROM usuários";
		
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
	public boolean updateId(Usuário usuário, int newId, Connection connection) throws SQLException 
	{
		String query = "UPDATE usuários SET id = ? WHERE id = ?";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, newId);
			statement.setInt(2, usuário.getId());
			
			statement.execute();
			
			UsuáriosCacheKeyDAO.getInstance().insert(new UsuáriosCacheKey(newId, Timestamp.valueOf(LocalDateTime.now()), DBOperationType.UPDATE), 
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
