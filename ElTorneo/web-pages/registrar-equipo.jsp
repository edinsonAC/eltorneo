        
<link href="assets/css/estiloRegistrar-usuario.css" rel="stylesheet" type="text/css">

<div class="card tc-card bodyRegistrar" id="bodyRegistrarEquipo" >
    <div class="card-body"  >
        <h4 class="card-title" id="tituloForm"> <b>  Registrar equipo </b></h4> 
        <br>
        <form class="forms-sample" id="reg_equipo"  onsubmit="return false;">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="nombreEquipo">* Nombre del equipo: </label>
                        <input type="text" class="form-control nombre" id="nombreEquipo" name="nombreEquipo" autocomplete="off" maxlength="20"  required>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="col-sm-6 mx-auto" style="text-align: -webkit-center;">
                        <button type="submit" id="boton" class="btn btn-success mr-2" onclick="validar('reg_equipo', 1);">Registrar</button>   
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
                        function redireccionar() {
                            jQuery("#contenidoPrincipal").load("mis-equipos.jsp");
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
                                registrarEquipo();
                            operacion = null;
                        }

                        $(document).ready(function () {//el document ready es por donde empieza a cargar, lo que ud ponga de primero aca, eso es lo que se hace de primero
//        $("#email").attr("remote", URL + "SevletValidarCorreo?bandera=usuario");
//        $("#documentoA").attr("remote", URL + "SevletValidarDocumento?bandera=usuario");
//        $("#usuario").attr("remote", URL + "ServletValidarCrendencial?bandera=usuario");

                            $("#reg_equipo").validate({// el validate es sacado de codigo de internet, valida que los campos que tengan required este llenos
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


                        function registrarEquipo() {//aqui empieza la funcion del registro del tecnico
                            $("#boton").prop('disabled', true);
                            //en la fachada declare que el metodo registrar tecnico recibia dos  objetos

                            //esta es la forma de armar un objeto, el nombre no importa, lo que importa son los atributos que deben ser igual a como estan en el dto
                            var equipo = {
                                nombre: $("#nombreEquipo").val(),
                                idTecnico: idTecnicoLogueado,
                                registradoPor: nombreUsuario  // es una variable global que cree en los jsp, guarda el nombre del que este logueado

                            };

                            ajaxElTorneo.registrarEquipo(equipo, {
                                callback: function (data) {
                                    if (data !== null) {
                                        $("#boton").prop('disabled', false);
                                        cargarPagina('mis-equipos.jsp');
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


                        function sorteo() { //aqui estaba probando el sorteo de partidos mediante un boton, solo era para probar
                            var fecha = "2018-10-22";
                            ajaxElTorneo.sorteoDePartidos(fecha, {
                                callback: function (data) {
                                    if (data !== null) {

                                    } else {

                                    }
                                },
                                timeout: 20000
                            });

                            // recargar();
                        }

</script>