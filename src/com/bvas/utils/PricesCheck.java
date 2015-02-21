package com.bvas.utils;

import java.io.File;
import java.io.FileWriter;

import org.apache.log4j.Logger;

public class PricesCheck {
  private static final Logger logger = Logger.getLogger(PricesCheck.class);

  public static void main(String[] args) {
    try {
      File file1 = new File("prices.txt");
      FileWriter ft = new FileWriter(file1);

      calPrices(ft, 2);

      calPrices(ft, 3);

      calPrices(ft, 4);

      calPrices(ft, 5);

      ft.close();
    } catch (Exception e) {
      logger.error("1---" + e);
    }

  }

  public static void calPrices(FileWriter ft, int roundingVal) {
    try {

      ft.write("Item  Price  Qty Total    %         Extra  One Item  Price  Total\n");
      String[][] arr =
          { {"1", "26.45", "5"}, {"2", "15.35", "20"}, {"3", "39.99", "9"}, {"4", "5.71", "24"},
              {"5", "10.99", "50"}};
      double percTotal = 0.0;
      double total = 1485.70;
      double extraTotal = 700.00;
      double finExtraTotal = 0.0;
      double finTotalTotal = 0.0;
      for (int i = 0; i < arr.length; i++) {
        String item = arr[i][0];
        double origPrice = Double.parseDouble(arr[i][1]);
        int qty = Integer.parseInt(arr[i][2]);
        double origTotal = origPrice * qty;
        origTotal = NumberUtils.cutFractions(origTotal, 2);
        double perc = origTotal * 100 / total;
        perc = NumberUtils.cutFractions(perc, roundingVal);
        percTotal += perc;
        double extra = extraTotal * perc / 100;
        extra = NumberUtils.cutFractions(extra, 2);
        double extraOneItem = extra / qty;
        extraOneItem = NumberUtils.cutFractions(extraOneItem, 2);
        double finPrice = origPrice + extraOneItem;
        double finTotal = finPrice * qty;
        finTotal = NumberUtils.cutFractions(finTotal, 2);
        finExtraTotal += extra;
        finTotalTotal += finTotal;
        ft.write(InvenFind.padSpaces(item + "", 5) + InvenFind.padSpaces(origPrice + "", 6)
            + InvenFind.padSpaces(qty + "", 3) + InvenFind.padSpaces(origTotal + "", 8)
            + InvenFind.padSpaces(perc + "", 9) + InvenFind.padSpaces(extra + "", 10)
            + InvenFind.padSpaces(extraOneItem + "", 10) + InvenFind.padSpaces(finPrice + "", 10)
            + InvenFind.padSpaces(finTotal + "", 10));
        ft.write("\n");
      }
      percTotal = NumberUtils.cutFractions(percTotal, 2);
      String finExtraTotalStr = finExtraTotal + "";
      String finTotalTotalStr = finTotalTotal + "";
      finExtraTotalStr = NumberUtils.cutFractions(finExtraTotalStr, 2);
      finTotalTotalStr = NumberUtils.cutFractions(finTotalTotalStr, 2);
      ft.write("\n");
      ft.write("Totals:         " + InvenFind.padSpaces(total + "", 10)
          + InvenFind.padSpaces(percTotal + "", 10) + "  " + finExtraTotalStr + "  "
          + finTotalTotalStr);
      ft.write("\n");
      ft.write("\n");
      ft.write("Total:  " + total);
      ft.write("\n");
      ft.write("Extra:  " + extraTotal);
      ft.write("\n");
      ft.write("\n");
      ft.write("Total:  " + 2185.70);
      ft.write("\n");
      ft.write("\n");

    } catch (Exception e) {
      logger.error("2---" + e);
    }
  }
}
