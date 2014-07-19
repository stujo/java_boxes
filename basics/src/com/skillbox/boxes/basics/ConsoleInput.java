package com.skillbox.boxes.basics;

import java.io.IOException;

import jline.TerminalFactory;
import jline.console.ConsoleReader;

/***
 * NOTE THIS IS NOT AN EXAMPLE, STILL READING ABOUT JLINE
 * 
 * 
 * @author stuart
 *
 */


public class ConsoleInput {
	public static void main(String[] args) {
		try {
			ConsoleReader console = new ConsoleReader();
			console.setPrompt("prompt> ");
			String line = null;
			while ((line = console.readLine()) != null) {
				console.setPrompt(line + ">");
				
				if (line.startsWith("q")) {
					console.println("Quitting!");
					console.flush();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				TerminalFactory.get().restore();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
