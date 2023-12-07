package com.davixavier.application.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

import com.davixavier.utils.Utils;

public class Logger 
{
	private FileWriter writer;
	private File file;
	
	private static Logger instance;
	
	private static LogEntry lastEntry;
	
	private Logger()
	{
		file = new File("log.txt");
		
		lastEntry = new LogEntry();
	}
	
	public static Logger getInstance()
	{
		if (instance == null)
		{
			synchronized (Logger.class) 
			{
				if (instance == null)
				{
					instance = new Logger();
				}
			}
		}
		
		return instance;
	}
	
	public void open() throws IOException
	{
		if (file.exists())
		{
			file.delete();
		}
		file.createNewFile();
		
		writer = new FileWriter(file);
	}
	
	public void log(String text, Level level)
	{
		LogEntry logEntry = new LogEntry();
		logEntry.setClassName(Utils.getCallerCallerClassName());
		logEntry.setMethodName(Utils.getCallerMethodName(3));
		logEntry.setEntry(text);
		logEntry.setDateTime(LocalDateTime.now());
		logEntry.setLevel(level);
		
		try 
		{
			if (lastEntry.getClassName().equals(logEntry.getClassName()) && lastEntry.getMethodName().equals(logEntry.getMethodName()))
			{
				writer.write("\n" + logEntry.toStringNoHeader());
				
				System.out.println(logEntry.toStringNoHeader());
			}
			else 
			{
				writer.write("\n\n" + logEntry.toString());
				
				System.out.println("\n" + logEntry.toString());
			}
			
			lastEntry = logEntry;
			
			writer.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		try 
		{
			writer.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
