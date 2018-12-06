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
public class ArbitrajeDTO implements Serializable {

    String arbitroCentral = Generales.EMPTYSTRING;
    String asistente1 = Generales.EMPTYSTRING;
    String asistente2 = Generales.EMPTYSTRING;
    String idPartido = Generales.EMPTYSTRING;

    public String getArbitroCentral() {
        return arbitroCentral;
    }

    public void setArbitroCentral(String arbitroCentral) {
        this.arbitroCentral = arbitroCentral;
    }

    public String getAsistente1() {
        return asistente1;
    }

    public void setAsistente1(String asistente1) {
        this.asistente1 = asistente1;
    }

    public String getAsistente2() {
        return asistente2;
    }

    public void setAsistente2(String asistente2) {
        this.asistente2 = asistente2;
    }

    public String getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(String idPartido) {
        this.idPartido = idPartido;
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
