document.addEventListener("DOMContentLoaded", loaded);

async function loaded(event) {
    try {
        await cargarPerfil();
        document.getElementById('perfil-form').addEventListener('submit', editarPerfil);
    } catch (error) {
        console.error('Error:', error);
    }
}

async function cargarPerfil() {
    try {
        const response = await fetch('/api/user/perfil');
        if (response.ok) {
            const proveedor = await response.json();
            document.getElementById('identificacion').value = proveedor.identificacion;
            document.getElementById('nombre').value = proveedor.nombre;
            document.getElementById('telefono').value = proveedor.telefono;
            document.getElementById('correo').value = proveedor.correo;
        } else {
            console.error('Error al cargar perfil:', response.statusText);
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

async function editarPerfil(event) {
    event.preventDefault(); // Evitar la recarga de la página

    const proveedor = {
        identificacion: document.getElementById('identificacion').value,
        nombre: document.getElementById('nombre').value,
        telefono: document.getElementById('telefono').value,
        correo: document.getElementById('correo').value
    };

    try {
        const response = await fetch('/api/user/editarPerfil', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(proveedor)
        });

        const data = await response.json();
        if (response.ok) {
            document.getElementById('responseMessage').innerText = 'Perfil actualizado correctamente';
        } else {
            document.getElementById('responseMessage').innerText = 'Error al actualizar perfil: ' + (data.message || response.statusText);
        }
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('responseMessage').innerText = 'Error al actualizar perfil';
    }
}
