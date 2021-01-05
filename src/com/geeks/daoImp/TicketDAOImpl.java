/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.daoImp;

import com.geeks.beans.TicketBean;
import com.geeks.connectivity.Connectivity;
import com.geeks.dao.TicketDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xyz
 */
public class TicketDAOImpl implements TicketDAO{

    static Connection con = Connectivity.connect();
    
    @Override
    public ResultSet viewAllTicketResultSet(TicketBean ticketBean) {
        ResultSet rst=null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT t.ticket_id,t.barcode,t.contact,t.date,t.total,"
                    + "t.paid FROM ticket t WHERE t.active=1 AND t.barcode LIKE '"+ticketBean.getBarcode()+"%'");
            rst = ps.executeQuery();
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return rst;    
    }

    @Override
    public Integer addTicketDetails(TicketBean ticketBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("insert into ticket(barcode,contact,date,total,paid,active,created_by,created_date) values(?,?,?,?,?,?,?,?)");
            ps.setString(1, ticketBean.getBarcode());
            ps.setString(2, ticketBean.getContact());
            ps.setString(3, ticketBean.getDate());
            ps.setFloat(4, ticketBean.getTotal());
            ps.setFloat(5, ticketBean.getPaid());
            ps.setInt(6, 1);
            ps.setInt(7, ticketBean.getCreatedBy());
            ps.setString(8, ticketBean.getCreatedDate());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Integer updateTicketDetails(TicketBean ticketBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update ticket set total=?,paid=?,contact=?,date=? where barcode=?");
            ps.setFloat(1, ticketBean.getTotal());
            ps.setFloat(2, ticketBean.getPaid());
            ps.setString(3, ticketBean.getContact());
            ps.setString(4, ticketBean.getDate());
            ps.setString(5, ticketBean.getBarcode());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Integer deleteTicketDetails(TicketBean ticketBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("delete from ticket where barcode=?");
            ps.setString(1, ticketBean.getBarcode());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public TicketBean getTicketDetails(TicketBean ticketBean) {
        TicketBean tb = null;
        try {
            PreparedStatement ps = con.prepareStatement("select * from ticket t where t.barcode=?");
            ps.setString(1, ticketBean.getBarcode());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                tb = new TicketBean();
                tb.setTicketId(rst.getInt("ticket_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tb;
    }
    
    @Override
    public List<String> checkBarCode() {
        List<String> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select barcode from product");
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                list.add(rst.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
