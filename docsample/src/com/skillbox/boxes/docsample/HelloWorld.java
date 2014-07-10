package com.skillbox.boxes.docsample;

import com.skillbox.boxes.docsample.sub.SecretStringEnumerator;

/**
 * Simple application which prints Hello World to System.out.
 *
 * <p>
 * This is an example for javadoc generation
 * </p>
 *
 * <pre>
 * http://www.oracle.com/technetwork/java/javase/documentation/index-137868.html
 * </pre>
 *
 * <p>
 * Generate full documents with:
 * </p>
 *
 * <pre>
 * javadoc -d ./doc -sourcepath ./src -subpackages com.skillbox.boxes.docsample
 * </pre>
 *
 * <p>
 * or use the ant task added to build.xml
 * </p>
 *
 * <pre>
 * ant doc
 * </pre>
 *
 * @author Stuart
 */
public class HelloWorld {
  /**
   * Prints Hello World to System.out
   * <p>
   * This method does nothing but print out 'Hello World'
   *
   * @param args
   *          Arguments are ignored
   * @see System
   */
  public static void main(final String[] args) {

    final String[] messages = {
        "Hello", "World"
    };
    final SecretStringEnumerator demo = new SecretStringEnumerator(messages);
    boolean first = true;
    while (demo.hasMoreElements()) {
      if (!first) {
        System.out.print(" ");
      } else {
        first = false;
      }
      System.out.print(demo.nextElement());
    }
  }
}
