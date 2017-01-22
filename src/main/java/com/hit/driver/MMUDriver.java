package com.hit.driver;

import java.lang.reflect.InvocationTargetException;

import com.hit.controller.MMUController;
import com.hit.model.MMUModel;

import com.hit.view.CLI;
import com.hit.view.MMUView;

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
			MMUModel model = new MMUModel(configuration);
			MMUView view = new MMUView();
			MMUController controller = new MMUController(model, view);
			model.addObserver(controller);
			view.addObserver(controller);
			model.start();
		}
		
		System.out.println("Done.");
	}
}
