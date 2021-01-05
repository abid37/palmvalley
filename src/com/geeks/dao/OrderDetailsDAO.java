/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.dao;

import com.geeks.beans.OrderDetailsBean;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author xyz
 */
public interface OrderDetailsDAO {
    public ResultSet viewAllOrderDetailsBeanResultSet(String order);
    public ResultSet viewAllOrderDetailsBeanResultSet2(String order);
    public Integer addOrderDetails(OrderDetailsBean orderDetailsBean);
    public OrderDetailsBean setAllTxtForReturn(OrderDetailsBean orderDetailsBean);
    public Boolean checkIfProductExists(OrderDetailsBean orderDetailsBean);
    public Integer updateIfProductExists(OrderDetailsBean orderDetailsBean);
    public Integer addProductIfReturns(OrderDetailsBean orderDetailsBean);
    public Integer getProductIdByOrderId(OrderDetailsBean orderDetailsBean);
}
