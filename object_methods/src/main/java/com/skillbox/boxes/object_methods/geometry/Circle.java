package com.skillbox.boxes.object_methods.geometry;

import java.io.Serializable;

public class Circle implements Serializable {
  private static final long serialVersionUID = 1L;
  private Location mCenter;
  private int mRadius;
  private String mName;
  private transient String mMemo;

  @Override
  public int hashCode() {
    // TODO Auto-generated method stub
    return super.hashCode();
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer(super.toString());

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
    mCenter = new Location(x, y);
    mRadius = radius;
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

  public void setMemo(String memo) {
    mMemo = memo;
  }

  public String getName() {
    return mName;
  }

  public void setName(String name) {
    mName = name;
  }
}
