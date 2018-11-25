/*
 * FachadaSeguridad.java
 *
 * Proyecto: elTorneo
 * All rights reserved
 */
package co.eltorneo.mvc.fachada;

import co.eltorneo.mvc.dto.UsuarioDTO;
import co.eltorneo.mvc.mediador.MediadorSeguridad;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.annotations.ScriptScope;

/**
 *
 * @author edinsonAC
 */
@RemoteProxy(name = "ajaxSeguridad", scope = ScriptScope.SESSION)
public class FachadaSeguridad {

    /**
     *
     */
    public FachadaSeguridad() {
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
     * @param usuario
     * @return
     */
    @RemoteMethod
    public UsuarioDTO consultarDatosUsuarioLogueado(String usuario) {
        return MediadorSeguridad.getInstancia().consultarDatosUsuarioLogueado(usuario);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     * LOS METODOS APARTIR DE AQUI NO HAN SIDO VALIDADOS
     * -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
}
