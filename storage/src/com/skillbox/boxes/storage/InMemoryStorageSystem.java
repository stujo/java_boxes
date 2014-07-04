package com.skillbox.boxes.storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * An implementation of StorageSystem Stores serialized objects in memory in a
 * key value map
 *
 * @see StorageSystem
 */

class InMemoryStorageSystem extends NamedStorageSystem {
  public InMemoryStorageSystem(String name) {
    super(name);
    this.mStorage = new HashMap<String, byte[]>();
  }

  HashMap<String, byte[]> mStorage;

  @Override
  public boolean exists(String key) {
    return this.mStorage.containsKey(key);
  }

  @Override
  public void discardAll() {
    this.mStorage.clear();
  }

  protected byte[] convertObjectToByteArray(Object value) throws IOException {
    // Serialize data object to a byte array
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bos);
    out.writeObject(value);
    out.close();
    byte[] bytes = bos.toByteArray();
    return bytes;
  }

  protected Object convertByteArrayToObject(byte[] bytes) throws IOException,
  ClassNotFoundException {
    if (bytes != null) {
      ByteArrayInputStream source = null;

      try {
        source = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = null;
        try {
          objectInputStream = new ObjectInputStream(source);
          return objectInputStream.readObject();
        } finally {
          if (objectInputStream != null) {
            objectInputStream.close();
          }
        }
      } finally {
        if (source != null) {
          source.close();
        }
      }
    } else {
      return null;
    }
  }

  @Override
  public void storeImpl(String validKey, Object validValue) throws IOException {
    this.mStorage.put(validKey, convertObjectToByteArray(validValue));
  }

  @Override
  public Object retrieve(String key) throws IOException, ClassNotFoundException {
    return convertByteArrayToObject(this.mStorage.get(key));
  }

  @Override
  public void discard(String key) {
    this.mStorage.remove(key);
  }
}
