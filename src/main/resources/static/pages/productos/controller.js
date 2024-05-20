var backend = "http://localhost:8080/api";
var api_login = backend + '/user';

document.addEventListener("DOMContentLoaded",loaded);

async function loaded(event) {
    try{ await checkuser();} catch(error){return;}
}
async function checkuser() {
    let request = new Request(api_login + '/current-user', { method: 'GET' });
    const response = await fetch(request);
    if (response.ok) {
        loginstate.logged = true;
        loginstate.usuario = await response.json();

        // Obtener detalles del usuario
        let detailsRequest = new Request(backend + '/user/details', { method: 'GET' });
        const detailsResponse = await fetch(detailsRequest);
        if (detailsResponse.ok) {
            let userDetails = await detailsResponse.json();
            loginstate.usuario.role = userDetails.role;
        } else {
            loginstate.logged = false;
        }
    } else {
        loginstate.logged = false;
    }
    render_menu();
}
var loginstate = {
    logged: false,
    usuario: { identificacion: "", role: "" }
};

function render_menu() {
    let html;
    if (!loginstate.logged) {
        html = `
            <div class="logo">
            </div>
            <div>
                <ul class="Menu">
                    <li id="loginlink"><a href="#"> Login</a></li>
                </ul>
            </div>
        `;
        document.querySelector('#menu').innerHTML = html;
        document.querySelector("#menu #loginlink").addEventListener('click', ask);
    } else {
        html = `
            <div class="logo">
            </div>
            <div>
                <ul class="Menu">
                    <li id="logoutlink"><a href="#"> Logout</a></li>
                    ${loginstate.usuario.role.includes('PRO') ? `
                        <li id="facturarLink"><a href="#">Facturar</a></li>
                        <li id="clientesLink"><a href="#">Clientes</a></li>
                        
                        <!-- Aqui debe resolverse este inconveniente con el link de Productos el cual lleva el ID del Proveedor -->
                        <li id="productosLink"><a href="#">Productos</a></li>
                       
                        <li id="facturasLink"><a href="#">Facturas</a></li>
                        <li id="perfilLink"><a href="#">Perfil</a></li>
                    ` : ''}
                    ${loginstate.usuario.role.includes('ADM') ? `
                        <li id="administrarLink"><a href="/pages/otros/administrador.html#">Administrar</a></li>
                    ` : ''}
                </ul>
            </div>
            <div class="usuario">&nbsp &nbsp ${loginstate.usuario.identificacion}</div>
        `;
        document.querySelector('#menu').innerHTML = html;
        document.querySelector("#menu #logoutlink").addEventListener('click', logout);
        document.querySelector("#menu #facturarLink").addEventListener('click', e => {
            document.location = "/pages/facturar/crearFactura.html";
        });
        document.querySelector("#menu #clientesLink").addEventListener('click', e => {
            document.location = "/pages/clientes/view.html";
        });
        document.querySelector("#menu #productosLink").addEventListener('click', e => {
            document.location = "/pages/productos/agregarProducto.html";
        });
        document.querySelector("#menu #facturasLink").addEventListener('click', e => {
            document.location = "/pages/facturar/showFacturar.html";
        });

        document.querySelector("#menu #perfilLink").addEventListener('click', e => {
            document.location = "/pages/otros/perfil.html";
        });

        document.querySelector("#menu #administrarLink").addEventListener('click', e => {
            document.location = "/pages/otros/administrador.html";
        });

    }
}