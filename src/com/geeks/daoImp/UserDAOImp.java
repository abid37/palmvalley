/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.daoImp;

import com.geeks.beans.UserBean;
import com.geeks.connectivity.Connectivity;
import com.geeks.dao.UserDAO;
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
public class UserDAOImp implements UserDAO{
    
    public static String getName;
    public static int userId;

    static Connection con = Connectivity.connect();
    
    @Override
    public Boolean login(UserBean userBean) {
        try {
            PreparedStatement ps = con.prepareStatement("select * from user u where u.user_name=? and u.password=?");
            ps.setString(1, userBean.getUserName());
            ps.setString(2, userBean.getPassword());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                UserBean userBean1 = new UserBean();
                userBean1.setName(rst.getString(2));
                getName = rst.getString(2);
                userId = rst.getInt(1);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ResultSet viewAllUserResultSet() {
            ResultSet rst=null;
            try {
                    Statement st = con.createStatement();
                    rst = st.executeQuery("SELECT u.id,u.full_name,u.user_name,u.dob,u.contact,u.cnic,u.user_type,u.create_by as created_by,u.created_on,q.full_name as modified_by,u.modified_on FROM user1 u LEFT JOIN USER q ON u.modified_by=q.user_id");
            } catch (SQLException e) {
                    e.printStackTrace();
            }
            return rst;
    }

    @Override
    public Integer addUserDetails(UserBean userBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("insert into user(full_name,user_name,password,dob,contact,cnic,"
                    + "user_type,active,created_by,created_date) values(?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, userBean.getName());
            ps.setString(2, userBean.getUserName());
            ps.setString(3, userBean.getPassword());
            ps.setString(4, userBean.getDOB());
            ps.setString(5, userBean.getContact());
            ps.setString(6, userBean.getCNIC());
            ps.setString(7, userBean.getUserType());
            ps.setInt(8, 1);
            ps.setInt(9, userBean.getCreatedBy());
            ps.setString(10, userBean.getCreatedDate());
            
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Boolean checkUser(UserBean userBean) {
        try {
            PreparedStatement ps = con.prepareStatement("select * from user u where u.user_name=?");
            ps.setString(1, userBean.getUserName());
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
    public List<UserBean> setAllTxt(UserBean userBean) {
        ArrayList<UserBean> list = new ArrayList<>();
         try {
            PreparedStatement ps = con.prepareStatement("select * from user u where u.user_id=?");
            ps.setInt(1, userBean.getUseriD());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                UserBean userBean1 = new UserBean();
                userBean1.setName(rst.getString("full_name"));
                userBean1.setUserName(rst.getString("user_name"));
                userBean1.setPassword(rst.getString("password"));
                userBean1.setDOB(rst.getString("dob"));
                userBean1.setContact(rst.getString("contact"));
                userBean1.setCNIC(rst.getString("cnic"));
                userBean1.setUserType(rst.getString("user_type"));
                list.add(userBean1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Integer updateUserDetails(UserBean userBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update user set full_name=?,user_name=?,password=?,dob=?,contact=?,cnic=?,"
                    + "user_type=?,active=?,modified_by=?,modified_date=? where user_id=?");
            ps.setString(1, userBean.getName());
            ps.setString(2, userBean.getUserName());
            ps.setString(3, userBean.getPassword());
            ps.setString(4, userBean.getDOB());
            ps.setString(5, userBean.getContact());
            ps.setString(6, userBean.getCNIC());
            ps.setString(7, userBean.getUserType());
            ps.setInt(8, 1);
            ps.setInt(9, userBean.getModifiedBy());
            ps.setString(10, userBean.getModifiedDate());
            ps.setInt(11, userBean.getUseriD());   
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Integer deleteUserDetails(UserBean userBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update user set active=? where user_id=?");
            ps.setInt(1, 0);
            ps.setInt(2, userBean.getUseriD());   
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
}
