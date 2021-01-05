/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.daoImp;

import com.geeks.beans.AccountBean;
import com.geeks.beans.ProductBean;
import com.geeks.beans.StockBean;
import com.geeks.connectivity.Connectivity;
import com.geeks.dao.StockDAO;
import static com.geeks.daoImp.ProductDAOImpl.con;
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
public class StockDAOImpl implements StockDAO{

    static Connection con = Connectivity.connect();
    public static int setQuantity;
    
    @Override
    public ResultSet viewAllStockResultSet() {
        ResultSet rst=null;
        try {
             PreparedStatement ps = con.prepareStatement("SELECT s.stock_id AS Id,s.stock AS Stock,a.name AS Dealer,p.product_name AS Product,s.quantity AS Quantity,s.whole_sale_price AS WSP,s.retail_price AS RSP,s.date AS Buy_Date,u1.full_name AS Created_By,s.created_date AS Created_On,u2.full_name AS Modified_By,s.modified_date AS Modified_On \n" +
"FROM stock s\n" +
"INNER JOIN account a ON s.account_id=a.id\n" +
"INNER JOIN product p ON s.product_id=p.product_id\n" +
"INNER JOIN USER u1 ON u1.user_id = s.created_by\n" +
"LEFT JOIN USER u2 ON u2.user_id = s.modified_by \n" +
"AND s.active=1;");
            rst = ps.executeQuery();
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return rst;
    }
    
    @Override
    public Integer addStockDetails(StockBean stockBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("insert into stock(stock,account_id,product_id,quantity,whole_sale_price"
                    + ",retail_price,date,active,created_by,created_date) values(?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, stockBean.getStock());
            ps.setInt(2, stockBean.getAccountBean().getAccountId());
            ps.setInt(3, stockBean.getProductBean().getProductID());
            ps.setInt(4, stockBean.getQuantity());
            ps.setString(5, stockBean.getWSP());
            ps.setFloat(6, stockBean.getRP());
            ps.setString(7, stockBean.getDate());
            ps.setInt(8, 1);
            ps.setInt(9, stockBean.getCreatedBy());
            ps.setString(10, stockBean.getCreatedDate());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Boolean checkStock(StockBean stockBean) {
         try {
            PreparedStatement ps = con.prepareStatement("select * from account u where u.name=?");
            ps.setString(1, stockBean.getStock());
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
    public List<StockBean> setAllTxt(StockBean stockBean) {
        ArrayList<StockBean> list = new ArrayList<>();
        try {
             PreparedStatement ps = con.prepareStatement("SELECT s.stock_id,s.stock,a.name,p.product_name,s.quantity,s.whole_sale_price AS WSP,"
                     + "s.retail_price,s.date,s.created_by,s.created_date,s.modified_by,s.modified_date "
                     + "FROM stock s,account a,product p WHERE s.account_id=a.id AND s.product_id=p.product_id AND s.active=1 and s.stock_id=?");
            ps.setInt(1, stockBean.getStockID());
            ResultSet rst = ps.executeQuery();
            while(rst.next()){
                StockBean s = new StockBean();
                s.setStockID(rst.getInt("stock_id"));
                s.setStock(rst.getString("stock"));
                    AccountBean ab = new AccountBean();
                    ab.setAccountName(rst.getString("name"));
                s.setAccountBean(ab);
                    ProductBean pb = new ProductBean();
                    pb.setProductName(rst.getString("product_name"));
                s.setProductBean(pb);
                s.setQuantity(rst.getInt(5));
                s.setWSP(rst.getString(6));
                s.setRP(rst.getFloat(7));
                s.setDate(rst.getString(8));
                s.setCreatedBy(rst.getInt(9));
                s.setCreatedDate(rst.getString(10));
                s.setModifiedBy(rst.getInt(11));
                s.setModifiedDate(rst.getString(12));
                list.add(s);
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return list;
    }

    @Override
    public Integer updateStockDetails(StockBean stockBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update stock set stock=?,account_id=?,product_id=?,quantity=?,whole_sale_price=?"
                    + ",retail_price=?,date=?,active=?,modified_by=?,modified_date=? where stock_id=?");
            ps.setString(1, stockBean.getStock());
            ps.setInt(2, stockBean.getAccountBean().getAccountId());
            ps.setInt(3, stockBean.getProductBean().getProductID());
            ps.setInt(4, stockBean.getQuantity());
            ps.setString(5, stockBean.getWSP());
            ps.setFloat(6, stockBean.getRP());
            ps.setString(7, stockBean.getDate());
            ps.setInt(8, 1);
            ps.setInt(9, stockBean.getModifiedBy());
            ps.setString(10, stockBean.getModifiedDate());
            ps.setInt(11, stockBean.getStockID());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Integer deleteStockDetails(StockBean stockBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update stock set active=? where stock_id=?");
            ps.setInt(1, 0);
            ps.setInt(2, stockBean.getStockID());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public List<String> viewAllAccount() {
        List<String> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select * from account u");
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                list.add(rst.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<String> viewAllProduct() {
        List<String> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select * from product u");
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                list.add(rst.getString("product_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Integer getIdByAccount(AccountBean accountBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("select * from account where name=?");
            ps.setString(1, accountBean.getAccountName());
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
    public Integer getIdByProduct(ProductBean productBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("select * from product where product_name=?");
            ps.setString(1, productBean.getProductName());
            ResultSet rst = ps.executeQuery();
            while (rst.next()) {
                r = rst.getInt(1);
                setQuantity = rst.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Integer updateProductByStock(StockBean stockBean) {
        int r=0;
        try {
            PreparedStatement ps = con.prepareStatement("update product set quantity=?,price=? where product_id=?");
            ps.setInt(1, stockBean.getQuantity());
            ps.setFloat(2, stockBean.getRP());
            ps.setInt(3, stockBean.getProductBean().getProductID());
            r = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
    
}
