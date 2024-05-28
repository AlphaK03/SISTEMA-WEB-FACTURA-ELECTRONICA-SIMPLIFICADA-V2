document.addEventListener("DOMContentLoaded", loaded);

var facturaState = {
    proveedor: { nombre: "", identificacion: "" },
    cliente: { identificacion: "", nombre: "", telefono: "", correo: "" },
    factura: {
        fecha: formatoFecha(),
        proveedorByProveedor: { identificacion: "" },
        clienteByCliente: { identificacion: "" },
        detallesByNumero: []
    }
};

function formatoFecha() {
    const fechaActual = new Date();
    const dia = String(fechaActual.getDate()).padStart(2, '0');
    const mes = String(fechaActual.getMonth() + 1).padStart(2, '0'); // Los meses en JavaScript van de 0 a 11
    const año = fechaActual.getFullYear();

    const soloFecha = `${dia}/${mes}/${año}`;
    console.log(soloFecha);
    return soloFecha
}

async function loaded(event) {
    try {
        await render_proveedor();
        render_cliente();
        render_producto();
        render_list();
    } catch (error) {
        console.error('Error:', error);
    }
}

//MANEJO DE LA LISTA DE DETALLES
class Detalle {
    constructor(numero, descripcion, cantidad, producto, precio, monto) {
        this.numero = numero;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.productoByCodigoproducto = producto; // Aquí se asigna el objeto completo de Producto
        this.precioProducto = precio;
        this.monto = monto;
    }
}

//CRUD DE LA LISTA DE DETALLES
function agregarDetalle(numero, descripcion, cantidad, producto, precio) {
    const detalle = new Detalle(numero, descripcion, cantidad, producto, precio, cantidad * precio);

    // Agregar el detalle a la lista de detalles de la factura
    facturaState.factura.detallesByNumero.push(detalle);

    console.log("Detalle agregado correctamente");
    load_detalles();
}

function sumarDetalle(numero) {
    facturaState.factura.detallesByNumero.forEach(detalle => {
        if (detalle.numero === numero) {
            detalle.cantidad += 1;
            detalle.monto = detalle.precioProducto * detalle.cantidad;
        }
    });
    load_detalles(); // Recargar la lista después de modificar un detalle
}

function restarDetalle(numero) {
    facturaState.factura.detallesByNumero.forEach(detalle => {
        if (detalle.numero === numero && detalle.cantidad > 1) {
            detalle.cantidad -= 1;
            detalle.monto = detalle.precioProducto * detalle.cantidad;
        }
    });
    load_detalles(); // Recargar la lista después de modificar un detalle
}

function eliminarDetalle(numero) {
    const indice = facturaState.factura.detallesByNumero.findIndex(det => det.numero === numero);
    if (indice !== -1) {
        facturaState.factura.detallesByNumero.splice(indice, 1);
        console.log(`Detalle con número ${numero} eliminado.`);
    } else {
        console.error(`Detalle con número ${numero} no encontrado.`);
    }
    load_detalles(); // Recargar la lista después de eliminar un detalle
}

function totalFactura() {
    return facturaState.factura.detallesByNumero.reduce((total, detalle) => total + detalle.monto, 0);
}

//------------------------BUSQUEDA PROVEEDOR
async function render_proveedor() {
    const data = await load_proveedor();
    if (data) {
        facturaState.proveedor = data;
        const container_search = document.querySelector('#search-container');
        let proveedorNombre = `
            <div class="section">
                <h4>Proveedor:</h4>
                <label id="nombreProveedor">${facturaState.proveedor.nombre}</label>
            </div>
        `;
        container_search.insertAdjacentHTML('beforeend', proveedorNombre);
    }
}

async function load_proveedor(event) {
    try {
        const response = await fetch('/api/facturas/proveedorID');
        if (!response.ok) {
            throw new Error('Error al obtener el proveedor');
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error al cargar proveedor:', error);
    }
}

//------------------------BUSQUEDA CLIENTE
function render_cliente() {
    const container_search = document.querySelector('#search-container');
    let clienteSearchHTML = `
        <div id="cliente-search" class="section search">
            <h4>Buscar Cliente:</h4>
            <div id ="info-cliente">
                <h4>Cliente:</h4>
                <label id="nombre-cliente"></label>
            </div>
            <form id = "clienteForm">
                <input type="text" id="identificacion-cliente" name="identificacion" placeholder="Ingrese el nombre del cliente">
                <button type="submit">Buscar</button>
            </form>
        </div>
    `;
    container_search.insertAdjacentHTML('beforeend', clienteSearchHTML);
    document.querySelector('#clienteForm').addEventListener('submit', cliente_search);
}

function render_producto() {
    const container_search = document.querySelector('#search-container');
    let productoSearchHTML = `
        <div id = "producto-search" class="section search">
            <h4>Buscar Producto:</h4>

            <form id = "productoForm">
                <input type="text" id = "nombreProducto" name = "nombre" placeholder="Ingrese el nombre del producto">
                <button type="submit">Buscar</button>
            </form>
        </div>
    `;
    container_search.insertAdjacentHTML('beforeend', productoSearchHTML);
    document.querySelector('#productoForm').addEventListener('submit', producto_search);
}

async function cliente_search(event) {
    event.preventDefault();
    console.log("ESTOY en clienteSearch");

    const id_cliente = document.getElementById("identificacion-cliente").value;
    const request = new Request(`/api/facturas/searchCliente?identificacionCliente=${id_cliente}`, {
        method: 'GET',
        headers: {}
    });
    try {
        const response = await fetch(request);
        const data = await response.json();

        if (data.error) {
            console.error('Error al buscar el cliente', data.error);
            document.getElementById('nombre-cliente').innerText = `Error al buscar cliente: ${data.error}`;
            return;
        }
        console.log(data);
        facturaState.cliente = data;
        facturaState.factura.idCliente = data.identificacion;
        //Mostrar la información del cliente en el HTML
        document.getElementById('nombre-cliente').innerText = facturaState.cliente.nombre;
    } catch (error) {
        console.error('Error al buscar cliente:', error);
        document.getElementById('cliente-info').innerText = 'Error al buscar cliente';
    }
}

//----------------------------BUSQUEDA PRODUCTO
async function producto_search(event) {
    event.preventDefault();
    console.log("ESTOY en ADDProducto");

    const nombreProducto = document.getElementById("nombreProducto").value;
    console.log(nombreProducto);
    const request = new Request(`/api/facturas/searchProducto?nombreProducto=${nombreProducto}`, {
        method: 'GET',
        headers: {}
    });
    try {
        const response = await fetch(request);
        const producto = await response.json();
        console.log(producto);
        if (producto.error) {
            console.error('Error al buscar el producto', producto.error);
            return;
        }

        // Agregar el detalle con el objeto completo de Producto
        agregarDetalle(facturaState.factura.detallesByNumero.length + 1, producto.nombre, 1, producto, producto.precio);
    } catch (error) {
        console.error('Error al buscar producto:', error);
    }
}





//----------------------------LISTA DE DETALLES (FACTURA)
function render_list() {
    const lista = document.querySelector('#lista-productos');
    let listaHTML = `
        <h2>Lista de Detalles</h2>
        <table>
        <thead>
            <tr>
                <th>Eliminar</th>
                <th>Cantidad</th>
                <th>Descripción</th>
                <th>Precio</th>
                <th>Monto</th>
                <th>Botones</th>
            </tr>
            </thead>
            <tbody id="detallesTable">
            </tbody>
        </table>
        <h4>Total: <span id="total"></span></h4>
        <button id = "crear-factura">Crear Factura</button>
    `;
    lista.innerHTML = listaHTML;
    document.querySelector('#crear-factura').addEventListener('click', add_factura);
    load_detalles(); // Cargar detalles
}

function load_detalles() {
    try {
        const detallesTable = document.getElementById('detallesTable');
        detallesTable.innerHTML = ''; // Limpiar la lista anterior
        facturaState.factura.detallesByNumero.forEach(detalle => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td><button class="eliminar" data-numero="${detalle.numero}"><img src="/images/equis.png" width="20px"/></button></td>
                <td>${detalle.cantidad}</td>
                <td>${detalle.descripcion}</td>
                <td>${detalle.precioProducto}</td>
                <td>${detalle.monto}</td>
                <td>
                    <button class="sumar" data-numero="${detalle.numero}"><img src="/images/suma.png" width="20px"/></button>
                    <button class="restar" data-numero="${detalle.numero}"><img src="/images/resta.png" width="20px"/></button>
                </td>
            `;
            detallesTable.appendChild(row);
        });

        // Agregar event listeners para los botones
        document.querySelectorAll('.eliminar').forEach(button => {
            button.addEventListener('click', (e) => {
                const numero = parseInt(e.target.closest('button').dataset.numero);
                eliminarDetalle(numero);
            });
        });

        document.querySelectorAll('.sumar').forEach(button => {
            button.addEventListener('click', (e) => {
                const numero = parseInt(e.target.closest('button').dataset.numero);
                sumarDetalle(numero);
            });
        });

        document.querySelectorAll('.restar').forEach(button => {
            button.addEventListener('click', (e) => {
                const numero = parseInt(e.target.closest('button').dataset.numero);
                restarDetalle(numero);
            });
        });

        document.getElementById('total').innerText = totalFactura(); // Actualizar el total
    } catch (error) {
        console.error('Error al cargar detalles:', error);
    }
}

//----------------------------AGREGAR FACTURA
function validar() {
    var error = false;
    document.querySelectorAll('input').forEach((i) => {
        i.classList.remove("invalid");
    });

    if (facturaState.cliente.identificacion.length === 0) {
        document.querySelector("#identificacion-cliente").classList.add("invalid");
        console.log("Error: No hay cliente asociado");
        error = true;
    }

    if (facturaState.factura.detallesByNumero.length === 0) {
        //Agregar un campo en donde se le diga que no hay productos agregados
        console.log("Error: Lista de productos vacía");
        error = true;
    }
    console.log("Validando: ", error);
    return !error;
}
async function add_factura(event) {
    event.preventDefault();

    // Suponiendo que tienes los datos del proveedor y del cliente correctamente asignados
    facturaState.factura.proveedorByProveedor = { identificacion: facturaState.proveedor.identificacion };
    facturaState.factura.clienteByCliente = { identificacion: facturaState.cliente.identificacion };

    console.log("Enviando la factura:");
    console.log(JSON.stringify(facturaState.factura, null, 2)); // Pretty-print JSON

    let facturaRequest = new Request('/api/facturas/crearFactura', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(facturaState.factura)
    });

    try {
        const facturaResponse = await fetch(facturaRequest);
        const facturaData = await facturaResponse.json();
        console.log(facturaData);
        if (facturaResponse.ok) {
            // Obtener el número de factura generado
            const numeroFactura = facturaData.numero;

            // Agregar el número de factura a cada detalle
            facturaState.factura.detallesByNumero.forEach(detalle => {
                detalle.numerofactura = numeroFactura;
            });

            // Enviar los detalles de la factura
            for (const detalle of facturaState.factura.detallesByNumero) {
                let detalleRequest = new Request('/api/detalles/crearDetalle', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(detalle)
                });
                const detalleResponse = await fetch(detalleRequest);
                const detalleData = await detalleResponse.json();
                if (!detalleResponse.ok) {
                    console.error('Error en el detalle:', detalleData);
                }
            }
            document.getElementById('responseMessage').innerText = 'Se ha agregado correctamente la factura y los detalles';
        } else {
            document.getElementById('responseMessage').innerText = 'Error en la factura: ' + facturaData.message;
        }
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('responseMessage').innerText = 'Error al ingresar la factura';
    }
}


//----------------------------AGREGAR DETALLES

function addDetalle(numero, descripcion, cantidad, codigoProducto, precio) {
    const detalle = new Detalle(numero, descripcion, cantidad, codigoProducto, precio, precio * cantidad);
    let detalles = JSON.parse(localStorage.getItem('facturaDetalles')) || [];
    detalles.push(detalle);
    localStorage.setItem('facturaDetalles', JSON.stringify(detalles));
    facturaState.factura.detallesByNumero = detalles; // Actualizar facturaState
    load_detalles();
}
