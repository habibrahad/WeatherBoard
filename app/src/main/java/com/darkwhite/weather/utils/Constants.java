package com.darkwhite.weather.utils;

public class Constants {
  public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
  public static final String UNITS = "metric";
  public static final String[] DAYS_OF_WEEK_english = {
      "Sunday",
      "Monday",
      "Tuesday",
      "Wednesday",
      "Thursday",
      "Friday",
      "Saturday"
  };
  public static final String[] MONTH_NAME_english = {
      "January",
      "February",
      "March",
      "April",
      "May",
      "June",
      "July",
      "August",
      "September",
      "October",
      "November",
      "December"
  };

  public static final String[] DAYS_OF_WEEK_bangla = {
      "রবিবার",
      "সোমবার",
      "মঙ্গলবার",
      "বুধবার",
      "বৃহস্পতিবার",
      "শুক্রবার",
      "শনিবার"
  };
  public static final String[] MONTH_NAME_bangla = {
      "জানুয়ারি",
      "ফেব্রুয়ারি",
      "মার্চ",
      "এপ্রিল",
      "মে",
      "জুন",
      "জুলাই",
      "আগস্ট",
      "সেপ্টেম্বর",
      "অক্টোবর",
      "নভেম্বের",
      "ডিসেম্বর"
  };

  public static final String[] WEATHER_STATUS_english = {
      "Thunderstorm",
      "Drizzle",
      "Rain",
      "Snow",
      "Atmosphere",
      "Clear",
      "Few Clouds",
      "Broken Clouds",
      "Cloud"
  };

  public static final String[] WEATHER_STATUS_bangla = {
      "বজ্রপাত সহ ঝড়",
      "গুড়ি গুড়ি বৃষ্টি",
      "বৃষ্টি",
      "তুষারপাত",
      "অস্থির/দমকা পরিবেশ",
      "পরিষ্কার",
      "হালকা মেঘাছন্ন",
      "গুটি-গুটি মেঘ",
      "মেঘাচ্ছন্ন"

  };


  public static final String CITY_INFO = "city-info";

  public static final long TIME_TO_PASS = 6 * 600000;

  public static final String LAST_STORED_CURRENT = "last-stored-current";
  public static final String LAST_STORED_MULTIPLE_DAYS = "last-stored-multiple-days";
  public static final String OPEN_WEATHER_MAP_WEBSITE = "https://home.openweathermap.org/api_keys";

  public static final String API_KEY = "api-key";
  public static final String LANGUAGE = "language";
  public static final String DARK_THEME = "dark-theme";
}
