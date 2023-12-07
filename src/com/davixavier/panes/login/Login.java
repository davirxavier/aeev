package com.davixavier.panes.login;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.http.ParseException;

import com.davixavier.application.MainController;
import com.davixavier.application.dbcache.CacherRunnable;
import com.davixavier.application.logging.Logger;
import com.davixavier.entidades.usuarios.Usuário;
import com.davixavier.entidades.usuarios.UsuárioDAO;
import com.davixavier.entidades.usuarios.UsuárioType;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.Utils;

import http.HttpConsts;
import http.client.HttpClientController;

public class Login
{
	public static final int DEFAULTUSERNUM = 1;
	
	private static Usuário usuárioAtual;
	
	private static final Logger LOGGER = Logger.getInstance();
	
	public static boolean realizarLogin(String username, String password) throws SQLException
	{
		Usuário usuário = new Usuário(username, Utils.getHashString(password), "", "");
		UsuárioDAO usuárioDAO = UsuárioDAO.getInstance();
		
		if (usuárioDAO.search(usuário))
		{
			usuárioAtual = usuárioDAO.getDefault();
			
			try 
			{
				LOGGER.log("Logado localmente, tentando login com serviço web...", Level.INFO);
				HttpClientController.getInstance().postLoginRequest(HttpConsts.REQUEST_URL + "/login", username, password);
				LOGGER.log("Logado no serviço web com sucesso.", Level.INFO);
				
				MainController.setLoading(true);
				ExecuterServices.getCacheExecutor().scheduleWithFixedDelay(new CacherRunnable(), 0, 30, TimeUnit.SECONDS);
			} 
			catch (ParseException | IOException e)
			{
				e.printStackTrace();
				
				Logger.getInstance().log("Login no serviço web sem sucesso.", Level.SEVERE);
			}
			
			return true;
		}
		
		return false;
	}
	
	public static boolean isAdmin()
	{
		if (usuárioAtual == null)
			return false;
		return usuárioAtual.getType().equals(UsuárioType.GERENTE.getString());
	}

	public static Usuário getUsuárioAtual()
	{
		return usuárioAtual;
	}
}
