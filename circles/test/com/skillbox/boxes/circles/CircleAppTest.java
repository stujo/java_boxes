package com.skillbox.boxes.circles;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CircleAppTest {

	@Test
	public void test() {

		double[] radii = { 1, 2, 3, 4 };

		assertEquals(radii.length, CircleApp.getCircleInfo(radii).length);
	}
}
