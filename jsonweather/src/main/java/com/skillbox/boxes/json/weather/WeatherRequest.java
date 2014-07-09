package com.skillbox.boxes.json.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherRequest {

  static final String API_PROTOCOL = "http";
  static final String API_HOST = "api.openweathermap.org";
  static final String API_PATH = "/data/2.5/weather";

  // The output
  JSONObject mJSONResponse;

  // The input
  private final String mQuery;
  private final Logger mLogger;

  public WeatherRequest(final String query, final Logger logger) {
    mQuery = query;
    mLogger = logger;
  }

  public boolean loadData() {
    mJSONResponse = null;
    if (getQuery() != null && getQuery().length() > 1) {

      HttpURLConnection connection;
      try {
        connection = getConnection();
        if (connection != null) {
          mLogger.log(Level.FINE, "Querying: '"
              + connection.getURL().toString() + "'");
          // Will block to read
          if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStreamReader isr = null;

            final StringBuffer rawJson = new StringBuffer();

            try {
              isr = new InputStreamReader(connection.getInputStream());

              // @see
              // http://docs.oracle.com/javase/6/docs/api/java/io/BufferedReader.html
              BufferedReader bufferedReader = null;
              try {
                bufferedReader = new BufferedReader(isr);

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                  rawJson.append(line);
                }

                mJSONResponse = new JSONObject(rawJson.toString());

                mLogger.log(Level.FINE, mJSONResponse.toString());

              } finally {
                if (bufferedReader != null) {
                  try {
                    bufferedReader.close();
                  } catch (final IOException e) {
                  }
                }
              }
            } finally {
              if (isr != null) {
                try {
                  isr.close();
                } catch (final IOException e) {
                }
              }
            }

          }
        }
      } catch (final IOException ioe) {
        logError("Exception processing '" + getQuery() + "'", ioe);
      }
    } else {
      logError("Invalid query '" + getQuery() + "'");
    }

    return mJSONResponse != null;
  }

  private void logError(final String message, final Exception e) {
    mLogger.log(Level.SEVERE, message, e);
  }

  private void logError(final String message) {
    mLogger.log(Level.SEVERE, message);
  }

  private HttpURLConnection getConnection() throws IOException {

    HttpURLConnection conn = null;
    URI uri = null;
    try {
      uri = new URI(API_PROTOCOL, API_HOST, API_PATH, "q=" + getQuery(), null);
      final URL url = uri.toURL();
      conn = (HttpURLConnection) url.openConnection();
    } catch (final URISyntaxException e) {
      logError("Unable to create URI for '" + getQuery() + "'", e);
    } catch (final MalformedURLException e) {
      logError("Unable to create valid URL for '" + getQuery() + "'", e);
    }

    return conn;
  }

  public String getWeatherForcast() {
    final JSONArray weatherArray = mJSONResponse.getJSONArray("weather");
    final JSONObject weather = weatherArray.getJSONObject(0);
    return weather.getString("main");
  }

  public String getQuery() {
    return mQuery;
  }
}
