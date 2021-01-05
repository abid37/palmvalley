/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.daoImp;

import com.geeks.beans.CategoryBean;
import com.geeks.beans.TaxBean;
import com.geeks.connectivity.Connectivity;
import com.geeks.dao.TaxDAO;
import static com.geeks.daoImp.CategoryDAOImpl.con;
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
public class TaxDAOImpl implements TaxDAO{
    
    static Connection con = Connectivity.connect();

    @Override
    public ResultSet viewAllTaxResultSet() {
        ResultSet rst=null;
        try {
            Statement st = con.createStatement();
            rst = st.executeQuery("SELECT t.tax_id AS Id,t.tax AS Tax,t.from_date AS WEF,t.to_date AS Up_To,u1.full_name AS Created_By,t.created_date AS Created_On,u2.full_name AS Modified_By,t.modified_date AS Modified_On\n" +
"FROM tax t \n" +
"INNER JOIN USER u1 ON u1.user_id = t.created_by\n" +
"LEFT JOIN USER u2 ON u2.user_id = t.modified_by \n" +
"AND t.active=1;");
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return rst;
    }

    @Override
    public Integer addTaxDetails(TaxBean taxBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("insert into tax(tax,from_date,to_date,active,created_by,created_date) values(?,?,?,?,?,?)");
            ps.setFloat(1, taxBean.getTax());
            ps.setString(2, taxBean.getFromDate());
            ps.setString(3, taxBean.getToDate());
            ps.setInt(4, 1);
            ps.setInt(5, taxBean.getCreatedBy());
            ps.setString(6, taxBean.getCreatedDate());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public List<TaxBean> setAllTxt(TaxBean taxBean) {
        ArrayList<TaxBean> list = new ArrayList<>();
         try {
            PreparedStatement ps = con.prepareStatement("select * from tax u where u.tax_id=?");
            ps.setInt(1, taxBean.getTaxId());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                TaxBean taxBean1 = new TaxBean();
                taxBean1.setTax(rst.getFloat("tax"));
                taxBean1.setFromDate(rst.getString("from_date"));
                taxBean1.setToDate(rst.getString("to_date"));
                list.add(taxBean1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Integer updateTaxDetails(TaxBean taxBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update tax set tax=?,from_date=?,to_date=?,modified_by=?,modified_date=? where tax_id=?");
            ps.setFloat(1, taxBean.getTax());
            ps.setString(2, taxBean.getFromDate());
            ps.setString(3, taxBean.getToDate());
            ps.setInt(4, taxBean.getModifiedBy());
            ps.setString(5, taxBean.getModifiedDate());
            ps.setInt(6, taxBean.getTaxId());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Integer deleteTaxDetails(TaxBean taxBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update tax set active=? where tax_id=?");
            ps.setInt(1, 0);
            ps.setInt(2, taxBean.getTaxId());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public TaxBean checkDatesForTax(String date) {
        TaxBean tb = new TaxBean();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM tax WHERE '"+date+"' BETWEEN from_date AND to_date AND active=1 LIMIT 1");
            ResultSet rst = ps.executeQuery();
            while(rst.next()){
                tb.setTax(rst.getFloat(2));
                tb.setTaxId(rst.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tb;
    }
    
}
