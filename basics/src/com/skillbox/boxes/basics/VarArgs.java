package com.skillbox.boxes.basics;

public class VarArgs {

	static void doSomething(String... withThese) {
		for (String that : withThese) {
			System.out.println(that);
		}
	}

	public static void main(String[] args) {
		doSomething("Fee", "Fye", "Foe", "Fum");
	}
}
