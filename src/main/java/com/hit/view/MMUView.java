package com.hit.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class MMUView extends Observable implements View 
{
	public static int BYTES_IN_PAGE;
	public static int NUM_MMU_PAGES;
	public int numProcesses;
	public int ramCapacity;
	private List<String> commands;
	
	public MMUView()
	{
		commands = new ArrayList<>();
	}
	
	public void setConfiguration(List<String> commands)
	{
		this.commands = commands;
	}
	
	@Override
	public void open()
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			@Override
			public void run() 
			{
				createAndShowGUI();
			}
		});
		
		setChanged();
		notifyObservers();
	}
	
	private void createAndShowGUI()
	{
		JFrame frame = new JFrame("Hello");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel label = new JLabel(commands.toString());
		frame.getContentPane().add(label);
		frame.pack();
		frame.setVisible(true);
	}

}
