package com.hit.processes;

import javax.imageio.stream.MemoryCacheImageOutputStream;

import com.hit.memoryunits.MemoryManagementUnit;

public class Process implements Runnable 
{
	private int m_id;
	private MemoryManagementUnit m_mmu;
	private ProcessCycles m_processCycles;
	
	public Process(int id, MemoryManagementUnit mmu, ProcessCycles processCycles)
	{
		m_id = id;
		m_mmu = mmu;
		m_processCycles = processCycles;
	}
	
	@Override
	public void run() 
	{	
		
	}

}
