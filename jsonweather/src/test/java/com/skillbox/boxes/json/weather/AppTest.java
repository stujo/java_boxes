package com.skillbox.boxes.json.weather;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for App.
 */
public class AppTest {

  @Test
  public void testProcess() {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    final PrintStream out = new PrintStream(baos);
    final WeatherRequest mockRequest = Mockito.mock(WeatherRequest.class);

    Mockito.doReturn(true).when(mockRequest).loadJSON();
    Mockito.doReturn("San Jose,ca,us").when(mockRequest).getQuery();
    Mockito.doReturn(true).when(mockRequest).successful();
    Mockito.doReturn("Sunny!").when(mockRequest).getWeatherForcast();

    App.process(mockRequest, out, Mockito.mock(Logger.class));

    final String str = baos.toString();

    Assert.assertEquals(
        "Main weather forecast for 'San Jose,ca,us' is 'Sunny!'\n", str);
  }

  @Test
  public void testProcessServerError() {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    final PrintStream out = new PrintStream(baos);
    final WeatherRequest mockRequest = Mockito.mock(WeatherRequest.class);

    Mockito.doReturn(true).when(mockRequest).loadJSON();
    Mockito.doReturn(false).when(mockRequest).successful();
    Mockito.doReturn("San Jose,ca,us").when(mockRequest).getQuery();
    Mockito.doReturn("Lookup Failed").when(mockRequest).getResponseMessage();

    App.process(mockRequest, out, Mockito.mock(Logger.class));

    final String str = baos.toString();

    Assert.assertEquals(
        "An error occur looking up 'San Jose,ca,us' : Lookup Failed\n", str);
  }

  @Test
  public void testProcessFail() {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    final PrintStream out = new PrintStream(baos);
    final WeatherRequest mockRequest = Mockito.mock(WeatherRequest.class);

    Mockito.doReturn(false).when(mockRequest).loadJSON();
    Mockito.doReturn("San Jose,ca,us").when(mockRequest).getQuery();
    Mockito.doReturn("Lookup Failed").when(mockRequest).getResponseMessage();

    App.process(mockRequest, out, Mockito.mock(Logger.class));

    final String str = baos.toString();

    Assert.assertEquals(
        "Unable to search for  'San Jose,ca,us' : Lookup Failed\n", str);
  }

  @Test
  public void testBuildRequestFromArgsWithArg() {
    final String[] args = new String[1];
    args[0] = "test input";
    final WeatherRequest request = App.buildRequestFromArgs(args,
        Mockito.mock(Logger.class));

    Assert.assertEquals(args[0], request.getQuery());
  }

  @Test
  public void testBuildRequestFromArgsWithoutArg() {
    final String[] args = null;

    final WeatherRequest request = App.buildRequestFromArgs(args,
        Mockito.mock(Logger.class));

    Assert.assertNull(request);
  }

}
