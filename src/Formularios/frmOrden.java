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
    private boolean actualizandoTabla = false;
    private int idOrdenCargada = -1;

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
                new String[]{"ID", "Descripción", "Precio Unit.", "Cantidad", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDetalle.setModel(modelo);
        tblDetalle.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblDetalle.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        tblDetalle.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblDetalle.getColumnModel().getColumn(1).setPreferredWidth(729);
        tblDetalle.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblDetalle.getColumnModel().getColumn(3).setPreferredWidth(90);
        tblDetalle.getColumnModel().getColumn(4).setPreferredWidth(76);

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

                    cargarCombos();
                } else {
                    Categoria cat = (Categoria) seleccion;
                    cargarProductosPorCategoria(cat.getIdCategoria()); 
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
        List<Categoria> cats = categoriaDAO.listar();
        for (Categoria c : cats) {
            modelo.addElement(c);
        }
        cmbCategoria.setModel(modelo);
        cargarCombos();
    }

    private void cargarCombos() {
        ComboDAO comboDAO = new ComboDAO();
        List<Combo> combos = comboDAO.listarCombos("");

        DefaultComboBoxModel<Item> modelo = new DefaultComboBoxModel<>();
        for (Combo c : combos) {
            modelo.addElement(new Item(c.getIdCombo(), c.getCombo(), c.getPrecioCombo(), true));
        }
        cmbProudcto_Combo.setModel(modelo);
    }

    private void cargarProductosPorCategoria(String idCategoria) { 
        ProductoDAO productoDAO = new ProductoDAO();
        List<Producto> productos = productoDAO.listarPorCategoria(idCategoria);

        DefaultComboBoxModel<Item> modelo = new DefaultComboBoxModel<>();
        for (Producto p : productos) {
            modelo.addElement(new Item(
                    p.getIdProducto(),
                    p.getNombreProducto(),
                    Double.parseDouble(p.getPrecioProducto()), 
                    false
            ));
        }
        cmbProudcto_Combo.setModel(modelo);
    }

    private void limpiarFormulario() {
        ordenActual = new Orden();
        idOrdenCargada = -1;

        ((DefaultTableModel) tblDetalle.getModel()).setRowCount(0);
        txtTotal.setText("0.00");
        txtIDOrdeen.setText(String.valueOf(ordenDAO.obtenerProximoId()));
        txtFechaHora.setText(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .format(new java.util.Date()));
        cargarMesas();
        spnCantidad.setValue(1);
    }

    public void cargarOrdenExistente(Orden orden) {
        idOrdenCargada = orden.getIdOrden();
        ordenActual = orden;

        txtIDOrdeen.setText(String.valueOf(orden.getIdOrden()));
        txtFechaHora.setText(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                .format(orden.getFechaHora()));

        cargarTodasLasMesas();
        seleccionarMesa(orden.getIdMesa());

        DefaultTableModel modelo = (DefaultTableModel) tblDetalle.getModel();
        modelo.setRowCount(0);
        for (DetalleOrden d : orden.getDetalles()) {
            actualizandoTabla = true;
            modelo.addRow(new Object[]{
                d.getIdLinea(),
                d.getNombre(),
                String.format("$%.2f", d.getPrecioUnitario()),
                d.getCantidad(),
                String.format("$%.2f", d.getSubtotal())
            });
            actualizandoTabla = false;
        }
        txtTotal.setText(String.format("%.2f", orden.getTotal()));
    }

    private void cargarTodasLasMesas() {
        List<Mesa> mesas = mesaDAO.listarTodas();
        DefaultComboBoxModel<Mesa> modelo = new DefaultComboBoxModel<>();
        for (Mesa m : mesas) {
            modelo.addElement(m);
        }
        cmbMesa.setModel(modelo);
    }

    private void seleccionarMesa(int idMesa) {
        for (int i = 0; i < cmbMesa.getItemCount(); i++) {
            if (cmbMesa.getItemAt(i).getIdMesa() == idMesa) {
                cmbMesa.setSelectedIndex(i);
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel9 = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
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
        btnAgregar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetalle = new javax.swing.JTable();
        btnProcesar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cmbMesa = new javax.swing.JComboBox<>();
        btnMenu = new javax.swing.JButton();
        btnVerOrdenes = new javax.swing.JButton();
        btnAnular = new javax.swing.JButton();
        btnNuevaOrden = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btnDespachar = new javax.swing.JButton();
        spnCantidad = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();

        jLabel9.setText("Ordenes Pendientes:");

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 51));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1110, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 610, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ordenes");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblLogo.setText("jLabel1");
        getContentPane().add(lblLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 160, 140));

        jLabel1.setText("ID de Orden:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 18, -1, -1));

        jLabel2.setText("Fecha y Hora:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 66, -1, -1));

        jLabel3.setText("Usuario:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 119, -1, -1));

        txtIDOrdeen.setEditable(false);
        txtIDOrdeen.setEnabled(false);
        getContentPane().add(txtIDOrdeen, new org.netbeans.lib.awtextra.AbsoluteConstraints(302, 15, 75, -1));

        txtFechaHora.setEditable(false);
        txtFechaHora.setEnabled(false);
        getContentPane().add(txtFechaHora, new org.netbeans.lib.awtextra.AbsoluteConstraints(302, 63, 247, -1));

        txtUsuario.setEditable(false);
        txtUsuario.setEnabled(false);
        getContentPane().add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(302, 116, 247, -1));

        jLabel4.setText("Categoria:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 20, -1, -1));

        cmbCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCategoriaActionPerformed(evt);
            }
        });
        getContentPane().add(cmbCategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(771, 15, 310, -1));

        jLabel5.setText("Producto/Combo:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 70, -1, -1));

        jLabel6.setText("Cantidad:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 123, -1, -1));

        getContentPane().add(cmbProudcto_Combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 70, 311, -1));

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/agregar.png"))); // NOI18N
        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(946, 119, 135, -1));

        tblDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblDetalle);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 175, 1060, 310));

        btnProcesar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/procesar.png"))); // NOI18N
        btnProcesar.setText("Procesar");
        btnProcesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcesarActionPerformed(evt);
            }
        });
        getContentPane().add(btnProcesar, new org.netbeans.lib.awtextra.AbsoluteConstraints(799, 516, -1, -1));

        jLabel7.setText("Total $:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(931, 520, -1, -1));

        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });
        getContentPane().add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(991, 520, 90, -1));

        jLabel8.setText("Mesa:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 519, 70, 20));

        getContentPane().add(cmbMesa, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 512, 100, 30));

        btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/menu.png"))); // NOI18N
        btnMenu.setText("Menu");
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });
        getContentPane().add(btnMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(971, 557, 110, -1));

        btnVerOrdenes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ver.png"))); // NOI18N
        btnVerOrdenes.setText("Ver odenes");
        btnVerOrdenes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerOrdenesActionPerformed(evt);
            }
        });
        getContentPane().add(btnVerOrdenes, new org.netbeans.lib.awtextra.AbsoluteConstraints(451, 516, 144, -1));

        btnAnular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminarr.png"))); // NOI18N
        btnAnular.setText("Anular");
        btnAnular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnularActionPerformed(evt);
            }
        });
        getContentPane().add(btnAnular, new org.netbeans.lib.awtextra.AbsoluteConstraints(451, 557, 147, -1));

        btnNuevaOrden.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/NuevaOrden.png"))); // NOI18N
        btnNuevaOrden.setText("Nueva orden");
        btnNuevaOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaOrdenActionPerformed(evt);
            }
        });
        getContentPane().add(btnNuevaOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 516, -1, 30));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/historial-de-transacciones.png"))); // NOI18N
        jButton1.setText("Historial del dia");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 510, -1, -1));

        btnDespachar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pagar.png"))); // NOI18N
        btnDespachar.setText("Despachar");
        btnDespachar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDespacharActionPerformed(evt);
            }
        });
        getContentPane().add(btnDespachar, new org.netbeans.lib.awtextra.AbsoluteConstraints(622, 557, 150, -1));
        getContentPane().add(spnCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 120, 70, -1));

        jPanel2.setBackground(new java.awt.Color(255, 255, 102));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1120, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 620, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1120, 620));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCategoriaActionPerformed

    private void btnProcesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcesarActionPerformed

        if (idOrdenCargada > 0) {
            JOptionPane.showMessageDialog(this,
                    "Ya hay una orden cargada (#" + idOrdenCargada + ").\nUse 'Nueva orden' para crear una diferente.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ordenActual.tieneDetalles()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un producto a la orden.");
            return;
        }

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
        ordenActual.setNombreUsuario(u.toString());
        ordenActual.setIdMesa(mesa.getIdMesa());
        ordenActual.setNumeroMesa(mesa.getNumero());
        ordenActual.setFechaHora(new java.util.Date());
        ordenActual.setEstado("PROCESADA");

        int idOrden = ordenService.registrarOrden(ordenActual);

        if (idOrden > 0) {
            JOptionPane.showMessageDialog(this,
                    "Orden #" + idOrden + " registrada.\nMesa " + mesa.getNumero() + " ocupada.");
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar la orden.",
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

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        this.dispose();
        new frmMenu().setVisible(true);
    }//GEN-LAST:event_btnMenuActionPerformed

    private void btnVerOrdenesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerOrdenesActionPerformed
        new frmOrdenesPendientes(this).setVisible(true);
    }//GEN-LAST:event_btnVerOrdenesActionPerformed

    private void btnDespacharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDespacharActionPerformed
        if (idOrdenCargada < 0) {
            JOptionPane.showMessageDialog(this, "Primero cargue una orden desde 'Ver órdenes'.");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Despachar la Orden #" + idOrdenCargada + "? Se generará el ticket.",
                "Confirmar despacho", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            boolean ok = ordenService.despacharOrden(idOrdenCargada);
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Orden #" + idOrdenCargada + " despachada. Ticket generado.");
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Error al despachar la orden.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnDespacharActionPerformed

    private void btnAnularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnularActionPerformed
        // Solo ADMINISTRADOR puede anular
        String rol = Sesion.getUsuarioActual().getRol();
        if (!rol.equals("ADMINISTRADOR")) {
            JOptionPane.showMessageDialog(this, "Solo el Administrador puede anular órdenes.",
                    "Acceso denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (idOrdenCargada < 0) {
            JOptionPane.showMessageDialog(this, "Primero cargue una orden desde 'Ver órdenes'.");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Anular la Orden #" + idOrdenCargada + "? Esta acción no se puede deshacer.",
                "Confirmar anulación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmar == JOptionPane.YES_OPTION) {
            boolean ok = ordenService.anularOrden(idOrdenCargada);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Orden #" + idOrdenCargada + " anulada.");
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se puede anular. Solo se pueden anular órdenes PROCESADAS o DESPACHADAS del día de hoy.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_btnAnularActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        if (idOrdenCargada < 0) {
            JOptionPane.showMessageDialog(this, "Primero cargue una orden pendiente.");
            return;
        }
        if (!ordenActual.tieneDetalles()) {
            JOptionPane.showMessageDialog(this, "La orden no tiene productos.");
            return;
        }

        boolean ok = ordenService.actualizarOrden(idOrdenCargada, ordenActual);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Orden #" + idOrdenCargada + " actualizada correctamente.");
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar la orden.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalActionPerformed

    private void btnNuevaOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaOrdenActionPerformed
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Descartar cambios y comenzar una nueva orden?",
                "Nueva orden", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            limpiarFormulario();
        }
    }//GEN-LAST:event_btnNuevaOrdenActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new frmHistorialDia().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

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
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnAnular;
    private javax.swing.JButton btnDespachar;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnNuevaOrden;
    private javax.swing.JButton btnProcesar;
    private javax.swing.JButton btnVerOrdenes;
    private javax.swing.JComboBox<Object> cmbCategoria;
    private javax.swing.JComboBox<Mesa> cmbMesa;
    private javax.swing.JComboBox<Item> cmbProudcto_Combo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
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
