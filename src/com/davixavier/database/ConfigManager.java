package com.davixavier.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ConfigManager
{
	private static File configFile;
	
	static
	{
		try
		{
			setFile("cfg/dbconfig.cfg");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void setFile(String path) throws IOException
	{
		configFile = new File(path);
		
		if (!configFile.exists())
		{
			configFile.createNewFile();
			
			FileOutputStream outputStream = new FileOutputStream(configFile);
			String configString = "url=\nusername=\npassword=\ndbname=";
			outputStream.write(configString.getBytes());
			
			outputStream.close();
		}
	}
	
	public static DBConfig getConfigs() throws FileNotFoundException
	{
		DBConfig config = new DBConfig();
		Scanner scanner = new Scanner(configFile);
		
		for (int i = 0; i < 4; i++)
		{
			String[] line = scanner.nextLine().split("\\=");
			if (line.length == 1)
				continue;
			
			if (line[0].contains("url"))
			{
				config.setUrl(line[1].replace(" ", ""));
			}
			else if(line[0].contains("username"))
			{
				config.setUsername(line[1].replace(" ", ""));
			}
			else if(line[0].contains("password"))
			{
				config.setPassword(line[1].replace(" ", ""));
			}
			else if(line[0].contains("dbname"))
			{
				config.setDbName(line[1].replace(" ", ""));
			}
		}
		
		scanner.close();
		return config;
	}
}
