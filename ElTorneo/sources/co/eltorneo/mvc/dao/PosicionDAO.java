/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.eltorneo.mvc.dao;

import co.eltorneo.common.util.LoggerMessage;
import co.eltorneo.mvc.dto.PosicionDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Administrador
 */
public class PosicionDAO {

    /**
     *
     * @param conexion
     * @return
     */
    public ArrayList<PosicionDTO> listarPosiciones(Connection conexion) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<PosicionDTO> posiciones = null;
        PosicionDTO pos = null;
        StringBuilder cadSQL = null;

        try {
            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT poju_id, poju_nombre");
            cadSQL.append(" FROM posicion_jugador ");
            ps = conexion.prepareStatement(cadSQL.toString());

            rs = ps.executeQuery();
            posiciones = new ArrayList();

            while (rs.next()) {
                pos = new PosicionDTO();
                pos.setId(rs.getString("poju_id"));
                pos.setNombre(rs.getString("poju_nombre"));
                posiciones.add(pos);

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
                if (posiciones != null && posiciones.isEmpty()) {
                    posiciones = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return null;
            }
        }

        return posiciones;
    }
}
