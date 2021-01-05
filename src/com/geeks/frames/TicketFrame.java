/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.frames;

import com.geeks.beans.PackageBean;
import com.geeks.beans.TicketBean;
import com.geeks.beans.TicketDetailsBean;
import com.geeks.dao.PackageDAO;
import com.geeks.dao.TicketDAO;
import com.geeks.dao.TicketDetailsDAO;
import com.geeks.daoImp.PackageDAOImpl;
import com.geeks.daoImp.TicketDAOImpl;
import com.geeks.daoImp.TicketDetailsDAOImpl;
import com.geeks.daoImp.UserDAOImp;
import com.geeks.utils.BarCodeGenerator;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author xyz
 */
public class TicketFrame extends javax.swing.JFrame {

    String serial="",paid="";
    private void setOrderTxtRetrunTxt(){
        serial = GetAllTickets.ticket;
        paid = GetAllTickets.paid;
        txtTicketNo.setText(serial);
        txtPaid.setText(paid);
    }
    public TicketFrame() throws SQLException {
        initComponents();
        setBarCode();
        setPackageTable();
        setTicketTable();
        setDate();
        setOrderTxtRetrunTxt();
        setTotalGTotal();
        setTicketDetailsTable();
    }
    
    private void setTotalGTotal() {
        //sum start
        lblTotal.setText("");
        txtPaid.setText(paid);
        //lblReturn.setText("");
        int len = tableTicket.getRowCount() - 1;
        float total = 0;
        while (len >= 0) {
            BigDecimal val = (BigDecimal) tableTicket.getValueAt(len, 4);
            float val2 = val.floatValue();
            total = total + val2;
            len--;
        }
        lblTotal.setText("Rs. " + total);
        float getAmmount = 0;
        String checkRe = txtPaid.getText();
        if (checkRe.equals("")) {
            getAmmount = 0;
        } else {
            getAmmount = Float.parseFloat(txtPaid.getText());
        }
        
        float returned = total - getAmmount;
        if (total <= getAmmount) {
            lblMsgForReturn.setText("Returned");
            lblReturn.setText("Rs. " + Math.abs(returned));
        } else {
            lblMsgForReturn.setText("Remaining");
            lblReturn.setText("Rs. " + Math.abs(returned));
        }
        //sum end
    }

    private void setAllTxtsForReturn() {
        int id = (int) tableTicket.getValueAt(tableTicket.getSelectedRow(), 0);
        TicketDetailsBean tdb = new TicketDetailsBean();
        tdb.setTicketDetialsId(id);

        TicketDetailsDAO ticketDetailsDAO = new TicketDetailsDAOImpl();
        TicketDetailsBean tdb1 = ticketDetailsDAO.setAllTxtForReturn(tdb);
        txtPackage.setText(tdb1.getPackageBean().getPackageName());
        txtQuantity.setText(String.valueOf(tdb1.getQuantity()));
        lblPrice.setText("Rs. "+tdb1.getPackageBean().getPackagePrice());
    }

    private void setBarCode() {
        String bar = BarCodeGenerator.generator();
        TicketDAO ticketDAO = new TicketDAOImpl();
        if (ticketDAO.checkBarCode().contains(bar)) {
            setBarCode();
        } else {
            lblTicketNo.setText(bar);
        }
    }

    private void setDate() {
        Date d = new Date();
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        String m[] = f.format(d).split("-");
        //System.out.println(f.format(d));
        comboMon.setSelectedIndex(Integer.parseInt(m[1])-1);
        comboDay.setSelectedItem(m[0]);
        comboYear.setSelectedItem(m[2]);
    }

    public void setPackageTable() throws SQLException {
        PackageDAO productDAO = new PackageDAOImpl();
        //tablePackage.setModel(buildTableModel(productDAO.viewAllPackageResultSet()));
        
        String name = txtPackage.getText();
        PackageBean pb = new PackageBean();
        pb.setPackageName(name);
        tablePackage.setModel(buildTableModel(productDAO.viewAllPackageResultSet(pb)));
    }

    public void setTicketTable() throws SQLException {
        TicketDetailsDAO ticketDetailsDAO = new TicketDetailsDAOImpl();
        String order = lblTicketNo.getText();
        tableTicket.setModel(buildTableModel(ticketDetailsDAO.viewAllTicketDetailsBeanResultSet(order)));
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
    
     private void setTicketDetailsTable(){
        TicketDetailsDAO orderDetailsDAO = new TicketDetailsDAOImpl();
        String order = txtTicketNo.getText();
        lblTicketNo.setText(order);
        txtPaid.setText(paid);
        txtContact.setText(GetAllTickets.contact);
        if(GetAllTickets.date==null){
            setDate();
        }else{
            String date[] = String.valueOf(GetAllTickets.date).split("-");
            comboDay.setSelectedItem(date[2]);
            comboMon.setSelectedIndex(Integer.parseInt(date[1]) - 1);
            comboYear.setSelectedItem(date[0]);
        }
        if(order.equals("")){
            setBarCode();
        }
        try {
            tableTicket.setModel(buildTableModel(orderDetailsDAO.viewAllTicketDetailsBeanResultSet(order)));
            if(tableTicket.getRowCount()>0){
                btnUpdate.setEnabled(true);
            }else{
                btnUpdate.setEnabled(false);
            }
            setTotalGTotal();
        } catch (SQLException ex) {
            Logger.getLogger(TicketFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        txtContact = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        lblPrice = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtPaid = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableTicket = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        comboDay = new javax.swing.JComboBox();
        comboMon = new javax.swing.JComboBox();
        comboYear = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablePackage = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        lblTicketNo = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        lblMsgForReturn = new javax.swing.JLabel();
        lblReturn = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtPackage = new javax.swing.JTextField();
        btnUpdate = new javax.swing.JButton();
        btnNewOrder = new javax.swing.JButton();
        btnNewOrder1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtTicketNo = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtContact.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jPanel2.add(txtContact, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, 280, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel1.setText("Contact");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 170, 40));

        lblPrice.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        lblPrice.setText("Rs.");
        jPanel2.add(lblPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 160, 280, 40));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel5.setText("Quantity");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 180, 40));

        txtPaid.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtPaid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPaidActionPerformed(evt);
            }
        });
        txtPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPaidKeyReleased(evt);
            }
        });
        jPanel2.add(txtPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(1430, 550, 210, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel7.setText("Ticket Price");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 160, 190, 40));

        tableTicket.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tableTicket.setModel(new javax.swing.table.DefaultTableModel(
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
        tableTicket.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableTicketMouseClicked(evt);
            }
        });
        tableTicket.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableTicketKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tableTicket);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 20, 570, 370));

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
        jPanel2.add(comboDay, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 20, 70, 40));

        comboMon.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        comboMon.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" }));
        jPanel2.add(comboMon, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 20, 90, 40));

        comboYear.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        comboYear.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2019", "2020", "2021", "2022", "2023" }));
        jPanel2.add(comboYear, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 20, 100, 40));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel8.setText("Ticket date");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 20, 190, 40));

        tablePackage.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tablePackage.setModel(new javax.swing.table.DefaultTableModel(
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
        tablePackage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePackageMouseClicked(evt);
            }
        });
        tablePackage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tablePackageKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tablePackage);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 1070, 350));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel6.setText("Ticket no.");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 170, 40));

        lblTicketNo.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        lblTicketNo.setText("abc-123");
        jPanel2.add(lblTicketNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, 230, 40));

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jButton5.setText("Remove All");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1470, 410, 210, -1));

        jButton7.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jButton7.setText("Remove");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 410, 210, -1));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel12.setText("Total");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 500, 190, 40));

        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblTotal.setText("Rs.");
        jPanel2.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1430, 500, 210, 40));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel14.setText("Paid");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 550, 190, 40));

        txtQuantity.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        txtQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantityActionPerformed(evt);
            }
        });
        txtQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQuantityKeyReleased(evt);
            }
        });
        jPanel2.add(txtQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 230, 280, -1));

        lblMsgForReturn.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblMsgForReturn.setText("Returned");
        jPanel2.add(lblMsgForReturn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 600, 190, 40));

        lblReturn.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblReturn.setText("Rs.");
        jPanel2.add(lblReturn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1430, 600, 210, 40));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel9.setText("Package/Age");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 220, 40));

        txtPackage.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        txtPackage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPackageActionPerformed(evt);
            }
        });
        txtPackage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPackageKeyReleased(evt);
            }
        });
        jPanel2.add(txtPackage, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 160, 280, -1));

        btnUpdate.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.setEnabled(false);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel2.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 80, 120, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1700, 740));

        btnNewOrder.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        btnNewOrder.setText("New Ticket");
        btnNewOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewOrderActionPerformed(evt);
            }
        });
        jPanel1.add(btnNewOrder, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        btnNewOrder1.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        btnNewOrder1.setText("Get All Tickets");
        btnNewOrder1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewOrder1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnNewOrder1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 30, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel4.setText("Ticket Panel");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 30, -1, 40));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel13.setText("Ticket No.");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 40, 190, 40));

        txtTicketNo.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        txtTicketNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTicketNoKeyReleased(evt);
            }
        });
        jPanel1.add(txtTicketNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(1340, 40, 270, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1721, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPaidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPaidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPaidActionPerformed

    private void updateTicketTable(int num){
        String day = comboDay.getSelectedItem().toString();
        String mon = String.valueOf(comboMon.getSelectedIndex() + 1);
        String year = comboYear.getSelectedItem().toString();
        if (Integer.parseInt(mon) <= 9) {
            mon = "0" + mon;
        }
        String date = year + "-" + mon + "-" + day;

        int createdBy = UserDAOImp.userId;

        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("yyyy-MM-dd");

        String currentDate = df.format(d);

        float total = Float.parseFloat(lblTotal.getText().replace("Rs. ", ""));
        float paid = 0;
        String p = txtPaid.getText();
        if(p.equals("")){
            paid = 0;
        }else{
            paid = Float.parseFloat(txtPaid.getText());
        }

        TicketBean ticketBean = new TicketBean();
        ticketBean.setBarcode(lblTicketNo.getText());
        ticketBean.setContact(txtContact.getText());
        ticketBean.setDate(date);
        ticketBean.setTotal(total);
        ticketBean.setPaid(paid);
        ticketBean.setCreatedBy(createdBy);
        ticketBean.setCreatedDate(currentDate);
        
        TicketDAO ticketDAO = new TicketDAOImpl();
        if(num==1){
            ticketDAO.updateTicketDetails(ticketBean);
        }else{
            if(ticketDAO.updateTicketDetails(ticketBean)!=0){
                JOptionPane.showMessageDialog(this, "Updated!");
                txtContact.setText("");
                txtPackage.setText("");
                txtQuantity.setText("");
                lblPrice.setText("Rs. ");
                setDate();
            }else{
                JOptionPane.showMessageDialog(this, "Failed to update!");
            }
        }
    }
    
    private void txtPaidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPaidKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            lblReturn.setText("");
            String val = lblTotal.getText().replace("Rs. ", "");

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
            //new
            updateTicketTable(2);
            onNewTicket();
        }
    }//GEN-LAST:event_txtPaidKeyReleased

    private void onNewTicket(){
        txtTicketNo.setText("");
        paid = "";
        setDate();
        txtContact.setText("");
        setBarCode();
        TicketDetailsDAO ticketDetailsDAO = new TicketDetailsDAOImpl();
        String ticket = lblTicketNo.getText();
        try {
            tableTicket.setModel(buildTableModel(ticketDetailsDAO.viewAllTicketDetailsBeanResultSet(ticket)));
        } catch (SQLException ex) {
            Logger.getLogger(TicketFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        setTotalGTotal();
    }
    
    private void tableTicketMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableTicketMouseClicked
        // TODO add your handling code here:
        setAllTxtsForReturn();
    }//GEN-LAST:event_tableTicketMouseClicked

    private void tableTicketKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableTicketKeyReleased
        // TODO add your handling code here:
        setAllTxtsForReturn();
    }//GEN-LAST:event_tableTicketKeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void onSelectPackage(){
        int id = (int) tablePackage.getValueAt(tablePackage.getSelectedRow(), 0);
        PackageDAO productDAO = new PackageDAOImpl();
        PackageBean pb = new PackageBean();
        pb.setPackageId(id);
        PackageBean pb1 = productDAO.setAllTxt(pb);
        txtPackage.setText(pb1.getPackageName());
        lblPrice.setText("Rs. "+pb1.getPackagePrice());
    }
    private void tablePackageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePackageMouseClicked
        // TODO add your handling code here:
        onSelectPackage();
    }//GEN-LAST:event_tablePackageMouseClicked

    private void tablePackageKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablePackageKeyReleased
        // TODO add your handling code here:
        onSelectPackage();
    }//GEN-LAST:event_tablePackageKeyReleased

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        int len = tableTicket.getRowCount() - 1;
        while (len >= 0) {
            int id = (int) tableTicket.getValueAt(len, 0);
            TicketDetailsBean tdb = new TicketDetailsBean();
            tdb.setTicketDetialsId(id);
            TicketDetailsDAO ticketDetailsDAO = new TicketDetailsDAOImpl();
            ticketDetailsDAO.deleteIfReturns(tdb);
            try {
                setTicketTable();
            } catch (SQLException ex) {
                Logger.getLogger(TicketFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            setTotalGTotal();
            updateTicketTable(1);
            len--;
        }
        float total = Float.parseFloat(lblTotal.getText().replace("Rs. ", ""));
        if(total==0.0){
            TicketBean ticketBean = new TicketBean();
            ticketBean.setBarcode(lblTicketNo.getText());
            TicketDAO ticketDAO = new TicketDAOImpl();
            int r = ticketDAO.deleteTicketDetails(ticketBean);
            if(r!=0){
                JOptionPane.showMessageDialog(this, "Ticket deleted!");
                onNewTicket();
            }else{
                JOptionPane.showMessageDialog(this, "Failed!");
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        int id = (int) tableTicket.getValueAt(tableTicket.getSelectedRow(), 0);
        TicketDetailsBean tdb = new TicketDetailsBean();
        tdb.setTicketDetialsId(id);
        TicketDetailsDAO ticketDetailsDAO = new TicketDetailsDAOImpl();
        ticketDetailsDAO.deleteIfReturns(tdb);
        try {
            setTicketTable();
        } catch (SQLException ex) {
            Logger.getLogger(TicketFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        setTotalGTotal();
        updateTicketTable(2);

        float total = Float.parseFloat(lblTotal.getText().replace("Rs. ", ""));
        if(total==0.0){
            TicketBean ticketBean = new TicketBean();
            ticketBean.setBarcode(lblTicketNo.getText());
            TicketDAO ticketDAO = new TicketDAOImpl();
            int r = ticketDAO.deleteTicketDetails(ticketBean);
            if(r!=0){
                JOptionPane.showMessageDialog(this, "Order deleted!");
                onNewTicket();
            }else{
                JOptionPane.showMessageDialog(this, "Failed!");
            }
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void txtQuantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantityActionPerformed

    int ticketId = 0;
    private void txtQuantityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQuantityKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //txtProduct.transferFocus();
            //start data for ticket table

            //for ticket number
            String serial = lblTicketNo.getText();

            //For Contact
            String contact = txtContact.getText();

            //for date
            String day = comboDay.getSelectedItem().toString();
            String mon = String.valueOf(comboMon.getSelectedIndex() + 1);
            String year = comboYear.getSelectedItem().toString();
            if (Integer.parseInt(mon) <= 9) {
                mon = "0" + mon;
            }
            String date = year + "-" + mon + "-" + day;

            int createdBy = UserDAOImp.userId;

            Date d = new Date();
            SimpleDateFormat df = new SimpleDateFormat();
            df.applyPattern("yyyy-MM-dd");

            String currentDate = df.format(d);

            float total = Float.parseFloat(lblTotal.getText().replace("Rs. ", ""));
            float paid = 0;
            String p = txtPaid.getText();
            if(p.equals("")){
                paid = 0;
            }else{
                paid = Float.parseFloat(txtPaid.getText());
            }

            //System.out.println("Ticket table-->" + serial + "\t" + contact + "\t" + date +"\t"+createdBy+"\t"+currentDate);

            TicketBean ticketBean = new TicketBean();
            ticketBean.setBarcode(serial);
            ticketBean.setContact(contact);
            ticketBean.setDate(date);
            ticketBean.setTotal(total);
            ticketBean.setPaid(paid);
            ticketBean.setCreatedBy(createdBy);
            ticketBean.setCreatedDate(currentDate);

            TicketDAO ticketDAO = new TicketDAOImpl();
            if(ticketDAO.getTicketDetails(ticketBean)==null){
                if(ticketDAO.addTicketDetails(ticketBean)!=0){
                    TicketBean tb1 = ticketDAO.getTicketDetails(ticketBean);
                    ticketId = tb1.getTicketId();
                }
            }else{
                TicketBean tb1 = ticketDAO.getTicketDetails(ticketBean);
                ticketId = tb1.getTicketId();
            }

            //end data for ticket table
            //start data for ticket details table

            //for package id
            int pkgId=0;
            if(tablePackage.isRowSelected(tablePackage.getSelectedRow())){
                pkgId = (int) tablePackage.getValueAt(tablePackage.getSelectedRow(), 0);
            }else if(tableTicket.isRowSelected(tableTicket.getSelectedRow())){
                pkgId = (int) tableTicket.getValueAt(tableTicket.getSelectedRow(), 6);
            }else{
                pkgId = (int) tablePackage.getValueAt(0, 0);
            }

            //for quantity
            String q = txtQuantity.getText();
            int quantity = 0;
            if(q.equals("")){
                quantity = 0;
            }else{
                quantity = Integer.parseInt(txtQuantity.getText());
            }

            //System.out.println("Ticket Details table-->" + pkgId+"\t"+quantity+"\t"+ticketId);

            //intert data in ticket details
            TicketDetailsBean ticketDetailsBean = new TicketDetailsBean();
            PackageBean packageBean = new PackageBean();
            packageBean.setPackageId(pkgId);
            ticketDetailsBean.setPackageBean(packageBean);
            TicketBean tb = new TicketBean();
            tb.setTicketId(ticketId);
            ticketDetailsBean.setTicketBean(tb);
            ticketDetailsBean.setQuantity(quantity);

            TicketDetailsDAO ticketDetailsDAO = new TicketDetailsDAOImpl();

            if(ticketDetailsDAO.checkIfPackageExists(ticketDetailsBean)){
                if(ticketDetailsDAO.updateIfPackageExists(ticketDetailsBean)!=0){
                    try {
                        txtPackage.setText("");
                        txtQuantity.setText("");
                        lblPrice.setText("Rs. 0.0");
                        txtQuantity.transferFocusBackward();
                        setTicketTable();
                        setPackageTable();
                    } catch (SQLException ex) {
                        Logger.getLogger(TicketFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    JOptionPane.showMessageDialog(this, "Failed");
                }
            }else{
                if(ticketDetailsDAO.addTicketDetails(ticketDetailsBean)!=0){
                    try {
                        txtPackage.setText("");
                        txtQuantity.setText("");
                        lblPrice.setText("Rs. 0.0");
                        txtQuantity.transferFocusBackward();
                        setTicketTable();
                        setPackageTable();
                    } catch (SQLException ex) {
                        Logger.getLogger(TicketFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    JOptionPane.showMessageDialog(this, "Failed");
                }
            }
            setTotalGTotal();
            total = Float.parseFloat(lblTotal.getText().replace("Rs. ", ""));

            float getAmmount = 0;
            String checkRe = txtPaid.getText();
            if (checkRe.equals("")) {
                getAmmount = 0;
            } else {
                getAmmount = Float.parseFloat(txtPaid.getText());
            }

            ticketBean.setPaid(getAmmount);
            ticketBean.setTotal(total);
            ticketDAO.updateTicketDetails(ticketBean);
        }
    }//GEN-LAST:event_txtQuantityKeyReleased

    private void txtPackageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPackageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPackageActionPerformed

    private void txtPackageKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPackageKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int id = (int) tablePackage.getValueAt(0, 0);
            PackageDAO productDAO = new PackageDAOImpl();
            PackageBean pb = new PackageBean();
            pb.setPackageId(id);
            PackageBean pb1 = productDAO.setAllTxt(pb);
            txtPackage.setText(pb1.getPackageName());
            lblPrice.setText("Rs. "+pb1.getPackagePrice());
            txtPackage.transferFocus();
        }
        try {
            // TODO add your handling code here:
            setPackageTable();
        } catch (SQLException ex) {
            Logger.getLogger(OrderFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtPackageKeyReleased

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        updateTicketTable(2);
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked

    }//GEN-LAST:event_jPanel2MouseClicked

    private void btnNewOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewOrderActionPerformed
        // TODO add your handling code here:
        onNewTicket();
    }//GEN-LAST:event_btnNewOrderActionPerformed

    private void btnNewOrder1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewOrder1ActionPerformed
        try {
            // TODO add your handling code here:
            GetAllTickets getAllTickets = new GetAllTickets();
            getAllTickets.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(TicketFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
    }//GEN-LAST:event_btnNewOrder1ActionPerformed

    private void txtTicketNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTicketNoKeyReleased
        // TODO add your handling code here:
        setTicketDetailsTable();
    }//GEN-LAST:event_txtTicketNoKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TicketFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TicketFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TicketFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TicketFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new TicketFrame().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(TicketFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNewOrder;
    private javax.swing.JButton btnNewOrder1;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox comboDay;
    private javax.swing.JComboBox comboMon;
    private javax.swing.JComboBox comboYear;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JLabel lblMsgForReturn;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblReturn;
    private javax.swing.JLabel lblTicketNo;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tablePackage;
    private javax.swing.JTable tableTicket;
    private javax.swing.JTextField txtContact;
    private javax.swing.JTextField txtPackage;
    private javax.swing.JTextField txtPaid;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtTicketNo;
    // End of variables declaration//GEN-END:variables
}
