var backend = "http://localhost:8080/api";
var api_login = backend + '/login';

var loginstate = {
    logged: false,
    usuario: { identificacion: "", rol: "" }
};



async function checkuser() {
    let request = new Request(api_login + '/current-user', { method: 'GET', credentials: 'include' });
    const response = await fetch(request);
    if (response.ok) {
        loginstate.logged = true;
        loginstate.usuario = await response.json();

        // Obtener detalles del usuario
        let detailsRequest = new Request(backend + '/user/details', { method: 'GET', credentials: 'include' });
        const detailsResponse = await fetch(detailsRequest);
        if (detailsResponse.ok) {
            let userDetails = await detailsResponse.json();
            loginstate.usuario.rol = userDetails.role;
        } else {
            loginstate.logged = false;
        }
    } else {
        loginstate.logged = false;
    }
}


async function menu() {
    loadLoginState();
    if (!loginstate.logged && document.location.pathname != "/pages/login/show.html") {
        document.location = "/pages/login/show.html";
        throw new Error("Usuario no autorizado");
    }
    render_menu();
}

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
        render_loginoverlay();
        render_loginview();
    } else {
        html = `
            <div class="logo">
            </div>
            <div>
                <ul class="Menu">
                    <li id="logoutlink"><a href="#"> Logout</a></li>
                    ${loginstate.usuario.rol.includes('PRO') ? `
                        <li id="facturarLink"><a href="#">Facturar</a></li>
                        <li id="clientesLink"><a href="#">Clientes</a></li>
                        <li id="productosLink"><a href="#">Productos</a></li>
                        <li id="facturasLink"><a href="#">Facturas</a></li>
                        <li id="perfilLink"><a href="#">Perfil</a></li>
                    ` : ''}
                    ${loginstate.usuario.rol.includes('ADM') ? `
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

function render_loginoverlay() {
    let html = `<div id="loginoverlay" class="loginoverlay"></div>`;
    let overlay = document.createElement('div');
    overlay.innerHTML = html;
    document.body.appendChild(overlay);
    document.querySelector("#loginoverlay").addEventListener("click", toggle_loginview);
}

function render_loginview() {
    let html = `
        <div id="loginview" class='loginview'>
            <div class='col-12'>
                <div>
                    <form name="formulario">
                        <div class='container'>
                            <div class='row'><div class='col-12 text-centered cooper'>Login</div></div>
                            <div class='row'><div class='col-3 text-right'>Id</div><div class='col-6'><input type="text" name="identificacion" id="identificacion" value=""></div></div>
                            <div class='row'><div class='col-3 text-right'>Clave</div><div class='col-6'><input type="password" name="contrasena" id="contrasena" value=""></div></div>
                            <div class='row'>
                                <div class='col-6 text-centered cooper'>
                                    <input id="login" class="boton" type="button" value="Login">
                                    &nbsp
                                    <input id="cancelar" class="boton" type="button" value="Cancelar">
                                    <li id="registro"><a href="/pages/registro/registrar.html#">Resgistro</a></li>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>    
    `;
    let view = document.createElement('div');
    view.innerHTML = html;
    document.body.appendChild(view);
    document.querySelector("#loginview #login").addEventListener("click", login);
    document.querySelector("#loginview #cancelar").addEventListener("click", toggle_loginview);
    document.querySelector("#menu #registro").addEventListener('click', e => {
        document.location = "/pages/registro/registrar.html";
    });
}

function ask(event) {
    event.preventDefault();
    toggle_loginview();
    document.querySelectorAll('#loginview input').forEach((i) => { i.classList.remove("invalid"); });
    document.querySelector("#loginview #identificacion").value = "";
    document.querySelector("#loginview #contrasena").value = "";
}

function toggle_loginview() {
    document.getElementById("loginoverlay").classList.toggle("active");
    document.getElementById("loginview").classList.toggle("active");
}

async function login() {
    let usuario = {
        identificacion: document.getElementById("identificacion").value,
        contrasena: document.getElementById("contrasena").value
    };
    let request = new Request(api_login + '/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(usuario)
    });
    const response = await fetch(request);
    if (!response.ok) {
        errorMessage(response.status);
        return;
    }

    // Guardar el estado de login en localStorage
    loginstate.logged = true;
    loginstate.usuario = await response.json();
    localStorage.setItem('loginstate', JSON.stringify(loginstate));

    document.location = "/pages/productos/agregarProducto.html";
}

async function logout(event) {
    event.preventDefault();
    let request = new Request(api_login + '/logout', { method: 'POST' });
    const response = await fetch(request);
    if (!response.ok) {
        errorMessage(response.status);
        return;
    }

    // Limpiar el estado de login de localStorage
    localStorage.removeItem('loginstate');

    document.location = "/pages/login/show.html";
}

function errorMessage(status) {
    alert("Error: " + status);
}


document.addEventListener('DOMContentLoaded', () => {
    loadLoginState();
});
document.addEventListener('DOMContentLoaded', menu);

/*window.onload = function() {
    var headerContainer = document.getElementById("header");
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            headerContainer.innerHTML = this.responseText;
        }
    };
    xhttp.open("GET", "/pages/header.html", true);
    xhttp.send();
};

document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("producto-form");

    form.addEventListener("submit", async function(event) {
        event.preventDefault();

        const nombre = document.getElementById("nombre").value;
        const precio = document.getElementById("precio").value;
        const proveedor = document.getElementById("proveedor").value;

        try {
            const response = await fetch("/agregarProducto", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: `nombre=${nombre}&precio=${precio}&proveedor=${proveedor}`
            });

            if (response.ok) {
                alert("Producto agregado correctamente");
                // Realizar cualquier otra acción necesaria después de agregar el producto
            } else {
                alert("Error al agregar el producto");
            }
        } catch (error) {
            console.error("Error:", error);
            alert("Ocurrió un error al procesar la solicitud");
        }
    });
});
*/






//--------------REGISTRO
function loadLoginState() {
    let savedState = localStorage.getItem('loginstate');
    if (savedState) {
        loginstate = JSON.parse(savedState);
    }
}