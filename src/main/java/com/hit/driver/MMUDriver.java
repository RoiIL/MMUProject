package com.hit.driver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
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
import com.hit.view.CLI;

public class MMUDriver 
{
	public MMUDriver()
	{
		
	}
	
	public static void main(java.lang.String[] args) throws InterruptedException, InvocationTargetException
	{
		CLI cli = new CLI(System.in, System.out);
		String[] configuration = null;
		
		while((configuration = cli.getConfiguration()) != null)
		{
			IAlgoCache<Long, Long> algo = null;
			int capacity = 0;
			
			String algoType = configuration[0];
			capacity = Integer.parseInt(configuration[1]);
			MMULogger.getInstace().write(MessageFormat.format("RC:{0}\n", capacity), Level.INFO);
			
			switch (algoType)
			{
			case "lru":
				algo = new LRUAlgoCacheImpl<>(capacity);
				break;
			case "mfu":
				algo = new MFUAlgoCacheImpl<>(capacity);
				break;
			case "secondChance":
				algo = new SecondChanceAlgoCacheImpl<>(capacity);
			}
			
			MemoryManagementUnit mmu = new MemoryManagementUnit(capacity, algo);
			
			RunConfiguration runConfig = readConfigurationFile();
			List<ProcessCycles> processCyclesList = runConfig.getProcessCycles();
			List<Process> processes = createProcesses(processCyclesList, mmu);
			MMULogger.getInstace().write(MessageFormat.format("PN:{0}\n\n", processes.size()), Level.INFO);
			
			runProcesses(processes);
		}
		
		System.out.println("Done.");
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
