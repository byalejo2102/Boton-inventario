package basede;

import confi.Conexion;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class VerAjustes extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public VerAjustes() {
        setTitle("Ajustes de Cierre de Inventario");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Columnas de la tabla
        String[] columnNames = {"Código Producto", "Código Ajuste", "Descripción", "Unidad de Medida", "Fecha", "Saldo Inicial", "Ingresos", "Egresos", "Ajustes", "Saldo Final", "Saldo Físico", "Saldo Total"};

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
        String sql = "SELECT PROCODIGO, AJUCODIGO, PXADESCRIPCION, PXAUNIDADMEDIDA, PXAFECHA, PXASALDOINICIAL, PXAINGRESOS, PXAEGRESOS, PXAAJUSTES, PXASALDOFINAL, PXASALDOFISICO, PXASALDOTOTAL " +
                     "FROM PXA ORDER BY PXAFECHA DESC";

        try (Connection conn = new Conexion().Conexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String procodigo = rs.getString("PROCODIGO");
                String ajucodigo = rs.getString("AJUCODIGO");
                String descripcion = rs.getString("PXADESCRIPCION");
                String unidadMedida = rs.getString("PXAUNIDADMEDIDA");
                Date fecha = rs.getDate("PXAFECHA");
                double saldoInicial = rs.getDouble("PXASALDOINICIAL");
                double ingresos = rs.getDouble("PXAINGRESOS");
                double egresos = rs.getDouble("PXAEGRESOS");
                double ajustes = rs.getDouble("PXAAJUSTES");
                double saldoFinal = rs.getDouble("PXASALDOFINAL");
                double saldoFisico = rs.getDouble("PXASALDOFISICO");
                double saldoTotal = rs.getDouble("PXASALDOTOTAL");

                Object[] row = {procodigo, ajucodigo, descripcion, unidadMedida, fecha, saldoInicial, ingresos, egresos, ajustes, saldoFinal, saldoFisico, saldoTotal};
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos de los ajustes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VerAjustes());
    }
}
