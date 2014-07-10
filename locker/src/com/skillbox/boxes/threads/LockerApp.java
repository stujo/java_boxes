package com.skillbox.boxes.threads;

public class LockerApp {
  static class Friend {
    private final String name;

    public Friend(final String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }

    public synchronized void bow(final Friend bower) {
      System.out.format("%s: %s" + "  has bowed to me!%n", this.name,
          bower.getName());
      System.out.format("%s: Now calling bowBack on %s%n", this.name,
          bower.getName());
      bower.bowBack(this);
    }

    public synchronized void bowBack(final Friend bower) {
      System.out.format("%s: %s" + " has bowed back to me!%n", this.name,
          bower.getName());
    }
  }

  public static void main(final String[] args) {
    System.out.println("Starting Locker");

    final Friend alphonse = new Friend("Alphonse");

    final Friend gaston = new Friend("Gaston");

    new Thread(new Runnable() {
      @Override
      public void run() {
        alphonse.bow(gaston);
      }
    }).start();
    new Thread(new Runnable() {
      @Override
      public void run() {
        gaston.bow(alphonse);
      }
    }).start();

    System.out.println("Ending Locker");
  }

}
