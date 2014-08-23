package com.skillbox.boxes.threads;

class Job implements Runnable {
	String mLabel;
	private long mSleeper;

	public Job(String label, long sleeper) {
		super();
		mLabel = label;
		mSleeper = sleeper;
	}

	public void run() {
		System.out.println("Starting " + mLabel);

		try {
			Thread.sleep(mSleeper);
		} catch (InterruptedException e) {
			System.out.println("Interrupted " + mLabel);
		}

		System.out.println("Ending " + mLabel);
	}

}

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Hello World!");

		Job job1 = new Job("One", 4000);
		Job job2 = new Job("Two", 2000);
		Job job3 = new Job("Three", 3000);

		Thread thread1 = new Thread(job1);
		Thread thread2 = new Thread(job2);
		Thread thread3 = new Thread(job3);

		thread1.start();
		thread2.start();
		thread3.start();

		thread1.join();
		System.out.println("After Thread1 Join");
		thread2.join();
		System.out.println("After Thread2 Join");
		thread3.join();
		System.out.println("After Thread3 Join");

		System.out.println("Goodbye World!");

	}
}
