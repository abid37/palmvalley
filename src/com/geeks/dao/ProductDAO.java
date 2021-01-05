/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.dao;

import com.geeks.beans.CategoryBean;
import com.geeks.beans.ProductBean;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author xyz
 */
public interface ProductDAO {
    public ResultSet viewAllProductResultSet();
    public Integer addProductDetails(ProductBean productBean);
    public Boolean checkProduct(ProductBean productBean);
    public List<ProductBean> setAllTxt(ProductBean productBean);
    public Integer updateProductDetails(ProductBean productBean);
    public Integer deleteProductDetails(ProductBean productBean);
    public List<String> viewAllCategory();
    public Integer getIdByCategory(CategoryBean categoryBean);
    public List<String> checkBarCode();
    public ResultSet viewAllProductResultSetForOrder(ProductBean productBean);
    //public Integer getProductByProductId(ProductBean productBean);
}
