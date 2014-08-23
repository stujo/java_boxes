package com.skillbox.boxes.singletons;

class SingleThing {
	private String mLabel;

	public SingleThing(String label) {
		mLabel = label;
		System.out.println("Constructing: " + toString());
	}

	@Override
	public String toString() {
		return "SingleThing [mLabel=" + mLabel + "]";
	}

}

/**
 * Hello world!
 *
 */
public class App {

	/**
	 * Simple Singleton
	 */
	static SingleThing sFirstSingleThing = new SingleThing("First");

	/**
	 * Lazy Load with Double Checked Locking Singleton
	 */
	static SingleThing sSecondSingleThing;

	/**
	 * DO NOT USE! Hmmm: This may not be as great as it might look:
	 * http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
	 * 
	 * JIT Compiler Optimizations May Break This
	 * 
	 * @return
	 */
	static SingleThing getSecondSingleThing() {
		if (sSecondSingleThing == null) {
			synchronized (App.class) {
				if (sSecondSingleThing == null) {
					sSecondSingleThing = new SingleThing("Second");
				}
			}
		}
		return sSecondSingleThing;
	}

	/**
	 * Lazy Load with Lock Singleton
	 */
	static SingleThing sThirdSingleThing;

	static synchronized SingleThing getThirdSingleThing() {
		if (sThirdSingleThing == null) {
			sThirdSingleThing = new SingleThing("Third");
		}
		return sThirdSingleThing;
	}

	/**
	 * Lazy Load with Double Checked Locking volatile Singleton
	 * 
	 * It's been said that volatile reads take longer, so this more verbose
	 * version is optimized for a single volatile read in the common case
	 * 
	 */
	static volatile SingleThing sFourthSingleThing;

	static SingleThing getFourthSingleThing() {

		SingleThing result = sFourthSingleThing;

		if (result == null) {
			synchronized (App.class) {
				if (sFourthSingleThing == null) {
					sFourthSingleThing = result = new SingleThing("Fourth");
				}
			}
		}
		return result;

	}

	public static void main(String[] args) {
		System.out.println("Hello World!");

		@SuppressWarnings("unused")
		SingleThing second = getSecondSingleThing();

		@SuppressWarnings("unused")
		SingleThing third = getThirdSingleThing();

		@SuppressWarnings("unused")
		SingleThing fourth = getFourthSingleThing();
	}
}
