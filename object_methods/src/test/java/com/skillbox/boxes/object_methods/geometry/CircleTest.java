package com.skillbox.boxes.object_methods.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CircleTest {

  private static final String NAME_VAL = "Bob";
  private static final int Y_VAL = 0;
  private static final int X_VAL = -4;
  private static final int RADIUS_VAL = 17;
  private Circle mCircle;

  @Before
  public void setUp() throws Exception {
    mCircle = new Circle(X_VAL, Y_VAL, RADIUS_VAL, NAME_VAL);
  }

  @After
  public void tearDown() throws Exception {
    mCircle = null;
  }

  @Test
  public void testX() {
    assertEquals(X_VAL, mCircle.getCenter().getX());
  }

  @Test
  public void testY() {
    assertEquals(Y_VAL, mCircle.getCenter().getY());
  }

  @Test
  public void testRadius() {
    assertEquals(RADIUS_VAL, mCircle.getRadius());
  }

  @Test
  public void testEquality() {
    final Circle c2 = new Circle(X_VAL, Y_VAL, RADIUS_VAL, NAME_VAL);
    assertEquals(mCircle, c2);
  }

  @Test
  public void testHashCodeEquality() {
    final Circle c2 = new Circle(X_VAL, Y_VAL, RADIUS_VAL, NAME_VAL);
    assertEquals(mCircle.hashCode(), c2.hashCode());
  }

  @Test
  public void testInqualityY() {
    final Circle c2 = new Circle(X_VAL, Y_VAL + 1, RADIUS_VAL);
    assertNotEquals(mCircle, c2);
  }

  @Test
  public void testInqualityX() {
    final Circle c2 = new Circle(X_VAL + 1, Y_VAL, RADIUS_VAL, NAME_VAL);
    assertNotEquals(mCircle, c2);
  }

  @Test
  public void testInqualityRadius() {
    final Circle c2 = new Circle(X_VAL, Y_VAL, RADIUS_VAL + 1, NAME_VAL);
    assertNotEquals(mCircle, c2);
  }

  @Test
  public void testInqualityName() {
    final Circle c2 = new Circle(X_VAL, Y_VAL, RADIUS_VAL, "Mary");
    assertNotEquals(mCircle, c2);
  }

  @Test
  public void testInqualityNullName() {
    final Circle c2 = new Circle(X_VAL, Y_VAL, RADIUS_VAL, null);
    assertNotEquals(mCircle, c2);
    assertNotEquals(c2, mCircle);
  }

}
