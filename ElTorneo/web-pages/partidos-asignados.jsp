<div class="content" id="tablaPartidos" >
    <div class="card bodyRegistrar">
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

<div class="modal fade" tabindex="-1" role="dialog" id="modalPartido">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Partido</h4>
            </div>
            <div class="modal-body">
                <form id="example-form" action="#">
                    <div>
                        <h3>Tarjetas</h3>
                        <section>
                            <div class="row">
                                <div class="col-md-6" id="participantesA">
                                    <h4 id="nombreEquipoA"></h4>
                                </div>
                                <div class="col-md-6" id="participantesB">
                                    <h4  id="nombreEquipoB"></h4>
                                </div> 
                            </div>

                        </section>
                        <h3>Participantes</h3>
                        <section>
                            <div class="row">
                                <div class="form-group" id="labelRadio" >
                                    <div class="form-group" style="display:  flex;flex-direction:  row;    justify-content: space-around; flex-wrap: wrap;" >
                                        <div class="form-radio form-radio-flat" >
                                            <label class="form-check-label" >
                                                <input type="radio" class="form-check-input" id="radio1" name="contador" > <span id="nombreEquipoA2"></span>
                                            </label>
                                        </div>
                                        <div class="form-radio form-radio-flat">
                                            <label class="form-check-label">
                                                <input type="radio" class="form-check-input"  id="radio2" name="contador"> <span id="nombreEquipoB2"></span>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="pos_jugador">Jugador </label>
                                        <select id="jugadores" class="form-control"></select>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="pos_jugador">Tarjeta </label>
                                        <select id="tarjetas" class="form-control">
                                            <option value="1">Tarjeta Amarilla</option>
                                            <option value="2">Tarjeta roja</option>
                                        </select>
                                    </div>
                                </div> 
                            </div>
                            <div class="row" style="text-align: center;">
                                <button type="button" class="btn btn-primary">A�adir</button>
                            </div>
                            <div id="tablaContacto" hidden class="table-responsive">
                                <table class="table table-bordered" id="tablaContactoDatos">
                                    <thead>
                                        <tr>
                                            <th>Nombres</th>
                                            <th>Apellidos</th>
                                            <th>Documento</th>
                                            <th>Acci�n</th>
                                        </tr>
                                    </thead>
                                    <tbody id="listadoTabla">

                                    </tbody>
                                </table>
                            </div>
                        </section>
                        <h3>Goles</h3>
                        <section>

                        </section>
                        <h3>Asistencias</h3>
                        <section>
                            <input id="acceptTerms" name="acceptTerms" type="checkbox" class="required"> <label for="acceptTerms">I agree with the Terms and Conditions.</label>
                        </section>
                    </div>
                </form>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
<script type="text/javascript" src="assets/js/wizard.js"></script>
<script>
    //variables globales
    var idEquipoA;
    var golesA = [];
    var golesB = [];
    var amarillasA = [];
    var amarillasB = [];
    var rojasA = [];
    var rojasB = [];
    var jugadoresA = [];
    var jugadoresB = [];
    var asistenciasA = [];
    var asistenciasB = [];

    function agregarJugadoresA(id) {
        if ($('#' + id).prop('checked')) {
            jugadoresA.push(id);
        } else {
            for (var i = 0; i < jugadoresA.length; i++) {
                if (jugadoresA[i] === id) {
                    jugadoresA.splice(i, 1);
                }
            }
        }
    }
    function agregarJugadoresB(id) {
        if ($('#' + id).prop('checked')) {
            jugadoresB.push(id);
        } else {
            for (var i = 0; i < jugadoresB.length; i++) {
                if (jugadoresB[i] === id) {
                    jugadoresB.splice(i, 1);
                }
            }
        }
    }

    $(document).ready(function () {
        listarPartidosPorIdArbitroCentral(idArbitroLogueado);

    });

    var listaPartidos = [];
    function listarPartidosPorIdArbitroCentral(id) {
        ajaxElTorneo.listarPartidosPorIdArbitroCentral(id, {
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

    var mapaPartidos = [
        function (data) {
            return '<p data-toggle="modal" data-target="#modalPartido" onclick="seleccionarPartido(' + data.id + ',' + data.equipoA + ',' + data.equipoB + ')" > ' + data.nombreEquipoA + '</p>';
        },
        function (data) {
            return '<p data-toggle="modal" data-target="#modalPartido" onclick="seleccionarPartido(' + data.id + ',' + data.equipoA + ',' + data.equipoB + ')"> ' + data.nombreEquipoB + '</p>';
        },
        function (data) {
            var d = new Date()
            var gmtHours = -d.getTimezoneOffset() / 60;
            var fecha = new Date(data.fechaPartido + " GMT" + gmtHours);

            var options = {weekday: 'long', year: 'numeric', month: 'long', day: 'numeric'};
            return '<p data-toggle="modal" data-target="#modalPartido" onclick="seleccionarPartido(' + data.id + ',' + data.equipoA + ',' + data.equipoB + ')"> ' + fecha.toLocaleDateString("es-CO", options) + '</p>';
        },
        function (data) {
            return '<p data-toggle="modal" data-target="#modalPartido" onclick="seleccionarPartido(' + data.id + ',' + data.equipoA + ',' + data.equipoB + ')"> ' + data.horaInicial + " - " + data.horaFinal + " PM" + '</p>';
        },
        function (data) {
            return '<p data-toggle="modal" data-target="#modalPartido" onclick="seleccionarPartido(' + data.id + ',' + data.equipoA + ',' + data.equipoB + ')"> ' + data.jornada + '</p>';
        }
    ];

    function seleccionarPartido(id, equipoA, equipoB) {
        ajaxElTorneo.verInformacionDePartidoPorId(id, {
            callback: function (data) {
                if (data !== null) {
                    $("#nombreEquipoA").text(data.nombreEquipoA);
                    $("#nombreEquipoB").text(data.nombreEquipoB);
                    $("#nombreEquipoA2").text(data.nombreEquipoA);
                    $("#nombreEquipoB2").text(data.nombreEquipoB);

                    $("#radio1").attr("onchange", "listarJugadoresEquipo(" + equipoA + ");");
                    $("#radio2").attr("onchange", "listarJugadoresEquipo(" + equipoB + ");");
                }
            },
            timeout: 20000
        });


        ajaxElTorneo.listarJugadoresPorIdEquipo(equipoA, {
            callback: function (data) {
                if (data !== null) {
                    $("#participantesA").html("");
                    console.log("entra primero", data);
                    for (var i = 0; i < data.length; i++) {
                        $("#participantesA").append("<div class='form-check form-check-flat'><label class='form-check-label'><input id=" + data[i].id + " value=" + i + " onclick='agregarJugadoresA(this.id);' type='checkbox' class='form-check-input impuestos'>" + data[i].dorsal + ". " + data[i].nombre + "<i class='input-helper'></i></div>");
                    }
                }
            },
            timeout: 20000
        });
        ajaxElTorneo.listarJugadoresPorIdEquipo(equipoB, {
            callback: function (data2) {
                if (data2 !== null) {
                    $("#participantesB").html("");
                    for (var j = 0; j < data2.length; j++) {
                        $("#participantesB").append("<div class='form-check form-check-flat'><label><input id=" + data2[j].id + " value=" + j + " onclick='agregarJugadoresB(this.id);' type='checkbox' class='form-check-input impuestos'> " + data2[j].dorsal + "." + data2[j].nombre + "<i class='input-helper'></i></div>");
                    }
                }
            },
            timeout: 20000
        });

    }


    function listarJugadoresEquipo(idEquipo) {
        ajaxElTorneo.listarJugadoresPorIdEquipo(idEquipo, {
            callback: function (data) {
                if (data !== null) {
                    dwr.util.removeAllOptions("jugadores");
                    dwr.util.addOptions("jugadores", [{
                            id: '',
                            nombreSelect: 'Seleccione jugador',
                        }], 'id', 'nombreSelect');
                    dwr.util.addOptions("jugadores", data, 'id', 'nombreSelect');
                }
            },
            timeout: 20000
        });
    }

   function agregarTarjeta() {

        var datos = {
            primerNombre: $("#nom1_contacto").val(),
            otrosNombres: $("#nom2_contacto").val()
        };
        listadoContacto[cantidadDatos] = datos;
        cantidadDatos++;
        $('#tablaContacto').removeAttr("hidden");
        dwr.util.removeAllRows("listadoTabla");
        dwr.util.addRows("listadoTabla", listadoContacto, mapaContacto, {
            escapeHtml: false
        });
        //   return showToastPosition('Mensaje', 'Se ha agregado el contacto con �xito', 'bottom-center', false);
    }

</script>