/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.eltorneo.mvc.dao;

import co.eltorneo.common.util.AsignaAtributoStatement;
import co.eltorneo.common.util.LoggerMessage;
import co.eltorneo.mvc.dto.EquipoDTO;
import co.eltorneo.mvc.dto.PartidoDTO;
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
public class PartidoDAO {

    public RespuestaDTO registrarPartido(Connection conexion, PartidoDTO partido) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int nRows = 0;
        StringBuilder cadSQL = null;
        RespuestaDTO registro = null;

        try {
            registro = new RespuestaDTO();
            // System.out.println("partido " + partido.toStringJson());
            cadSQL = new StringBuilder();
            cadSQL.append(" INSERT INTO partido(part_dia, hopa_id,part_jornada,temp_id)");
            cadSQL.append(" VALUES (?, ?, ? ,?) ");

            ps = conexion.prepareStatement(cadSQL.toString(), Statement.RETURN_GENERATED_KEYS);

            AsignaAtributoStatement.setString(1, partido.getFechaPartido(), ps);
            AsignaAtributoStatement.setString(2, partido.getIdHorario(), ps);
            AsignaAtributoStatement.setInt(3, String.valueOf(partido.getJornada()), ps);
            AsignaAtributoStatement.setString(4, partido.getTemporada(), ps);

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
     * @param partido
     * @return
     * @throws SQLException
     */
    public RespuestaDTO registrarPartidoJuego(Connection conexion, PartidoDTO partido) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int nRows = 0;
        StringBuilder cadSQL = null;
        RespuestaDTO registro = null;

        try {
            registro = new RespuestaDTO();
            System.out.println("entra al dao con este partido-> " + partido.toStringJson());
            cadSQL = new StringBuilder();
            cadSQL.append(" INSERT INTO partido_equipo(equi_a,equi_b, part_id)");
            cadSQL.append(" VALUES (?,?,?) ");

            ps = conexion.prepareStatement(cadSQL.toString(), Statement.RETURN_GENERATED_KEYS);

            AsignaAtributoStatement.setString(1, partido.getEquipoA(), ps);
            AsignaAtributoStatement.setString(2, partido.getEquipoB(), ps);
            AsignaAtributoStatement.setString(3, partido.getId(), ps);

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
    public ArrayList<PartidoDTO> listarPartidos(Connection conexion) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<PartidoDTO> partidos = null;
        PartidoDTO part = null;
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT part_id, part_jornada");
            cadSQL.append(" FROM partido ");
            ps = conexion.prepareStatement(cadSQL.toString());

            rs = ps.executeQuery();
            partidos = new ArrayList();

            while (rs.next()) {
                part = new PartidoDTO();
                part.setId(rs.getString("part_id"));
                part.setJornada(rs.getInt("part_jornada"));
                partidos.add(part);

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
                if (partidos != null && partidos.isEmpty()) {
                    partidos = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return null;
            }
        }

        return partidos;
    }

    /**
     * *
     * valida si el equipo ya esta en un partido de la jornada
     *
     * @param conexion
     * @param idEquipo
     * @param jornada
     * @return
     */
    public boolean validarEquipoPorJornada(Connection conexion, String idEquipo, int jornada) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean rta = false;
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT paq.part_id  FROM partido_equipo paq");
            cadSQL.append(" INNER JOIN partido pa ON pa.part_id = paq.part_id ");
            cadSQL.append(" WHERE paq.equi_id = ? AND pa.part_jornada = ?");
            ps = conexion.prepareStatement(cadSQL.toString());
            AsignaAtributoStatement.setString(1, idEquipo, ps);
            AsignaAtributoStatement.setString(2, String.valueOf(jornada), ps);

            rs = ps.executeQuery();

            while (rs.next()) {
                rta = true;

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
                    rs.close();
                    rs = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return false;
            }
        }

        return rta;
    }

    /**
     *
     * @param conexion
     * @param idEquipoA
     * @param idEquipoB
     * @return
     */
    public boolean validarEquipoPorPartidos(Connection conexion, String idEquipoA, String idEquipoB) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean rta = false;
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT part_id FROM partido_equipo paq ");
            cadSQL.append(" WHERE paq.equi_a = ? AND paq.equi_b = ? or paq.equi_a = ? AND paq.equi_b = ? ");

            ps = conexion.prepareStatement(cadSQL.toString());
            AsignaAtributoStatement.setString(1, idEquipoA, ps);
            AsignaAtributoStatement.setString(2, idEquipoB, ps);
            AsignaAtributoStatement.setString(3, idEquipoB, ps);
            AsignaAtributoStatement.setString(4, idEquipoA, ps);

            rs = ps.executeQuery();

            while (rs.next()) {
                rta = true;
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
                    rs.close();
                    rs = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return false;
            }
        }

        return rta;
    }

    /**
     *
     * @param conexion
     * @param partido
     * @return
     * @throws SQLException
     */
    public RespuestaDTO registrarEncuentro(Connection conexion, PartidoDTO partido) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int nRows = 0;
        StringBuilder cadSQL = null;
        RespuestaDTO registro = null;

        try {
            registro = new RespuestaDTO();
            System.out.println("entra el partido " + partido.toStringJson());
            cadSQL = new StringBuilder();
            cadSQL.append(" UPDATE partido SET equi_a = ?,equi_b = ?");
            cadSQL.append(" WHERE part_id = ?");

            ps = conexion.prepareStatement(cadSQL.toString(), Statement.RETURN_GENERATED_KEYS);

            AsignaAtributoStatement.setInt(1, partido.getEquipoA(), ps);
            AsignaAtributoStatement.setInt(2, partido.getEquipoB(), ps);
            AsignaAtributoStatement.setInt(3, partido.getId(), ps);

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
    public ArrayList<PartidoDTO> listarPartidosPorIdTemporada(Connection conexion, String idTemporada) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<PartidoDTO> partidos = null;
        PartidoDTO part = null;
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT pa.part_id, pa.equi_a,pa.equi_b, eq.equi_nombre as equipoa,equ.equi_nombre as equipob, pa.part_dia, h.hopa_horainicial,h.hopa_horafinal,part_jornada  ");
            cadSQL.append(" FROM partido pa");
            cadSQL.append(" INNER JOIN horario_partido h ON h.hopa_id = pa.hopa_id");
            cadSQL.append(" INNER JOIN equipo eq ON eq.equi_id = pa.equi_a");
            cadSQL.append(" INNER JOIN equipo equ ON equ.equi_id = pa.equi_b");
            cadSQL.append(" WHERE pa.temp_id = ?");
            cadSQL.append(" order by (pa.part_id) asc");

            ps = conexion.prepareStatement(cadSQL.toString());
            AsignaAtributoStatement.setInt(1, idTemporada, ps);
            rs = ps.executeQuery();
            partidos = new ArrayList();

            while (rs.next()) {
                part = new PartidoDTO();
                part.setId(rs.getString("part_id"));
                part.setEquipoA(rs.getString("pa.equi_a"));
                part.setEquipoB(rs.getString("pa.equi_b"));
                part.setNombreEquipoA(rs.getString("equipoa"));
                part.setNombreEquipoB(rs.getString("equipob"));
                part.setHoraInicial(rs.getString("h.hopa_horainicial"));
                part.setHoraFinal(rs.getString("h.hopa_horafinal"));
                part.setFechaPartido(rs.getString("pa.part_dia"));
                part.setJornada(rs.getInt("part_jornada"));
                partidos.add(part);

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
                if (partidos != null && partidos.isEmpty()) {
                    partidos = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return null;
            }
        }

        return partidos;
    }

    /**
     *
     * @param conexion
     * @param idPartido
     * @return
     */
    public ArrayList<PartidoDTO> listarPartidosPorIdArbitroCentral(Connection conexion, String idArbitro) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<PartidoDTO> partidos = null;
        PartidoDTO part = null;
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT pa.part_id, pa.equi_a,pa.equi_b, eq.equi_nombre as equipoa,equ.equi_nombre as equipob, pa.part_dia, h.hopa_horainicial,h.hopa_horafinal,part_jornada ");
            cadSQL.append(" FROM partido pa");
            cadSQL.append(" INNER JOIN horario_partido h ON h.hopa_id = pa.hopa_id");
            cadSQL.append(" INNER JOIN equipo eq ON eq.equi_id = pa.equi_a");
            cadSQL.append(" INNER JOIN equipo equ ON equ.equi_id = pa.equi_b");
            cadSQL.append(" INNER JOIN partido_arbitro par ON par.part_id = pa.part_id");
            cadSQL.append(" WHERE par.arbi_id = ? AND par.paar_arbitrocentral = '1'");

            ps = conexion.prepareStatement(cadSQL.toString());
            AsignaAtributoStatement.setInt(1, idArbitro, ps);
            rs = ps.executeQuery();
            partidos = new ArrayList();

            while (rs.next()) {
                part = new PartidoDTO();
                part.setId(rs.getString("part_id"));
                part.setEquipoA(rs.getString("pa.equi_a"));
                part.setEquipoB(rs.getString("pa.equi_b"));
                part.setNombreEquipoA(rs.getString("equipoa"));
                part.setNombreEquipoB(rs.getString("equipob"));
                part.setHoraInicial(rs.getString("h.hopa_horainicial"));
                part.setHoraFinal(rs.getString("h.hopa_horafinal"));
                part.setFechaPartido(rs.getString("pa.part_dia"));
                part.setJornada(rs.getInt("part_jornada"));
                partidos.add(part);

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
                if (partidos != null && partidos.isEmpty()) {
                    partidos = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return null;
            }
        }

        return partidos;
    }

    /**
     *
     * @param conexion
     * @param idPartido
     * @return
     */
    public PartidoDTO verInformacionDePartidoPorId(Connection conexion, String idPartido) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        PartidoDTO part = null;
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT pa.part_id, pa.equi_a,pa.equi_b, eq.equi_nombre as equipoa,equ.equi_nombre as equipob, pa.part_dia, h.hopa_horainicial,h.hopa_horafinal,part_jornada ");
            cadSQL.append(" FROM partido pa");
            cadSQL.append(" INNER JOIN horario_partido h ON h.hopa_id = pa.hopa_id");
            cadSQL.append(" INNER JOIN equipo eq ON eq.equi_id = pa.equi_a");
            cadSQL.append(" INNER JOIN equipo equ ON equ.equi_id = pa.equi_b");
            cadSQL.append(" INNER JOIN partido_arbitro par ON par.part_id = pa.part_id");
            cadSQL.append(" WHERE par.part_id");

            ps = conexion.prepareStatement(cadSQL.toString());
            AsignaAtributoStatement.setInt(1, idPartido, ps);
            rs = ps.executeQuery();

            while (rs.next()) {
                part = new PartidoDTO();
                part.setId(rs.getString("part_id"));
                part.setEquipoA(rs.getString("pa.equi_a"));
                part.setEquipoB(rs.getString("pa.equi_b"));
                part.setNombreEquipoA(rs.getString("equipoa"));
                part.setNombreEquipoB(rs.getString("equipob"));
                part.setHoraInicial(rs.getString("h.hopa_horainicial"));
                part.setHoraFinal(rs.getString("h.hopa_horafinal"));
                part.setFechaPartido(rs.getString("pa.part_dia"));
                part.setJornada(rs.getInt("part_jornada"));

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

        return part;
    }
}
