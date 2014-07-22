package com.skillbox.boxes.dynproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingProxyFactory {

	static String asString(Object[] args) {
		StringBuilder sb = new StringBuilder("(");
		for (Object arg : args) {
			sb.append(String.format("%n  %s : %s", arg.getClass()
					.getSimpleName(), arg));
		}
		sb.append(String.format("%n);"));
		return sb.toString();
	}

	public static Mathy getMathyProxy(final Mathy s) {

		return (Mathy) Proxy.newProxyInstance(s.getClass().getClassLoader(),
				new Class[] { Mathy.class }, new InvocationHandler() {

					long mCallCounter = 0L;
					Logger mLogger = Logger.getLogger(Mathy.class.getName());

					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						long callNumber = ++mCallCounter;

						mLogger.log(Level.INFO, "#" + callNumber + " Calling "
								+ method.getName() + asString(args));

						Object result = method.invoke(s, args);

						mLogger.log(Level.INFO,
								"#" + callNumber + " " + method.getName()
										+ " returned " + result);
						return result;

					}
				});
	}
}
