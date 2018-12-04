<div class="card tc-card" id="bodyRegistrar">
    <div class="card-body"  >
        <h4 class="card-title" id="tituloForm"> <b>  Registrar Arbitro </b></h4> 
        <br>
        <form class="forms-sample" id="reg_arbitro"  onsubmit="return false;">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="nom_arbitro">* Nombres: </label>
                        <input type="text" class="form-control nombre" id="nom_arbitro" name="nom_arbitro" autocomplete="off" maxlength="45"  required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="ap_arbitro">* Apellidos: </label>
                        <input type="text" class="form-control nombre" id="ap_arbitro" name="ap_arbitro" autocomplete="off" maxlength="45" >
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="doc_arbitro">* Número documento: </label>
                        <input type="text" maxlength="10"  class="form-control numeros"  id="doc_arbitro" name="doc_arbitro" autocomplete="off" >
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="form-group">
                        <label for="email_arbitro">* Correo: </label>
                        <input type="email" class="form-control" id="email_arbitro" maxlength="45" name="email" autocomplete="off" required>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="cel_arbitro">* Celular: </label>
                        <input type="text" class="form-control numeros" id="cel_arbitro" name="cel_arbitro" maxlength="10" autocomplete="off" required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="dir_arbitro">* Dirección: </label>
                        <input type="text" class="form-control" id="dir_arbitro" name="dir_arbitro" maxlength="45" autocomplete="off" required>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="tel_arbitro">* Telefono: </label>
                        <input type="text" class="form-control numeros" id="tel_arbitro" name="tel_arbitro" maxlength="10" autocomplete="off">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="usuario_arbitro">* Usuario: </label>
                        <input type="text" class="form-control" id="usuario_arbitro" name="usuario" maxlength="15" autocomplete="off">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="col-sm-6 mx-auto" style="text-align: -webkit-center;">
                        <button type="submit" id="boton" class="btn btn-success mr-2" onclick="validar('reg_arbitro', 1);">Registrar</button>   
                    </div>
                    <input class="btn btn-light" type="button" value="Volver" onclick="javascript:redireccionar();">
                </div>
            </div>
        </form>
        <br>
    </div>
</div>
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
            registrarArbitro();
        operacion = null;
    }

    $(document).ready(function () {//el document ready es por donde empieza a cargar, lo que ud ponga de primero aca, eso es lo que se hace de primero
        $("#email_arbitro").attr("remote", URL + "ServletValidarCorreo");
        $("#doc_arbitro").attr("remote", URL + "ServletValidarDocumento?bandera=arbitro");
        $("#usuario_arbitro").attr("remote", URL + "ServletValidarUsuario");

        $("#reg_arbitro").validate({// el validate es sacado de codigo de internet, valida que los campos que tengan required este llenos
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

    function registrarArbitro() {
        $("#boton").prop('disabled', true);
        var usuario = {
            correo: $("#email_arbitro").val(),
            idTipoUsuario: ARBITRO,
            usuario: $("#usuario_arbitro").val(),
            registradoPor: nombreUsuario

        };

        var arbitro = {
            nombres: $("#nom_arbitro").val(),
            apellidos: $("#ap_arbitro").val(),
            documento: $("#doc_arbitro").val(),
            direccion: $("#dir_arbitro").val(),
            celular: $("#cel_arbitro").val(),
            telefono: $("#tel_arbitro").val(),
            registradoPor: nombreUsuario

        };

        ajaxElTorneo.registrarArbitro(arbitro, usuario, {
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


</script>