package com.skillbox.boxes.storage;

import org.junit.Assert;
import org.junit.Test;

import com.skillbox.boxes.storage.InMemoryStorageSystem;
import com.skillbox.boxes.storage.NamedStorageSystem;


public class InMemoryStorageSystemTest extends NamedStorageSystemTest {

	@Override
	public NamedStorageSystem createEmptyStorage() {
		return new InMemoryStorageSystem("under_test");
	}


	@Test
	public void testGetName() {
		Assert.assertEquals("The name should be stored", "under_test",
				getStorage().getName());
	}
}