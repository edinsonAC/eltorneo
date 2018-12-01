        
<link href="assets/css/estiloRegistrar-usuario.css" rel="stylesheet" type="text/css">

<div class="card tc-card" id="bodyRegistrar">
    <div class="card-body"  >
        <h4 class="card-title" id="tituloForm"> <b>  Registrar Jugadores </b></h4> 
        <br>
        <form class="forms-sample" id="reg_jugador"  onsubmit="return false;">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="nom_jugador">* Nombres: </label>
                        <input type="text" class="form-control nombre" id="nom_jugador" name="nom_jugador" autocomplete="off" maxlength="45"  required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="ap_jugador">* Apellidos: </label>
                        <input type="text" class="form-control nombre" id="ap_jugador" name="ap_jugador" autocomplete="off" maxlength="45" >
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="doc_jugador">* Número documento: </label>
                        <input type="text" maxlength="10"  class="form-control numeros"  id="doc_jugador" name="doc_jugador" autocomplete="off" >
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="form-group">
                        <label for="email_jugador">* Correo: </label>
                        <input type="email" class="form-control" id="email_jugador" maxlength="45" name="email_jugador" autocomplete="off" required>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="cel_jugador">* Celular: </label>
                        <input type="text" class="form-control numeros" id="cel_jugador" name="cel_jugador" maxlength="10" autocomplete="off" required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="dir_jugador">* Dirección: </label>
                        <input type="text" class="form-control" id="dir_jugador" name="dir_jugador" maxlength="45" autocomplete="off" required>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="tel_jugador">* Telefono: </label>
                        <input type="text" class="form-control numeros" id="tel_jugador" name="tel_jugador" maxlength="10" autocomplete="off">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="usuario_jugador">* Usuario: </label>
                        <input type="text" class="form-control" id="usuario_jugador" name="usuario_jugador" maxlength="15" autocomplete="off">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="tel_jugador">*posicion de juego </label>
                        <select id="pos_jugador" class="form-control"></select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="col-sm-6 mx-auto" style="text-align: -webkit-center;">
                        <button type="submit" id="boton" class="btn btn-success mr-2" onclick="validar('reg_jugador', 1);">Registrar</button>     
                    </div>
                    <input class="btn btn-light" type="button" value="Volver" onclick="javascript:redireccionar();">
                </div>
            </div>
        </form>
        <br>
    </div>
</div>
<script type="text/javascript" src="assets/js/Constantes.js"></script>
<script>
                        function recargar() {
                            jQuery("#contenidoPpal").load("admin/registrar-usuario.jsp");
                        }
                        // esto que esta debajo son clases que hacen que en los input no se puedan usar algunos caracteres 
                        $(".numeros").on("input", function () {
                            this.value = this.value.replace(/[^0-9]/g, '');//si pone esta clase en el input,solo permite numeros
                        });
                        $(".nombre").on("input", function () {
                            this.value = this.value.replace(/[^a-zA-ZñÑ\s]*$/g, ''); // /^[a-zA-Z\s]*$/ solo letras
                        });
                        $(".alfanumerico").on("input", function () {
                            this.value = this.value.replace(/[^A-Za-z0-9\s]*$/g, '');// numeros y letras
                        });
                        $("#usuario").on("input", function () {
                            this.value = this.value.replace(/[^A-Za-z0-9\s]*$/g, '').replace(' ', '');//no permite espacios
                        });
                        ////////

                        //globales
                        var operacion = null;

                        function validar(form, r) {
                            operacion = r;
                        }

                        function postValidate() {
                            if (operacion == 1)
                                registrarJugador();
                            operacion = null;
                        }

                        $(document).ready(function () {//el document ready es por donde empieza a cargar, lo que ud ponga de primero aca, eso es lo que se hace de primero
//        $("#email").attr("remote", URL + "SevletValidarCorreo?bandera=usuario");
//        $("#documentoA").attr("remote", URL + "SevletValidarDocumento?bandera=usuario");
//        $("#usuario").attr("remote", URL + "ServletValidarCrendencial?bandera=usuario");
                            listarPosiciones();
                            $("#reg_jugador").validate({// el validate es sacado de codigo de internet, valida que los campos que tengan required este llenos
                                errorPlacement: function (label, element) {
                                    label.addClass('mt-2 text-danger');
                                    $(element).parent().append(label);
                                },
                                highlight: function (element, errorClass) {
                                    $(element).parent().addClass('has-danger');
                                    $(element).addClass('form-control-danger');
                                    // operacion = null;
                                },
                                success: function (label) {
                                    jQuery(label).closest('.has-danger').removeClass('has-danger');
                                    label.remove();
                                },
                                submitHandler: function () {
                                    postValidate();  // cuando llega aca es cuando se lleno bien el formulario, y va a la funcin que cree, que se llama postValidate
                                }
                            });

                            $(".loader-backdrop").fadeOut();

                        });


                        function registrarJugador() {
                            $("#boton").prop('disabled', true);
                            var usuario = {
                                correo: $("#email_jugador").val(),
                                idTipoUsuario: JUGADOR,
                                usuario: $("#usuario_jugador").val(),
                                registradoPor: nombreUsuario

                            };

                            var jugador = {
                                nombre: $("#nom_jugador").val(),
                                apellido: $("#ap_jugador").val(),
                                documento: $("#doc_jugador").val(),
                                direccion: $("#dir_jugador").val(),
                                celular: $("#cel_jugador").val(),
                                telefono: $("#tel_jugador").val(),
                                idPosicion: $("#pos_jugador").val(),
                                dorsal: $("#dorsal_jugador").val(),
                                idEquipo: idUsuario,
                                registradoPor: nombreUsuario

                            };

                            ajaxElTorneo.registrarJugador(jugador, usuario, {
                                callback: function (data) {
                                    if (data !== null) {
                                        $("#boton").prop('disabled', false);
                                        limpiar();
                                    } else {
                                        $("#boton").prop('disabled', false);
                                    }
                                },
                                timeout: 20000
                            });

                            // recargar();
                        }


                        function limpiar() {//limpia solo los input, excepto los i que sean tipo button 
                            $('input').not(":button").val('');
                        }

                        function listarPosiciones() {
                            ajaxElTorneo.listarPosicionesDeJuego({
                                callback: function (data) {
                                    if (data !== null) {
                                        dwr.util.removeAllOptions("pos_jugador");
                                        dwr.util.addOptions("pos_jugador", [{
                                                id: '',
                                                nombre: 'Seleccione posicion',
                                            }], 'id', 'nombre');
                                        dwr.util.addOptions("pos_jugador", data, 'id', 'nombre');
                                    }
                                },
                                timeout: 20000
                            });
                        }

</script>