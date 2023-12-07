package com.davixavier.panes.login;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.http.ParseException;

import com.davixavier.application.MainController;
import com.davixavier.application.dbcache.CacherRunnable;
import com.davixavier.application.logging.Logger;
import com.davixavier.entidades.usuarios.Usu�rio;
import com.davixavier.entidades.usuarios.Usu�rioDAO;
import com.davixavier.entidades.usuarios.Usu�rioType;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.Utils;

import http.HttpConsts;
import http.client.HttpClientController;

public class Login
{
	public static final int DEFAULTUSERNUM = 1;
	
	private static Usu�rio usu�rioAtual;
	
	private static final Logger LOGGER = Logger.getInstance();
	
	public static boolean realizarLogin(String username, String password) throws SQLException
	{
		Usu�rio usu�rio = new Usu�rio(username, Utils.getHashString(password), "", "");
		Usu�rioDAO usu�rioDAO = Usu�rioDAO.getInstance();
		
		if (usu�rioDAO.search(usu�rio))
		{
			usu�rioAtual = usu�rioDAO.getDefault();
			
			try 
			{
				LOGGER.log("Logado localmente, tentando login com servi�o web...", Level.INFO);
				HttpClientController.getInstance().postLoginRequest(HttpConsts.REQUEST_URL + "/login", username, password);
				LOGGER.log("Logado no servi�o web com sucesso.", Level.INFO);
				
				MainController.setLoading(true);
				ExecuterServices.getCacheExecutor().scheduleWithFixedDelay(new CacherRunnable(), 0, 30, TimeUnit.SECONDS);
			} 
			catch (ParseException | IOException e)
			{
				e.printStackTrace();
				
				Logger.getInstance().log("Login no servi�o web sem sucesso.", Level.SEVERE);
			}
			
			return true;
		}
		
		return false;
	}
	
	public static boolean isAdmin()
	{
		if (usu�rioAtual == null)
			return false;
		return usu�rioAtual.getType().equals(Usu�rioType.GERENTE.getString());
	}

	public static Usu�rio getUsu�rioAtual()
	{
		return usu�rioAtual;
	}
}
