package com.skillbox.boxes.storage;

import java.io.IOException;

/**
 * Factory for StorageSystem implementations to hide implementation classes from
 * clients
 * 
 * @author stujo
 *
 */
public class StorageSystemFactory {

  private StorageSystemFactory() throws InstantiationException {
    throw new InstantiationException("Instances of this type are forbidden.");
  }

  /**
   * Return an in memory storage
   *
   * @param name
   *          The name you want to use to identify this instance
   * 
   * @return an in memory storage instance
   */
  public static StorageSystem inMemoryStorage(String name) {
    return new InMemoryStorageSystem(name);
  }

  /**
   * Return a file based storage
   *
   * @param name
   *          The name you want to use to identify this instance
   * 
   * @return a file based storage
   * @throws IOException
   */
  public static StorageSystem fileBasedStorage(String name, String rootPath)
      throws IOException {
    return new FileBasedStorageSystem(name, rootPath);
  }
}
