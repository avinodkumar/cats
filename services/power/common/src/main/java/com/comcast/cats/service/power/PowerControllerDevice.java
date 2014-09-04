/**
 * Copyright 2014 Comcast Cable Communications Management, LLC
 *
 * This file is part of CATS.
 *
 * CATS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CATS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CATS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comcast.cats.service.power;

import java.util.Date;

import com.comcast.cats.service.PowerInfo;
import com.comcast.cats.service.PowerStatistics;
/**
 * Abstract representation of a power controller device.
 */
public abstract class PowerControllerDevice {

   /**
    * The WTI_IPS_1600 string.
    */
   public static final String   MODEL_POWER_DEVICE_WTI_IPS_1600 = "WTI_IPS_1600";
   
   /**
    * The NETBOOTER_NP_16.
    */
   public static final String   MODEL_POWER_DEVICE_NETBOOTER_NP_1601D = "SYNACCESS NETBOOTER_NP_16";
   
   /**
    * Number of outlets in a WTI_IPS_1600.
    */
   public static final String   NUM_OUTLETS_WTI_IPS_1600        = "16";
   
   /**
    * The WTI_IPS_1600 outlet list.
    */
   public static final String[] WTI_IPS_1600_OUTLET_LIST        = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"};

   private String               name                            = "";
   private String               model                           = "";
   protected String             ip                              = "";
   protected String             scheme                          = "";
   protected int                port;
   protected int                numOutlets                      = 0;
   protected PowerInfo powerInfo = new PowerInfo();

   /**
    * OFF string.
    */
   public static final String         OFF                             = "OFF";

   /**
    * ON string.
    */
   public static final String         ON                              = "ON";

   /**
    * Boot string.
    */
   public static final String         BOOT                            = "BOOT";

   private String               state;

   /**
    * Power on the specified outlet.
    * 
    * @param outlet The outlet to power on.
    * 
    * @return <b>true</b> on success.
    */
   public abstract boolean powerOn(int outlet);

   /**
    * Power off the specified outlet.
    * 
    * @param outlet The outlet to power off.
    * 
    * @return <b>true</b> on success.
    */
   public abstract boolean powerOff(int outlet);

   /**
    * Power toggle the specified outlet. Toggle off then on.
    * 
    * @param outlet The outlet to power toggle.
    * 
    * @return <b>true</b> on success.
    */
   public abstract boolean powerToggle(int outlet);

   /**
    * Gets the outlet status of the specified outlet.
    * @param outlet The outlet to get the status for.
    * @return The status string.
    */
   public abstract String getOutletStatus(int outlet);
   

   /**
    * Method to initialize the power device connection.
    */
   public abstract void createPowerDevConn();
   
   /**
    * Gets a string value containing the information related to this PowerControllerDevice.
    * @return a string value containing the information related to this PowerControllerDevice.
    */
  @Override
   public String toString() {
      return new StringBuilder().append("Name:").append(name).append(" Model:").append(model).append(" IP Address:").append(ip).append(" Port:").append(port).append(" Outlets:").append(getNumOutlets()).append(".").toString();
   }

   /**
    * Get the name.
    * @return The name.
    */
   public String getName() {
      return name;
   }

   /**
    * Set the name.
    * @param name the name.
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * Get the model.
    * @return The model.
    */
   public String getModel() {
      return model;
   }

   /**
    * Set the model.
    * @param model the model.
    */
   public void setModel(String model) {
      this.model = model;
   }
   
   /**
    * Get the scheme.
    * @return scheme.
    */
   public String getScheme() {
      return scheme;
   }

   /**
    * Set the scheme.
    * @param scheme the scheme to be set.
    */
   public void setScheme(String scheme) {
      this.scheme = scheme;
   }

   /**
    * Get the IP.
    * @return the ip.
    */
   public String getIp() {
      return ip;
   }

   /**
    * Set the ip.
    * @param ip The ip.
    */
   public void setIp(String ip) {
      this.ip = ip;
   }

   /**
    * Get the number of outlets.
    * @return the number of outlets.
    */
   public int getNumOutlets() {
      return numOutlets;
   }

   /**
    * Set the number of outlets.
    * @param numOutlets The number of outlets.
    */
   public void setNumOutlets(int numOutlets) {
      this.numOutlets = numOutlets;
   }

   /**
    * Get the port.
    * @return the port.
    */
   public int getPort() {
      return port;
   }

   /**
    * Set the port.
    * @param port the port.
    */
   public void setPort(int port) {
      this.port = port;
   }

   /**
    * Get the state.
    * @return the state.
    */
   public String getState() {
      return state;
   }

   /**
    * Set the state.
    * @param state the state.
    */
   public void setState(String state) {
      this.state = state;
   }
   
   /**
    * Get the  PowerInfo object.
    * @return PowerInfo object
    */
   public PowerInfo getPowerInfo(){
	   return powerInfo;
   }
   
   /**
    * Set PowerInfo object.
    * @param pInfo
    */
   public void setPowerInfo(PowerInfo pInfo){
	   this.powerInfo = pInfo;
   }
   
   /**
    * To update the power on/off/reboot statistics 
    * @param outlet - Outlet number
    * @param cmd  - ON/OFF/REBOOT
    * @param ret  - true for success and false for failure.
    */
   protected void updateStatistics(int outlet, String cmd, boolean ret){
       
       PowerStatistics powerStats  = this.powerInfo.getPowerStatisticsAt(outlet);
	   if( powerStats == null){
	       powerStats = new PowerStatistics(outlet);
	   }
	   if (cmd.contains(ON)){
		   if(ret){
		       powerStats.incrementPowerOnCount();
		   }
		   else {
		       powerStats.incrementPowerOnFailure();
		   }	
	   }
	   else if (cmd.contains(OFF)){
		   if(ret){
		       powerStats.incrementPowerOffCount();
		   }
		   else {
		       powerStats.incrementPowerOffFailure();
		   }
	   }
	   else if (cmd.contains(BOOT)){	   
	       if(ret){
		       powerStats.incrementPowerToggleCount();
		   }
		   else {
		       powerStats.incrementPowerToggleFailure();
		   }
       }
	   
	   powerStats.setLastRequestedTime(new Date());
	   this.powerInfo.setPowerStatisticsAt(powerStats, outlet);
    }

   /**
    * Destroy the PowerControllerDevice. Implemented in the subclasses.
    */
   public abstract void destroy();

}
