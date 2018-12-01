
<div class="content" id="tablaTecn">
    <div class="card">
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

<div class="card tc-card" id="bodyGestionTecnico">
    <div class="card-body"  >
        <h4 class="card-title" id="tituloForm"> <b>  Editar Arbitro </b></h4> 
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
                        <input type="email" class="form-control" id="email_tecnico" maxlength="45" name="email_tecnico" autocomplete="off" required>
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
                        <input type="text" class="form-control" id="usuario_tecnico" name="usuario_tecnico" maxlength="15" autocomplete="off">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="col-sm-6 mx-auto" style="text-align: -webkit-center;">
                        <button type="submit" id="boton" class="btn btn-success mr-2" onclick="validar('edit_tecnico', 1);">Registrar</button>   
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
                    console.log("llega--> ", data);
                    $("#email_tecnico").val(data.correo);
                    $("#usuario_arbitro").val(data.usuario);
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

</script>