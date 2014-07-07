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
    return this.mX;
  }

  public int getY() {
    return this.mY;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.mX;
    result = (prime * result) + this.mY;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Location other = (Location) obj;
    if (this.mX != other.mX) {
      return false;
    }
    if (this.mY != other.mY) {
      return false;
    }
    return true;
  }

}
