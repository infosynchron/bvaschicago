package com.bvas.formBeans;

import java.io.Serializable;

public class OrderDetailsForm implements Serializable {

  public String partNo = "";

  public String vendorPartNo = "";

  public String partDescription1 = "";

  public String partDescription2 = "";

  public String unitsInStock = "";

  public double sellingRate;

  public int noOfPieces;

  public int quantity = 0;

  public double priceForUs;

}
