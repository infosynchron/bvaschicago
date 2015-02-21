package com.bvas.utils;

import java.text.ParseException;

import org.apache.log4j.Logger;

public class DateUtils {
  private static final Logger logger = Logger.getLogger(DateUtils.class);

  public static String convertUSToMySQLFormat(String usDate) {
    // logger.error("usDate:" + usDate);
    String mysqlDate = "";
    java.text.SimpleDateFormat usSDF = new java.text.SimpleDateFormat("MM-dd-yyyy");
    java.text.SimpleDateFormat mysqlSDF = new java.text.SimpleDateFormat("yyyy-MM-dd");

    try {

      java.util.Date dd = usSDF.parse(usDate);
      mysqlDate = mysqlSDF.format(dd);

    } catch (ParseException e) {
      logger.error(e);
    }

    return mysqlDate;
  }

  public static String convertMySQLToUSFormat(String mysqlDate) {
    String usDate = "";
    java.text.SimpleDateFormat usSDF = new java.text.SimpleDateFormat("MM-dd-yyyy");
    java.text.SimpleDateFormat mysqlSDF = new java.text.SimpleDateFormat("yyyy-MM-dd");
    // logger.error("mysqlDate:" + mysqlDate);
    try {

      java.util.Date dd = mysqlSDF.parse(mysqlDate);
      usDate = usSDF.format(dd);

    } catch (ParseException e) {
      logger.error(e);
    }

    return usDate;
  }

  public static String getNewUSDate() {
    String usDate = "";
    java.util.Date dd = new java.util.Date();
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy");
    usDate = sdf.format(dd);

    return usDate;
  }

  public static String getNewUSDateForInvoice() {
    String usDate = "";
    /*
     * try { java.util.Date dd1 = new java.util.Date(); java.text.SimpleDateFormat sdf = new
     * java.text.SimpleDateFormat("MM-dd-yyyy"); usDate = sdf.format(dd1); java.util.Date dd2 =
     * sdf.parse(sdf.format(dd1));
     * 
     * Calendar cc = java.util.Calendar.getInstance(); cc.setTime(dd1); int daysToAdd = 1;
     * //logger.error(dd1.toString()); //logger.error("Day Is: " + cc.get(cc.DAY_OF_WEEK) + "  :" +
     * Calendar.SATURDAY); if (cc.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) { daysToAdd = 2; }
     * 
     * if (dd1.getTime() - dd2.getTime() > (3600000 * 13)) { dd1.setTime(dd1.getTime() + (3600000 *
     * 24 * (long)daysToAdd)); } usDate = sdf.format(dd1); } catch (Exception e) {
     * logger.error(e.getMessage()); }
     */

    usDate = getNewUSDate();

    return usDate;
  }

  public static long getLongFromUSDate(String usDate) {
    try {
      java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy");
      java.util.Date dd1 = sdf.parse(usDate);
      return dd1.getTime();
    } catch (Exception e) {
      return 0L;
    }
  }

  public static String getUSDateFromLong(long usDate) {
    return "";
  }

}
