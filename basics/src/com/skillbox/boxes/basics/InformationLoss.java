package com.skillbox.boxes.basics;

public class InformationLoss {

	public static void main(String args[]) {
		int i1 = Short.MAX_VALUE;
		short s1 = (short) i1;
		System.out.printf("%d == %d -> %b%n", i1, s1, i1 == s1);

		int i2 = Short.MAX_VALUE + 1;
		short s2 = (short) i2;
		System.out.printf("%d == %d -> %b%n", i2, s2, i2 == s2);
	}
}
