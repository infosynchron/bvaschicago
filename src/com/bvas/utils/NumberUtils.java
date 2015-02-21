package com.bvas.utils;

import java.text.NumberFormat;

import org.apache.log4j.Logger;

public class NumberUtils {
  private static final Logger logger = Logger.getLogger(NumberUtils.class);

  public static double cutFractions(double orig) {
    double removed = 0.0;
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMinimumFractionDigits(2);
    nf.setMaximumFractionDigits(2);
    try {
      removed = Double.parseDouble(nf.format(orig));
    } catch (Exception e) {
      removed = 0;
      logger.error("Error When Parsing the number in NumberUtils" + e);
    }
    return removed;
  }

  public static String removeChar(String orig, String ch) {
    String removed = "";
    if (orig.indexOf(ch) != -1) {
      removed =
          orig.substring(0, orig.indexOf(ch))
              + removeChar(orig.substring(orig.indexOf(ch) + 1), ch);
    } else {
      removed = orig;
    }
    return removed;
  }

  public static void main(String[] args) {
    double o = 1559.129;
    o = cutFractions(o, 2);
  }

  public static double cutFractions(double orig, int rnd) {
    double removed = 0.0;
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMinimumFractionDigits(rnd);
    nf.setMaximumFractionDigits(rnd);
    try {
      removed = Double.parseDouble(nf.format(orig));
    } catch (Exception e) {
      removed = 0;
      logger.error("Error When Parsing the number in NumberUtils" + e);
    }
    return removed;
  }

  public static String cutFractions(String orig, int rnd) {
    String removed = "";
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMinimumFractionDigits(rnd);
    nf.setMaximumFractionDigits(rnd);
    try {
      removed = nf.format(Double.parseDouble(orig));
    } catch (Exception e) {
      removed = "";
      logger.error("Error When Parsing the number in NumberUtils" + e);
    }
    return removed;
  }

  public static String cutFractions(String orig) {
    String removed = "";
    if (orig.indexOf(".") != -1 && ((orig.indexOf(".") + 2) < orig.length() - 1)) {
      removed = orig.substring(0, orig.indexOf(".") + 3);
    } else {
      removed = orig;
    }
    return removed;
  }

}
