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
import co.eltorneo.mvc.dao.UsuarioDAO;
import co.eltorneo.mvc.dto.ArbitroDTO;
import co.eltorneo.mvc.dto.EquipoDTO;
import co.eltorneo.mvc.dto.FuncionalidadDTO;
import co.eltorneo.mvc.dto.HorarioDTO;
import co.eltorneo.mvc.dto.JugadorDTO;
import co.eltorneo.mvc.dto.PartidoDTO;
import co.eltorneo.mvc.dto.PosicionDTO;
import co.eltorneo.mvc.dto.RespuestaDTO;
import co.eltorneo.mvc.dto.TecnicoDTO;
import co.eltorneo.mvc.dto.UsuarioDTO;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.util.jar.Pack200;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
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
            return datosUsuario;
        }

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

            // esta es la parte donde envio el correo, to lo meti en un hilo para que se vaya enviando en paralelo y sea mas rapido el metodo
            Correo envio = new Correo(usuario.getCorreo(), usuario.getClave(), "1", tecnico.getNombre(), "1", "ninguna"); //el contructor del hilo
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
            return respuesta;
        }

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
            return tecnicos;
        }

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
            arbitro.setIdUsuario(respuesta.getIdResgistrado());
            respuesta2 = new ArbitroDAO().registrarArbitro(conexion, arbitro);

            if (!respuesta2.isRegistro()) {
                respuesta2.setMensaje("No se pudo registrar el arbitro");
                conexion.rollback();
                throw new Exception("ERROR: no se pudo registrar el arbitro");

            }

            Correo envio = new Correo(usuario.getCorreo(), usuario.getClave(), "1", arbitro.getNombres(), "1", "ninguna");
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
            return respuesta;
        }

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
            return arbitros;
        }

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
            return respuesta;
        }

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
            return equipos;
        }

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
            jugador.setIdUsuario(respuesta.getIdResgistrado());
            respuesta2 = new JugadorDAO().registrarJugador(conexion, jugador);

            if (!respuesta2.isRegistro()) {
                respuesta2.setMensaje("No se pudo registrar el jugador");
                conexion.rollback();
                throw new Exception("ERROR: no se pudo registrar el jugador");

            }

            Correo envio = new Correo(usuario.getCorreo(), usuario.getClave(), "1", jugador.getNombre(), "1", "ninguna");
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
            return respuesta;
        }

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
            return jugadores;
        }

    }

    /**
     *
     * @param fechaInicio
     * @return
     */
    public RespuestaDTO sorteoDePartidos(String fechaInicio) {
        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = null;
        RespuestaDTO respuesta2 = null;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        ArrayList<EquipoDTO> equipos = null;
        ArrayList<EquipoDTO> equipos2 = null;
        ArrayList<HorarioDTO> horarios = null;
        PartidoDTO partido = null;
        PartidoDTO partidoJuego = null;
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
            byte y = 0, l = 0;
            for (int i = 0; i < nEquipos; i++) {
                y = 0;
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    calendar.add(calendar.DAY_OF_YEAR, dia);
                }

                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

                    for (int m = 0; m < horarios.size(); m++) {
                        if (nPartidosPorJornada > 0) {
                            partido = new PartidoDTO();
                            partido.setFechaPartido(formato.format(calendar.getTime()));
                            partido.setEstado("0");
                            partido.setTemporada("1");
                            partido.setIdHorario(horarios.get(m).getId());
                            partido.setJornada(i + 1);
                            respuesta = new PartidoDAO().registrarPartido(conexion, partido);
                            System.out.println("se registro el partido con el id " + respuesta.getIdResgistrado());
                            if (respuesta.isRegistro()) {
                                System.out.println("va a validar el equipo -> " + equipos.get(y).getId() + "en la jornada " + partido.getJornada());
                                while (new PartidoDAO().validarEquipoPorJornada(conexion, equipos.get(y).getId(), partido.getJornada())) {
                                    System.out.println("si existe el equipo -> " + equipos.get(y).getId() + "en la jornada " + partido.getJornada());
                                    y++;
                                }
                                System.out.println("paso el equipo con -> " + equipos.get(y).getId() + "en la jornada " + partido.getJornada());

                                partidoJuego = new PartidoDTO();
                                partidoJuego.setId(respuesta.getIdResgistrado());
                                partidoJuego.setEquipoA(equipos.get(y).getId());

                                y = 0;
                                System.out.println("va a validar el equipo -> " + equipos.get(l).getId() + "en la jornada " + partido.getJornada());
                                while (new PartidoDAO().validarEquipoPorJornada(conexion, equipos.get(l).getId(), partido.getJornada())) {
                                    System.out.println("si existe el equipo -> " + equipos.get(l).getId() + "en la jornada " + partido.getJornada());
                                    l++;
                                }

                                System.out.println("paso el equipo con -> " + equipos.get(y).getId() + "en la jornada " + partido.getJornada());
                                partidoJuego.setEquipoB(equipos.get(y).getId());

                                while (new PartidoDAO().validarEquipoPorPartidos(conexion, partido.getEquipoA(), partido.getEquipoB())) {
                                    System.out.println("ya jugaron entre ellos  ");
                                    y++;
                                }

                                respuesta2 = new PartidoDAO().registrarPartidoJuego(conexion, partidoJuego);
                                nPartidosPorJornada--;
                                y = 0;
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
                                partido.setTemporada("1");
                                partido.setIdHorario(horarios.get(h).getId());
                                partido.setJornada(i + 1);
                                respuesta = new PartidoDAO().registrarPartido(conexion, partido);
                                System.out.println("se registro el partido con el id " + respuesta.getIdResgistrado());
                                if (respuesta.isRegistro()) {
                                    System.out.println("va a validar el equipo -> " + equipos.get(y).getId() + "en la jornada " + partido.getJornada());
                                    while (new PartidoDAO().validarEquipoPorJornada(conexion, equipos2.get(y).getId(), partido.getJornada())) {
                                        System.out.println("si existe el equipo -> " + equipos.get(y).getId() + "en la jornada " + partido.getJornada());
                                        y++;
                                    }
                                    System.out.println("paso el equipo con -> " + equipos2.get(y).getId() + "en la jornada " + partido.getJornada());

                                    partidoJuego = new PartidoDTO();
                                    partidoJuego.setId(respuesta.getIdResgistrado());
                                    partidoJuego.setEquipoA(equipos.get(y).getId());

                                    y = 0;
                                    System.out.println("va a validar el equipo -> " + equipos.get(y).getId() + "en la jornada " + partido.getJornada());
                                    while (new PartidoDAO().validarEquipoPorJornada(conexion, equipos.get(y).getId(), partido.getJornada())) {
                                        y++;
                                        System.out.println("si existe el equipo -> " + equipos.get(y).getId() + "en la jornada " + partido.getJornada());
                                    }
                                    System.out.println("paso el equipo con -> " + equipos.get(y).getId() + "en la jornada " + partido.getJornada());

                                    partidoJuego.setEquipoB(equipos.get(y).getId());
                                    respuesta2 = new PartidoDAO().registrarPartidoJuego(conexion, partidoJuego);
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

            respuesta.setMensaje("Se ha realizado el sorteo!");
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
            return respuesta;
        }

    }

    public RespuestaDTO sorteoDeEquipos(String fechaInicio) {
        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = null;
        ArrayList<EquipoDTO> equipos = null;
        ArrayList<PartidoDTO> partidos = null;
        PartidoDTO partido = null;
        int dia = 1;

        try {
            dbcon = DataBaseConnection.getInstance();
            conexion = dbcon.getConnection(ContextDataResourceNames.MYSQL_ELTORNEO_JDBC);
            conexion.setAutoCommit(false);

            respuesta = sorteoDePartidos(fechaInicio);
            if (respuesta == null) {
                respuesta.setMensaje("No se pudo realizar el sorteo de partidos");
                throw new Exception("ERROR: No se pudo realizar el sorteo de partidos");
            } else {
                partidos = new PartidoDAO().listarPartidos(conexion);
            }

            respuesta.setMensaje("Se ha realizado el sorteo!");
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
            return respuesta;
        }

    }

    /**
     *
     * @param usuario
     * @return
     */
    public RespuestaDTO registrarUsuario(UsuarioDTO usuario) {

        DataBaseConnection dbcon = null;
        Connection conexion = null;
        RespuestaDTO respuesta = null, respuesta2 = new RespuestaDTO();
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
            String idUsuario = usuario.getId();
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
            return respuesta;
        }

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
            return posiciones;
        }

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
            return jugadores;
        }

    }
}
