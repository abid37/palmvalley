/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.frames;

import com.geeks.beans.AccountBean;
import com.geeks.beans.OrderBean;
import com.geeks.beans.OrderDetailsBean;
import com.geeks.beans.ProductBean;
import com.geeks.beans.StockBean;
import com.geeks.beans.TaxBean;
import com.geeks.beans.UserBean;
import com.geeks.dao.AccountDAO;
import com.geeks.dao.OrderDAO;
import com.geeks.dao.OrderDetailsDAO;
import com.geeks.dao.ProductDAO;
import com.geeks.dao.StockDAO;
import com.geeks.dao.TaxDAO;
import com.geeks.dao.UserDAO;
import com.geeks.daoImp.AccountDAOImpl;
import com.geeks.daoImp.OrderDAOImpl;
import com.geeks.daoImp.OrderDetailsDAOImpl;
import com.geeks.daoImp.ProductDAOImpl;
import com.geeks.daoImp.StockDAOImpl;
import com.geeks.daoImp.TaxDAOImpl;
import com.geeks.daoImp.UserDAOImp;
import com.geeks.utils.BarCodeGenerator;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author xyz
 */
public class OrderFrame extends javax.swing.JFrame {

    /**
     * Creates new form UserDAO
     */
    String val="",retrun="";
    private void setOrderTxtRetrunTxt(){
        val = HotelSales.order;
        retrun = HotelSales.paid;
        txtOrderNo.setText(val);
        txtPaid.setText(retrun);
    }
    public OrderFrame() throws SQLException {
        initComponents();
        setBarCode();
        setProductTable();
        setOrderTable();
        setDate();
        setTax();
        setOrderTxtRetrunTxt();
        setTotalGTotal();
        setOrderDetailsTable();
        setComboCustomer();
    }
    
    private void setOrderDetailsTable(){
        OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAOImpl();
        String order = txtOrderNo.getText();
        lblOrderNo.setText(order);
        if(order.equals("")){
            setBarCode();
        }
        try {
            tableOrder.setModel(buildTableModel(orderDetailsDAO.viewAllOrderDetailsBeanResultSet(order)));
            setTotalGTotal();
        } catch (SQLException ex) {
            Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        OrderBean orderBean1 = new OrderBean();
            orderBean1.setOrderNumber(order);
        OrderDAO orderDAO = new OrderDAOImpl();
        if(orderDAO.getOrderDetails(orderBean1)!=null){
            OrderBean orderBean = orderDAO.getOrderDetails(orderBean1);
            String date[] = orderBean.getDate().split("-");
            comboDay.setSelectedItem(date[2]);
            comboMon.setSelectedIndex(Integer.parseInt(date[1]) - 1);
            comboYear.setSelectedItem(date[0]);

            lblTax.setText(orderBean.getTaxBean().getTax()+"%");
            lblTax2.setText(orderBean.getTaxBean().getTax()+"%");

            comboCustomer.setSelectedItem(orderBean.getAccountBean().getAccountName());
            btnUpdate.setEnabled(true);
        }else{
            btnUpdate.setEnabled(false);
        }
    }
    
    private void setComboCustomer() {
        AccountDAO accountDAO = new AccountDAOImpl();
        List<String> li = accountDAO.getAllAccountNames();
        for (String s : li) {
            comboCustomer.addItem(s);
        }
        comboCustomer.setSelectedItem("Walking Away");
    }

    private void setTotalGTotal() {
        //sum start
        lblTotal.setText("");
        lblGTotal.setText("");
        String tax = lblTax.getText();
        tax = tax.replace("%", "");
        float taxNum = Float.parseFloat(tax);

        int len = tableOrder.getRowCount() - 1;
        float total = 0;
        while (len >= 0) {
            BigDecimal val = (BigDecimal) tableOrder.getValueAt(len, 4);
            float val2 = val.floatValue();
            total = total + val2;
            len--;
        }

        float gtotal = (total + ((taxNum / 100) * total));
        lblTotal.setText("Rs. " + total);
        lblGTotal.setText("Rs. " + gtotal);
        //sum end
    }

    private void setAllTxtsForReturn() {
        int id = (int) tableOrder.getValueAt(tableOrder.getSelectedRow(), 0);
        OrderDetailsBean odb1 = new OrderDetailsBean();
        odb1.setOdId(id);

        OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAOImpl();
        OrderDetailsBean odb = orderDetailsDAO.setAllTxtForReturn(odb1);
        txtProduct.setText(odb.getProductBean().getProductName());
        txtQuantity1.setText(String.valueOf(odb.getQuantity()));
        lblRP.setText(String.valueOf(odb.getPrice()));

    }

    private void setBarCode() {
        String bar = BarCodeGenerator.generator();
        ProductDAO productDAO = new ProductDAOImpl();
        if (productDAO.checkBarCode().contains(bar)) {
            setBarCode();
        } else {
            lblOrderNo.setText(bar);
        }
    }

    private void setTax() {
        String day = comboDay.getSelectedItem().toString();
        String mon = String.valueOf(comboMon.getSelectedIndex() + 1);
        String year = comboYear.getSelectedItem().toString();

        if (Integer.parseInt(mon) <= 9) {
            mon = "0" + mon;
        }
        String date = year + "-" + mon + "-" + day;

        TaxDAO taxDAO = new TaxDAOImpl();
        TaxBean tb = taxDAO.checkDatesForTax(date);
        if(tb==null){
            lblTax.setText("0.0%");
            lblTax2.setText("0.0%");
        }else{
            lblTax.setText(tb.getTax() + "%");
            lblTax2.setText(tb.getTax() + "%");
        }
        setTotalGTotal();
    }

    private void setDate() {
//        Date d = new Date();
//        comboMon.setSelectedIndex(d.getMonth());
//        if (d.getDate() <= 9) {
//            comboDay.setSelectedItem("0" + String.valueOf(d.getDate()));
//        } else {
//            comboDay.setSelectedItem(String.valueOf(d.getDate()));
//        }
//        comboYear.setSelectedItem(d.getYear());
        Date d = new Date();
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        String m[] = f.format(d).split("-");
        System.out.println(f.format(d));
        comboMon.setSelectedIndex(Integer.parseInt(m[1])-1);
        comboDay.setSelectedItem(m[0]);
        comboYear.setSelectedItem(m[2]);
    }

    public void setProductTable() throws SQLException {
        ProductDAO productDAO = new ProductDAOImpl();
        String name = txtProduct.getText();
        ProductBean pb = new ProductBean();
        pb.setProductName(name);
        tableProduct.setModel(buildTableModel(productDAO.viewAllProductResultSetForOrder(pb)));
    }

    public void setOrderTable() throws SQLException {
        OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAOImpl();
        String order = lblOrderNo.getText();
        tableOrder.setModel(buildTableModel(orderDetailsDAO.viewAllOrderDetailsBeanResultSet(order)));
    }

    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtProduct = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        lblRP = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtPaid = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableOrder = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        comboDay = new javax.swing.JComboBox();
        comboMon = new javax.swing.JComboBox();
        comboYear = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        comboCustomer = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableProduct = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        lblOrderNo = new javax.swing.JLabel();
        lblTax = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        lblTax2 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblGTotal = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtQuantity1 = new javax.swing.JTextField();
        lblMsgForReturn = new javax.swing.JLabel();
        lblReturn = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        btnUpdate1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtOrderNo = new javax.swing.JTextField();
        btnDelete1 = new javax.swing.JButton();
        btnNewOrder = new javax.swing.JButton();
        btnNewOrder1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtProduct.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        txtProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProductKeyReleased(evt);
            }
        });
        jPanel2.add(txtProduct, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 160, 270, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel1.setText("Product");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 170, 40));

        lblRP.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        lblRP.setText("Rs.");
        jPanel2.add(lblRP, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 160, 270, 40));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel5.setText("Quantity");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 180, 40));

        txtPaid.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPaidKeyReleased(evt);
            }
        });
        jPanel2.add(txtPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(1470, 550, 210, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel7.setText("Retail Price");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 160, 190, 40));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel9.setText("Tax");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 90, 180, 40));

        tableOrder.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tableOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableOrderMouseClicked(evt);
            }
        });
        tableOrder.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableOrderKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tableOrder);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 20, 570, 290));

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jButton4.setText("< Back");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 670, 180, -1));

        comboDay.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        comboDay.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        comboDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboDayActionPerformed(evt);
            }
        });
        jPanel2.add(comboDay, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 20, 70, 40));

        comboMon.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        comboMon.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" }));
        comboMon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboMonActionPerformed(evt);
            }
        });
        jPanel2.add(comboMon, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 20, 90, 40));

        comboYear.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        comboYear.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2019", "2020", "2021", "2022", "2023" }));
        comboYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboYearActionPerformed(evt);
            }
        });
        jPanel2.add(comboYear, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 20, 100, 40));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel8.setText("Order date");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 20, 190, 40));

        comboCustomer.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        comboCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboCustomerActionPerformed(evt);
            }
        });
        jPanel2.add(comboCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, 270, 40));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel10.setText("Customer");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 170, 40));

        tableProduct.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tableProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableProductMouseClicked(evt);
            }
        });
        tableProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableProductKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tableProduct);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 1070, 350));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel6.setText("Order no.");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 170, 40));

        lblOrderNo.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        lblOrderNo.setText("abc-123");
        jPanel2.add(lblOrderNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 230, 40));

        lblTax.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        lblTax.setText("%");
        jPanel2.add(lblTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 90, 140, 40));

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jButton5.setText("Return All");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1470, 340, 210, -1));

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jButton6.setText("Edit");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 97, 130, 30));

        jButton7.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jButton7.setText("<< Return");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 340, 210, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel11.setText("Tax");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1230, 400, 180, 40));

        lblTax2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTax2.setText("%");
        jPanel2.add(lblTax2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1470, 400, 210, 40));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel12.setText("Total");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(1230, 450, 190, 40));

        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTotal.setText("Rs.");
        jPanel2.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1470, 450, 210, 40));

        lblGTotal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblGTotal.setText("Rs.");
        jPanel2.add(lblGTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1470, 500, 210, 40));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel14.setText("Paid");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(1230, 550, 190, 40));

        txtQuantity1.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        txtQuantity1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantity1ActionPerformed(evt);
            }
        });
        txtQuantity1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQuantity1KeyReleased(evt);
            }
        });
        jPanel2.add(txtQuantity1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 230, 270, -1));

        lblMsgForReturn.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblMsgForReturn.setText("Returned");
        jPanel2.add(lblMsgForReturn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1230, 600, 190, 40));

        lblReturn.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblReturn.setText("Rs.");
        jPanel2.add(lblReturn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1470, 600, 210, 40));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel15.setText("Grand Total");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(1230, 500, 190, 40));

        btnUpdate.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.setEnabled(false);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel2.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 670, 150, -1));

        btnUpdate1.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        btnUpdate1.setText("Generate Receit");
        btnUpdate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdate1ActionPerformed(evt);
            }
        });
        jPanel2.add(btnUpdate1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1280, 680, 290, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1700, 740));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel4.setText("Order Panel");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 30, -1, 40));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel13.setText("Order No.");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 40, 190, 40));

        txtOrderNo.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        txtOrderNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtOrderNoKeyReleased(evt);
            }
        });
        jPanel1.add(txtOrderNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 40, 270, -1));

        btnDelete1.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        btnDelete1.setText("Fetch");
        btnDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnDelete1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1550, 40, 150, -1));

        btnNewOrder.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        btnNewOrder.setText("New Order");
        btnNewOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewOrderActionPerformed(evt);
            }
        });
        jPanel1.add(btnNewOrder, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        btnNewOrder1.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        btnNewOrder1.setText("Get All Order");
        btnNewOrder1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewOrder1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnNewOrder1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 30, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1721, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void addForOneUpdateForTwo(int one) {
        String stock = txtProduct.getText();
        // String dealer = comboAccount.getSelectedItem().toString();
        //String product = comboProduct.getSelectedItem().toString();
        //String WSP = txtWSP.getText();
        //String RP = txtRP.getText();

        String p = txtPaid.getText();
        int quantity = 0;
        if (p.equals("")) {
            quantity = 0;
        } else {
            quantity = Integer.parseInt(p);
        }

        String day = comboDay.getSelectedItem().toString();
        int mon = comboMon.getSelectedIndex() + 1;
        String m = mon + "";
        if (mon < 10) {
            m = "0" + mon;
        }

        String year = comboYear.getSelectedItem().toString();
        String purchaseDate = year + "-" + m + "-" + day;

        int createdBy = UserDAOImp.userId;

        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("yyyy-MM-dd");

        String currentDate = df.format(d);

        StockBean stockBean = new StockBean();
        stockBean.setStock(stock);
        StockDAO stockDAO = new StockDAOImpl();
        AccountBean accountBean = new AccountBean();
        //accountBean.setAccountName(dealer);
        int accountId = stockDAO.getIdByAccount(accountBean);
        accountBean.setAccountId(accountId);

        ProductBean productBean = new ProductBean();
        //productBean.setProductName(product);
        int productId = stockDAO.getIdByProduct(productBean);
        productBean.setProductID(productId);

        stockBean.setAccountBean(accountBean);
        stockBean.setProductBean(productBean);
        stockBean.setQuantity(quantity);
        //stockBean.setWSP(WSP);
        //stockBean.setRP(RP);
        stockBean.setDate(purchaseDate);

        if (one == 1) {
            if (stockDAO.checkStock(stockBean)) {
                JOptionPane.showMessageDialog(this, "Stock name already exits!");
            } else {
                stockBean.setCreatedBy(createdBy);
                stockBean.setCreatedDate(currentDate);

                if (stockDAO.addStockDetails(stockBean) != 0) {
                    try {
                        clearAll();
                        setProductTable();
                        JOptionPane.showMessageDialog(this, "Stock added successfully!");
                    } catch (SQLException ex) {
                        Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add the stock!");
                }
            }
        } else if (one == 2) {
            int id = (int) tableOrder.getValueAt(tableOrder.getSelectedRow(), 0);

            stockBean.setModifiedBy(createdBy);
            stockBean.setModifiedDate(currentDate);
            stockBean.setStockID(id);

            if (stockDAO.updateStockDetails(stockBean) != 0) {
                try {
                    clearAll();
                    setProductTable();
                    JOptionPane.showMessageDialog(this, "Stock updated successfully!");
                } catch (SQLException ex) {
                    Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update the stock!");
            }
        } else if (one == 3) {
            int id = (int) tableOrder.getValueAt(tableOrder.getSelectedRow(), 0);
            stockBean.setStockID(id);
            if (stockDAO.deleteStockDetails(stockBean) != 0) {
                try {
                    clearAll();
                    setProductTable();
                    JOptionPane.showMessageDialog(this, "Stock deleted successfully!");
                } catch (SQLException ex) {
                    Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete the user!");
            }
        }
    }
    private void tableOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableOrderMouseClicked
        // TODO add your handling code here:
        setAllTxtsForReturn();
    }//GEN-LAST:event_tableOrderMouseClicked

    private void tableOrderKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableOrderKeyReleased
        // TODO add your handling code here:
        setAllTxtsForReturn();
    }//GEN-LAST:event_tableOrderKeyReleased

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        
    }//GEN-LAST:event_jPanel2MouseClicked

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jPanel1MouseClicked

    public void clearAll() {
        txtProduct.setText("");
        //comboAccount.setSelectedIndex(0);
        //comboProduct.setSelectedIndex(0);
        txtPaid.setText("");
        //txtWSP.setText("");
        //txtRP.setText("");

        Date d1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("yyyy-MM-dd");

        String date[] = df.format(d1).split("-");
        comboDay.setSelectedItem(date[2]);
        comboMon.setSelectedIndex(Integer.parseInt(date[1]) - 1);
        comboYear.setSelectedItem(date[0]);
    }
    private void btnDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete1ActionPerformed
        // TODO add your handling code here:
        OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAOImpl();
        String order = txtOrderNo.getText();
        lblOrderNo.setText(order);
        txtOrderNo.setText("");
        try {
            tableOrder.setModel(buildTableModel(orderDetailsDAO.viewAllOrderDetailsBeanResultSet(order)));
            setTotalGTotal();
        } catch (SQLException ex) {
            Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDelete1ActionPerformed

    private void comboCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboCustomerActionPerformed

    private void onSelectItem(){
         int id = (int) tableProduct.getValueAt(tableProduct.getSelectedRow(), 0);
            ProductDAO productDAO = new ProductDAOImpl();
            ProductBean pb = new ProductBean();
            pb.setProductID(id);
            System.out.println(id);
            List<ProductBean> li = productDAO.setAllTxt(pb);
            for (ProductBean pb1 : li) {
                lblRP.setText("Rs. " + pb1.getPrice());
                txtProduct.setText(pb1.getProductName());
            }
            setTax();
    }
    
    private void tableProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableProductMouseClicked
        // TODO add your handling code here:
        onSelectItem();
    }//GEN-LAST:event_tableProductMouseClicked

    private void tableProductKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableProductKeyReleased
        // TODO add your handling code here:
        onSelectItem();
    }//GEN-LAST:event_tableProductKeyReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int len = tableOrder.getRowCount() - 1;
        while (len >= 0) {
            int id = (int) tableOrder.getValueAt(len, 0);
            int orderId = (int) tableOrder.getValueAt(len, 9);
            OrderDetailsBean odb1 = new OrderDetailsBean();
            OrderBean ob = new OrderBean();
            ob.setOrderId(orderId);
            odb1.setOdId(id);
            odb1.setOrderBean(ob);

            OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAOImpl();
            OrderDAO orderDAO = new OrderDAOImpl();

            int productId = orderDetailsDAO.getProductIdByOrderId(odb1);
            ProductBean pb = new ProductBean();
            pb.setProductID(productId);

            odb1.setProductBean(pb);
            if (orderDetailsDAO.addProductIfReturns(odb1) == 2) {
                try {
                    txtProduct.setText("");
                    txtQuantity1.setText("");
                    lblRP.setText("");
                    setProductTable();
                    setOrderTable();
                    setTotalGTotal();
                    float total = Float.parseFloat(lblGTotal.getText().replace("Rs. ", ""));
                    String serial = lblOrderNo.getText();
                    ob.setOrderNumber(serial);
                    ob.setTotalPrice(total);
                    orderDAO.updateOrderDetails(ob);
                    if(total==0.0){
                        orderDAO.deleteOrderDetails(ob);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
                }

                //JOptionPane.showMessageDialog(this, "Returned!");
            }
            len--;
        }
//        int n = tableOrder.getRowCount();
//        for(int c=0; c<n; c++){
//            int id = (int) tableOrder.getValueAt(c, 0);
//         
//            OrderDetailsBean odb1 = new OrderDetailsBean();
//            odb1.setOdId(id);
//             
//            OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAOImpl();
//
//            int productId = orderDetailsDAO.getProductIdByOrderId(odb1);
//            ProductBean pb = new ProductBean();
//            pb.setProductID(productId);
//
//            odb1.setProductBean(pb);
//            if(orderDetailsDAO.addProductIfReturns(odb1)==2){
//                try {
//                    txtProduct.setText("");
//                    txtQuantity1.setText("");
//                    lblRP.setText("");
//                    setProductTable();
//                    setOrderTable();
//                } catch (SQLException ex) {
//                    Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//                //JOptionPane.showMessageDialog(this, "Returned!");
//            }
//        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            // TODO add your handling code here:
            TaxFrame tf = new TaxFrame();
            tf.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void txtProductKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int id = (int) tableProduct.getValueAt(0, 0);
            ProductDAO productDAO = new ProductDAOImpl();
            ProductBean pb = new ProductBean();
            pb.setProductID(id);
            List<ProductBean> li = productDAO.setAllTxt(pb);
            for (ProductBean pb1 : li) {
                lblRP.setText("Rs. " + pb1.getPrice());
                txtProduct.setText(pb1.getProductName());
            }
            setTax();
            txtProduct.transferFocus();
        }
        try {
            // TODO add your handling code here:
            setProductTable();
        } catch (SQLException ex) {
            Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtProductKeyReleased

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        int id = (int) tableOrder.getValueAt(tableOrder.getSelectedRow(), 0);
        int orderId = (int) tableOrder.getValueAt(tableOrder.getSelectedRow(), 9);
        OrderDetailsBean odb1 = new OrderDetailsBean();
        OrderBean ob = new OrderBean();
        ob.setOrderId(orderId);
        odb1.setOdId(id);
        odb1.setOrderBean(ob);

        OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAOImpl();
        OrderDAO orderDAO = new OrderDAOImpl();

        int productId = orderDetailsDAO.getProductIdByOrderId(odb1);
        ProductBean pb = new ProductBean();
        pb.setProductID(productId);

        odb1.setProductBean(pb);
        if (orderDetailsDAO.addProductIfReturns(odb1) == 2) {
            try {
                txtProduct.setText("");
                txtQuantity1.setText("");
                lblRP.setText("");
                setProductTable();
                setOrderTable();
                setTotalGTotal();
                float total = Float.parseFloat(lblGTotal.getText().replace("Rs. ", ""));
                String serial = lblOrderNo.getText();
                ob.setOrderNumber(serial);
                ob.setTotalPrice(total);
                orderDAO.updateOrderDetails(ob);
                if(total==0.0){
                    orderDAO.deleteOrderDetails(ob);
                }
            } catch (SQLException ex) {
                Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

            //JOptionPane.showMessageDialog(this, "Returned!");
        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void setOnQuantity1() {
        //s
        String ser = lblOrderNo.getText();
        OrderBean o = new OrderBean();
        o.setOrderNumber(ser);
        OrderDAO ao = new OrderDAOImpl();
        if (ao.getOrderDetails(o) != null) {

        }
        String day = comboDay.getSelectedItem().toString();
        String mon = String.valueOf(comboMon.getSelectedIndex() + 1);
        String year = comboYear.getSelectedItem().toString();
        if (Integer.parseInt(mon) <= 9) {
            mon = "0" + mon;
        }
        String date = year + "-" + mon + "-" + day;
        TaxDAO taxDAO = new TaxDAOImpl();
        TaxBean tb = taxDAO.checkDatesForTax(date);
        int taxId = tb.getTaxId();

        String serial = lblOrderNo.getText();

        String cust = comboCustomer.getSelectedItem().toString();
        AccountDAO accountDAO = new AccountDAOImpl();
        AccountBean ab = new AccountBean();
        ab.setAccountName(cust);

        int accountId = accountDAO.getIdByAccount(ab);
        System.out.println(accountId);
        String gtotalStr = lblGTotal.getText().replace("Rs. ", "");
        float gtotal = Float.parseFloat(gtotalStr);

        OrderBean orderBean = new OrderBean();
        orderBean.setOrderNumber(serial);
        AccountBean accountBean = new AccountBean();
        accountBean.setAccountId(accountId);
        orderBean.setAccountBean(accountBean);
        TaxBean taxBean = new TaxBean();
        taxBean.setTaxId(taxId);
        orderBean.setTaxBean(taxBean);
        orderBean.setTotalPrice(gtotal);

        OrderDAO orderDAO = new OrderDAOImpl();
        if (orderDAO.addOrderDetails(orderBean) != 0) {
            //System.out.println(orderDAO.getOrderId(orderBean));
            JOptionPane.showMessageDialog(this, "Done");
        }
        //e
        orderBean.setOrderNumber(serial);
        OrderBean ob1 = orderDAO.getOrderDetails(orderBean);
        orderBean.setOrderId(ob1.getOrderId());
        System.out.println(ob1.getOrderId());

        String quan = txtQuantity1.getText();
        int quantity = 0;
        if (quan.equals("")) {
            quantity = 0;
        } else {
            quantity = Integer.parseInt(quan);
        }
        //check quantity
        int qua = (int) tableProduct.getValueAt(0, 5);
        if (qua >= quantity) {
            float RP = 0;
            int id = (int) tableProduct.getValueAt(0, 0);
            ProductDAO productDAO = new ProductDAOImpl();
            ProductBean pb = new ProductBean();
            pb.setProductID(id);

            List<ProductBean> li = productDAO.setAllTxt(pb);
            for (ProductBean pb1 : li) {
                RP = pb1.getPrice();
            }

            int createdBy = UserDAOImp.userId;

            Date d = new Date();
            SimpleDateFormat df = new SimpleDateFormat();
            df.applyPattern("yyyy-MM-dd");
            String currentDate = df.format(d);

            OrderDetailsBean orderDetailsBean = new OrderDetailsBean();
            orderDetailsBean.setProductBean(pb);
            orderDetailsBean.setQuantity(quantity);
            orderDetailsBean.setPrice(RP);
            orderDetailsBean.setCreatedBy(createdBy);
            orderDetailsBean.setCreatedDate(currentDate);
            orderDetailsBean.setOrderBean(orderBean);

            OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAOImpl();
            if (orderDetailsDAO.checkIfProductExists(orderDetailsBean)) {
                if (orderDetailsDAO.updateIfProductExists(orderDetailsBean) == 2) {
                    //JOptionPane.showMessageDialog(this, "Done1");
                    txtProduct.setText("");
                    txtQuantity1.setText("");
                    lblRP.setText("");
                }
            } else {
                if (orderDetailsDAO.addOrderDetails(orderDetailsBean) == 2) {
                    txtProduct.setText("");
                    txtQuantity1.setText("");
                    lblRP.setText("");
                    //JOptionPane.showMessageDialog(this, "Done2");
                }
            }
            txtQuantity1.transferFocusBackward();
        } else {
            JOptionPane.showMessageDialog(this, "You are out of stock!");
        }
        try {
            // TODO add your handling code here:
            setProductTable();
            setOrderTable();
            setTotalGTotal();
        } catch (SQLException ex) {
            Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        //start re
        lblReturn.setText("");
        String val = lblGTotal.getText().replace("Rs. ", "");

        float getAmmount = 0;
        String checkRe = txtPaid.getText();
        if (checkRe.equals("")) {
            getAmmount = 0;
        } else {
            getAmmount = Float.parseFloat(txtPaid.getText());
        }
        float returned = Float.parseFloat(val) - getAmmount;
        if (Float.parseFloat(val) <= getAmmount) {
            lblMsgForReturn.setText("Returned");
            lblReturn.setText("Rs. " + Math.abs(returned));
        } else {
            lblMsgForReturn.setText("Remaining");
            lblReturn.setText("Rs. " + Math.abs(returned));
        }
        //end re
    }

    private void setOnQuantity2() {
        //s
        String ser = lblOrderNo.getText();
        OrderBean o = new OrderBean();
        o.setOrderNumber(ser);
        OrderDAO ao = new OrderDAOImpl();
        if (ao.getOrderDetails(o) == null) {
            String day = comboDay.getSelectedItem().toString();
            String mon = String.valueOf(comboMon.getSelectedIndex() + 1);
            String year = comboYear.getSelectedItem().toString();
            if (Integer.parseInt(mon) <= 9) {
                mon = "0" + mon;
            }
            String date = year + "-" + mon + "-" + day;
            TaxDAO taxDAO = new TaxDAOImpl();
            TaxBean tb = taxDAO.checkDatesForTax(date);
            int taxId = tb.getTaxId();

            String serial = lblOrderNo.getText();

            String cust = comboCustomer.getSelectedItem().toString();
            AccountDAO accountDAO = new AccountDAOImpl();
            AccountBean ab = new AccountBean();
            ab.setAccountName(cust);

            int accountId = accountDAO.getIdByAccount(ab);
            System.out.println(accountId);
            String gtotalStr = lblGTotal.getText().replace("Rs. ", "");
            float gtotal = Float.parseFloat(gtotalStr);

            OrderBean orderBean = new OrderBean();
            orderBean.setOrderNumber(serial);
            AccountBean accountBean = new AccountBean();
            accountBean.setAccountId(accountId);
            orderBean.setAccountBean(accountBean);
            TaxBean taxBean = new TaxBean();
            taxBean.setTaxId(taxId);
            orderBean.setTaxBean(taxBean);
            orderBean.setTotalPrice(gtotal);

            OrderDAO orderDAO = new OrderDAOImpl();
            if (orderDAO.addOrderDetails(orderBean) != 0) {
                //System.out.println(orderDAO.getOrderId(orderBean));
                JOptionPane.showMessageDialog(this, "Done");
            }
            //e
            orderBean.setOrderNumber(serial);
            OrderBean ob1 = orderDAO.getOrderDetails(orderBean);
            orderBean.setOrderId(ob1.getOrderId());
            System.out.println(ob1.getOrderId());

            String quan = txtQuantity1.getText();
            int quantity = 0;
            if (quan.equals("")) {
                quantity = 0;
            } else {
                quantity = Integer.parseInt(quan);
            }
            //check quantity
            int qua = (int) tableProduct.getValueAt(0, 5);
            if (qua >= quantity) {
                float RP = 0;
                int id = (int) tableProduct.getValueAt(0, 0);
                ProductDAO productDAO = new ProductDAOImpl();
                ProductBean pb = new ProductBean();
                pb.setProductID(id);

                List<ProductBean> li = productDAO.setAllTxt(pb);
                for (ProductBean pb1 : li) {
                    RP = pb1.getPrice();
                }

                int createdBy = UserDAOImp.userId;

                Date d = new Date();
                SimpleDateFormat df = new SimpleDateFormat();
                df.applyPattern("yyyy-MM-dd");
                String currentDate = df.format(d);

                OrderDetailsBean orderDetailsBean = new OrderDetailsBean();
                orderDetailsBean.setProductBean(pb);
                orderDetailsBean.setQuantity(quantity);
                orderDetailsBean.setPrice(RP);
                orderDetailsBean.setCreatedBy(createdBy);
                orderDetailsBean.setCreatedDate(currentDate);
                orderDetailsBean.setOrderBean(orderBean);

                OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAOImpl();
                if (orderDetailsDAO.checkIfProductExists(orderDetailsBean)) {
                    if (orderDetailsDAO.updateIfProductExists(orderDetailsBean) == 2) {
                        //JOptionPane.showMessageDialog(this, "Done1");
                        txtProduct.setText("");
                        txtQuantity1.setText("");
                        lblRP.setText("");
                    }
                } else {
                    if (orderDetailsDAO.addOrderDetails(orderDetailsBean) == 2) {
                        txtProduct.setText("");
                        txtQuantity1.setText("");
                        lblRP.setText("");
                        //JOptionPane.showMessageDialog(this, "Done2");
                    }
                }
                txtQuantity1.transferFocusBackward();
            } else {
                JOptionPane.showMessageDialog(this, "You are out of stock!");
            }
            try {
                // TODO add your handling code here:
                setProductTable();
                setOrderTable();
                setTotalGTotal();
            } catch (SQLException ex) {
                Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

            //start re
            lblReturn.setText("");
            String val = lblGTotal.getText().replace("Rs. ", "");

            float getAmmount = 0;
            String checkRe = txtPaid.getText();
            if (checkRe.equals("")) {
                getAmmount = 0;
            } else {
                getAmmount = Float.parseFloat(txtPaid.getText());
            }
            float returned = Float.parseFloat(val) - getAmmount;
            if (Float.parseFloat(val) <= getAmmount) {
                lblMsgForReturn.setText("Returned");
                lblReturn.setText("Rs. " + Math.abs(returned));
            } else {
                lblMsgForReturn.setText("Remaining");
                lblReturn.setText("Rs. " + Math.abs(returned));
            }
            //end re
        } else {
            String day = comboDay.getSelectedItem().toString();
            String mon = String.valueOf(comboMon.getSelectedIndex() + 1);
            String year = comboYear.getSelectedItem().toString();
            if (Integer.parseInt(mon) <= 9) {
                mon = "0" + mon;
            }
            String date = year + "-" + mon + "-" + day;
            TaxDAO taxDAO = new TaxDAOImpl();
            TaxBean tb = taxDAO.checkDatesForTax(date);
            int taxId = tb.getTaxId();

            String serial = lblOrderNo.getText();

            String cust = comboCustomer.getSelectedItem().toString();
            AccountDAO accountDAO = new AccountDAOImpl();
            AccountBean ab = new AccountBean();
            ab.setAccountName(cust);

            int accountId = accountDAO.getIdByAccount(ab);
            System.out.println(accountId);
            String gtotalStr = lblGTotal.getText().replace("Rs. ", "");
            float gtotal = Float.parseFloat(gtotalStr);

            OrderBean orderBean = new OrderBean();
            orderBean.setOrderNumber(serial);
            AccountBean accountBean = new AccountBean();
            accountBean.setAccountId(accountId);
            orderBean.setAccountBean(accountBean);
            TaxBean taxBean = new TaxBean();
            taxBean.setTaxId(taxId);
            orderBean.setTaxBean(taxBean);
            orderBean.setTotalPrice(gtotal);

            OrderDAO orderDAO = new OrderDAOImpl();

            //e
            orderBean.setOrderNumber(serial);
            OrderBean ob1 = orderDAO.getOrderDetails(orderBean);
            orderBean.setOrderId(ob1.getOrderId());
            System.out.println(ob1.getOrderId());

            String quan = txtQuantity1.getText();
            int quantity = 0;
            if (quan.equals("")) {
                quantity = 0;
            } else {
                quantity = Integer.parseInt(quan);
            }
            //check quantity
            int qua = (int) tableProduct.getValueAt(0, 5);
            if (qua >= quantity) {
                float RP = 0;
                int id = (int) tableProduct.getValueAt(0, 0);
                ProductDAO productDAO = new ProductDAOImpl();
                ProductBean pb = new ProductBean();
                pb.setProductID(id);

                List<ProductBean> li = productDAO.setAllTxt(pb);
                for (ProductBean pb1 : li) {
                    RP = pb1.getPrice();
                }

                int createdBy = UserDAOImp.userId;

                Date d = new Date();
                SimpleDateFormat df = new SimpleDateFormat();
                df.applyPattern("yyyy-MM-dd");
                String currentDate = df.format(d);

                OrderDetailsBean orderDetailsBean = new OrderDetailsBean();
                orderDetailsBean.setProductBean(pb);
                orderDetailsBean.setQuantity(quantity);
                orderDetailsBean.setPrice(RP);
                orderDetailsBean.setCreatedBy(createdBy);
                orderDetailsBean.setCreatedDate(currentDate);
                orderDetailsBean.setOrderBean(orderBean);

                OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAOImpl();
                if (orderDetailsDAO.checkIfProductExists(orderDetailsBean)) {
                    if (orderDetailsDAO.updateIfProductExists(orderDetailsBean) == 2) {
                        //JOptionPane.showMessageDialog(this, "Done1");
                        txtProduct.setText("");
                        txtQuantity1.setText("");
                        lblRP.setText("");
                    }
                } else {
                    if (orderDetailsDAO.addOrderDetails(orderDetailsBean) == 2) {
                        txtProduct.setText("");
                        txtQuantity1.setText("");
                        lblRP.setText("");
                        //JOptionPane.showMessageDialog(this, "Done2");
                    }
                }
                txtQuantity1.transferFocusBackward();
            } else {
                JOptionPane.showMessageDialog(this, "You are out of stock!");
            }
            try {
                // TODO add your handling code here:
                setProductTable();
                setOrderTable();
                setTotalGTotal();
            } catch (SQLException ex) {
                Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

            //start re
            lblReturn.setText("");
            String val = lblGTotal.getText().replace("Rs. ", "");

            float getAmmount = 0;
            String checkRe = txtPaid.getText();
            if (checkRe.equals("")) {
                getAmmount = 0;
            } else {
                getAmmount = Float.parseFloat(txtPaid.getText());
            }
            float returned = Float.parseFloat(val) - getAmmount;
            if (Float.parseFloat(val) <= getAmmount) {
                lblMsgForReturn.setText("Returned");
                lblReturn.setText("Rs. " + Math.abs(returned));
            } else {
                lblMsgForReturn.setText("Remaining");
                lblReturn.setText("Rs. " + Math.abs(returned));
            }
            //end re
            orderBean.setTotalPrice(gtotal);
            if (orderDAO.updateOrderDetails(orderBean) != 0) {
                //System.out.println(orderDAO.getOrderId(orderBean));
                JOptionPane.showMessageDialog(this, "Done");
            }
        }

    }

    int orderId = 0;
    private void txtQuantity1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQuantity1KeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //txtProduct.transferFocus();
            //start data for order table

            //for tax_id
            String day = comboDay.getSelectedItem().toString();
            String mon = String.valueOf(comboMon.getSelectedIndex() + 1);
            String year = comboYear.getSelectedItem().toString();
            if (Integer.parseInt(mon) <= 9) {
                mon = "0" + mon;
            }
            String date = year + "-" + mon + "-" + day;

            int taxId=0;
            TaxDAO taxDAO = new TaxDAOImpl();
            TaxBean tb = taxDAO.checkDatesForTax(date);
            if(tb==null){
                lblTax.setText("0.0%");
                lblTax2.setText("0.0%");
            }else{
                taxId = tb.getTaxId();
                lblTax.setText(tb.getTax() + "%");
                lblTax2.setText(tb.getTax() + "%");
            }

            //for order_no
            String serial = lblOrderNo.getText();

            //for customer_id
            String cust = comboCustomer.getSelectedItem().toString();
            AccountDAO accountDAO = new AccountDAOImpl();
            AccountBean ab = new AccountBean();
            ab.setAccountName(cust);

            int customerId = accountDAO.getIdByAccount(ab);
            
            float total = Float.parseFloat(lblGTotal.getText().replace("Rs. ", ""));

            //System.out.println("Order table-->" + taxId + "\t" + serial + "\t" + customerId+"\t"+total);
            
            OrderBean orderBean = new OrderBean();
                TaxBean taxBean = new TaxBean();
                taxBean.setTaxId(taxId);
            orderBean.setTaxBean(taxBean);
            orderBean.setOrderNumber(serial);
                AccountBean accountBean = new AccountBean();
                accountBean.setAccountId(customerId);
            orderBean.setAccountBean(accountBean);
            orderBean.setDate(date);
            
            //for order_id for order details table
            
            OrderDAO orderDAO = new OrderDAOImpl();
            
            if(orderDAO.getOrderDetails(orderBean)==null){
                if(orderDAO.addOrderDetails(orderBean)!=0){
                OrderBean orderBean1 = orderDAO.getOrderDetails(orderBean);
                orderId = orderBean1.getOrderId();
                }
            }else{
                OrderBean orderBean1 = orderDAO.getOrderDetails(orderBean);
                orderId = orderBean1.getOrderId();
            }
            
            //end data for order table
            //start data for order details table
            
            //for active 1
            
            //for created_by
            int createdBy = UserDAOImp.userId;

            //for created_date
            Date d = new Date();
            SimpleDateFormat df = new SimpleDateFormat();
            df.applyPattern("yyyy-MM-dd");
            String currentDate = df.format(d);
            
            //for product_id
            int productId=0;
            if(tableProduct.isRowSelected(tableProduct.getSelectedRow())){
                productId = (int) tableProduct.getValueAt(tableProduct.getSelectedRow(), 0);
            }else{
                productId = (int) tableProduct.getValueAt(0, 0);
            }
            

            //for quantity
            String quan = txtQuantity1.getText();
            int quantity = 0;
            if (quan.equals("")) {
                quantity = 0;
            } else {
                quantity = Integer.parseInt(quan);
            }
            //check quantity
            int qua = (int) tableProduct.getValueAt(0, 5);
            if (qua >= quantity) {
                float RP = 0;
                //int id = (int) tableProduct.getValueAt(0, 0);
                ProductDAO productDAO = new ProductDAOImpl();
                ProductBean pb = new ProductBean();
                pb.setProductID(productId);

                List<ProductBean> li = productDAO.setAllTxt(pb);
                for (ProductBean pb1 : li) {
                    RP = pb1.getPrice();
                }

                
                //System.out.println("Order Details table-->" + productId+"\t"+quantity+"\t"+RP+"\t"+createdBy+"\t"+currentDate+"\t"+orderId);
                
               
                //intert data in order details
                OrderDetailsBean orderDetailsBean = new OrderDetailsBean();
                    ProductBean productBean = new ProductBean();
                    productBean.setProductID(productId);
                orderDetailsBean.setProductBean(productBean);
                orderDetailsBean.setQuantity(quantity);
                orderDetailsBean.setPrice(RP);
                orderDetailsBean.setCreatedBy(createdBy);
                orderDetailsBean.setCreatedDate(currentDate);
                    OrderBean orderBean1 = new OrderBean();
                    orderBean1.setOrderId(orderId);
                orderDetailsBean.setOrderBean(orderBean1);
                
                OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAOImpl();
                
                if(orderDetailsDAO.checkIfProductExists(orderDetailsBean)){
                    if(orderDetailsDAO.updateIfProductExists(orderDetailsBean)!=0){
                        try {
                            txtProduct.setText("");
                            txtQuantity1.setText("");
                            txtQuantity1.transferFocusBackward();
                            setOrderTable();
                            setProductTable();
                        } catch (SQLException ex) {
                            Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else{
                        JOptionPane.showMessageDialog(this, "Failed");
                    }
                }else{
                    if(orderDetailsDAO.addOrderDetails(orderDetailsBean)!=0){
                        try {
                            txtProduct.setText("");
                            txtQuantity1.setText("");
                            txtQuantity1.transferFocusBackward();
                            setOrderTable();
                            setProductTable();
                        } catch (SQLException ex) {
                            Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else{
                        JOptionPane.showMessageDialog(this, "Failed");
                    }
                } 
            } else {
                JOptionPane.showMessageDialog(this, "You are out of stock!");
            }
            setTotalGTotal();
            total = Float.parseFloat(lblGTotal.getText().replace("Rs. ", ""));
            //partial or full paid start
            float getAmmount = 0;
            String checkRe = txtPaid.getText();
            if (checkRe.equals("")) {
                getAmmount = 0;
            } else {
                getAmmount = Float.parseFloat(txtPaid.getText());
            }
            //partial or full paid end
            orderBean.setPaidPrice(getAmmount);
            orderBean.setTotalPrice(total);        
            orderDAO.updateOrderDetails(orderBean);
            //end data for order details table
        }
    }//GEN-LAST:event_txtQuantity1KeyReleased

    private void comboDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboDayActionPerformed
        // TODO add your handling code here:
        setTax();
    }//GEN-LAST:event_comboDayActionPerformed

    private void comboMonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboMonActionPerformed
        // TODO add your handling code here:
        setTax();
    }//GEN-LAST:event_comboMonActionPerformed

    private void comboYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboYearActionPerformed
        // TODO add your handling code here:
        setTax();
    }//GEN-LAST:event_comboYearActionPerformed

    private void txtPaidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPaidKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            lblReturn.setText("");
            String val = lblGTotal.getText().replace("Rs. ", "");

            float getAmmount = 0;
            String checkRe = txtPaid.getText();
            if (checkRe.equals("")) {
                getAmmount = 0;
            } else {
                getAmmount = Float.parseFloat(txtPaid.getText());
            }
            OrderDAO orderDAO = new OrderDAOImpl();
            OrderBean orderBean = new OrderBean();
            //partial or full paid end
            orderBean.setOrderNumber(lblOrderNo.getText());
            orderBean.setTotalPrice(Float.parseFloat(val));
            orderBean.setPaidPrice(getAmmount);
            if(orderDAO.updateOrderDetails(orderBean)!=0){
                //JOptionPane.showMessageDialog(this, "Done");
            }else{
                //JOptionPane.showMessageDialog(this, "Failed");
            }
            
            
            float returned = Float.parseFloat(val) - getAmmount;
            if (Float.parseFloat(val) <= getAmmount) {
                lblMsgForReturn.setText("Returned");
                lblReturn.setText("Rs. " + Math.abs(returned));
            } else {
                lblMsgForReturn.setText("Remaining");
                lblReturn.setText("Rs. " + Math.abs(returned));
            }
        }
    }//GEN-LAST:event_txtPaidKeyReleased

    private void txtQuantity1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantity1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantity1ActionPerformed

    private void txtOrderNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOrderNoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOrderNoKeyReleased

    
    
    private void btnNewOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewOrderActionPerformed
        onNewOrder();
    }//GEN-LAST:event_btnNewOrderActionPerformed

    private void btnNewOrder1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewOrder1ActionPerformed
        try {
            // TODO add your handling code here:
            HotelSales getAllOrder = new HotelSales();
            getAllOrder.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
    }//GEN-LAST:event_btnNewOrder1ActionPerformed

    private void onNewOrder(){
         setBarCode();
        txtPaid.setText("");
        OrderDetailsDAO orderDetailsDAO = new OrderDetailsDAOImpl();
        String order = lblOrderNo.getText();
        try {
            tableOrder.setModel(buildTableModel(orderDetailsDAO.viewAllOrderDetailsBeanResultSet(order)));
        } catch (SQLException ex) {
            Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        setTotalGTotal();
    }
 
    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        //start data for order table

        //for tax_id
        String day = comboDay.getSelectedItem().toString();
        String mon = String.valueOf(comboMon.getSelectedIndex() + 1);
        String year = comboYear.getSelectedItem().toString();
        if (Integer.parseInt(mon) <= 9) {
            mon = "0" + mon;
        }
        String date = year + "-" + mon + "-" + day;

        int taxId=0;
        TaxDAO taxDAO = new TaxDAOImpl();
        TaxBean tb = taxDAO.checkDatesForTax(date);
        if(tb==null){
            lblTax.setText("0.0%");
            lblTax2.setText("0.0%");
        }else{
            taxId = tb.getTaxId();
            lblTax.setText(tb.getTax() + "%");
            lblTax2.setText(tb.getTax() + "%");
        }

        //for order_no
        String serial = lblOrderNo.getText();

        //for customer_id
        String cust = comboCustomer.getSelectedItem().toString();
        AccountDAO accountDAO = new AccountDAOImpl();
        AccountBean ab = new AccountBean();
        ab.setAccountName(cust);

        int customerId = accountDAO.getIdByAccount(ab);

        float total = Float.parseFloat(lblGTotal.getText().replace("Rs. ", ""));
        float paid = Float.parseFloat(txtPaid.getText().replace("Rs. ", ""));

        //System.out.println("Order table-->" + taxId + "\t" + serial + "\t" + customerId+"\t"+total);

        OrderBean orderBean = new OrderBean();
        TaxBean taxBean = new TaxBean();
        taxBean.setTaxId(taxId);
        orderBean.setTaxBean(taxBean);
        orderBean.setOrderNumber(serial);
        AccountBean accountBean = new AccountBean();
        accountBean.setAccountId(customerId);
        orderBean.setAccountBean(accountBean);
        orderBean.setDate(date);
        orderBean.setTotalPrice(total);
        orderBean.setPaidPrice(paid);

        OrderDAO orderDAO = new OrderDAOImpl();
        if(orderDAO.updateOrderDetails2(orderBean)!=0){
            JOptionPane.showMessageDialog(this, "Updated!");
            onNewOrder();
        }else{
            JOptionPane.showMessageDialog(this, "Failed to update!");
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnUpdate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdate1ActionPerformed
        // TODO add your handling code here:
        //report();
    }//GEN-LAST:event_btnUpdate1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OrderFrame.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete1;
    private javax.swing.JButton btnNewOrder;
    private javax.swing.JButton btnNewOrder1;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnUpdate1;
    private javax.swing.JComboBox comboCustomer;
    private javax.swing.JComboBox comboDay;
    private javax.swing.JComboBox comboMon;
    private javax.swing.JComboBox comboYear;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblGTotal;
    private javax.swing.JLabel lblMsgForReturn;
    private javax.swing.JLabel lblOrderNo;
    private javax.swing.JLabel lblRP;
    private javax.swing.JLabel lblReturn;
    private javax.swing.JLabel lblTax;
    private javax.swing.JLabel lblTax2;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tableOrder;
    private javax.swing.JTable tableProduct;
    private javax.swing.JTextField txtOrderNo;
    private javax.swing.JTextField txtPaid;
    private javax.swing.JTextField txtProduct;
    private javax.swing.JTextField txtQuantity1;
    // End of variables declaration//GEN-END:variables

    private static class setVisible {

        public setVisible(boolean b) {
        }
    }
}
