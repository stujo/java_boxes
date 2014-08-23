package com.skillbox.boxes.fibonacci;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class AppTest {
	@Test
	public void testCollectFibsUpTo() {
		ArrayList<Integer> fibs = App.collectFibsUpTo(10);
		Object[] expected = { 0, 1, 1, 2, 3, 5, 8 };
		assertArrayEquals(expected, fibs.toArray());
	}
}
