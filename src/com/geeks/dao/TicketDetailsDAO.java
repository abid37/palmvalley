/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.dao;

import com.geeks.beans.TicketDetailsBean;
import java.sql.ResultSet;

/**
 *
 * @author xyz
 */
public interface TicketDetailsDAO {
    public ResultSet viewAllTicketDetailsBeanResultSet(String ticket);
    public ResultSet viewAllTicketDetailsBeanResultSet2(String ticket);
    public Integer addTicketDetails(TicketDetailsBean ticketDetailsBean);
    public TicketDetailsBean setAllTxtForReturn(TicketDetailsBean ticketDetailsBean);
    public Boolean checkIfPackageExists(TicketDetailsBean ticketDetailsBean);
    public Integer updateIfPackageExists(TicketDetailsBean ticketDetailsBean);
    public Integer deleteIfReturns(TicketDetailsBean ticketDetailsBean);
    public Integer getPackageIdByTicketId(TicketDetailsBean ticketDetailsBean);
}
