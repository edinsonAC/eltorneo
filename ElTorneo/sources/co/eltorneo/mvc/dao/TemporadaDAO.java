/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.eltorneo.mvc.dao;

import co.eltorneo.common.util.AsignaAtributoStatement;
import co.eltorneo.common.util.LoggerMessage;
import co.eltorneo.mvc.dto.RespuestaDTO;
import co.eltorneo.mvc.dto.TemporadaDTO;
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
public class TemporadaDAO {

    /**
     *
     * @param conexion
     * @return
     */
    public ArrayList<TemporadaDTO> listarTemporadas(Connection conexion) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<TemporadaDTO> temporadas = null;
        TemporadaDTO temp = null;
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT temp_id, temp_nombre, temp_numequipos,temp_fechafinal,temp_fechainicial");
            cadSQL.append(" FROM temporada");

            ps = conexion.prepareStatement(cadSQL.toString());

            rs = ps.executeQuery();
            temporadas = new ArrayList();

            while (rs.next()) {
                temp = new TemporadaDTO();
                temp.setId(rs.getString("temp_id"));
                temp.setNombre(rs.getString("temp_nombre"));
                temp.setFechaFinal(rs.getString("temp_fechafinal"));
                temp.setFechaInicial(rs.getString("temp_fechainicial"));
                temp.setNumEquipos(rs.getString("temp_numequipos"));
                temporadas.add(temp);

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
                if (temporadas != null && temporadas.isEmpty()) {
                    temporadas = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return null;
            }
        }

        return temporadas;
    }

    /**
     *
     * @param conexion
     * @param temporada
     * @return
     * @throws SQLException
     */
    public RespuestaDTO registrarTemporada(Connection conexion, TemporadaDTO temporada) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int nRows = 0;
        StringBuilder cadSQL = null;
        RespuestaDTO registro = null;

        try {
            registro = new RespuestaDTO();
            System.out.println("temporada " + temporada.toStringJson());
            cadSQL = new StringBuilder();
            cadSQL.append(" INSERT INTO temporada(temp_nombre, temp_fechainicial,temp_numequipos)");
            cadSQL.append(" VALUES (?, ?, ?) ");

            ps = conexion.prepareStatement(cadSQL.toString(), Statement.RETURN_GENERATED_KEYS);

            AsignaAtributoStatement.setString(1, temporada.getNombre(), ps);
            AsignaAtributoStatement.setString(2, temporada.getFechaInicial(), ps);
            AsignaAtributoStatement.setString(3, temporada.getNumEquipos(), ps);

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
     * @param idTemporada
     * @return
     */
    public TemporadaDTO buscarTemporadaPorId(Connection conexion, String idTemporada) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        TemporadaDTO temp = null;
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT temp_id, temp_nombre, temp_fechainicial, temp_fechafinal, temp_numequipos");
            cadSQL.append(" FROM temporada ");
            cadSQL.append(" WHERE temp_id = ?");

            ps = conexion.prepareStatement(cadSQL.toString());
            AsignaAtributoStatement.setString(1, idTemporada, ps);
            rs = ps.executeQuery();

            while (rs.next()) {
                temp = new TemporadaDTO();
                temp.setId(rs.getString("temp_id"));
                temp.setNombre(rs.getString("temp_nombre"));
                temp.setFechaInicial(rs.getString("temp_fechainicial"));
                temp.setFechaFinal(rs.getString("temp_fechafinal"));
                temp.setNumEquipos(rs.getString("temp_numequipos"));
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

        return temp;
    }

    /**
     *
     * @param conexion
     * @param temp
     * @return
     * @throws SQLException
     */
    public RespuestaDTO actualizarTemporada(Connection conexion, TemporadaDTO temp) throws SQLException {
        PreparedStatement ps = null;
        int nRows = 0;
        StringBuilder cadSQL = null;
        RespuestaDTO registro = null;

        try {
            registro = new RespuestaDTO();
            System.out.println("temporada " + temp.toStringJson());
            cadSQL = new StringBuilder();
            cadSQL.append(" UPDATE temporada SET temp_nombre = ?, temp_fechainicial = ? ,temp_numequipos = ?");
            cadSQL.append(" WHERE temp_id = ?");

            ps = conexion.prepareStatement(cadSQL.toString(), Statement.RETURN_GENERATED_KEYS);

            AsignaAtributoStatement.setString(1, temp.getNombre(), ps);
            AsignaAtributoStatement.setString(2, temp.getFechaInicial(), ps);
            AsignaAtributoStatement.setString(3, temp.getNumEquipos(), ps);
            AsignaAtributoStatement.setString(4, temp.getId(), ps);

            nRows = ps.executeUpdate();
            if (nRows > 0) {
                registro.setRegistro(true);

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
