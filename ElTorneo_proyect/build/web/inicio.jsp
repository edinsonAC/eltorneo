<%@page import="co.eltorneo.mvc.dto.MenuDTO"%>
<!DOCTYPE html>

<%@page import="java.util.ArrayList"%>
<%@page import="co.eltorneo.mvc.dto.UsuarioDTO"%>
<%
    UsuarioDTO datosUsuario = (UsuarioDTO) session.getAttribute("datosUsuario");
    ArrayList<MenuDTO> menu = null;
    // System.out.println("estamos en inicio.jsp----<" + datosUsuario.toStringJson());
    if (datosUsuario.getMenu() != null) {
        menu = datosUsuario.getMenu();
        for (MenuDTO elem : menu) {
            //  System.out.println("este es el menu cargado+..." + elem.toStringJson());
        }

    }

%>

<html lang="en">

    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>El Torneo</title>

        <!-- Global stylesheets -->
        <link href="assets/css/bootstrap.css" rel="stylesheet" type="text/css">
        <link href="assets/css/components.css" rel="stylesheet" type="text/css">
        <link href="assets/css/colors.css" rel="stylesheet" type="text/css">
        <link href="assets/css/estilos.css" rel="stylesheet" type="text/css">
        <link href="assets/css/dataTable.css" rel="stylesheet" type="text/css">
        <link href="assets/css/estiloInicio.css" rel="stylesheet" type="text/css">
        <link href="assets/css/estiloRegistrar-usuario.css" rel="stylesheet" type="text/css">
        <link href="assets/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <link rel="shortcut icon" href="assets/images/balon_estadio.jpg" />
        <!-- Core JS files -->
        <script type="text/javascript" src="assets/js/core/libraries/jquery.min.js"></script>
        <script type="text/javascript" src="assets/js/core/libraries/bootstrap.min.js"></script>
        <script type="text/javascript" src="assets/js/plugins/loaders/blockui.min.js"></script>
        <script type="text/javascript" src="assets/js/plugins/loaders/pace.min.js"></script>
        <!-- /core JS files -->

        <!-- Theme JS files -->
        <script type='text/javascript' src='/ElTorneo/dwr/interface/ajaxElTorneo.js'></script>
        <script type='text/javascript' src='/ElTorneo/dwr/interface/ajaxSeguridad.js'></script>
        <script type='text/javascript' src='/ElTorneo/dwr/engine.js'></script>
        <script type='text/javascript' src='/ElTorneo/dwr/util.js'></script>


        <!-- Theme JS files -->
        <script type="text/javascript" src="assets/js/core/libraries/interactions.min.js"></script>
        <!--        Theme JS files -->
        <!--        <script type="text/javascript" src="assets/js/plugins/tables/datatables/datatables.min.js"></script>-->
        <script type="text/javascript" src="assets/js/plugins/tables/datatables/datatables2.js"></script>
        <!--        Theme JS files-->
        <script src="assets/libs/bootstrap-validator/js/jquery.validate.js"></script>
        <script src="assets/libs/bootstrap-validator/js/bootstrapValidator.min.js"></script>
        <script type="text/javascript" src="assets/js/plugins/forms/validation/validate.min.js"></script>
        <script src="assets/js/jquery-steps-master/jquery-steps-master/build/jquery.steps.min.js"></script>
        <script src="assets/js/Constantes.js"></script>

    </head>

    <body>
        <form action="${pageContext.request.contextPath}/CerrarSesion" method="post">
            <input style="display: none" type="submit" value="Logout" id="Logout">
        </form>
        <div class="container" id="hola">
            <header>

                <nav class="navbar navbar-default default-nav">
                    <div class="container nav-transparent"  >
                        <div class="navbar-header">
                            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#nav1">
                                <span class="sr-only">menu</span>
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                            </button>                           
                        </div>
                        <div class="collapse navbar-collapse transparent" id="nav1" >
                            <ul class="nav navbar-nav nav-content">
                                <li class="dropdown"><img src="assets/images/balonn.png" width="50px" height="50px"></li>
                                    <% for (MenuDTO elem : menu) {%>                             
                                <li class="dropdown">  
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><%=elem.getTituloMenu()%></a>
                                    <ul class="dropdown-menu">
                                        <%for (int j = 0; j < elem.getFuncionalidad().size(); j++) {%>
                                        <li>
                                            <a href="javascript:cargarPagina('<%=elem.getFuncionalidad().get(j).getPagina()%>');"><%=elem.getFuncionalidad().get(j).getTitulo()%></a>
                                        </li>
                                        <% }%>
                                    </ul>
                                </li>
                                <%}%>
                                <button type="button" class="btn btn-primary btn-sesion" onclick="$('#Logout').click()">cerrar sesion</button>
                            </ul>

                        </div>
                    </div>
                </nav>
            </header>



        </div><br>


        <div id="contenidoPrincipal" class="page-container"  >
        </div>
        <script>
            var idTipoUsuarioLogueado;
            var nombreUsuario;
            var idUsuario;
            var usuario;
            var idEquipo;
            var idArbitroLogueado;
            var idTecnicoLogueado;

            function cargarPagina(pagina) {
                console.log("entro a la funcion", pagina);
                $("#contenidoPrincipal").load('' + pagina);
//  $("#loader").show();
            }

            $(document).ready(function () {
                idTipoUsuarioLogueado = '<%=datosUsuario.getIdTipoUsuario()%>';
                nombreUsuario = '<%=datosUsuario.getNombre()%>';
                usuario = '<%=datosUsuario.getUsuario()%>';
                idEquipo = '<%=datosUsuario.getIdEquipo()%>';
                idArbitroLogueado = '<%=datosUsuario.getArbitro()%>';
                idTecnicoLogueado = '<%=datosUsuario.getIdTecnico()%>';

                jQuery.extend(jQuery.validator.messages, {
                    required: "Este campo es obligatorio.",
                    remote: "Este valor ya esta registrado.",
                    email: "Por favor, escribe una dirección de correo válida",
                    url: "Por favor, escribe una URL válida.",
                    date: "Por favor, escribe una fecha válida.",
                    dateISO: "Por favor, escribe una fecha (ISO) válida.",
                    number: "Por favor, escribe un número entero válido.",
                    digits: "Por favor, escribe sólo dígitos.",
                    creditcard: "Por favor, escribe un número de tarjeta válido.",
                    equalTo: "Por favor, escribe el mismo valor de nuevo.",
                    accept: "Por favor, escribe un valor con una extensión aceptada.",
                    maxlength: jQuery.validator.format("Por favor, no escribas más de {0} caracteres."),
                    minlength: jQuery.validator.format("Por favor, no escribas menos de {0} caracteres."),
                    rangelength: jQuery.validator.format("Por favor, escribe un valor entre {0} y {1} caracteres."),
                    range: jQuery.validator.format("Por favor, escribe un valor entre {0} y {1}."),
                    max: jQuery.validator.format("Por favor, escribe un valor menor o igual a {0}."),
                    min: jQuery.validator.format("Por favor, escribe un valor mayor o igual a {0}.")
                });

                if (idTipoUsuarioLogueado == ADMINISTRADOR) {
                    $(".loader-backdrop").show();
                    cargarPagina('gestion-equipo.jsp');
                }
                if (idTipoUsuarioLogueado == TECNICO) {
                    $(".loader-backdrop").show();
                    cargarPagina('mis-equipos.jsp');
                }
            });

        </script>
    </body>
</html>
