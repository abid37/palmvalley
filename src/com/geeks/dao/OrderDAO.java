/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.dao;

import com.geeks.beans.OrderBean;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author xyz
 */
public interface OrderDAO {
    public ResultSet viewAllOrderResultSet(OrderBean orderBean);
    public Integer addOrderDetails(OrderBean orderBean);
    public Integer updateOrderDetails(OrderBean orderBean);
    public Integer updateOrderDetails2(OrderBean orderBean);
    public Integer deleteOrderDetails(OrderBean orderBean);
    public OrderBean getOrderDetails(OrderBean orderBean);
}
