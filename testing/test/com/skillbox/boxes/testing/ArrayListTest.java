package com.skillbox.boxes.testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class ArrayListTest {

	@Test
	public void testAdd() {
		ArrayList<Integer> list = new ArrayList<Integer>();

		list.add(42);

		assertEquals("Should have one in it", 1, list.size());
	}
}
