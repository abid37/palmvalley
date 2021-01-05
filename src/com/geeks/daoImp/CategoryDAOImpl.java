/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.daoImp;

import com.geeks.beans.CategoryBean;
import com.geeks.connectivity.Connectivity;
import com.geeks.dao.CategoryDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xyz
 */
public class CategoryDAOImpl implements CategoryDAO{

    static Connection con = Connectivity.connect();
    @Override
    public Integer addCategoryDetails(CategoryBean categoryBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("insert into category(category_name,description,active,created_by,created_date) values(?,?,?,?,?)");
            ps.setString(1, categoryBean.getCategoryName());
            ps.setString(2, categoryBean.getDescription());
            ps.setInt(3, 1);
            ps.setInt(4, categoryBean.getCreatedBy());
            ps.setString(5, categoryBean.getCreatedDate());  
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Boolean checkCategory(CategoryBean categoryBean) {
        try {
            PreparedStatement ps = con.prepareStatement("select * from category u where u.category_name=?");
            ps.setString(1, categoryBean.getCategoryName());
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
    public List<CategoryBean> setAllTxt(CategoryBean categoryBean) {
         ArrayList<CategoryBean> list = new ArrayList<>();
         try {
            PreparedStatement ps = con.prepareStatement("select * from category u where u.category_id=?");
            ps.setInt(1, categoryBean.getCategoryiD());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                CategoryBean categoryBean1 = new CategoryBean();
                categoryBean1.setCategoryName(rst.getString("category_name"));
                categoryBean1.setDescription(rst.getString("description"));
                list.add(categoryBean1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Integer updateCategoryDetails(CategoryBean categoryBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update category set category_name=?,description=?,modified_by=?,modified_date=? where category_id=?");
            ps.setString(1, categoryBean.getCategoryName());
            ps.setString(2, categoryBean.getDescription());
            ps.setInt(3, categoryBean.getModifiedBy());
            ps.setString(4, categoryBean.getModifiedDate());
            ps.setInt(5, categoryBean.getCategoryiD());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Integer deleteCategoryDetails(CategoryBean categoryBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update category set active=? where category_id=?");
            ps.setInt(1, 0);
            ps.setInt(2, categoryBean.getCategoryiD());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public ResultSet viewAllCategoryResultSet() {
        ResultSet rst=null;
        try {
            Statement st = con.createStatement();
            rst = st.executeQuery("SELECT c.category_id AS Id,c.category_name AS Category_Name,c.description AS Description,c.created_date AS Created_Date,u1.full_name AS Created_by,c.modified_date AS Modified_Date,u2.full_name AS Modified_By \n" +
"FROM category c \n" +
"INNER JOIN USER u1 ON u1.user_id = c.created_by\n" +
"LEFT JOIN USER u2 ON u2.user_id = c.modified_by \n" +
"AND c.active=1");
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return rst;
    }
    
}
