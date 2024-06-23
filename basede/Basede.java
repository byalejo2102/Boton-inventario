package basede;

import confi.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.sql.*;
import java.sql.ResultSet;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Basede extends JFrame {
    // Componentes de la interfaz
    private JTextField textCodigo, textDescripcion, textSaldoInicial, textIngresos, textEgresos, textAjustes, textCostoUM;
    private JComboBox<String> comboUnidad, comboStatus;
    private JButton btnInsertar, btnActualizar, btnEliminar, btnSeleccionar, btnSubirFoto, btnMostrarTabla, btnCierreInventario, btnVerAjustes;
    private JLabel labelError, labelFoto;
    private JPanel panel;
    private byte[] fotoProducto;

    public Basede() {
        setTitle("Gestión de Productos");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel principal con fondo
        panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255)); // Color de fondo del panel
        add(panel);

        // Componentes con etiquetas y campos
        JLabel lblCodigo = new JLabel("Código:");
        JLabel lblDescripcion = new JLabel("Descripción:");
        JLabel lblUnidadMedida = new JLabel("Unidad de Medida:");
        JLabel lblSaldoInicial = new JLabel("Saldo Inicial:");
        JLabel lblIngresos = new JLabel("Ingresos:");
        JLabel lblEgresos = new JLabel("Egresos:");
        JLabel lblAjustes = new JLabel("Ajustes:");
        JLabel lblCostoUM = new JLabel("Costo UM:");
        JLabel lblEstado = new JLabel("Estado:");
        JLabel lblFoto = new JLabel("Foto:");

        textCodigo = new JTextField(10);
        textDescripcion = new JTextField(20);
        comboUnidad = new JComboBox<>(new String[]{"kg", "gr", "lt", "ml", "un", "m", "cm", "mm"});
        textSaldoInicial = new JTextField(10);
        textIngresos = new JTextField(10);
        textEgresos = new JTextField(10);
        textAjustes = new JTextField(10);
        textCostoUM = new JTextField(10);
        textCostoUM.setEditable(false); // Bloquear este campo
        comboStatus = new JComboBox<>(new String[]{"ACT", "INA", "DIS", "ESC"});
        labelFoto = new JLabel("No hay foto seleccionada");
        btnSubirFoto = new JButton("Subir Foto");

        // Botones
        btnInsertar = new JButton("Insertar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnSeleccionar = new JButton("Seleccionar");
        btnMostrarTabla = new JButton("Mostrar Tabla");
        btnCierreInventario = new JButton("Cierre Inventario");
        btnVerAjustes = new JButton("Ver Ajustes");

        setupButton(btnInsertar);
        setupButton(btnActualizar);
        setupButton(btnEliminar);
        setupButton(btnSeleccionar);
        setupButton(btnSubirFoto);
        setupButton(btnMostrarTabla);
        setupButton(btnCierreInventario);
        setupButton(btnVerAjustes);

        // Añadir acciones a los botones
        btnInsertar.addActionListener(this::insertAction);
        btnActualizar.addActionListener(this::updateAction);
        btnEliminar.addActionListener(this::deleteAction);
        btnSeleccionar.addActionListener(this::readAction);
        btnSubirFoto.addActionListener(this::subirFotoAction);
        btnMostrarTabla.addActionListener(e -> new TablaProductos());
        btnCierreInventario.addActionListener(e -> new CierreInventario());
        btnVerAjustes.addActionListener(e -> new VerAjustes());

        // Configuración del GroupLayout
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Definir el grupo horizontal
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblCodigo)
                    .addComponent(lblDescripcion)
                    .addComponent(lblUnidadMedida)
                    .addComponent(lblSaldoInicial)
                    .addComponent(lblIngresos)
                    .addComponent(lblEgresos)
                    .addComponent(lblAjustes)
                    .addComponent(lblCostoUM)
                    .addComponent(lblEstado)
                    .addComponent(lblFoto)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(textCodigo)
                    .addComponent(textDescripcion)
                    .addComponent(comboUnidad)
                    .addComponent(textSaldoInicial)
                    .addComponent(textIngresos)
                    .addComponent(textEgresos)
                    .addComponent(textAjustes)
                    .addComponent(textCostoUM)
                    .addComponent(comboStatus)
                    .addComponent(labelFoto)
                    .addComponent(btnSubirFoto)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnInsertar)
                        .addComponent(btnActualizar)
                        .addComponent(btnEliminar)
                        .addComponent(btnSeleccionar)
                    )
                    .addComponent(btnMostrarTabla)
                    .addComponent(btnCierreInventario)
                    .addComponent(btnVerAjustes)
                )
        );

        // Definir el grupo vertical
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCodigo)
                    .addComponent(textCodigo)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDescripcion)
                    .addComponent(textDescripcion)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUnidadMedida)
                    .addComponent(comboUnidad)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSaldoInicial)
                    .addComponent(textSaldoInicial)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIngresos)
                    .addComponent(textIngresos)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEgresos)
                    .addComponent(textEgresos)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAjustes)
                    .addComponent(textAjustes)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCostoUM)
                    .addComponent(textCostoUM)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEstado)
                    .addComponent(comboStatus)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFoto)
                    .addComponent(labelFoto)
                )
                .addComponent(btnSubirFoto)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInsertar)
                    .addComponent(btnActualizar)
                    .addComponent(btnEliminar)
                    .addComponent(btnSeleccionar)
                )
                .addComponent(btnMostrarTabla)
                .addComponent(btnCierreInventario)
                .addComponent(btnVerAjustes)
        );

        setVisible(true);
    }

    private void setupButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        button.setFocusPainted(false);
    }

    private void subirFotoAction(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            labelFoto.setText(file.getName());
            try {
                fotoProducto = Files.readAllBytes(file.toPath());
            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo de imagen.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void insertAction(ActionEvent e) {
        String codigo = textCodigo.getText();
        String descripcion = textDescripcion.getText();
        String unidadMedida = (String) comboUnidad.getSelectedItem();
        double saldoInicial, ingresos, egresos, ajustes, costoUM, precioUM, saldoFinal;

        try {
            saldoInicial = Double.parseDouble(textSaldoInicial.getText().isEmpty() ? "0" : textSaldoInicial.getText());
            ingresos = Double.parseDouble(textIngresos.getText().isEmpty() ? "0" : textIngresos.getText());
            egresos = Double.parseDouble(textEgresos.getText().isEmpty() ? "0" : textEgresos.getText());
            ajustes = Double.parseDouble(textAjustes.getText().isEmpty() ? "0" : textAjustes.getText());
            costoUM = Double.parseDouble(textCostoUM.getText().isEmpty() ? "0" : textCostoUM.getText());
            precioUM = 0; // Establecer un valor predeterminado o calcularlo si es necesario
            saldoFinal = saldoInicial + ingresos - egresos + ajustes; // Calcula el saldo final

            // Validaciones adicionales
            if (saldoInicial < 0 || ingresos < 0 || egresos < 0 || ajustes < 0 || saldoFinal < 0 || costoUM < 0 || precioUM < 0) {
                JOptionPane.showMessageDialog(this, "Los valores numéricos no pueden ser negativos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese números válidos en los campos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String estado = (String) comboStatus.getSelectedItem();

        String sql = "INSERT INTO PRODUCTOS (PROCODIGO, PRODESCRIPCION, PROUNIDADMEDIDA, PROSALDOINICIAL, PROINGRESOS, PROEGRESOS, PROAJUSTES, PROSALDOFINAL, PROCOSTOUM, PROPRECIOUM, PROSTATUS, PROFOTO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new Conexion().Conexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            pstmt.setString(2, descripcion);
            pstmt.setString(3, unidadMedida);
            pstmt.setDouble(4, saldoInicial);
            pstmt.setDouble(5, ingresos);
            pstmt.setDouble(6, egresos);
            pstmt.setDouble(7, ajustes);
            pstmt.setDouble(8, saldoFinal);
            pstmt.setDouble(9, costoUM);
            pstmt.setDouble(10, precioUM);
            pstmt.setString(11, estado);
            pstmt.setBytes(12, fotoProducto);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Producto insertado exitosamente!");
            } else {
                JOptionPane.showMessageDialog(this, "Error al insertar producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al insertar producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAction(ActionEvent e) {
        String codigo = textCodigo.getText();
        String descripcion = textDescripcion.getText();
        String unidadMedida = (String) comboUnidad.getSelectedItem();
        double saldoInicial, ingresos, egresos, ajustes, costoUM, precioUM, saldoFinal;

        try {
            saldoInicial = Double.parseDouble(textSaldoInicial.getText().isEmpty() ? "0" : textSaldoInicial.getText());
            ingresos = Double.parseDouble(textIngresos.getText().isEmpty() ? "0" : textIngresos.getText());
            egresos = Double.parseDouble(textEgresos.getText().isEmpty() ? "0" : textEgresos.getText());
            ajustes = Double.parseDouble(textAjustes.getText().isEmpty() ? "0" : textAjustes.getText());
            costoUM = Double.parseDouble(textCostoUM.getText().isEmpty() ? "0" : textCostoUM.getText());
            precioUM = 0; // Establecer un valor predeterminado o calcularlo si es necesario
            saldoFinal = saldoInicial + ingresos - egresos + ajustes; // Calcula el saldo final
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese números válidos en los campos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String estado = (String) comboStatus.getSelectedItem();

        String sql = "UPDATE PRODUCTOS SET PRODESCRIPCION=?, PROUNIDADMEDIDA=?, PROSALDOINICIAL=?, PROINGRESOS=?, PROEGRESOS=?, PROAJUSTES=?, PROSALDOFINAL=?, PROCOSTOUM=?, PROPRECIOUM=?, PROSTATUS=?, PROFOTO=? WHERE PROCODIGO=?";
        try (Connection conn = new Conexion().Conexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, descripcion);
            pstmt.setString(2, unidadMedida);
            pstmt.setDouble(3, saldoInicial);
            pstmt.setDouble(4, ingresos);
            pstmt.setDouble(5, egresos);
            pstmt.setDouble(6, ajustes);
            pstmt.setDouble(7, saldoFinal);
            pstmt.setDouble(8, costoUM);
            pstmt.setDouble(9, precioUM);
            pstmt.setString(10, estado);
            pstmt.setBytes(11, fotoProducto);
            pstmt.setString(12, codigo);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente!");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAction(ActionEvent e) {
    String codigo = textCodigo.getText();
    String deletePXA = "DELETE FROM PXA WHERE PROCODIGO=?";
    String deleteProducto = "DELETE FROM PRODUCTOS WHERE PROCODIGO=?";
    
    try (Connection conn = new Conexion().Conexion()) {
        conn.setAutoCommit(false);
        
        try (PreparedStatement pstmtPXA = conn.prepareStatement(deletePXA);
             PreparedStatement pstmtProducto = conn.prepareStatement(deleteProducto)) {
            
            pstmtPXA.setString(1, codigo);
            pstmtPXA.executeUpdate();
            
            pstmtProducto.setString(1, codigo);
            int result = pstmtProducto.executeUpdate();
            
            if (result > 0) {
                conn.commit();
                JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente!");
            } else {
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Error al eliminar producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            conn.rollback();
            JOptionPane.showMessageDialog(this, "Error al eliminar producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void readAction(ActionEvent e) {
        String codigo = textCodigo.getText();
        String sql = "SELECT * FROM PRODUCTOS WHERE PROCODIGO=?";
        try (Connection conn = new Conexion().Conexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                textDescripcion.setText(rs.getString("PRODESCRIPCION"));
                comboUnidad.setSelectedItem(rs.getString("PROUNIDADMEDIDA"));
                textSaldoInicial.setText(rs.getString("PROSALDOINICIAL"));
                textIngresos.setText(rs.getString("PROINGRESOS"));
                textEgresos.setText(rs.getString("PROEGRESOS"));
                textAjustes.setText(rs.getString("PROAJUSTES"));
                textCostoUM.setText(rs.getString("PROCOSTOUM"));
                comboStatus.setSelectedItem(rs.getString("PROSTATUS"));
                fotoProducto = rs.getBytes("PROFOTO");
                if (fotoProducto != null) {
                    labelFoto.setText("Foto seleccionada");
                } else {
                    labelFoto.setText("No hay foto seleccionada");
                }
                JOptionPane.showMessageDialog(this, "Datos del producto cargados.");
            } else {
                JOptionPane.showMessageDialog(this, "Producto no encontrado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer información del producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Basede());
    }
}

class TablaProductos extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public TablaProductos() {
        setTitle("Tabla de Productos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Columnas de la tabla
        String[] columnNames = {"Código", "Descripción", "Unidad de Medida", "Saldo Inicial", "Ingresos", "Egresos", "Ajustes", "Saldo Final", "Costo UM", "Estado"};

        // Modelo de tabla
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Cargar datos desde la base de datos
        cargarDatos();

        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private void cargarDatos() {
        String sql = "SELECT PROCODIGO, PRODESCRIPCION, PROUNIDADMEDIDA, PROSALDOINICIAL, PROINGRESOS, PROEGRESOS, PROAJUSTES, PROSALDOFINAL, PROCOSTOUM, PROSTATUS FROM PRODUCTOS";
        try (Connection conn = new Conexion().Conexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String codigo = rs.getString("PROCODIGO");
                String descripcion = rs.getString("PRODESCRIPCION");
                String unidadMedida = rs.getString("PROUNIDADMEDIDA");
                double saldoInicial = rs.getDouble("PROSALDOINICIAL");
                double ingresos = rs.getDouble("PROINGRESOS");
                double egresos = rs.getDouble("PROEGRESOS");
                double ajustes = rs.getDouble("PROAJUSTES");
                double saldoFinal = rs.getDouble("PROSALDOFINAL");
                double costoUM = rs.getDouble("PROCOSTOUM");
                String estado = rs.getString("PROSTATUS");

                Object[] row = {codigo, descripcion, unidadMedida, saldoInicial, ingresos, egresos, ajustes, saldoFinal, costoUM, estado};
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos de los productos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TablaProductos());
    }
}
