package com.davixavier.autoupdate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import com.davixavier.application.Main;
import com.davixavier.utils.Utils;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import javafx.application.Platform;

public class VersionConfig
{
	private String configFilePath;
	
	public VersionConfig()
	{
		configFilePath = "https://www.dropbox.com/s/iwr7zx20401uyw0/versions.cfg?dl=1";
	}
	
	public String getLatestUpdateVersion()
	{
		try
		{
			Scanner scanner = new Scanner(new URL(configFilePath).openStream());
			String[] split = scanner.nextLine().split("\\;")[0].split("\\,");
			scanner.close();
			
			if (split.length > 0)
			{
				return split[0];
			}
			else 
			{
				return Main.CURRENTVERSION;
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			
			return Main.CURRENTVERSION;
		}
	}
	
	public String getLatestUpdateFile()
	{
		try
		{
			Scanner scanner = new Scanner(new URL(configFilePath).openStream());
			String[] split = scanner.nextLine().split("\\;")[0].split("\\,");
			scanner.close();
			
			if (split.length > 1)
			{
				return split[1];
			}
			else 
			{
				return "";
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			
			return "";
		}
	}
	
	public static int compareVersions(String version1, String version2)
	{
		String[] version1split = version1.split("\\.");
		String[] version2split = version2.split("\\.");
		
		if (version1split.length > version2split.length)
			return 1;
		else if (version2split.length > version1split.length)
			return -1;
		
		int ret = 0;
		
		int max = 0;
		if (version1split.length > version2split.length)
			max = version1split.length;
		else
			max = version2split.length;
		
		for (int i = 0; i < max; i++)
		{
			int v1 = Integer.parseInt(version1split[i]); 
			int v2 = Integer.parseInt(version2split[i]); 
			
			if (i < version1split.length && i < version2split.length)
			{
				ret += Integer.compare(v1, v2);
			}
		}
		
		return ret;
	}
	
	public void checkVersions()
	{
		if (compareVersions(getLatestUpdateVersion(), Main.CURRENTVERSION) > 0)
		{
			AutoUpdate autoUpdate = new AutoUpdate();
			autoUpdate.executeAutoUpdateJar();
			
			Platform.exit();
		}
	}
}
