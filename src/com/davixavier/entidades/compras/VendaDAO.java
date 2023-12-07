package com.davixavier.entidades.compras;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.davixavier.application.dbcache.EstoqueCacheKey;
import com.davixavier.application.dbcache.EstoqueCacheKeyDAO;
import com.davixavier.application.dbcache.VendasCacheKey;
import com.davixavier.application.dbcache.VendasCacheKeyDAO;
import com.davixavier.database.ConnectionFactory;
import com.davixavier.database.DBOperationType;
import com.davixavier.entidades.clientes.Cliente;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.utils.AdvancedDAO;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.Utils;

public class VendaDAO implements AdvancedDAO<Venda, VendasCacheKey>
{
	private static VendaDAO instance;
	
	private VendaDAO()
	{
	}
	
	public static VendaDAO getInstance()
	{
		if (instance == null)
		{
			synchronized (VendaDAO.class) 
			{
				if (instance == null)
				{
					instance = new VendaDAO();
				}
			}
		}
		
		return instance;
	}
	
	public boolean insert(Venda venda, Connection connection) throws SQLException
	{
		String query = "INSERT INTO vendas(id, data, preço, descrição, produtos, cliente) "
				+ "VALUES (?, ?, ?, ?, ?, ?) "
				+ "ON DUPLICATE KEY UPDATE data = ?, preço = ?, descrição = ?, produtos = ?, cliente = ?;";
		
		PreparedStatement statement = null;
		PreparedStatement nextidStatement = null;
		PreparedStatement produtosStatement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			statement.setInt(1, venda.getId());
			statement.setTimestamp(2, venda.getData());
			statement.setDouble(3, venda.getPreço());
			statement.setString(4, venda.getDescrição());
			
			String produtos = "";
			for (ProdutoVenda p : venda.getProdutos())
			{
				produtos += p.toString() + ";";
			}
			statement.setString(5, produtos);
			
			String cliente = "";
			if (venda.getCliente() != null)
			{
				cliente = venda.getCliente().toString();
			}
			statement.setString(6, cliente);
			
			statement.setTimestamp(7, venda.getData());
			statement.setDouble(8, venda.getPreço());
			statement.setString(9, venda.getDescrição());
			statement.setString(10, produtos);
			statement.setString(11, cliente);
			
			statement.execute();
			
			VendasCacheKeyDAO.getInstance().insert(new VendasCacheKey(venda.getId(), Timestamp.valueOf(LocalDateTime.now()), DBOperationType.INSERT), 
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
			Utils.closeQuietly(nextidStatement);
			Utils.closeQuietly(produtosStatement);
		}
	}
	
	public Venda get(int id, Connection connection) throws SQLException
	{
		String query = "SELECT * FROM vendas WHERE id = ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			Venda venda = new Venda();
			
			venda.setId(resultSet.getInt("id"));
			venda.setData(resultSet.getTimestamp("data"));
			venda.setDescrição(resultSet.getString("descrição"));
			venda.setProdutos(ProdutoVenda.parseArray(resultSet.getString("produtos").split("\\;")));
			venda.setCliente(Cliente.fromString(resultSet.getString("cliente")));
			
			return venda;
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
	
	public HashMap<Integer, Venda> getBetweenDates(Timestamp start, Timestamp end, Connection connection) throws SQLException
	{
		HashMap<Integer, Venda> hashMap = new HashMap<Integer, Venda>();
		
		String query = "SELECT * FROM vendas WHERE data >= ? AND data <= ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setDate(1, new Date(start.getTime()));
			statement.setDate(2, new Date(end.getTime()));
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				Venda venda = new Venda();
				venda.setId(resultSet.getInt("id"));
				venda.setPreço(resultSet.getDouble("preço"));
				venda.setData(resultSet.getTimestamp("data"));
				venda.setDescrição(resultSet.getString("descrição"));
				venda.setProdutos(ProdutoVenda.parseArray(resultSet.getString("produtos").split("\\;")));
				venda.setCliente(Cliente.fromString(resultSet.getString("cliente")));
				
				hashMap.put(venda.getId(), venda);
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
	
	public HashMap<Integer, Venda> getAll(Connection connection) throws SQLException
	{
		HashMap<Integer, Venda> hashMap = new HashMap<>();
		
		String query = "SELECT * FROM vendas";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				Venda venda = new Venda();
				venda.setId(resultSet.getInt("id"));
				venda.setPreço(resultSet.getDouble("preço"));
				venda.setData(resultSet.getTimestamp("data"));
				venda.setDescrição(resultSet.getString("descrição"));
				venda.setProdutos(ProdutoVenda.parseArray(resultSet.getString("produtos").split("\\;")));
				venda.setCliente(Cliente.fromString(resultSet.getString("cliente")));
				
				hashMap.put(venda.getId(), venda);
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
	
	public boolean remove(Venda venda, Connection connection) throws SQLException
	{
		String query = "DELETE FROM vendas WHERE id = ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, venda.getId());
			statement.execute();
			
			VendasCacheKeyDAO.getInstance().insert(new VendasCacheKey(venda.getId(), Timestamp.valueOf(LocalDateTime.now()), DBOperationType.DELETE), 
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
	
	public boolean update(Venda venda, Connection connection) throws SQLException
	{
		String query = "UPDATE vendas "
					 + "SET preço = ?, descrição = ?, data = ?, produtos = ?, cliente = ? "
					 + "WHERE id = ?";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setDouble(1, venda.getPreço());
			statement.setString(2, venda.getDescrição());
			statement.setTimestamp(3, venda.getData());
			
			String produtos = "";
			for (ProdutoVenda p : venda.getProdutos())
			{
				produtos += p.toString() + ";";
			}
			statement.setString(4, produtos);
			statement.setInt(5, venda.getId());
			statement.setString(6, venda.getCliente().toString());
			
			statement.execute();
			
			VendasCacheKeyDAO.getInstance().insert(new VendasCacheKey(venda.getId(), Timestamp.valueOf(LocalDateTime.now()), DBOperationType.UPDATE), 
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
	public boolean insertBatch(List<Venda> list, Connection connection) throws SQLException
	{
		String query = "INSERT INTO vendas(id, data, preço, descrição, produtos, cliente) "
				+ "VALUES "
				+ "("
				+ "?, ?, ?, ?, ?, ?"
				+ ") ON DUPLICATE KEY UPDATE data = ?, preço = ?, descrição = ?, produtos = ?, cliente = ?;";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			for (Venda venda : list)
			{
				statement.setInt(1, venda.getId());
				statement.setTimestamp(2, venda.getData());
				statement.setDouble(3, venda.getPreço());
				statement.setString(4, venda.getDescrição());
				
				String produtos = "";
				for (ProdutoVenda p : venda.getProdutos())
				{
					produtos += p.toString() + ";";
				}
				statement.setString(5, produtos);
				
				String cliente = "";
				if (venda.getCliente() != null)
				{
					cliente = venda.getCliente().toString();
				}
				statement.setString(6, cliente);
				
				statement.setTimestamp(7, venda.getData());
				statement.setDouble(8, venda.getPreço());
				statement.setString(9, venda.getDescrição());
				statement.setString(10, produtos);
				statement.setString(11, cliente);
				
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
	public List<Venda> getAllList(Connection connection, List<VendasCacheKey> keys)  throws SQLException
	{
		String query = "SELECT * FROM vendas WHERE id IN(";
		for (int i = 0; i < keys.size(); i++)
		{
			query += "?";
			if (i+1 < keys.size())
			{
				query += ",";
			}
		}
		query += ")";
		
		ArrayList<Venda> vendas = new ArrayList<Venda>();
		if (keys.size() == 0)
			return vendas;
		
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
				Venda venda = new Venda();
				venda.setId(resultSet.getInt("id"));
				venda.setPreço(resultSet.getDouble("preço"));
				venda.setData(resultSet.getTimestamp("data"));
				venda.setDescrição(resultSet.getString("descrição"));
				venda.setProdutos(ProdutoVenda.parseArray(resultSet.getString("produtos").split("\\;")));
				venda.setCliente(Cliente.fromString(resultSet.getString("cliente")));
				
				vendas.add(venda);
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
		
		return vendas;
	}

	@Override
	public List<Venda> getAllList(Connection connection)  throws SQLException
	{
		List<Venda> vendas = new ArrayList<Venda>();
		
		String query = "SELECT * FROM vendas";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next())
			{
				Venda venda = new Venda();
				venda.setId(resultSet.getInt("id"));
				venda.setPreço(resultSet.getDouble("preço"));
				venda.setData(resultSet.getTimestamp("data"));
				venda.setDescrição(resultSet.getString("descrição"));
				venda.setProdutos(ProdutoVenda.parseArray(resultSet.getString("produtos").split("\\;")));
				venda.setCliente(Cliente.fromString(resultSet.getString("cliente")));
				
				vendas.add(venda);
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
		
		return vendas;
	}

	@Override
	public boolean updateBatch(List<Venda> list, Connection connection) throws SQLException
	{
		String query = "UPDATE vendas "
				 + "SET preço = ?, descrição = ?, data = ?, produtos = ?, cliente = ? "
				 + "WHERE id = ?";
	
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			for(Venda venda : list)
			{
				statement = connection.prepareStatement(query);
				statement.setDouble(1, venda.getPreço());
				statement.setString(2, venda.getDescrição());
				statement.setTimestamp(3, venda.getData());
				
				String produtos = "";
				for (ProdutoVenda p : venda.getProdutos())
				{
					produtos += p.toString() + ";";
				}
				statement.setString(4, produtos);
				statement.setInt(5, venda.getId());
				statement.setString(6, venda.getCliente().toString());
				
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
	public boolean removeBatch(List<Venda> list, Connection connection) throws SQLException
	{
		String query = "DELETE FROM vendas WHERE id IN(";
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
		String query = "SELECT MAX(id) AS maxid FROM vendas";
		
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
	public boolean updateId(Venda venda, int newId, Connection connection) throws SQLException
	{
		String query = "UPDATE vendas SET id = ? WHERE id = ?";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, newId);
			statement.setInt(2, venda.getId());
			
			statement.execute();
			
			VendasCacheKeyDAO.getInstance().insert(new VendasCacheKey(newId, Timestamp.valueOf(LocalDateTime.now()), DBOperationType.UPDATE), 
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
