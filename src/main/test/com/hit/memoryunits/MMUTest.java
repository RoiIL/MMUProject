package com.hit.memoryunits;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;

public class MMUTest {
	
	@SuppressWarnings("deprecation")
	@Test
	public void memoryManagementUnitTest() throws IOException
	{
		IAlgoCache<Long, Long> algo = new LRUAlgoCacheImpl<>(5);
		MemoryManagementUnit mmu = new MemoryManagementUnit(5, algo);
		
		Long[] pageIds = new Long[] {0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L};
		boolean[] writePages = new boolean[pageIds.length];
		Arrays.fill(writePages, true);
		Page<byte[]>[] retPages = mmu.getPages(pageIds, writePages);
		@SuppressWarnings("unchecked")
		Page<byte[]>[] expected = new Page[9];
		
		Long value = 0L;
		for (int i = 0; i < 9; i++)
		{
			expected[i] = new Page<byte[]>(value, value.toString().getBytes());
			value++;
		}
		assertEquals(expected, retPages);
	}
	
	@Test
	public void hardDriveTest() throws FileNotFoundException, IOException 
	{
		HardDisk.getInstance();
		Page<byte[]> page0 = new Page<byte[]>((long) 0, "0".getBytes());
		Page<byte[]> page1 = new Page<byte[]>((long) 2000, "Hello".getBytes());
//		Page<byte[]> page2 = new Page<byte[]>((long) 4000, "World".getBytes());
//		Page<byte[]> page3 = new Page<byte[]>((long) 6000, "Again".getBytes());
		
		Page<byte[]> returnedPage = HardDisk.getInstance().pageReplacement(page1, page0.getPageId());
		assertEquals(page0, returnedPage);
		
		returnedPage = HardDisk.getInstance().pageFault((long) 2000);
		assertEquals(page1, returnedPage);
	}
}
