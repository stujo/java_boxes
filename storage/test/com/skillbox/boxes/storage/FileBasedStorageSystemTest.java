package com.skillbox.boxes.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileBasedStorageSystemTest extends NamedStorageSystemTest {

  private static File sTemporaryRootFolder;

  @BeforeClass
  static public void ensureTemporaryRootFolder() throws IOException {
    sTemporaryRootFolder = new File(
        TestHelper.createTempDirectory(FileBasedStorageSystemTest.class
            .getSimpleName()));
  }

  @AfterClass
  static public void removeTemporaryRootFolder() throws IOException {
    sTemporaryRootFolder.delete();
  }

  @Override
  public NamedStorageSystem createEmptyStorage() throws IOException {
    return new FileBasedStorageSystem("fbss_under_test",
        sTemporaryRootFolder.getAbsolutePath());
  }

  @Test(expected = FileNotFoundException.class)
  public void testInvalidRootFolder() throws IOException {
    new FileBasedStorageSystem("name", "()@#@!");
  }

  @Test
  public void testGetName() {
    Assert.assertEquals("The name should be stored", "fbss_under_test",
        getStorage().getName());
  }
}