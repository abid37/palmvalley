/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.daoImp;

import com.geeks.beans.PackageBean;
import com.geeks.beans.TicketDetailsBean;
import com.geeks.dao.TicketDetailsDAO;
import static com.geeks.daoImp.OrderDetailsDAOImpl.con;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author xyz
 */
public class TicketDetailsDAOImpl implements TicketDetailsDAO{

    @Override
    public ResultSet viewAllTicketDetailsBeanResultSet(String ticket) {
        ResultSet rst=null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT td.ticketdetails_id,p.pkg_name,td.quantity,p.price,"
                    + "(p.price*td.quantity) AS total_price,td.ticket_id,p.pkg_id FROM ticketdetails td,package p, "
                    + "ticket t  WHERE t.ticket_id=td.ticket_id AND td.pkg_id=p.pkg_id AND t.barcode=?");
            ps.setString(1, ticket);
            rst = ps.executeQuery();
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return rst;
    }

    @Override
    public ResultSet viewAllTicketDetailsBeanResultSet2(String ticket) {
        ResultSet rst=null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT td.ticketdetails_id,p.pkg_name,td.quantity,p.price,"
                    + "(p.price*td.quantity) AS total_price,td.ticket_id FROM ticketdetails td,package p, "
                    + "ticket t  WHERE t.ticket_id=td.ticket_id AND td.pkg_id=p.pkg_id AND t.barcode like '"+ticket+"%'");
            rst = ps.executeQuery();
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return rst;
    }

    @Override
    public Integer addTicketDetails(TicketDetailsBean ticketDetailsBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("insert into ticketdetails(pkg_id,quantity,ticket_id) values(?,?,?)");
            ps.setInt(1, ticketDetailsBean.getPackageBean().getPackageId());
            ps.setInt(2, ticketDetailsBean.getQuantity());
            ps.setInt(3, ticketDetailsBean.getTicketBean().getTicketId());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public TicketDetailsBean setAllTxtForReturn(TicketDetailsBean ticketDetailsBean) {
        TicketDetailsBean ticketDetailsBean1 = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT td.ticketdetails_id,p.pkg_name,td.quantity,p.price "
                    + "FROM ticketdetails td,package p,ticket t WHERE t.ticket_id=td.ticket_id AND td.pkg_id=p.pkg_id "
                    + "AND td.ticketdetails_id=?");
            ps.setInt(1, ticketDetailsBean.getTicketDetialsId());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                ticketDetailsBean1 = new TicketDetailsBean();
                    PackageBean pb = new PackageBean();
                    pb.setPackageName(rst.getString("pkg_name"));
                    pb.setPackagePrice(rst.getFloat("price"));
                ticketDetailsBean1.setPackageBean(pb);
                ticketDetailsBean1.setQuantity(rst.getInt("quantity"));
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return ticketDetailsBean1;
    }

    @Override
    public Boolean checkIfPackageExists(TicketDetailsBean ticketDetailsBean) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ticketdetails WHERE pkg_id=? and ticket_id=?");
            ps.setInt(1, ticketDetailsBean.getPackageBean().getPackageId());
            ps.setInt(2, ticketDetailsBean.getTicketBean().getTicketId());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Integer updateIfPackageExists(TicketDetailsBean ticketDetailsBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update ticketdetails set quantity=((SELECT quantity WHERE pkg_id=?)+?) where pkg_id=?");
            ps.setInt(1, ticketDetailsBean.getPackageBean().getPackageId()); 
            ps.setInt(2, ticketDetailsBean.getQuantity());
            ps.setInt(3, ticketDetailsBean.getPackageBean().getPackageId()); 
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Integer deleteIfReturns(TicketDetailsBean ticketDetailsBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("delete from ticketdetails where ticketdetails_id=?");
            ps.setInt(1, ticketDetailsBean.getTicketDetialsId()); 
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Integer getPackageIdByTicketId(TicketDetailsBean ticketDetailsBean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
