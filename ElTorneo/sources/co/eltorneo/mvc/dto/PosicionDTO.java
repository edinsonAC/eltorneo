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
public class PosicionDTO implements Serializable {

    private String id = Generales.EMPTYSTRING;
    private String nombre = Generales.EMPTYSTRING;
    private String descripcion = Generales.EMPTYSTRING;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
