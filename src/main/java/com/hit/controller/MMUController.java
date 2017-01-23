package com.hit.controller;

import java.util.Observable;
import java.util.Observer;

import com.hit.model.MMUModel;
import com.hit.model.Model;
import com.hit.view.MMUView;
import com.hit.view.View;

public class MMUController implements Controller, Observer
{
	private Model model;
	private View view;
	
	public MMUController(Model model, View view) 
	{
		this.model = model;
		this.view = view;
	}
	
	@Override
	public void update(Observable o, Object arg) 
	{
		if (o == model)
		{
			view.open();
		}
		else if(o == view)
		{
			model.readData();
			((MMUView)view).setConfiguration(((MMUModel)model).getCommands());
			((MMUView)view).numProcesses = ((MMUModel)model).numProcesses;
			((MMUView)view).ramCapacity = ((MMUModel)model).ramCapacity;
		}
	}

}
