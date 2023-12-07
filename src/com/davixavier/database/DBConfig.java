package com.davixavier.database;

import java.util.Properties;

public class DBConfig
{
	private String url;
	private String username;
	private String password;
	private String dbName;
	
	public DBConfig()
	{
		url = "";
		username = "";
		password = "";
		dbName = "";
	}
	
	@Override
	public String toString()
	{
		return "url=" + url + ";username=" + username + ";password=" + password + ";dbname=" + dbName;
	}
	
	public Properties getProperties()
	{
		Properties properties = new Properties();
		
		properties.put("user", username);
		properties.put("password", password);
		
		return properties;
	}
	
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getDbName()
	{
		return dbName;
	}
	public void setDbName(String dbName)
	{
		this.dbName = dbName;
	}
}
