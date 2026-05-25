package Formularios;

import Clases.Combo;
import Clases.ComboDAO;
import Clases.Detalle;
import Clases.DetalleDAO;
import Clases.Producto;
import Clases.ProductoDAO;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class frmCombo extends javax.swing.JFrame {

    private DefaultTableModel modeloProductos;
    private DefaultTableModel modeloDetalle;

    public frmCombo() {
        initComponents();
        this.setLocationRelativeTo(null);
        configurarDisenoTablas();
        mostrarProductosDisponibles();
        asignarIdAutomatico();

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Imagenes/tacoSinFondo.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(
                jLabel8.getWidth(),
                jLabel8.getHeight(),
                Image.SCALE_SMOOTH
        );
        jLabel8.setIcon(new ImageIcon(scaledImage));

    }

    public void cargarDatosParaEditar(Combo seleccionado) {
        txtIDCombo.setText(String.valueOf(seleccionado.getIdCombo()));
        txtNombreCombo.setText(seleccionado.getCombo());
        txtPrecio.setText(String.valueOf(seleccionado.getPrecioCombo()));

        modeloDetalle.setRowCount(0);

        for (Detalle det : seleccionado.getDetalle()) {

            double precioUnitario = Double.parseDouble(det.getProductos().getPrecioProducto());
            int cantidad = det.getCantidad();
            double subtotal = precioUnitario * cantidad;

            modeloDetalle.addRow(new Object[]{
                det.getProductos().getIdProducto(),
                det.getProductos().getNombreProducto(),
                precioUnitario,
                cantidad,
                subtotal
            });
        }
        calcularGranTotal();

        btnGuardar.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);
    }

    private void limpiarFormulario() {
        txtNombreCombo.setText("");
        txtPrecio.setText("");
        txtTotalProductos.setText("0.00");
        txrBuscarPorNombre.setText("");

        DefaultTableModel modelo = (DefaultTableModel) tblMostrarCombos.getModel();
        modelo.setRowCount(0);

        asignarIdAutomatico();

        mostrarProductosDisponibles();

        txtNombreCombo.requestFocus();
    }

    private void asignarIdAutomatico() {
        ComboDAO dao = new ComboDAO();
        int siguienteId = dao.generarIdAutomatico();
        txtIDCombo.setText(String.format("%03d", siguienteId));
    }

    private void configurarDisenoTablas() {

        modeloProductos = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Descripción", "ID Cat", "Categoría", "Precio"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMostrarProductos.setModel(modeloProductos);

        tblMostrarProductos.getColumnModel().getColumn(0).setPreferredWidth(10);
        tblMostrarProductos.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblMostrarProductos.getColumnModel().getColumn(2).setPreferredWidth(330);
        tblMostrarProductos.getColumnModel().getColumn(3).setPreferredWidth(20);
        tblMostrarProductos.getColumnModel().getColumn(4).setPreferredWidth(20);
        tblMostrarProductos.getColumnModel().getColumn(5).setPreferredWidth(5);

        modeloDetalle = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Precio Unit.", "Cantidad", "Subtotal"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMostrarCombos.setModel(modeloDetalle);

        tblMostrarCombos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblMostrarCombos.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblMostrarCombos.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblMostrarCombos.getColumnModel().getColumn(3).setPreferredWidth(60);
        tblMostrarCombos.getColumnModel().getColumn(4).setPreferredWidth(80);

        DefaultTableCellRenderer derecha = new DefaultTableCellRenderer();
        derecha.setHorizontalAlignment(SwingConstants.RIGHT);

        tblMostrarProductos.getColumnModel().getColumn(5).setCellRenderer(derecha);
        tblMostrarCombos.getColumnModel().getColumn(2).setCellRenderer(derecha);
        tblMostrarCombos.getColumnModel().getColumn(4).setCellRenderer(derecha);
    }

    private void mostrarProductosDisponibles() {
        ProductoDAO dao = new ProductoDAO();
        List<Producto> lista = dao.listar();
        modeloProductos.setRowCount(0);

        for (Producto p : lista) {
            modeloProductos.addRow(new Object[]{
                p.getIdProducto(),
                p.getNombreProducto(),
                p.getDescripcionProducto(),
                p.getIdCategoria(),
                p.getCategoria(),
                "$" + p.getPrecioProducto()
            });
        }
    }

    public void calcularGranTotal() {
        double total = 0;
        for (int i = 0; i < tblMostrarCombos.getRowCount(); i++) {
            total += Double.parseDouble(tblMostrarCombos.getValueAt(i, 4).toString());
        }
        txtTotalProductos.setText(String.format("%.2f", total));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIDCombo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNombreCombo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txrBuscarPorNombre = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMostrarProductos = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblMostrarCombos = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtTotalProductos = new javax.swing.JTextField();
        txtPrecio = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtVerCombos = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        pnlFondo = new javax.swing.JPanel();
        btnQuitar = new javax.swing.JButton();
        btnMenu = new javax.swing.JButton();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 134, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("ID de combo:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(297, 29, -1, -1));

        txtIDCombo.setEnabled(false);
        getContentPane().add(txtIDCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(433, 26, 86, -1));

        jLabel2.setText("Nombre del Combo:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(297, 72, -1, -1));
        getContentPane().add(txtNombreCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(434, 69, 502, -1));

        jLabel3.setText("Seleccione uno por uno los productos del combo:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(297, 112, -1, -1));

        jLabel4.setText("Buscar por nombre:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(297, 155, -1, -1));

        txrBuscarPorNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txrBuscarPorNombreActionPerformed(evt);
            }
        });
        txrBuscarPorNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txrBuscarPorNombreKeyReleased(evt);
            }
        });
        getContentPane().add(txrBuscarPorNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(433, 152, 497, -1));

        tblMostrarProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblMostrarProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMostrarProductosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblMostrarProductos);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 201, 965, 209));

        jLabel5.setText("Su combo tiene los siguientes productos:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 422, -1, -1));

        tblMostrarCombos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tblMostrarCombos);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 459, 603, 145));

        jLabel6.setText("Total en Productos:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(669, 462, -1, -1));

        jLabel7.setText("Precio:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(669, 505, -1, -1));

        txtTotalProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalProductosActionPerformed(evt);
            }
        });
        getContentPane().add(txtTotalProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 459, 73, -1));
        getContentPane().add(txtPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 502, 73, -1));

        btnGuardar.setBackground(new java.awt.Color(255, 51, 51));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(891, 459, 105, -1));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disquete(1).png"))); // NOI18N
        jLabel8.setText("jLabel8");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 29, 211, 148));

        txtVerCombos.setBackground(new java.awt.Color(255, 51, 51));
        txtVerCombos.setText("Ver Combos");
        txtVerCombos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVerCombosActionPerformed(evt);
            }
        });
        getContentPane().add(txtVerCombos, new org.netbeans.lib.awtextra.AbsoluteConstraints(891, 502, 110, -1));

        btnActualizar.setBackground(new java.awt.Color(255, 51, 51));
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(891, 539, 105, -1));

        btnEliminar.setBackground(new java.awt.Color(255, 51, 51));
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(891, 576, 105, -1));

        pnlFondo.setBackground(new java.awt.Color(255, 204, 255));
        pnlFondo.setForeground(new java.awt.Color(255, 204, 204));

        btnQuitar.setBackground(new java.awt.Color(255, 51, 51));
        btnQuitar.setText("Quitar");
        btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });

        btnMenu.setText("Menu");
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFondoLayout = new javax.swing.GroupLayout(pnlFondo);
        pnlFondo.setLayout(pnlFondoLayout);
        pnlFondoLayout.setHorizontalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFondoLayout.createSequentialGroup()
                .addContainerGap(673, Short.MAX_VALUE)
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnQuitar, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(btnMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(257, 257, 257))
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFondoLayout.createSequentialGroup()
                .addContainerGap(536, Short.MAX_VALUE)
                .addComponent(btnQuitar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnMenu)
                .addGap(7, 7, 7))
        );

        getContentPane().add(pnlFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1010, 610));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txrBuscarPorNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txrBuscarPorNombreActionPerformed

    }//GEN-LAST:event_txrBuscarPorNombreActionPerformed

    private void txtTotalProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalProductosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalProductosActionPerformed

    private void tblMostrarProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMostrarProductosMouseClicked
        int fila = tblMostrarProductos.getSelectedRow();
        if (fila != -1) {
            String idBuscado = tblMostrarProductos.getValueAt(fila, 0).toString();
            String nombre = tblMostrarProductos.getValueAt(fila, 1).toString();

            String precioStr = tblMostrarProductos.getValueAt(fila, 5).toString().replace("$", "");
            double precio = Double.parseDouble(precioStr);

            boolean productoEncontrado = false;
            int filaDestino = -1;

            for (int i = 0; i < tblMostrarCombos.getRowCount(); i++) {
                String idEnTabla = tblMostrarCombos.getValueAt(i, 0).toString();
                if (idEnTabla.equals(idBuscado)) {
                    productoEncontrado = true;
                    filaDestino = i;
                    break;
                }
            }

            if (productoEncontrado) {
                int cantActual = Integer.parseInt(tblMostrarCombos.getValueAt(filaDestino, 3).toString());
                int nuevaCant = cantActual + 1;
                double nuevoSubtotal = nuevaCant * precio;

                tblMostrarCombos.setValueAt(nuevaCant, filaDestino, 3);
                tblMostrarCombos.setValueAt(nuevoSubtotal, filaDestino, 4);

            } else {
                Object[] filaDetalle = {idBuscado, nombre, precio, 1, precio};
                modeloDetalle.addRow(filaDetalle);
            }

            calcularGranTotal();
        }
    }//GEN-LAST:event_tblMostrarProductosMouseClicked

    private void txrBuscarPorNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txrBuscarPorNombreKeyReleased
        String buscar = txrBuscarPorNombre.getText().toLowerCase();
        ProductoDAO dao = new ProductoDAO();
        List<Producto> lista = dao.listar();

        DefaultTableModel modelo = (DefaultTableModel) tblMostrarProductos.getModel();
        modelo.setRowCount(0);

        for (Producto p : lista) {
            if (p.getNombreProducto().toLowerCase().contains(buscar)) {
                modelo.addRow(new Object[]{
                    p.getIdProducto(),
                    p.getNombreProducto(),
                    p.getDescripcionProducto(),
                    p.getIdCategoria(),
                    p.getCategoria(),
                    "$" + p.getPrecioProducto()
                });
            }
        }
    }//GEN-LAST:event_txrBuscarPorNombreKeyReleased

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // 1. Validar que haya productos
        if (tblMostrarCombos.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Agregue productos al combo antes de guardar.");
            return;
        }

        double precioFinal;
        try {
            if (!txtPrecio.getText().trim().isEmpty()) {
                precioFinal = Double.parseDouble(txtPrecio.getText().trim());
            } else {
                precioFinal = Double.parseDouble(txtTotalProductos.getText().trim());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Asegúrese de que los precios sean números válidos.");
            return;
        }

        if (txtPrecio.getText().equals("0.0") || txtPrecio.getText().equals("00.00") || txtPrecio.getText().equals("0") || txtPrecio.getText().equals("0.")) {
            JOptionPane.showMessageDialog(null, "Agregue el precio");
            return;
        }

        List<Detalle> listaDetalles = new ArrayList<>();
        for (int i = 0; i < tblMostrarCombos.getRowCount(); i++) {
            int idProd = Integer.parseInt(tblMostrarCombos.getValueAt(i, 0).toString());
            int cant = Integer.parseInt(tblMostrarCombos.getValueAt(i, 3).toString());

            Detalle det = new Detalle();
            Producto p = new Producto();

            p.setIdProducto(idProd);
            det.setProductos(p);
            det.setCantidad(cant);

            listaDetalles.add(det);
        }

        int idComboActual = Integer.parseInt(txtIDCombo.getText());
        Combo c = new Combo();
        c.setIdCombo(idComboActual);
        c.setCombo(txtNombreCombo.getText().toUpperCase());
        c.setPrecioCombo(precioFinal);
        c.setDetalle(listaDetalles);

        ComboDAO comboDao = new ComboDAO();
        DetalleDAO detalleDao = new DetalleDAO();

        if (comboDao.guardarCombo(c)) {

            detalleDao.guardarDetalle(idComboActual, listaDetalles);

            JOptionPane.showMessageDialog(this, "¡Combo y productos guardados!");
            limpiarFormulario();
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtVerCombosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVerCombosActionPerformed

        frmBuscarCombo buscador = new frmBuscarCombo(this);
        buscador.setVisible(true);
    }//GEN-LAST:event_txtVerCombosActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed

        if (txtNombreCombo.getText().isEmpty() || tblMostrarCombos.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Debe tener un nombre y al menos un producto.");
            return;
        }

        if (txtPrecio.getText().equals("0.0") || txtPrecio.getText().equals("00.00") || txtPrecio.getText().equals("0") || txtPrecio.getText().equals("0.")) {
            JOptionPane.showMessageDialog(null, "Agregue el precio");
            return;
        }

        Combo c = new Combo();
        c.setIdCombo(Integer.parseInt(txtIDCombo.getText()));
        c.setCombo(txtNombreCombo.getText());

        double precio = txtPrecio.getText().isEmpty()
                ? Double.parseDouble(txtTotalProductos.getText())
                : Double.parseDouble(txtPrecio.getText());
        c.setPrecioCombo(precio);

        List<Detalle> detalles = new ArrayList<>();
        for (int i = 0; i < tblMostrarCombos.getRowCount(); i++) {
            Detalle d = new Detalle();
            Producto p = new Producto();

            p.setIdProducto(Integer.parseInt(tblMostrarCombos.getValueAt(i, 0).toString()));
            d.setProductos(p);
            d.setCantidad(Integer.parseInt(tblMostrarCombos.getValueAt(i, 3).toString()));

            detalles.add(d);
        }
        c.setDetalle(detalles);

        ComboDAO dao = new ComboDAO();
        if (dao.actualizarCombo(c)) {
            JOptionPane.showMessageDialog(this, "Combo actualizado exitosamente.");
            limpiarFormulario();
            btnActualizar.setEnabled(false);
            btnEliminar.setEnabled(false);
            btnGuardar.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "Error al intentar actualizar.");
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        int fila = tblMostrarCombos.getSelectedRow();

        if (fila != -1) {
            modeloDetalle.removeRow(fila);
            calcularGranTotal();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto de la tabla para quitarlo.");
        }
    }//GEN-LAST:event_btnQuitarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed

        if (txtIDCombo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un combo desde el buscador.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el combo '" + txtNombreCombo.getText() + "'?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtIDCombo.getText());
            ComboDAO dao = new ComboDAO();

            if (dao.eliminarCombo(id)) {
                JOptionPane.showMessageDialog(this, "Combo eliminado correctamente.");
                limpiarFormulario();
                btnActualizar.setEnabled(false);
                btnEliminar.setEnabled(false);
                btnGuardar.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el combo.");
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        this.dispose();
        new frmMenu().setVisible(true);
    }//GEN-LAST:event_btnMenuActionPerformed

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
            java.util.logging.Logger.getLogger(frmCombo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmCombo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmCombo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmCombo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmCombo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable2;
    private javax.swing.JPanel pnlFondo;
    private javax.swing.JTable tblMostrarCombos;
    private javax.swing.JTable tblMostrarProductos;
    private javax.swing.JTextField txrBuscarPorNombre;
    private javax.swing.JTextField txtIDCombo;
    private javax.swing.JTextField txtNombreCombo;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtTotalProductos;
    private javax.swing.JButton txtVerCombos;
    // End of variables declaration//GEN-END:variables
}
