package com.davixavier.entidades.clientes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.davixavier.application.dbcache.ClientesCacheKey;
import com.davixavier.application.dbcache.ClientesCacheKeyDAO;
import com.davixavier.application.dbcache.EstoqueCacheKey;
import com.davixavier.database.ConnectionFactory;
import com.davixavier.database.DBOperationType;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.utils.AdvancedDAO;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.Utils;

public class ClienteDAO implements AdvancedDAO<Cliente, ClientesCacheKey>
{
	private static ClienteDAO instance;
	
	public ClienteDAO()
	{
	}
	
	public static ClienteDAO getInstance()
	{
		if (instance == null)
		{
			synchronized (ClienteDAO.class) 
			{
				if (instance == null)
				{
					instance = new ClienteDAO();
				}
			}
		}
		
		return instance;
	}
	
	public boolean insert(Cliente cliente, Connection connection) throws SQLException
	{
		String sql = "INSERT INTO clientes(id, cpf, nome, telefones, endereço, nomefantasia) "
				+ "VALUES "
				+ "("
				+ "?, ?, ?, ?, ?, ?"
				+ ") ON DUPLICATE KEY UPDATE cpf = ?, nome = ?, telefones = ?, endereço = ?, nomefantasia = ?;";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(sql);
			statement.setInt(1, cliente.getId());
			statement.setString(2, cliente.getCpfCnpj().toString());
			statement.setString(3, cliente.getNome());
			statement.setString(4, cliente.getTelefonesString());
			statement.setString(5, cliente.getEndereço().toString());
			statement.setString(6, cliente.getNomeFantasia());
			
			statement.setString(7, cliente.getCpfCnpj().toString());
			statement.setString(8, cliente.getNome());
			statement.setString(9, cliente.getTelefonesString());
			statement.setString(10, cliente.getEndereço().toString());
			statement.setString(11, cliente.getNomeFantasia());
			
			statement.execute();
			
			ClientesCacheKeyDAO.getInstance().insert(new ClientesCacheKey(cliente.getId(), Timestamp.valueOf(LocalDateTime.now()), DBOperationType.INSERT), 
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

	public boolean remove(Cliente cliente, Connection connection) throws SQLException
	{
		String sql = "DELETE FROM clientes WHERE id = ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(sql);
			statement.setInt(1, cliente.getId());
			statement.execute();
			
			ClientesCacheKeyDAO.getInstance().insert(new ClientesCacheKey(cliente.getId(), Timestamp.valueOf(LocalDateTime.now()), DBOperationType.DELETE), 
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
	
	public HashMap<Integer, Cliente> getAll(Connection connection) throws SQLException
	{
		HashMap<Integer, Cliente> hashMap = new HashMap<>();
		String query = "SELECT * FROM clientes";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				Cliente cliente = new Cliente();
				cliente.setId(resultSet.getInt("id"));
				cliente.setNome(resultSet.getString("nome"));
				cliente.setNomeFantasia(resultSet.getString("nomefantasia"));
				cliente.setCpfCnpj(IdentificaçãoFactory.identificávelFromString(resultSet.getString("cpf")));
				cliente.setEndereço(Endereço.fromString(resultSet.getString("endereço")));
				cliente.setTelefones(Cliente.telefonesFromString(resultSet.getString("telefones")));
				
				hashMap.put(cliente.getId(), cliente);
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
	
	public HashMap<Integer, Cliente> getAll() throws SQLException
	{
		HashMap<Integer, Cliente> hashMap = new HashMap<>();
		String query = "SELECT * FROM clientes";
		
		PreparedStatement statement = null;
		try
		{
			statement = ConnectionFactory.getConnection().prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				Cliente cliente = new Cliente();
				cliente.setId(resultSet.getInt("id"));
				cliente.setNome(resultSet.getString("nome"));
				cliente.setNomeFantasia(resultSet.getString("nomefantasia"));
				cliente.setCpfCnpj(IdentificaçãoFactory.identificávelFromString(resultSet.getString("cpf")));
				cliente.setEndereço(Endereço.fromString(resultSet.getString("endereço")));
				cliente.setTelefones(Cliente.telefonesFromString(resultSet.getString("telefones")));
				
				hashMap.put(cliente.getId(), cliente);
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
	
	public boolean update(Cliente cliente, Connection connection) throws SQLException
	{
		String sql = "UPDATE clientes "
				   + "SET nome = ?, endereço = ?, telefones = ?, cpf = ?, nomefantasia = ? "
				   + "WHERE id = ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(sql);
			statement.setString(1, cliente.getNome());
			statement.setString(2, cliente.getEndereço().toString());
			statement.setString(3, cliente.getTelefonesString());
			statement.setString(4, cliente.getCpfCnpj().toString());
			statement.setString(5, cliente.getNomeFantasia());
			statement.setInt(6, cliente.getId());
			
			statement.execute();
			
			ClientesCacheKeyDAO.getInstance().insert(new ClientesCacheKey(cliente.getId(), Timestamp.valueOf(LocalDateTime.now()), DBOperationType.UPDATE), 
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
	public boolean insertBatch(List<Cliente> list, Connection connection) throws SQLException
	{
		String query = "INSERT INTO clientes(id, cpf, nome, telefones, endereço, nomefantasia) "
				+ "VALUES "
				+ "("
				+ "?, ?, ?, ?, ?, ?"
				+ ") ON DUPLICATE KEY UPDATE cpf = ?, nome = ?, telefones = ?, endereço = ?, nomefantasia = ?;";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			for (Cliente cliente : list)
			{
				statement.setInt(1, cliente.getId());
				statement.setString(2, cliente.getCpfCnpj().toString());
				statement.setString(3, cliente.getNome());
				statement.setString(4, cliente.getTelefonesString());
				statement.setString(5, cliente.getEndereço().toString());
				statement.setString(6, cliente.getNomeFantasia());
				
				statement.setString(7, cliente.getCpfCnpj().toString());
				statement.setString(8, cliente.getNome());
				statement.setString(9, cliente.getTelefonesString());
				statement.setString(10, cliente.getEndereço().toString());
				statement.setString(11, cliente.getNomeFantasia());
				
				statement.addBatch();
			}
			
			if (list.size() > 0)
			{
				statement.executeBatch();
			}
			
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
	public List<Cliente> getAllList(Connection connection, List<ClientesCacheKey> keys)  throws SQLException
	{
		String query = "SELECT * FROM clientes WHERE id IN(";
		for (int i = 0; i < keys.size(); i++)
		{
			query += "?";
			if (i+1 < keys.size())
			{
				query += ",";
			}
		}
		query += ")";
		
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();
		if (keys.size() == 0)
			return clientes;
		
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
				Cliente cliente = new Cliente();
				cliente.setId(resultSet.getInt("id"));
				cliente.setNome(resultSet.getString("nome"));
				cliente.setNomeFantasia(resultSet.getString("nomefantasia"));
				cliente.setCpfCnpj(IdentificaçãoFactory.identificávelFromString(resultSet.getString("cpf")));
				cliente.setEndereço(Endereço.fromString(resultSet.getString("endereço")));
				cliente.setTelefones(Cliente.telefonesFromString(resultSet.getString("telefones")));
				
				clientes.add(cliente);
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
		
		return clientes;
	}

	@Override
	public List<Cliente> getAllList(Connection connection)  throws SQLException
	{
		List<Cliente> list = new ArrayList<Cliente>();
		String query = "SELECT * FROM clientes";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				Cliente cliente = new Cliente();
				cliente.setId(resultSet.getInt("id"));
				cliente.setNome(resultSet.getString("nome"));
				cliente.setNomeFantasia(resultSet.getString("nomefantasia"));
				cliente.setCpfCnpj(IdentificaçãoFactory.identificávelFromString(resultSet.getString("cpf")));
				cliente.setEndereço(Endereço.fromString(resultSet.getString("endereço")));
				cliente.setTelefones(Cliente.telefonesFromString(resultSet.getString("telefones")));
				
				list.add(cliente);
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
		
		return list;
	}

	@Override
	public boolean updateBatch(List<Cliente> list, Connection connection) throws SQLException
	{
		String query = "UPDATE clientes "
			     + "SET nome = ?, cpf = ?, endereço = ?, telefones = ?, id = ?, nomefantasia = ? "
			     + "WHERE id = ?;";
	
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			for(Cliente cliente : list)
			{
				statement.setString(1, cliente.getNome());
				statement.setString(2, cliente.getCpfCnpj().toString());
				statement.setString(3, cliente.getEndereço().toString());
				statement.setString(4, cliente.getTelefonesString());
				statement.setInt(5, cliente.getId());
				statement.setString(6, cliente.getNomeFantasia());
				
				statement.addBatch();
			}
			
			if (list.size() > 0)
			{
				statement.executeBatch();
			}
			
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
	public boolean removeBatch(List<Cliente> list, Connection connection) throws SQLException
	{
		String query = "DELETE FROM clientes WHERE id IN(";
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
	public Cliente get(int i, Connection connection) throws SQLException 
	{
		String query = "SELECT * FROM clientes WHERE id = ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, i);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			Cliente cliente = new Cliente();
			cliente.setId(resultSet.getInt("id"));
			cliente.setNome(resultSet.getString("nome"));
			cliente.setNomeFantasia(resultSet.getString("nomefantasia"));
			cliente.setCpfCnpj(IdentificaçãoFactory.identificávelFromString(resultSet.getString("cpf")));
			cliente.setEndereço(Endereço.fromString(resultSet.getString("endereço")));
			cliente.setTelefones(Cliente.telefonesFromString(resultSet.getString("telefones")));
			
			return cliente;
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
	public int getLastIndex(Connection connection) throws SQLException 
	{
		String query = "SELECT MAX(id) AS maxid FROM clientes";
		
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
	public boolean updateId(Cliente cliente, int newId, Connection connection) throws SQLException
	{
		String query = "UPDATE clientes SET id = ? WHERE id = ?";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, newId);
			statement.setInt(2, cliente.getId());
			
			statement.execute();
			
			ClientesCacheKeyDAO.getInstance().insert(new ClientesCacheKey(newId, Timestamp.valueOf(LocalDateTime.now()), DBOperationType.UPDATE), 
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
