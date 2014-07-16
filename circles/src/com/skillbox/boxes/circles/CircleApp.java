package com.skillbox.boxes.circles;

public class CircleApp {

	static final int RADIUS_INDEX = 0;
	static final int AREA_INDEX = 1;
	static final int CIRCUMFERENCE_INDEX = 2;

	static double[][] getCircleInfo(double[] radii) {
		double[][] result = new double[radii.length][3];

		for (int i = 0; i < radii.length; i++) {
			result[i][RADIUS_INDEX] = radii[i];
			result[i][AREA_INDEX] = Math.PI * radii[i] * radii[i];
			result[i][CIRCUMFERENCE_INDEX] = Math.PI * radii[i] * 2;
		}

		return result;
	}

	public static void main(String[] args) {

		double[] radii = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

		double[][] table = getCircleInfo(radii);

		for (double[] circleInfo : table) {
			System.out.printf("%f,%f,%f,%f%n", circleInfo[RADIUS_INDEX],
					circleInfo[AREA_INDEX], circleInfo[CIRCUMFERENCE_INDEX],
					circleInfo[AREA_INDEX] / circleInfo[CIRCUMFERENCE_INDEX]);
		}

	}

}
