package confi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    Connection con;
    String ur = "jdbc:postgresql://localhost/Inventario";
    String uss = "postgres";
    String pas = "corina2102";

    public Connection Conexion() {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(ur, uss, pas);
            System.out.println("Conexión exitosa");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al conectarse a la BD " + e);
        }
        return con;
    }

    public static void main(String[] args) {
        Conexion db = new Conexion();
        db.Conexion();
    }
}
