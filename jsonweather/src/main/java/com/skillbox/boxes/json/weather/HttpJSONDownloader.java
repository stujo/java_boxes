package com.skillbox.boxes.json.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

public class HttpJSONDownloader {

  private final Logger mLogger;

  // The input
  private final URL mURL;

  // The output
  private JSONObject mResponseJSON;

  private int mHTTPResponseCode;

  private IOException mIOException;

  public HttpJSONDownloader(final URL url, final Logger logger) {
    mURL = url;
    mLogger = logger;
  }

  HttpURLConnection getConnection() throws IOException {
    return (HttpURLConnection) mURL.openConnection();
  }

  private String getJSONStringFromAPI() {
    try {
      final HttpURLConnection connection = getConnection();
      mLogger.log(Level.FINE, "Querying URL: '"
          + connection.getURL().toString() + "'");
      // Will block to read
      mHTTPResponseCode = connection.getResponseCode();

      if (mHTTPResponseCode == HttpURLConnection.HTTP_OK) {
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
    } catch (final IOException ioe) {
      mLogger.log(Level.SEVERE, "Exception processing '" + mURL + "'", ioe);
      mIOException = ioe;
    }
    return null;
  }

  public boolean loadData() {

    reset();

    final String rawJson = getJSONStringFromAPI();

    if (null != rawJson) {
      mResponseJSON = new JSONObject(rawJson);
      if (mLogger.isLoggable(Level.FINE)) {
        mLogger.log(Level.FINE, getResponseJSON().toString());
      }
    }

    return successful();
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
    mHTTPResponseCode = 0;
    mIOException = null;
  }

  public boolean successful() {
    return getResponseJSON() != null && mHTTPResponseCode == 200;
  }

  public IOException getIOException() {
    return mIOException;
  }

  public JSONObject getResponseJSON() {
    return mResponseJSON;
  }

}
