<!DOCTYPE html>
<html lang="en">

    <meta http-equiv="content-type" content="text/html;charset=UTF-8" />
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>el torneo</title>

        <!-- Global stylesheets -->
        <link href="https://fonts.googleapis.com/css?family=Roboto:400,300,100,500,700,900" rel="stylesheet" type="text/css">
        <link href="assets/css/icons/icomoon/styles.css" rel="stylesheet" type="text/css">
        <link href="assets/css/bootstrap.css" rel="stylesheet" type="text/css">
        <link href="assets/css/components.css" rel="stylesheet" type="text/css">
        <link href="assets/css/colors.css" rel="stylesheet" type="text/css">
        <!-- /global stylesheets -->

        <!-- Core JS files -->
        <script type="text/javascript" src="assets/js/plugins/loaders/pace.min.js"></script>
        <script type="text/javascript" src="assets/js/core/libraries/jquery.min.js"></script>
        <script type="text/javascript" src="assets/js/core/libraries/bootstrap.min.js"></script>
        <script type="text/javascript" src="assets/js/plugins/loaders/blockui.min.js"></script>
        <!-- /core JS files -->
        <script src="assets/libs/bootstrap-validator/js/jquery.validate.js"></script>
        <script src="assets/libs/bootstrap-validator/js/bootstrapValidator.min.js"></script>

    </head>

    <body class="login-container">

        <!-- Main navbar -->
        <div class="navbar navbar-inverse">
            <div class="navbar-header">
                <!--
                <a class="navbar-brand" href="index.html"><img src="assets/images/logo_light.png" alt=""></a>
                -->
                <ul class="nav navbar-nav pull-right visible-xs-block">
                    <li><a data-toggle="collapse" data-target="#navbar-mobile"><i class="icon-tree5"></i></a></li>
                </ul>
            </div>


            <div class="navbar-collapse collapse" id="navbar-mobile">
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <a href="login.jsp">
                            <i class="icon-user-tie"></i> <span class="visible-xs-inline-block position-right">Ir a Login</span>
                        </a>
                    </li>

                </ul>
            </div>

        </div>
        <!-- /main navbar -->


        <!-- Page container -->
        <div class="page-container">

            <!-- Page content -->
            <div class="page-content">

                <!-- Main content -->
                <div class="content-wrapper">

                    <!-- Content area -->
                    <div class="content">

                        <!-- Password recovery -->
                        <form id="recoverform" action="return:false">
                            <div class="panel panel-body login-form">

                                <div class="alert alert-danger alert-dismissable" id="alertError" style="display: none;">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>  
                                    <p id="msgError"> </p>
                                </div>

                                <div class="alert alert-success" id="alertCorrecto" style="display: none;">   
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                                    <h4>Recuperacion con Exito!</h4>
                                    <p>La contraseña fue enviada al correo <strong id="correo"></strong></p> <br>
                                    <div class="col-sm-8"><p>Ir al login: </p></div>
                                    <a href="login.jsp" class="btn  btn-success">Aceptar</a>                         
                                </div>

                                <div class="text-center">
                                    <!--<div class="icon-object border-slate-300 text-slate-300"><i class="icon-reading"></i></div>-->
                                    <div><img src="assets/images/logo_login.jpg" alt=""></div>
                                    <h5 class="content-group">Recuperación de contraseña<small class="display-block">Se enviará una contraseña nueva al correo</small></h5>
                                </div>
<!--                                <div class="text-center">
                                    <div class="icon-object border-warning text-warning"><i class="icon-spinner11"></i></div>
                                    <h5 class="content-group">Recuperación de contraseña<small class="display-block">Se enviará una contraseña nueva al correo</small></h5>
                                </div>-->

                                <div class="form-group has-feedback">
                                    <input type="email" class="form-control" required id="email" placeholder="Su correo">

                                    <div class="form-control-feedback">
                                        <i class="icon-mail5 text-muted"></i>
                                    </div>
                                </div>

                                <button type="submit" onclick="validar();" class="btn bg-blue btn-block">Recuperar contraseña<i class="icon-arrow-right14 position-right"></i></button>
                            </div>
                        </form>
                        <!-- /password recovery -->


                        <!-- Footer -->

                        <!-- /footer -->

                    </div>
                    <!-- /content area -->

                </div>
                <!-- /main content -->

            </div>
            <!-- /page content -->

        </div>
        <!-- /page container -->

    </body>


    <script>

        $(document).ready(function() {
            $("#alertCorrecto").hide();
            $("#alertError").hide();
        });


        function validar() {
            $("#alertCorrecto").hide();
            $("#alertError").hide();

            $('#recoverform').validate({
                highlight: function(label) {
                    jQuery(label).closest('.form-group').removeClass('has-success').addClass('has-error');
                },
                success: function(label) {
                    jQuery(label).closest('.form-group').removeClass('has-error');
                    label.remove();
                },
                errorPlacement: function(error, element) {
                    var placement = element.closest('.input-group');
                    if (!placement.get(0)) {
                        placement = element;
                    }
                    if (error.text() !== '') {
                        placement.after(error);
                    }
                },
                submitHandler: function() {
                    llamarServlet();
                }
            });
        }


        function llamarServlet() {

            $.ajax({
                url: 'ServletRecuperarClave',
                data: {
                    documento: $("#email").val()
                },
                success: function(data) {
                    if (data !== "incorrecto" && data !== "") {
                        $("#alertCorrecto").show();
                        $('#correo').text(data);
                    }
                    if (data === "incorrecto") {
                        $("#alertError").show();
                        $('#msgError').text("Ocurrio un problema en la recuperación de la contreseña");
                    }
                    if (data === "") {
                        $("#alertError").show();
                        $('#msgError').text("No existe un usuario con correo: " + $("#email").val());
                    }
                }
            });
        }


    </script>
</html>
