/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.eltorneo.mvc.dao;

import co.eltorneo.common.util.AsignaAtributoStatement;
import co.eltorneo.common.util.LoggerMessage;
import co.eltorneo.mvc.dto.ArbitroDTO;
import co.eltorneo.mvc.dto.RespuestaDTO;
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
public class ArbitroDAO {

    /**
     *
     * @param conexion
     * @param arbitro
     * @return
     * @throws SQLException
     */
    public RespuestaDTO registrarArbitro(Connection conexion, ArbitroDTO arbitro) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int nRows = 0;
        StringBuilder cadSQL = null;
        RespuestaDTO registro = null;

        try {
            registro = new RespuestaDTO();
            System.out.println("tecnico " + arbitro.toStringJson());
            cadSQL = new StringBuilder();
            cadSQL.append(" INSERT INTO arbitro(arbi_nombre, arbi_apellido, arbi_telefono, arbi_celular,usua_id,arbi_documento)");
            cadSQL.append(" VALUES (?, ?, ?, ?, ?, ?) ");

            ps = conexion.prepareStatement(cadSQL.toString(), Statement.RETURN_GENERATED_KEYS);

            AsignaAtributoStatement.setString(1, arbitro.getNombres(), ps);
            AsignaAtributoStatement.setString(2, arbitro.getApellidos(), ps);
            AsignaAtributoStatement.setString(3, arbitro.getTelefono(), ps);
            AsignaAtributoStatement.setString(4, arbitro.getCelular(), ps);
            AsignaAtributoStatement.setString(5, arbitro.getIdUsuario(), ps);
            AsignaAtributoStatement.setString(6, arbitro.getDocumento(), ps);

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
    public ArrayList<ArbitroDTO> listarArbitros(Connection conexion) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<ArbitroDTO> listadoArbitro = null;
        ArbitroDTO arbitro = null;
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT arbi_id, arbi_nombre, arbi_apellido,arbi_telefono,arbi_celular,arbi_documento");
            cadSQL.append(" FROM arbitro ");
            ps = conexion.prepareStatement(cadSQL.toString());

            rs = ps.executeQuery();

            listadoArbitro = new ArrayList();

            while (rs.next()) {
                arbitro = new ArbitroDTO();
                arbitro.setId(rs.getString("arbi_id"));
                arbitro.setNombres(rs.getString("arbi_nombre"));
                arbitro.setApellidos(rs.getString("arbi_apellido"));
                arbitro.setTelefono(rs.getString("arbi_telefono"));
                arbitro.setCelular(rs.getString("arbi_celular"));
                arbitro.setDocumento(rs.getString("arbi_documento"));

                listadoArbitro.add(arbitro);

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
                if (listadoArbitro != null && listadoArbitro.isEmpty()) {
                    listadoArbitro = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return null;
            }
        }

        return listadoArbitro;
    }

    /**
     *
     * @param conexion
     * @param idUsuario
     * @return
     */
    public String obtenerIdEquipoPorIdArbitroLogueado(Connection conexion, String idUsuario) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String idEquipo = "";
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT t.tecn_id,e.equi_id  ");
            cadSQL.append(" FROM tecnico t");
            cadSQL.append(" INNER JOIN equipo e ON e.tecn_id = t.tecn_id");
            cadSQL.append(" WHERE t.usua_id = ? AND e.equi_estado = '1'");
            ps = conexion.prepareStatement(cadSQL.toString());
            AsignaAtributoStatement.setString(1, idUsuario, ps);

            rs = ps.executeQuery();

            while (rs.next()) {
                idEquipo = rs.getString("e.equi_id");
            }
            rs.close();
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

        return idEquipo;
    }

    /**
     *
     * @param conexion
     * @param idArbitro
     * @return
     */
    public ArbitroDTO buscarArbitroPorId(Connection conexion, String idArbitro) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArbitroDTO arbitro = null;
        StringBuilder cadSQL = null;

        try {
            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT a.arbi_nombre,a.arbi_apellido,a.arbi_documento,a.arbi_celular,a.arbi_telefono,a.usua_id,u.usua_usuario,a.arbi_direccion,u.usua_correo  ");
            cadSQL.append(" FROM arbitro a");
            cadSQL.append(" INNER JOIN usuario u ON u.usua_id = a.usua_id");
            cadSQL.append(" WHERE a.arbi_id = ?");

            ps = conexion.prepareStatement(cadSQL.toString());
            AsignaAtributoStatement.setString(1, idArbitro, ps);

            rs = ps.executeQuery();

            while (rs.next()) {
                arbitro = new ArbitroDTO();
                arbitro.setId(idArbitro);
                arbitro.setNombres(rs.getString("a.arbi_nombre"));
                arbitro.setApellidos(rs.getString("a.arbi_apellido"));
                arbitro.setTelefono(rs.getString("a.arbi_telefono"));
                arbitro.setCelular(rs.getString("a.arbi_celular"));
                arbitro.setDocumento(rs.getString("a.arbi_documento"));
                arbitro.setIdUsuario(rs.getString("a.usua_id"));
                arbitro.setUsuario(rs.getString("u.usua_usuario"));
                arbitro.setCorreo(rs.getString("u.usua_correo"));
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

        return arbitro;
    }

    /**
     *
     * @param conexion
     * @param arbitro
     * @return
     * @throws SQLException
     */
    public RespuestaDTO actualizarArbitro(Connection conexion, ArbitroDTO arbitro) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int nRows = 0;
        StringBuilder cadSQL = null;
        RespuestaDTO registro = null;

        try {
            registro = new RespuestaDTO();
            System.out.println("arbitro " + arbitro.toStringJson());
            cadSQL = new StringBuilder();
            cadSQL.append(" UPDATE arbitro set arbi_nombre = ? ,  arbi_apellido = ? , arbi_documento = ? , arbi_telefono = ?, ");
            cadSQL.append(" arbi_direccion = ?, arbi_celular = ? ");
            cadSQL.append(" WHERE arbi_id = ?  ");

            ps = conexion.prepareStatement(cadSQL.toString(), Statement.RETURN_GENERATED_KEYS);

            AsignaAtributoStatement.setString(1, arbitro.getNombres(), ps);
            AsignaAtributoStatement.setString(2, arbitro.getApellidos(), ps);
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
