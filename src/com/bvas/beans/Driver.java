package com.bvas.beans;

import java.io.Serializable;

/**
 *
 * @author Roohul Amin
 */
public class Driver implements Serializable{
    private String serial;
    private String driverName;
    private String active;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
    
}
