package com.bvas.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.bvas.utils.DBInterfaceLocal;

public class ManufacturerBean implements Serializable {
  private static final Logger logger = Logger.getLogger(ManufacturerBean.class);

  private String manufacturerId = null;

  private String manufacturerName = null;

  public String getManufacturerId() {
    return (this.manufacturerId);
  }

  public String getManufacturerName() {
    return (this.manufacturerName);
  }

  public void setManufacturerId(String manufacturerId) {
    this.manufacturerId = manufacturerId;
  }

  public void setManufacturerName(String manufacturerName) {
    this.manufacturerName = manufacturerName;
  }

  public static Hashtable<String, String> getAllManufacturers() {
    Hashtable<String, String> htable = null;

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM Manufacturer order by ManufacturerId");
      htable = new Hashtable<String, String>();
      while (rs.next()) {
        String mKey = rs.getInt("ManufacturerId") + "";
        String mVal = rs.getString("ManufacturerName");

        htable.put(mKey.trim(), mVal.trim());
      }
      rs.close();
      stmt.close();
      con.close();

    } catch (SQLException e) {
      logger.error("In Manufacturer Bean - Unable to get All Manufacturers");
    }
    return htable;
  }

}
