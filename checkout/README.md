Checkout Demo
--------------

A simple java demo app:

* Local Maven Dependency on the jdbcinventory package for compilation

* Shade Plugin to embed local dependencies in the Jar File

* [Cliche command line library](https://code.google.com/p/cliche/) used for command line commands

* Classpath used to reference the non-maven dependencies when the application is started

To run the App
``java -cp "lib/*:target/*" com.skillbox.boxes.checkout.App``


#Interesting Bits

* Cliche Uses introspection of handler methods, to build a command line menu
The ``@Command`` Annotation acts as marker:

__Shell.java__

	private void addDeclaredMethods(Object handler, String prefix) throws SecurityException {
	   for (Method m : handler.getClass().getMethods()) {
	       Command annotation = m.getAnnotation(Command.class);
	       if (annotation != null) {
	           commandTable.addMethod(m, handler, prefix);
	       }
	   }
	}


