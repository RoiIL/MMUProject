package com.hit.memoryunits;

public class HardDisk 
{
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
}
