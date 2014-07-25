package com.skillbox.boxes.dynproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenericProxyFactory<T> {

	static String asString(Object[] args) {
		StringBuilder sb = new StringBuilder("(");
		for (Object arg : args) {
			sb.append(String.format("%n  %s : %s", arg.getClass()
					.getSimpleName(), arg));
		}
		sb.append(String.format("%n);"));
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public T getProxy(final T target, Class<?> interfaze) {

		if (!interfaze.isInstance(target) || !interfaze.isInterface()) {
			throw new IllegalArgumentException(String.format(
					"Target %s must implement the supplied interface %s",
					target.getClass().getName(), interfaze.getName()));
		}

		return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
				new Class[] { interfaze }, new InvocationHandler() {

					long mCallCounter = 0L;
					Logger mLogger = Logger.getLogger(target.getClass()
							.getName());

					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						long callNumber = ++mCallCounter;

						mLogger.log(Level.INFO, "#" + callNumber + " Calling "
								+ method.getName() + asString(args));

						Object result = method.invoke(target, args);

						mLogger.log(Level.INFO,
								"#" + callNumber + " " + method.getName()
										+ " returned " + result);
						return result;

					}
				});
	}
}
