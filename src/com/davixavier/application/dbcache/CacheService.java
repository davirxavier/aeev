package com.davixavier.application.dbcache;

import java.sql.Connection;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.json.JSONArray;
import org.json.JSONObject;

import com.davixavier.application.MainController;
import com.davixavier.application.logging.Logger;
import com.davixavier.database.ConnectionFactory;
import com.davixavier.database.DBOperationType;
import com.davixavier.database.DBUtils;
import com.davixavier.entidades.clientes.ClienteDAO;
import com.davixavier.entidades.compras.VendaDAO;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.entidades.estoque.ProdutoDAO;
import com.davixavier.entidades.usuarios.UsuárioDAO;
import com.davixavier.utils.AdvancedDAO;
import com.davixavier.utils.CacheDAO;
import com.davixavier.utils.DAO;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.JSONEncoder;
import com.davixavier.utils.Utils;

import http.GenericDAO;
import http.JSONClienteHandler;
import http.JSONProdutoHandler;
import http.JSONUsuárioHandler;
import http.JSONVendaHandler;

public class CacheService 
{
	private static boolean first = true;
	private static final Logger LOGGER = Logger.getInstance();
	private ProdutoDAO produtoDAO;
	
	public void sync()
	{
//		synchronized (ConnectionFactory.getOnlineConnection()) 
//		{
//			if (ConnectionFactory.getOnlineConnection() == null)
//			{
//				return;
//			}
//		}
		
		produtoDAO = ProdutoDAO.getInstance();
		
		LOGGER.log("Começando sincronização...", Level.INFO);
		syncTable(ProdutoDAO.getInstance(), EstoqueCacheKeyDAO.getInstance(), JSONProdutoHandler.getInstance(), "estoque");
		syncTable(ClienteDAO.getInstance(), ClientesCacheKeyDAO.getInstance(), JSONClienteHandler.getInstance(), "clientes");
		syncTable(UsuárioDAO.getInstance(), UsuáriosCacheKeyDAO.getInstance(), JSONUsuárioHandler.getInstance(), "usuarios");
		syncTable(VendaDAO.getInstance(), VendasCacheKeyDAO.getInstance(), JSONVendaHandler.getInstance(), "vendas");
		
		MainController.updateModules();
		
		LOGGER.log("Sincronização completa concluída.", Level.INFO);
	}
	
	private <T, S extends CacheKey> void syncTable(AdvancedDAO<T, S> dataDAO, CacheDAO<S> cacheDAO, JSONEncoder<T> encoder, String nome)
	{
		boolean error = false;
		
		LOGGER.log("Checando tabela de " + nome + "...", Level.INFO);
		
		try 
		{
			LOGGER.log("Sincronizando chaves de inserção...", Level.INFO);
			
			List<S> insertKeys = cacheDAO.getAll(ConnectionFactory.getOfflineConnection(), 
					DBOperationType.INSERT);
			
			if (insertKeys.size() > 0)
			{
				GenericDAO.insertBatch(encoder.encode(dataDAO.getAllList(ConnectionFactory.getOfflineConnection(), insertKeys)), nome);
				cacheDAO.deleteBatch(insertKeys, ConnectionFactory.getOfflineConnection());
			}
			
			LOGGER.log("Sincronização de inserção concluída.", Level.INFO);
			
		} 
		catch (Exception e) 
		{
			LOGGER.log(Utils.getStackTraceString(e), Level.SEVERE);
			error = true;
		}
	
		try 
		{
			LOGGER.log("Sincronizando chaves de atualização...", Level.INFO);
			
			List<S> updateKeys = cacheDAO.getAll(ConnectionFactory.getOfflineConnection(), 
					DBOperationType.UPDATE);
			
			for (S key : updateKeys)
			{
				if (true)//ConnectionFactory.getOnlineConnection() != null)
				{
					if (key.getIdModificado() == -1)
					{
						LOGGER.log("Achada chave inicializadora, sincronizando " + nome + " online à partir do cache...", Level.INFO);
						
						GenericDAO.insertBatch(encoder.encode(dataDAO.getAllList(ConnectionFactory.getOfflineConnection())), nome);
						//cacheDAO.remove(key, ConnectionFactory.getOnlineConnection());
						cacheDAO.remove(key, ConnectionFactory.getOfflineConnection());
						
						LOGGER.log("Sincronização de " + nome + " inicial terminada.", Level.INFO);
					}
				}
			}
			
			if (updateKeys.size() > 0)
			{
				GenericDAO.updateBatch(encoder.encode(dataDAO.getAllList(ConnectionFactory.getOfflineConnection(), updateKeys)), nome);
				cacheDAO.deleteBatch(updateKeys, 
						ConnectionFactory.getOfflineConnection());
			}
			
			LOGGER.log("Sincronização de atualização concluída.", Level.INFO);
				
		} 
		catch (Exception e)
		{
			LOGGER.log(Utils.getStackTraceString(e), Level.SEVERE);
			error = true;
		}
		
		try
		{
			LOGGER.log("Sincronizando chaves de deleção...", Level.INFO);
			
			List<S> deletionKeys = cacheDAO.getAll(ConnectionFactory.getOfflineConnection(), 
					DBOperationType.DELETE);
			if (deletionKeys.size() > 0)
			{
				JSONObject idsObject = new JSONObject();
				JSONArray idsJsonArray = new JSONArray();
				deletionKeys.forEach(d ->
				{
					idsJsonArray.put(d.getIdModificado());
				});
				idsObject.put("delete", idsJsonArray);
				
				GenericDAO.delete(idsObject, nome);
				cacheDAO.deleteBatch(deletionKeys, ConnectionFactory.getOfflineConnection());
			}
			
			LOGGER.log("Sincronização de deleção concluída.", Level.INFO);
			 
		} catch (Exception e) 
		{
			LOGGER.log(Utils.getStackTraceString(e), Level.SEVERE);
			error = true;
		}
			
		
		try 
		{
			if (!error)
			{
				LOGGER.log("Sincronizando cache a partir do db online...", Level.INFO);
				
				List<T> onList = encoder.decode(GenericDAO.getAll(nome));
				List<T> offList = dataDAO.getAllList(ConnectionFactory.getOfflineConnection());
				List<T> toRemove = new ArrayList<T>();
				
				offList.forEach(p ->
				{
					if (!onList.contains(p))
					{
						toRemove.add(p);
					}
				});
				dataDAO.removeBatch(toRemove, ConnectionFactory.getOfflineConnection());
				
				dataDAO.insertBatch(onList, ConnectionFactory.getOfflineConnection());
				
				LOGGER.log("Sincronização do cache de " + nome + " completa.", Level.INFO);
			}
		} 
		catch (Exception e) 
		{
			LOGGER.log(Utils.getStackTraceString(e), Level.SEVERE);
			error = true;
		}
		
		if (error)
		{
			LOGGER.log("Erro detectado, fase de sincronização de cache pulada.", Level.INFO);
		}
		
		if (first)
		{
			first = false;
			MainController.setLoading(false);
		}
	}
}
