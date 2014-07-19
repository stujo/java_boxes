package com.skillbox.boxes.basics;

public class Bits {
	public static void main(String[] args) {

		System.out.printf("-----Trying << -------------%n");

		int x = -600;
		for (int i = 0; i < 33; i++) {
			System.out.printf("%2d : %s : %d%n", i, Integer.toBinaryString(x),
					x);
			x = x << 1;
		}

		System.out.printf("-----Trying >> -------------%n");
		x = -600;
		for (int i = 0; i < 33; i++) {
			System.out.printf("%2d : %s : %d%n", i, Integer.toBinaryString(x),
					x);
			x = x >> 1;
		}

		System.out.printf("-----Trying >>> ------------%n");
		x = -600;
		for (int i = 0; i < 33; i++) {
			System.out.printf("%2d : %s : %d%n", i, Integer.toBinaryString(x),
					x);
			x = x >>> 1;
		}

	}
}
