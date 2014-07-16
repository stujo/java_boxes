package com.skillbox.boxes.filing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;

public class App {

	public static void appendText(File file, String text) throws IOException {
		BufferedWriter out = null;
		FileWriter fw = null;

		try {
			fw = new FileWriter(file, true);
			out = new BufferedWriter(fw);
			out.write(text);
		} finally {
			if (out != null) {
				out.close();
			}
			if (fw != null) {
				fw.close();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.MEDIUM);

		java.util.Date date = new java.util.Date();

		String filename = "testfile.txt";

		File file = new File(filename);

		if (file.exists()) {
			System.out.printf("Attempting to append to existing file %s%n",
					file.getAbsoluteFile());
		} else {
			System.out.printf("Attempting to create new file %s%n",
					file.getAbsoluteFile());
		}

		appendText(file, String.format("Hello Again %s%n", df.format(date)));

		dumpFile(file);
	}

	public static void dumpFile(File file) throws IOException {

		FileInputStream in = null;

		try {
			in = new FileInputStream(file);
			int c;

			while ((c = in.read()) != -1) {
				System.out.write(c);
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}
}
