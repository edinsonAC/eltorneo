        
<link href="assets/css/estiloRegistrar-usuario.css" rel="stylesheet" type="text/css">

<div class="card tc-card bodyRegistrar" id="bodyRegistrar" >
    <div class="card-body"  >
        <h4 class="card-title" id="tituloForm"> <b>  Crear torneo </b></h4> 
        <br>
        <form class="forms-sample" id="reg_temporada"  onsubmit="return false;">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="nombre_temporada">* Nombre del torneo </label>
                        <input type="text" class="form-control nombre" id="nombre_temporada" name="nombre_temporada" autocomplete="off" maxlength="20"  required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="fechaIn_temporada">* Fecha inicio </label>
                        <input type="date" class="form-control" id="fechaIn_temporada" name="fechaIn_temporada" autocomplete="off" maxlength="20"  required>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="num_equipos">* Numeros equipo: </label>
                        <select class="form-control" id="num_equipos" name="num_equipos"></select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="col-sm-6 mx-auto" style="text-align: -webkit-center;">
                        <button type="submit" id="boton" class="btn btn-success mr-2" onclick="validar('reg_temporada', 1);">Registrar</button>     
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
                            this.value = this.value.replace(/[^a-zA-ZÒ—\s]*$/g, ''); // /^[a-zA-Z\s]*$/ solo letras
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
                                registrarTemporada()();
                            operacion = null;
                        }

                        $(document).ready(function () {
                            llenarSelect();
//        $("#email").attr("remote", URL + "SevletValidarCorreo?bandera=usuario");
//        $("#documentoA").attr("remote", URL + "SevletValidarDocumento?bandera=usuario");
//        $("#usuario").attr("remote", URL + "ServletValidarCrendencial?bandera=usuario");

                            $("#reg_temporada").validate({// el validate es sacado de codigo de internet, valida que los campos que tengan required este llenos
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


                        function registrarTemporada() {//aqui empieza la funcion del registro del tecnico
                            $("#boton").prop('disabled', true);
                            var temporada = {
                                nombre: $("#nombre_temporada").val(),
                                numEquipos: $("#num_equipos").val(),
                                fechaInicial: $("#fechaIn_temporada").val()

                            };

                            ajaxElTorneo.registrarTemporada(temporada, {
                                callback: function (data) {
                                    if (data !== null) {
                                        $("#boton").prop('disabled', false);
                                        cargarPagina('temporada.jsp');
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


                        function llenarSelect() {
                            for (var i = 4; i <= 12; i++) {
                                $("#num_equipos").append(new Option(i, i));
                            }
                        }

</script>