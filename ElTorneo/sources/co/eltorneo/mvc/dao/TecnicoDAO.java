/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.eltorneo.mvc.dao;

import co.eltorneo.common.util.AsignaAtributoStatement;
import co.eltorneo.common.util.LoggerMessage;
import co.eltorneo.mvc.dto.RespuestaDTO;
import co.eltorneo.mvc.dto.TecnicoDTO;
import co.eltorneo.mvc.dto.UsuarioDTO;
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
public class TecnicoDAO {

    /**
     *
     * @param conexion
     * @param tecnico
     * @return
     * @throws SQLException
     */
    public RespuestaDTO registrarTecnico(Connection conexion, TecnicoDTO tecnico) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int nRows = 0;
        StringBuilder cadSQL = null;
        RespuestaDTO registro = null;

        try {
            registro = new RespuestaDTO();
            System.out.println("tecnico " + tecnico.toStringJson());
            cadSQL = new StringBuilder();
            cadSQL.append(" INSERT INTO tecnico(tecn_nombre, tecn_apellido, tecn_telefono, tecn_celular,tecn_direccion,usua_id,tecn_documento)");
            cadSQL.append(" VALUES (?, ?, ?, ?, ?, ?,?) ");

            ps = conexion.prepareStatement(cadSQL.toString(), Statement.RETURN_GENERATED_KEYS);

            AsignaAtributoStatement.setString(1, tecnico.getNombre(), ps);
            AsignaAtributoStatement.setString(2, tecnico.getApellido(), ps);
            AsignaAtributoStatement.setString(3, tecnico.getTelefono(), ps);
            AsignaAtributoStatement.setString(4, tecnico.getCelular(), ps);
            AsignaAtributoStatement.setString(5, tecnico.getDireccion(), ps);
            AsignaAtributoStatement.setString(6, tecnico.getIdUsuario(), ps);
            AsignaAtributoStatement.setString(7, tecnico.getDocumento(), ps);

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
    public ArrayList<TecnicoDTO> listarTecnicos(Connection conexion) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<TecnicoDTO> listadoTecnicos = null;
        TecnicoDTO tecnico = null;
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT tecn_id, tecn_nombre, tecn_apellido,tecn_telefono, tecn_direccion,tecn_celular,tecn_documento");
            cadSQL.append(" FROM tecnico ");
            ps = conexion.prepareStatement(cadSQL.toString());

            rs = ps.executeQuery();

            listadoTecnicos = new ArrayList();

            while (rs.next()) {
                tecnico = new TecnicoDTO();
                tecnico.setId(rs.getString("tecn_id"));
                tecnico.setNombre(rs.getString("tecn_nombre"));
                tecnico.setApellido(rs.getString("tecn_apellido"));
                tecnico.setTelefono(rs.getString("tecn_telefono"));
                tecnico.setCelular(rs.getString("tecn_celular"));
                tecnico.setDireccion(rs.getString("tecn_direccion"));
                tecnico.setDocumento(rs.getString("tecn_documento"));

                listadoTecnicos.add(tecnico);

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
                if (listadoTecnicos != null && listadoTecnicos.isEmpty()) {
                    listadoTecnicos = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return null;
            }
        }

        return listadoTecnicos;
    }

    /**
     *
     * @param conexion
     * @param idTecnico
     * @return
     */
    public TecnicoDTO buscarTecnicoPorId(Connection conexion, String idTecnico) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        TecnicoDTO tecnico = null;
        StringBuilder cadSQL = null;

        try {
            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT tecn_id, tecn_nombre, tecn_apellido,tecn_telefono,tecn_celular,tecn_documento, ");
            cadSQL.append(" us.usua_id, us.usua_usuario, us.usua_correo, tecn_direccion");
            cadSQL.append(" FROM tecnico ");
            cadSQL.append(" INNER JOIN usuario us ON us.usua_id = tecnico.usua_id");
            cadSQL.append(" WHERE tecn_id = ?");

            ps = conexion.prepareStatement(cadSQL.toString());
            AsignaAtributoStatement.setString(1, idTecnico, ps);

            rs = ps.executeQuery();

            while (rs.next()) {
                tecnico = new TecnicoDTO();
                tecnico.setId(rs.getString("tecn_id"));
                tecnico.setNombre(rs.getString("tecn_nombre"));
                tecnico.setApellido(rs.getString("tecn_apellido"));
                tecnico.setTelefono(rs.getString("tecn_telefono"));
                tecnico.setCelular(rs.getString("tecn_celular"));
                tecnico.setDocumento(rs.getString("tecn_documento"));
                tecnico.setIdUsuario(rs.getString("us.usua_id"));
                tecnico.setCorreo(rs.getString("us.usua_correo"));
                tecnico.setUsuario(rs.getString("us.usua_usuario"));
                tecnico.setDireccion(rs.getString("tecn_direccion"));
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

        return tecnico;
    }

    /**
     *
     * @param conexion
     * @param arbitro
     * @return
     * @throws SQLException
     */
    public RespuestaDTO actualizarTecnico(Connection conexion, TecnicoDTO arbitro) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int nRows = 0;
        StringBuilder cadSQL = null;
        RespuestaDTO registro = null;

        try {
            registro = new RespuestaDTO();
            System.out.println("tecnico " + arbitro.toStringJson());
            cadSQL = new StringBuilder();
            cadSQL.append(" UPDATE tecnico set tecn_nombre = ? ,  tecn_apellido = ? , tecn_documento = ? , tecn_telefono = ?, ");
            cadSQL.append(" tecn_direccion = ?, tecn_celular = ? ");
            cadSQL.append(" WHERE tecn_id = ?  ");

            ps = conexion.prepareStatement(cadSQL.toString(), Statement.RETURN_GENERATED_KEYS);

            AsignaAtributoStatement.setString(1, arbitro.getNombre(), ps);
            AsignaAtributoStatement.setString(2, arbitro.getApellido(), ps);
            AsignaAtributoStatement.setString(3, arbitro.getDocumento(), ps);
            AsignaAtributoStatement.setString(4, arbitro.getTelefono(), ps);
            AsignaAtributoStatement.setString(5, arbitro.getDireccion(), ps);
            AsignaAtributoStatement.setString(6, arbitro.getCelular(), ps);
            AsignaAtributoStatement.setString(7, arbitro.getId(), ps);

            nRows = ps.executeUpdate();
            if (nRows > 0) {
                registro.setRegistro(true);
                registro.setMensaje("Se actualizo el arbitro");
            }
            ps.close();
            ps = null;

        } catch (SQLException se) {
            LoggerMessage.getInstancia().loggerMessageException(se);
            return null;
        }
        return registro;
    }
}
