package com.skillbox.boxes.object_methods.geometry;

import java.io.Serializable;

/**
 * Represents a location in a two dimensional space
 * 
 * between {@link Integer#MAX_VALUE} and {@link Integer#MIN_VALUE}
 * 
 * @author stuart
 *
 */
public class Location implements Serializable {
  private static final long serialVersionUID = 1L;
  private int mX;
  private int mY;

  public Location(final int x, final int y) {
    this.mX = x;
    this.mY = y;
  }

  public int getX() {
    return mX;
  }

  public int getY() {
    return mY;
  }

}
