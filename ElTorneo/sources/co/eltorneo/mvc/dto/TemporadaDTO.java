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
public class TemporadaDTO implements Serializable {

    String id = Generales.EMPTYSTRING;
    String nombre = Generales.EMPTYSTRING;
    String fechaInicial = Generales.EMPTYSTRING;
    String fechaFinal = Generales.EMPTYSTRING;
    String numEquipos = Generales.EMPTYSTRING;
    String estado = Generales.EMPTYSTRING;
    String banderaSorteo = Generales.EMPTYSTRING;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getNumEquipos() {
        return numEquipos;
    }

    public void setNumEquipos(String numEquipos) {
        this.numEquipos = numEquipos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getBanderaSorteo() {
        return banderaSorteo;
    }

    public void setBanderaSorteo(String banderaSorteo) {
        this.banderaSorteo = banderaSorteo;
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
