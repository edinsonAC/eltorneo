
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

<div class="content" id="tablaTecn">
    <div class="card">
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

<script>
    $(document).ready(function () {
        listarJugadores();

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
            return '<td><button type="button"  class="btn btn-primary position-right">Detalle</button></td>';
        }
    ];
    function listarJugadores() {
        ajaxElTorneo.listarJugadoresPorIdEquipo(idEquipo,{
            callback: function (data) {
                if (data !== null) {
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

</script>