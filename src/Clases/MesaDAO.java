package Clases;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MesaDAO {
    // Trae solo las mesas LIBRES para el combo al abrir la orden
    public ArrayList<Mesa> listarMesasLibres() {
        ArrayList<Mesa> lista = new ArrayList<>();
        try (Connection cn = Conexion.conectar()) {
            String sql = "SELECT idMesa, numero, capacidad, estado "
                    + "FROM mesa WHERE estado = 'LIBRE' ORDER BY numero";
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Mesa m = new Mesa();
                m.setIdMesa(rs.getInt("idMesa"));
                m.setNumero(rs.getInt("numero"));
                m.setCapacidad(rs.getInt("capacidad"));
                m.setEstado(rs.getString("estado"));
                lista.add(m);
            }
        } catch (Exception e) {
            System.out.println("Error listarMesasLibres: " + e.getMessage());
        }
        return lista;
    }

    // Cambia el estado: "OCUPADA" al procesar, "LIBRE" al cerrar ticket
    public boolean actualizarEstado(int idMesa, String estado) {
        try (Connection cn = Conexion.conectar()) {
            String sql = "UPDATE mesa SET estado = ? WHERE idMesa = ?";
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, idMesa);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizarEstado: " + e.getMessage());
            return false;
        }
    }

    public List<Mesa> listarTodas() {
        List<Mesa> lista = new ArrayList<>();
        try (Connection cn = Conexion.conectar()) {
            String sql = "SELECT idMesa, numero, capacidad, estado FROM mesa ORDER BY numero";
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Mesa m = new Mesa();
                m.setIdMesa(rs.getInt("idMesa"));
                m.setNumero(rs.getInt("numero"));
                m.setCapacidad(rs.getInt("capacidad"));
                m.setEstado(rs.getString("estado"));
                lista.add(m);
            }
        } catch (Exception e) {
            System.out.println("Error listarTodas: " + e.getMessage());
        }
        return lista;
    }

}
