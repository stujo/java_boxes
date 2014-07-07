package com.skillbox.boxes.object_methods;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test object_methods
 */

public class AppTest {

  @Test
  public void testRandomizerZero() {
    Assert.assertEquals(0, App.randomInt(0, 0));
  }

  @Test
  public void testRandomizerOne() {
    assertEquals(1, App.randomInt(1, 1));
  }

  /**
   * This is a reasonable test, but not great, anything with random numbers is
   * not guaranteed to pass But this test reasonably tests that the randomizer
   * produces a an average distribution of values within the specified range
   */
  @Test
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

    assertEquals(1.0, average, 0.1);

  }
}
