package com.davixavier.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class OnlineDBTables 
{
	public static final String ESTOQUE_TABLE_CREATE_STATEMENT = 
			"CREATE TABLE IF NOT EXISTS estoque"
			+ "("
			+ "id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
			+ "nome VARCHAR(300) NOT NULL,"
			+ "preço DECIMAL(10, 2),"
			+ "preço_compra DECIMAL(10, 2),"
			+ "quantidade INT,"
			+ "codigo INT"
			+ ");";
	
	public static final String ESTOQUE_INDEX_STATEMENT = "CREATE INDEX IF NOT EXISTS idx_estoque_id ON estoque(id);";
	public static final String USUARIOS_TABLE_CREATE_STATEMENT = 
			"CREATE TABLE IF NOT EXISTS usuários"
		  + "("
		  + "id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
		  + "username VARCHAR(100) NOT NULL,"
		  + "password VARCHAR(255),"
		  + "email VARCHAR(100),"
		  + "type VARCHAR(20)"
		  + ");";
	public static final String USUARIOS_INDEX_STATEMENT = "CREATE INDEX IF NOT EXISTS idx_usuarios_username ON usuários(username)";
	public static final String VENDAS_TABLE_CREATE_STATEMENT = 
			"CREATE TABLE IF NOT EXISTS vendas"
			+ "("
			+ "id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
			+ "data DATETIME,"
			+ "preço DECIMAL(10,2),"
			+ "descrição VARCHAR(300),"
			+ "produtos VARCHAR(5050),"
			+ "cliente VARCHAR(5050)"
			+ ");";
	public static final String VENDAS_INDEX_STATEMENT = "CREATE INDEX IF NOT EXISTS idx_vendas_id ON vendas(id)";
	
	public static final String CLIENTES_CREATE_TABLE_STATEMENT = 
			"CREATE TABLE IF NOT EXISTS clientes"
			+ "("
			+ "id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,"
			+ "cpf VARCHAR(20),"
			+ "nome VARCHAR(300),"
			+ "telefones VARCHAR(1000),"
			+ "endereço VARCHAR(1000)"
			+ ");";
	public static final String CLIENTES_INDEX_STATEMENT = "CREATE INDEX IF NOT EXISTS idx_clientes_id ON clientes(id)";
	
	public static final String LOJAS_CREATE_TABLE_STATEMENT = 
			"CREATE TABLE IF NOT EXISTS lojas"
			+ "("
			+ "idusuário INT,"
			+ "userimage BLOB, "
			+ "nomeloja VARCHAR(300),"
			+ "telefone1 VARCHAR(30),"
			+ "telefone2 VARCHAR(30)"
			//+ "CONSTRAINT FK_usuário_loja FOREIGN KEY(idusuário) REFERENCES usuários (id)"
			+ ");";
}
