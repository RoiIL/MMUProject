package com.hit.memoryunits;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;

public class MMUTest {

	@Test
	public void test() throws FileNotFoundException, IOException 
	{
		HashMap<Long, Page<byte[]>> dataOnFile = new HashMap<>();
		Page<byte[]> page1 = new Page<byte[]>((long) 5, "Hello".getBytes());
		Page<byte[]> page2 = new Page<byte[]>((long) 7, "World".getBytes());
		Page<byte[]> page3 = new Page<byte[]>((long) 9, "Again".getBytes());
		
		dataOnFile.put((long) 5, page1);
		dataOnFile.put((long) 7, page2);
		
		FileOutputStream hdFile = new FileOutputStream("HD");
		ObjectOutputStream writeData = new ObjectOutputStream(hdFile);
		writeData.writeObject(dataOnFile);
		writeData.flush();
		hdFile.close();
		
		Page<byte[]> returnedPage = HardDisk.getInstance().pageFault((long) 5);
		assertEquals(page1, returnedPage);
		
		returnedPage = HardDisk.getInstance().pageReplacement(page3, (long) 7);
		assertEquals(page2, returnedPage);
		
		returnedPage = HardDisk.getInstance().pageFault((long) 9);
		assertEquals(page3, returnedPage);
		
		hdFile.close();
	}

}
