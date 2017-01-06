package com.hit.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class CLI 
{
	InputStream in;
	OutputStream out;
	Scanner scanner;
	
	public CLI(InputStream in, OutputStream out) 
	{
		this.in = in;
		this.out = out;
		scanner = new Scanner(in);
	}
	
	public String[] getConfiguration()
	{
		String userInput;
		boolean isValid;
		do 
		{
			isValid = true;
			System.out.println("What would you like to do? (stop/start)");
			userInput = scanner.nextLine();
			if (!userInput.toLowerCase().equals("start") && !userInput.toLowerCase().equals("stop"))
			{
				System.out.println("Please enter 'start' or 'stop' only");
				isValid = false;
			}
		}
		while (!isValid);
		
		int ramCapacityIndex = 0;
		String[] algoChoiceAndCapacity;
		String algo;
		do 
		{
			System.out.println("Please enter required algorithm and RAM capacity: ");
			System.out.println("(LRU / MFU / Second Chance)");
			userInput = scanner.nextLine();
			ramCapacityIndex = 1;
			algoChoiceAndCapacity = userInput.split(" ");
			algo = algoChoiceAndCapacity[0].toLowerCase();
			if (algo.equals("second"))
			{
				algo = algo + " " + algoChoiceAndCapacity[1].toLowerCase();
				ramCapacityIndex++;
			}
	
			if (!algo.equals("lru")&& !algo.equals("mfu") && !algo.equals("second chance"))
			{
				System.out.println("ERROR! Not a valid algoithm name");
				isValid = false;
			}
			
			if (isValid)
			{
				if (algoChoiceAndCapacity.length > ramCapacityIndex + 1 || algoChoiceAndCapacity.length < 2)
				{
					System.out.println("ERROR! You have entered too many argument");
					isValid = false;
				}
				
				if (isValid)
				{
					try
					{
						Integer.parseInt(algoChoiceAndCapacity[ramCapacityIndex]);
					}
					catch (NumberFormatException exception)
					{
						System.out.println("ERROR! Not a valid value for RAM capacity");
						isValid = false;
					}
				}
			}
		}
		while (!isValid);
		
		System.out.println("Processing...");
		
		if (algo.equals("second chance"))
		{
			String[] algoSecondChance = new String[]{"secondChance", algoChoiceAndCapacity[ramCapacityIndex]};
			return algoSecondChance;
		}
		return algoChoiceAndCapacity;
	}
	
	public void write(String string)
	{
		
	}
	
}
