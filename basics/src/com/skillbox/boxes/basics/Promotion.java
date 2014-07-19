package com.skillbox.boxes.basics;

public class Promotion {
	public static void main(String[] args) {

		short x = 5;
		short y = 2;
		short shortSum = (short) (y * x);
		int intSum = (y * x);

		System.out.printf("%d == %d -> %b%n", shortSum, intSum,
				shortSum == intSum);

	}
}
