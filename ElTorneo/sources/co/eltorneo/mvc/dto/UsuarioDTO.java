/*
 * UsuarioDTO.java
 *
 * Proyecto: elTorneo
 * Cliente: UFPS
 * Copyright EdinsonAC
 * All rights reserved
 */
package co.eltorneo.mvc.dto;

import co.eltorneo.common.util.Generales;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.ArrayList;

public class UsuarioDTO implements Serializable {

    String id = Generales.EMPTYSTRING;
    String usuario = Generales.EMPTYSTRING;
    String nombre = Generales.EMPTYSTRING;
    String apellido = Generales.EMPTYSTRING;
    String documento = Generales.EMPTYSTRING;
    String idTipoUsuario = Generales.EMPTYSTRING;
    String direccion = Generales.EMPTYSTRING;
    String telefono = Generales.EMPTYSTRING;
    String correo = Generales.EMPTYSTRING;
    String imagenPerfil = Generales.EMPTYSTRING;
    String clave = Generales.EMPTYSTRING;
    String estado = Generales.EMPTYSTRING;
    String registradoPor = Generales.EMPTYSTRING;
    String fechaRegistro = Generales.EMPTYSTRING;
    String descripcionErrorLogueo = Generales.EMPTYSTRING;
    String idEquipo = Generales.EMPTYSTRING;
    ArrayList<MenuDTO> menu = new ArrayList<>();

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(String registradoPor) {
        this.registradoPor = registradoPor;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(String idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public ArrayList<MenuDTO> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<MenuDTO> menu) {
        this.menu = menu;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDescripcionErrorLogueo() {
        return descripcionErrorLogueo;
    }

    public void setDescripcionErrorLogueo(String descripcionErrorLogueo) {
        this.descripcionErrorLogueo = descripcionErrorLogueo;
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
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
