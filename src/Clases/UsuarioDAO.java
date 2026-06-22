package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    public Usuario login(String user, String password) {
        String sql = """
            SELECT u.idUsuario, u.nombre, u.apellido, u.usuario,
                   u.idRol, r.rol
            FROM usuario u
            INNER JOIN rol r ON u.idRol = r.idRol
            WHERE u.usuario = ?
            AND u.contrasenia = SHA2(?, 256)
        """;

        try (
                Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setIdRol(rs.getInt("idRol"));
                usuario.setRol(rs.getString("rol"));
                return usuario;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertar(String nombre, String apellido, String usuario, String contrasenia, int idRol) {
        String sql = """
        INSERT INTO usuario (nombre, apellido, usuario, contrasenia, idRol)
        VALUES (?, ?, ?, SHA2(?, 256), ?)
    """;

        try (
                Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, usuario);
            ps.setString(4, contrasenia);
            ps.setInt(5, idRol);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public java.util.List<Usuario> listarNoAdmins() {
        java.util.List<Usuario> lista = new java.util.ArrayList<>();
        String sql = """
        SELECT u.idUsuario, u.nombre, u.apellido, u.usuario, u.idRol, r.rol
        FROM usuario u
        INNER JOIN rol r ON u.idRol = r.idRol
        WHERE r.rol != 'ADMINISTRADOR'
    """;
        try (
                java.sql.Connection con = Conexion.conectar(); java.sql.PreparedStatement ps = con.prepareStatement(sql); java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("idUsuario"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setUsuario(rs.getString("usuario"));
                u.setIdRol(rs.getInt("idRol"));
                u.setRol(rs.getString("rol"));
                lista.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean actualizar(int idUsuario, String nombre, String apellido, String usuario, String contrasenia, int idRol) {
        String sql;
        if (contrasenia == null || contrasenia.isEmpty()) {
            sql = "UPDATE usuario SET nombre=?, apellido=?, usuario=?, idRol=? WHERE idUsuario=?";
        } else {
            sql = "UPDATE usuario SET nombre=?, apellido=?, usuario=?, contrasenia=SHA2(?,256), idRol=? WHERE idUsuario=?";
        }
        try (
                java.sql.Connection con = Conexion.conectar(); java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, usuario);
            if (contrasenia == null || contrasenia.isEmpty()) {
                ps.setInt(4, idRol);
                ps.setInt(5, idUsuario);
            } else {
                ps.setString(4, contrasenia);
                ps.setInt(5, idRol);
                ps.setInt(6, idUsuario);
            }
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int idUsuario) {
        String sql = "DELETE FROM usuario WHERE idUsuario = ?";
        try (
                java.sql.Connection con = Conexion.conectar(); java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
