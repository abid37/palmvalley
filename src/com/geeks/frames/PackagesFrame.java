/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.frames;

import com.geeks.beans.PackageBean;
import com.geeks.dao.PackageDAO;
import com.geeks.daoImp.PackageDAOImpl;
import com.geeks.daoImp.UserDAOImp;
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
public class PackagesFrame extends javax.swing.JFrame {

    public PackagesFrame() throws SQLException {
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
        int id = (int) tablePackage.getValueAt(tablePackage.getSelectedRow(), 0);
        PackageBean packageBean = new PackageBean();
        packageBean.setPackageId(id);
        
        PackageDAO packageDAO = new PackageDAOImpl();
        PackageBean pb =  packageDAO.setAllTxt(packageBean);
        txtPackageName.setText(pb.getPackageName());
        txtNoOfPerson.setText(String.valueOf(pb.getPackageCount()));
        txtPrice.setText(String.valueOf(pb.getPackagePrice()));
        
    }
    private void clearAll() {
        txtPackageName.setText("");
        txtNoOfPerson.setText("");
        txtPrice.setText("");
    }
    
    public void setTable() throws SQLException {
        PackageDAO packageDAO = new PackageDAOImpl();
        PackageBean pb = new PackageBean();
        pb.setPackageName("");
        tablePackage.setModel(buildTableModel(packageDAO.viewAllPackageResultSet(pb)));
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
        String packageName = txtPackageName.getText();
        String p = txtPrice.getText();
        float price = 0;
        if(p.equals("")){
            price = 0;
        }else{
            price = Float.parseFloat(txtPrice.getText());
        }
        String c = txtNoOfPerson.getText();
        int count = 0;
        if(c.equals("")){
            count = 0;
        }else{
            count = Integer.parseInt(txtNoOfPerson.getText());
        }
        
        int createdBy = UserDAOImp.userId;
    
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("yyyy-MM-dd");

        String currentDate = df.format(d);

        PackageBean packageBean = new PackageBean();
        packageBean.setPackageName(packageName);
        packageBean.setPackagePrice(price);
        packageBean.setPackageCount(count);
           
        PackageDAO packageDAO = new PackageDAOImpl();
        if (one == 1) {
            if (packageDAO.checkPackage(packageBean)!=null) {
                JOptionPane.showMessageDialog(this, "Package already exits!");
            } else {
                packageBean.setCreatedBy(createdBy);
                packageBean.setCreatedDate(currentDate);
                
                if (packageDAO.addPackageDetails(packageBean) != 0) {
                    try {
                        clearAll();
                        setTable();
                        JOptionPane.showMessageDialog(this, "Package added successfully!");
                    } catch (SQLException ex) {
                        Logger.getLogger(PackagesFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add the package!");
                }
            }
        } else if(one==2) {
            int id = (int) tablePackage.getValueAt(tablePackage.getSelectedRow(), 0);
            
            packageBean.setModifiedBy(createdBy);
            packageBean.setModifiedDate(currentDate);
            packageBean.setPackageId(id);
            
            if (packageDAO.updatePackageDetails(packageBean) != 0) {
                try {
                    clearAll();
                    setTable();
                    setButtons(2);
                    JOptionPane.showMessageDialog(this, "Package updated successfully!");
                } catch (SQLException ex) {
                    Logger.getLogger(PackagesFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update the package!");
            }
        }else if(one==3){
                int id = (int) tablePackage.getValueAt(tablePackage.getSelectedRow(), 0);
                packageBean.setPackageId(id);
                if (packageDAO.deletePackageDetails(packageBean) != 0) {
                try {
                    clearAll();
                    setTable();
                    setButtons(2);
                    JOptionPane.showMessageDialog(this, "Package deleted successfully!");
                } catch (SQLException ex) {
                    Logger.getLogger(PackagesFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete the package!");
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
        txtPackageName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePackage = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtNoOfPerson = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtPackageName.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jPanel2.add(txtPackageName, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 20, 320, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel1.setText("Package name");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, 290, 40));

        btnUpdate.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel2.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 240, 150, -1));

        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel2.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 240, 150, -1));

        btnDelete.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel2.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 240, 150, -1));

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
        jScrollPane1.setViewportView(tablePackage);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 320, 1350, 290));

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jButton4.setText("< Back");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 660, 180, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel3.setText("No. of person");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 90, 290, 40));

        txtNoOfPerson.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jPanel2.add(txtNoOfPerson, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 90, 320, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel5.setText("Price");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 160, 290, 40));

        txtPrice.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jPanel2.add(txtPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 160, 320, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 1430, 740));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel4.setText("Package Panel");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 30, -1, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        addForOneUpdateForTwo(2);
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        addForOneUpdateForTwo(1);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        addForOneUpdateForTwo(3);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tablePackageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePackageMouseClicked
        // TODO add your handling code here:
        setAllTxts();
    }//GEN-LAST:event_tablePackageMouseClicked

    private void tablePackageKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablePackageKeyReleased
        // TODO add your handling code here:
        setAllTxts();
    }//GEN-LAST:event_tablePackageKeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        // TODO add your handling code here:
        setButtons(2);
    }//GEN-LAST:event_jPanel2MouseClicked

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
            java.util.logging.Logger.getLogger(PackagesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PackagesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PackagesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PackagesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new PackagesFrame().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(PackagesFrame.class.getName()).log(Level.SEVERE, null, ex);
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablePackage;
    private javax.swing.JTextField txtNoOfPerson;
    private javax.swing.JTextField txtPackageName;
    private javax.swing.JTextField txtPrice;
    // End of variables declaration//GEN-END:variables
}
