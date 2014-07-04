package com.skillbox.boxes.storage;

import java.io.IOException;
import java.io.Serializable;

/**
 * Abstract class for StorageSystem implementations to handle common
 * functionality such as name and key/value validations
 * 
 * {@link #storeImpl(String, Object)} should be implemented in place of
 * {@link #store(String, Object)} because we have validated the input parameters
 * 
 */
abstract class NamedStorageSystem implements StorageSystem {

  protected static final int MAX_KEY_STRING_LENGTH = 100;
  private String mName;

  public NamedStorageSystem(String name) {
    this.mName = name;
  }

  @Override
  public String getName() {
    return this.mName;
  }

  @Override
  final public boolean isValidKey(String key) {
    return (key != null) && (key.length() > 0)
        && (key.length() < MAX_KEY_STRING_LENGTH);
  }

  @Override
  final public boolean isValidValue(Object value) {
    return (value != null) && (value instanceof Serializable);
  }

  @Override
  public void store(String key, Object value) throws IOException {
    if (isValidKey(key)) {
      if (isValidValue(value)) {
        storeImpl(key, value);
      } else {
        throw new IllegalArgumentException(String.format(
            "Invalid value provided for '%1$s'", key));
      }
    } else {
      throw new IllegalArgumentException(String.format(
          "'%1$s' is an invalid key", key));
    }
  }

  abstract protected void storeImpl(String validKey, Object validValue)
      throws IOException;

}
