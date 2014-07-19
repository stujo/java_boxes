package com.skillbox.boxes.basics;

enum MyDay {
	SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;

}

enum MyCleverDay {
	SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;

	public String tellItLikeItIs() {
		switch (this) {
		case FRIDAY:
			return "Fridays are cool!";
		case MONDAY:
			return "Mondays are bad.";
		case SATURDAY:
		case SUNDAY:
			return "Weekends are the best!";
		default:
			return "Midweek is meh.";
		}
	}
}

enum MySlickDay {
	SUNDAY("Sunny"), MONDAY("Monie"), TUESDAY("Chewy"), WEDNESDAY("Nessy"), THURSDAY(
			"Thor"), FRIDAY("The Funk"), SATURDAY("Disco");

	private String nickname;

	MySlickDay(String nickname) {
		this.nickname = nickname;
	}

	public String quoteMe(String message) {
		return String.format("%s - says '%s'", message, this.nickname);
	}

	public String howsYourDay() {
		switch (this) {
		case FRIDAY:
			return quoteMe("Fridays are cool!");
		case MONDAY:
			return quoteMe("Mondays are bad.");
		case SATURDAY:
		case SUNDAY:
			return quoteMe("Weekends are the best!");
		default:
			return quoteMe("Midweek is meh.");
		}
	}
}

public class DaysOfTheWeek {

	public static void main(String[] args) {

		System.out.printf("%n%nMyDays%n---------%n");
		for (int i = 0; i < MyDay.values().length; i++) {
			MyDay day = MyDay.values()[i];

			switch (day) {
			case FRIDAY:
				System.out.println("Fridays are cool!");
				break;
			case MONDAY:
				System.out.println("Mondays are bad.");
				break;
			case SATURDAY:
			case SUNDAY:
				System.out.println("Weekends are the best!");
				break;
			default:
				System.out.println("Midweek is meh.");
				break;
			}
		}

		System.out.printf("%n%nMyCleverDays%n-----------%n");
		for (MyCleverDay day : MyCleverDay.values()) {
			System.out.println(day.tellItLikeItIs());
		}

		System.out.printf("%n%nMySlickDays%n-----------%n");
		for (MySlickDay day : MySlickDay.values()) {
			System.out.println(day.howsYourDay());
		}
	}
}
