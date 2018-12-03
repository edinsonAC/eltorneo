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
import co.eltorneo.mvc.dto.PartidoDTO;
import co.eltorneo.mvc.dto.PosicionDTO;
import co.eltorneo.mvc.dto.RequisitoDTO;
import co.eltorneo.mvc.dto.RespuestaDTO;
import co.eltorneo.mvc.dto.TecnicoDTO;
import co.eltorneo.mvc.dto.TemporadaDTO;
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
     * @param jugador
     * @param usuario
     * @return
     */
    @RemoteMethod
    public RespuestaDTO registrarJugador(JugadorDTO jugador, UsuarioDTO usuario) {
        return MediadorElTorneo.getInstancia().registrarJugador(jugador, usuario);
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
     * @param idTemporada
     * @return
     */
    @RemoteMethod
    public RespuestaDTO sorteoDePartidos(String fechaInicio, String idTemporada) {
        return MediadorElTorneo.getInstancia().sorteoDePartidos(fechaInicio, idTemporada);
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

    /**
     *
     * @param idTecnico
     * @return
     */
    @RemoteMethod
    public TecnicoDTO buscarTecnicoPorId(String idTecnico) {
        return MediadorElTorneo.getInstancia().buscarTecnicoPorId(idTecnico);
    }

    /**
     *
     * @param idArbitro
     * @return
     */
    @RemoteMethod
    public ArbitroDTO buscarArbitroPorId(String idArbitro) {
        return MediadorElTorneo.getInstancia().buscarArbitroPorId(idArbitro);
    }

    /**
     *
     * @param arbitro
     * @return
     */
    @RemoteMethod
    public RespuestaDTO actualizarArbitro(ArbitroDTO arbitro) {
        return MediadorElTorneo.getInstancia().actualizarArbitro(arbitro);
    }

    /**
     *
     * @param tecnico
     * @return
     */
    @RemoteMethod
    public RespuestaDTO actualizarTecnico(TecnicoDTO tecnico) {
        return MediadorElTorneo.getInstancia().actualizarTecnico(tecnico);
    }

    /**
     *
     * @param jugador
     * @return
     */
    @RemoteMethod
    public RespuestaDTO actualizarJugador(JugadorDTO jugador) {
        return MediadorElTorneo.getInstancia().actualizarJugador(jugador);
    }

    /**
     *
     * @param idEquipo
     * @return
     */
    @RemoteMethod
    public EquipoDTO buscarEquipoPorId(String idEquipo) {
        return MediadorElTorneo.getInstancia().buscarEquipoPorId(idEquipo);
    }

    /**
     *
     * @param equipo
     * @return
     */
    @RemoteMethod
    public RespuestaDTO actualizarEquipo(EquipoDTO equipo) {
        return MediadorElTorneo.getInstancia().actualizarEquipo(equipo);
    }

    /**
     *
     * @param idTemporada
     * @return
     */
    @RemoteMethod
    public ArrayList<PartidoDTO> listarPartidosPorIdTemporada(String idTemporada) {
        return MediadorElTorneo.getInstancia().listarPartidosPorIdTemporada(idTemporada);
    }

    /**
     *
     * @return
     */
    @RemoteMethod
    public ArrayList<TemporadaDTO> listarTemporadas() {
        return MediadorElTorneo.getInstancia().listarTemporadas();
    }

    /**
     *
     * @param temporada
     * @return
     */
    @RemoteMethod
    public RespuestaDTO registrarTemporada(TemporadaDTO temporada) {
        return MediadorElTorneo.getInstancia().registrarTemporada(temporada);
    }

    /**
     *
     * @param idTemporada
     * @return
     */
    @RemoteMethod
    public TemporadaDTO buscarTemporadaPorId(String idTemporada) {
        return MediadorElTorneo.getInstancia().buscarTemporadaPorId(idTemporada);
    }

    /**
     *
     * @param temporada
     * @return
     */
    @RemoteMethod
    public RespuestaDTO actualizarTemporada(TemporadaDTO temporada) {
        return MediadorElTorneo.getInstancia().actualizarTemporada(temporada);
    }

    /**
     *
     * @return
     */
    @RemoteMethod
    public RequisitoDTO arbitrosEquiposActivos() {
        return MediadorElTorneo.getInstancia().arbitrosEquiposActivos();
    }
}
