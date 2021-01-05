/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.daoImp;

import com.geeks.beans.AccountBean;
import com.geeks.beans.OrderBean;
import com.geeks.beans.TaxBean;
import com.geeks.connectivity.Connectivity;
import com.geeks.dao.OrderDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author xyz
 */
public class OrderDAOImpl implements OrderDAO{

    static Connection con = Connectivity.connect();
    @Override
    public ResultSet viewAllOrderResultSet(OrderBean orderBean) {
        ResultSet rst=null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT o.order_id,o.order_no,a.name,t.tax,o.total_price,o.paid,o.date "
                    + "FROM order_ o,account a,tax t WHERE o.customer_id=a.id AND t.tax_id=o.tax_id "
                    + "AND o.order_no LIKE '%"+orderBean.getOrderNumber()+"%'");
            //ps.setString(1, orderBean.getOrderNumber());
            rst = ps.executeQuery();
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return rst;    
    }

    @Override
    public Integer addOrderDetails(OrderBean orderBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("insert into order_(order_no,customer_id,tax_id,total_price,date) values(?,?,?,?,?)");
            ps.setString(1, orderBean.getOrderNumber());
            ps.setInt(2, orderBean.getAccountBean().getAccountId());
            ps.setInt(3, orderBean.getTaxBean().getTaxId());
            ps.setFloat(4, orderBean.getTotalPrice());
            ps.setString(5, orderBean.getDate());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Integer updateOrderDetails(OrderBean orderBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update order_ set total_price=?,paid=? where order_no=?");
            ps.setFloat(1, orderBean.getTotalPrice());
            ps.setFloat(2, orderBean.getPaidPrice());
            ps.setString(3, orderBean.getOrderNumber());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    
    @Override
    public Integer updateOrderDetails2(OrderBean orderBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update order_ set customer_id=?,tax_id=?,total_price=?,paid=?,date=? where order_no=?");
            ps.setInt(1, orderBean.getAccountBean().getAccountId());
            ps.setInt(2, orderBean.getTaxBean().getTaxId());
            ps.setFloat(3, orderBean.getTotalPrice());
            ps.setFloat(4, orderBean.getPaidPrice());
            ps.setString(5, orderBean.getDate());
            ps.setString(6, orderBean.getOrderNumber());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Integer deleteOrderDetails(OrderBean orderBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("delete from order_ where order_no=?");
            ps.setString(1, orderBean.getOrderNumber());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public OrderBean getOrderDetails(OrderBean orderBean) {
        OrderBean ob = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT o.order_id,o.order_no,a.name,t.tax,o.total_price,"
                    + "o.paid,o.date FROM order_ o, account a,tax t WHERE o.customer_id=a.id "
                    + "AND t.tax_id=o.tax_id AND o.order_no=?");
            ps.setString(1, orderBean.getOrderNumber());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                ob = new OrderBean();
                ob.setOrderId(rst.getInt("order_id"));
                    AccountBean accountBean = new AccountBean();
                    accountBean.setAccountName(rst.getString("name"));
                ob.setAccountBean(accountBean);
                    TaxBean taxBean = new TaxBean();
                    taxBean.setTax(rst.getFloat("tax"));
                ob.setTaxBean(taxBean);
                ob.setTotalPrice(rst.getFloat("total_price"));
                ob.setPaidPrice(rst.getFloat("paid"));
                ob.setDate(rst.getString("date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ob;
    }

}
