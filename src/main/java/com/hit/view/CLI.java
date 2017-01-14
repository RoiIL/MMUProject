package com.hit.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class CLI 
{
	Scanner in;
	PrintWriter out;
	Scanner scanner;
	
	public CLI(InputStream in, OutputStream out) 
	{
		this.in = new Scanner(in);
		this.out = new PrintWriter(out);
	}
	
	public String[] getConfiguration()
	{
		String userInput;
		boolean isValid;
		do 
		{
			isValid = true;
			write("What would you like to do? (stop/start)");
			userInput = in.nextLine();
			if (!userInput.toLowerCase().equals("start") && !userInput.toLowerCase().equals("stop"))
			{
				write("Please enter 'start' or 'stop' only");
				isValid = false;
			}
		}
		while (!isValid);
		
		if (userInput.equals("start"))
		{
			int ramCapacityIndex = 0;
			String[] algoChoiceAndCapacity;
			String algo;
			do 
			{
				isValid = true;
				write("Please enter required algorithm and RAM capacity: ");
				write("(LRU / MFU / Second Chance)");
				userInput = in.nextLine();
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
					write("ERROR! Not a valid algoithm name");
					isValid = false;
				}
				
				if (isValid)
				{
					if (algoChoiceAndCapacity.length > ramCapacityIndex + 1)
					{
						write("ERROR! You have entered too many arguments");
						isValid = false;
					}
					if ((algo.equals("second chance") && algoChoiceAndCapacity.length == 2) || algoChoiceAndCapacity.length < 2)
					{
						write("ERROR! You are missing arguments");
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
							write("ERROR! Not a valid value for RAM capacity");
							isValid = false;
						}
					}
				}
			}
			while (!isValid);
			
			write("Processing...");
			
			if (algo.equals("second chance"))
			{
				String[] algoSecondChance = new String[]{"secondChance", algoChoiceAndCapacity[ramCapacityIndex]};
				return algoSecondChance;
			}
			return algoChoiceAndCapacity;
		}
		else
		{
			return null;
		}
	}
	
	public void write(String string)
	{
		out.println(string);
		out.flush();
	}
	
}
