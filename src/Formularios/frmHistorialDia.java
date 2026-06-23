package Formularios;

import Clases.Orden;
import Clases.OrdenDAO;
import Clases.OrdenService;
import Clases.Sesion;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class frmHistorialDia extends javax.swing.JFrame {

    private OrdenDAO ordenDAO = new OrdenDAO();
    private OrdenService ordenService = new OrdenService();
    private int idOrdenSeleccionada = -1;
    private String estadoOrdenSeleccionada = "";

    public frmHistorialDia() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Historial de Órdenes del Día");
        configurarTabla();
        cargarHistorial();
        configurarClick();
        aplicarPermisos();
    }

    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"# Orden", "Hora", "Mesa", "Mesero", "Total", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblHistorial.setModel(modelo);
        tblHistorial.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblHistorial.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblHistorial.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblHistorial.getColumnModel().getColumn(2).setPreferredWidth(60);
        tblHistorial.getColumnModel().getColumn(3).setPreferredWidth(180);
        tblHistorial.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblHistorial.getColumnModel().getColumn(5).setPreferredWidth(100);
    }

    private void cargarHistorial() {
        DefaultTableModel modelo = (DefaultTableModel) tblHistorial.getModel();
        modelo.setRowCount(0);
        idOrdenSeleccionada = -1;
        estadoOrdenSeleccionada = "";

        ArrayList<Orden> ordenes = ordenDAO.listarDelDia();

        if (ordenes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay órdenes registradas hoy.");
            return;
        }

        for (Orden o : ordenes) {
            String hora = new java.text.SimpleDateFormat("HH:mm:ss").format(o.getFechaHora());
            modelo.addRow(new Object[]{
                o.getIdOrden(),
                hora,
                "Mesa " + o.getNumeroMesa(),
                o.getNombreUsuario(),
                String.format("$%.2f", o.getTotalGuardado()),
                o.getEstado()
            });
        }
    }

    private void configurarClick() {
        tblHistorial.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int fila = tblHistorial.getSelectedRow();
                if (fila >= 0) {
                    idOrdenSeleccionada = (int) tblHistorial.getValueAt(fila, 0);
                    estadoOrdenSeleccionada = tblHistorial.getValueAt(fila, 5).toString();
                }
            }
        });
    }

 
    private void aplicarPermisos() {
        String rol = Sesion.getUsuarioActual().getRol();
        btnAnular.setVisible(rol.equals("ADMINISTRADOR"));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblHistorial = new javax.swing.JTable();
        btnAnular = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblHistorial.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblHistorial);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 17, 777, 320));

        btnAnular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnAnular.setText("Anular");
        btnAnular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnularActionPerformed(evt);
            }
        });
        getContentPane().add(btnAnular, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 367, 107, 34));

        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/x(2).png"))); // NOI18N
        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(149, 367, 103, 34));

        jPanel2.setBackground(new java.awt.Color(255, 255, 102));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 810, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 440));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnularActionPerformed
        if (idOrdenSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden de la tabla.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

 
        if (!estadoOrdenSeleccionada.equals("PROCESADA") && !estadoOrdenSeleccionada.equals("DESPACHADA")) {
            JOptionPane.showMessageDialog(this,
                    "Solo se pueden anular órdenes en estado PROCESADA o DESPACHADA.",
                    "No permitido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Anular la Orden #" + idOrdenSeleccionada + "?\nEsta acción no se puede deshacer.",
                "Confirmar anulación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = ordenService.anularOrden(idOrdenSeleccionada);
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Orden #" + idOrdenSeleccionada + " anulada correctamente.");
                cargarHistorial(); 
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo anular la orden.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnAnularActionPerformed

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
            java.util.logging.Logger.getLogger(frmHistorialDia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmHistorialDia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmHistorialDia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmHistorialDia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmHistorialDia().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnular;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblHistorial;
    // End of variables declaration//GEN-END:variables
}
