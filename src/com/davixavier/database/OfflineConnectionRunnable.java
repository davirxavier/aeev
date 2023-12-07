package com.davixavier.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.davixavier.application.dbcache.CacherRunnable;
import com.davixavier.application.logging.Logger;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.Utils;

import javafx.beans.property.SimpleBooleanProperty;

public class OfflineConnectionRunnable implements Runnable
{
	private static final Logger LOGGER = Logger.getInstance();
	private Connection connection;
	private SimpleBooleanProperty connectionCreated;
	
	public OfflineConnectionRunnable(Connection connection, SimpleBooleanProperty connectionCreated) 
	{
		this.connection = connection;
		this.connectionCreated = connectionCreated;
	}
	
	@Override
	public void run() 
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
			ExecuterServices.getCacheExecutor().scheduleWithFixedDelay(new CacherRunnable(), 0, 20, TimeUnit.SECONDS);
			//MainController.setLoading(false);
		}
		
	}

}
