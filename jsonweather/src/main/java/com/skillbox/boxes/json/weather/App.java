package com.skillbox.boxes.json.weather;

import java.io.PrintStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Check the weather
 */
public class App {
  static void process(final WeatherRequest request, final PrintStream out,
      final Logger logger) {
    if (request.loadData()) {
      if (request.successful()) {
        out.println(String.format("Main weather forecast for '%s' is '%s'",
            request.getQuery(), request.getWeatherForcast()));
      } else {
        out.format("An error occur looking up '%s' : %s%n", request.getQuery(),
            request.getResponseMessage());
      }
    } else {
      out.format("Unable to search for  '%s' : %s%n", request.getQuery(),
          request.getResponseMessage());
    }
  }

  public static void main(final String[] args) {
    final Logger logger = Logger.getLogger(App.class.getName());

    setupDebugLogging(args, logger);

    final WeatherRequest request = buildRequestFromArgs(args, logger);

    if (request != null) {
      process(request, System.out, logger);
    }
  }

  private static void setupDebugLogging(final String[] args, final Logger logger) {

    boolean debugging = false;

    if (args != null && args.length >= 2) {
      debugging = debugging || args[1].equals("debug");
    }

    if (debugging) {
      logger.setLevel(Level.ALL);
      for (final Handler h : logger.getParent().getHandlers()) {
        if (h instanceof ConsoleHandler) {
          h.setLevel(Level.ALL);
        }
      }
    }
  }

  private static WeatherRequest buildRequestFromArgs(final String[] args,
      final Logger logger) {

    WeatherRequest wr = null;

    if (args != null && args.length >= 1) {
      wr = new WeatherRequest(args[0], logger);
    } else {
      logger.log(Level.SEVERE,
          "A query is required as an argument e.g.'London,uk'");
    }

    return wr;
  }
}
