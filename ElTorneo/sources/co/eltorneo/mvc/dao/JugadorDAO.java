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
            cadSQL.append(" poju_id,equi_id, juga_dorsal)");
            cadSQL.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ? ,? ,?) ");

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
            AsignaAtributoStatement.setString(10, jugador.getDorsal(), ps);

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
            cadSQL.append(" SELECT juga_id, juga_nombre, juga_apellido,juga_telefono,juga_celular,juga_documento,juga_dorsal, ");
            cadSQL.append(" jugador.poju_id,equi_id, poju.poju_nombre, CONCAT_WS(' ', juga_dorsal,juga_nombre,juga_apellido) AS nombregeneral");
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
                jugador.setDorsal(rs.getString("juga_dorsal"));
                jugador.setIdEquipo(rs.getString("equi_id"));
                jugador.setNombreSelect(rs.getString("nombregeneral"));
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
            cadSQL.append(" jugador.poju_id,equi_id, poju.poju_nombre, us.usua_usuario, juga_direccion, us.usua_correo, juga_dorsal");
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
                jugador.setTelefono(rs.getString("juga_telefono"));
                jugador.setCelular(rs.getString("juga_celular"));
                jugador.setDocumento(rs.getString("juga_documento"));
                jugador.setIdPosicion(rs.getString("poju_id"));
                jugador.setPosicion(rs.getString("poju_nombre"));
                jugador.setIdEquipo(rs.getString("equi_id"));
                jugador.setUsuario(rs.getString("us.usua_usuario"));
                jugador.setDireccion(rs.getString("juga_direccion"));
                jugador.setCorreo(rs.getString("us.usua_correo"));
                jugador.setDorsal(rs.getString("juga_dorsal"));
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

    /**
     *
     * @param conexion
     * @param jugador
     * @return
     * @throws SQLException
     */
    public RespuestaDTO actualizarJugador(Connection conexion, JugadorDTO jugador) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int nRows = 0;
        StringBuilder cadSQL = null;
        RespuestaDTO registro = null;

        try {
            registro = new RespuestaDTO();
            System.out.println("jugador " + jugador.toStringJson());
            cadSQL = new StringBuilder();
            cadSQL.append(" UPDATE jugador set juga_nombre = ? ,  juga_apellido = ? , juga_documento = ? , juga_telefono = ?, ");
            cadSQL.append(" juga_direccion = ?, juga_celular = ? , juga_dorsal = ?");
            cadSQL.append(" WHERE juga_id = ?  ");

            ps = conexion.prepareStatement(cadSQL.toString(), Statement.RETURN_GENERATED_KEYS);

            AsignaAtributoStatement.setString(1, jugador.getNombre(), ps);
            AsignaAtributoStatement.setString(2, jugador.getApellido(), ps);
            AsignaAtributoStatement.setString(3, jugador.getDocumento(), ps);
            AsignaAtributoStatement.setString(4, jugador.getTelefono(), ps);
            AsignaAtributoStatement.setString(5, jugador.getDireccion(), ps);
            AsignaAtributoStatement.setString(6, jugador.getCelular(), ps);
            AsignaAtributoStatement.setString(7, jugador.getDorsal(), ps);
            AsignaAtributoStatement.setString(8, jugador.getId(), ps);

            nRows = ps.executeUpdate();
            if (nRows > 0) {
                registro.setRegistro(true);
                registro.setMensaje("Se actualizo el jugador");
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
     * @param documento
     * @return
     */
    public boolean validarDocumentoJugador(Connection conexion, String documento) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean existe = false;
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT juga_id");
            cadSQL.append(" FROM jugador ");
            cadSQL.append(" WHERE juga_documento  = ? ");

            ps = conexion.prepareStatement(cadSQL.toString());
            AsignaAtributoStatement.setString(1, documento, ps);
            rs = ps.executeQuery();

            if (rs.next()) {
                existe = true;
            }

            ps.close();
            ps = null;

        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return false;
            }
        }

        return existe;
    }

    /**
     *
     * @param conexion
     * @param dorsal
     * @param equipo
     * @return
     */
    public boolean validarDorsalJugador(Connection conexion, String dorsal, String equipo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean existe = false;
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT juga_id");
            cadSQL.append(" FROM jugador ");
            cadSQL.append(" WHERE juga_dorsal  = ? And equi_id = ?");

            ps = conexion.prepareStatement(cadSQL.toString());
            AsignaAtributoStatement.setString(1, dorsal, ps);
            AsignaAtributoStatement.setString(2, equipo, ps);

            rs = ps.executeQuery();

            if (rs.next()) {
                existe = true;
            }

            ps.close();
            ps = null;

        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return false;
            }
        }

        return existe;
    }
}
