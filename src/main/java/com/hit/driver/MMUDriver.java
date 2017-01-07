package com.hit.driver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.MFUAlgoCacheImpl;
import com.hit.algorithm.SecondChanceAlgoCacheImpl;
import com.hit.memoryunits.MemoryManagementUnit;
import com.hit.processes.Process;
import com.hit.processes.ProcessCycles;
import com.hit.processes.RunConfiguration;
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
			
			FileReader configFile = null;
			try {
				configFile = new FileReader("src/main/resources/configuration/Configuration.json");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			RunConfiguration runConfig = new Gson().fromJson(configFile, RunConfiguration.class);
			List<ProcessCycles> processCyclesList = runConfig.getProcessCycles();
			List<Process> processList = new ArrayList<>();
			int processId = 0;
			for (ProcessCycles processCycles : processCyclesList)
			{
				processList.add(new Process(processId, mmu, processCycles));
				processId++;
			}
			
			List<Thread> threadList = new ArrayList<>();
			for (Process process : processList) 
			{
				threadList.add(new Thread(process));
			}
			
			for (Thread thread : threadList) 
			{
				thread.start();
			}
			
			for (Thread thread : threadList) 
			{
				thread.join();
			}
		}
		
		System.out.println("Done.");
	}
}
