package com.hit.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MMULogger 
{
	public final static String DEFAULT_FILE_NAME = "logs/log.txt";
	private static MMULogger instance = null;
	private FileHandler handler;
	
	private MMULogger()
	{
		try 
		{
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
		if (instance == null)
		{
			instance = new MMULogger();
		}
		
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
}
