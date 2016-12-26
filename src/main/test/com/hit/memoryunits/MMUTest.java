package com.hit.memoryunits;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.junit.Test;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;

public class MMUTest {

	@Test
	public void test() throws FileNotFoundException, IOException 
	{
		HashMap<Long, Page<byte[]>> dataOnFile = new HashMap<>();
		Page<byte[]> page1 = new Page<byte[]>((long) 2000, "Hello".getBytes());
		Page<byte[]> page2 = new Page<byte[]>((long) 4000, "World".getBytes());
		Page<byte[]> page3 = new Page<byte[]>((long) 6000, "Again".getBytes());
		
		dataOnFile.put((long) 2000, page1);
		dataOnFile.put((long) 4000, page2);
		
		FileOutputStream hdFile = new FileOutputStream("HdPages");
		ObjectOutputStream writeData = new ObjectOutputStream(hdFile);
		writeData.writeObject(dataOnFile);
		writeData.flush();
		hdFile.close();
		
		Page<byte[]> returnedPage = HardDisk.getInstance().pageFault((long) 2000);
		assertEquals(page1, returnedPage);
		
		returnedPage = HardDisk.getInstance().pageReplacement(page3, (long) 4000);
		assertEquals(page2, returnedPage);
		
		returnedPage = HardDisk.getInstance().pageFault((long) 6000);
		assertEquals(page3, returnedPage);
		
		hdFile.close();
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void MemoryManagementUnitTest() throws IOException
	{
		IAlgoCache<Long, Long> algo = new LRUAlgoCacheImpl<>(5);
		MemoryManagementUnit mmu = new MemoryManagementUnit(5, algo);
		
		Long[] pageIds = new Long[] {0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L};
		Page<byte[]>[] retPages = mmu.getPages(pageIds);
		@SuppressWarnings("unchecked")
		Page<byte[]>[] expected = new Page[9];
		
		Long value = 4L;
		for (int i = 0; i < 5; i++)
		{
			expected[i] = new Page<byte[]>(value, value.toString().getBytes());
			value++;
		}
		assertEquals(expected, retPages);
	}

}
