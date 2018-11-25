/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.eltorneo.mvc.dao;

import co.eltorneo.common.util.AsignaAtributoStatement;
import co.eltorneo.common.util.LoggerMessage;
import co.eltorneo.mvc.dto.FuncionalidadDTO;
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
public class FuncionalidadesDAO {

    /**
     *
     * @param conexion
     * @param idMenu
     * @param idUsuario
     * @return
     */
    public ArrayList<FuncionalidadDTO> listarFuncionalidadesPorMenu(Connection conexion, String idMenu, String idUsuario) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<FuncionalidadDTO> listado = null;
        FuncionalidadDTO datos = null;
        StringBuilder cadSQL = null;

        try {
            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT DISTINCT usfu.usua_id,usfu.pagi_id,func.pagi_pagina,func.pagi_titulo,func.pagi_icono ");
            cadSQL.append(" FROM  usuario_paginas usfu   ");
            cadSQL.append(" INNER JOIN pagina func ON func.pagi_id = usfu.pagi_id   ");
            cadSQL.append(" INNER JOIN menu menu ON menu.menu_id = func.menu_id    ");
            cadSQL.append(" WHERE usfu.usua_id = ? and menu.menu_id = ? ");

            ps = conexion.prepareStatement(cadSQL.toString());

            AsignaAtributoStatement.setString(1, idUsuario, ps);
            AsignaAtributoStatement.setString(2, idMenu, ps);
            rs = ps.executeQuery();
            listado = new ArrayList();
            while (rs.next()) {
                datos = new FuncionalidadDTO();
                datos.setId(rs.getString("pagi_id"));
                datos.setPagina(rs.getString("pagi_pagina"));
                datos.setTitulo(rs.getString("pagi_titulo"));
                datos.setIcono(rs.getString("pagi_icono"));
                listado.add(datos);
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
                if (listado != null && listado.isEmpty()) {
                    listado = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return null;
            }
        }
        return listado;
    }

    /**
     *
     * @param conexion
     * @param idTipoUsuario
     * @return
     */
    public ArrayList<FuncionalidadDTO> listarFuncionalidadesPorTipoUsuario(Connection conexion, String idTipoUsuario) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<FuncionalidadDTO> listado = null;
        FuncionalidadDTO datos = null;
        StringBuilder cadSQL = null;

        try {
            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT DISTINCT tiuf.pagi_id,tiuf.tius_id ");
            cadSQL.append(" FROM  tipo_usuario_pagina tiuf  ");
            cadSQL.append(" WHERE tiuf.tius_id=? ");

            ps = conexion.prepareStatement(cadSQL.toString());

            AsignaAtributoStatement.setString(1, idTipoUsuario, ps);
            rs = ps.executeQuery();
            listado = new ArrayList();
            while (rs.next()) {
                datos = new FuncionalidadDTO();
                datos.setId(rs.getString("tiuf.pagi_id"));
                listado.add(datos);
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
                if (listado != null && listado.isEmpty()) {
                    listado = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return null;
            }
        }
        return listado;
    }

    /**
     *
     * @param conexion
     * @param idUsuario
     * @param idFuncionalidad
     * @return
     * @throws SQLException
     */
    public boolean registrarUsuarioFuncionalidad(Connection conexion, String idUsuario, String idPagina) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int nRows = 0;
        StringBuilder cadSQL = null;
        RespuestaDTO respuesta = new RespuestaDTO();;

        try {
            cadSQL = new StringBuilder();

            cadSQL.append(" INSERT INTO usuario_paginas(usua_id,pagi_id)");
            cadSQL.append(" VALUES (?, ?) ");

            ps = conexion.prepareStatement(cadSQL.toString(), Statement.RETURN_GENERATED_KEYS);

            AsignaAtributoStatement.setString(1, idUsuario, ps);
            AsignaAtributoStatement.setString(2, idPagina, ps);

            nRows = ps.executeUpdate();

            if (nRows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    respuesta = new RespuestaDTO();
                    respuesta.setRegistro(true);
                    respuesta.setMensaje("Registro exitoso");
                }

                rs.close();
                rs = null;
            }
        } catch (SQLException se) {
            LoggerMessage.getInstancia().loggerMessageException(se);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    ps = null;
                }

            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return respuesta.isRegistro();
            }
        }
        return respuesta.isRegistro();
    }

}
