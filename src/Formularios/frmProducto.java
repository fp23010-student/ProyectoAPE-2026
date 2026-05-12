package Formularios;

import Clases.Categoria;
import Clases.CategoriaDAO;
import Clases.Producto;
import Clases.ProductoDAO;
import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class frmProducto extends javax.swing.JFrame {

    public frmProducto() {
        initComponents();
        setIconImage(new ImageIcon(getClass().getResource("/Imagenes/miLogo.png")).getImage());
        this.setLocationRelativeTo(null);
        llenarCategorias();
        asignarIdAutomatico();
        configurarTabla();
        mostrarEnTabla();
        txtIDProducto.setEnabled(false);
        txtNombreProducto.requestFocus();
        
                ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Imagenes/tacoSinFondo.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(
                jLabel6.getWidth(),
                jLabel6.getHeight(),
                Image.SCALE_SMOOTH
        );
        jLabel6.setIcon(new ImageIcon(scaledImage));

        // Anchos de columnas
        tblMostrarProductos.getColumnModel().getColumn(0).setPreferredWidth(30);
        tblMostrarProductos.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblMostrarProductos.getColumnModel().getColumn(2).setPreferredWidth(290);
        tblMostrarProductos.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblMostrarProductos.getColumnModel().getColumn(4).setPreferredWidth(55);

        // Encabezado
        tblMostrarProductos.getTableHeader().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
        tblMostrarProductos.getTableHeader().setOpaque(false);
        tblMostrarProductos.getTableHeader().setBackground(new java.awt.Color(0, 51, 0));
        tblMostrarProductos.getTableHeader().setForeground(java.awt.Color.BLACK);
    }

    //Metodo para configurar la tabla
    private void configurarTabla() {
        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Descripción", "Categoría", "Precio"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMostrarProductos.setModel(modelo);

        //Anchos
        tblMostrarProductos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblMostrarProductos.getColumnModel().getColumn(1).setPreferredWidth(180);

        //Encabezado profesional
        javax.swing.table.JTableHeader header = tblMostrarProductos.getTableHeader();
        header.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 14));
        header.setBackground(new java.awt.Color(51, 51, 51));
        header.setForeground(java.awt.Color.BLACK);

        // Altura de filas
        tblMostrarProductos.setRowHeight(25);
    }

    //Metodo para mostrar la tabla
    private void mostrarEnTabla() {
        ProductoDAO dao = new ProductoDAO();
        List<Producto> lista = dao.listar();
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) tblMostrarProductos.getModel();

        modelo.setRowCount(0);

        for (Producto p : lista) {
            Object[] fila = new Object[5];
            fila[0] = p.getIdProducto();
            fila[1] = p.getNombreProducto();
            fila[2] = p.getDescripcionProducto();
            fila[3] = p.getIdCategoria();
            fila[4] = "$" + p.getPrecioProducto();
            modelo.addRow(fila);
        }
    }

    //Metodo para llenar el cmb de las categorias
    private void llenarCategorias() {
        CategoriaDAO dao = new CategoriaDAO();
        cmbCategoria.removeAllItems();

        Categoria placeholder = new Categoria("00", "Seleccione una categoría");
        cmbCategoria.addItem(placeholder);

        for (Categoria cat : dao.listar()) {
            cmbCategoria.addItem(cat);
        }
        cmbCategoria.setSelectedIndex(0);
    }

    private void limpiarCampos() {
        txtNombreProducto.setText("");
        txtPrecioProducto.setText("");
        txtBuscarIDProducto.setText("");
        txtDescripcionProducto.setText("");
        cmbCategoria.setSelectedIndex(0);
        asignarIdAutomatico();
    }

    // Método extra para que nombres largos no rompan las columnas
    private String cortarTexto(String texto, int max) {
        if (texto.length() > max) {
            return texto.substring(0, max - 3) + "...";
        }
        return texto;
    }

    //Metodo para asignar un ID correlativo
    private void asignarIdAutomatico() {
        ProductoDAO dao = new ProductoDAO();
        int siguienteId = dao.generarIdAutomatico();
        txtIDProducto.setText(String.format("%03d", siguienteId));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFondo = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtBuscarIDProducto = new javax.swing.JTextField();
        btnBuscarIDProducto = new javax.swing.JButton();
        btnActualizarProducto = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtIDProducto = new javax.swing.JTextField();
        txtNombreProducto = new javax.swing.JTextField();
        cmbCategoria = new javax.swing.JComboBox<>();
        txtPrecioProducto = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtDescripcionProducto = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblMostrarProductos = new javax.swing.JTable();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Productos");
        setBackground(new java.awt.Color(255, 0, 51));
        setFont(new java.awt.Font("Arundina Sans", 2, 10)); // NOI18N
        setForeground(new java.awt.Color(255, 51, 51));
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlFondo.setBackground(new java.awt.Color(255, 204, 255));

        jLabel3.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        jLabel3.setText("Ingrese el ID a buscar:");

        txtBuscarIDProducto.setBackground(new java.awt.Color(255, 153, 51));
        txtBuscarIDProducto.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        txtBuscarIDProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarIDProductoActionPerformed(evt);
            }
        });
        txtBuscarIDProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarIDProductoKeyTyped(evt);
            }
        });

        btnBuscarIDProducto.setBackground(new java.awt.Color(255, 102, 102));
        btnBuscarIDProducto.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        btnBuscarIDProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa(1).png"))); // NOI18N
        btnBuscarIDProducto.setText("Buscar");
        btnBuscarIDProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarIDProductoActionPerformed(evt);
            }
        });

        btnActualizarProducto.setBackground(new java.awt.Color(255, 102, 102));
        btnActualizarProducto.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        btnActualizarProducto.setForeground(new java.awt.Color(0, 0, 0));
        btnActualizarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar-flecha.png"))); // NOI18N
        btnActualizarProducto.setText("Actualizar");
        btnActualizarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarProductoActionPerformed(evt);
            }
        });

        btnEliminarProducto.setBackground(new java.awt.Color(255, 102, 102));
        btnEliminarProducto.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        btnEliminarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnEliminarProducto.setText("Eliminar");
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/miLogo.png"))); // NOI18N
        jLabel6.setText("jLabel6");

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        jLabel5.setText("ID del producto:");

        jLabel1.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        jLabel1.setText("Ingrese el nombre del producto:");

        jLabel2.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        jLabel2.setText("Seleccione la categoria:");

        jLabel4.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        jLabel4.setText("Precio del producto:");

        txtIDProducto.setEditable(false);
        txtIDProducto.setBackground(new java.awt.Color(255, 153, 0));
        txtIDProducto.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        txtIDProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDProductoActionPerformed(evt);
            }
        });

        txtNombreProducto.setBackground(new java.awt.Color(255, 153, 51));
        txtNombreProducto.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        txtNombreProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreProductoActionPerformed(evt);
            }
        });
        txtNombreProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreProductoKeyTyped(evt);
            }
        });

        cmbCategoria.setBackground(new java.awt.Color(255, 153, 51));
        cmbCategoria.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        cmbCategoria.setForeground(new java.awt.Color(255, 255, 51));
        cmbCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCategoriaActionPerformed(evt);
            }
        });

        txtPrecioProducto.setBackground(new java.awt.Color(255, 153, 0));
        txtPrecioProducto.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        txtPrecioProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioProductoActionPerformed(evt);
            }
        });
        txtPrecioProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioProductoKeyTyped(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(255, 102, 102));
        btnGuardar.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disquete(1).png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnLimpiar.setBackground(new java.awt.Color(255, 102, 102));
        btnLimpiar.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/escoba.png"))); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        jLabel7.setText("Descripción:");

        txtDescripcionProducto.setBackground(new java.awt.Color(255, 153, 51));
        txtDescripcionProducto.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        txtDescripcionProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionProductoKeyTyped(evt);
            }
        });

        tblMostrarProductos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblMostrarProductos.setFont(new java.awt.Font("sansserif", 3, 13)); // NOI18N
        tblMostrarProductos.setForeground(new java.awt.Color(0, 0, 0));
        tblMostrarProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tblMostrarProductos);

        btnCerrar.setBackground(new java.awt.Color(255, 102, 102));
        btnCerrar.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/x(2).png"))); // NOI18N
        btnCerrar.setText("Cerrrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFondoLayout = new javax.swing.GroupLayout(pnlFondo);
        pnlFondo.setLayout(pnlFondoLayout);
        pnlFondoLayout.setHorizontalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtBuscarIDProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnActualizarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(btnEliminarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnBuscarIDProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCerrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(58, 58, 58))
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43)
                                .addComponent(btnLimpiar)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4))
                                .addGap(14, 14, 14)
                                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNombreProducto)
                                    .addComponent(cmbCategoria, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtDescripcionProducto)
                                    .addGroup(pnlFondoLayout.createSequentialGroup()
                                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtIDProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 167, Short.MAX_VALUE)))))
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlFondoLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6))))
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFondoLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtIDProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addComponent(txtBuscarIDProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscarIDProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnActualizarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCerrar))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(185, 185, 185))
        );

        getContentPane().add(pnlFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 860, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreProductoActionPerformed

    private void txtPrecioProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioProductoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

        Categoria seleccionada = (Categoria) cmbCategoria.getSelectedItem();

        if (txtNombreProducto.getText().isEmpty() || txtPrecioProducto.getText().isEmpty()
                || seleccionada.getIdCategoria().equals("00")) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos obligatorios.");
            return;
        }

        if (txtPrecioProducto.getText().equals("0")) {
            JOptionPane.showMessageDialog(this, "El precio no puede ser cero");
            return;
        }
        int id = Integer.parseInt(txtIDProducto.getText());
        Producto p = new Producto(
                id,
                txtNombreProducto.getText(),
                txtPrecioProducto.getText(),
                seleccionada.getIdCategoria(), // El ID numérico (ej: "01")
                seleccionada.getDescripcion(), // <--- AGREGA ESTO (El nombre del combo)
                txtDescripcionProducto.getText()
        );

        ProductoDAO dao = new ProductoDAO();
        if (dao.insertar(p)) {
            JOptionPane.showMessageDialog(this, "¡Producto agregado con éxito!");
            limpiarCampos();
            mostrarEnTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al intentar guardar.");
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        String idStr = txtBuscarIDProducto.getText();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero busque un producto para eliminar.");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            ProductoDAO dao = new ProductoDAO();
            if (dao.eliminar(Integer.parseInt(idStr))) {
                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
                limpiarCampos();
                mostrarEnTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el producto.");
            }
        }
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    private void btnBuscarIDProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarIDProductoActionPerformed
        String idABuscar = txtBuscarIDProducto.getText();
        if (idABuscar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID para buscar.");
            return;
        }
        ProductoDAO dao = new ProductoDAO();
        Producto p = dao.buscar(Integer.parseInt(idABuscar));
        if (p != null) {
            txtIDProducto.setText(p.getIdProducto()+"");
            txtNombreProducto.setText(p.getNombreProducto());
            txtPrecioProducto.setText(p.getPrecioProducto());
            txtDescripcionProducto.setText(p.getDescripcionProducto());
            for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
                Categoria item = (Categoria) cmbCategoria.getItemAt(i);
                if (item != null && item.getIdCategoria().equals(p.getIdCategoria())) {
                    cmbCategoria.setSelectedIndex(i);
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Producto no encontrado.");
            txtBuscarIDProducto.setText("");
        }
    }//GEN-LAST:event_btnBuscarIDProductoActionPerformed

    private void btnActualizarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarProductoActionPerformed

        Categoria seleccionada = (Categoria) cmbCategoria.getSelectedItem();

        if (txtIDProducto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe buscar el producto por ID primero.");
            return;
        }

        if (txtNombreProducto.getText().isEmpty() || txtPrecioProducto.getText().isEmpty()
                || seleccionada == null || seleccionada.getIdCategoria().equals("00")) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos antes de actualizar.");
            return;
        }

        if (txtPrecioProducto.getText().equals("0")) {
            JOptionPane.showMessageDialog(this, "El precio no puede ser cero");
            return;
        }
        int id = Integer.parseInt(txtIDProducto.getText());
        Producto p = new Producto(
                id,
                txtNombreProducto.getText(),
                txtPrecioProducto.getText(),
                seleccionada.getIdCategoria(), // El ID numérico (ej: "01")
                seleccionada.getDescripcion(), // <--- AGREGA ESTO (El nombre del combo)
                txtDescripcionProducto.getText()
        );

        ProductoDAO dao = new ProductoDAO();
        if (dao.actualizar(p)) {
            JOptionPane.showMessageDialog(this, "¡Producto actualizado con éxito!");
            limpiarCampos();
            mostrarEnTabla();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el producto en la base de datos.");
        }
    }//GEN-LAST:event_btnActualizarProductoActionPerformed

    private void txtPrecioProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioProductoKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || c == '.' || c == java.awt.event.KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        }
        if (c == '.' && txtPrecioProducto.getText().contains(".")) {
            evt.consume();
        }
    }//GEN-LAST:event_txtPrecioProductoKeyTyped

    private void cmbCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCategoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCategoriaActionPerformed

    private void txtBuscarIDProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarIDProductoKeyTyped
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtBuscarIDProductoKeyTyped

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void txtBuscarIDProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarIDProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarIDProductoActionPerformed

    private void txtIDProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDProductoActionPerformed

    private void txtDescripcionProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionProductoKeyTyped

    }//GEN-LAST:event_txtDescripcionProductoKeyTyped

    private void txtNombreProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreProductoKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isLetter(c) || c == java.awt.event.KeyEvent.VK_SPACE || c == java.awt.event.KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNombreProductoKeyTyped

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
            java.util.logging.Logger.getLogger(frmProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmProducto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarProducto;
    private javax.swing.JButton btnBuscarIDProducto;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox<Clases.Categoria> cmbCategoria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnlFondo;
    private javax.swing.JTable tblMostrarProductos;
    private javax.swing.JTextField txtBuscarIDProducto;
    private javax.swing.JTextField txtDescripcionProducto;
    private javax.swing.JTextField txtIDProducto;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtPrecioProducto;
    // End of variables declaration//GEN-END:variables
}
