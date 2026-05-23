
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
                Connection con = Conexion.conectar();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
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
    
}
