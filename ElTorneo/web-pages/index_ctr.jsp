<%-- 
    Document   : index_ctr
    Created on : 27/10/2016, 04:08:38 PM
    Author     : Administrator
--%>

<%@ page import="javax.servlet.*"%>
<%@ page import="java.util.*,java.io.*"%>

<%@ page import="co.eltorneo.mvc.dto.*"%>
<%@ page import="co.eltorneo.common.util.*"%>

<jsp:useBean type="co.eltorneo.mvc.fachada.FachadaSeguridad" scope="application" id="fachadaSeguridad"/>

<%
    UsuarioDTO datosUsuario = null;

    try {
        System.out.print("llegue al index control");
        if (!ValidaString.isNullOrEmptyString(request.getRemoteUser())) {
            System.out.print("pase el primer if");
            datosUsuario = fachadaSeguridad.consultarDatosUsuarioLogueado(request.getRemoteUser());
            System.out.print(datosUsuario.getEstado());

            if (datosUsuario != null && !datosUsuario.getEstado().equals("0")) {
                System.out.print("pase el segundo if");
                session.setAttribute("datosUsuario", datosUsuario);
                System.out.println("" + datosUsuario.getIdTipoUsuario());
//                response.sendRedirect(request.getContextPath() + "/index.jsp");

                if (datosUsuario.getIdTipoUsuario().equals("1")) {
                    //  session.setAttribute("paginaInicial", "index_superadmin.jsp");
                    System.out.println("estamos en index control " + datosUsuario.toStringJson());
                    request.getRequestDispatcher("/inicio.jsp").forward(request, response);

                } else {
                    request.getRequestDispatcher("/inicio.jsp").forward(request, response);
                }
            } else {
                if (datosUsuario.getDescripcionErrorLogueo().isEmpty()) {
                    request.getRequestDispatcher("/login.jsp?error=Ingreso no autorizado").forward(request, response);

                } else {
                    request.getRequestDispatcher("/login.jsp?error=" + datosUsuario.getDescripcionErrorLogueo()).forward(request, response);

                }

            }
        } else {
            request.getRequestDispatcher("/login.jsp").forward(request, response);

        }
    } catch (Exception e) {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
%>
