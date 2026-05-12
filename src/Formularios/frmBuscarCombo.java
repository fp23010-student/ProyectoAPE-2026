package Formularios;

import Clases.Combo;
import Clases.ComboDAO;
import javax.swing.table.DefaultTableModel;

public class frmBuscarCombo extends javax.swing.JFrame {

    private DefaultTableModel modeloDetalle;
    private frmCombo formularioPrincipal;

    public frmBuscarCombo(frmCombo principal) {
        initComponents();
        configurarTabla();
        this.formularioPrincipal = principal;
        this.setLocationRelativeTo(null);
        mostrarCombos("");
    }

    private void mostrarCombos(String nombre) {
        ComboDAO dao = new ComboDAO();
        java.util.List<Combo> lista = dao.listarCombos(nombre); 

        modeloDetalle.setRowCount(0);
        for (Combo c : lista) {
            modeloDetalle.addRow(new Object[]{
                c.getIdCombo(),
                c.getCombo(),
                "$" + c.getPrecioCombo()
            });
        }
    }

    private void configurarTabla() {
        modeloDetalle = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Subtotal"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMostrarCombos.setModel(modeloDetalle);

        tblMostrarCombos.getColumnModel().getColumn(0).setPreferredWidth(5); 
        tblMostrarCombos.getColumnModel().getColumn(1).setPreferredWidth(250); 
        tblMostrarCombos.getColumnModel().getColumn(2).setPreferredWidth(1);   

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblMostrarCombos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtBuscarCombo = new javax.swing.JTextField();
        btnCerrar = new javax.swing.JButton();
        pnlFondo = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Combos");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblMostrarCombos.setModel(new javax.swing.table.DefaultTableModel(
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
        tblMostrarCombos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMostrarCombosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblMostrarCombos);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 63, 730, 263));

        jLabel1.setText("Buscar por nombre:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 23, -1, -1));

        txtBuscarCombo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarComboKeyReleased(evt);
            }
        });
        getContentPane().add(txtBuscarCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 20, 399, -1));

        btnCerrar.setBackground(new java.awt.Color(255, 51, 51));
        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 344, 80, 30));

        pnlFondo.setBackground(new java.awt.Color(255, 204, 255));

        javax.swing.GroupLayout pnlFondoLayout = new javax.swing.GroupLayout(pnlFondo);
        pnlFondo.setLayout(pnlFondoLayout);
        pnlFondoLayout.setHorizontalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 770, Short.MAX_VALUE)
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
        );

        getContentPane().add(pnlFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 770, 390));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblMostrarCombosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMostrarCombosMouseClicked
        int fila = tblMostrarCombos.getSelectedRow();
        if (fila != -1) {
            int idCombo = Integer.parseInt(tblMostrarCombos.getValueAt(fila, 0).toString());

            ComboDAO dao = new ComboDAO();
            Combo seleccionado = dao.buscarComboPorId(idCombo);

            if (seleccionado != null) {
                formularioPrincipal.cargarDatosParaEditar(seleccionado);
                this.dispose();
            }
        }

    }//GEN-LAST:event_tblMostrarCombosMouseClicked

    private void txtBuscarComboKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarComboKeyReleased
        String nombre = txtBuscarCombo.getText();
        mostrarCombos(nombre);
    }//GEN-LAST:event_txtBuscarComboKeyReleased

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed
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
            java.util.logging.Logger.getLogger(frmBuscarCombo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmBuscarCombo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmBuscarCombo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmBuscarCombo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new frmBuscarCombo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlFondo;
    private javax.swing.JTable tblMostrarCombos;
    private javax.swing.JTextField txtBuscarCombo;
    // End of variables declaration//GEN-END:variables
}
