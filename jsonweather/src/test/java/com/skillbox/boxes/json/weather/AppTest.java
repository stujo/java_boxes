package com.skillbox.boxes.json.weather;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for Weather App.
 */
public class AppTest {

  @Test
  public void testProcessFailureAccordingToAPI() throws Exception {

    final String fakeQueryString = "San Francisco,ca,us";
    final String fakeAPIUrl = "http://example.com/api";
    final int fakeHttpResponseCode = HttpURLConnection.HTTP_OK;
    final String fakeJSONResponse = "{message:\"test_failure\", cod:404}";

    final String output = processFakeWeatherRequest(fakeQueryString,
        fakeAPIUrl, fakeHttpResponseCode, fakeJSONResponse);

    Assert.assertEquals(
        "An error occur looking up 'San Francisco,ca,us' : test_failure\n",
        output);
  }

  @Test
  public void testProcessSuccessAccordingToAPI() throws Exception {

    final String fakeQueryString = "Spring Valley,ny,us";
    final String fakeAPIUrl = "http://example.com/api";
    final int fakeHttpResponseCode = HttpURLConnection.HTTP_OK;
    final String fakeJSONResponse = "{\"id\":5120797,\"dt\":1405043760,\"clouds\":{\"all\":75},\"coord\":{\"lon\":-74.04,\"lat\":41.12},\"wind\":{\"speed\":2.1,\"deg\":160},\"cod\":200,\"sys\":{\"message\":0.0509,\"sunset\":1405124969,\"sunrise\":1405071243,\"country\":\"United States of America\"},\"name\":\"Spring Valley\",\"base\":\"cmc stations\",\"weather\":[{\"id\":701,\"icon\":\"50n\",\"description\":\"mist\",\"main\":\"Mist\"}],\"rain\":{\"3h\":0},\"main\":{\"humidity\":77,\"pressure\":1020,\"temp_max\":297.15,\"temp_min\":292.15,\"temp\":294.35}}";

    final String output = processFakeWeatherRequest(fakeQueryString,
        fakeAPIUrl, fakeHttpResponseCode, fakeJSONResponse);

    Assert.assertEquals(
        "Main weather forecast for 'Spring Valley,ny,us' is 'Mist'\n", output);
  }

  private String processFakeWeatherRequest(final String fakeQueryString,
      final String fakeAPIUrl, final int fakeHttpResponseCode,
      final String fakeJSONResponse) throws IOException, MalformedURLException {
    final Logger mockLogger = Mockito.mock(Logger.class);
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    final WeatherRequest request = Mockito.spy(new WeatherRequest(
        fakeQueryString, mockLogger));

    final HttpURLConnection mockConnection = Mockito
        .mock(HttpURLConnection.class);

    Mockito.doReturn(mockConnection).when(request).getConnection();

    final ByteArrayInputStream bais = new ByteArrayInputStream(
        fakeJSONResponse.getBytes());

    final URL dummyUrl = new URL(fakeAPIUrl);

    Mockito.doReturn(dummyUrl).when(mockConnection).getURL();
    Mockito.doReturn(fakeHttpResponseCode).when(mockConnection)
        .getResponseCode();
    Mockito.doReturn(bais).when(mockConnection).getInputStream();

    final PrintStream out = new PrintStream(baos);
    App.process(request, out, mockLogger);
    out.close();
    final String output = baos.toString();
    return output;
  }
}
