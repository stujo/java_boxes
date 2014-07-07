package com.skillbox.boxes.object_methods.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LocationTest {

  private static final int Y_VAL = -2;
  private static final int X_VAL = 7;
  private Location mLocation;

  @Before
  public void setUp() throws Exception {
    mLocation = new Location(X_VAL, Y_VAL);
  }

  @After
  public void tearDown() throws Exception {
    mLocation = null;
  }

  @Test
  public void testX() {
    assertEquals(X_VAL, mLocation.getX());
  }

  @Test
  public void testY() {
    assertEquals(Y_VAL, mLocation.getY());
  }

  @Test
  public void testEquality() {
    final Location l2 = new Location(X_VAL, Y_VAL);
    assertEquals(mLocation, l2);
  }

  @Test
  public void testHashCodeEquality() {
    final Location l2 = new Location(X_VAL, Y_VAL);
    assertEquals(mLocation.hashCode(), l2.hashCode());
  }

  @Test
  public void testInqualityY() {
    final Location l2 = new Location(X_VAL, Y_VAL + 1);
    assertNotEquals(mLocation, l2);
  }

  @Test
  public void testInqualityX() {
    final Location l2 = new Location(X_VAL + 1, Y_VAL);
    assertNotEquals(mLocation, l2);
  }
}
