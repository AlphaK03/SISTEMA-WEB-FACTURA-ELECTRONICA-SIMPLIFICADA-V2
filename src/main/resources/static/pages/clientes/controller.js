document.addEventListener("DOMContentLoaded", async function () {
    render_client_register();
    await load_client_list();
});

function render_client_register() {
    const container = document.querySelector('#form-container');
    container.innerHTML = '';  // Limpiar contenido previo

    let registerHtml = `
        <h2>Registrar cliente</h2>
        <form id="clientRegisterForm">
            <label for="identificacion">Identificación:</label><br>
            <input type="text" id="identificacion" name="identificacion"><br>
            <label for="nombre">Nombre:</label><br>
            <input type="text" id="nombre" name="nombre"><br>
            <label for="telefono">Teléfono:</label><br>
            <input type="text" id="telefono" name="telefono"><br>
            <label for="correo">Correo electrónico:</label><br>
            <input type="email" id="correo" name="correo"><br>
            <input type="submit" value="Registrarse">
        </form>
        <div id="responseMessage"></div>
    `;
    container.insertAdjacentHTML('beforeend', registerHtml);
    document.querySelector('#clientRegisterForm').addEventListener('submit', client_register);
}

function render_client_list() {
    const container = document.querySelector('#list-container');
    container.innerHTML = '';  // Limpiar contenido previo

    let listHtml = `
        <div class="product-list">
            <h2>Listado de Clientes</h2>
            <table id="client-list">
                <thead>
                    <tr>
                        <th>Identificación</th>
                        <th>Nombre</th>
                        <th>Teléfono</th>
                        <th>Correo</th>
                    </tr>
                </thead>
                <tbody id="clientesTable">
                </tbody>
            </table>
        </div>
    `;
    container.insertAdjacentHTML('beforeend', listHtml);
}

async function load_client_list() {
    render_client_list();  // Renderizar la estructura vacía de la lista

    try {
        const response = await fetch('/api/clientes/listar');
        if (!response.ok) {
            throw new Error('Error al cargar la lista de clientes');
        }
        const data = await response.json();
        const clientesTable = document.getElementById('clientesTable');
        clientesTable.innerHTML = ''; // Limpiar contenido previo

        data.forEach(cliente => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${cliente.identificacion}</td>
                <td>${cliente.nombre}</td>
                <td>${cliente.telefono}</td>
                <td>${cliente.correo}</td>
            `;
            clientesTable.appendChild(row);
        });
    } catch (error) {
        console.error('Error al cargar clientes:', error);
    }
}

async function client_register(event) {
    event.preventDefault();
    let cliente = {
        identificacion: document.getElementById("identificacion").value,
        nombre: document.getElementById("nombre").value,
        telefono: document.getElementById("telefono").value,
        correo: document.getElementById("correo").value
    };
    let request = new Request('/api/clientes/crearCliente', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(cliente)
    });

    try {
        const response = await fetch(request);
        const data = await response.text(); // Ahora que devolvemos texto
        if (response.ok) {
            document.getElementById('responseMessage').innerText = 'Registro exitoso';
            await load_client_list(); // Actualiza la lista después de registrar
        } else {
            document.getElementById('responseMessage').innerText = 'Error en el registro: ' + data;
        }
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('responseMessage').innerText = 'Error en el registro';
    }
}
