/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.daoImp;

import com.geeks.beans.AccountBean;
import com.geeks.connectivity.Connectivity;
import com.geeks.dao.AccountDAO;
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
public class AccountDAOImpl implements AccountDAO{

    static Connection con = Connectivity.connect();
    @Override
    public ResultSet viewAllAccountResultSet() {
        ResultSet rst=null;
        try {
            Statement st = con.createStatement();
            rst = st.executeQuery("SELECT a.id AS Id,a.name AS NAME,a.contact AS Contact,a.description AS Desp,"
                    + "a.created_date AS Created_Date,u1.full_name AS Created_by,a.modified_date AS Modified_Date,"
                    + "u.full_name AS Modified_By FROM account a LEFT JOIN USER u ON a.modified_by=u.user_id "
                    + "LEFT JOIN USER u1 ON a.created_by=u1.user_id AND a.active=1");
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return rst;
    }

    @Override
    public Integer addAccountDetails(AccountBean accountBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("insert into account(name,contact,description,active,created_by,created_date) values(?,?,?,?,?,?)");
            ps.setString(1, accountBean.getAccountName());
            ps.setString(2, accountBean.getAccountConnect());
            ps.setString(3, accountBean.getDescription());
            ps.setInt(4, 1);
            ps.setInt(5, accountBean.getCreatedBy());
            ps.setString(6, accountBean.getCreatedDate());  
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Boolean checkAccount(AccountBean accountBean) {
        try {
            PreparedStatement ps = con.prepareStatement("select * from account u where u.name=?");
            ps.setString(1, accountBean.getAccountName());
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
    public List<AccountBean> setAllTxt(AccountBean accountBean) {
        ArrayList<AccountBean> list = new ArrayList<>();
         try {
            PreparedStatement ps = con.prepareStatement("select * from account u where u.id=?");
            ps.setInt(1, accountBean.getAccountId());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                AccountBean a = new AccountBean();
                a.setAccountName(rst.getString("name"));
                a.setAccountConnect(rst.getString("contact"));
                a.setDescription(rst.getString("description"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Integer updateAccountDetails(AccountBean accountBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update account set name=?,contact=?,description=?,modified_by=?,modified_date=? where id=?");
            ps.setString(1, accountBean.getAccountName());
            ps.setString(2, accountBean.getAccountConnect());
            ps.setString(3, accountBean.getDescription());
            ps.setInt(4, accountBean.getModifiedBy());
            ps.setString(5, accountBean.getModifiedDate());
            ps.setInt(6, accountBean.getAccountId());
        
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Integer deleteAccountDetails(AccountBean accountBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update account set active=? where id=?");
            ps.setInt(1, 0);
            ps.setInt(2, accountBean.getAccountId());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Integer getIdByAccount(AccountBean accountBean) {
        int r=0;
         try {
            PreparedStatement ps = con.prepareStatement("select u.id from account u where u.name=? and active=1");
            ps.setString(1, accountBean.getAccountName());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                r=rst.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public List<String> getAllAccountNames() {
        List<String> li = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select u.name from account u where u.active=1");
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                li.add(rst.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return li;
    }
}
