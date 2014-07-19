package com.skillbox.boxes.basics;

import java.util.Arrays;

public class BubbleSorter {
	public static void main(String[] args) {

		int nums[] = { 3, 15, 7, 8456, 234, 52, 3252, 2, 1 };

		int temp;

		int copy[] = nums.clone();

		printArray("Unsorted Array", nums);
		for (int a = 1; a < nums.length; a++) {
			for (int b = nums.length - 1; b >= a; b--) {
				if (nums[b - 1] > nums[b]) {
					temp = nums[b - 1];
					nums[b - 1] = nums[b];
					nums[b] = temp;
				}
			}
			printArray("Partially Sorted Array", nums);
		}

		printArray("Sorted Array", nums);

		printArray("Cloned Array", copy);

		Arrays.sort(copy);

		printArray("Arrays Sorted Cloned Array", copy);

		assert (copy.equals(nums));
	}

	private static void printArray(String label, int[] nums) {
		System.out.println(label);
		for (int a = 0; a < nums.length; a++) {
			System.out.printf("%d ", nums[a]);
		}
		System.out.print("\n\n");
	}
}
