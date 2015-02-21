package com.bvas.beans;

import java.io.Serializable;

/**
 *
 * @author Roohul Amin
 */
public class WarehouseStaff implements Serializable{
    private int serialNo;
    private String staffName;
    private int active;

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
    
}
