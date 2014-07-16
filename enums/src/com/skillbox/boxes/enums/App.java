package com.skillbox.boxes.enums;

enum Color {
	RED, WHITE, BLUE;
}

public class App {
	public static void main(String[] args) {
		System.out.printf("toString %s%n", Color.RED);
		System.out.printf("toString %s%n", Color.WHITE);
		System.out.printf("toString %s%n", Color.BLUE);

		for (Color c : Color.values()) {
			System.out.printf("In foreach %s%n", c);

			switch (c) {
			case BLUE:
				System.out.printf("In switch[BLUE] %s%n", c);
				break;
			case RED:
				System.out.printf("In switch[RED] %s%n", c);
				break;
			case WHITE:
				System.out.printf("In switch[WHITE] %s%n", c);
				break;
			default:
				System.out.printf("In switch[default] %s%n", c);
				break;
			}
		}
	}
}
