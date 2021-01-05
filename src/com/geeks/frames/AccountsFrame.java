/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.frames;

import com.geeks.beans.AccountBean;
import com.geeks.beans.CategoryBean;
import com.geeks.dao.AccountDAO;
import com.geeks.dao.CategoryDAO;
import com.geeks.daoImp.AccountDAOImpl;
import com.geeks.daoImp.CategoryDAOImpl;
import com.geeks.daoImp.UserDAOImp;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class AccountsFrame extends javax.swing.JFrame {

    /**
     * Creates new form UserDAO
     */
    public AccountsFrame() throws SQLException {
        initComponents();
        setTable();
        setButtons(2);
    }
    
    private void setButtons(int val){
        if(val==1){
            btnAdd.setEnabled(false);
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
        }else{
            btnAdd.setEnabled(true);
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
        }
    }
    
    public void setAllTxts() {
        setButtons(1);
        int id = (int) tableAccountsFrame.getValueAt(tableAccountsFrame.getSelectedRow(), 0);
       
        AccountBean accountBean = new AccountBean();
        accountBean.setAccountId(id);
        
        AccountDAO accountDAO = new AccountDAOImpl();
        ArrayList<AccountBean> list = (ArrayList<AccountBean>) accountDAO.setAllTxt(accountBean);
        for (AccountBean accountBean1 : list) {
            txtName.setText(accountBean1.getAccountName());
            txtContact.setText(accountBean1.getAccountConnect());
            txtAreaDescription.setText(accountBean1.getDescription());
        }
    }
    
    private void clearAll() {
        txtName.setText("");
        txtContact.setText("");
        txtAreaDescription.setText("");
    }
    
    public void setTable() throws SQLException {
        AccountDAO accountDAO = new AccountDAOImpl();
        tableAccountsFrame.setModel(buildTableModel(accountDAO.viewAllAccountResultSet()));
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
    
    public void addForOneUpdateForTwo(int one) {
        String name = txtName.getText();
        String contact = txtContact.getText();
        String description = txtAreaDescription.getText();

        int createdBy = UserDAOImp.userId;
    
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("yyyy-MM-dd");

        String currentDate = df.format(d);

        AccountBean accountBean = new AccountBean();
        accountBean.setAccountName(name);
        accountBean.setAccountConnect(contact);
        accountBean.setDescription(description);
           
        AccountDAO accountDAO = new AccountDAOImpl();
        if (one == 1) {
//            if (accountDAO.checkCategory(accountBean)) {
//                JOptionPane.showMessageDialog(this, "Category already exits!");
//            } else {
                accountBean.setCreatedBy(createdBy);
                accountBean.setCreatedDate(currentDate);
                
                if (accountDAO.addAccountDetails(accountBean) != 0) {
                    try {
                        clearAll();
                        setTable();
                        JOptionPane.showMessageDialog(this, "Account added successfully!");
                    } catch (SQLException ex) {
                        Logger.getLogger(AccountsFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add the account!");
                }
//            }
        } else if(one==2) {
            int id = (int) tableAccountsFrame.getValueAt(tableAccountsFrame.getSelectedRow(), 0);
            
            accountBean.setModifiedBy(createdBy);
            accountBean.setModifiedDate(currentDate);
            accountBean.setAccountId(id);
            
            if (accountDAO.updateAccountDetails(accountBean) != 0) {
                try {
                    clearAll();
                    setTable();
                    setButtons(2);
                    JOptionPane.showMessageDialog(this, "Account updated successfully!");
                } catch (SQLException ex) {
                    Logger.getLogger(AccountsFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update the account!");
            }
        }else if(one==3){
                int id = (int) tableAccountsFrame.getValueAt(tableAccountsFrame.getSelectedRow(), 0);
                accountBean.setAccountId(id);
                if (accountDAO.deleteAccountDetails(accountBean) != 0) {
                try {
                    clearAll();
                    setTable();
                    setButtons(2);
                    JOptionPane.showMessageDialog(this, "Account deleted successfully!");
                } catch (SQLException ex) {
                    Logger.getLogger(AccountsFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete the account!");
            }
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
        txtName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtContact = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableAccountsFrame = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaDescription = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtName.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jPanel2.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 140, 310, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel1.setText("Name");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 140, 220, 40));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel3.setText("Contact");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 210, 230, 40));

        txtContact.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jPanel2.add(txtContact, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 210, 310, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel2.setText("Description");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 280, 220, 40));

        btnUpdate.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel2.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 530, 150, -1));

        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel2.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 530, 150, -1));

        btnDelete.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel2.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(1440, 530, 150, -1));

        tableAccountsFrame.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tableAccountsFrame.setModel(new javax.swing.table.DefaultTableModel(
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
        tableAccountsFrame.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableAccountsFrameMouseClicked(evt);
            }
        });
        tableAccountsFrame.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableAccountsFrameKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tableAccountsFrame);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 940, 440));

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jButton4.setText("< Back");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 660, 180, -1));

        txtAreaDescription.setColumns(20);
        txtAreaDescription.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        txtAreaDescription.setRows(5);
        jScrollPane2.setViewportView(txtAreaDescription);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 280, 310, 160));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1600, 740));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel4.setText("Account Panel");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 30, -1, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1623, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        addForOneUpdateForTwo(1);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        addForOneUpdateForTwo(2);
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        addForOneUpdateForTwo(3);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tableAccountsFrameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableAccountsFrameMouseClicked
        // TODO add your handling code here:
        setAllTxts();
    }//GEN-LAST:event_tableAccountsFrameMouseClicked

    private void tableAccountsFrameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableAccountsFrameKeyReleased
        // TODO add your handling code here:
        setAllTxts();
    }//GEN-LAST:event_tableAccountsFrameKeyReleased

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        // TODO add your handling code here:
        setButtons(2);
    }//GEN-LAST:event_jPanel2MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new AccountsFrame().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(AccountsFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tableAccountsFrame;
    private javax.swing.JTextArea txtAreaDescription;
    private javax.swing.JTextField txtContact;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
