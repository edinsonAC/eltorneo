/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.eltorneo.hilo;

import co.eltorneo.common.util.Constantes;
import static co.eltorneo.common.util.Constantes.CORREO;
import co.eltorneo.common.util.LoggerMessage;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Administrador
 */
public class Correo extends Thread {

    private String tipo = StringUtils.EMPTY;
    private String asunto = StringUtils.EMPTY;
    private String cuerpo = StringUtils.EMPTY;
    private String nombre = StringUtils.EMPTY;
    private String correo = StringUtils.EMPTY;
    private String clave = StringUtils.EMPTY;
    private String observacion = StringUtils.EMPTY;
    private String direccionImagen = StringUtils.EMPTY;
    private String usuario = StringUtils.EMPTY;

    public Correo(String destino2, String clave2, String asunto2, String nombre2, String tipoCopy, String observacion, String usuario) {
        this.nombre = nombre2;
        this.correo = destino2;
        this.asunto = asunto2;
        this.tipo = tipoCopy;
        this.observacion = observacion;
        this.clave = clave2;
        this.usuario = usuario;
    }

    public boolean sendFromGMail(String[] to, String subject, String body) {

        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", CORREO);
        props.put("mail.smtp.password", Constantes.CLAVE_CORREO);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        boolean exito = false;
        try {
//            System.out.println("////" + from);
//            System.out.println("////" + pass);
//            System.out.println("////" + to);
//            System.out.println("////" + subject);
//            System.out.println("////" + body);
            message.setFrom(new InternetAddress(CORREO));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for (int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for (int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setContent(body, "text/html");
            Transport transport = session.getTransport("smtp");
            transport.connect(host, CORREO, Constantes.CLAVE_CORREO);
            transport.sendMessage(message, message.getAllRecipients());
            for (int i = 0; i < to.length; i++) {
                // System.out.print("Contraseña enviada a  " + to[i] + "\n");

            }

            transport.close();
            exito = true;
        } catch (AddressException ae) {
            ae.printStackTrace();
            return exito;
        } catch (MessagingException me) {
            me.printStackTrace();
            return exito;
        }
        return exito;

    }

    public void enviarCorreoBienvenida() {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<||||||||||EMPEZANDO EL ENVIO|||||||||>>>>>>>>>>>>>>>>>>>>>>>>>");

        String mensajeBienvenida = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                + "<head>\n"
                + "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n"
                + "    <title>A Simple Responsive HTML Email</title>\n"
                + "    <style type=\"text/css\">\n"
                + "        body {\n"
                + "            margin: 0;\n"
                + "            padding: 0;\n"
                + "            min-width: 100%!important;\n"
                + "        }\n"
                + "\n"
                + "        img {\n"
                + "            height: auto;\n"
                + "        }\n"
                + "\n"
                + "        .content {\n"
                + "            width: 100%;\n"
                + "            max-width: 600px;\n"
                + "        }\n"
                + "\n"
                + "        .header {\n"
                + "            padding: 40px 30px 20px 30px;\n"
                + "        }\n"
                + "\n"
                + "        .innerpadding {\n"
                + "            padding: 30px 30px 30px 30px;\n"
                + "        }\n"
                + "\n"
                + "        .borderbottom {\n"
                + "            border-bottom: 1px solid #f2eeed;\n"
                + "        }\n"
                + "\n"
                + "        .subhead {\n"
                + "            font-size: 15px;\n"
                + "            color: #ffffff;\n"
                + "            font-family: sans-serif;\n"
                + "            letter-spacing: 10px;\n"
                + "        }\n"
                + "\n"
                + "        .h1,\n"
                + "        .h2,\n"
                + "        .bodycopy {\n"
                + "            color: #153643;\n"
                + "            font-family: sans-serif;\n"
                + "        }\n"
                + "\n"
                + "        .h1 {\n"
                + "            font-size: 33px;\n"
                + "            line-height: 38px;\n"
                + "            font-weight: bold;\n"
                + "        }\n"
                + "\n"
                + "        .h2 {\n"
                + "            padding: 0 0 15px 0;\n"
                + "            font-size: 24px;\n"
                + "            line-height: 28px;\n"
                + "            font-weight: bold;\n"
                + "        }\n"
                + "\n"
                + "        .bodycopy {\n"
                + "            font-size: 16px;\n"
                + "            line-height: 22px;\n"
                + "        }\n"
                + "\n"
                + "        .button {\n"
                + "            text-align: center;\n"
                + "            font-size: 18px;\n"
                + "            font-family: sans-serif;\n"
                + "            font-weight: bold;\n"
                + "            padding: 0 30px 0 30px;\n"
                + "        }\n"
                + "\n"
                + "        .button a {\n"
                + "            color: #ffffff;\n"
                + "            text-decoration: none;\n"
                + "        }\n"
                + "\n"
                + "        .footer {\n"
                + "            padding: 20px 30px 15px 30px;\n"
                + "        }\n"
                + "\n"
                + "        .footercopy {\n"
                + "            font-family: sans-serif;\n"
                + "            font-size: 14px;\n"
                + "            color: #ffffff;\n"
                + "        }\n"
                + "\n"
                + "        .footercopy a {\n"
                + "            color: #ffffff;\n"
                + "            text-decoration: underline;\n"
                + "        }\n"
                + "\n"
                + "        @media only screen and (max-width: 550px),\n"
                + "        screen and (max-device-width: 550px) {\n"
                + "            body[yahoo] .hide {\n"
                + "                display: none!important;\n"
                + "            }\n"
                + "            body[yahoo] .buttonwrapper {\n"
                + "                background-color: transparent!important;\n"
                + "            }\n"
                + "            body[yahoo] .button {\n"
                + "                padding: 0px!important;\n"
                + "            }\n"
                + "            body[yahoo] .button a {\n"
                + "                background-color: #e05443;\n"
                + "                padding: 15px 15px 13px!important;\n"
                + "            }\n"
                + "            body[yahoo] .unsubscribe {\n"
                + "                display: block;\n"
                + "                margin-top: 20px;\n"
                + "                padding: 10px 50px;\n"
                + "                background: #2f3942;\n"
                + "                border-radius: 5px;\n"
                + "                text-decoration: none!important;\n"
                + "                font-weight: bold;\n"
                + "            }\n"
                + "        }      \n"
                + "    </style>\n"
                + "</head>\n"
                + "<body yahoo bgcolor=\"#f6f8f1\">\n"
                + "    <table width=\"100%\" bgcolor=\"#f6f8f1\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n"
                + "        <tr>\n"
                + "            <td>\n"
                + "                <table bgcolor=\"#ffffff\" class=\"content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
                + "                    <tr>\n"
                + "                        <td>\n"
                + "                            <img src=\"https://lh3.googleusercontent.com/cXWMyZwbeseqgOZXX7xmkfAl9Rmr4Oqq-GhvPbGiLyC3O6FMlz0qLmvyD0H6dilQrriFkSlttjAvHWhrur3XiLnUp0YAJWD-TC8=w2880-h1920-rw-no\" alt=\"Smiley face\" width=\"100%\" height=\"250px\">\n"
                + "\n"
                + "                        </td>\n"
                + "                    </tr>\n"
                + "                    <tr>\n"
                + "                        <td class=\"innerpadding borderbottom\">\n"
                + "                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                                <tr>\n"
                + "                                    <td class=\"h2\">\n"
                + "                                        Hola " + nombre + ",\n"
                + "                                    </td>\n"
                + "                                </tr>\n"
                + "                                <tr>\n"
                + "                                    <td class=\"bodycopy\">\n"
                + "                                            ElTorneo te da la bienvenida, Puedes ingresar con tu usuario y la siguiente clave de acceso: <br> Usuario: " + usuario + " <br> Clave: " + clave + " \n"
                + "                                    </td>\n"
                + "                                </tr>\n"
                + "                            </table>\n"
                + "                        </td>\n"
                + "                    </tr>\n"
                + "                    <tr>\n"
                + "                        <td class=\"footer\" bgcolor=\"#44525f\">\n"
                + "                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                                <tr>\n"
                + "                                    <td align=\"center\" class=\"footercopy\">\n"
                + "                                        &reg; ElTorneo, 2018\n"
                + "                                        <br/>\n"
                + "                                        <a href=\"https://www.google.com\" class=\"unsubscribe\">\n"
                + "                                            <font color=\"#ffffff\">eltorneo.com</font>\n"
                + "                                        </a>\n"
                + "                                    </td>\n"
                + "                                </tr>\n"
                + "                            </table>\n"
                + "                        </td>\n"
                + "                    </tr>\n"
                + "                </table>               \n"
                + "            </td>\n"
                + "        </tr>\n"
                + "    </table>  \n"
                + "    <script src=\"http://code.jquery.com/jquery-1.10.1.min.js\"></script>\n"
                + "    <script src=\"http://tutsplus.github.io/github-analytics/ga-tracking.min.js\"></script>\n"
                + "</body>\n"
                + "</html>";

        String[] to = {correo};
        String asunto = "SOLICITUD: REGISTRO ElTorneo";

        boolean envio = sendFromGMail(to, asunto, mensajeBienvenida);

        if (envio) {
            System.out.println("Se logro el envio.");
        } else {
            System.out.println("No se pudo enviar el correo.");
        }

    }
    
       @Override
    public void run() {
        switch (this.tipo) {
            case "1":
                this.enviarCorreoBienvenida();
                break;
            case "2":
                System.out.println("case 2");
                break;
        }

    }
}
