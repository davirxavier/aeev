package com.davixavier.database;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.davixavier.application.MainController;
import com.davixavier.application.dbcache.CacherRunnable;
import com.davixavier.application.logging.Logger;
import com.davixavier.entidades.clientes.Cliente;
import com.davixavier.entidades.clientes.ClienteDAO;
import com.davixavier.entidades.clientes.Endereço;
import com.davixavier.entidades.clientes.IdentificaçãoFactory;
import com.davixavier.entidades.clientes.Telefone;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.entidades.estoque.ProdutoDAO;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.Utils;
import com.davixavier.utils.pdf.EstoquePDF;
import com.davixavier.utils.pdf.ProdutoPDFTable;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;

public class ConnectionFactory
{
	private static final Logger LOGGER = Logger.getInstance();
	private static Connection connection;
	private static Connection onlineConnection;
	
	private static SimpleBooleanProperty offlineconnectionCreated = new SimpleBooleanProperty(false);
	private static SimpleBooleanProperty onlineConnectionCreated = new SimpleBooleanProperty(false);
	private static SimpleBooleanProperty connectionCreated = new SimpleBooleanProperty(false);
	
	public synchronized static Connection getOfflineConnection()
	{
		if (connection != null)
			return connection;
		
		MainController.setLoading(true);
		
		ExecuterServices.getExecutor().execute(() ->
		{
			try
			{
				LOGGER.log("Inicializando conexão ao db de cache...", Level.INFO);
				
				Class.forName("org.hsqldb.jdbc.JDBCDriver");
				Properties connectionProps = new Properties();
				connectionProps.put("username", "SA");
				connectionProps.put("password", "");
				
				connection = DriverManager.getConnection("jdbc:hsqldb:file:C:/AEEV/db/database", connectionProps);

				DBUtils.createTables(connection, OfflineDBTables.ESTOQUE_TABLE_CREATE_STATEMENT, 
						OfflineDBTables.VENDAS_TABLE_CREATE_STATEMENT, OfflineDBTables.USUARIOS_TABLE_CREATE_STATEMENT, 
						OfflineDBTables.CLIENTES_CREATE_TABLE_STATEMENT, OfflineDBTables.LOJAS_CREATE_TABLE_STATEMENT);
				
				connectionCreated.set(true);
				
				LOGGER.log("Conexão realizada com sucesso.", Level.INFO);
			} 
			catch (SQLException | ClassNotFoundException e)
			{
				LOGGER.log("Falha na conexão com o db de cache: \n" + Utils.getStackTraceString(e), Level.SEVERE);
			}
			finally 
			{
				//ExecuterServices.getCacheExecutor().scheduleWithFixedDelay(new CacherRunnable(), 0, 30, TimeUnit.SECONDS);
				MainController.setLoading(false);
			}
		});
		
		return connection;
	}
	
	public static Connection getOnlineConnection()
	{
		if (onlineConnection == null)
		{
			synchronized (ConnectionFactory.class) 
			{
				if (onlineConnection == null)
				{
					MainController.setLoading(true);
					
					try 
					{
						LOGGER.log("Inicializando conexão com o banco de dados online...", Level.INFO);
						
						DBConfig config = new DBConfig();
						config = ConfigManager.getConfigs();
						
						onlineConnection = DriverManager.getConnection(config.getUrl() + config.getDbName(), config.getProperties());
						
						DBUtils.createTables(onlineConnection, OnlineDBTables.ESTOQUE_TABLE_CREATE_STATEMENT, 
								OnlineDBTables.VENDAS_TABLE_CREATE_STATEMENT, OnlineDBTables.USUARIOS_TABLE_CREATE_STATEMENT, 
								OnlineDBTables.CLIENTES_CREATE_TABLE_STATEMENT, OnlineDBTables.LOJAS_CREATE_TABLE_STATEMENT);
						
						LOGGER.log("Conexão ao db online realizada com sucesso.", Level.INFO);
					}
					catch (FileNotFoundException | SQLException e) 
					{
						LOGGER.log("Falha na conexão ao db online: " + Utils.getStackTraceString(e), Level.WARNING);
					}
					finally 
					{
						MainController.setLoading(false);
					}
				}
			}
		}
		
		return onlineConnection;
	}
	
	public static Connection getConnection()
	{
		return getOfflineConnection();
	}
	
	public static void closeQuietly()
	{
		try
		{
			if (connection != null)
				connection.close();
			
			connectionCreated.set(false);
		} 
		catch (SQLException e) {}
	}
	
	public static boolean isConnectionCreated()
	{
		return connectionCreated.get();
	}
	
	public static SimpleBooleanProperty connectionCreatedProperty()
	{
		return connectionCreated;
	}
}
