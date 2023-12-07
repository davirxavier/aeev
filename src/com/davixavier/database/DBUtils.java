package com.davixavier.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.davixavier.application.LojaDAO;
import com.davixavier.application.dbcache.CacheKey;
import com.davixavier.application.dbcache.CacherRunnable;
import com.davixavier.application.dbcache.ClientesCacheKey;
import com.davixavier.application.dbcache.ClientesCacheKeyDAO;
import com.davixavier.application.dbcache.EstoqueCacheKey;
import com.davixavier.application.dbcache.EstoqueCacheKeyDAO;
import com.davixavier.application.dbcache.UsuáriosCacheKey;
import com.davixavier.application.dbcache.UsuáriosCacheKeyDAO;
import com.davixavier.application.dbcache.VendasCacheKey;
import com.davixavier.application.dbcache.VendasCacheKeyDAO;
import com.davixavier.entidades.clientes.Cliente;
import com.davixavier.entidades.clientes.ClienteDAO;
import com.davixavier.entidades.compras.VendaDAO;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.entidades.estoque.ProdutoDAO;
import com.davixavier.entidades.usuarios.Usuário;
import com.davixavier.entidades.usuarios.UsuárioDAO;
import com.davixavier.entidades.usuarios.UsuárioType;
import com.davixavier.utils.AdvancedDAO;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.Utils;

import javafx.application.Platform;

public abstract class DBUtils 
{
	private static final String ESTOQUE_CACHE_TABLE = 
			  "CREATE TABLE IF NOT EXISTS estoque_cache ("  
			+ "    idproduto INT,"  
			+ "    datamodificação TIMESTAMP,"
			+ "    tipoop VARCHAR(20)"
			//+ "    CONSTRAINT FK_estoque_cache FOREIGN KEY(idprodutomodificado) REFERENCES estoque (id)"    
			+ ");";
	
	private static final String CLIENTES_CACHE_TABLE =
			 "CREATE TABLE IF NOT EXISTS clientes_cache (" 
			+"    idcliente INT,"  
			+"    datamodificação TIMESTAMP,"
			+ "    tipoop VARCHAR(20)"
			//+ "   CONSTRAINT FK_clientes_cache FOREIGN KEY(idclientemodificado) REFERENCES clientes (id)"  
			+");";
	
	private static final String USUÁRIOS_CACHE_TABLE = 
			 "CREATE TABLE IF NOT EXISTS usuários_cache ("  
			+"    idusuário INT," 
			+"    datamodificação TIMESTAMP,"
			+ "    tipoop VARCHAR(20)"
			//+ "   CONSTRAINT FK_usuários_cache FOREIGN KEY (idusuáriomodificado) REFERENCES usuários (id)" 
			+");";
	
	private static final String VENDAS_CACHE_TABLE = 
			 "CREATE TABLE IF NOT EXISTS vendas_cache ("  
			+"    idvenda INT," 
			+"    datamodificação TIMESTAMP,"
			+ "    tipoop VARCHAR(20)"
			//+ "   CONSTRAINT FK_usuários_cache FOREIGN KEY (idusuáriomodificado) REFERENCES usuários (id)" 
			+");";
	
	public static void createTables(Connection connection, String estoqueCreate, 
			String vendasCreate, String usuariosCreate, String clientesCreate,
			String optionsCreate) throws SQLException
	{
		ArrayList<PreparedStatement> statements = new ArrayList<PreparedStatement>();
		
		boolean usuarioexists = DBUtils.tableExists(connection, null, "USUÁRIOS");
		boolean cacheexists = DBUtils.tableExists(connection, null, "estoque_cache");
		
		if (connection == ConnectionFactory.getOfflineConnection())
		{
			statements.add(connection.prepareStatement("SET DATABASE SQL SYNTAX MYS TRUE"));
		}
		
		//estoque
		statements.add(connection.prepareStatement(estoqueCreate));
		//statements.add(connection.prepareStatement(ESTOQUE_INDEX_STATEMENT));
		
		//vendas
		statements.add(connection.prepareStatement(vendasCreate));
		//statements.add(connection.prepareStatement(VENDAS_INDEX_STATEMENT));
		
		//usuários
		statements.add(connection.prepareStatement(usuariosCreate));
		//statements.add(connection.prepareStatement(USUARIOS_INDEX_STATEMENT));
		
		//clientes
		statements.add(connection.prepareStatement(clientesCreate));
		//statements.add(connection.prepareStatement(CLIENTES_INDEX_STATEMENT));
		
		//opções
		statements.add(connection.prepareStatement(optionsCreate));
		
		//caches
		statements.add(connection.prepareStatement(ESTOQUE_CACHE_TABLE));
		statements.add(connection.prepareStatement(CLIENTES_CACHE_TABLE));
		statements.add(connection.prepareStatement(USUÁRIOS_CACHE_TABLE));
		statements.add(connection.prepareStatement(VENDAS_CACHE_TABLE));
		
		for (PreparedStatement statement : statements)
		{
			statement.execute();
			statement.close();
		}
		
		statements.clear();
		
		//Modificações as tabelas
		modifyTables(connection);
		
		for (PreparedStatement statement : statements)
		{
			statement.execute();
			statement.close();
		}
		
		//Valores padrões de tabelas diversas depois daqui
		if (!usuarioexists)
		{
			UsuárioDAO.getInstance().insert(new Usuário("admin", Utils.getHashString("admin"), UsuárioType.GERENTE.getString(), ""), connection);
		}
		
		if (!cacheexists)
		{
			EstoqueCacheKeyDAO.getInstance().insert(new EstoqueCacheKey(-1, Timestamp.valueOf(LocalDateTime.now()), DBOperationType.UPDATE), connection);
			ClientesCacheKeyDAO.getInstance().insert(new ClientesCacheKey(-1, Timestamp.valueOf(LocalDateTime.now()), DBOperationType.UPDATE), connection);
			UsuáriosCacheKeyDAO.getInstance().insert(new UsuáriosCacheKey(-1, Timestamp.valueOf(LocalDateTime.now()), DBOperationType.UPDATE), connection);
			VendasCacheKeyDAO.getInstance().insert(new VendasCacheKey(-1, Timestamp.valueOf(LocalDateTime.now()), DBOperationType.UPDATE), connection);
		}
		
		updateAllZeroValues(connection);
		
		if (!LojaDAO.exists(connection))
			LojaDAO.insert(connection);
		
		LojaDAO.get(connection);
	}
	
	public static void updateAllZeroValues(Connection connection)
	{
		updateZeroValues(ProdutoDAO.getInstance(), connection);
		updateZeroValues(VendaDAO.getInstance(), connection);
		updateZeroValues(UsuárioDAO.getInstance(), connection);
		updateZeroValues(ClienteDAO.getInstance(), connection);
	}
	
	private static <T, S extends CacheKey> void updateZeroValues(AdvancedDAO<T, S> dao, Connection connection)
	{
		try 
		{
			T t = dao.get(0, connection);
			
			if (t != null)
			{
				int newId = dao.getLastIndex(connection)+1;
				
				dao.updateId(t, newId, connection);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	private static void modifyTables(Connection connection)
	{
		ArrayList<PreparedStatement> statements = new ArrayList<PreparedStatement>();
		
		//clientes
		try 
		{
			if (!DBUtils.columnExists(connection, "CLIENTES", "nomefantasia"))
			{
				statements.add(connection.prepareStatement("ALTER TABLE clientes "
							 + "ADD COLUMN nomefantasia VARCHAR(300)"));
			}
			
			if (!DBUtils.columnExists(connection, "ESTOQUE", "codigo"))
			{
				statements.add(connection.prepareStatement("ALTER TABLE estoque "
														 + "ADD COLUMN codigo INT"));
			}
			
			statements.add(connection.prepareStatement("ALTER TABLE estoque "
													 + "ALTER COLUMN "
													 + "id INT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1)"));
			
			statements.add(connection.prepareStatement("ALTER TABLE vendas "
					 								 + "ALTER COLUMN "
					 								 + "id INT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1)"));
			
			statements.add(connection.prepareStatement("ALTER TABLE clientes "
					 								 + "ALTER COLUMN "
					 								 + "id INT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1)"));

			statements.add(connection.prepareStatement("ALTER TABLE usuários "
					                                 + "ALTER COLUMN "
					                                 + "id INT GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1)"));
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		try 
		{
			for (PreparedStatement statement : statements)
			{
				statement.execute();
				statement.close();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static final boolean tableExists(Connection connection, String schema, String tablename) throws SQLException
	{
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.columns WHERE TABLE_NAME = ?");
		
		if (connection == ConnectionFactory.getOfflineConnection())
			statement.setString(1, tablename.toUpperCase());
		else
			statement.setString(1, tablename.toLowerCase());
		
		return statement.executeQuery().next();
	}
	
	public static final boolean columnExists(Connection connection, String tablename, String columnname) throws SQLException
	{
		if (connection == ConnectionFactory.getOfflineConnection())
		{
			return connection.getMetaData().getColumns(null, null, tablename.toUpperCase(), columnname.toUpperCase()).next();
		}
		else 
		{
			return connection.getMetaData().getColumns(null, null, tablename.toLowerCase(), columnname.toLowerCase()).next();
		}
	}
}
