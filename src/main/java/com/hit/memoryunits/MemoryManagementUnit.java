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
	
	public Page<byte[]>[] getPages(Long[] pageIds, boolean[] writePages) throws IOException
	{
		List<Long> returnedPages = m_algo.getElement(Arrays.asList(pageIds));
		System.out.println(m_algo.toString());
		List<Long> pagesNotInRam = new ArrayList<>();
		
		int pageIndex = 0;
		for (Long pageId : returnedPages) // Collect all pages that are not in the RAM
		{
			if (pageId == null)
			{
				pagesNotInRam.add(pageIds[pageIndex]);
			}
			pageIndex++;
		}
		
		// Handling pages that are not in the RAM depends if RAM is full or not
		Page<byte[]> pageToInsert = null;
		List<Long> pageIdsToHandle = m_algo.putElement(pagesNotInRam, pagesNotInRam);
		System.out.println("pages to handle: " + pageIdsToHandle);
		System.out.println(m_algo.toString());
		
		int pageToHandleIndex = 0;
		for (Long pageIdToHandle : pageIdsToHandle) 
		{
			if (pageIdToHandle == null) // Ram is not full and the page can be inserted directly to RAM
			{
				pageToInsert = HardDisk.getInstance().pageFault(pagesNotInRam.get(pageToHandleIndex));
			}
			else                        // RAM is full, replacing the page
			{
				Page<byte[]> pageToReplace = m_ram.getPage(pageIdToHandle); 
				m_ram.removePage(pageToReplace);
				System.out.println("Page to remvoe " + pageToReplace);
				pageToInsert = HardDisk.getInstance().pageReplacement(pageToReplace, pagesNotInRam.get(pageToHandleIndex));
			}
			pageToHandleIndex++;
			System.out.println("page to insert: " + pageToInsert);
			System.out.println("RAM content before adding: " + m_ram.getPages().toString());
			m_ram.addPage(pageToInsert);
			System.out.println("RAM content after adding: " + m_ram.getPages().toString());
		}
		
		
		//System.out.println("RAM content: " + m_ram.getPages().toString());
		Page<byte[]>[] pages = m_ram.getPages(pageIds);
		for (Page<byte[]> page : pages) {
			if (page == null)
			{
				System.out.println("not good");
			}
		}
		
		return m_ram.getPages(pageIds);
	}
}
