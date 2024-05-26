document.addEventListener("DOMContentLoaded", loaded);

var facturaState={
    lista_detalles: new Array(),
    proveedor:{nombre:""},
    cliente : {identificacion:"", nombre:"", telefono:"", correo:""},
    producto:{codigo:""}
}

async function loaded(event) {
    try {
        await render_proveedor();
        render_cliente();
        producto_search();
    } catch (error) {
        console.error('Error:', error);
    }
}

function producto_search(){
    const container_search = document.querySelector('#search-container');
    let productoSearchHTML = `
        <div id = "producto-search" class="section search">
            <h4>Buscar Producto:</h4>
            <td text="${factura.setDetallesByNumero(detalles)}"></td>

            <label text="${producto.getNombre()}"></label><br>
            <form id = "productoForm" action="/facturaProducto" method="post">
                <input type="text" id = "codigo" name = "codigo" placeholder="Ingrese el nombre del producto">
                <button type="submit">Buscar</button>
            </form>
        </div>
    `;
    container_search.insertAdjacentHTML('beforeend', productoSearchHTML);
    document.querySelector('#productoForm').addEventListener('submit', add_producto());

}
async function render_proveedor() {
    const data = await load_proveedor();
    if (data) {
        facturaState.proveedor = data;
        const container_search = document.querySelector('#search-container');
        container_search.innerHTML = '';
        let proveedorNombre = `
            <div class="section">
                <h4>Proveedor:</h4>
                <label id="nombreProveedor">${facturaState.proveedor.nombre}</label>
            </div>
        `;
        container_search.insertAdjacentHTML('beforeend', proveedorNombre);
    }
}
function render_cliente(){
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
function render_factura(){



}
async function cliente_search(event){
    event.preventDefault();
    console.log("ESTOY en clienteSearch");

    const id_cliente = document.getElementById("identificacion-cliente").value;
    const request = new Request(`/api/facturas/searchCliente?identificacionCliente=${id_cliente}`, {
        method: 'GET',
        headers: { }
    });
    try{
        const response = await fetch(request);
        const data = await response.json();

        if (data.error) {
            console.error('Error al buscar el cliente', data.error);
            document.getElementById('nombre-cliente').innerText = `Error al buscar cliente: ${data.error}`;
            return;
        }
        console.log(data);
        facturaState.cliente=data;
        //Mostrar la información del cliente en el HTML
        document.getElementById('nombre-cliente').innerText=facturaState.cliente.nombre;
    }catch(error){
        console.error('Error al buscar cliente:', error);
        document.getElementById('cliente-info').innerText = 'Error al buscar cliente';
    }
}
async function load_proveedor(event){
    try {
        const response = await fetch('/api/facturas/proveedorID');
        if(!response.ok){
            throw new Error('Error al obtener el proveedor');
        }
        const data = await response.json();
        return data;
    } catch(error) {
        console.error('Error al cargar proveedor:', error);
    }
}

async function add_producto(event){
    
}

