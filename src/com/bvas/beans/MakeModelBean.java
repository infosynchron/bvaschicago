package com.bvas.beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.bvas.utils.DBInterfaceGR;
import com.bvas.utils.DBInterfaceLocal;
import com.bvas.utils.DBInterfaceNY;
import com.bvas.utils.UserException;

public class MakeModelBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private static final Logger logger = Logger.getLogger(MakeModelBean.class);

  private String makeModelCode = null;

  private String makeModelName = null;

  private int manufacturerId;

  private String interChangeModel = null;

  public String getMakeModelCode() {
    return (this.makeModelCode);
  }

  public String getMakeModelName() {
    return (this.makeModelName);
  }

  public int getManufacturerId() {
    return (this.manufacturerId);
  }

  public String getInterChangeModel() {
    return (this.interChangeModel);
  }

  public void setMakeModelCode(String makeModelCode) {
    this.makeModelCode = makeModelCode;
  }

  public void setMakeModelName(String makeModelName) {
    this.makeModelName = makeModelName;
  }

  public void setManufacturerId(int manufacturerId) {
    this.manufacturerId = manufacturerId;
  }

  public void setInterChangeModel(String interChangeModel) {
    this.interChangeModel = interChangeModel;
  }

  public void getMakeModel() throws UserException {
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      if (getMakeModelName() == null || getMakeModelName().trim().equals("")) {
        setMakeModelName("XXX");
      }
      if (getMakeModelCode() == null || getMakeModelCode().trim().equals("")) {
        setMakeModelCode("XXX");
      }
      ResultSet rs =
          stmt.executeQuery("SELECT * FROM MakeModel WHERE MakeModelCode LIKE'"
              + getMakeModelCode() + "%' OR MakeModelName LIKE '" + getMakeModelName() + "%'");
      if (rs.next()) {
        setMakeModelCode(rs.getString("MakeModelCode"));
        setMakeModelName(rs.getString("MakeModelName"));
        setManufacturerId(rs.getInt("ManufacturerId"));
        setInterChangeModel(rs.getString("InterChangeModel"));

      } else {
        setMakeModelCode("");
        setMakeModelName("");
        setManufacturerId(0);
        setInterChangeModel("");
        throw new UserException("No Model Available");
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error("Unable to Get the Make Model");
    }
  }

  public void addNewMakeModel() throws UserException {

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      if (getMakeModelCode() != null && !getMakeModelCode().trim().equals("")
          && getMakeModelName() != null && !getMakeModelName().trim().equals("")
          && getManufacturerId() != 0) {

        stmt.execute("INSERT INTO MakeModel (MakeModelCode, MakeModelName, ManufacturerId, InterChangeModel) VALUES ('"
            + getMakeModelCode()
            + "', '"
            + getMakeModelName()
            + "', "
            + getManufacturerId()
            + ", '" + getInterChangeModel() + "')");

        try {
          Statement stmt1 = con.createStatement();
          ResultSet rs =
              stmt1.executeQuery("Select Value from Properties Where Name='ThisLocation'");
          if (rs.next() && rs.getInt(1) == 1) {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conGR = DBInterfaceGR.getSQLConnection();
            Statement stmtGR = conGR.createStatement();
            stmtGR
                .execute("INSERT INTO MakeModel (MakeModelCode, MakeModelName, ManufacturerId, InterChangeModel) VALUES ('"
                    + getMakeModelCode()
                    + "', '"
                    + getMakeModelName()
                    + "', "
                    + getManufacturerId() + ", '" + getInterChangeModel() + "')");
            stmtGR.close();
            conGR.close();
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conNY = DBInterfaceNY.getSQLConnection();
            Statement stmtNY = conNY.createStatement();
            stmtNY
                .execute("INSERT INTO MakeModel (MakeModelCode, MakeModelName, ManufacturerId, InterChangeModel) VALUES ('"
                    + getMakeModelCode()
                    + "', '"
                    + getMakeModelName()
                    + "', "
                    + getManufacturerId() + ", '" + getInterChangeModel() + "')");
            stmtNY.close();
            conNY.close();
          }
          rs.close();
          stmt1.close();

        } catch (Exception ex) {
          logger.error(ex);
        }

      } else {
        throw new UserException("Not Added...  Please check your values");
      }

      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Unable to add New Make Model" + e);
    }
  }

  public void changeMakeModel() throws UserException {

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      if (getMakeModelCode() != null && !getMakeModelCode().trim().equals("")
          && getMakeModelName() != null && !getMakeModelName().trim().equals("")
          && getManufacturerId() != 0) {

        stmt.executeUpdate("UPDATE MakeModel set MakeModelName='" + getMakeModelName()
            + "', ManufacturerId=" + getManufacturerId() + ", InterChangeModel='"
            + getInterChangeModel() + "' where MakeModelCode='" + getMakeModelCode() + "'");

        try {
          Statement stmt1 = con.createStatement();
          ResultSet rs =
              stmt1.executeQuery("Select Value from Properties Where Name='ThisLocation'");
          if (rs.next() && rs.getInt(1) == 1) {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conGR = DBInterfaceGR.getSQLConnection();

            Statement stmtGR = conGR.createStatement();
            stmtGR.executeUpdate("UPDATE MakeModel set MakeModelName='" + getMakeModelName()
                + "', ManufacturerId=" + getManufacturerId() + ", InterChangeModel='"
                + getInterChangeModel() + "' where MakeModelCode='" + getMakeModelCode() + "'");

            stmtGR.close();
            conGR.close();

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conNY = DBInterfaceNY.getSQLConnection();;
            Statement stmtNY = conNY.createStatement();
            stmtNY.executeUpdate("UPDATE MakeModel set MakeModelName='" + getMakeModelName()
                + "', ManufacturerId=" + getManufacturerId() + ", InterChangeModel='"
                + getInterChangeModel() + "' where MakeModelCode='" + getMakeModelCode() + "'");

            stmtNY.close();
            conNY.close();
          }
          rs.close();
          stmt1.close();
        } catch (Exception ex) {
          logger.error(ex);
        }

      } else {
        throw new UserException("Not Changed...  Please check your values");
      }

      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Unable to change Make Model" + e);
    }
  }

  public void removeMakeModel() throws UserException {

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      if (getMakeModelCode() != null && !getMakeModelCode().trim().equals("")) {

        stmt.executeUpdate("DELETE FROM MakeModel where MakeModelCode='" + getMakeModelCode() + "'");

        try {
          Statement stmt1 = con.createStatement();
          ResultSet rs =
              stmt1.executeQuery("Select Value from Properties Where Name='ThisLocation'");
          if (rs.next() && rs.getInt(1) == 1) {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conGR = DBInterfaceGR.getSQLConnection();

            Statement stmtGR = conGR.createStatement();
            stmtGR.executeUpdate("DELETE FROM MakeModel where MakeModelCode='" + getMakeModelCode()
                + "'");

            stmtGR.close();
            conGR.close();
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conNY = DBInterfaceNY.getSQLConnection();
            Statement stmtNY = conNY.createStatement();
            stmtNY.executeUpdate("DELETE FROM MakeModel where MakeModelCode='" + getMakeModelCode()
                + "'");
            stmtNY.close();
            conNY.close();
          }
          rs.close();
          stmt1.close();
        } catch (Exception ex) {
          logger.error(ex);
        }

      } else {
        throw new UserException("Not Removed...  Please check your values");
      }

      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error(e);
      throw new UserException("Unable to remove Make Model" + e);
    }
  }

  public static String getMakeModelName(String makeModelCode) {
    String makeModelName = null;
    Connection con = DBInterfaceLocal.getSQLConnection();
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = con.createStatement();
      rs =
          stmt.executeQuery("select MakeModelName from MakeModel where MakeModelCode='"
              + makeModelCode + "'");
      if (rs.next()) {
        makeModelName = rs.getString("MakeModelName");
      } else {
        logger.error("In MakeModelBean - Unable to get MakeModelName" + makeModelCode);
        makeModelName = "";
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error(e);
      logger.error("In MakeModelBean - Unable to get MakeModelName--" + makeModelCode + "--" + e);
      makeModelName = "";
    }
    return makeModelName;
  }

  public static String getMakeModelCode(String makeModelName) {
    String makeModelCode = null;
    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("select MakeModelCode from MakeModel where MakeModelName='"
              + makeModelName + "'");
      if (rs.next()) {
        makeModelCode = rs.getString("MakeModelCode");
      } else {
        logger.error("In MakeModelBean - Unable to get MakeModelCode");
        makeModelCode = "";
      }
      rs.close();
      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error("In MakeModelBean - Unable to get MakeModelCode");
      makeModelCode = "";
    }
    return makeModelCode;
  }

  public static Hashtable<String, String> getMakeModelForManufacId(int manufacId) {
    Hashtable<String, String> htable = new Hashtable<String, String>();

    Connection con = DBInterfaceLocal.getSQLConnection();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs =
          stmt.executeQuery("select * from MakeModel where ManufacturerId=" + manufacId
              + " order by MakeModelName");
      while (rs.next()) {
        String mKey = rs.getString("MakeModelCode");
        String mVal = rs.getString("MakeModelName");

        htable.put(mKey, mVal);

      }
      rs.close();
      stmt.close();
      con.close();
    } catch (SQLException e) {
      logger.error("In MakeModelBean - Unable to get MakeModel For ManufacId" + e);
      htable = null;
    }

    return htable;
  }

}
