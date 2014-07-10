package com.skillbox.boxes.docsample.sub;

import java.util.Enumeration;

/**
 * An enumeration wrapper around a String[]
 * <p>
 * Allows the client to iterate over a String[] using the Enumeration syntax
 *
 * @author stuart
 *
 */
public class Demo implements Enumeration<String> {

  private final String[] mStrings;
  private int mNextIndex;

  /**
   *
   * @param strings
   *          The array of strings to iterate
   */
  public Demo(final String[] strings) {
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
