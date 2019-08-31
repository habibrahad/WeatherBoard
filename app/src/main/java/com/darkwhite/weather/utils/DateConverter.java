package com.darkwhite.weather.utils;

public class DateConverter {

  private int bnYear; // Year part of a Bangla date
  private int bnMonth; // Month part of a Bangla date
  private int bnDay; // Day part of a Bangla date
  private int gYear; // Year part of a Gregorian date
  private int gMonth; // Month part of a Gregorian date
  private int leap; // Number of years since the last leap year (0 to 4)
  private int JDN; // Julian Day Number
  private int april; // The 14th april is the first day of Bangla Calender.

  /**
   * JavaSource_Calendar: This constructor receives a Gregorian date and
   * initializes the other private members of the class accordingly.
   *
   * @param year  int
   * @param month int
   * @param day   int
   */
  public DateConverter(int year, int month, int day) {
    setGregorianDate(year, month, day);
  }

  /**
   * getBanglaYear: Returns the 'year' part of the Bangla date.
   *
   * @return int
   */
  public int getBanglaYear() {
    return bnYear;
  }

  /**
   * getBanglaMonth: Returns the 'month' part of the Bangla date.
   *
   * @return int
   */
  public int getBanglaMonth() {
    return bnMonth;
  }

  /**
   * getBanglaDay: Returns the 'day' part of the Bangla date.
   *
   * @return int
   */
  public int getBanglaDay() {
    return bnDay;
  }

  /**
   * setGregorianDate: Sets the date according to the Gregorian calendar and
   * adjusts the other dates.
   *
   * @param year  int
   * @param month int
   * @param day   int
   */
  private void setGregorianDate(int year, int month, int day) {
    gYear = year;
    gMonth = month;
    JDN = gregorianDateToJDN(year, month, day);
    JDNToBangla();
    JDNToJulian();
    JDNToGregorian();
  }

  /**
   * BanglaCalendar: This method determines if the Bangla year is
   * leap (366-day long) or is the common year (365 days), and finds the day
   * in april (Gregorian Calendar)of the first day of the Bangla year
   * ('bnYear').Bangla year (bnYear) ranges from (-61 to 3177).This method
   * will set the following private data members as follows: leap: Number of
   * years since the last leap year (0 to 4)
   */
  private void BanglaCalendar() {
    // Bangla years starting the 33-year rule
    int[] Breaks = {-61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210,
        1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178};
    int jm, N, leapJ, leapG, jp, j, jump;
    gYear = bnYear + 594;
    leapJ = -14;
    jp = Breaks[0];
    // Find the limiting years for the Bangla year 'bnYear'
    j = 1;
    do {
      jm = Breaks[j];
      jump = jm - jp;
      if (bnYear >= jm) {
        leapJ += (jump / 33 * 8 + (jump % 33) / 4);
        jp = jm;
      }
      j++;
    } while ((j < 20) && (bnYear >= jm));
    N = bnYear - jp;
    // current
    // Bangla year in the Bangla calendar
    leapJ += (N / 33 * 8 + ((N % 33) + 3) / 4);
    if (((jump % 33) == 4) && ((jump - N) == 4))
      leapJ++;
    leapG = gYear / 4 - ((gYear / 100 + 1) * 3 / 4) - 150;
    april = 20 + leapJ - leapG;
    // Find how many years have passed since the last leap year
    if ((jump - N) < 6)
      N = N - jump + ((jump + 4) / 33 * 33);
    leap = (((N + 1) % 33) - 1) % 4;
    if (leap == -1)
      leap = 4;
  }

  /**
   * IsLeap: This method determines if the Bangla year is leap
   * (366-day long) or is the common year (365 days), and finds the day in
   * april (Gregorian Calendar)of the first day of the Bangla year
   * ('bnYear').Bangla year (bnYear) ranges from (-61 to 3177).This method
   * will set the following private data members as follows: leap: Number of
   * years since the last leap year (0 to 4) Gy: Gregorian year of the
   * beginning of Bangla year april
   */
  public boolean IsLeap(int bnYear1) {
    // Bangla years starting the 33-year rule
    int[] Breaks = {-61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181, 1210,
        1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178};
    int jm, N, leapJ, leapG, jp, j, jump;
    gYear = bnYear1 + 594;
    leapJ = -14;
    jp = Breaks[0];
    // Find the limiting years for the Bangla year 'bnYear'
    j = 1;
    do {
      jm = Breaks[j];
      jump = jm - jp;
      if (bnYear1 >= jm) {
        leapJ += (jump / 33 * 8 + (jump % 33) / 4);
        jp = jm;
      }
      j++;
    } while ((j < 20) && (bnYear1 >= jm));
    N = bnYear1 - jp;
    // Find the number of leap years from AD 594 to the beginning of the
    // current
    // Bangla year in the Bangla calendar
    leapJ += (N / 33 * 8 + ((N % 33) + 3) / 4);
    if (((jump % 33) == 4) && ((jump - N) == 4))
      leapJ++;
    // And the same in the Gregorian date
    leapG = gYear / 4 - ((gYear / 100 + 1) * 3 / 4) - 150;
    april = 20 + leapJ - leapG;
    // Find how many years have passed since the last leap year
    if ((jump - N) < 6)
      N = N - jump + ((jump + 4) / 33 * 33);
    leap = (((N + 1) % 33) - 1) % 4;
    if (leap == -1)
      leap = 4;
    return leap == 4 || leap == 0;

  }

  /**
   * JDNToBangla: Converts the current value of 'JDN' Julian Day Number to a
   * date in the Bangla calendar. The caller should make sure that the
   * current value of 'JDN' is set correctly. This method first converts the
   * JDN to Gregorian calendar and then to Bangla calendar.
   */
  private void JDNToBangla() {
    JDNToGregorian();
    bnYear = gYear - 594;
    BanglaCalendar(); // This invocation will update 'leap' and 'april'
    int JDN1F = gregorianDateToJDN(gYear, 4, april);
    int k = JDN - JDN1F;
    if (k >= 0) {
      if (k <= 185) {
        bnMonth = 1 + k / 31;
        bnDay = (k % 31) + 1;
        return;
      } else
        k -= 186;
    } else {
      bnYear--;
      k += 179;
      if (leap == 1)
        k++;
    }
    bnMonth = 7 + k / 30;
    bnDay = (k % 30) + 1;
  }

  /**
   * Its not necessary.
   * JDNToJulian: Calculates Julian calendar dates from the julian day number
   * (JDN) for the period since JDN=-34839655 (i.e. the year -100100 of both
   * calendars) to some millions (10^6) years ahead of the present. The
   * algorithm is based on D.A. Hatcher, Q.Jl.R.Astron.Soc. 25(1984), 53-55
   * slightly modified by K.M. Borkowski, Post.Astron. 25(1987), 275-279).
   */
  private void JDNToJulian() {
    int j = 4 * JDN + 139361631;
    int i = ((j % 1461) / 4) * 5 + 308;
    // Month part of a Julian date
    int juMonth = ((i / 153) % 12) + 1;
  }

  /**
   * gergorianDateToJDN: Calculates the julian day number (JDN) from Gregorian
   * calendar dates. This integer number corresponds to the noon of the date
   * (i.e. 12 hours of Universal Time). This method was tested to be good
   * (valid) since 1 april, -100100 (of both calendars) up to a few millions
   * (10^6) years into the future. The algorithm is based on D.A.Hatcher,
   * Q.Jl.R.Astron.Soc. 25(1984), 53-55 slightly modified by K.M. Borkowski,
   * Post.Astron. 25(1987), 275-279.
   *
   * @param year  int
   * @param month int
   * @param day   int
   * @return int
   */
  private int gregorianDateToJDN(int year, int month, int day) {
    int jdn = (year + (month - 8) / 6 + 100100) * 1461 / 4
        + (153 * ((month + 9) % 12) + 2) / 5 + day - 34840408;
    jdn = jdn - (year + 100100 + (month - 8) / 6) / 100 * 3 / 4 + 752;
    return (jdn);
  }

  /**
   * JDNToGregorian: Calculates Gregorian calendar dates from the julian day
   * number (JDN) for the period since JDN=-34839655 (i.e. the year -100100 of
   * both calendars) to some millions (10^6) years ahead of the present. The
   * algorithm is based on D.A. Hatcher, Q.Jl.R.Astron.Soc. 25(1984), 53-55
   * slightly modified by K.M. Borkowski, Post.Astron. 25(1987), 275-279).
   */
  private void JDNToGregorian() {
    int j = 4 * JDN + 139361631;
    j = j + (((((4 * JDN + 183187720) / 146097) * 3) / 4) * 4 - 3908);
    int i = ((j % 1461) / 4) * 5 + 308;
    gMonth = ((i / 153) % 12) + 1;
    gYear = j / 1461 - 100100 + (8 - gMonth) / 6;
  }
  // jaYear)
} // End of Class 'JavaSource_Calendar
