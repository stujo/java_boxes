package com.skillbox.boxes.dynproxy;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {

		Mathy mathy = new MathyImpl();

		// Fixed Factory
		Mathy mathyproxy = LoggingProxyFactory.getMathyProxy(mathy);

		mathyproxy.add(5, 2);
		mathyproxy.add(51, 513);
		mathyproxy.add(762, 13);
		mathyproxy.add(166, 31536);

		// Genericized Proxy Class
		GenericProxyFactory<Mathy> gpFactory = new GenericProxyFactory<Mathy>();

		Mathy gproxy = gpFactory.getProxy(mathy, Mathy.class);

		gproxy.add(5, 2);
		gproxy.add(51, 513);
		gproxy.add(762, 13);
		gproxy.add(166, 31536);
	}
}
