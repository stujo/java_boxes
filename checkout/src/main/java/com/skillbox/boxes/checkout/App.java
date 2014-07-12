package com.skillbox.boxes.checkout;

import java.io.IOException;

import asg.cliche.Command;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;

/**
 * Checkout Time
 *
 */
public class App {

  @Command
  public String loadInventory(final String inventoryFile) {
    return "testing![" + inventoryFile + "]";
  }

  public static void main(final String[] args) throws IOException {
    System.out.println("Welcome to the checkout!");
    final Shell shell = ShellFactory.createConsoleShell("checkout", "",
        new App());

    shell.commandLoop();
  }
}
