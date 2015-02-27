package com.bvas.utils;

import com.bvas.beans.Driver;
import com.bvas.beans.WarehouseStaff;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Roohul Amin
 */
public class Getter {
    private static final Logger logger = Logger.getLogger(Getter.class);
    public List populateDrivers() {
        
        List<Driver> drvrList = new ArrayList<Driver>();
        
        Connection con = null;
        Statement stmtDrvr = null;
        ResultSet rsDrvr = null;

        try {
            con = DBInterfaceLocal.getSQLConnection();
            stmtDrvr = con.createStatement();
            rsDrvr = stmtDrvr.executeQuery("Select serial,drivername From driver  Where active=1 order by drivername");
            while (rsDrvr.next()) {
                Driver drvr = new Driver();
                drvr.setSerial(rsDrvr.getString("serial"));
                drvr.setDriverName(rsDrvr.getString("drivername"));
                drvrList.add(drvr);
            }
             stmtDrvr.close();
            rsDrvr.close();
            con.close();
        } catch (SQLException e) {
            logger.error(e);
        } finally{
           
        }
        return drvrList;
    }
    
     public List populateWarehouseStaff() {
        List<WarehouseStaff> list=new ArrayList();
        try {
            Connection con = DBInterfaceLocal.getSQLConnection();
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("Select * from warehousestaff where active=1 order by staffname");
            while(rs.next()){
                WarehouseStaff staff=new WarehouseStaff();
                staff.setSerialNo(rs.getInt("serial"));
                staff.setStaffName(rs.getString("staffname"));
                list.add(staff);
            }
            stmt.close();
            rs.close();
            con.close();
            } catch (SQLException e) {
      logger.error(e);
    }
        return list;
    }
}
