/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.beans;

import java.io.Serializable;

/**
 *
 * @author xyz
 */
public class TicketDetailsBean implements Serializable{
    private int ticketDetialsId;
    private PackageBean packageBean;
    private int quantity;
    private TicketBean ticketBean;
   
    public TicketDetailsBean(){
    
    }

    public int getTicketDetialsId() {
        return ticketDetialsId;
    }

    public void setTicketDetialsId(int ticketDetialsId) {
        this.ticketDetialsId = ticketDetialsId;
    }

    public PackageBean getPackageBean() {
        return packageBean;
    }

    public void setPackageBean(PackageBean packageBean) {
        this.packageBean = packageBean;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public TicketBean getTicketBean() {
        return ticketBean;
    }

    public void setTicketBean(TicketBean ticketBean) {
        this.ticketBean = ticketBean;
    }
    
    
}
