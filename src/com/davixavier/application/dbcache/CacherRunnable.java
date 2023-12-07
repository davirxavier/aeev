package com.davixavier.application.dbcache;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import com.davixavier.application.logging.Logger;
import com.davixavier.database.ConnectionFactory;
import com.davixavier.database.DBOperationType;
import com.davixavier.entidades.estoque.ProdutoDAO;
import com.davixavier.utils.ExecuterServices;

//Nada na tabela de modifica��es online mas coisas na offline significam atualiza��o das tabelas online a partir das offline
//e vice-versa

//Cache vazio mas nada na tabela de modifica��es significa uma sincroniza��o total do online para o offline
//o contr�rio n�o � verdadeiro salvo quando a vari�vel no banco de dados de primeira vez de acesso for falsa

//
public class CacherRunnable implements Runnable
{	
	@Override
	public void run() 
	{
		Logger.getInstance().log("Thread de sincroniza��o inicializada.", Level.INFO);
		
		CacheService cacheService = new CacheService();
		cacheService.sync();
	}
}
