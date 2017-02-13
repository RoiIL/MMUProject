package com.hit.view;

import java.util.ArrayList;
import java.util.List;
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
		amountOfPageFaultLabel.setText ("3");
		
		Label pageReplacementLable = new Label (pageSection, SWT.TOP);
		pageReplacementLable.setText ("Page Replacement Amount:");
		Label amountOfPageReplacementLabel = new Label (pageSection, SWT.NONE);
		amountOfPageReplacementLabel.setText ("1");
		

		Button ok = new Button (shell, SWT.PUSH);
		ok.setText("OK");
		ok.setSize(10,20);
		ok.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				ok.setText("PUSHED");				
				ok.setSize(200,40);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
				
			}
		});
		
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.heightHint = 100;
		table.setLayoutData(data);
		String[] titles = {" ", "C", "!", "Description", "Resource", "In Folder", "Location"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}
		int count = 5;
		for (int i=0; i<count; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (0, "x");
			item.setText (1, "y");
			item.setText (2, "!");
			item.setText (3, "this stuff behaves the way I expect");
			item.setText (4, "almost everywhere");
			item.setText (5, "some.folder");
			item.setText (6, "line " + i + " in nowhere");
		}
		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}
		
		Button cancel = new Button (shell, SWT.PUSH);
		cancel.setText("Cancel");
		shell.setDefaultButton (cancel);
		shell.pack();
		shell.open ();
		
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

}
