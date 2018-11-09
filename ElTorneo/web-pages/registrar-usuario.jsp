        
<link href="assets/css/estiloRegistrar-usuario.css" rel="stylesheet" type="text/css">

<div class="card tc-card" id="bodyRegistrar" >
    <div class="card-body"  >
        <h4 class="card-title" id="tituloForm"> <b>  Registrar Tecnico </b></h4> 
        <br>
        <form class="forms-sample" id="reg_usuario"  onsubmit="return false;">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="ap1_usuario">* Nombres: </label>
                        <input type="text" class="form-control nombre" id="nom1_usuario" name="ap1_usuario" autocomplete="off" maxlength="45"  required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="ap2_usuario">* Apellidos: </label>
                        <input type="text" class="form-control nombre" id="ap1_usuario" name="ap2_usuario" autocomplete="off" maxlength="45" >
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="documento">* Número documento: </label>
                        <input type="text" maxlength="10"  class="form-control numeros"  id="documentoB" name="documento" autocomplete="off" >
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="form-group">
                        <label for="email">* Correo: </label>
                        <input type="email" class="form-control" id="email" maxlength="45" name="email" autocomplete="off" required>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="cel_usuario">* Celular: </label>
                        <input type="text" class="form-control numeros" id="cel_usuario" name="cel_usuario" maxlength="10" autocomplete="off" required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="dir_usuario">* Dirección: </label>
                        <input type="text" class="form-control" id="dir_usuario" name="dir_usuario" maxlength="45" autocomplete="off" required>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="tel_usuario">* Telefono: </label>
                        <input type="text" class="form-control numeros" id="tel_usuario" name="tel_usuario" maxlength="10" autocomplete="off">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="usuario">* Usuario: </label>
                        <input type="text" class="form-control" id="usuario" name="usuario" maxlength="15" autocomplete="off">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="col-sm-6 mx-auto" style="text-align: -webkit-center;">
                        <button type="submit" id="boton" class="btn btn-success mr-2" onclick="validar('reg_usuario', 1);">Registrar</button>   
                        <button type="button" id="boton2" class="btn btn-success mr-2" onclick="sorteo();">sorteo</button>   
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
                                registro();
                            operacion = null;
                        }

                        $(document).ready(function () {//el document ready es por donde empieza a cargar, lo que ud ponga de primero aca, eso es lo que se hace de primero
//        $("#email").attr("remote", URL + "SevletValidarCorreo?bandera=usuario");
//        $("#documentoA").attr("remote", URL + "SevletValidarDocumento?bandera=usuario");
//        $("#usuario").attr("remote", URL + "ServletValidarCrendencial?bandera=usuario");

                            $("#reg_usuario").validate({ // el validate es sacado de codigo de internet, valida que los campos que tengan required este llenos
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


                        function registro() {//aqui empieza la funcion del registro del tecnico
                            $("#boton").prop('disabled', true);
                            //en la fachada declare que el metodo registrar tecnico recibia dos  objetos
                            
                            //esta es la forma de armar un objeto, el nombre no importa, lo que importa son los atributos que deben ser igual a como estan en el dto
                            var usuario = {
                                correo: $("#email").val(),  //la forma de obtener el valor de un campo con jquery es $("#elIdDelInput").val()
                                idTipoUsuario: TECNICO,     // tecnico es una contante que cree en un paquete del proyecto
                                usuario: $("#usuario").val(),
                                registradoPor: nombreUsuario  // es una variable global que cree en los jsp, guarda el nombre del que este logueado

                            };

                            var tecnico = {
                                nombre: $("#nom1_usuario").val(),
                                apellido: $("#ap1_usuario").val(),
                                documento: $("#documentoB").val(),
                                direccion: $("#dir_usuario").val(),
                                celular: $("#cel_usuario").val(),
                                registradoPor: nombreUsuario

                            };
                            
                            //esta es la forma de llamar un metodo de la fachada, ajaxElTorneo. y el nombre de la funcion
                            //aqui si importa el orden de los objetos, por que como este en la fachada se deben poner, en este caso primero el tecnico
                            ajaxElTorneo.registrarTecnico(tecnico, usuario, {
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
                        
                        //esta siempre es la estructura
                       // ajaxElTorneo.nombredelafuncion(parametros, parametros, {
                         //       callback: function (data) { recibe lo que devuelve la funcion
                           //         if (data !== null) { si lo que se devuelve es diferente de null ud ahi hace algo, un mensaje o lo que sea
                             ///      
                               // },
                     //           timeout: 20000  es el tiempo maximo para que devuelva algo la funcion
                       //     });
                        
                        
                        

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