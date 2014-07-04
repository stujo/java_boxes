package com.skillbox.boxes.storage;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class StorageSystemFactoryTest {

  @Test
  public void testInMemoryStorage() throws IOException {
    StorageSystem imss = StorageSystemFactory.inMemoryStorage("imss");
    assertThat(imss, instanceOf(InMemoryStorageSystem.class));
  }

  @Test
  public void testFileBasedStorage() throws IOException {
    String rootDir = TestHelper
        .createTempDirectory(StorageSystemFactoryTest.class.getSimpleName());
    try {
      StorageSystem fbss = StorageSystemFactory.fileBasedStorage("fbss",
          rootDir);
      assertThat(fbss, instanceOf(FileBasedStorageSystem.class));
    } finally {
      if (null != rootDir) {
        (new File(rootDir)).delete();
      }
    }
  }

  @Test(expected = InstantiationException.class)
  public void testCannotInstantiateStorageSystemFactory() throws Throwable {
    final Class<?> cls = StorageSystemFactory.class;
    final Constructor<?> c = cls.getDeclaredConstructors()[0];
    c.setAccessible(true);
    try {
      c.newInstance((Object[]) null);
    } catch (InvocationTargetException ite) {
      if (ite.getTargetException() instanceof InstantiationException) {
        throw (InstantiationException) ite.getTargetException();
      }
    }
  }
}
