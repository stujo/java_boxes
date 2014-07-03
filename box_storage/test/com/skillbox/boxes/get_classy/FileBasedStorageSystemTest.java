package com.skillbox.boxes.get_classy;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileBasedStorageSystemTest extends NamedStorageSystemTest {

	private static File sTemporaryRootFolder;

	@BeforeClass
	static public void ensureTemporaryRootFolder() throws IOException {
		sTemporaryRootFolder = File.createTempFile(
				"FileBasedStorageSystemTest", Long.toString(System.nanoTime()));
		// Delete the temporary file
		if (!(sTemporaryRootFolder.delete())) {
			throw new IOException("Could not delete temp file: "
					+ sTemporaryRootFolder.getAbsolutePath());
		}
		// Create a temporary directory in it's place
		if (!(sTemporaryRootFolder.mkdir())) {
			throw new IOException("Could not create temp directory: "
					+ sTemporaryRootFolder.getAbsolutePath());
		}
	}

	@AfterClass
	static public void removeTemporaryRootFolder() throws IOException {
		if (sTemporaryRootFolder != null && sTemporaryRootFolder.exists()) {
			sTemporaryRootFolder.delete();
		}
	}

	@Override
	public NamedStorageSystem createEmptyStorage() throws IOException {
		return new FileBasedStorageSystem("fbss_under_test",
				sTemporaryRootFolder.getAbsolutePath());
	}

	@Test
	public void testGetName() {
		Assert.assertEquals("The name should be stored", "fbss_under_test",
				getStorage().getName());
	}
}