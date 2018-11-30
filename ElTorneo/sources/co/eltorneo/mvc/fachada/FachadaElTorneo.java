/*
 * 
 *
 * Proyecto: ElTorneo
 * Copyright EdinsonAC
 * All rights reserved
 */
package co.eltorneo.mvc.fachada;

import co.eltorneo.mvc.dto.ArbitroDTO;
import co.eltorneo.mvc.dto.EquipoDTO;
import co.eltorneo.mvc.dto.JugadorDTO;
import co.eltorneo.mvc.dto.PosicionDTO;
import co.eltorneo.mvc.dto.RespuestaDTO;
import co.eltorneo.mvc.dto.TecnicoDTO;
import co.eltorneo.mvc.dto.UsuarioDTO;
import co.eltorneo.mvc.mediador.MediadorElTorneo;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.annotations.ScriptScope;

/**
 *
 * @author Edinson AC
 */
@RemoteProxy(name = "ajaxElTorneo", scope = ScriptScope.SESSION)
public class FachadaElTorneo {

    /**
     *
     */
    public FachadaElTorneo() {
    }

    /**
     *
     * @return
     */
    @RemoteMethod
    public boolean servicioActivo() {
        return true;
    }

    /**
     *
     * @param documento
     * @return
     */
    public UsuarioDTO recuperarContrassenia(String documento) {
        return MediadorElTorneo.getInstancia().recuperarContrasenia(documento);
    }

    /**
     * aqui usted llama los metodos que hizo en el mediador y el dao, es como el
     * puente entre la vista y codigo
     *
     * @param tecnico
     * @param usuario
     * @return
     */
    @RemoteMethod //para que en los jsp le reconozca el metodo debe estar esta anotacion @RemoteMethod
    public RespuestaDTO registrarTecnico(TecnicoDTO tecnico, UsuarioDTO usuario) { //declara el nombre del metodo como lo va a llamar en el jsp
        return MediadorElTorneo.getInstancia().registrarTecnico(tecnico, usuario); // llama los metodos que hizo en el mediador
    }

    /**
     *
     * @return
     */
    @RemoteMethod
    public ArrayList<TecnicoDTO> listarTecnicos() {
        return MediadorElTorneo.getInstancia().listarTecnicos();
    }

    /**
     *
     * @param arbitro
     * @param usuario
     * @return
     */
    @RemoteMethod
    public RespuestaDTO registrarArbitro(ArbitroDTO arbitro, UsuarioDTO usuario) {
        return MediadorElTorneo.getInstancia().registrarArbitro(arbitro, usuario);
    }

    /**
     *
     * @return
     */
    @RemoteMethod
    public ArrayList<ArbitroDTO> listarArbitros() {
        return MediadorElTorneo.getInstancia().listarArbitros();
    }

    /**
     *
     * @param equipo
     * @return
     */
    @RemoteMethod
    public RespuestaDTO registrarEquipo(EquipoDTO equipo) {
        return MediadorElTorneo.getInstancia().registrarEquipo(equipo);
    }

    /**
     *
     * @return
     */
    @RemoteMethod
    public ArrayList<EquipoDTO> listarEquipos() {
        return MediadorElTorneo.getInstancia().listarEquipos();
    }

    /**
     *
     * @param equipo
     * @param usuario
     * @return
     */
    @RemoteMethod
    public RespuestaDTO registrarJugador(JugadorDTO equipo, UsuarioDTO usuario) {
        return MediadorElTorneo.getInstancia().registrarJugador(equipo, usuario);
    }

    /**
     *
     * @return
     */
    @RemoteMethod
    public ArrayList<JugadorDTO> listarJugadores() {
        return MediadorElTorneo.getInstancia().listarJugadores();
    }

    /**
     *
     * @param fechaInicio
     * @return
     */
    @RemoteMethod
    public RespuestaDTO sorteoDePartidos(String fechaInicio) {
        return MediadorElTorneo.getInstancia().sorteoDePartidos(fechaInicio);
    }

    /**
     *
     * @return
     */
    @RemoteMethod
    public ArrayList<PosicionDTO> listarPosicionesDeJuego() {
        return MediadorElTorneo.getInstancia().listarPosicionDeJuego();
    }

    /**
     *
     * @param idEquipo
     * @return
     */
    @RemoteMethod
    public ArrayList<JugadorDTO> listarJugadoresPorIdEquipo(String idEquipo) {
        return MediadorElTorneo.getInstancia().listarJugadoresPorIdEquipo(idEquipo);
    }

    /**
     *
     * @param id
     * @return
     */
    @RemoteMethod
    public JugadorDTO buscarJugadorPorId(String id) {
        return MediadorElTorneo.getInstancia().buscarJugadorPorID(id);
    }
}
