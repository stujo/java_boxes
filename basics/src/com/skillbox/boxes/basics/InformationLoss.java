package com.skillbox.boxes.basics;

public class InformationLoss {

	public static void main(String args[]) {
		int i1 = Short.MAX_VALUE;
		short s1 = (short) i1;
		System.out.printf("%d == %d -> %b%n", i1, s1, i1 == s1);

		int i2 = Short.MAX_VALUE + 1;
		short s2 = (short) i2;
		System.out.printf("%d == %d -> %b%n", i2, s2, i2 == s2);
		//
		// byte bMax = Byte.MAX_VALUE;
		//
		// System.out.printf("%d == %d -> %b%n", bMax);

		int iMaxB = Byte.MAX_VALUE;
		System.out.printf("%d == %d -> %b%n", iMaxB, Byte.MAX_VALUE,
				iMaxB == Byte.MAX_VALUE);

		int iMaxBytePlus1 = Byte.MAX_VALUE;
		iMaxBytePlus1++;
		
		byte maxBytePlus1 = Byte.MAX_VALUE;
		maxBytePlus1++;
		
		System.out.printf("%d == %d -> %b%n", iMaxBytePlus1, maxBytePlus1,
				iMaxBytePlus1 == maxBytePlus1);
	}
}
