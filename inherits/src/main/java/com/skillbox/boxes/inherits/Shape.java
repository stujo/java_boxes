package com.skillbox.boxes.inherits;

public abstract class Shape {

	protected Shape() {
		System.out.println("Shape Constructor");
	}

	abstract public double area();

	@Override
	abstract public String toString();

	@Override
	abstract public int hashCode();

	@Override
	abstract public boolean equals(Object other);
}
