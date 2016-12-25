package com.hit.memoryunits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hit.algorithm.IAlgoCache;

public class MemoryManagementUnit 
{
	private IAlgoCache<Long, Long> m_algo;
	private RAM m_ram;
	
	public MemoryManagementUnit(int ramCapacity, IAlgoCache<Long, Long> algo)
	{
		m_algo = algo;
		m_ram = new RAM(ramCapacity);
	}
	
	public Page<byte[]>[] getPages(Long[] pageIds) throws IOException
	{
		Page<byte[]> pageToGet;
		List<Long> pageIdToHandle;
		for (Long pageId : pageIds) 
		{
			if (m_algo.getElement(Arrays.asList(pageId)) == null) 	// the page is not in ram
			{
				pageIdToHandle = m_algo.putElement(Arrays.asList(pageId), Arrays.asList(pageId));
				
				if (pageIdToHandle == null) 						// ram is not full, performing page fault
				{
					pageToGet = HardDisk.getInstance().pageFault(pageId);
				}
				else												// ram is full, performing page replacement
				{
					Page<byte[]> pageToReplace = m_ram.getPage(pageIdToHandle.get(0));
					m_ram.removePage(pageToReplace);
					pageToGet = HardDisk.getInstance().pageReplacement(pageToReplace, pageId);
				}
				
				m_ram.addPage(pageToGet);
			}
		}
		
		return m_ram.getPages(pageIds);
	}
}
