package com.hit.memoryunits;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.util.HashMap;

public class HardDisk 
{
	public static final String DEFAULT_FILE_NAME = "HD";
	private static int _SIZE = 1000;
	private static HardDisk m_instance = null;
	
	
	private HardDisk()
	{	
	}
	
	public static HardDisk getInstance()
	{
		if (m_instance == null)
		{
			m_instance = new HardDisk();
		}
		
		return m_instance;
	}
	
	@SuppressWarnings("unchecked")
	public Page<byte[]> pageFault(Long pageId) throws FileNotFoundException, IOException
	{
		HashMap<Long, Page<byte[]>> dataOnFile = new HashMap<>();
		FileInputStream fileInput = new FileInputStream(DEFAULT_FILE_NAME);
		ObjectInputStream hdInputFile = new ObjectInputStream(fileInput);
		try 
		{
			dataOnFile = (HashMap<Long, Page<byte[]>>) hdInputFile.readObject();
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			fileInput.close();
		}
		
		Page<byte[]> pageToReturn = dataOnFile.get(pageId);
		
		FileOutputStream hdFile = new FileOutputStream(DEFAULT_FILE_NAME);
		ObjectOutputStream writeData = new ObjectOutputStream(hdFile);
		dataOnFile.remove(pageId);
		writeData.writeObject(dataOnFile);
		writeData.flush();
		hdFile.close();
		
		return pageToReturn;
	}
	
	@SuppressWarnings("unchecked")
	public Page<byte[]> pageReplacement(Page<byte[]> moveToHdPage, Long moveToRamId) throws FileNotFoundException, IOException
	{
		HashMap<Long, Page<byte[]>> dataOnFile = new HashMap<>();
		FileInputStream fileInput = new FileInputStream(DEFAULT_FILE_NAME);
		ObjectInputStream hdInputFile = new ObjectInputStream(fileInput);
		try 
		{
			dataOnFile = (HashMap<Long, Page<byte[]>>) hdInputFile.readObject();
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			fileInput.close();
		}
		
		dataOnFile.put(moveToHdPage.getPageId(), moveToHdPage);
		Page<byte[]> pageToReturn = dataOnFile.get(moveToRamId);
		dataOnFile.remove(moveToRamId);
		
		FileOutputStream hdFile = new FileOutputStream(DEFAULT_FILE_NAME);
		ObjectOutputStream writeData = new ObjectOutputStream(hdFile);
		writeData.writeObject(dataOnFile);
		writeData.flush();
		hdFile.close();
		
		return pageToReturn;
	}
}
