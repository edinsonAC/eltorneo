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
public class EquipoDTO implements Serializable {

    String id = Generales.EMPTYSTRING;
    String nombre = Generales.EMPTYSTRING;
    String registradoPor = Generales.EMPTYSTRING;
    String idTecnico = Generales.EMPTYSTRING;
    String tecnico = Generales.EMPTYSTRING;
    String estado = Generales.EMPTYSTRING;

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

    public String getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(String registradoPor) {
        this.registradoPor = registradoPor;
    }

    public String getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(String idTecnico) {
        this.idTecnico = idTecnico;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
