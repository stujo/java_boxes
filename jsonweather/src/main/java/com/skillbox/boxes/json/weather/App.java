package com.skillbox.boxes.json.weather;

import java.io.PrintStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Check the weather
 *
 */
public class App {
  static void process(final WeatherRequest request, final PrintStream out,
      final Logger logger) {
    if (request.loadData()) {
      out.println(String.format("Main weather forecast for '%s' is '%s'",
          request.getQuery(), request.getWeatherForcast()));
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

  //
  // static private String ParseResult(final String json) throws JSONException {
  //
  // final JSONObject jsonObject = new JSONObject(json);
  //
  // final String parsedResult = "Number of object = " + jsonObject.length()
  // + "\n\n";
  //
  // // "coord"
  // final JSONObject JSONObject_coord = jsonObject.getJSONObject("coord");
  // final Double result_lon = JSONObject_coord.getDouble("lon");
  // final Double result_lat = JSONObject_coord.getDouble("lat");
  //
  // // "sys"
  // final JSONObject JSONObject_sys = jsonObject.getJSONObject("sys");
  // final String result_country = JSONObject_sys.getString("country");
  // final int result_sunrise = JSONObject_sys.getInt("sunrise");
  // final int result_sunset = JSONObject_sys.getInt("sunset");
  //
  // // "weather"
  // String result_weather;
  // final JSONArray JSONArray_weather = jsonObject.getJSONArray("weather");
  // if (JSONArray_weather.length() > 0) {
  // final JSONObject JSONObject_weather = JSONArray_weather.getJSONObject(0);
  // final int result_id = JSONObject_weather.getInt("id");
  // final String result_main = JSONObject_weather.getString("main");
  // final String result_description = JSONObject_weather
  // .getString("description");
  // final String result_icon = JSONObject_weather.getString("icon");
  //
  // result_weather = "weather\tid: " + result_id + "\tmain: " + result_main
  // + "\tdescription: " + result_description + "\ticon: " + result_icon;
  // } else {
  // result_weather = "weather empty!";
  // }
  //
  // // "base"
  // final String result_base = jsonObject.getString("base");
  //
  // // "main"
  // final JSONObject JSONObject_main = jsonObject.getJSONObject("main");
  // final Double result_temp = JSONObject_main.getDouble("temp");
  // final Double result_pressure = JSONObject_main.getDouble("pressure");
  // final Double result_humidity = JSONObject_main.getDouble("humidity");
  // final Double result_temp_min = JSONObject_main.getDouble("temp_min");
  // final Double result_temp_max = JSONObject_main.getDouble("temp_max");
  //
  // // "wind"
  // final JSONObject JSONObject_wind = jsonObject.getJSONObject("wind");
  // final Double result_speed = JSONObject_wind.getDouble("speed");
  // // Double result_gust = JSONObject_wind.getDouble("gust");
  // final Double result_deg = JSONObject_wind.getDouble("deg");
  // final String result_wind = "wind\tspeed: " + result_speed + "\tdeg: "
  // + result_deg;
  //
  // // "clouds"
  // final JSONObject JSONObject_clouds = jsonObject.getJSONObject("clouds");
  // final int result_all = JSONObject_clouds.getInt("all");
  //
  // // "dt"
  // final int result_dt = jsonObject.getInt("dt");
  //
  // // "id"
  // final int result_id = jsonObject.getInt("id");
  //
  // // "name"
  // final String result_name = jsonObject.getString("name");
  //
  // // "cod"
  // final int result_cod = jsonObject.getInt("cod");
  //
  // return "coord\tlon: " + result_lon + "\tlat: " + result_lat + "\n"
  // + "sys\tcountry: " + result_country + "\tsunrise: " + result_sunrise
  // + "\tsunset: " + result_sunset + "\n" + result_weather + "\n"
  // + "base: " + result_base + "\n" + "main\ttemp: " + result_temp
  // + "\thumidity: " + result_humidity + "\tpressure: " + result_pressure
  // + "\ttemp_min: " + result_temp_min + "\ttemp_max: " + result_temp_min
  // + "\n" + result_wind + "\n" + "clouds\tall: " + result_all + "\n"
  // + "dt: " + result_dt + "\n" + "id: " + result_id + "\n" + "name: "
  // + result_name + "\n" + "cod: " + result_cod + "\n" + "\n";
  // }
}
