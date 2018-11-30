/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.eltorneo.mvc.dao;

import co.eltorneo.common.util.AsignaAtributoStatement;
import co.eltorneo.common.util.LoggerMessage;
import co.eltorneo.mvc.dto.ArbitroDTO;
import co.eltorneo.mvc.dto.EquipoDTO;
import co.eltorneo.mvc.dto.JugadorDTO;
import co.eltorneo.mvc.dto.RespuestaDTO;
import co.eltorneo.mvc.dto.TecnicoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Administrador
 */
public class JugadorDAO {

    /**
     *
     * @param conexion
     * @param jugador
     * @return
     * @throws SQLException
     */
    public RespuestaDTO registrarJugador(Connection conexion, JugadorDTO jugador) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int nRows = 0;
        StringBuilder cadSQL = null;
        RespuestaDTO registro = null;

        try {
            registro = new RespuestaDTO();
            System.out.println("tecnico " + jugador.toStringJson());
            cadSQL = new StringBuilder();
            cadSQL.append(" INSERT INTO jugador(juga_nombre, juga_apellido, juga_telefono, juga_celular,juga_direccion,usua_id,juga_documento,");
            cadSQL.append(" poju_id,equi_id)");
            cadSQL.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ? ,?) ");

            ps = conexion.prepareStatement(cadSQL.toString(), Statement.RETURN_GENERATED_KEYS);

            AsignaAtributoStatement.setString(1, jugador.getNombre(), ps);
            AsignaAtributoStatement.setString(2, jugador.getApellido(), ps);
            AsignaAtributoStatement.setString(3, jugador.getTelefono(), ps);
            AsignaAtributoStatement.setString(4, jugador.getCelular(), ps);
            AsignaAtributoStatement.setString(5, jugador.getDireccion(), ps);
            AsignaAtributoStatement.setString(6, jugador.getIdUsuario(), ps);
            AsignaAtributoStatement.setString(7, jugador.getDocumento(), ps);
            AsignaAtributoStatement.setString(8, jugador.getIdPosicion(), ps);
            AsignaAtributoStatement.setString(9, jugador.getIdEquipo(), ps);

            nRows = ps.executeUpdate();
            if (nRows > 0) {
                rs = ps.getGeneratedKeys();
                registro.setRegistro(true);
                if (rs.next()) {
                    registro.setIdResgistrado(rs.getString(1));

                }
                rs.close();
                rs = null;
            }
            ps.close();
            ps = null;

        } catch (SQLException se) {
            LoggerMessage.getInstancia().loggerMessageException(se);
            return null;
        }
        return registro;
    }

    /**
     *
     * @param conexion
     * @return
     */
    public ArrayList<JugadorDTO> listarJugadores(Connection conexion) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<JugadorDTO> listadoJugador = null;
        JugadorDTO jugador = null;
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT juga_id, juga_nombre, juga_apellido,juga_telefono,juga_celular,juga_documento,");
            cadSQL.append(" poju_id,equi_id");
            cadSQL.append(" FROM jugador ");
            ps = conexion.prepareStatement(cadSQL.toString());

            rs = ps.executeQuery();

            listadoJugador = new ArrayList();

            while (rs.next()) {
                jugador = new JugadorDTO();
                jugador.setId(rs.getString("juga_id"));
                jugador.setNombre(rs.getString("juga_nombre"));
                jugador.setApellido(rs.getString("juga_apellido"));
                jugador.setTelefono(rs.getString("juga_apellido"));
                jugador.setCelular(rs.getString("juga_celular"));
                jugador.setDocumento(rs.getString("juga_documento"));
                jugador.setIdPosicion(rs.getString("poju_id"));
                jugador.setIdEquipo(rs.getString("equi_id"));

                listadoJugador.add(jugador);

            }
            ps.close();
            ps = null;

        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
            return null;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
                if (listadoJugador != null && listadoJugador.isEmpty()) {
                    listadoJugador = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return null;
            }
        }

        return listadoJugador;
    }

    /**
     *
     * @param conexion
     * @param idEquipo
     * @return
     */
    public ArrayList<JugadorDTO> listarJugadoresPorIdEquipo(Connection conexion, String idEquipo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<JugadorDTO> listadoJugador = null;
        JugadorDTO jugador = null;
        StringBuilder cadSQL = null;

        try {
            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT juga_id, juga_nombre, juga_apellido,juga_telefono,juga_celular,juga_documento, ");
            cadSQL.append(" jugador.poju_id,equi_id, poju.poju_nombre");
            cadSQL.append(" FROM jugador ");
            cadSQL.append(" INNER JOIN posicion_jugador poju ON poju.poju_id = jugador.poju_id");
            cadSQL.append(" WHERE equi_id = ?");

            ps = conexion.prepareStatement(cadSQL.toString());
            AsignaAtributoStatement.setString(1, idEquipo, ps);

            rs = ps.executeQuery();

            listadoJugador = new ArrayList();
            while (rs.next()) {
                jugador = new JugadorDTO();
                jugador.setId(rs.getString("juga_id"));
                jugador.setNombre(rs.getString("juga_nombre"));
                jugador.setApellido(rs.getString("juga_apellido"));
                jugador.setTelefono(rs.getString("juga_apellido"));
                jugador.setCelular(rs.getString("juga_celular"));
                jugador.setDocumento(rs.getString("juga_documento"));
                jugador.setIdPosicion(rs.getString("poju_id"));
                jugador.setPosicion(rs.getString("poju_nombre"));
                jugador.setIdEquipo(rs.getString("equi_id"));

                listadoJugador.add(jugador);

            }
            ps.close();
            ps = null;

        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
            return null;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
                if (listadoJugador != null && listadoJugador.isEmpty()) {
                    listadoJugador = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return null;
            }
        }

        return listadoJugador;
    }

    /**
     *
     * @param conexion
     * @param idJugador
     * @return
     */
    public JugadorDTO buscarJugadorPorId(Connection conexion, String idJugador) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        JugadorDTO jugador = null;
        StringBuilder cadSQL = null;

        try {
            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT juga_id, juga_nombre, juga_apellido,juga_telefono,juga_celular,juga_documento, ");
            cadSQL.append(" jugador.poju_id,equi_id, poju.poju_nombre, us.usua_usuario");
            cadSQL.append(" FROM jugador ");
            cadSQL.append(" INNER JOIN posicion_jugador poju ON poju.poju_id = jugador.poju_id");
            cadSQL.append(" INNER JOIN usuario us ON us.usua_id = jugador.usua_id");
            cadSQL.append(" WHERE juga_id = ?");

            ps = conexion.prepareStatement(cadSQL.toString());
            AsignaAtributoStatement.setString(1, idJugador, ps);

            rs = ps.executeQuery();

            while (rs.next()) {
                jugador = new JugadorDTO();
                jugador.setId(rs.getString("juga_id"));
                jugador.setNombre(rs.getString("juga_nombre"));
                jugador.setApellido(rs.getString("juga_apellido"));
                jugador.setTelefono(rs.getString("juga_apellido"));
                jugador.setCelular(rs.getString("juga_celular"));
                jugador.setDocumento(rs.getString("juga_documento"));
                jugador.setIdPosicion(rs.getString("poju_id"));
                jugador.setPosicion(rs.getString("poju_nombre"));
                jugador.setIdEquipo(rs.getString("equi_id"));
                jugador.setIdUsuario(rs.getString("us.usua_id"));
                jugador.setUsuario(rs.getString("us.usua_usuario"));
            }
            ps.close();
            ps = null;

        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
            return null;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    ps = null;
                }

            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return null;
            }
        }

        return jugador;
    }
}
