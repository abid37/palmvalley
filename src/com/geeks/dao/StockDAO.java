/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.dao;

import com.geeks.beans.AccountBean;
import com.geeks.beans.ProductBean;
import com.geeks.beans.StockBean;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author xyz
 */
public interface StockDAO {
    public ResultSet viewAllStockResultSet();
    public Integer addStockDetails(StockBean stockBean);
    public Boolean checkStock(StockBean stockBean);
    public List<StockBean> setAllTxt(StockBean stockBean);
    public Integer updateStockDetails(StockBean stockBean);
    public Integer deleteStockDetails(StockBean stockBean);
    public List<String> viewAllAccount();
    public List<String> viewAllProduct();
    public Integer getIdByAccount(AccountBean accountBean);
    public Integer getIdByProduct(ProductBean productBean);
    public Integer updateProductByStock(StockBean stockBean);
}
