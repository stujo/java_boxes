package com.skillbox.boxes.storage;

import java.io.IOException;
import java.io.Serializable;

public abstract class NamedStorageSystem implements StorageSystem {

  private String mName;

  public NamedStorageSystem(String name) {
    mName = name;
  }

  public String getName() {
    return mName;
  }

  @Override
  public boolean isValidKey(String key) {
    return key != null && key.length() > 0 && key.length() < getMaxKeyLength();
  }

  @Override
  public boolean isValidValue(Object value) {
    return value != null && value instanceof Serializable;
  }

  protected int getMaxKeyLength() {
    return 100;
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
