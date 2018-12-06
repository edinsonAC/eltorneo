/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.eltorneo.servlet;

import co.eltorneo.mvc.dao.JugadorDAO;
import co.eltorneo.mvc.mediador.MediadorElTorneo;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrador
 */
@WebServlet(name = "ServletValidarDorsal", urlPatterns = {"/ServletValidarDorsal"})
public class ServletValidarDorsal extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ServletValidarDorsal</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ServletValidarDorsal at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession servletSesion;
         System.out.println("Servlet validar ::: Entra al servlet de validar documento");
        servletSesion = request.getSession(false);
        response.setContentType("text/plain");

        try {
            PrintWriter out = response.getWriter();
            boolean existe;
            System.out.println(" Datos a validar: " + request.getParameter("dorsal"));
            existe = MediadorElTorneo.getInstancia().validarDorsalEquipo((String) request.getParameter("dorsal"), request.getParameter("equipo"));
//            existe = new JugadorDAO().validarDorsalJugador(conexion, dorsal, equipo);
            System.out.println(" Existe : " + existe);
            out.println(!existe);
            out.flush();

        } catch (Exception ex) {
            Logger.getLogger(ex.getMessage());
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession servletSesion;
        System.out.println("Servlet validar ::: Entra al servlet de validar documento");
        servletSesion = request.getSession(false);
        response.setContentType("text/plain");

        try {
            PrintWriter out = response.getWriter();
            boolean existe;
            System.out.println(" Datos a validar: " + request.getParameter("documento"));
            existe = MediadorElTorneo.getInstancia().validarDorsalEquipo((String) request.getParameter("dorsal"), request.getParameter("equipo"));
//            existe = new JugadorDAO().validarDorsalJugador(conexion, dorsal, equipo);
            System.out.println(" Existe : " + existe);
            out.println(!existe);
            out.flush();

        } catch (Exception ex) {
            Logger.getLogger(ex.getMessage());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
