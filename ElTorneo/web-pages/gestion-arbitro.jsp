<div class="content" id="tablaArb">
    <div class="row">
        <div class="col-md-12 divAgregar">
            <button type="button" class="btn btn-primary" onclick="javascript:cargarPagina('registrar-arbitro.jsp');" >Agregar arbitro</button>
        </div>
    </div>
    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table datatable-html" >
                    <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>Apellido</th>
                            <th>Telefono</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody id="listArbitro">

                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>

<div class="card tc-card" id="bodyGestionArbitro" style="display: none;">
    <div class="card-body"  >
        <h4 class="card-title" id="tituloForm"> <b>  Editar Arbitro </b></h4> 
        <br>
        <form class="forms-sample" id="edi_arbitro"  onsubmit="return false;">
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
                        <input type="email" class="form-control" id="email_arbitro" maxlength="45" name="email_arbitro" autocomplete="off" required>
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
                        <input type="text" class="form-control" id="usuario_arbitro" name="usuario_arbitro" maxlength="15" autocomplete="off">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="col-sm-6 mx-auto" style="text-align: -webkit-center;">
                        <button type="submit" id="boton" class="btn btn-success mr-2" onclick="validar('edit_arbitro', 1);">Guardar</button>   
                    </div>
                    <input class="btn btn-light" type="button" value="Volver" onclick="javascript:cargarPagina('gestion-arbitro.jsp');">
                </div>
            </div>
        </form>
        <br>
    </div>
</div>
<script>
    // variables globales
    var idArbitroEditar;
    var operacion = null;

    function validar(form, r) {
        operacion = r;
    }

    function postValidate() {
        if (operacion == 1)
            editarArbitro();
        operacion = null;
    }

    $(document).ready(function () {
        listarArbitros();

    });
    var listado2 = [];
    var mapa = [
        function (data) {
            return data.nombres;
        },
        function (data) {
            return data.apellidos;
        },
        function (data) {
            return data.telefono;
        },
        function (data) {
            return '<td><button type="button"  class="btn btn-primary position-right" onclick="llenarArbitro(' + data.id + ')">Editar</button></td>';
        }
    ];
    function listarArbitros() {
        ajaxElTorneo.listarArbitros({
            callback: function (data) {
                if (data !== null) {
                    jQuery('.datatable-html').dataTable().fnDestroy();
                    dwr.util.removeAllRows("listArbitro");
                    listado2 = data;
                    dwr.util.addRows("listArbitro", listado2, mapa, {
                        escapeHtml: false
                    });
                    $('.datatable-html').dataTable();
                }
            },
            timeout: 20000
        });
    }

    $("#edi_arbitro").validate({// el validate es sacado de codigo de internet, valida que los campos que tengan required este llenos
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


    function llenarArbitro(idArbitro) {
        $("#bodyGestionArbitro").show();
        $("#tablaArb").hide();
        ajaxElTorneo.buscarArbitroPorId(idArbitro, {
            callback: function (data) {
                if (data !== null) {
                    idArbitroEditar = data.id;
                    $("#email_arbitro").val(data.correo);
                    $("#usuario_arbitro").val(data.usuario);
                    $("#nom_arbitro").val(data.nombres);
                    $("#ap_arbitro").val(data.apellidos);
                    $("#doc_arbitro").val(data.documento);
                    $("#dir_arbitro").val(data.direccion);
                    $("#cel_arbitro").val(data.celular);
                    $("#tel_arbitro").val(data.telefono);

                }
            },
            timeout: 20000
        });
    }

    function editarArbitro() {
        $("#boton").prop('disabled', true);

        var arbitro = {
            id: idArbitroEditar,
            nombres: $("#nom_arbitro").val(),
            apellidos: $("#ap_arbitro").val(),
            documento: $("#doc_arbitro").val(),
            direccion: $("#dir_arbitro").val(),
            celular: $("#cel_arbitro").val(),
            telefono: $("#tel_arbitro").val(),
            correo: $("#email_arbitro").val(),
            idTipoUsuario: ARBITRO,
            usuario: $("#usuario_arbitro").val(),
            registradoPor: nombreUsuario

        };

        ajaxElTorneo.actualizarArbitro(arbitro, {
            callback: function (data) {
                if (data !== null) {
                    cargarPagina('gestion-arbitro.jsp');
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