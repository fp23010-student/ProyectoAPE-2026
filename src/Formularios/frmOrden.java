package Formularios;

import Clases.Categoria;
import Clases.CategoriaDAO;
import Clases.Combo;
import Clases.ComboDAO;
import Clases.DetalleOrden;
import Clases.Item;
import Clases.Mesa;
import Clases.MesaDAO;
import Clases.Orden;
import Clases.OrdenDAO;
import Clases.OrdenService;
import Clases.Producto;
import Clases.ProductoDAO;
import Clases.Sesion;
import Clases.TicketPDF;
import Clases.Usuario;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class frmOrden extends javax.swing.JFrame {

    private OrdenService ordenService = new OrdenService();
    private OrdenDAO ordenDAO = new OrdenDAO();
    private MesaDAO mesaDAO = new MesaDAO();
    private CategoriaDAO categoriaDAO = new CategoriaDAO();
    private Orden ordenActual = new Orden();
    private boolean actualizandoTabla = false; // variable de instancia

    public frmOrden() {
        initComponents();
        this.setLocationRelativeTo(null);

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Imagenes/tacoSinFondo.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(
                lblLogo.getWidth(), lblLogo.getHeight(), Image.SCALE_SMOOTH);
        lblLogo.setIcon(new ImageIcon(scaledImage));

        configurarTabla();
        cargarDatosIniciales();
        configurarEventoCmbCategoria();
    }

    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"#", "Descripción", "Precio Unit.", "Cantidad", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // solo columna Cantidad editable
            }
        };
        tblDetalle.setModel(modelo);
        tblDetalle.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        tblDetalle.getColumnModel().getColumn(0).setPreferredWidth(30);
        tblDetalle.getColumnModel().getColumn(1).setPreferredWidth(160);
        tblDetalle.getColumnModel().getColumn(2).setPreferredWidth(70);
        tblDetalle.getColumnModel().getColumn(3).setPreferredWidth(50);
        tblDetalle.getColumnModel().getColumn(4).setPreferredWidth(70);

        // Tecla Suprimir para eliminar fila seleccionada
        tblDetalle.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE) {
                    int fila = tblDetalle.getSelectedRow();
                    if (fila >= 0) {
                        ordenActual.eliminarDetalle(fila);
                        ((DefaultTableModel) tblDetalle.getModel()).removeRow(fila);
                        reordenarNumeros();
                        txtTotal.setText(String.format("%.2f", ordenActual.getTotal()));
                    }
                }
            }
        });

        // Escuchar cambios en la columna Cantidad
        modelo.addTableModelListener(e -> {
            if (actualizandoTabla) {
                return; // ignorar cambios programáticos
            }
            if (e.getColumn() == 3 && e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
                int fila = e.getFirstRow();
                if (fila < 0 || fila >= ordenActual.getDetalles().size()) {
                    return;
                }

                try {
                    Object valor = tblDetalle.getModel().getValueAt(fila, 3);
                    int nuevaCantidad = Integer.parseInt(valor.toString().trim());
                    DetalleOrden detalle = ordenActual.getDetalles().get(fila);

                    if (nuevaCantidad <= 0) {
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            ordenActual.eliminarDetalle(fila);
                            ((DefaultTableModel) tblDetalle.getModel()).removeRow(fila);
                            reordenarNumeros();
                            txtTotal.setText(String.format("%.2f", ordenActual.getTotal()));
                        });
                    } else {
                        detalle.setCantidad(nuevaCantidad);
                        actualizandoTabla = true; // bloquear antes de setValueAt
                        tblDetalle.getModel().setValueAt(
                                String.format("$%.2f", detalle.getSubtotal()), fila, 4);
                        actualizandoTabla = false; // desbloquear
                        txtTotal.setText(String.format("%.2f", ordenActual.getTotal()));
                    }

                } catch (NumberFormatException ex) {
                    if (fila < ordenActual.getDetalles().size()) {
                        actualizandoTabla = true;
                        tblDetalle.getModel().setValueAt(
                                ordenActual.getDetalles().get(fila).getCantidad(), fila, 3);
                        actualizandoTabla = false;
                    }
                }
            }
        });
    }

    private void reordenarNumeros() {
        DefaultTableModel modelo = (DefaultTableModel) tblDetalle.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.setValueAt(i + 1, i, 0);
        }
    }

    private void cargarDatosIniciales() {
        txtIDOrdeen.setText(String.valueOf(ordenDAO.obtenerProximoId()));
        txtIDOrdeen.setEditable(false);

        txtFechaHora.setText(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .format(new java.util.Date()));
        txtFechaHora.setEditable(false);

        Usuario u = Sesion.getUsuarioActual();
        txtUsuario.setText(u.toString());
        txtUsuario.setEditable(false);

        txtTotal.setEditable(false);
        txtTotal.setText("0.00");

        cargarMesas();
        cargarCategorias();
    }

    private void configurarEventoCmbCategoria() {
        cmbCategoria.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object seleccion = cmbCategoria.getSelectedItem();
                if (seleccion == null) {
                    return;
                }

                if (seleccion instanceof String) {
                    // Es "-- Combos --"
                    cargarCombos();
                } else {
                    Categoria cat = (Categoria) seleccion;
                    cargarProductosPorCategoria(cat.getIdCategoria()); // ya es String
                }
            }
        });
    }

    private void cargarMesas() {
        ArrayList<Mesa> mesas = mesaDAO.listarMesasLibres();
        DefaultComboBoxModel<Mesa> modelo = new DefaultComboBoxModel<>();
        for (Mesa m : mesas) {
            modelo.addElement(m);
        }
        cmbMesa.setModel(modelo);
    }

    private void cargarCategorias() {
        DefaultComboBoxModel<Object> modelo = new DefaultComboBoxModel<>();
        modelo.addElement("Combos");

        CategoriaDAO categoriaDAO = new CategoriaDAO();
        List<Categoria> cats = categoriaDAO.listar(); // listar() no listarTodos()
        for (Categoria c : cats) {
            modelo.addElement(c);
        }
        cmbCategoria.setModel(modelo);
        cargarCombos(); // por defecto carga combos al abrir
    }

    private void cargarCombos() {
        ComboDAO comboDAO = new ComboDAO();
        List<Combo> combos = comboDAO.listarCombos(""); // listarCombos("") no listarTodos()

        DefaultComboBoxModel<Item> modelo = new DefaultComboBoxModel<>();
        for (Combo c : combos) {
            modelo.addElement(new Item(c.getIdCombo(), c.getCombo(), c.getPrecioCombo(), true));
        }
        cmbProudcto_Combo.setModel(modelo);
    }

    private void cargarProductosPorCategoria(String idCategoria) { // String no int
        ProductoDAO productoDAO = new ProductoDAO();
        List<Producto> productos = productoDAO.listarPorCategoria(idCategoria);

        DefaultComboBoxModel<Item> modelo = new DefaultComboBoxModel<>();
        for (Producto p : productos) {
            modelo.addElement(new Item(
                    p.getIdProducto(),
                    p.getNombreProducto(),
                    Double.parseDouble(p.getPrecioProducto()), // precioProducto es String
                    false
            ));
        }
        cmbProudcto_Combo.setModel(modelo);
    }

    private void limpiarFormulario() {
        ordenActual = new Orden();

        // Limpiar tabla
        ((DefaultTableModel) tblDetalle.getModel()).setRowCount(0);

        // Resetear total
        txtTotal.setText("0.00");

        // Nuevo ID próximo
        txtIDOrdeen.setText(String.valueOf(ordenDAO.obtenerProximoId()));

        // Nueva fecha
        txtFechaHora.setText(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .format(new java.util.Date()));

        // Recargar mesas (la que se ocupó ya no aparece)
        cargarMesas();

        // Resetear spinner
        spnCantidad.setValue(1);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblLogo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtIDOrdeen = new javax.swing.JTextField();
        txtFechaHora = new javax.swing.JTextField();
        txtUsuario = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cmbCategoria = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbProudcto_Combo = new javax.swing.JComboBox<>();
        spnCantidad = new javax.swing.JSpinner();
        btnAgregar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetalle = new javax.swing.JTable();
        btnProcesar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cmbMesa = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ordenes");

        lblLogo.setText("jLabel1");

        jLabel1.setText("ID de Orden:");

        jLabel2.setText("Fecha y Hora:");

        jLabel3.setText("Usuario:");

        jLabel4.setText("Categoria:");

        cmbCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCategoriaActionPerformed(evt);
            }
        });

        jLabel5.setText("Producto/Combo:");

        jLabel6.setText("Cantidad:");

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        tblDetalle.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblDetalle);

        btnProcesar.setText("Procesar");
        btnProcesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcesarActionPerformed(evt);
            }
        });

        jLabel7.setText("Total $:");

        jLabel8.setText("Mesa:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(cmbMesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnProcesar)
                        .addGap(43, 43, 43)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIDOrdeen, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFechaHora, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                            .addComponent(txtUsuario))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbProudcto_Combo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(spnCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                                .addComponent(btnAgregar)))))
                .addGap(17, 17, 17))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtIDOrdeen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtFechaHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(cmbProudcto_Combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(spnCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAgregar)))
                    .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnProcesar)
                            .addComponent(jLabel7)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(cmbMesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCategoriaActionPerformed

    private void btnProcesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcesarActionPerformed
        if (!ordenActual.tieneDetalles()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un producto a la orden.");
            return;
        }

        Mesa mesa = (Mesa) cmbMesa.getSelectedItem();
        if (mesa == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una mesa.");
            return;
        }

        Usuario u = Sesion.getUsuarioActual();
        ordenActual.setIdUsuario(u.getIdUsuario());
        ordenActual.setNombreUsuario(u.getNombre() + " " + u.getApellido());
        ordenActual.setIdMesa(mesa.getIdMesa());
        ordenActual.setNumeroMesa(mesa.getNumero());
        ordenActual.setFechaHora(new java.util.Date());

        int idOrden = ordenService.procesarOrden(ordenActual);

        if (idOrden > 0) {
            new TicketPDF().generarTicket(idOrden);
            ordenService.liberarMesa(mesa.getIdMesa());
            JOptionPane.showMessageDialog(this,
                    "Orden #" + idOrden + " procesada correctamente.\nTicket generado.");
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error al procesar la orden. Intente de nuevo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnProcesarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        Item itemSeleccionado = (Item) cmbProudcto_Combo.getSelectedItem();
        if (itemSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto o combo.");
            return;
        }
        int cantidad = (int) spnCantidad.getValue();
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
            return;
        }
        DefaultTableModel modelo = (DefaultTableModel) tblDetalle.getModel();

        // Buscar si el item ya existe en la tabla
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String nombreFila = modelo.getValueAt(i, 1).toString();
            if (nombreFila.equals(itemSeleccionado.getNombre())) {
                DetalleOrden detalle = ordenActual.getDetalles().get(i);
                int nuevaCantidad = detalle.getCantidad() + cantidad;
                detalle.setCantidad(nuevaCantidad);

                actualizandoTabla = true;
                modelo.setValueAt(nuevaCantidad, i, 3);
                modelo.setValueAt(String.format("$%.2f", detalle.getSubtotal()), i, 4);
                actualizandoTabla = false;

                txtTotal.setText(String.format("%.2f", ordenActual.getTotal()));
                spnCantidad.setValue(1);
                return;
            }
        }

        // Si no existe, agregar nueva fila
        DetalleOrden detalle = new DetalleOrden(0, itemSeleccionado, cantidad);
        ordenActual.agregarDetalle(detalle);

        actualizandoTabla = true;
        modelo.addRow(new Object[]{
            detalle.getIdLinea(),
            detalle.getNombre(),
            String.format("$%.2f", detalle.getPrecioUnitario()),
            detalle.getCantidad(),
            String.format("$%.2f", detalle.getSubtotal())
        });
        actualizandoTabla = false;

        txtTotal.setText(String.format("%.2f", ordenActual.getTotal()));
        spnCantidad.setValue(1);
    }//GEN-LAST:event_btnAgregarActionPerformed

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
            java.util.logging.Logger.getLogger(frmOrden.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmOrden.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmOrden.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmOrden.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmOrden().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnProcesar;
    private javax.swing.JComboBox<Object> cmbCategoria;
    private javax.swing.JComboBox<Mesa> cmbMesa;
    private javax.swing.JComboBox<Item> cmbProudcto_Combo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JSpinner spnCantidad;
    private javax.swing.JTable tblDetalle;
    private javax.swing.JTextField txtFechaHora;
    private javax.swing.JTextField txtIDOrdeen;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
