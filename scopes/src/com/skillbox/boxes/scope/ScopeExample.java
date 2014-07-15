package com.skillbox.boxes.scope;

public class ScopeExample {
  static final int sClassScoped = 100;

  int mInstanceScoped = 200;

  static void classMethod() {
    // sClassScoped is CLASS SCOPED and so is available in static methods
    System.out.printf("%d%n%n", sClassScoped);

    // mInstanceScoped is INSTANCE SCOPED and not available outside of instance
    // methods
    // System.out.printf("%d%n%n", mInstanceScoped);
  }

  public void instanceMethod() {
    // sClassScoped is CLASS SCOPED and so is available in static and instance
    // methods
    System.out.printf("%d%n%n", sClassScoped);

    // mInstanceScoped is INSTANCE SCOPED and is available inside of instance
    // methods
    System.out.printf("%d%n%n", mInstanceScoped);

    final int methodScoped = 300;

    System.out.printf("%d%n%n", methodScoped);

    for (int loopScoped = 0; loopScoped < 10; loopScoped++) {
      System.out.printf("%d%n%n", loopScoped);
    }

    // loopScoped is LOOP SCOPED and NOT available here
    // System.out.printf("%d%n%n", loopScoped);

    {
      final int blockScoped = 400;
      System.out.printf("%d%n%n", blockScoped);
    }

    // blockScoped is BLOCK SCOPED and NOT available here
    // System.out.printf("%d%n%n", blockScoped);
  }

  String shadowy = "INSTANCE SCOPE";

  void shadowingExample() {
    // Here we have access to the instance variable
    System.out.printf("01-ScopeExample:shadowy='%s'%n%n", shadowy);

    // Now we are declaring a method scoped variable of the same name
    // This will 'shadow' the instance variable
    final String shadowy = "METHOD SCOPE";
    System.out.printf("02-ScopeExample:shadowy='%s'%n%n", shadowy);

    // We can still access the instance variable with the this keyword
    System.out.printf("03-ScopeExample:this.shadowy='%s'%n%n", this.shadowy);

    // Call a method to demonstrate shadowing by parameter
    final String parameter = "PARAMETER";
    shadowyMethod(parameter);

    // Notice that even though shadowyMethod assigned a value to the
    // parameter, it does not affect our reference here!
    // That is because only values and references are passed
    // shadowyMethod simply changed the String to which it's local
    // reference was referring to
    System.out.printf("08-ScopeExample:parameter='%s'%n%n", parameter);

  }

  void shadowyMethod(String shadowy) {
    // Here the parameter is shadowing the instance variable of the same name
    System.out.printf("04-ScopeExample:shadowy='%s'%n%n", shadowy);
    // Again we can access the instance variable with the this keyword
    System.out.printf("05-ScopeExample:this.shadowy='%s'%n%n", this.shadowy);

    // We we assign a value to the parameter it does not change the instance
    // variable
    shadowy = "ASSIGNED TO PARAMETER REFERENCE";
    System.out.printf("06-ScopeExample:shadowy='%s'%n%n", shadowy);
    System.out.printf("07-ScopeExample:this.shadowy='%s'%n%n", this.shadowy);
  }

  public static void main(final String[] args) {
    // shadowy is INSTANCE SCOPED and NOT available here
    // System.out.printf("00-ScopeExample:shadowy='%s'%n%n",shadowy);
    final ScopeExample example = new ScopeExample();

    example.shadowingExample();
  }

}
