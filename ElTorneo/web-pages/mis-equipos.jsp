
<%@page import="co.eltorneo.mvc.dto.UsuarioDTO"%>
<style>
    label.error {
        color: #D84315 ;
        font-size: 0.9em;
        margin-top: -2px;
        padding: 0;
    }

</style>

<div class="content" id="tablaEqui">
    <div class="row">
        <div class="col-md-12 divAgregar">
            <button type="button" class="btn btn-primary" onclick="javascript:cargarPagina('registrar-equipo.jsp');" >Registrar equipo</button>
        </div>
    </div>
    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table datatable-html" >
                    <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>Tecnico</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody id="listaEquipos">

                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>

<div class="card tc-card" id="bodyRegistrarEquipo" style="display: none;">
    <div class="card-body"  >
        <h4 class="card-title" id="tituloForm"> <b>  Registrar equipo </b></h4> 
        <br>
        <form class="forms-sample" id="reg_equipo"  onsubmit="return false;">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="ap1_usuario">* Nombre del equipo: </label>
                        <input type="text" class="form-control nombre" id="nombreEquipo" name="nombreEquipo" autocomplete="off" maxlength="20"  required>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="col-sm-6 mx-auto" style="text-align: -webkit-center;">
                        <button type="submit" id="boton" class="btn btn-success mr-2" onclick="validar('reg_equipo', 1);">Guardar</button>   
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
    var idEquipoEditar;
    //globales
    var operacion = null;

    function validar(form, r) {
        operacion = r;
    }

    function postValidate() {
        if (operacion == 1)
            editarEquipo();
        operacion = null;
    }
    $(document).ready(function () {
        listarEquiposPorTecnico();

    });
    var listadoEquipos = [];
    var mapa = [
        function (data) {
            return data.nombre;
        },
        function (data) {
            return data.tecnico;
        },
        function (data) {
            return '<td><button type="button"  class="btn btn-primary" onclick="llenarEquipo(' + data.id + ')" >Detalle</button></td>';
        }
    ];

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
    function listarEquiposPorTecnico() {
        ajaxElTorneo.listarTodosLosEquiposPorTecnico(idTecnicoLogueado, {
            callback: function (data) {
                if (data !== null) {
                    jQuery('.datatable-html').dataTable().fnDestroy();
                    dwr.util.removeAllRows("listaEquipos");
                    listadoEquipos = data;
                    dwr.util.addRows("listaEquipos", listadoEquipos, mapa, {
                        escapeHtml: false
                    });
                    $('.datatable-html').dataTable();
                }
            },
            timeout: 20000
        });
    }

    function llenarEquipo(idEquipo) {
        $("#bodyRegistrarEquipo").show();
        $("#tablaEqui").hide();
        ajaxElTorneo.buscarEquipoPorId(idEquipo, {
            callback: function (data) {
                if (data !== null) {
                    console.log("esta es la data", data);
                    idEquipoEditar = data.id;
                    $("#nombreEquipo").val(data.nombre);
                }
            },
            timeout: 20000
        });
    }

    function editarEquipo() {
        $("#boton").prop('disabled', true);
        var equipo = {
            id: idEquipoEditar,
            nombre: $("#nombreEquipo").val(),
            registradoPor: nombreUsuario

        };

        ajaxElTorneo.actualizarEquipo(equipo, {
            callback: function (data) {
                if (data !== null) {
                    cargarPagina('mis-equipos.jsp');
                    $("#boton").prop('disabled', false);
                } else {
                    $("#boton").prop('disabled', false);
                }
            },
            timeout: 20000
        });
    }


</script>