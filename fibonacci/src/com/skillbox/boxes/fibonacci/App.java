package com.skillbox.boxes.fibonacci;

import java.util.ArrayList;

public class App {
	public static void main(String[] args) {
		int limit = 2000;

		printFibsUpTo(limit);

		ArrayList<Integer> fibList = collectFibsUpTo(limit);
		for (int fib : fibList) {
			System.out.printf("List %d%n", fib);
		}
	}

	private static void printFibsUpTo(int limit) {
		int[] current = { 0, 1 };
		while (current[1] < limit) {
			System.out.printf("Print %d%n", current[1]);
			int last = current[0];
			current[0] = current[1];
			current[1] = last + current[0];
		}

		System.out.printf("Did not print %d because it's greater than %d%n",
				current[1], limit);
	}

	private static ArrayList<Integer> collectFibsUpTo(int limit) {

		ArrayList<Integer> result = new ArrayList<Integer>();
		result.add(0);
		result.add(1);
		int next = 1;
		while (next < limit) {
			result.add(next);
			next = result.get(result.size() - 2)
					+ result.get(result.size() - 1);
		}

		System.out.printf("Did not add %d because it's greater than %d%n",
				next, limit);

		return result;
	}
}
