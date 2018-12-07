
<%@page import="co.eltorneo.mvc.dto.UsuarioDTO"%>
<%-- TABLA ADMINISTRADOR (USUARIOS) --%>
<%

    UsuarioDTO datosUsuario = (UsuarioDTO) session.getAttribute("datosUsuario");
    System.out.print("Gestion de usuarios JSP >>>> Datos usuario  " + datosUsuario.toStringJson());

%>
<style>
    label.error {
        color: #D84315 ;
        font-size: 0.9em;
        margin-top: -2px;
        padding: 0;
    }

</style>

<div class="content" id="tablaJuga">
    <div class="row">
        <div class="col-md-12 divAgregar">
            <button type="button" class="btn btn-primary" onclick="javascript:cargarPagina('registrarJugador.jsp');" >Agregar jugador</button>
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
                            <th>Posicion</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody id="listJugador">

                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>

<div class="card tc-card bodyRegistrar" id="bodyGestionJugador" style="display: none;">
    <div class="card-body"  >
        <h4 class="card-title" id="tituloForm"> <b>  Editar jugador </b></h4> 
        <br>
        <form class="forms-sample" id="mod_jugador"  onsubmit="return false;">
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
                        <input type="email" class="form-control" id="email_jugador" maxlength="45" name="email_jugador" autocomplete="off" required disabled>
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
                        <input type="text" class="form-control" id="usuario_jugador" name="usuario_jugador" maxlength="15" autocomplete="off" disabled>
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
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="dorsal_jugador">* Dorsal </label>
                        <input type="number" class="form-control" id="dorsal_jugador" name="dorsal_jugador" maxlength="15" autocomplete="off">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="col-sm-6 mx-auto" style="text-align: -webkit-center;">
                        <button type="submit" id="boton" class="btn btn-success mr-2" onclick="validar('mod_jugador', 1);">Guardar</button>    
                    </div>
                    <input class="btn btn-light" type="button" value="Volver" onclick="javascript:redireccionar();">
                </div>
            </div>
        </form>
        <br>
    </div>
</div>
<script>
    // variables globales
    function redireccionar() {
        cargarPagina('gestion-jugador.jsp');
    }
    var idJugadorEditar;
    var operacion = null;
    function validar(form, r) {
        operacion = r;
    }

    function postValidate() {
        if (operacion == 1)
            editarJugador();
        operacion = null;
    }


    $(document).ready(function () {
        if (idEquipo == "" || idEquipo == null) {
            $(".divAgregar").hide();
        }
        listarJugadores();
        listarPosiciones();
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
            return data.posicion;
        },
        function (data) {
            return '<td><button type="button"  class="btn btn-primary position-right" onclick="llenarJugador(' + data.id + ')">Editar</button></td>';
        }
    ];

    $("#mod_jugador").validate({
        errorPlacement: function (label, element) {
            label.addClass('mt-2 text-danger');
            $(element).parent().append(label);
        },
        highlight: function (element, errorClass) {
            $(element).parent().addClass('has-danger');
            $(element).addClass('form-control-danger');

        },
        success: function (label) {
            jQuery(label).closest('.has-danger').removeClass('has-danger');
            label.remove();
        },
        submitHandler: function () {
            postValidate();
        }
    });


    function listarJugadores() {
        ajaxElTorneo.listarJugadoresPorIdEquipo(idEquipo, {
            callback: function (data) {
                if (data !== null) {
                    if (data.length > 19) {
                        $(".divAgregar").hide();
                    }
                    jQuery('.datatable-html').dataTable().fnDestroy();
                    dwr.util.removeAllRows("listJugador");
                    listado2 = data;
                    dwr.util.addRows("listJugador", listado2, mapa, {
                        escapeHtml: false
                    });
                    $('.datatable-html').dataTable();
                }
            },
            timeout: 20000
        });
    }

    function llenarJugador(idJugador) {
        $("#bodyGestionJugador").show();
        $("#tablaJuga").hide();
        ajaxElTorneo.buscarJugadorPorId(idJugador, {
            callback: function (data) {
                if (data !== null) {
                    console.log("esta es la data", data);
                    idJugadorEditar = data.id;
                    $("#email_jugador").val(data.correo);
                    $("#usuario_jugador").val(data.usuario);
                    $("#nom_jugador").val(data.nombre);
                    $("#ap_jugador").val(data.apellido);
                    $("#doc_jugador").val(data.documento);
                    $("#dir_jugador").val(data.direccion);
                    $("#cel_jugador").val(data.celular);
                    $("#tel_jugador").val(data.telefono);
                    $("#pos_jugador").val(data.idPosicion);
                    $("#dorsal_jugador").val(data.dorsal);
                }
            },
            timeout: 20000
        });
    }


    function editarJugador() {
        $("#boton").prop('disabled', true);
        var jugador = {
            id: idJugadorEditar,
            nombre: $("#nom_jugador").val(),
            apellido: $("#ap_jugador").val(),
            documento: $("#doc_jugador").val(),
            direccion: $("#dir_jugador").val(),
            celular: $("#cel_jugador").val(),
            telefono: $("#tel_jugador").val(),
            idPosicion: $("#pos_jugador").val(),
            dorsal: $("#dorsal_jugador").val(),
            correo: $("#email_jugador").val(),
            usuario: $("#usuario_jugador").val()

        };

        ajaxElTorneo.actualizarJugador(jugador, {
            callback: function (data) {
                if (data !== null) {
                    $("#boton").prop('disabled', false);
                    cargarPagina('gestion-jugador.jsp');

                } else {
                    $("#boton").prop('disabled', false);
                }
            },
            timeout: 20000
        });

        // recargar();
    }

    function listarPosiciones() {
        console.log("entraa la funcion de posiciones");
        ajaxElTorneo.listarPosicionesDeJuego({
            callback: function (data) {
                if (data !== null) {
                    console.log("con esta data", data);
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