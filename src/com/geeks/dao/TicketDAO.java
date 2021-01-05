/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.dao;

import com.geeks.beans.TicketBean;
import java.sql.ResultSet;
import java.util.List;


public interface TicketDAO {
    public ResultSet viewAllTicketResultSet(TicketBean ticketBean);
    public Integer addTicketDetails(TicketBean ticketBean);
    public Integer updateTicketDetails(TicketBean ticketBean);
    public Integer deleteTicketDetails(TicketBean ticketBean);
    public TicketBean getTicketDetails(TicketBean ticketBean);
    public List<String> checkBarCode();
}
