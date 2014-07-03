package com.skillbox.boxes.storage;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.skillbox.boxes.storage.NamedStorageSystem;

abstract public class NamedStorageSystemTest {

	private NamedStorageSystem mStorage;

	@After
	public void cleanUp() throws IOException {
		// In case we allocated a file or a connection
		getStorage().discard();
	}

	abstract public NamedStorageSystem createEmptyStorage() throws IOException;

	protected NamedStorageSystem getStorage() {
		return mStorage;
	}

	private void setStorage(NamedStorageSystem storage) {
		mStorage = storage;
	}

	@Before
	public void setUp() throws IOException {
		setStorage(createEmptyStorage());
		getStorage().store("theAnswer", new Integer(42));
	}

	@Test
	public void testDiscardRemovesEntry() {
		getStorage().discard();
		Assert.assertFalse("theAnswer should not exist",
				getStorage().exists("theAnswer"));
	}

	@Test
	public void testExists() {
		Assert.assertTrue("theAnswer should exist",
				getStorage().exists("theAnswer"));
	}

	@Test
	public void testNotExists() {
		Assert.assertFalse("theQuestion should not exist",
				getStorage().exists("theQuestion"));
	}

	@Test
	public void testStore() throws IOException, ClassNotFoundException {

		Double[] myArray = { 3.142, 1.234, 15.00000001 };

		getStorage().store("myArray", myArray);

		Object retrievedArray = getStorage().retrieve("myArray");

		assertThat(retrievedArray, instanceOf(Double[].class));

		if (retrievedArray instanceof Double[]) {
			assertTrue("Stored array is deeply equal",
					java.util.Arrays.deepEquals(myArray,
							(Double[]) retrievedArray));
		}

	}

	@Test
	public void testStoredDataIsCloned() throws IOException,
			ClassNotFoundException {

		HashSet<String> theData = new HashSet<String>();

		theData.add("Hello");

		getStorage().store("theData", theData);

		theData.add("Goodbye");

		Object retrievedRaw = getStorage().retrieve("theData");

		// Because of type erasure it's just a HashSet at runtime
		assertThat(retrievedRaw, instanceOf(HashSet.class));

		if (retrievedRaw instanceof HashSet) {
			HashSet<?> retrieved = (HashSet<?>) retrievedRaw;
			assertFalse(
					"Should not contain Goodbye since we add that after storage",
					retrieved.contains("Goodbye"));
		}
	}

}
