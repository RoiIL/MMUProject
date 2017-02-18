package com.hit.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Map.Entry;

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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.hit.util.MMULogger;

public class MMUView extends Observable implements View 
{
	public static int BYTES_IN_PAGE;
	public static int NUM_MMU_PAGES;
	public int numOfProcesses;
	public int ramCapacity;
	private List<String> commands;
	private String singleCommand;
	private int lineIndex = 2;
	private int columnIndex = -1;
	private List<TableItem> listOfTableItems;
	private List<String> replaceCommands;
	private List<String> ramPagesList;
	private Map<String, PageInfo> pagesInfoMap;
	private Map<String, Integer> PageIdToColumnMap;
	private Map<String, Boolean> ProcessIdState;
	private boolean isRamHasSpace;
	private Table table;
	private Integer pageFaultConter;
	private Integer pageReplacementCounter;
	private Label amountOfPageFaultLabel;
	private Label amountOfPageReplacementLabel;
	private Button play;
	private Button playAll;
	
	public MMUView()
	{
		commands = new ArrayList<>();
		singleCommand = "";
		listOfTableItems = new ArrayList<>();
		replaceCommands = new ArrayList<>();
		ramPagesList = new ArrayList<>();
		pagesInfoMap = new HashMap<>();
		PageIdToColumnMap = new HashMap<>();
		ProcessIdState = new HashMap<>();
		isRamHasSpace = true;
		pageFaultConter = 0;
		pageReplacementCounter = 0;

	}
	
	public void setConfiguration(List<String> commands)
	{
		this.commands = commands;
	}
	
	@Override
	public void open()
	{
		setChanged();
		notifyObservers();
		createAndShowGUI();
	}
	
	private void createAndShowGUI()
	{
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Memory Management Unit Project");
		GridLayout layout = new GridLayout(1, false);

		shell.setSize(750, 600);
        shell.setLayout(layout);
        shell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
        table = new Table(shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		
        Composite buttonsAndPages = new Composite(shell, SWT.NONE);
        GridLayout buttonsAndPagesGrid = new GridLayout(2, false);
        buttonsAndPages.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        buttonsAndPages.setLayout(buttonsAndPagesGrid);
        
		Composite buttonsSection = new Composite(buttonsAndPages, SWT.NONE);
		GridLayout buttonsSectionGrid = new GridLayout(2, false);
		buttonsSection.setLayout(buttonsSectionGrid);
        
		Composite pageSection = new Composite(buttonsAndPages, SWT.NONE);
		GridLayout pageSectionGrid = new GridLayout(2, false);
		pageSection.setLayout(pageSectionGrid);
		
		Label pageFaultLable = new Label (pageSection, SWT.TOP);
		pageFaultLable.setText ("Page Fault Amount:");
		amountOfPageFaultLabel = new Label (pageSection, SWT.NONE);
		amountOfPageFaultLabel.setText(pageFaultConter.toString());
		GridData pageFaultData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		pageFaultData.widthHint = 50;
		amountOfPageFaultLabel.setLayoutData(pageFaultData);
		
		Label pageReplacementLable = new Label (pageSection, SWT.TOP);
		pageReplacementLable.setText ("Page Replacement Amount:");
		amountOfPageReplacementLabel = new Label (pageSection, SWT.NONE);
		amountOfPageReplacementLabel.setText(pageReplacementCounter.toString());
		GridData pageReplacementData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		pageReplacementData.widthHint = 50;
		amountOfPageReplacementLabel.setLayoutData(pageReplacementData);
		
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		data.heightHint = 120;
		data.widthHint = 750;
		table.setLayoutData(data);
		
		String ramCapacety = commands.get(0);
		ramCapacety = ramCapacety.replace("RC:", "");
		String[] pageNumbers = new String[Integer.parseInt(ramCapacety)];
		for (int i = 0; i < pageNumbers.length; i++) 
		{
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText("");
			column.setWidth(50);
		}
		
		for (int i = 0; i < 5; i++) 
		{
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(i, "");
			listOfTableItems.add(item);
		}
		
		play = new Button(buttonsSection, SWT.PUSH);
		play.setText("Play");
		play.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				if (lineIndex < commands.size())
				{
					processOneCommand();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}
		});
		
		playAll = new Button (buttonsSection, SWT.PUSH);
		playAll.setText("Play All");
		playAll.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				while (lineIndex < commands.size())
				{
					processOneCommand();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		Table processesTable = new Table(shell, SWT.CHECK | SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
	    for (int i = 0; i < numOfProcesses; i++) 
	    {
	      TableItem item = new TableItem(processesTable, SWT.NONE);
	      item.setText("Process " + i);
	      item.setChecked(true);
	      ProcessIdState.put(((Integer)i).toString(), true);
	    }
	    
	    GridData processesData = new GridData(SWT.NONE, SWT.NONE, true, true, 1, 1);
	    processesData.heightHint = 130;
	    processesData.widthHint = 70;
	    processesTable.setLayoutData(processesData);
	    
	    processesTable.addListener(SWT.Selection, new Listener()
        {
            @Override
            public void handleEvent(Event event) 
            {
                TableItem item = (TableItem) event.item;
                String curProcessId = ((Integer)processesTable.indexOf(item)).toString();
                if(!item.getChecked())
                {
                	for (Entry<String, PageInfo> entry : pagesInfoMap.entrySet()) 
            		{
                		if (entry.getValue().processId.equals(curProcessId))
                		{
                			entry.getValue().toDisplay = false;
                			
                			int curColumnIndex = PageIdToColumnMap.get(entry.getKey());
                			if (curColumnIndex >= 0)
                			{
	                			table.getColumn(curColumnIndex).setText(" ");                			
								for (int i = 0; i < entry.getValue().data.length; i++) 
								{
									listOfTableItems.get(i).setText(curColumnIndex, " ");
								}
                			}
                		}
                    }

                    item.setChecked(false);
                    ProcessIdState.put(curProcessId, false);
                }
                else
                {
                	for (Entry<String, PageInfo> entry : pagesInfoMap.entrySet()) 
            		{
                		PageInfo curPage = entry.getValue();
                		if (curPage.processId.equals(curProcessId))
                		{
                			curPage.toDisplay = true;
                			
                			int curColumnIndex = PageIdToColumnMap.get(entry.getKey());
                			if (curColumnIndex >= 0)
                			{
			        			table.getColumn(curColumnIndex).setText(curPage.pageId);
								for (int i = 0; i < curPage.data.length; i++) 
								{
									listOfTableItems.get(i).setText(curColumnIndex, curPage.data[i]);
								}
                			}
                		}
                    }

                    item.setChecked(true);
                    ProcessIdState.put(curProcessId, true);
                }
            }         
        });    
		
		shell.setDefaultButton(play);
		shell.pack();
		shell.open ();
		
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
	
	private void processOneCommand()
	{
		while(!singleCommand.contains("GP:") && lineIndex < commands.size())
		{
			singleCommand = commands.get(lineIndex);
			lineIndex++;
			if (singleCommand.contains("PR:"))
			{
				replaceCommands.add(singleCommand);
				amountOfPageReplacementLabel.setText((pageReplacementCounter++).toString());
			}
			if (singleCommand.contains("PF:"))
			{
				amountOfPageFaultLabel.setText((pageFaultConter++).toString());
			}
		}
		
		if (lineIndex < commands.size())
		{
			PageInfo curPage = null;
			String curPageId = singleCommand.substring(6, singleCommand.indexOf("[") - 1);
			if (pagesInfoMap.containsKey(curPageId))
			{
				curPage = pagesInfoMap.get(curPageId);							
			}
			else
			{
				curPage = new PageInfo();
				curPage.pageId = curPageId;
				curPage.processId = singleCommand.substring(4, singleCommand.indexOf(" "));
				curPage.toDisplay = ProcessIdState.get(curPage.processId);
			}

			if (PageIdToColumnMap.containsKey(curPage.pageId) && PageIdToColumnMap.get(curPage.pageId) >= 0)
			{
				columnIndex = PageIdToColumnMap.get(curPage.pageId);
			}
			else if (isRamHasSpace)
			{
				columnIndex++;
				if (columnIndex == ramCapacity - 1)
				{
					isRamHasSpace = false;
				}
			}
			else
			{
				for (String commandLine : replaceCommands) 
				{
					if (commandLine.contains(curPage.pageId))
					{
						//Sample for replacing page - PR:MTH 5 MTR 10
						String pageToRemove = commandLine.substring(commandLine.indexOf("H") + 1, commandLine.indexOf("MTR")).trim();
						columnIndex = PageIdToColumnMap.get(pageToRemove);
						PageIdToColumnMap.put(pageToRemove, -1);
						replaceCommands.remove(commandLine);
						break;
					}
				}
			}
			
			PageIdToColumnMap.put(curPage.pageId, columnIndex);
			pagesInfoMap.put(curPage.pageId ,curPage);
			
			String dataLine = singleCommand.substring(singleCommand.indexOf("[") + 1, singleCommand.indexOf("]"));
			curPage.data = dataLine.split(",");
			
			if (curPage.toDisplay)
			{
				table.getColumn(columnIndex).setText(curPage.pageId);
				
				for (int i = 0; i < curPage.data.length; i++) 
				{
					listOfTableItems.get(i).setText(columnIndex, curPage.data[i]);
				}
			}
			
			singleCommand = "";
		}
		else
		{
			play.setEnabled(false);
			playAll.setEnabled(false);
		}
	}

}
