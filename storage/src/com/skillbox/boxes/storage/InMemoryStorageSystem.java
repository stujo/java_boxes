package com.skillbox.boxes.storage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class InMemoryStorageSystem extends NamedStorageSystem {
  public InMemoryStorageSystem(String name) {
    super(name);
    mStorage = new HashMap<String, byte[]>();
  }

  HashMap<String, byte[]> mStorage;

  @Override
  public boolean exists(String key) {
    return mStorage.containsKey(key);
  }

  @Override
  public void discardAll() {
    mStorage.clear();
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
    mStorage.put(validKey, convertObjectToByteArray(validValue));
  }

  @Override
  public Object retrieve(String key) throws IOException, ClassNotFoundException {
    return convertByteArrayToObject(mStorage.get(key));
  }

  @Override
  public void discard(String key) {
    mStorage.remove(key);
  }
}
