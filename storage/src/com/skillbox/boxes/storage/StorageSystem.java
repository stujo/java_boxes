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
  abstract void store(String key, Object value) throws IOException;

  abstract Object retrieve(String key) throws IOException,
      ClassNotFoundException;

  abstract boolean exists(String key);

  abstract void discard(String key);

  abstract void discardAll();

  abstract boolean isValidKey(String key);

  abstract boolean isValidValue(Object value);
}
