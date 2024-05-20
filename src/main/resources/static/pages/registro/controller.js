document.addEventListener("DOMContentLoaded", loaded);

async function loaded(event) {
    try {
        render_register();
    } catch (error) {
        console.error('Error:', error);
    }
}

function render_register() {
    let html = `
        <h2>Registrarse</h2>
        <form id="registerForm">
            <label for="identificacion">Identificación:</label><br>
            <input type="text" id="identificacion" name="identificacion"><br>
            <label for="nombre">Nombre:</label><br>
            <input type="text" id="nombre" name="nombre"><br>
            <label for="telefono">Teléfono:</label><br>
            <input type="text" id="telefono" name="telefono"><br>
            <label for="correo">Correo electrónico:</label><br>
            <input type="email" id="correo" name="correo"><br>
            <label for="contrasena">Contraseña:</label><br>
            <input type="password" id="contrasena" name="contrasena"><br><br>
            <input type="submit" value="Registrarse">
        </form>
        <div id="responseMessage"></div>
    `;
    document.querySelector('#register-container').innerHTML = html;

    document.querySelector('#registerForm').addEventListener('submit', register);
}

async function register(event) {
    event.preventDefault();

    let usuario = {
        identificacion: document.getElementById("identificacion").value,
        nombre: document.getElementById("nombre").value,
        telefono: document.getElementById("telefono").value,
        correo: document.getElementById("correo").value,
        contrasena: document.getElementById("contrasena").value
    };

    let request = new Request('/api/registro/registro', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(usuario)
    });

    try {
        const response = await fetch(request);
        const data = await response.json();

        if (response.ok && data.success) {
            document.getElementById('responseMessage').innerText = 'Registro exitoso';
            document.location = "/pages/registro/registroExitoso.html"; // Redirigir a la página de éxito
        } else {
            document.getElementById('responseMessage').innerText = 'Error en el registro: ' + (data.message || 'Desconocido');
        }
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('responseMessage').innerText = 'Error en el registro';
    }
}

