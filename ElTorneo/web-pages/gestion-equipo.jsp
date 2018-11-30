
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

<script>
    $(document).ready(function () {
        listarEquipos();

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
            return '<td><button type="button"  class="btn btn-primary position-right">Detalle</button></td>';
        }
    ];
    function listarEquipos() {
        ajaxElTorneo.listarEquipos({
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

</script>