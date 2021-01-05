/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.daoImp;

import com.geeks.beans.OrderDetailsBean;
import com.geeks.beans.ProductBean;
import com.geeks.connectivity.Connectivity;
import com.geeks.dao.OrderDetailsDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author xyz
 */
public class OrderDetailsDAOImpl implements OrderDetailsDAO{

    static Connection con = Connectivity.connect();
    @Override
    public ResultSet viewAllOrderDetailsBeanResultSet(String order) {
        ResultSet rst=null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT o.od_id,p.product_name,o.quantity,o.price, "
                    + "(o.quantity*o.price) AS total,o.created_by,o.created_date,o.modified_by,o.modified_date, "
                    + "o.order_id FROM orderdetails o,product p, order_ od WHERE o.product_id=p.product_id "
                    + "AND o.order_id=od.order_id AND od.order_no=?");
            ps.setString(1, order);
            rst = ps.executeQuery();
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return rst;
    }
    
    @Override
    public ResultSet viewAllOrderDetailsBeanResultSet2(String order) {
        ResultSet rst=null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT od.od_id,p.product_name,od.quantity,od.price,"
                    + "(od.quantity*od.price)AS total FROM product p,orderdetails od,order_ o "
                    + "WHERE od.product_id=p.product_id AND od.order_id=o.order_id AND o.order_no LIKE '"+order+"%'");
            rst = ps.executeQuery();
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return rst;
    }

    @Override
    public Integer addOrderDetails(OrderDetailsBean orderDetailsBean) {
        int r=0,r1=0;
        try {
            PreparedStatement ps = con.prepareStatement("insert into orderdetails(product_id,quantity,price,active,created_by,created_date,order_id) values(?,?,?,?,?,?,?)");
            ps.setInt(1, orderDetailsBean.getProductBean().getProductID());
            ps.setInt(2, orderDetailsBean.getQuantity());
            ps.setFloat(3, orderDetailsBean.getPrice());
            ps.setInt(4, 1);
            ps.setInt(5, orderDetailsBean.getCreatedBy());
            ps.setString(6, orderDetailsBean.getCreatedDate());
            ps.setInt(7, orderDetailsBean.getOrderBean().getOrderId());
            r = ps.executeUpdate();
            
            PreparedStatement ps1 = con.prepareStatement("UPDATE product SET quantity=((SELECT quantity WHERE product_id=?)-?) WHERE product_id=?");
            ps1.setInt(1, orderDetailsBean.getProductBean().getProductID());
            ps1.setInt(2, orderDetailsBean.getQuantity());
            ps1.setInt(3, orderDetailsBean.getProductBean().getProductID());
            r1 = ps1.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r+r1;
    }

    @Override
    public OrderDetailsBean setAllTxtForReturn(OrderDetailsBean orderDetailsBean) {
        OrderDetailsBean odb = new OrderDetailsBean();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT o.od_id,p.product_name,o.quantity,o.price FROM orderdetails o,product p WHERE o.od_id=? AND o.product_id=p.product_id");
            ps.setInt(1, orderDetailsBean.getOdId());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                ProductBean pb = new ProductBean();
                pb.setProductName(rst.getString("product_name"));
                odb.setProductBean(pb);
                odb.setQuantity(rst.getInt("quantity"));
                odb.setPrice(rst.getFloat("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return odb;
    }

    @Override
    public Boolean checkIfProductExists(OrderDetailsBean orderDetailsBean) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM orderdetails WHERE product_id=? and order_id=?");
            ps.setInt(1, orderDetailsBean.getProductBean().getProductID());
            ps.setInt(2, orderDetailsBean.getOrderBean().getOrderId());
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
    public Integer updateIfProductExists(OrderDetailsBean orderDetailsBean) {
        int r=0,r1=0;
        try {
            PreparedStatement ps = con.prepareStatement("update orderdetails set quantity=((SELECT quantity WHERE product_id=?)+?) where product_id=?");
            ps.setInt(1, orderDetailsBean.getProductBean().getProductID()); 
            ps.setInt(2, orderDetailsBean.getQuantity());
            ps.setInt(3, orderDetailsBean.getProductBean().getProductID()); 
            r = ps.executeUpdate();
            
            PreparedStatement ps1 = con.prepareStatement("UPDATE product SET quantity=((SELECT quantity WHERE product_id=?)-?) WHERE product_id=?");
            ps1.setInt(1, orderDetailsBean.getProductBean().getProductID());
            ps1.setInt(2, orderDetailsBean.getQuantity());
            ps1.setInt(3, orderDetailsBean.getProductBean().getProductID());
            r1 = ps1.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r+r1;
    }

    @Override
    public Integer addProductIfReturns(OrderDetailsBean orderDetailsBean) {
        int r=0,r1=0;
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE product SET quantity=((SELECT quantity WHERE product_id=?)+(SELECT quantity FROM orderdetails WHERE product_id=? AND order_id=?)) WHERE product_id=?");
            ps.setInt(1, orderDetailsBean.getProductBean().getProductID());
            ps.setInt(2, orderDetailsBean.getProductBean().getProductID());
            ps.setInt(3, orderDetailsBean.getOrderBean().getOrderId());
            ps.setInt(4, orderDetailsBean.getProductBean().getProductID());
            r = ps.executeUpdate();
            
            PreparedStatement ps1 = con.prepareStatement("DELETE FROM orderdetails WHERE product_id=? AND order_id=?");
            ps1.setInt(1, orderDetailsBean.getProductBean().getProductID());
            ps1.setInt(2, orderDetailsBean.getOrderBean().getOrderId());
            r1 = ps1.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r+r1;
    }

    @Override
    public Integer getProductIdByOrderId(OrderDetailsBean orderDetailsBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT product_id FROM orderdetails WHERE od_id=?");
            ps.setInt(1, orderDetailsBean.getOdId());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                r = rst.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
}
