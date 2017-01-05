package com.hit.processes;

import java.util.ArrayList;
import java.util.List;

public class RunConfiguration 
{
	private List<ProcessCycles> processCycles;

	public RunConfiguration(List<ProcessCycles> processCycles) {
		this.processCycles = processCycles;
	}

	public List<ProcessCycles> getProcessCycles() {
		return processCycles;
	}

	public void setProcessCycles(List<ProcessCycles> processCycles) {
		this.processCycles = processCycles;
	}
	
	@Override
	public String toString()
	{
		return processCycles.toArray().toString();
	}
}
