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
public class RequisitoDTO implements Serializable {

    String arbitrosActivos = Generales.EMPTYSTRING;
    String equiposActivos = Generales.EMPTYSTRING;

    public String getArbitrosActivos() {
        return arbitrosActivos;
    }

    public void setArbitrosActivos(String arbitrosActivos) {
        this.arbitrosActivos = arbitrosActivos;
    }

    public String getEquiposActivos() {
        return equiposActivos;
    }

    public void setEquiposActivos(String equiposActivos) {
        this.equiposActivos = equiposActivos;
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
