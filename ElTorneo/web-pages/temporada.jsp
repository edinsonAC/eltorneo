<div class="content" id="tablaTempa">
    <div class="row">
        <div class="col-md-12 divAgregar">
            <button type="button" class="btn btn-primary" onclick="javascript:cargarPagina('registrar-temporada.jsp');" >Crear torneo</button>
        </div>
    </div>
    <div class="card bodyRegistrar">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table datatable-html">
                    <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>fecha inicio</th>
                            <th>fecha fin </th>
                            <th>Equipos</th>
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
    <div class="row">
        <div class="col-md-12 divAgregar">
            <button type="button" class="btn btn-primary" onclick="javascript:cargarPagina('registrar-temporada.jsp');" >Crear torneo</button>
        </div>
    </div>
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


<div class="card tc-card" id="bodyRegistrarTemporada" style="display: none;">
    <div class="card-body"  >
        <h4 class="card-title" id="tituloForm"> <b> Torneo </b></h4> 
        <br>
        <form class="forms-sample" id="edit_temporada"  onsubmit="return false;">
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
                        <button type="submit" id="boton" class="btn btn-success mr-2" onclick="validar('edit_temporada', 1);">Guardar</button>     
                    </div>
                    <input class="btn btn-light" type="button" value="Volver" onclick="javascript:redireccionar();">
                </div>
            </div>
        </form>
        <br>
    </div>
</div>


<div class="card tc-card" id="sorteo" style="display: none;">
    <div class="card-body"  >
        <h4 class="card-title" id="tituloForm"> <b> Iniciar torneo </b></h4> 
        <br>
        <div class="row">
            <div class="col-md-6">
                <ul>
                    <li>Requisitos</li>
                    <li>Equipos registrados <span id="equi_activos">0</span> de <span id="equi_requisito">0</span> </li>
                    <li>Arbitros activos <span id="arbi_activos">0</span> de <span id="arbi_requisito">3</span> </li>                  
                </ul>
            </div>
            <div class="col-md-6">
                <ul>
                    <button type="button" id="boton2" class="btn btn-success mr-2" onclick="sortearEquipos();">sorteo</button>                 
                </ul>
            </div>
        </div>
        <br>
    </div>
</div>
<script>
    // variables globales
    var idTemporadaEditar;
    var operacion = null;
    var fechaSorteo;
    function redireccionar() {
        cargarPagina('temporada.jsp');
    }
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
        listarTemporadas();
        validarTemporadaEnProceso();


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

    function listarTemporadas() {
        ajaxElTorneo.listarTemporadas({
            callback: function (data) {
                if (data !== null) {
                    console.log("va,oss!!", data);
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

    function validarTemporadaEnProceso() {
        ajaxElTorneo.validarTemporadaEnProceso({
            callback: function (data) {
                if (data) {
                    $(".divAgregar").hide();
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

                    if (data.banderaSorteo == '1') {
                        $("#num_equipos").prop("disabled", true);
                        $("#fechaIn_temporada").prop("disabled", true);
                        $("#sorteo").hide();
                    } else {
                        $("#sorteo").show();
                    }
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
            if (data.fechaFinal == null || data.fechaFinal == "") {
                return "Por definir";
            } else {
                return data.fechaFinal;
            }

        },
        function (data) {
            return data.numEquipos;
        },
        function (data) {
            return '<td><button type="button"  class="btn btn-primary position-right" onclick="llenarTemporada(' + data.id + ')">Editar</button></td>';
        }
    ];

    var mapaPartidos = [
        function (data) {
            return data.nombreEquipoA;
        },
        function (data) {
            return data.nombreEquipoB;
        },
        function (data) {
            var d = new Date()
            var gmtHours = -d.getTimezoneOffset() / 60;
            var fecha = new Date(data.fechaPartido + " GMT" + gmtHours);

            var options = {weekday: 'long', year: 'numeric', month: 'long', day: 'numeric'};
            return fecha.toLocaleDateString("es-CO", options);
        },
        function (data) {
            return data.horaInicial + " - " + data.horaFinal + " PM";
        },
        function (data) {
            return data.jornada;
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
    function listarPartidosPorTemporada() {
        $("#tablaTempa").hide();
        $("#bodyRegistrarTemporada").hide();
        $("#sorteo").hide();
        $("#tablaPartidos").show();
        ajaxElTorneo.listarPartidosPorIdTemporada(idTemporadaEditar, {
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