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
  JSONObject mResponseJSON;

  // The input
  private final String mQuery;
  private final Logger mLogger;
  private String mResponseMessage;
  private int mResponseCode;

  public WeatherRequest(final String query, final Logger logger) {
    mQuery = query;
    mLogger = logger;
  }

  public boolean loadData() {

    reset();

    if (getQuery() != null && getQuery().length() > 1) {

      final String rawJson = getJSONStringFromAPI();

      if (null != rawJson) {

        mResponseJSON = new JSONObject(rawJson);

        mLogger.log(Level.FINE, mResponseJSON.toString());

        mResponseCode = mResponseJSON.getInt("cod");

        if (mResponseJSON.has("message")) {
          mResponseMessage = mResponseJSON.getString("message");
        }
      } else {
        mResponseMessage = "Unable to get weather for '" + getQuery() + "'";
      }
    } else {
      mResponseMessage = "Invalid query '" + getQuery() + "'";
    }

    return mResponseJSON != null;
  }

  private String getJSONStringFromAPI() {
    try {
      final HttpURLConnection connection = getConnection();
      if (connection != null) {
        mLogger.log(Level.FINE, "Querying: '" + connection.getURL().toString()
            + "'");
        // Will block to read
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
          InputStreamReader isr = null;
          try {
            isr = new InputStreamReader(connection.getInputStream());
            return readRawText(isr);
          } finally {
            if (isr != null) {
              try {
                isr.close();
              } catch (final IOException e) {
              }
            }
          }
        }
      } else {
        logError("Unable to open connection to API");
      }
    } catch (final IOException ioe) {
      logError("Exception processing '" + getQuery() + "'", ioe);
    }
    return null;
  }

  private String readRawText(final InputStreamReader isr) throws IOException {
    BufferedReader bufferedReader = null;
    try {
      final StringBuffer rawText = new StringBuffer();
      bufferedReader = new BufferedReader(isr);

      String line = null;
      while ((line = bufferedReader.readLine()) != null) {
        rawText.append(line);
      }

      if (rawText.length() > 0) {
        return rawText.toString();
      }
    } finally {
      if (bufferedReader != null) {
        try {
          bufferedReader.close();
        } catch (final IOException e) {
        }
      }
    }
    return null;
  }

  private void reset() {
    mResponseJSON = null;
    mResponseCode = 0;
    mResponseMessage = null;
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

    String weatherMessage = null;

    if (mResponseJSON.has("weather")) {
      final JSONArray weatherArray = mResponseJSON.getJSONArray("weather");
      final JSONObject weather = weatherArray.getJSONObject(0);
      weatherMessage = weather.getString("main");
    }
    return weatherMessage;
  }

  public String getQuery() {
    return mQuery;
  }

  public boolean successful() {
    return mResponseJSON != null && mResponseCode == 200;
  }

  public String getResponseMessage() {
    return mResponseMessage;
  }

}
