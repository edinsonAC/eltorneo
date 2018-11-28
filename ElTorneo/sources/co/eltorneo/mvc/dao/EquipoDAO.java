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
public class EquipoDAO {

    /**
     *
     * @param conexion
     * @param equipo
     * @return
     * @throws SQLException
     */
    public RespuestaDTO registrarEquipo(Connection conexion, EquipoDTO equipo) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int nRows = 0;
        StringBuilder cadSQL = null;
        RespuestaDTO registro = null;

        try {
            registro = new RespuestaDTO();
            System.out.println("tecnico " + equipo.toStringJson());
            cadSQL = new StringBuilder();
            cadSQL.append(" INSERT INTO equipo(equi_nombre, tecn_id)");
            cadSQL.append(" VALUES (?, ?) ");

            ps = conexion.prepareStatement(cadSQL.toString(), Statement.RETURN_GENERATED_KEYS);

            AsignaAtributoStatement.setString(1, equipo.getNombre(), ps);
            AsignaAtributoStatement.setString(2, equipo.getIdTecnico(), ps);

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
    public ArrayList<EquipoDTO> listarEquipos(Connection conexion) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<EquipoDTO> listadoEquipo = null;
        EquipoDTO equipo = null;
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT equi_id, equi_nombre, tecn_id");
            cadSQL.append(" FROM equipo ");
            ps = conexion.prepareStatement(cadSQL.toString());

            rs = ps.executeQuery();

            listadoEquipo = new ArrayList();

            while (rs.next()) {
                equipo = new EquipoDTO();
                equipo.setId(rs.getString("equi_id"));
                equipo.setNombre(rs.getString("equi_nombre"));
                equipo.setIdTecnico(rs.getString("tecn_id"));

                listadoEquipo.add(equipo);

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
                if (listadoEquipo != null && listadoEquipo.isEmpty()) {
                    listadoEquipo = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return null;
            }
        }

        return listadoEquipo;
    }
}
