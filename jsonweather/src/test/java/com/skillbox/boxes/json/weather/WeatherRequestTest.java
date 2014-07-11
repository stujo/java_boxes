package com.skillbox.boxes.json.weather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class WeatherRequestTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testBuildURI() throws MalformedURLException, URISyntaxException {
    final WeatherRequest request = new WeatherRequest("San Diego,ca,us",
        Mockito.mock(Logger.class));

    final URI uri = request.buildURI();

    assertEquals("Expecting escaped san diego", WeatherRequest.API_PROTOCOL
        + "://" + WeatherRequest.API_HOST + WeatherRequest.API_PATH
        + "?q=San%20Diego,ca,us", uri.toString());
  }

  @Test
  public void testSuccess() throws MalformedURLException {
    final String queryTerm = "Hello";
    final boolean loadDataResult = true;

    final WeatherRequest request = buildRequestWithMockedDownloader(queryTerm,
        TestHelper.WEATHER_JSON, loadDataResult);

    assertTrue("load should succeed", request.loadJSON());
    assertTrue("should be succesful", request.successful());

    assertEquals("Should be Misty", "Mist", request.getWeatherForcast());
  }

  @Test
  public void testServerError() throws MalformedURLException {
    final String queryTerm = "Hello";
    final boolean loadDataResult = true;

    final WeatherRequest request = buildRequestWithMockedDownloader(queryTerm,
        TestHelper.FAILED_WEATHER_JSON, loadDataResult);

    assertTrue("load should succeed", request.loadJSON());
    assertFalse("should be unsuccessful", request.successful());

    assertEquals("failed on server", request.getResponseMessage());
  }

  @Test
  public void testLoadFail() throws MalformedURLException {
    final String queryTerm = "Hello";
    final JSONObject mockJSON = Mockito.mock(JSONObject.class);
    final boolean loadDataResult = false;

    final WeatherRequest request = buildRequestWithMockedDownloader(queryTerm,
        mockJSON, loadDataResult);

    assertFalse("load should fail", request.loadJSON());
    assertFalse("should be unsuccessful", request.successful());
  }

  WeatherRequest buildRequestWithMockedDownloader(final String queryTerm,
      final JSONObject mockJSON, final boolean loadDataResult)
          throws MalformedURLException {
    final WeatherRequest request = Mockito.spy(new WeatherRequest(queryTerm,
        Mockito.mock(Logger.class)));

    final HttpJSONDownloader mockDownloader = Mockito
        .mock(HttpJSONDownloader.class);
    Mockito.doReturn(loadDataResult).when(mockDownloader).loadData();
    Mockito.doReturn(mockJSON).when(mockDownloader).getResponseJSON();

    Mockito.doReturn(mockDownloader).when(request)
    .buildDownloader(Mockito.any(URI.class));
    return request;
  }

}
