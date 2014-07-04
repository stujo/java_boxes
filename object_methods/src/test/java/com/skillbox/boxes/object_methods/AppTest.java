package com.skillbox.boxes.object_methods;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName
   *          name of the test case
   */
  public AppTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(AppTest.class);
  }

  public void testRandomizerZero() {
    assertEquals(0, App.randomInt(0, 0));
  }

  public void testRandomizerOne() {
    assertEquals(1, App.randomInt(1, 1));
  }

  public void testRandomizer() {
    int[] rands = new int[1000000];
    for (int i = 0; i < rands.length; i++) {
      rands[i] = App.randomInt(-1, 3);
    }
    double total = 0.0;
    for (int i = 0; i < rands.length; i++) {
      total += rands[i];
    }
    double average = total / rands.length;

    assertEquals(1.0, average, 0.001);

  }
}
