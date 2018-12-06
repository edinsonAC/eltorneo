
<div class="content " id="tablaTecn">
    <div class="row">
        <div class="col-md-12 divAgregar">
            <button type="button" class="btn btn-primary" onclick="javascript:cargarPagina('registrar-usuario.jsp');" >Agregar tecnico</button>
        </div>
    </div>
    <div class="card bodyRegistrar">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table datatable-html" >
                    <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>Apellido</th>
                            <th>Teléfono</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody id="listTecnico">

                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>

<div class="card tc-card" id="bodyGestionTecnico" style="display: none;">
    <div class="card-body">
        <h4 class="card-title" id="tituloForm"> <b>  Editar tecnico </b></h4> 
        <br>
        <form class="forms-sample" id="edit_tecnico"  onsubmit="return false;">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="nom_tecnico">* Nombres: </label>
                        <input type="text" class="form-control nombre" id="nom_tecnico" name="nom_tecnico" autocomplete="off" maxlength="45"  required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="ap_tecnico">* Apellidos: </label>
                        <input type="text" class="form-control nombre" id="ap_tecnico" name="ap_tecnico" autocomplete="off" maxlength="45" >
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="doc_tecnico">* Número documento: </label>
                        <input type="text" maxlength="10"  class="form-control numeros"  id="doc_tecnico" name="doc_tecnico" autocomplete="off" >
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="form-group">
                        <label for="email_tecnico">* Correo: </label>
                        <input type="email" class="form-control" id="email_tecnico" maxlength="45" name="email_tecnico" autocomplete="off" required disabled>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="cel_tecnico">* Celular: </label>
                        <input type="text" class="form-control numeros" id="cel_tecnico" name="cel_tecnico" maxlength="10" autocomplete="off" required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="dir_tecnico">* Dirección: </label>
                        <input type="text" class="form-control" id="dir_tecnico" name="dir_tecnico" maxlength="45" autocomplete="off" required>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="tel_tecnico">* Telefono: </label>
                        <input type="text" class="form-control numeros" id="tel_tecnico" name="tel_tecnico" maxlength="10" autocomplete="off">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="usuario_tecnico">* Usuario: </label>
                        <input type="text" class="form-control" id="usuario_tecnico" name="usuario_tecnico" maxlength="15" autocomplete="off" disabled>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="col-sm-6 mx-auto" style="text-align: -webkit-center;">
                        <button type="submit" id="boton" class="btn btn-success mr-2" onclick="validar('edit_tecnico', 1);">Guardar</button>   
                    </div>
                    <input class="btn btn-light" type="button" value="Volver" onclick="javascript:redireccionar();">
                </div>
            </div>
        </form>
        <br>
    </div>
</div>

<script>
    //globales
    var idTecnicoEditar;
    var operacion = null;
    function validar(form, r) {
        operacion = r;
    }

    function postValidate() {
        if (operacion == 1)
            editarTecnico();
        operacion = null;
    }

    $(document).ready(function () {
        listarTecnicos();

    });
    var listado2 = [];
    var mapa = [
        function (data) {
            return data.nombre;
        },
        function (data) {
            return data.apellido;
        },
        function (data) {
            return data.celular;
        },
        function (data) {
            return '<td><button type="button"  class="btn btn-primary position-right" onclick="llenartecnico(' + data.id + ')">Detalle</button></td>';
        }
    ];

    $("#edit_tecnico").validate({// el validate es sacado de codigo de internet, valida que los campos que tengan required este llenos
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





    function listarTecnicos() {
        ajaxElTorneo.listarTecnicos({
            callback: function (data) {
                if (data !== null) {
                    jQuery('.datatable-html').dataTable().fnDestroy();
                    dwr.util.removeAllRows("listTecnico");
                    listado2 = data;
                    dwr.util.addRows("listTecnico", listado2, mapa, {
                        escapeHtml: false
                    });
                    $('.datatable-html').dataTable();
                }
            },
            timeout: 20000
        });
    }


    function llenartecnico(idTecnico) {
        $("#bodyGestionTecnico").show();
        $("#tablaTecn").hide();
        ajaxElTorneo.buscarTecnicoPorId(idTecnico, {
            callback: function (data) {
                if (data !== null) {
                    idTecnicoEditar = data.id;
                    $("#email_tecnico").val(data.correo);
                    $("#nom_tecnico").val(data.nombre);
                    $("#ap_tecnico").val(data.apellido);
                    $("#doc_tecnico").val(data.documento);
                    $("#dir_tecnico").val(data.direccion);
                    $("#cel_tecnico").val(data.celular);
                    $("#tel_tecnico").val(data.telefono);
                    $("#usuario_tecnico").val(data.usuario);

                }
            },
            timeout: 20000
        });
    }


    function editarTecnico() {
        $("#boton").prop('disabled', true);

        var tecnico = {
            id: idTecnicoEditar,
            nombre: $("#nom_tecnico").val(),
            apellido: $("#ap_tecnico").val(),
            documento: $("#doc_tecnico").val(),
            direccion: $("#dir_tecnico").val(),
            celular: $("#cel_tecnico").val(),
            telefono: $("#tel_tecnico").val(),
            correo: $("#email_tecnico").val(),
            idTipoUsuario: TECNICO,
            usuario: $("#usuario_tecnico").val()

        };

        ajaxElTorneo.actualizarTecnico(tecnico, {
            callback: function (data) {
                if (data !== null) {
                    cargarPagina('gestion_tecnico.jsp');
                    // limpiar();
                } else {
                    $("#boton").prop('disabled', false);
                }
            },
            timeout: 20000
        });

        // recargar();
    }

</script>