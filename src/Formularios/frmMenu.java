package Formularios;

import Clases.Sesion;
import Clases.Usuario;
import java.awt.Image;
import javax.swing.ImageIcon;

public class frmMenu extends javax.swing.JFrame {

    public frmMenu() {
        initComponents();
        this.setLocationRelativeTo(null);
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Imagenes/tacoSinFondo.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(
                lblLogo.getWidth(),
                lblLogo.getHeight(),
                Image.SCALE_SMOOTH
        );
        lblLogo.setIcon(new ImageIcon(scaledImage));
        aplicarPermisos();
    }

    private void aplicarPermisos() {
        Usuario u = Sesion.getUsuarioActual();
        String rol = u.getRol();

        switch (rol) {
            case "ADMINISTRADOR":
            case "SUPERVISOR":
                
                btnProductos.setEnabled(true);
                btnCombos.setEnabled(true);
                btnOrdenes.setEnabled(true);
                break;

            case "MESERO":
                
                btnProductos.setEnabled(false);
                btnCombos.setEnabled(false);
                btnOrdenes.setEnabled(true);
                break;

            case "COCINERO":
                
                btnProductos.setEnabled(false);
                btnCombos.setEnabled(false);
                btnOrdenes.setEnabled(false);
                break;

            default:
                
                btnProductos.setEnabled(false);
                btnCombos.setEnabled(false);
                btnOrdenes.setEnabled(false);
                break;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnProductos = new javax.swing.JButton();
        btnCombos = new javax.swing.JButton();
        btnOrdenes = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        lblLogo = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Menu");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/prodcutos.png"))); // NOI18N
        btnProductos.setText("Productos");
        btnProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductosActionPerformed(evt);
            }
        });
        getContentPane().add(btnProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(267, 52, -1, -1));

        btnCombos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/comboss.png"))); // NOI18N
        btnCombos.setText("Combos");
        btnCombos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCombosActionPerformed(evt);
            }
        });
        getContentPane().add(btnCombos, new org.netbeans.lib.awtextra.AbsoluteConstraints(453, 52, 168, -1));

        btnOrdenes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/orden.png"))); // NOI18N
        btnOrdenes.setText("Ordenes");
        btnOrdenes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrdenesActionPerformed(evt);
            }
        });
        getContentPane().add(btnOrdenes, new org.netbeans.lib.awtextra.AbsoluteConstraints(267, 169, 147, -1));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/cerrarsesion.png"))); // NOI18N
        jButton4.setText("Cerrar Sesion");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(453, 169, -1, -1));

        lblLogo.setText("jLabel1");
        getContentPane().add(lblLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 40, 206, 193));

        jPanel1.setBackground(new java.awt.Color(255, 255, 51));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 680, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 280, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 680, 280));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductosActionPerformed
        new frmProducto().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnProductosActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        dispose();
        new frmLogin().setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnOrdenesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrdenesActionPerformed
        new frmOrden().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnOrdenesActionPerformed

    private void btnCombosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCombosActionPerformed
        new frmCombo().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCombosActionPerformed

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
            java.util.logging.Logger.getLogger(frmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCombos;
    private javax.swing.JButton btnOrdenes;
    private javax.swing.JButton btnProductos;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblLogo;
    // End of variables declaration//GEN-END:variables
}
