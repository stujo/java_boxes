package com.skillbox.boxes.storage;

import java.io.IOException;

/**
 * A interface defining a key value storage system
 * 
 * This is just an example to illustrate interfaces
 * 
 * 
 * @author stuart
 *
 */
interface StorageSystem {
  public abstract void store(String key, Object value) throws IOException;

  public abstract Object retrieve(String key) throws IOException,
      ClassNotFoundException;

  public abstract boolean exists(String key);

  public abstract void discard(String key);

  public abstract void discardAll();

  public abstract boolean isValidKey(String key);

  public abstract boolean isValidValue(Object value);
}
