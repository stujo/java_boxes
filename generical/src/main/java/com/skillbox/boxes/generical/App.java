package com.skillbox.boxes.generical;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		String tapped = tap("Hello");
		System.out.println("Tap result='" + tapped + "'");
		String shadow = emptyShadow("Hello");
		System.out.println("emptyShadow result='" + shadow + "'");
	}

	/**
	 * Example Generic Method
	 * 
	 * @param object
	 * @return
	 */
	static <T> T tap(T object) {
		System.out
				.println("Tapping instance of:" + object.getClass().getName());
		return object;
	}

	/**
	 * Example Generic Method
	 * 
	 * @param object
	 * @return
	 */
	static <T> T emptyShadow(T object) {

		try {
			return (T) object.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
