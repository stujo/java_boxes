package com.skillbox.boxes.object_methods.geometry;

import java.io.Serializable;

/**
 *
 * Note: this class has a natural ordering that is inconsistent with equals.
 *
 *
 * @author stuart
 */
public final class Circle implements Serializable {
  private static final long serialVersionUID = 1L;
  private final Location mCenter;
  private final int mRadius;
  private final String mName;
  private transient String mMemo;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((mCenter == null) ? 0 : mCenter.hashCode());
    result = prime * result + ((mName == null) ? 0 : mName.hashCode());
    result = prime * result + mRadius;
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Circle other = (Circle) obj;
    if (mCenter == null) {
      if (other.mCenter != null) {
        return false;
      }
    } else if (!mCenter.equals(other.mCenter)) {
      return false;
    }
    if (mName == null) {
      if (other.mName != null) {
        return false;
      }
    } else if (!mName.equals(other.mName)) {
      return false;
    }
    if (mRadius != other.mRadius) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer(super.toString());

    sb.append("{");
    sb.append(String.format("\n  name=[%s]", getName()));
    sb.append(String.format("\n  x=[%d]", getCenter().getX()));
    sb.append(String.format("\n  y=[%d]", getCenter().getY()));
    sb.append(String.format("\n  radius=[%d]", getRadius()));
    sb.append(String.format("\n  memo=[%s]", getMemo()));
    sb.append("\n}");

    return sb.toString();
  }

  public Circle(final int x, final int y, final int radius) {
    this(x, y, radius, null);
  }

  public Circle(final int x, final int y, final int radius, final String name) {
    mCenter = new Location(x, y);
    mRadius = radius;
    mName = name;
  }

  public int getRadius() {
    return mRadius;
  }

  public Location getCenter() {
    return mCenter;
  }

  public String getMemo() {
    return mMemo;
  }

  public void setMemo(final String memo) {
    mMemo = memo;
  }

  public String getName() {
    return mName;
  }
}
