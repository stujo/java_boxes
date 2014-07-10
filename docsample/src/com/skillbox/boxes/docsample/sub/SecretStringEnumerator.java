package com.skillbox.boxes.docsample.sub;

import java.util.Enumeration;

/**
 * An enumeration wrapper around a String[]
 * <p>
 * Allows the client to iterate over a String[] using the Enumeration syntax
 * </p>
 *
 * @author stuart
 *
 */
public class SecretStringEnumerator implements Enumeration<String> {

  /**
   * The secret array of Strings
   */
  private final String[] mStrings;

  /**
   * The next cursor index
   */
  private int mNextIndex;

  /**
   * Construct a new SecretStringEnumerator Instance
   *
   * @param strings
   *          The array of strings to iterate
   */
  public SecretStringEnumerator(final String[] strings) {
    mStrings = strings;
    mNextIndex = 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasMoreElements() {
    return mStrings != null && mNextIndex < mStrings.length;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String nextElement() {
    return mStrings[mNextIndex++];
  }

}
