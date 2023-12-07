package com.davixavier.autoupdate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.davixavier.application.Main;
import com.davixavier.utils.ExecuterServices;

import javafx.application.Platform;

public class AutoUpdate
{
	private File programJar;
	
	public AutoUpdate()
	{
		String[] jars = System.getProperty("java.class.path").split("\\;");
		programJar = new File(jars[0]);
	}
	
	public void executeAutoUpdateJar()
	{
		Runtime runtime = Runtime.getRuntime();
		try
		{
			Process process = runtime.exec("java -jar update.jar");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void executeProgramJar()
	{
		Runtime runtime = Runtime.getRuntime();
		try
		{
			if (new File("aeev.exe").exists())
			{
				Process process = runtime.exec("aeev.exe");
			}
			else 
			{
				Process process = runtime.exec("java -jar aeev.jar");
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void update()
	{
		VersionConfig versionConfig = new VersionConfig();
		
		if (VersionConfig.compareVersions(versionConfig.getLatestUpdateVersion(), Main.CURRENTVERSION) > 0)
		{
			downloadJar();
			
			File newJar = new File("C:" + File.separatorChar + "AEEV" + File.separatorChar + "aeev.jar");
			File jar = new File("aeev.jar");
			
			if (jar.exists())
			{
				try
				{
					FileInputStream inputStream = new FileInputStream(newJar);
					Files.copy(inputStream, Paths.get(jar.toURI()), StandardCopyOption.REPLACE_EXISTING);
					
					executeProgramJar();
					Platform.exit();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void downloadJar()
	{
		VersionConfig versionConfig = new VersionConfig();
		
		File jar = new File("C:" + File.separatorChar + "AEEV" + File.separatorChar + "aeev.jar");
			
		try
		{
			InputStream in = new URL(versionConfig.getLatestUpdateFile()).openStream();
			Files.copy(in, Paths.get(jar.toURI()), StandardCopyOption.REPLACE_EXISTING);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
