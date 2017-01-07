package com.hit.processes;

import java.util.List;

public class RunConfiguration 
{
	private List<ProcessCycles> processesCycles;

	public RunConfiguration(List<ProcessCycles> processesCycles) {
		this.processesCycles = processesCycles;
	}

	public List<ProcessCycles> getProcessCycles() {
		return processesCycles;
	}

	public void setProcessCycles(List<ProcessCycles> processesCycles) {
		this.processesCycles = processesCycles;
	}
	
	@Override
	public String toString()
	{
		return processesCycles.toArray().toString();
	}
}
