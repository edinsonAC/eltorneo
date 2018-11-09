<%@page import="co.eltorneo.mvc.dto.MenuDTO"%>
<!DOCTYPE html>

<%@page import="java.util.ArrayList"%>
<%@page import="co.eltorneo.mvc.dto.UsuarioDTO"%>
<%
    System.out.println("estamos en inicio.jsp----<");
    UsuarioDTO datosUsuario = (UsuarioDTO) session.getAttribute("datosUsuario");
    ArrayList<MenuDTO> menu = null;
    System.out.println("estamos en inicio.jsp----<" + datosUsuario.toStringJson());
    if (datosUsuario.getMenu() != null) {
        menu = datosUsuario.getMenu();
        for (MenuDTO elem : menu) {
            System.out.println("este es el menu cargado+..." + elem.toStringJson());
        }

    }

%>

<html lang="en">

    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Registro - El Torneo</title>

        <link href="assets/css/integral-core.css" rel="stylesheet">
        <!-- Global stylesheets -->
        <link href="assets/css/bootstrap.css" rel="stylesheet" type="text/css">
        <link href="assets/css/core.css" rel="stylesheet" type="text/css">
        <link href="assets/css/components.css" rel="stylesheet" type="text/css">
        <link href="assets/css/colors.css" rel="stylesheet" type="text/css">
        <link href="assets/css/estilos.css" rel="stylesheet" type="text/css">
        <link href="assets/css/estiloInicio.css" rel="stylesheet" type="text/css">

        <!-- Core JS files -->
        <script type="text/javascript" src="assets/js/plugins/loaders/pace.min.js"></script>
        <script type="text/javascript" src="assets/js/core/libraries/jquery.min.js"></script>
        <script type="text/javascript" src="assets/js/core/libraries/bootstrap.min.js"></script>
        <script type="text/javascript" src="assets/js/core/app.js"></script>
        <script type="text/javascript" src="assets/js/plugins/loaders/blockui.min.js"></script>

        <!-- /core JS files -->

        <!-- Modal SCRIPT -->
        <script type="text/javascript" src="assets/js/pages/components_modals.js"></script>
        <!-- Modal SCRIPT -->

        <!-- Theme JS files -->
        <script type="text/javascript" src="assets/js/plugins/forms/selects/bootstrap_multiselect.js"></script>
        <script type='text/javascript' src='/ElTorneo/dwr/interface/ajaxElTorneo.js'></script>
        <script type='text/javascript' src='/ElTorneo/dwr/interface/ajaxSeguridad.js'></script>
        <script type='text/javascript' src='/ElTorneo/dwr/engine.js'></script>
        <script type='text/javascript' src='/ElTorneo/dwr/util.js'></script>


        <!-- Theme JS files -->
        <script type="text/javascript" src="assets/js/core/libraries/jquery_ui/interactions.min.js"></script>
        <script type="text/javascript" src="assets/js/core/libraries/jquery_ui/touch.min.js"></script>

        <script type="text/javascript" src="assets/js/pages/components_navs.js"></script>
        <!--        Theme JS files -->
        <script type="text/javascript" src="assets/js/plugins/tables/datatables/datatables.min.js"></script>
        <script type="text/javascript" src="assets/js/plugins/forms/selects/selectboxit.min.js"></script>
        <script type="text/javascript" src="assets/js/plugins/forms/styling/switch.min.js"></script>
        <!--        Theme JS files-->
        <script type="text/javascript" src="assets/js/plugins/notifications/pnotify.min.js"></script>
        <script src="assets/libs/bootstrap-validator/js/jquery.validate.js"></script>
        <script src="assets/libs/bootstrap-validator/js/bootstrapValidator.min.js"></script>

        <script type="text/javascript" src="assets/js/plugins/forms/wizards/form_wizard/form_wizard.min.js"></script>
        <script type="text/javascript" src="assets/js/plugins/forms/wizards/steps.min.js"></script>

        <script type="text/javascript" src="assets/js/plugins/loaders/pace.min.js"></script>
        <script type="text/javascript" src="assets/js/plugins/forms/validation/validate.min.js"></script>

    </head>

    <body>
        <div class="container" id="hola">
            <header>
                
                <nav class="navbar navbar-default">
                    <div class="container"  >
                        <div class="navbar-header">
                            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#nav1">
                                <span class="sr-only">menu</span>
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                            </button>
                            <h4>El Torneo</h4>
                        </div>
                        <div class="collapse navbar-collapse" id="nav1" >
                            <ul class="nav navbar-nav">
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
            

            function cargarPagina(pagina) {
                console.log("entro a la funcion", pagina);
                $("#contenidoPrincipal").load('' + pagina);
//  $("#loader").show();
            }

            $(document).ready(function () {
                idTipoUsuarioLogueado = '<%=datosUsuario.getIdTipoUsuario()%>';
                nombreUsuario = '<%=datosUsuario.getNombre()%>';
                usuario = '<%=datosUsuario.getUsuario()%>';
                
                if (idTipoUsuarioLogueado == 1) {
                    $(".loader-backdrop").show();
                    cargarPagina('registrar-usuario.jsp');
                }
            });

        </script>
    </body>
</html>
