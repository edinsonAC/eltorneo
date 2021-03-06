/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.eltorneo.mvc.mediador;

import co.eltorneo.common.connection.ContextDataResourceNames;
import co.eltorneo.common.connection.DataBaseConnection;
import co.eltorneo.common.util.Constantes;
import co.eltorneo.common.util.Formato;
import co.eltorneo.common.util.LoggerMessage;
import co.eltorneo.hilo.Correo;
import co.eltorneo.mvc.dao.ArbitroDAO;
import co.eltorneo.mvc.dao.EquipoDAO;
import co.eltorneo.mvc.dao.FuncionalidadesDAO;
import co.eltorneo.mvc.dao.HorarioDAO;
import co.eltorneo.mvc.dao.JugadorDAO;
import co.eltorneo.mvc.dao.PartidoDAO;
import co.eltorneo.mvc.dao.PosicionDAO;
import co.eltorneo.mvc.dao.TecnicoDAO;
import co.eltorneo.mvc.dao.TemporadaDAO;
import co.eltorneo.mvc.dao.UsuarioDAO;
import co.eltorneo.mvc.dto.ArbitrajeDTO;
import co.eltorneo.mvc.dto.ArbitroDTO;
import co.eltorneo.mvc.dto.EquipoDTO;
import co.eltorneo.mvc.dto.FuncionalidadDTO;
import co.eltorneo.mvc.dto.HorarioDTO;
import co.eltorneo.mvc.dto.JugadorDTO;
import co.eltorneo.mvc.dto.PartidoDTO;
import co.eltorneo.mvc.dto.PosicionDTO;
import co.eltorneo.mvc.dto.RequisitoDTO;
import co.eltorneo.mvc.dto.RespuestaDTO;
import co.eltorneo.mvc.dto.TecnicoDTO;
import co.eltorneo.mvc.dto.TemporadaDTO;
import co.eltorneo.mvc.dto.UsuarioDTO;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.UUID;
import java.util.jar.Pack200;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.ArrayUtils;
import org.directwebremoting.WebContextFactory;

/**
 *
 * @author DANNY
 */
public class MediadorElTorneo {

    /**
     *
     */
    private final static MediadorElTorneo instancia = null;
    private final LoggerMessage logMsg;

    /**
     *
     */
    public MediadorElTorneo() {
        logMsg = LoggerMessage.getInstancia();
    }

    /**
     *
     * @return
     */
    public static synchronized MediadorElTorneo getInstancia() {
        return instancia == null ? new MediadorElTorneo() : instancia;
    }

    /**
     *
     * @param documento
     * @return
     */
    public UsuarioDTO recuperarContrasenia(String documento) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        UsuarioDTO datosUsuario = null;

        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            datosUsuario = new UsuarioDAO().recuperarContrasenia(conexion, documento);

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return datosUsuario;
    }

    /**
     * ahora el mediador es donde uno mas codigo hace, es donde uno mezcla
     * funciones con tal de llegar a la solucion del metodo
     *
     * @param tecnico
     * @param usuario
     * @return
     */
    public RespuestaDTO registrarTecnico(TecnicoDTO tecnico, UsuarioDTO usuario) {  // se declara un metodo normal como sabemos, este devuelve un objeto respuesta, ese objeto lo cree para devolver si se registro y mensaje si necesito

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = null, respuesta2 = new RespuestaDTO();

        try {
            //// estas dos lineas siempre van, son para la conexion a la base de datos
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);
            ///

            conexion.setAutoCommit(false); // estos es para casos en los que uno hacer varios insert en el metodo
            //para lo que funciona es para que se haga todo o nada, es decir si falla algo se devuelva y no haga nada.... este es como un punto de guardado

            respuesta = registrarUsuario(usuario); // llama al metodo del dao
            if (!respuesta.isRegistro()) { // si no registro haga esto
                respuesta.setMensaje("No se pudo registrar el tecnico");// devuelco dentro del objeto respuesta un mensaje para saber en donde fallo
                conexion.rollback();  // este es el que hace que se devuelva al punto de guardado en caso que no funcione un metodo
                throw new Exception("ERROR: no se pudo registrar el tecnico");
            }
            tecnico.setIdUsuario(respuesta.getIdResgistrado());
            respuesta2 = new TecnicoDAO().registrarTecnico(conexion, tecnico);

            System.out.println("sale del dao---" + respuesta2.toStringJson());
            if (!respuesta2.isRegistro()) {
                respuesta2.setMensaje("No se pudo registrar el tecnico");
                conexion.rollback();
                throw new Exception("ERROR: no se pudo registrar el tecnico");

            }

            Correo envio = new Correo(usuario.getCorreo(), usuario.getClave(), "1", tecnico.getNombre(), "1", "ninguna", usuario.getUsuario());
            envio.start();

            respuesta.setMensaje("Se ha registrado el tecnico satisfactoriamente");
            conexion.commit(); //esta linea es cuando se hacen todo bn, se hace como punto de salvado
            conexion.close(); //se cierra la conexion
            conexion = null;
        } catch (Exception e) {//los catch es para capturar lo errores y saber que fallo
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally { // el finally es algo que oblig que el metodo simpre termine por ahi, en este caso, si esta abierta la conexion que la cierre
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return respuesta;
    }

    /**
     *
     * @return
     */
    public ArrayList<TecnicoDTO> listarTecnicos() {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        ArrayList<TecnicoDTO> tecnicos = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            tecnicos = new TecnicoDAO().listarTecnicos(conexion);
            if (tecnicos.isEmpty()) {
                throw new Exception("ERROR: No hay tecnicos registrados");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return tecnicos;
    }

    /**
     *
     * @param arbitro
     * @param usuario
     * @return
     */
    public RespuestaDTO registrarArbitro(ArbitroDTO arbitro, UsuarioDTO usuario) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = null, respuesta2 = new RespuestaDTO();

        try {
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);
            conexion.setAutoCommit(false);

            respuesta = registrarUsuario(usuario);
            if (!respuesta.isRegistro()) {
                respuesta.setMensaje("No se pudo registrar el usuario");
                conexion.rollback();
                throw new Exception("ERROR: no se pudo registrar el usuario");
            }
            arbitro.setIdUsuario(respuesta.getIdResgistrado());
            respuesta2 = new ArbitroDAO().registrarArbitro(conexion, arbitro);

            if (!respuesta2.isRegistro()) {
                respuesta2.setMensaje("No se pudo registrar el arbitro");
                conexion.rollback();
                throw new Exception("ERROR: no se pudo registrar el arbitro");

            }

            Correo envio = new Correo(usuario.getCorreo(), usuario.getClave(), "1", arbitro.getNombres(), "1", "ninguna", usuario.getUsuario());
            envio.start();

            respuesta.setMensaje("Se ha registrado el arbitro satisfactoriamente");
            conexion.commit();
            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return respuesta;
    }

    /**
     *
     * @return
     */
    public ArrayList<ArbitroDTO> listarArbitros() {
        DataBaseConnection dbcon = null;
        Connection conexion = null;
        ArrayList<ArbitroDTO> arbitros = null;
        try {
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            arbitros = new ArbitroDAO().listarArbitros(conexion);
            if (arbitros.isEmpty()) {
                throw new Exception("ERROR: No hay arbitros registrados");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return arbitros;
    }

    /**
     *
     * @param equipo
     * @return
     */
    public RespuestaDTO registrarEquipo(EquipoDTO equipo) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = null;

        try {
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            respuesta = new EquipoDAO().registrarEquipo(conexion, equipo);
            if (!respuesta.isRegistro()) {
                respuesta.setMensaje("No se pudo registrar el equipo");
                conexion.rollback();
                throw new Exception("ERROR: no se pudo registrar el equipo");
            }

            respuesta.setMensaje("Se ha registrado el equipo satisfactoriamente");
            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return respuesta;
    }

    /**
     *
     * @return
     */
    public ArrayList<EquipoDTO> listarEquipos() {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        ArrayList<EquipoDTO> equipos = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            equipos = new EquipoDAO().listarEquipos(conexion);
            if (equipos.isEmpty()) {
                throw new Exception("ERROR: No hay arbitros registrados");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return equipos;
    }

    /**
     *
     * @param jugador
     * @param usuario
     * @return
     */
    public RespuestaDTO registrarJugador(JugadorDTO jugador, UsuarioDTO usuario) {
        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = null, respuesta2 = new RespuestaDTO();
        String idEquipo = "";

        try {
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);
            conexion.setAutoCommit(false);

            respuesta = registrarUsuario(usuario);
            if (!respuesta.isRegistro()) {
                respuesta.setMensaje("No se pudo registrar el usuario");
                conexion.rollback();
                throw new Exception("ERROR: no se pudo registrar el usuario");
            }
            //    idEquipo = new TecnicoDAO().obtenerIdEquipoPorIdTecnicoLogueado(conexion, jugador.getIdEquipo());
            jugador.setIdUsuario(respuesta.getIdResgistrado());

            respuesta2 = new JugadorDAO().registrarJugador(conexion, jugador);

            if (!respuesta2.isRegistro()) {
                respuesta2.setMensaje("No se pudo registrar el jugador");
                conexion.rollback();
                throw new Exception("ERROR: no se pudo registrar el jugador");

            }

            Correo envio = new Correo(usuario.getCorreo(), usuario.getClave(), "1", jugador.getNombre(), "1", "ninguna", usuario.getUsuario());
            envio.start();

            respuesta.setMensaje("Se ha registrado el jugador satisfactoriamente");
            conexion.commit();
            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return respuesta;
    }

    /**
     *
     * @return
     */
    public ArrayList<JugadorDTO> listarJugadores() {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        ArrayList<JugadorDTO> jugadores = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            jugadores = new JugadorDAO().listarJugadores(conexion);
            if (jugadores.isEmpty()) {
                throw new Exception("ERROR: No hay arbitros registrados");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return jugadores;
    }

    /**
     *
     * @param fechaInicio
     * @param idTemporada
     * @return
     */
    public RespuestaDTO sorteoDePartidos(String fechaInicio, String idTemporada) {
        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = null;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        ArrayList<EquipoDTO> equipos = null;
        ArrayList<HorarioDTO> horarios = null;
        PartidoDTO partido = null;
        ArrayList<PartidoDTO> partidos = new ArrayList<>();
        int dia = 1, nEquipos = 0;

        try {
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);
            conexion.setAutoCommit(false);
            calendar.setTime(formato.parse(fechaInicio));
            horarios = new HorarioDAO().listarHorarios(conexion);
            equipos = new EquipoDAO().listarEquipos(conexion);

            int nPartidosPorJornada = equipos.size() / 2;
            nEquipos = equipos.size() - 1;
            for (int i = 0; i < nEquipos; i++) {
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    calendar.add(calendar.DAY_OF_YEAR, dia);
                }

                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

                    for (int m = 0; m < horarios.size(); m++) {
                        if (nPartidosPorJornada > 0) {
                            partido = new PartidoDTO();
                            partido.setFechaPartido(formato.format(calendar.getTime()));
                            partido.setEstado("0");
                            partido.setTemporada(idTemporada);
                            partido.setIdHorario(horarios.get(m).getId());
                            partido.setJornada(i + 1);
                            respuesta = new PartidoDAO().registrarPartido(conexion, partido);
                            partido.setId(respuesta.getIdResgistrado());
                            partidos.add(partido);
                            System.out.println("se registro el partido con el id " + respuesta.getIdResgistrado());
                            if (respuesta.isRegistro()) {
                                nPartidosPorJornada--;
                            } else {
                                System.out.println("no se registro el partido");
                            }
                        }
                    }

                    calendar.add(calendar.DAY_OF_YEAR, dia);
                    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

                        for (int h = 0; h < horarios.size(); h++) {
                            if (nPartidosPorJornada > 0) {
                                partido = new PartidoDTO();
                                partido.setFechaPartido(formato.format(calendar.getTime()));
                                partido.setEstado("0");
                                partido.setTemporada(idTemporada);
                                partido.setIdHorario(horarios.get(h).getId());
                                partido.setJornada(i + 1);
                                respuesta = new PartidoDAO().registrarPartido(conexion, partido);
                                partido.setId(respuesta.getIdResgistrado());
                                partidos.add(partido);
                                if (respuesta.isRegistro()) {
                                    nPartidosPorJornada--;
                                } else {
                                    System.out.println("no se registro elpartido");
                                }
                            }
                        }
                        nPartidosPorJornada = equipos.size() / 2;
                        calendar.add(calendar.DAY_OF_YEAR, dia);
                    }
                }
            }

            respuesta = new TemporadaDAO().activarBanderaSorteoTemporada(conexion, idTemporada, "1");

            respuesta.setMensaje("Se ha realizado el sorteo!");
            conexion.commit();
            conexion.close();
            conexion = null;
            sorteoDeEquipos(partidos, equipos);

        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return respuesta;
    }

    /**
     *
     * @param usuario
     * @return
     */
    public RespuestaDTO registrarUsuario(UsuarioDTO usuario) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = null;
        ArrayList<FuncionalidadDTO> listadoFuncionalidades = null;
        FuncionalidadDTO funcionalidad;

        String password = "";
        try {
            password = UUID.randomUUID().toString().substring(0, 8);
            usuario.setClave(password);
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);
            conexion.setAutoCommit(false);

            respuesta = new UsuarioDAO().registrarUsuario(conexion, usuario);
            if (!respuesta.isRegistro()) {
                respuesta.setMensaje("No se pudo registrar el usuario");
                conexion.rollback();
                throw new Exception("ERROR: no se pudo registrar el usuario");
            }

            listadoFuncionalidades = new FuncionalidadesDAO().listarFuncionalidadesPorTipoUsuario(conexion, usuario.getIdTipoUsuario());
            String idUsuario = respuesta.getIdResgistrado();
            if (listadoFuncionalidades != null) {
                for (int i = 0; i < listadoFuncionalidades.size(); i++) {
                    funcionalidad = listadoFuncionalidades.get(i);
                    System.out.println("funcionalidad : " + funcionalidad.toStringJson());
                    System.out.println("Usuario :::" + idUsuario);
                    if (!new FuncionalidadesDAO().registrarUsuarioFuncionalidad(conexion, idUsuario, funcionalidad.getId())) {
                        conexion.rollback();
                        throw new Exception("Error : No se pudo realizar el registro de la pagina del usuario ");
                    }
                }

            }

            respuesta.setMensaje("Se ha registrado el arbitro satisfactoriamente");
            conexion.commit();
            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return respuesta;
    }

    /**
     *
     * @return
     */
    public ArrayList<PosicionDTO> listarPosicionDeJuego() {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        ArrayList<PosicionDTO> posiciones = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            posiciones = new PosicionDAO().listarPosiciones(conexion);
            if (posiciones.isEmpty()) {
                throw new Exception("ERROR: listando las posiciones");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return posiciones;
    }

    /**
     *
     * @param idEquipo
     * @return
     */
    public ArrayList<JugadorDTO> listarJugadoresPorIdEquipo(String idEquipo) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        ArrayList<JugadorDTO> jugadores = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            jugadores = new JugadorDAO().listarJugadoresPorIdEquipo(conexion, idEquipo);
            if (jugadores.isEmpty()) {
                throw new Exception("ERROR: No hay arbitros registrados");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return jugadores;
    }

    /**
     * *
     *
     * @param id
     * @return
     */
    public JugadorDTO buscarJugadorPorID(String id) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        JugadorDTO jugador = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            jugador = new JugadorDAO().buscarJugadorPorId(conexion, id);
            if (jugador == null) {
                throw new Exception("ERROR: No se encontro el jugador");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return jugador;
    }

    /**
     *
     * @param encuentros
     * @param equipos
     * @return
     */
    public RespuestaDTO sorteoDeEquipos(ArrayList<PartidoDTO> encuentros, ArrayList<EquipoDTO> equipos) {
        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = null;
        ArrayList<PartidoDTO> partidosFinal = new ArrayList<>();
        int idPartido = Integer.parseInt(encuentros.get(0).getId());
        try {
            System.out.println("entro a la funcion de sorteo equipos");
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);
            conexion.setAutoCommit(false);

            for (int i = 0; i < equipos.size() - 1; i++) {
                cruzarEquipos(equipos.size() - 1, equipos, partidosFinal);
                combinar(equipos.size() - 1, equipos);
            }

            for (PartidoDTO part : partidosFinal) {
                // System.out.println("entra al for de registros");
                part.setId("" + idPartido);
                respuesta = new PartidoDAO().registrarEncuentro(conexion, part);
                if (respuesta == null) {
                    throw new Exception("ERROR no se pudo registrar el partido");
                }
                System.out.println("ID :" + part.getId() + " ---equipo-> " + part.getEquipoA() + "  vs  equipo ->" + part.getEquipoB());
                idPartido++;
            }

            conexion.commit();
            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return respuesta;
    }

    /**
     *
     * @param longitud
     * @param equipos
     * @param partidos
     * @return
     */
    public ArrayList<PartidoDTO> cruzarEquipos(int longitud, ArrayList<EquipoDTO> equipos, ArrayList<PartidoDTO> partidos) {
        // ArrayList<PartidoDTO> partidos = new ArrayList<>();
        PartidoDTO partido = null;

        for (int i = 0, j = longitud; i < j; i++, j--) {
            partido = new PartidoDTO();
            partido.setEquipoA(equipos.get(i).getId());
            partido.setEquipoB(equipos.get(j).getId());
            partidos.add(partido);
        }

        return partidos;
    }

    /**
     *
     * @param longitud
     * @param equipos
     * @return
     */
    public ArrayList<EquipoDTO> combinar(int longitud, ArrayList<EquipoDTO> equipos) {
        EquipoDTO ultimoEquipo = equipos.get(longitud);

        for (int i = longitud; i > 1; i--) {
            equipos.set(i, equipos.get(i - 1));
        }

        equipos.set(1, ultimoEquipo);

        return equipos;
    }

    /**
     *
     * @param idTecnico
     * @return
     */
    public TecnicoDTO buscarTecnicoPorId(String idTecnico) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        TecnicoDTO tecnico = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            tecnico = new TecnicoDAO().buscarTecnicoPorId(conexion, idTecnico);
            if (tecnico == null) {
                throw new Exception("ERROR: No se encontro el tecnico");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return tecnico;
    }

    /**
     *
     * @param idArbitro
     * @return
     */
    public ArbitroDTO buscarArbitroPorId(String idArbitro) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        ArbitroDTO arbitro = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            arbitro = new ArbitroDAO().buscarArbitroPorId(conexion, idArbitro);
            if (arbitro == null) {
                throw new Exception("ERROR: No se encontro el arbitro");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return arbitro;
    }

    /**
     *
     * @param arbitro
     * @return
     */
    public RespuestaDTO actualizarArbitro(ArbitroDTO arbitro) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = new RespuestaDTO();

        try {
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            respuesta = new ArbitroDAO().actualizarArbitro(conexion, arbitro);
            if (!respuesta.isRegistro()) {
                respuesta.setMensaje("No se pudo actualizar el arbitro");
                throw new Exception("ERROR: No se pudo actualizar el arbitro");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return respuesta;
    }

    /**
     *
     * @param tecnico
     * @return
     */
    public RespuestaDTO actualizarTecnico(TecnicoDTO tecnico) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = new RespuestaDTO();

        try {
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            respuesta = new TecnicoDAO().actualizarTecnico(conexion, tecnico);
            if (!respuesta.isRegistro()) {
                respuesta.setMensaje("No se pudo actualizar el tecnico");
                throw new Exception("ERROR: No se pudo actualizar el tecnico");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return respuesta;
    }

    /**
     *
     * @param jugador
     * @return
     */
    public RespuestaDTO actualizarJugador(JugadorDTO jugador) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = new RespuestaDTO();

        try {
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            respuesta = new JugadorDAO().actualizarJugador(conexion, jugador);
            if (!respuesta.isRegistro()) {
                respuesta.setMensaje("No se pudo actualizar el jugador");
                throw new Exception("ERROR: No se pudo actualizar el jugador");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return respuesta;
    }

    /**
     *
     * @param idEquipo
     * @return
     */
    public EquipoDTO buscarEquipoPorId(String idEquipo) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        EquipoDTO equipo = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            equipo = new EquipoDAO().buscarEquipoPorId(conexion, idEquipo);
            if (equipo == null) {
                throw new Exception("ERROR: No se encontro el jugador");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return equipo;
    }

    /**
     *
     * @param equipo
     * @return
     */
    public RespuestaDTO actualizarEquipo(EquipoDTO equipo) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = new RespuestaDTO();

        try {
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            respuesta = new EquipoDAO().actualizarEquipo(conexion, equipo);
            if (!respuesta.isRegistro()) {
                respuesta.setMensaje("No se pudo actualizar el equipo");
                throw new Exception("ERROR: No se pudo actualizar el equipo");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return respuesta;
    }

    /**
     *
     * @param idTemporada
     * @return
     */
    public ArrayList<PartidoDTO> listarPartidosPorIdTemporada(String idTemporada) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        ArrayList<PartidoDTO> partidos = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            partidos = new PartidoDAO().listarPartidosPorIdTemporada(conexion, idTemporada);
            if (partidos.isEmpty()) {
                throw new Exception("ERROR: listando las posiciones");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return partidos;
    }

    /**
     *
     * @return
     */
    public ArrayList<TemporadaDTO> listarTemporadas() {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        ArrayList<TemporadaDTO> temporadas = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            temporadas = new TemporadaDAO().listarTemporadas(conexion);
            if (temporadas.isEmpty()) {
                throw new Exception("ERROR: listando las temporadas");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return temporadas;
    }

    /**
     *
     * @param temporada
     * @return
     */
    public RespuestaDTO registrarTemporada(TemporadaDTO temporada) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = null;

        try {
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            respuesta = new TemporadaDAO().registrarTemporada(conexion, temporada);
            if (!respuesta.isRegistro()) {
                respuesta.setMensaje("No se pudo registrar la temporada");
                conexion.rollback();
                throw new Exception("ERROR: no se pudo registrar  la temporada");
            }

            respuesta.setMensaje("Se ha registrado  la temporada satisfactoriamente");
            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return respuesta;
    }

    /**
     *
     * @param idTemporada
     * @return
     */
    public TemporadaDTO buscarTemporadaPorId(String idTemporada) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        TemporadaDTO temp = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            temp = new TemporadaDAO().buscarTemporadaPorId(conexion, idTemporada);
            if (temp == null) {
                throw new Exception("ERROR: No se encontro la temporada");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return temp;
    }

    /**
     *
     * @param temp
     * @return
     */
    public RespuestaDTO actualizarTemporada(TemporadaDTO temp) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = new RespuestaDTO();

        try {
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            respuesta = new TemporadaDAO().actualizarTemporada(conexion, temp);
            if (!respuesta.isRegistro()) {
                respuesta.setMensaje("No se pudo actualizar el equipo");
                throw new Exception("ERROR: No se pudo actualizar el equipo");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return respuesta;
    }

    /**
     *
     * @return
     */
    public RequisitoDTO arbitrosEquiposActivos() {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RequisitoDTO requisito = new RequisitoDTO();
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            requisito.setArbitrosActivos(new ArbitroDAO().arbitrosActivos(conexion));
            requisito.setEquiposActivos(new EquipoDAO().equiposActivos(conexion));

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return requisito;
    }

    /**
     *
     * @param correo
     * @return
     */
    public boolean validarCorreo(String correo) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        boolean existe = false;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            existe = new UsuarioDAO().validarCorreo(conexion, correo);

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return existe;
    }

    /**
     *
     * @param usuario
     * @return
     */
    public boolean validarUsuario(String usuario) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        boolean existe = false;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            existe = new UsuarioDAO().validarUsuarios(conexion, usuario);

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return existe;
    }

    /**
     *
     * @param bandera
     * @param documento
     * @return
     */
    public boolean validarDocumento(String documento, String bandera) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        boolean existe = false;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            switch (bandera) {
                case "arbitro":
                    existe = new ArbitroDAO().validarDocumentoArbitro(conexion, documento);
                    break;
                case "tecnico":
                    existe = new TecnicoDAO().validarDocumentoTecnico(conexion, documento);
                    break;
                case "jugador":
                    existe = new JugadorDAO().validarDocumentoJugador(conexion, documento);
                    break;
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }
        }
        return existe;
    }

    /**
     *
     * @return
     */
    public ArrayList<TemporadaDTO> listarTemporadaEnProceso() {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        ArrayList<TemporadaDTO> temporadas = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            temporadas = new TemporadaDAO().listarTemporadasEnProceso(conexion);
            if (temporadas.isEmpty()) {
                throw new Exception("ERROR: listando las temporadas");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return temporadas;
    }

    /**
     *
     * @return
     */
    public ArrayList<ArbitroDTO> listarArbitrosActivos() {
        DataBaseConnection dbcon = null;
        Connection conexion = null;
        ArrayList<ArbitroDTO> arbitros = null;
        try {
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            arbitros = new ArbitroDAO().listarArbitrosActivos(conexion);
            if (arbitros.isEmpty()) {
                throw new Exception("ERROR: No hay arbitros registrados");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return arbitros;
    }

    /**
     *
     * @param arbitraje
     * @return
     */
    public RespuestaDTO asignarArbitraje(ArbitrajeDTO arbitraje) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = null;

        try {
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);
            conexion.setAutoCommit(false);

            respuesta = new ArbitroDAO().asignarArbitroPartido(conexion, arbitraje.getArbitroCentral(), arbitraje.getIdPartido(), "1");
            if (!respuesta.isRegistro()) {
                respuesta.setMensaje("No se asignar el arbitro");
                conexion.rollback();
                throw new Exception("ERROR: No se asignar el arbitro");
            }

            respuesta = new ArbitroDAO().asignarArbitroPartido(conexion, arbitraje.getAsistente1(), arbitraje.getIdPartido(), "0");
            if (!respuesta.isRegistro()) {
                respuesta.setMensaje("No se asignar el arbitro");
                conexion.rollback();
                throw new Exception("ERROR:No se asignar el arbitro");
            }

            respuesta = new ArbitroDAO().asignarArbitroPartido(conexion, arbitraje.getAsistente2(), arbitraje.getIdPartido(), "0");
            if (!respuesta.isRegistro()) {
                respuesta.setMensaje("No se asignar el arbitro");
                conexion.rollback();
                throw new Exception("ERROR: No se asignar el arbitro");
            }

            respuesta.setMensaje("Se ha registrado el arbitro satisfactoriamente");
            conexion.commit();
            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return respuesta;
    }

    /**
     *
     * @param idArbitro
     * @return
     */
    public ArrayList<PartidoDTO> listarPartidosPorIdArbitroCentral(String idArbitro) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        ArrayList<PartidoDTO> partidos = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            partidos = new PartidoDAO().listarPartidosPorIdArbitroCentral(conexion, idArbitro);
            if (partidos.isEmpty()) {
                throw new Exception("ERROR: listando las posiciones");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return partidos;
    }

    /**
     *
     * @param dorsal
     * @param idEquipo
     * @return
     */
    public boolean validarDorsalEquipo(String dorsal, String idEquipo) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        boolean existe = false;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            existe = new JugadorDAO().validarDorsalJugador(conexion, dorsal, idEquipo);

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }
        }
        return existe;
    }

    /**
     *
     * @return
     */
    public boolean validarTemporadaEnProceso() {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        boolean existe = false;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            existe = new TemporadaDAO().validarTemporadaEnProceso(conexion);

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return existe;
    }

    /**
     *
     * @param idTecnico
     * @return
     */
    public ArrayList<EquipoDTO> listarTodosLosEquiposPorTecnico(String idTecnico) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        ArrayList<EquipoDTO> equipos = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            equipos = new EquipoDAO().listarTodosLosEquiposPorTecnico(conexion, idTecnico);
            if (equipos.isEmpty()) {
                throw new Exception("ERROR: No hay arbitros registrados");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return equipos;
    }

    /**
     *
     * @param idPartido
     * @return
     */
    public PartidoDTO verInformacionDePartidoPorId(String idPartido) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        PartidoDTO partido = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            partido = new PartidoDAO().verInformacionDePartidoPorId(conexion, idPartido);
            if (partido == null) {
                throw new Exception("ERROR: listando las posiciones");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return partido;
    }

    /**
     *
     * @param idTemporada
     * @return
     */
    public ArrayList<PartidoDTO> listarPartidosPorIdTemporadaConArbitro(String idTemporada) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        ArrayList<PartidoDTO> partidos = null;
        try {

            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);

            partidos = new PartidoDAO().listarPartidosPorIdTemporadaConArbitro(conexion, idTemporada);
            if (partidos.isEmpty()) {
                throw new Exception("ERROR: listando las posiciones");
            }

            conexion.close();
            conexion = null;
        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
        } finally {
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                    conexion = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
            } finally {
                try {
                    if (conexion != null && !conexion.isClosed()) {
                        conexion.close();
                        conexion = null;
                    }

                } catch (Exception e) {
                    LoggerMessage.getInstancia().loggerMessageException(e);
                }
            }

        }
        return partidos;
    }

}
