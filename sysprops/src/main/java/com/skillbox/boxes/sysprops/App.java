package com.skillbox.boxes.sysprops;

/*
 * Hello world!
 *
 * Example invocation:
 *
 * java -Dcom.skillbox.boxes.sysprops.mykey=',or universe!' -cp target/sysprops-0.0.1-SNAPSHOT.jar com.skillbox.boxes.sysprops.App
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World! "
				+ System.getProperty("com.skillbox.boxes.sysprops.mykey"));
	}
}
