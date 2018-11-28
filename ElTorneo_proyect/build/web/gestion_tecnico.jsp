
<%@page import="co.eltorneo.mvc.dto.UsuarioDTO"%>
<%-- TABLA ADMINISTRADOR (USUARIOS) --%>
<%

    UsuarioDTO datosUsuario = (UsuarioDTO) session.getAttribute("datosUsuario");
    System.out.print("Gestion tecnico JSP >>>> Datos usuario  " + datosUsuario.toStringJson());

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

<script>
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
            return '<td><button type="button"  class="btn btn-primary position-right">Detalle</button></td>';
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

</script>