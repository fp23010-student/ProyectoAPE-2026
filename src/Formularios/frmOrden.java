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
                new String[]{"#", "Descripción", "Precio Unit.", "Cantidad", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; 
            }
        };
        tblDetalle.setModel(modelo);
        tblDetalle.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        tblDetalle.getColumnModel().getColumn(0).setPreferredWidth(30);
        tblDetalle.getColumnModel().getColumn(1).setPreferredWidth(160);
        tblDetalle.getColumnModel().getColumn(2).setPreferredWidth(70);
        tblDetalle.getColumnModel().getColumn(3).setPreferredWidth(50);
        tblDetalle.getColumnModel().getColumn(4).setPreferredWidth(70);

       
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

        
        modelo.addTableModelListener(e -> {
            if (actualizandoTabla) {
                return; 
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
        btnMenu = new javax.swing.JButton();
        btnVerOrdenes = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnPagar = new javax.swing.JButton();

        jLabel9.setText("Ordenes Pendientes:");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ordenes");

        lblLogo.setText("jLabel1");

        jLabel1.setText("ID de Orden:");

        jLabel2.setText("Fecha y Hora:");

        jLabel3.setText("Usuario:");

        txtIDOrdeen.setEditable(false);
        txtIDOrdeen.setEnabled(false);

        txtFechaHora.setEditable(false);
        txtFechaHora.setEnabled(false);

        txtUsuario.setEditable(false);
        txtUsuario.setEnabled(false);

        jLabel4.setText("Categoria:");

        cmbCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCategoriaActionPerformed(evt);
            }
        });

        jLabel5.setText("Producto/Combo:");

        jLabel6.setText("Cantidad:");

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/agregar.png"))); // NOI18N
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

        btnProcesar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/procesar.png"))); // NOI18N
        btnProcesar.setText("Procesar");
        btnProcesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcesarActionPerformed(evt);
            }
        });

        jLabel7.setText("Total $:");

        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });

        jLabel8.setText("Mesa:");

        btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/menu.png"))); // NOI18N
        btnMenu.setText("Menu");
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });

        btnVerOrdenes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/ver.png"))); // NOI18N
        btnVerOrdenes.setText("Ver odenes");
        btnVerOrdenes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerOrdenesActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminarr.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar.png"))); // NOI18N
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnPagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/pagar.png"))); // NOI18N
        btnPagar.setText("Pagar");
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIDOrdeen, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFechaHora, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(81, 81, 81)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(spnCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(cmbProudcto_Combo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(cmbCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1050, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addComponent(cmbMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(283, 283, 283)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(btnPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnVerOrdenes, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(247, 247, 247)
                                .addComponent(btnMenu))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addComponent(btnProcesar)
                                .addGap(23, 23, 23)
                                .addComponent(jLabel7)
                                .addGap(15, 15, 15)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(35, 35, 35))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel1)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel2)
                        .addGap(34, 34, 34)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtIDOrdeen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(txtFechaHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel4)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel5))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(cmbProudcto_Combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spnCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(btnAgregar))))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel7))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnVerOrdenes)
                            .addComponent(btnActualizar)
                            .addComponent(btnProcesar))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEliminar)
                    .addComponent(btnPagar)
                    .addComponent(btnMenu))
                .addGap(7, 7, 7))
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
        ordenActual.setNombreUsuario(u.toString());
        ordenActual.setIdMesa(mesa.getIdMesa());
        ordenActual.setNumeroMesa(mesa.getNumero());
        ordenActual.setFechaHora(new java.util.Date());
        ordenActual.setEstado("PENDIENTE");

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

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed
        if (idOrdenCargada < 0) {
            JOptionPane.showMessageDialog(this, "Primero cargue una orden pendiente.");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Confirmar pago de la Orden #" + idOrdenCargada + "?",
                "Confirmar pago", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            boolean ok = ordenService.pagarOrden(idOrdenCargada);
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Orden #" + idOrdenCargada + " pagada. Ticket generado.");
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Error al procesar el pago.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnPagarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        if (idOrdenCargada < 0) {
            JOptionPane.showMessageDialog(this, "Primero cargue una orden pendiente.");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Cancelar la Orden #" + idOrdenCargada + "? Esta acción no se puede deshacer.",
                "Confirmar cancelación", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            boolean ok = ordenService.cancelarOrden(idOrdenCargada);
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Orden #" + idOrdenCargada + " cancelada. Mesa liberada.");
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Error al cancelar la orden.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_btnEliminarActionPerformed

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
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar la orden.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalActionPerformed

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
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnPagar;
    private javax.swing.JButton btnProcesar;
    private javax.swing.JButton btnVerOrdenes;
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
    private javax.swing.JLabel jLabel9;
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
