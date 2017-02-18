package com.hit.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MMULogger 
{
	public final static String DEFAULT_FILE_NAME = "logs/log.txt";
	private static MMULogger instance = new MMULogger();
	private FileHandler handler;
	
	private MMULogger()
	{
		try 
		{
			File file = new File(DEFAULT_FILE_NAME);
			if(file.exists())
			{
				file.delete();
			}
			handler = new FileHandler(DEFAULT_FILE_NAME);
			handler.setFormatter(new OnlyMessageFormatter());
		} 
		catch (SecurityException | IOException exception) 
		{
			System.err.println("Log file Error!!!");
			exception.printStackTrace();
		}
	}
	
	public static MMULogger getInstace()
	{
		return instance;
	}
	
	public synchronized void write(String command, Level level)
	{
		handler.publish(new LogRecord(level, command));
	}
	
	public class OnlyMessageFormatter extends Formatter
	{
		public OnlyMessageFormatter() 
		{
			super();
		}
		@Override
		public String format(final LogRecord record) 
		{
			return record.getMessage();
		}
	}
	
	public void close()
	{
		handler.close();
	}
}
