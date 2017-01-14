package com.hit.memoryunits;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RAM 
{
	private int m_initialCapacity;
	private Map<Long, Page<byte[]>> m_pagesMap;
	
	public RAM(int initialCapacity)
	{
		m_initialCapacity = initialCapacity;
		m_pagesMap = new HashMap<Long, Page<byte[]>>(initialCapacity);
	}
	
	public Map<Long, Page<byte[]>> getPages()
	{
		return m_pagesMap;
	}
	
	public void setPages(Map<Long, Page<byte[]>> pages)
	{
		m_pagesMap = pages;
	}
	
	public Page<byte[]> getPage(Long pageId)
	{
		return m_pagesMap.get(pageId);
	}
	
	public void addPage(Page<byte[]> addPage)
	{
		m_pagesMap.put(addPage.getPageId(), addPage);
	}
	
	public void removePage(Page<byte[]> pageToRemove)
	{
		m_pagesMap.remove(pageToRemove.getPageId());
	}
	
	public Page<byte[]>[] getPages(Long[] pageIds)
	{
		@SuppressWarnings("unchecked")
		Page<byte[]>[] retPages = new Page[pageIds.length];
		int index = 0;
		for (Long pageId : pageIds) 
		{
			if (m_pagesMap.containsKey(pageId))
			{
				retPages[index] = m_pagesMap.get(pageId);
				index++;
			}
		}
		
		return retPages;
	}
	
	public void addPages(Page<byte[]>[] addPages)
	{
		for (Page<byte[]> page : addPages) 
		{
			addPage(page);
		}
	}
	
	public void removePages(Page<byte[]>[] remvoePages)
	{
		for (Page<byte[]> page : remvoePages) 
		{
			removePage(page);
		}
	}
	
	public int getInitialCapacity()
	{
		return m_initialCapacity;
	}
	
	public void setInitialCapacity(int initialCapacity)
	{
		m_initialCapacity = initialCapacity;
	}
}
