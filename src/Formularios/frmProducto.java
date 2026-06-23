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

        txtBuscarPorNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarPorNombre(txtBuscarPorNombre.getText().trim());
            }
        });

        tblMostrarProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tblMostrarProductos.getSelectedRow();
                if (fila < 0) {
                    return;
                }

                javax.swing.table.DefaultTableModel modelo
                        = (javax.swing.table.DefaultTableModel) tblMostrarProductos.getModel();

                String idStr = modelo.getValueAt(fila, 0).toString();
                txtBuscarIDProducto.setText(idStr);
                btnBuscarIDProductoActionPerformed(null); 
            }
        });

        txtIDProducto.setEnabled(false);
        txtNombreProducto.requestFocus();

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Imagenes/tacoSinFondo.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(
                jLabel6.getWidth(),
                jLabel6.getHeight(),
                Image.SCALE_SMOOTH
        );
        jLabel6.setIcon(new ImageIcon(scaledImage));

    }

    private void filtrarPorNombre(String texto) {
        ProductoDAO dao = new ProductoDAO();
        List<Producto> lista = dao.listar();
        javax.swing.table.DefaultTableModel modelo
                = (javax.swing.table.DefaultTableModel) tblMostrarProductos.getModel();
        modelo.setRowCount(0);

        for (Producto p : lista) {
            if (p.getNombreProducto().toLowerCase().contains(texto.toLowerCase())) {
                modelo.addRow(new Object[]{
                    p.getIdProducto(),
                    p.getNombreProducto(),
                    p.getDescripcionProducto(),
                    p.getIdCategoria(),
                    "$" + p.getPrecioProducto()
                });
            }
        }
    }

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
        tblMostrarProductos.setRowHeight(25);
        tblMostrarProductos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblMostrarProductos.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        tblMostrarProductos.getColumnModel().getColumn(1).setPreferredWidth(200);  // Nombre
        tblMostrarProductos.getColumnModel().getColumn(2).setPreferredWidth(450);  // Descripción
        tblMostrarProductos.getColumnModel().getColumn(3).setPreferredWidth(100);  // Categoría
        tblMostrarProductos.getColumnModel().getColumn(4).setPreferredWidth(80);   // Precio
        // Total: 880px ≈ ancho del scroll (891px)
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
        txtBuscarPorNombre.setText("");
        cmbCategoria.setSelectedIndex(0);
        asignarIdAutomatico();
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

        btnBuscarIDProducto = new javax.swing.JButton();
        txtBuscarIDProducto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        pnlFondo = new javax.swing.JPanel();
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
        btnEliminarProducto = new javax.swing.JButton();
        btnActualizarProducto = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtBuscarPorNombre = new javax.swing.JTextField();

        btnBuscarIDProducto.setBackground(new java.awt.Color(255, 102, 102));
        btnBuscarIDProducto.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        btnBuscarIDProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/lupa(1).png"))); // NOI18N
        btnBuscarIDProducto.setText("Buscar");
        btnBuscarIDProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarIDProductoActionPerformed(evt);
            }
        });

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

        jLabel3.setFont(new java.awt.Font("sansserif", 3, 18)); // NOI18N
        jLabel3.setText("Ingrese el ID a buscar:");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Productos");
        setBackground(new java.awt.Color(255, 0, 51));
        setFont(new java.awt.Font("Arundina Sans", 2, 10)); // NOI18N
        setForeground(new java.awt.Color(255, 51, 51));
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlFondo.setBackground(new java.awt.Color(255, 255, 102));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/miLogo.png"))); // NOI18N
        jLabel6.setText("jLabel6");

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("ID del producto:");

        jLabel1.setText("Ingrese el nombre del producto:");

        jLabel2.setText("Seleccione la categoria:");

        jLabel4.setText("Precio del producto:");

        txtIDProducto.setEditable(false);
        txtIDProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDProductoActionPerformed(evt);
            }
        });

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

        cmbCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCategoriaActionPerformed(evt);
            }
        });

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

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/disquete(1).png"))); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/escoba.png"))); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        jLabel7.setText("Descripción:");

        txtDescripcionProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionProductoKeyTyped(evt);
            }
        });

        tblMostrarProductos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblMostrarProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tblMostrarProductos);

        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/menu.png"))); // NOI18N
        btnCerrar.setText("Menu");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        btnEliminarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/eliminar.png"))); // NOI18N
        btnEliminarProducto.setText("Eliminar");
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });

        btnActualizarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/actualizar-flecha.png"))); // NOI18N
        btnActualizarProducto.setText("Actualizar");
        btnActualizarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarProductoActionPerformed(evt);
            }
        });

        jLabel8.setText("Buscar producto por nombre:");

        javax.swing.GroupLayout pnlFondoLayout = new javax.swing.GroupLayout(pnlFondo);
        pnlFondo.setLayout(pnlFondoLayout);
        pnlFondoLayout.setHorizontalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 891, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addComponent(btnActualizarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(btnEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(43, Short.MAX_VALUE))
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFondoLayout.createSequentialGroup()
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtBuscarPorNombre, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlFondoLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(115, 115, 115)
                                .addComponent(txtIDProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlFondoLayout.createSequentialGroup()
                                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel4)
                                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDescripcionProducto)
                                    .addComponent(cmbCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtNombreProducto)
                                    .addGroup(pnlFondoLayout.createSequentialGroup()
                                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))))
        );
        pnlFondoLayout.setVerticalGroup(
            pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoLayout.createSequentialGroup()
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE))
                    .addGroup(pnlFondoLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtIDProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(24, 24, 24)
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(txtBuscarPorNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActualizarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        getContentPane().add(pnlFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 960, 730));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreProductoActionPerformed

    private void txtPrecioProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioProductoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

        ProductoDAO daoe = new ProductoDAO();
        int idAutomatico = daoe.generarIdAutomatico();
        int idActual = Integer.parseInt(txtIDProducto.getText());

        if (idActual != idAutomatico) {
            JOptionPane.showMessageDialog(this,
                    "Hay un producto cargado. Use 'Actualizar' para modificarlo o 'Limpiar' para crear uno nuevo.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
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
            txtIDProducto.setText(p.getIdProducto() + "");
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
        new frmMenu().setVisible(true);
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
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnlFondo;
    private javax.swing.JTable tblMostrarProductos;
    private javax.swing.JTextField txtBuscarIDProducto;
    private javax.swing.JTextField txtBuscarPorNombre;
    private javax.swing.JTextField txtDescripcionProducto;
    private javax.swing.JTextField txtIDProducto;
    private javax.swing.JTextField txtNombreProducto;
    private javax.swing.JTextField txtPrecioProducto;
    // End of variables declaration//GEN-END:variables
}
