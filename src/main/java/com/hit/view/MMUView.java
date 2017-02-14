package com.hit.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class MMUView extends Observable implements View 
{
	public static int BYTES_IN_PAGE;
	public static int NUM_MMU_PAGES;
	public int numProcesses;
	public int ramCapacity;
	private List<String> commands;
	private String singleCommand;
	private int lineIndex = 2;
	private int columnIndex = 0;
	
	public MMUView()
	{
		commands = new ArrayList<>();
		singleCommand = "";
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
		Display display = new Display();
		Shell shell = new Shell(display);
		GridLayout layout = new GridLayout(2, false);

		shell.setSize(750, 600);
        shell.setLayout(layout);
        shell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
        Table table = new Table (shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		
		Composite pageSection = new Composite(shell, SWT.NONE);
		GridLayout pageSectionGrid = new GridLayout(2, false);
		pageSection.setLayout(pageSectionGrid);
		
		Label pageFaultLable = new Label (pageSection, SWT.TOP);
		pageFaultLable.setText ("Page Fault Amount:");
		Label amountOfPageFaultLabel = new Label (pageSection, SWT.NONE);
		amountOfPageFaultLabel.setText("3");
		
		Label pageReplacementLable = new Label (pageSection, SWT.TOP);
		pageReplacementLable.setText ("Page Replacement Amount:");
		Label amountOfPageReplacementLabel = new Label (pageSection, SWT.NONE);
		amountOfPageReplacementLabel.setText("1");
		
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.heightHint = 100;
		table.setLayoutData(data);
		
		String ramCapacety = commands.get(0);
		ramCapacety = ramCapacety.replace("RC:", "");
		String[] pageNumbers = new String[Integer.parseInt(ramCapacety)];
		for (int i = 0; i < pageNumbers.length; i++) 
		{
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText("");
			column.pack();
		}
		
		Composite buttonsSection = new Composite(shell, SWT.NONE);
		GridLayout buttonsSectionGrid = new GridLayout(2, false);
		buttonsSection.setLayout(buttonsSectionGrid);
		
		String processesAmount = commands.get(1);
		ramCapacety = ramCapacety.replace("PN:", "");
		
		Button play = new Button (buttonsSection, SWT.PUSH);
		play.setText("Play");
		play.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				List<String> columnToReplace = new ArrayList<>(); 
				while(!singleCommand.contains("GP:"))
				{
					singleCommand = commands.get(lineIndex);
					lineIndex++;
					if (singleCommand.contains("PR:"))
					{
						columnToReplace.add((singleCommand.substring(singleCommand.indexOf("H"), singleCommand.indexOf("MTR"))).trim());
					}
				}

				String pageId = singleCommand.substring(6, singleCommand.indexOf("[") - 1);
				if (columnToReplace.size() > 0)
				{
					columnIndex = Integer.parseInt(columnToReplace.get(0));
					columnToReplace.remove(0);
				}
				table.getColumn(columnIndex).setText(pageId);
				
				String dataLine = singleCommand.substring(singleCommand.indexOf("[") + 1, singleCommand.indexOf("]"));
				String[] data = dataLine.split(",");
				
				for (int i = 0; i < data.length; i++) 
				{
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(columnIndex, data[i]);
				}
				
				if (columnIndex < 5)
				{
					columnIndex++;
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
				
			}
		});
		
		Button playAll = new Button (buttonsSection, SWT.PUSH);
		playAll.setText("Play All");
		
		shell.setDefaultButton(play);
		shell.pack();
		shell.open ();
		
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

}
