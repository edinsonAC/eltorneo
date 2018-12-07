/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.eltorneo.mvc.dto;

import co.eltorneo.common.util.Generales;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;

/**
 *
 * @author Administrador
 */
public class PartidoDTO implements Serializable {

    String id = Generales.EMPTYSTRING;
    String fechaPartido = Generales.EMPTYSTRING;
    String idHorario = Generales.EMPTYSTRING;
    String estado = Generales.EMPTYSTRING;
    String equipoA = Generales.EMPTYSTRING;
    String equipoB = Generales.EMPTYSTRING;
    String temporada = Generales.EMPTYSTRING;
    String idEquipo = Generales.EMPTYSTRING;
    String horaInicial = Generales.EMPTYSTRING;
    String horaFinal = Generales.EMPTYSTRING;
    String nombreEquipoA = Generales.EMPTYSTRING;
    String nombreEquipoB = Generales.EMPTYSTRING;
    String arbitro = Generales.EMPTYSTRING;
    int jornada = Generales.ZEROVALUE;
    int golesA = Generales.ZEROVALUE;
    int golesB = Generales.ZEROVALUE;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFechaPartido() {
        return fechaPartido;
    }

    public void setFechaPartido(String fechaPartido) {
        this.fechaPartido = fechaPartido;
    }

    public String getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(String idHorario) {
        this.idHorario = idHorario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getJornada() {
        return jornada;
    }

    public void setJornada(int jornada) {
        this.jornada = jornada;
    }

    public String getEquipoA() {
        return equipoA;
    }

    public void setEquipoA(String equipoA) {
        this.equipoA = equipoA;
    }

    public String getEquipoB() {
        return equipoB;
    }

    public void setEquipoB(String equipoB) {
        this.equipoB = equipoB;
    }

    public String getTemporada() {
        return temporada;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(String horaInicial) {
        this.horaInicial = horaInicial;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }

    public int getGolesA() {
        return golesA;
    }

    public void setGolesA(int golesA) {
        this.golesA = golesA;
    }

    public int getGolesB() {
        return golesB;
    }

    public void setGolesB(int golesB) {
        this.golesB = golesB;
    }

    public String getNombreEquipoA() {
        return nombreEquipoA;
    }

    public void setNombreEquipoA(String nombreEquipoA) {
        this.nombreEquipoA = nombreEquipoA;
    }

    public String getNombreEquipoB() {
        return nombreEquipoB;
    }

    public void setNombreEquipoB(String nombreEquipoB) {
        this.nombreEquipoB = nombreEquipoB;
    }

    public String getArbitro() {
        return arbitro;
    }

    public void setArbitro(String arbitro) {
        this.arbitro = arbitro;
    }

    public String toStringJson() {
        String dtoJsonString = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            dtoJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
        }
        return dtoJsonString;
    }

}
