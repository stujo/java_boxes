package com.skillbox.boxes.dynproxy;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {

		Mathy mathy = new MathyImpl();

		Mathy mathyproxy = LoggingProxyFactory.getMathyProxy(mathy);

		mathyproxy.add(5, 2);
		mathyproxy.add(51, 513);
		mathyproxy.add(762, 13);
		mathyproxy.add(166, 31536);
	}
}
