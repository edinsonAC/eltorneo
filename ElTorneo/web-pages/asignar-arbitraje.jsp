<div class="content" id="tablaTempa">
    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table datatable-html">
                    <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>fecha inicio</th>
                            <th>Accion</th>
                        </tr>
                    </thead>
                    <tbody id="listTemporadas">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div class="content" id="tablaPartidos" style="display: none;">
    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table datatable-html" id="datatable-html">
                    <thead>
                        <tr>
                            <th>Equipo A</th>
                            <th>Equipo B</th>
                            <th>dia</th>
                            <th>Hora</th>
                            <th>Jornada</th>
                        </tr>
                    </thead>
                    <tbody id="listPartidos">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" tabindex="-1" role="dialog" id="modalArbitraje">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Asignar arbitros</h4>
            </div>
            <div class="modal-body">
                <p>One fine body&hellip;</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary">Save changes</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<script>
    // variables globales
    var idTemporadaEditar;
    var operacion = null;
    var fechaSorteo;
    function validar(form, r) {
        operacion = r;
    }

    function postValidate() {
        if (operacion == 1)
            editarTemporada();
        operacion = null;
    }
    var listado2 = [];
    $(document).ready(function () {
        llenarSelect();
        listarTemporadasEnProceso();


        $("#edit_temporada").validate({// el validate es sacado de codigo de internet, valida que los campos que tengan required este llenos
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

    });

    function listarTemporadasEnProceso() {
        ajaxElTorneo.listarTemporadaEnProceso({
            callback: function (data) {
                if (data !== null) {
                    jQuery('.datatable-html').dataTable().fnDestroy();
                    dwr.util.removeAllRows("listTemporadas");
                    listado2 = data;
                    dwr.util.addRows("listTemporadas", listado2, mapa, {
                        escapeHtml: false
                    });
                    $('.datatable-html').dataTable();
                }
            },
            timeout: 20000
        });
    }

    function llenarTemporada(idTemporada) {
        $("#bodyRegistrarTemporada").show();
        $("#sorteo").show();
        $("#tablaTempa").hide();
        ajaxElTorneo.buscarTemporadaPorId(idTemporada, {
            callback: function (data) {
                if (data !== null) {
                    console.log("esta es la data", data);
                    idTemporadaEditar = data.id;
                    $("#nombre_temporada").val(data.nombre);
                    $("#equi_requisito").text(data.numEquipos);
                    $("#num_equipos").val(data.numEquipos);
                    $("#fechaIn_temporada").val(data.fechaInicial);
                    fechaSorteo = data.fechaInicial;
                    calcularRequisitos();
                }
            },
            timeout: 20000
        });
    }
    function llenarSelect() {
        for (var i = 4; i <= 12; i++) {
            $("#num_equipos").append(new Option(i, i));
        }
    }


    var mapa = [
        function (data) {
            return data.nombre;
        },
        function (data) {
            return data.fechaInicial;
        },
        function (data) {
            return '<td><button type="button"  class="btn btn-primary position-right" onclick="listarPartidosPorTemporada(' + data.id + ')">Ver partidos</button></td>';
        }
    ];

    var mapaPartidos = [
        function (data) {
            return '<p data-toggle="modal" data-target="#modalArbitraje" > ' + data.nombreEquipoA + '</p>';
        },
        function (data) {
            return '<p data-toggle="modal" data-target="#modalArbitraje"> ' + data.nombreEquipoB + '</p>';
        },
        function (data) {
            var d = new Date()
            var gmtHours = -d.getTimezoneOffset() / 60;
            var fecha = new Date(data.fechaPartido + " GMT" + gmtHours);

            var options = {weekday: 'long', year: 'numeric', month: 'long', day: 'numeric'};
            return '<p data-toggle="modal" data-target="#modalArbitraje"> ' + fecha.toLocaleDateString("es-CO", options) + '</p>';
        },
        function (data) {
            return '<p data-toggle="modal" data-target="#modalArbitraje"> ' + data.horaInicial + " - " + data.horaFinal + " PM" + '</p>';
        },
        function (data) {
            return '<p data-toggle="modal" data-target="#modalArbitraje"> ' + data.jornada + '</p>';
        }
    ];



    function editarTemporada() {//aqui empieza la funcion del registro del tecnico
        $("#boton").prop('disabled', true);
        var temporada = {
            id: idTemporadaEditar,
            nombre: $("#nombre_temporada").val(),
            numEquipos: $("#num_equipos").val(),
            fechaInicial: $("#fechaIn_temporada").val()

        };

        ajaxElTorneo.actualizarTemporada(temporada, {
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
    }


    function sortearEquipos() {
        ajaxElTorneo.sorteoDePartidos(fechaSorteo, idTemporadaEditar, {
            callback: function (data) {
                if (data !== null) {
                    listarPartidosPorTemporada();
                }
            },
            timeout: 20000
        });
    }


    function calcularRequisitos() {
        ajaxElTorneo.arbitrosEquiposActivos({
            callback: function (data) {
                if (data !== null) {
                    $("#equi_activos").text(data.equiposActivos);
                    $("#arbi_activos").text(data.arbitrosActivos);
                }
            },
            timeout: 20000
        });
    }


    var listaPartidos = [];
    function listarPartidosPorTemporada(idTemporada) {
        $("#tablaTempa").hide();
        $("#tablaPartidos").show();
        ajaxElTorneo.listarPartidosPorIdTemporada(idTemporada, {
            callback: function (data) {
                if (data !== null) {
                    jQuery('#datatable-html').dataTable().fnDestroy();
                    dwr.util.removeAllRows("listPartidos");
                    listaPartidos = data;
                    $.when(dwr.util.addRows("listPartidos", listaPartidos, mapaPartidos, {
                        escapeHtml: false
                    })).done(function () {
                        $('#datatable-html').dataTable();

                    });
                }
            },
            timeout: 20000
        });
    }
</script>