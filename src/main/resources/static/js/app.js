var backend="http://localhost:8080/api";
var api_login=backend+'/login';

var loginstate ={
    logged: false,
    user : {id:"", rol:""}
}

async function checkuser(){
    let request = new Request(api_login+'/current-user', {method: 'GET'});
    const response = await fetch(request);
    if (response.ok) {
        loginstate.logged = true;
        loginstate.user = await response.json();
    } else {
        loginstate.logged = false;
    }
}

async function menu(){
    await checkuser();
    if (!loginstate.logged && document.location.pathname !== "/pages/productos/agregarProducto.html") {
        document.location = "/pages/productos/agregarProducto.html";
        throw new Error("Usuario no autorizado");
    }
    render_menu();
}

function render_menu() {
    let html;
    if (!loginstate.logged) {
        html = `
            <div class="logo">
                <span>Personas</span>
                <img src="/images/logo.jpg">
            </div>
            <div>
                <ul class="Menu">
                    <li id="loginlink"><a href="#"> Login</a></li>
                    <li id="bienvenidalink"><a href="#"> Bienvenida</a></li>
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
                <span>Personas</span>
                <img src="/images/logo.jpg">
            </div>
            <div>
                <ul class="Menu">
                    <li id="bienvenidalink"><a href="#"> Bienvenida</a></li>
                    <li id="personaslink"><a href="#"> Personas</a></li>
                    <li id="logoutlink"><a href="#"> Logout</a></li>
                </ul>
            </div>
            <div class="user">&nbsp &nbsp ${loginstate.user.id}</div>
        `;
        document.querySelector('#menu').innerHTML = html;
        document.querySelector("#menu #logoutlink").addEventListener('click', logout);
        document.querySelector("#menu #bienvenidalink").addEventListener('click', e => {
            document.location = "/pages/login/view.html";
        });
        document.querySelector("#menu #personaslink").addEventListener('click', e => {
            document.location = "/pages/login/view.html";
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
                            <div class='row'><div class='col-3 text-right'>Id</div><div class='col-6'><input type="text" name="id" id="id" value=""></div></div>
                            <div class='row'><div class='col-3 text-right'>Clave</div><div class='col-6'><input type="password" name="password" id="password" value=""></div></div>
                            <div class='row'>
                                <div class='col-6 text-centered cooper'>
                                    <input id="login" class="boton" type="button" value="Login">
                                    &nbsp
                                    <input id="cancelar" class="boton" type="button" value="Cancelar">
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
}

function ask(event){
    event.preventDefault();
    toggle_loginview();
    document.querySelectorAll('#loginview input').forEach((i) => { i.classList.remove("invalid"); });
    document.querySelector("#loginview #id").value = "";
    document.querySelector("#loginview #password").value = "";
}

function toggle_loginview(){
    document.getElementById("loginoverlay").classList.toggle("active");
    document.getElementById("loginview").classList.toggle("active");
}

async function login(){
    let user = {
        id: document.getElementById("id").value,
        password: document.getElementById("password").value
    };
    let request = new Request(api_login + '/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(user)
    });
    const response = await fetch(request);
    if (!response.ok) {
        errorMessage(response.status);
        return;
    }
    document.location = "/pages/personas/view.html";
}

async function logout(event){
    event.preventDefault();
    let request = new Request(api_login + '/logout', { method: 'POST' });
    const response = await fetch(request);
    if (!response.ok) {
        errorMessage(response.status);
        return;
    }
    document.location = "/pages/bienvenida/view.html";
}

function errorMessage(status) {
    alert("Error: " + status);
}

document.addEventListener('DOMContentLoaded', menu);
