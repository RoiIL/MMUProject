package com.hit.driver;

import java.lang.reflect.InvocationTargetException;

import com.hit.view.CLI;

public class MMUDriver 
{
	public MMUDriver()
	{
		
	}
	
	public static void main(java.lang.String[] args) throws InterruptedException, InvocationTargetException
	{
		CLI cli = new CLI(System.in, System.out);
		String[] configuration = cli.getConfiguration();
		
		System.out.println("Done.");
	}
}
