package com.hit.processes;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.memoryunits.Page;
import com.hit.util.MMULogger;

public class Process implements Runnable 
{
	private int id;
	private MemoryManagementUnit mmu;
	private ProcessCycles processCycles;
	
	public Process(int id, MemoryManagementUnit mmu, ProcessCycles processCycles)
	{
		this.id = id;
		this.mmu = mmu;
		this.processCycles = processCycles;
	}
	
	@Override
	public void run() 
	{	
		for (ProcessCycle processCycle : processCycles.getProcessCycles()) 
		{
			boolean writePages[] = new boolean[processCycle.getData().size()];
			
			int index = 0;
			for (byte[] pageData : processCycle.getData()) 
			{
				writePages[index] = pageData == null ? true : false;
				index++;;
			}
			
			synchronized (mmu) 
			{
				List<Long> pagesList = processCycle.getPages();
				Long[] pageIds = pagesList.toArray(new Long[pagesList.size()]);
				
				Page<byte[]>[] pagesFromMmu = null;
				try 
				{
					System.out.println("Pages requested: " + Arrays.asList(pageIds));
					pagesFromMmu = mmu.getPages(pageIds, writePages);
					System.out.println("Pages returned from mmu: " + Arrays.asList(pagesFromMmu));
				} 
				catch (IOException exception) 
				{		
					MMULogger.getInstace().write(exception.getMessage(), Level.SEVERE);
				}
				
				int pageIndex = 0;
				for (byte[] data : processCycle.getData()) 
				{
					if (pagesFromMmu[pageIndex] != null) // added to handle the bug in MFU algo! (the algorithm, not my code)
					{
						pagesFromMmu[pageIndex].setContent(data);
						MMULogger.getInstace().write(MessageFormat.format("GP:P{0} {1} {2}\n\n", 
								id, pagesFromMmu[pageIndex].getPageId(), Arrays.toString(data)), Level.INFO);
					}
					pageIndex++;
				}
			}
			
			try 
			{
				Thread.sleep(processCycle.getSleepMs());
			} 
			catch (InterruptedException exception) 
			{
				MMULogger.getInstace().write(exception.getMessage(), Level.SEVERE);
			}
		}
		
		System.out.println("Process " + id + " is done");
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	

}
