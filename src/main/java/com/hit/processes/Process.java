package com.hit.processes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.swing.plaf.SliderUI;

import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.memoryunits.Page;

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
			synchronized (mmu) 
			{
				List<Long> pagesList = processCycle.getPages();
				Long[] pageIds = pagesList.toArray(new Long[pagesList.size()]);
				Page<byte[]>[] pagesFromMmu = null;
				try 
				{
					pagesFromMmu = mmu.getPages(pageIds);
				} 
				catch (IOException e) 
				{		
					e.printStackTrace();
				}
				
				int pageIndex = 0;
				for (byte[] data : processCycle.getData()) 
				{
					pagesFromMmu[pageIndex].setContent(data);
					pageIndex++;
				}
			}
			
			try 
			{
				Thread.sleep(processCycle.getSleepMs());
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
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
