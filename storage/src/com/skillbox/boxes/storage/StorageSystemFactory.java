package com.skillbox.boxes.storage;

import java.io.IOException;

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
   */
  public static StorageSystem fileBasedStorage(String name, String rootPath) {
    try {
      return new FileBasedStorageSystem(name, rootPath);
    } catch (IOException e) {
      return null;
    }
  }
}
