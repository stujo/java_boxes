package com.skillbox.boxes.json.weather;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherRequest {

  static final String API_HOST = "api.openweathermap.org";
  static final String API_PATH = "/data/2.5/weather";
  static final String API_PROTOCOL = "http";

  // The input
  private final String mQuery;

  // The output
  private final Logger mLogger;

  private boolean mDownloadResult;
  private JSONObject mResponseJSON;

  public WeatherRequest(final String query, final Logger logger) {
    mQuery = query;
    mLogger = logger;
  }

  public String getQuery() {
    return mQuery;
  }

  private void reset() {
    mResponseJSON = null;
    mDownloadResult = false;
  }

  public boolean loadJSON() {
    reset();

    final URI uri = buildURI();
    if (uri != null) {
      try {
        final HttpJSONDownloader downloader = buildDownloader(uri);

        mDownloadResult = downloader.loadData();
        mResponseJSON = downloader.getResponseJSON();

      } catch (final MalformedURLException e) {
        mLogger.log(Level.SEVERE, "Unable to build URI to WEATHER API", e);
      }
    }
    return mDownloadResult;
  }

  URI buildURI() {
    URI uri = null;
    try {
      uri = new URI(API_PROTOCOL, API_HOST, API_PATH, "q=" + getQuery(), null);
    } catch (final URISyntaxException e) {
      mLogger.log(Level.SEVERE, "Unable to build URI to WEATHER API", e);
    }
    return uri;
  }

  HttpJSONDownloader buildDownloader(final URI uri)
      throws MalformedURLException {
    final HttpJSONDownloader downloader = new HttpJSONDownloader(uri.toURL(),
        mLogger);
    return downloader;
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

  public String getResponseMessage() {
    String message = null;
    if (mResponseJSON.has("message")) {
      message = mResponseJSON.getString("message");
    }
    return message;
  }

  public boolean successful() {
    return mDownloadResult && (null != mResponseJSON)
        && 200 == mResponseJSON.getInt("cod");
  }
}
