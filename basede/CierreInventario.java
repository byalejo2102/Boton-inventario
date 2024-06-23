package basede;

import confi.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class CierreInventario {

    public CierreInventario() {
        int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea realizar el cierre de inventario?", "Confirmación de cierre de inventario", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            realizarCierreInventario();
        } else {
            JOptionPane.showMessageDialog(null, "Cierre de inventario cancelado.");
        }
    }

    private void realizarCierreInventario() {
    Connection conn = null;
    PreparedStatement pstmtInsertAjuste = null;
    PreparedStatement pstmtInsertPXA = null;

    String insertAjusteSQL = "INSERT INTO AJUSTES (AJUCODIGO, AJUFECHA, AJUDESCRIPCION) VALUES (?, CURRENT_DATE, ?)";
    String insertPXASQL = "INSERT INTO PXA (PROCODIGO, AJUCODIGO, PXADESCRIPCION, PXAUNIDADMEDIDA, PXAFECHA, PXASALDOINICIAL, PXAINGRESOS, PXAEGRESOS, PXAAJUSTES, PXASALDOFINAL, PXASALDOFISICO, PXASALDOTOTAL) " +
                          "SELECT p.PROCODIGO, ?, p.PRODESCRIPCION, p.PROUNIDADMEDIDA, CURRENT_DATE, p.PROSALDOINICIAL, p.PROINGRESOS, p.PROEGRESOS, p.PROAJUSTES, p.PROSALDOFINAL, COALESCE(p.PROSALDOFISICO, 0), (p.PROSALDOFINAL - COALESCE(p.PROSALDOFISICO, 0)) " +
                          "FROM PRODUCTOS p";

    try {
        conn = new Conexion().Conexion();
        conn.setAutoCommit(false); // Start transaction

        // Generate a unique code for the new adjustment
        String ajusteCodigo = "AJ-" + String.format("%03d", System.currentTimeMillis() % 1000);

        // Insert a new record into the AJUSTES table
        pstmtInsertAjuste = conn.prepareStatement(insertAjusteSQL);
        pstmtInsertAjuste.setString(1, ajusteCodigo);
        pstmtInsertAjuste.setString(2, "Cierre de inventario");
        pstmtInsertAjuste.executeUpdate();

        // Insert records into the PXA table using the new adjustment code
        pstmtInsertPXA = conn.prepareStatement(insertPXASQL);
        pstmtInsertPXA.setString(1, ajusteCodigo);
        int result = pstmtInsertPXA.executeUpdate();

        conn.commit(); // Commit transaction

        if (result > 0) {
            JOptionPane.showMessageDialog(null, "Cierre de inventario realizado exitosamente!");
        } else {
            JOptionPane.showMessageDialog(null, "Error al realizar el cierre de inventario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        if (conn != null) {
            try {
                conn.rollback(); // Rollback transaction on error
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al realizar rollback: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        JOptionPane.showMessageDialog(null, "Error al realizar el cierre de inventario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (pstmtInsertAjuste != null) pstmtInsertAjuste.close();
            if (pstmtInsertPXA != null) pstmtInsertPXA.close();
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cerrar recursos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

}
