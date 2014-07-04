package com.skillbox.boxes.storage;

import java.io.IOException;

/**
 * A interface defining a key value storage system
 *
 * This is just an example to illustrate interfaces
 *
 * Implementations store serialized copies of objects in a key value store
 *
 * @author stujo
 *
 */
public interface StorageSystem {

  /**
   * Store a copy of the object for later use
   *
   * @param key
   *          The key used later to retrieve the object. The key must be valid
   *          as determined by {@link #isValidKey}
   *
   * @param value
   *          Any non-null serialize-able Object, check this with
   *          {@link #isValidValue}
   *
   * @exception IOException
   *              If there was an error storing the value
   * @exception IllegalArgumentException
   *              If key or value is invalid
   * @see IOException
   */
  abstract void store(String key, Object value) throws IOException;

  /**
   * Retrieve a previously stored object copy
   *
   * @param key
   *          valid {@link #isValidKey} key as used with {@link #store}
   * @return The object copy or null if no key is found
   * @throws IOException
   *           A error occurred reading the object from the store
   * @throws ClassNotFoundException
   *           A previously serialized object could not be restored
   */
  abstract Object retrieve(String key) throws IOException,
      ClassNotFoundException;

  /**
   * Test if a value is stored for the key
   *
   * @param key
   *          valid {@link #isValidKey} key as used with {@link #store}
   * @return true or false
   */
  abstract boolean exists(String key);

  /**
   * Remove a value from the store
   *
   * @param key
   *          valid {@link #isValidKey} key as used with {@link #store}
   */
  abstract void discard(String key);

  /**
   * Remove all values from the store
   *
   */
  abstract void discardAll();

  /**
   * Test if a key is valid for the store
   *
   * @param key
   *          a non-empty String
   *
   * @return true or false
   */
  abstract boolean isValidKey(String key);

  /**
   * Test if an object can be stored in the store
   *
   * @param value
   *
   * @return true or false
   */
  abstract boolean isValidValue(Object value);
}
