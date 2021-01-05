/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.daoImp;

import com.geeks.beans.PackageBean;
import com.geeks.connectivity.Connectivity;
import com.geeks.dao.PackageDAO;
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
public class PackageDAOImpl implements PackageDAO{

    static Connection con = Connectivity.connect();
    @Override
    public ResultSet viewAllPackageResultSet(PackageBean packageBean) {
        ResultSet rst=null;
        try {
            Statement st = con.createStatement();
            rst = st.executeQuery("SELECT p.pkg_id,p.pkg_name,p.count,p.price,u.full_name AS created_by,p.created_date,m.full_name AS modified_by,p.modified_date FROM package p LEFT JOIN `user` u ON u.user_id = p.created_by LEFT JOIN `user` m ON p.modified_by = m.user_id WHERE p.active =1 and p.pkg_name like '"+packageBean.getPackageName()+"%'");  
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return rst;
    }

    @Override
    public Integer addPackageDetails(PackageBean packageBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("insert into package(pkg_name,count,price,active,created_by,created_date) values(?,?,?,?,?,?)");
            ps.setString(1, packageBean.getPackageName());
            ps.setInt(2, packageBean.getPackageCount());
            ps.setFloat(3, packageBean.getPackagePrice());
            ps.setInt(4, 1);
            ps.setInt(5, packageBean.getCreatedBy());
            ps.setString(6, packageBean.getCreatedDate());  
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public PackageBean checkPackage(PackageBean packageBean) {
        PackageBean pb = null;
        try {
            PreparedStatement ps = con.prepareStatement("select * from package u where u.pkg_name=?");
            ps.setString(1, packageBean.getPackageName());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                pb = new PackageBean();
                pb.setPackageId(rst.getInt("pkg_id"));
                pb.setPackageName(rst.getString("pkg_name"));
                pb.setPackagePrice(rst.getFloat("price"));
                pb.setPackageCount(rst.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pb;
    }

    @Override
    public PackageBean setAllTxt(PackageBean packageBean) {
        PackageBean pb = null;
        try {
            PreparedStatement ps = con.prepareStatement("select * from package u where u.pkg_id=?");
            ps.setInt(1, packageBean.getPackageId());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                pb = new PackageBean();
                pb.setPackageName(rst.getString("pkg_name"));
                pb.setPackageCount(rst.getInt("count"));
                pb.setPackagePrice(rst.getFloat("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pb;
    }

    @Override
    public Integer updatePackageDetails(PackageBean packageBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update package set pkg_name=?,count=?,price=?,modified_by=?,modified_date=? where pkg_id=?");
            ps.setString(1, packageBean.getPackageName());
            ps.setInt(2, packageBean.getPackageCount());
            ps.setFloat(3, packageBean.getPackagePrice());
            ps.setInt(4, packageBean.getModifiedBy());
            ps.setString(5, packageBean.getModifiedDate());
            ps.setInt(6, packageBean.getPackageId());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Integer deletePackageDetails(PackageBean packageBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update package set active=? where pkg_id=?");
            ps.setInt(1, 0);
            ps.setInt(2, packageBean.getPackageId());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public List<String> getAllPackage() {
        List<String> li = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select pkg_name from package where active=1");
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                li.add(rst.getString("pkg_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return li;
    }
}
