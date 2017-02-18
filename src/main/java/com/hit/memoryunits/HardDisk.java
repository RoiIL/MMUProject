package com.hit.memoryunits;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Level;

import com.hit.util.MMULogger;

public class HardDisk 
{
	public static final String DEFAULT_FILE_NAME = "src/main/resources/harddisk/HdPages";
	private static int _SIZE = 1000;
	private static HardDisk instance = new HardDisk();
	HashMap<Long, Page<byte[]>> dataOnFile;
	
	private HardDisk()
	{
		dataOnFile = new HashMap<>();
		for (Long i = 0L; i < _SIZE; i++)
		{
			dataOnFile.put(i, new Page<byte[]>(i, i.toString().getBytes()));
		}
		try {
			writeDataToHd();
		} catch (IOException e) {
			MMULogger.getInstace().write(e.getMessage(), Level.SEVERE);
			e.printStackTrace();
		}
	}
	
	public static HardDisk getInstance() throws IOException
	{
		return instance;
	}
	
	public Page<byte[]> pageFault(Long pageId) throws FileNotFoundException, IOException
	{
		readDataFromHd();
		Page<byte[]> pageToReturn = dataOnFile.get(pageId);
		
		return pageToReturn;
	}
	
	public Page<byte[]> pageReplacement(Page<byte[]> moveToHdPage, Long moveToRamId) throws FileNotFoundException, IOException
	{
		readDataFromHd();
		dataOnFile.put(moveToHdPage.getPageId(), moveToHdPage);
		Page<byte[]> pageToReturn = dataOnFile.get(moveToRamId);
		writeDataToHd();
		
		return pageToReturn;
	}
	
	private void writeDataToHd() throws FileNotFoundException, IOException
	{
		FileOutputStream hdFile = null;
		ObjectOutputStream writeData = null;
		try
		{
			hdFile = new FileOutputStream(DEFAULT_FILE_NAME);
			writeData = new ObjectOutputStream(hdFile);
			writeData.writeObject(dataOnFile);
			writeData.flush();
		}
		catch (FileNotFoundException exception)
		{
			MMULogger.getInstace().write(exception.getMessage(), Level.SEVERE);
		}
		finally
		{
			writeData.close();
			hdFile.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void readDataFromHd() throws FileNotFoundException, IOException
	{
		FileInputStream fileInput = new FileInputStream(DEFAULT_FILE_NAME);
		ObjectInputStream hdInputFile = new ObjectInputStream(fileInput);
		try 
		{
			dataOnFile = (HashMap<Long, Page<byte[]>>) hdInputFile.readObject();
		} 
		catch (ClassNotFoundException exception) 
		{
			MMULogger.getInstace().write(exception.getMessage(), Level.SEVERE);
		}
		finally 
		{
			fileInput.close();
			hdInputFile.close();
		}
	}
}
