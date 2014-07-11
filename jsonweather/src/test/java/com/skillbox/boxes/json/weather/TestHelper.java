package com.skillbox.boxes.json.weather;

import org.json.JSONObject;

public class TestHelper {
  static final String WEATHER_JSON_STRING = "{\"id\":5120797,\"dt\":1405043760,\"clouds\":{\"all\":75},\"coord\":{\"lon\":-74.04,\"lat\":41.12},\"wind\":{\"speed\":2.1,\"deg\":160},\"cod\":200,\"sys\":{\"message\":0.0509,\"sunset\":1405124969,\"sunrise\":1405071243,\"country\":\"United States of America\"},\"name\":\"Spring Valley\",\"base\":\"cmc stations\",\"weather\":[{\"id\":701,\"icon\":\"50n\",\"description\":\"mist\",\"main\":\"Mist\"}],\"rain\":{\"3h\":0},\"main\":{\"humidity\":77,\"pressure\":1020,\"temp_max\":297.15,\"temp_min\":292.15,\"temp\":294.35}}";
  static final String FAILED_WEATHER_JSON_STRING = "{\"message\":\"failed on server\",\"cod\":404}";

  static final String FAKE_HTTP_EXAMPLE_COM_API_URL = "http://example.com/api";
  static final JSONObject WEATHER_JSON = new JSONObject(WEATHER_JSON_STRING);
  static final JSONObject FAILED_WEATHER_JSON = new JSONObject(
      FAILED_WEATHER_JSON_STRING);
}
