package com.skillbox.boxes.basics;

import java.io.IOException;

public class InputBreak {
	public static void main(String[] args) throws IOException {
		System.out.printf("Pressed 'x' to eXit%n");

		char input;

		for (;;) {
			// Depending on the console implementation this may block
			// until user presses return (not what you might expect)
			input = (char) System.in.read(); // get input
			if (input == 'x') {
				break;
			}
			System.out.print(input);
		}
	}
}
