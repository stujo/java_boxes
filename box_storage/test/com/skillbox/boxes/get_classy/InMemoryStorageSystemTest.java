package com.skillbox.boxes.get_classy;

import org.junit.Assert;
import org.junit.Test;


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