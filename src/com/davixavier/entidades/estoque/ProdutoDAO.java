package com.davixavier.entidades.estoque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.davixavier.application.dbcache.EstoqueCacheKey;
import com.davixavier.application.dbcache.EstoqueCacheKeyDAO;
import com.davixavier.database.ConnectionFactory;
import com.davixavier.database.DBOperationType;
import com.davixavier.utils.AdvancedDAO;
import com.davixavier.utils.DAO;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.Utils;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

public class ProdutoDAO implements AdvancedDAO<Produto, EstoqueCacheKey>
{
	private static ProdutoDAO instance;
	private EstoqueCacheKeyDAO estoqueCacheKeyDAO;
	
	private ProdutoDAO() 
	{
		estoqueCacheKeyDAO = EstoqueCacheKeyDAO.getInstance();
	}
	
	public static ProdutoDAO getInstance()
	{
		if (instance == null)
		{
			synchronized (ProdutoDAO.class) 
			{
				if (instance == null)
				{
					instance = new ProdutoDAO();
				}
			}
		}
		
		return instance;
	}
	
	public boolean insertBatch(List<Produto> produtos, Connection connection) throws SQLException
	{
		String query = "INSERT INTO estoque(id, nome, preço, preço_compra, quantidade, codigo) "
				+ "VALUES "
				+ "("
				+ "?, ?, ?, ?, ?, ?"
				+ ") ON DUPLICATE KEY UPDATE nome = ?, preço = ?, preço_compra = ?, quantidade = ?, codigo = ?;";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			for (Produto produto : produtos)
			{
				if (produto.getId() == -1)
				{
					statement.setNull(1, Types.INTEGER);
				}
				else 
				{
					statement.setInt(1, produto.getId());
				}

				statement.setString(2, produto.getNome());
				statement.setDouble(3, produto.getPreço());
				statement.setDouble(4, produto.getPreçoCompra());
				statement.setInt(5, produto.getQuantidade());
				statement.setInt(6, produto.getCodigo());
				
				statement.setString(7, produto.getNome());
				statement.setDouble(8, produto.getPreço());
				statement.setDouble(9, produto.getPreçoCompra());
				statement.setInt(10, produto.getQuantidade());
				statement.setInt(11, produto.getCodigo());
				
				statement.addBatch();
			}
			
			if (produtos.size() > 0)
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
	
	public boolean insert(Produto produto, Connection connection) throws SQLException
	{
		String query = "INSERT INTO estoque(nome, preço, preço_compra, quantidade, codigo) "
				+ "VALUES "
				+ "("
				+ "?, ?, ?, ?, ?"
				+ ")";// ON DUPLICATE KEY UPDATE nome = ?, preço = ?, preço_compra = ?, quantidade = ?, codigo = ?;";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			/*
			if (produto.getId() == -1)
			{
				statement.setNull(1, Types.INTEGER);
			}
			else 
			{
				statement.setInt(1, produto.getId());
			}
			
			if (produto.getId() == 0)
			{
				produto.setId(getLastIndex(connection)+1);
			}*/
			
			statement.setString(1, produto.getNome());
			statement.setDouble(2, produto.getPreço());
			statement.setDouble(3, produto.getPreçoCompra());
			statement.setInt(4, produto.getQuantidade());
			statement.setInt(5, produto.getCodigo());
			
			statement.execute();
			
			estoqueCacheKeyDAO.insert(new EstoqueCacheKey(getLastIndex(connection), Timestamp.valueOf(LocalDateTime.now()), DBOperationType.INSERT), 
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
	
	public boolean existsByCod(Produto produto, Connection connection) throws SQLException
	{
		if(produto.getCodigo() == 0)
		{
			return get(produto.getId(), connection) != null;
		}
		else 
		{
			return getByCod(produto.getCodigo(), connection) != null;
		}
	}
	
	public Produto getByCod(int cod, Connection connection) throws SQLException
	{
		String query = "SELECT * FROM estoque WHERE codigo = ?";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, cod);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			Produto produto = new Produto();
			produto.setId(resultSet.getInt("id"));
			produto.setNome(resultSet.getString("nome"));
			produto.setPreço(resultSet.getDouble("preço"));
			produto.setPreçoCompra(resultSet.getDouble("preço_compra"));
			produto.setQuantidade(resultSet.getInt("quantidade"));
			
			if (resultSet.getInt("codigo") == 0)
				produto.setCodigo(produto.getId());
			else
				produto.setCodigo(resultSet.getInt("codigo"));
			
			return produto;
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
	
	public boolean exists(Produto produto, Connection connection) throws SQLException
	{
		return get(produto.getId(), connection) != null;
	}
	
	public Produto get(int id, Connection connection) throws SQLException
	{
		String query = "SELECT * FROM estoque WHERE id = ?";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			
			ResultSet resultSet = statement.executeQuery();
			if (!resultSet.next())
				return null;
			
			Produto produto = new Produto();
			produto.setId(id);
			produto.setNome(resultSet.getString("nome"));
			produto.setPreço(resultSet.getDouble("preço"));
			produto.setPreçoCompra(resultSet.getDouble("preço_compra"));
			produto.setQuantidade(resultSet.getInt("quantidade"));
			
			if (resultSet.getInt("codigo") == 0)
				produto.setCodigo(produto.getId());
			else
				produto.setCodigo(resultSet.getInt("codigo"));
			
			return produto;
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
	
	public boolean removeBatch(List<Produto> produtos, Connection connection) throws SQLException
	{
		String query = "DELETE FROM estoque WHERE id IN(";
		for (int i = 0; i < produtos.size(); i++)
		{
			query += "?";
			
			if (i+1 < produtos.size())
			{
				query += ",";
			}
		}
		query += ")";
		
		PreparedStatement statement = null;
		try 
		{
			if (produtos.size() == 0)
				return true;
			
			statement = connection.prepareStatement(query);
			for (int i = 0; i < produtos.size(); i++)
			{
				statement.setInt(i+1, produtos.get(i).getId());
			}
			
			if (produtos.size() > 0)
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
	
	public boolean removeAll(Connection connection)  throws SQLException
	{
		String query = "DELETE FROM estoque";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
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
	
	public boolean remove(Produto produto, Connection connection)  throws SQLException
	{
		String query = "DELETE FROM estoque WHERE id = " + produto.getId();
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			statement.execute();
			
			estoqueCacheKeyDAO.insert(new EstoqueCacheKey(produto.getId(), Timestamp.valueOf(LocalDateTime.now()), DBOperationType.DELETE), 
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
	
	public HashMap<Integer, Produto> getAll(Connection connection) throws SQLException
	{
		String query = "SELECT * FROM estoque";
		HashMap<Integer, Produto> hashMap = new HashMap<Integer, Produto>();
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				Produto produto = new Produto();
				produto.setId(resultSet.getInt("id"));
				produto.setNome(resultSet.getString("nome"));
				produto.setPreço(resultSet.getDouble("preço"));
				produto.setPreçoCompra(resultSet.getDouble("preço_compra"));
				produto.setQuantidade(resultSet.getInt("quantidade"));
				
				if (resultSet.getInt("codigo") == 0)
					produto.setCodigo(produto.getId());
				else
					produto.setCodigo(resultSet.getInt("codigo"));
				
				hashMap.put(produto.getId(), produto);
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
	
	public List<Produto> getAllList(Connection connection, List<EstoqueCacheKey> keys) throws SQLException
	{
		String query = "SELECT * FROM estoque WHERE id IN(";
		for (int i = 0; i < keys.size(); i++)
		{
			query += "?";
			if (i+1 < keys.size())
			{
				query += ",";
			}
		}
		query += ")";
		
		ArrayList<Produto> produtos = new ArrayList<Produto>();
		if (keys.size() == 0)
			return produtos;
		
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
				Produto produto = new Produto();
				produto.setId(resultSet.getInt("id"));
				produto.setNome(resultSet.getString("nome"));
				produto.setPreço(resultSet.getDouble("preço"));
				produto.setPreçoCompra(resultSet.getDouble("preço_compra"));
				produto.setQuantidade(resultSet.getInt("quantidade"));

				if (resultSet.getInt("codigo") == 0)
					produto.setCodigo(produto.getId());
				else
					produto.setCodigo(resultSet.getInt("codigo"));
				
				produtos.add(produto);
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
		
		return produtos;
	}
	
	public ArrayList<Produto> getAllList(Connection connection) throws SQLException
	{
		String query = "SELECT * FROM estoque";
		ArrayList<Produto> produtos = new ArrayList<Produto>();
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next())
			{
				Produto produto = new Produto();
				produto.setId(resultSet.getInt("id"));
				produto.setNome(resultSet.getString("nome"));
				produto.setPreço(resultSet.getDouble("preço"));
				produto.setPreçoCompra(resultSet.getDouble("preço_compra"));
				produto.setQuantidade(resultSet.getInt("quantidade"));

				if (resultSet.getInt("codigo") == 0)
					produto.setCodigo(produto.getId());
				else
					produto.setCodigo(resultSet.getInt("codigo"));
				
				produtos.add(produto);
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
		
		return produtos;
	}
	
	public boolean updateWithId(Produto produto, int oldId, Connection connection) throws SQLException
	{
		String query = "UPDATE estoque "
				     + "SET nome = ?, preço = ?, preço_compra = ?, quantidade = ?, codigo = ?, id = ? "
				     + "WHERE id = ?;";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setString(1, produto.getNome());
			statement.setDouble(2, produto.getPreço());
			statement.setDouble(3, produto.getPreçoCompra());
			statement.setInt(4, produto.getQuantidade());
			statement.setInt(5, produto.getCodigo());
			
			if (produto.getId() == -1 || oldId == -1)
			{
				statement.setNull(6, Types.INTEGER);
				statement.setInt(7, produto.getId());
			}
			else 
			{
				statement.setInt(6, produto.getId());
				
				if (oldId != -1)
				{
					statement.setInt(7, oldId);
				}
				else 
				{
					statement.setInt(7, produto.getId());
				}
			}
			
			statement.execute();
			
			estoqueCacheKeyDAO.insert(new EstoqueCacheKey(produto.getId(), Timestamp.valueOf(LocalDateTime.now()), DBOperationType.UPDATE), 
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
	
	public boolean updateBatch(List<Produto> produtos, Connection connection) throws SQLException
	{
		String query = "UPDATE estoque "
				     + "SET nome = ?, preço = ?, preço_compra = ?, quantidade = ?, id = ?, codigo = ? "
				     + "WHERE id = ?;";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			for(Produto produto : produtos)
			{
				statement.setString(1, produto.getNome());
				statement.setDouble(2, produto.getPreço());
				statement.setDouble(3, produto.getPreçoCompra());
				statement.setInt(4, produto.getQuantidade());
				statement.setInt(5, produto.getId());
				statement.setInt(6, produto.getCodigo());
				statement.setInt(7, produto.getId());
				
				statement.addBatch();
			}
			
			if (produtos.size() > 0)
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
	
	public boolean update(Produto produto, Connection connection) throws SQLException
	{
		String query = "UPDATE estoque "
				     + "SET nome = ?, preço = ?, preço_compra = ?, quantidade = ?, codigo = ? "
				     + "WHERE id = ?;";
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			statement.setString(1, produto.getNome());
			statement.setDouble(2, produto.getPreço());
			statement.setDouble(3, produto.getPreçoCompra());
			statement.setInt(4, produto.getQuantidade());
			statement.setInt(5, produto.getCodigo());
			statement.setInt(6, produto.getId());
			
			statement.execute();
			
			estoqueCacheKeyDAO.insert(new EstoqueCacheKey(produto.getId(), Timestamp.valueOf(LocalDateTime.now()), DBOperationType.UPDATE), 
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
	
	public int getLastId(Connection connection) throws SQLException
	{
		String query = "SELECT * FROM estoque";
		
		int ret = 0;
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next())
			{
				if (resultSet.getInt("id") > ret)
					ret = resultSet.getInt("id");
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
		
		return ret;
	}

	@Override
	public int getLastIndex(Connection connection) throws SQLException 
	{
		String query = "SELECT MAX(id) AS maxid FROM estoque";
		
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
	public boolean updateId(Produto produto, int newId, Connection connection) throws SQLException 
	{
		String query = "UPDATE estoque SET id = ? WHERE id = ?";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, newId);
			statement.setInt(2, produto.getId());
			
			statement.execute();
			
			estoqueCacheKeyDAO.insert(new EstoqueCacheKey(newId, Timestamp.valueOf(LocalDateTime.now()), DBOperationType.UPDATE), 
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
