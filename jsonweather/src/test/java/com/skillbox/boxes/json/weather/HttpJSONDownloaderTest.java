package com.skillbox.boxes.json.weather;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class HttpJSONDownloaderTest {

  static URL sFakeURL;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    sFakeURL = new URL(TestHelper.FAKE_HTTP_EXAMPLE_COM_API_URL);
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
  public void testAPIStatus404() throws Exception {

    final int fakeHttpResponseCode = HttpURLConnection.HTTP_NOT_FOUND;
    final String fakeJSONResponse = "";

    final HttpJSONDownloader downloader = buildDownloaderForTest(sFakeURL,
        fakeHttpResponseCode, fakeJSONResponse);

    final boolean loaded = downloader.loadData();

    Assert.assertFalse("Expected Load to Fail", loaded);
  }

  @Test
  public void testAPIStatusOK() throws Exception {

    final int fakeHttpResponseCode = HttpURLConnection.HTTP_OK;
    final String fakeJSONResponse = TestHelper.WEATHER_JSON_STRING;

    final HttpJSONDownloader downloader = buildDownloaderForTest(sFakeURL,
        fakeHttpResponseCode, fakeJSONResponse);

    final boolean loaded = downloader.loadData();

    Assert.assertTrue("Expected Load to Succeed", loaded);

    final JSONObject result = downloader.getResponseJSON();

    Assert.assertEquals("Should Have Sunset Time", 1405124969, result
        .getJSONObject("sys").getInt("sunset"));
  }

  private HttpJSONDownloader buildDownloaderForTest(final URL fakeURL,
      final int fakeHttpResponseCode, final String fakeJSONResponse)
          throws IOException {

    final Logger mockLogger = Mockito.mock(Logger.class);
    final HttpJSONDownloader downloader = Mockito.spy(new HttpJSONDownloader(
        fakeURL, mockLogger));

    final HttpURLConnection mockConnection = Mockito
        .mock(HttpURLConnection.class);

    Mockito.doReturn(mockConnection).when(downloader).getConnection();

    final ByteArrayInputStream bais = new ByteArrayInputStream(
        fakeJSONResponse.getBytes());

    Mockito.doReturn(fakeURL).when(mockConnection).getURL();
    Mockito.doReturn(fakeHttpResponseCode).when(mockConnection)
    .getResponseCode();
    Mockito.doReturn(bais).when(mockConnection).getInputStream();

    return downloader;

    // boolean loaded = downloader.loadData();
    //
    // final String output = baos.toString();
    // return output;
  }
}
