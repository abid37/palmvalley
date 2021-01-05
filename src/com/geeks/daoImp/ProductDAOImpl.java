/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.daoImp;

import com.geeks.beans.CategoryBean;
import com.geeks.beans.ProductBean;
import com.geeks.connectivity.Connectivity;
import com.geeks.dao.ProductDAO;
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
public class ProductDAOImpl implements ProductDAO{

    static Connection con = Connectivity.connect();
    
    @Override
    public ResultSet viewAllProductResultSet() {
        ResultSet rst=null;
        try {
             PreparedStatement ps = con.prepareStatement("SELECT p.product_id,p.product_name,c.category_name,p.price,p.size,p.quantity,p.barcode,u1.full_name AS Created_By,u1.created_date,u2.full_name AS Modified_By,u2.modified_date \n" +
"FROM product p \n" +
"INNER JOIN category c ON p.category_id=c.category_id\n" +
//"INNER JOIN stock s ON s.product_id=p.product_id\n" +
"INNER JOIN USER u1 ON u1.user_id = p.created_by\n" +
"LEFT JOIN USER u2 ON u2.user_id = p.modified_by \n" +
"AND p.active=1 GROUP BY p.product_name;");
             
             
            rst = ps.executeQuery();
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return rst;
    }

    @Override
    public Integer addProductDetails(ProductBean productBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("insert into product(product_name,category_id,price,size,quantity,barcode"
                    + ",active,created_by,created_date) values(?,?,?,?,?,?,?,?,?)");
            ps.setString(1, productBean.getProductName());
            ps.setInt(2, productBean.getCategoryBean().getCategoryiD());
            ps.setFloat(3, productBean.getPrice());
            ps.setString(4, productBean.getSize());
            ps.setInt(5, productBean.getQuantity());
            ps.setString(6, productBean.getBarcode());
            ps.setInt(7, 1);
            ps.setInt(8, productBean.getCreatedBy());
            ps.setString(9, productBean.getCreatedDate());  
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Boolean checkProduct(ProductBean productBean) {
        try {
            PreparedStatement ps = con.prepareStatement("select * from product u where u.product_name=?");
            ps.setString(1, productBean.getProductName());
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
    public List<ProductBean> setAllTxt(ProductBean productBean) {
         ArrayList<ProductBean> list = new ArrayList<>();
         try {
            PreparedStatement ps = con.prepareStatement("SELECT p.product_name,c.category_name,p.price,p.size,p.quantity,"
                    + "p.barcode,p.created_by,p.created_date,p.modified_by,p.modified_date FROM product p,category c,stock s "
                    + "WHERE p.category_id=c.category_id AND s.product_id=p.product_id AND p.product_id=? AND p.active=1");
            ps.setInt(1, productBean.getProductID());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                ProductBean productBean1 = new ProductBean();
                productBean1.setProductName(rst.getString(1));
                    CategoryBean categoryBean = new CategoryBean();
                    categoryBean.setCategoryName(rst.getString(2));
                productBean1.setCategoryBean(categoryBean);
                productBean1.setPrice(rst.getFloat(3));
                productBean1.setSize(rst.getString(4));
                productBean1.setQuantity(rst.getInt(5));
                productBean1.setBarcode(rst.getString(6));
                list.add(productBean1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Integer updateProductDetails(ProductBean productBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update product set product_name=?,category_id=?,price=?,"
                    + "size=?,quantity=?,barcode=?,modified_by=?,modified_date=? where product_id=?");
            ps.setString(1, productBean.getProductName());
            ps.setInt(2, productBean.getCategoryBean().getCategoryiD());
            ps.setFloat(3, productBean.getPrice());
            ps.setString(4, productBean.getSize());
            ps.setInt(5, productBean.getQuantity());
            ps.setString(6, productBean.getBarcode());
            ps.setInt(7, productBean.getModifiedBy());
            ps.setString(8, productBean.getModifiedDate());
            ps.setInt(9, productBean.getProductID());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Integer deleteProductDetails(ProductBean productBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update product set active=? where product_id=?");
            ps.setInt(1, 0);
            ps.setInt(2, productBean.getProductID());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public List<String> viewAllCategory() {
        List<String> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select * from category u where active=1");
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                list.add(rst.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Integer getIdByCategory(CategoryBean categoryBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("select * from category where category_name=?");
            ps.setString(1, categoryBean.getCategoryName());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                r = rst.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
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
    
//    @Override
//    public ResultSet viewAllProductResultSetForOrder(ProductBean productBean) {
//        ResultSet rst=null;
//        try {
//           PreparedStatement ps = con.prepareStatement("SELECT p.product_id,p.product_name,c.category_name,p.price,"
//                     + "p.size,p.quantity,p.barcode "
//                     + "FROM product p,category c, stock s WHERE p.category_id=c.category_id AND s.product_id=p.product_id "
//                     + "AND p.active=1 AND p.product_name LIKE '"+productBean.getProductName()+"%' GROUP BY p.product_name");
//            rst = ps.executeQuery();
//        } catch (SQLException e) {
//                e.printStackTrace();
//        }
//        return rst;
//    }
    
    @Override
    public ResultSet viewAllProductResultSetForOrder(ProductBean productBean) {
        ResultSet rst=null;
        try {
           PreparedStatement ps = con.prepareStatement("SELECT p.product_id,p.product_name,c.category_name,p.price,"
                     + "p.size,p.quantity,p.barcode "
                     + "FROM product p,category c WHERE p.category_id=c.category_id "
                     + "AND p.active=1 AND p.product_name LIKE '"+productBean.getProductName()+"%' GROUP BY p.product_name");
            rst = ps.executeQuery();
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return rst;
    }
    
}
