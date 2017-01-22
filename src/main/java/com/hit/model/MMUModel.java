package com.hit.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.MessageFormat;
import java.text.spi.NumberFormatProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.MFUAlgoCacheImpl;
import com.hit.algorithm.SecondChanceAlgoCacheImpl;
import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.processes.Process;
import com.hit.processes.ProcessCycles;
import com.hit.processes.RunConfiguration;
import com.hit.util.MMULogger;

public class MMUModel extends Observable implements Model 
{
	public int numProcesses;
	public int ramCapacity;
	private List<String> data;
	
	public MMUModel(String[] configuration)
	{
		data = new ArrayList<>();
		IAlgoCache<Long, Long> algo = null;
		
		String algoType = configuration[0];
		ramCapacity = Integer.parseInt(configuration[1]);
		MMULogger.getInstace().write(MessageFormat.format("RC:{0}\n", ramCapacity), Level.INFO);
		
		switch (algoType)
		{
		case "lru":
			algo = new LRUAlgoCacheImpl<>(ramCapacity);
			break;
		case "mfu":
			algo = new MFUAlgoCacheImpl<>(ramCapacity);
			break;
		case "secondChance":
			algo = new SecondChanceAlgoCacheImpl<>(ramCapacity);
		}
		
		MemoryManagementUnit mmu = new MemoryManagementUnit(ramCapacity, algo);
		
		RunConfiguration runConfig = readConfigurationFile();
		List<ProcessCycles> processCyclesList = runConfig.getProcessCycles();
		List<Process> processes = createProcesses(processCyclesList, mmu);
		MMULogger.getInstace().write(MessageFormat.format("PN:{0}\n\n", processes.size()), Level.INFO);
		numProcesses = processes.size();
		
		runProcesses(processes);
	}
	
	public List<String> getCommands()
	{
		return null;
	}
	
	@Override
	public void readData() 
	{
		
	}

	@Override
	public void start() 
	{
		
	}
	
	private static RunConfiguration readConfigurationFile()
	{
		FileReader configFile = null;
		try 
		{
			configFile = new FileReader("src/main/resources/configuration/Configuration.json");
		} 
		catch (FileNotFoundException exception) 
		{
			MMULogger.getInstace().write(exception.getMessage(), Level.SEVERE);
		}
		
		return new Gson().fromJson(configFile, RunConfiguration.class);
	}
	
	private static List<Process> createProcesses(List<ProcessCycles> processCyclesList, MemoryManagementUnit mmu)
	{
		List<Process> processList = new ArrayList<>();
		int processId = 0;
		for (ProcessCycles processCycles : processCyclesList)
		{
			processList.add(new Process(processId, mmu, processCycles));
			processId++;
		}
		
		return processList;
	}
	
	private static void runProcesses(List<Process> processes)
	{
		ExecutorService executor = Executors.newCachedThreadPool();
		for (Process process : processes) 
		{
			executor.execute(process);
		}
		
		executor.shutdown();
		try 
		{
			executor.awaitTermination(2, TimeUnit.MINUTES);
		} 
		catch (InterruptedException exception) 
		{
			MMULogger.getInstace().write(exception.getMessage(), Level.SEVERE);
		}
	}

}
