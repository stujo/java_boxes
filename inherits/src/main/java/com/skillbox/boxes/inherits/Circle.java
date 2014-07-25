package com.skillbox.boxes.inherits;

public class Circle extends Shape {
	private double mRadius;

	public Circle(double radius) {
		System.out.println("Circle Constructor");
		mRadius = radius;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "{ radius=" + mRadius + " }";
	}

	public double getRadius() {
		return mRadius;
	}

	public void setRadius(double radius) {
		mRadius = radius;
	}

	@Override
	public double area() {
		return mRadius * mRadius * Math.PI;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(mRadius);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Circle other = (Circle) obj;
		if (Double.doubleToLongBits(mRadius) != Double
				.doubleToLongBits(other.mRadius))
			return false;
		return true;
	}
}
