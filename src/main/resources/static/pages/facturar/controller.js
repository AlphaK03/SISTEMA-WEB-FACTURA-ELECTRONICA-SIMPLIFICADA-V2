document.addEventListener("DOMContentLoaded", loaded);

var facturaState={
    lista_detalles: [],
    proveedor:{nombre:""},
    cliente : {identificacion:"", nombre:"", telefono:"", correo:""},
    producto:{codigo:""}
}
async function loaded(event) {
    try {
        await render_proveedor();
        render_cliente();
        render_producto();
        render_list();
        load_detalles();
    } catch (error) {
        console.error('Error:', error);
    }
}

//MANEJO DE LA LISTA DE DETALLES
class Detalle{
    constructor (numero, descripcion, cantidad, codigoProducto, precio, monto){
        this.numero = numero;
        this.descripcion=descripcion;
        this.cantidad=cantidad;
        this.codigoProducto=codigoProducto;
        this.precioProducto = precio;
        this.monto = monto;
    }
}
function agregarDetalle(numero, descripcion, cantidad, codigoProducto, precio) {
    const detalle = new Detalle(numero, descripcion, cantidad, codigoProducto, precio, precio);
    console.log("detalle");
    facturaState.lista_detalles.push(detalle);
    console.log("Detalle agregardo correctamente");
}
//MODIFICAR sólo cambia la cantidad por medio del botón
function sumarDetalle(numero) {
    if(facturaState.lista_detalles.length >0){
        facturaState.lista_detalles.forEach(detalle => {
            if ( detalle.numero === numero){
                detalle.cantidad = detalle.cantidad + 1;
                detalle.monto = detalle.precio * detalle.cantidad;
            }
        });
    }else{
        console.error("La lista de detalles está vacía");
    }
}
function restarDetalle(numero) {
    if(facturaState.lista_detalles.length >0){
        facturaState.lista_detalles.forEach(detalle => {
            if ( detalle.numero === numero && detalle.cantidad > 1){
                detalle.cantidad = detalle.cantidad - 1;
                detalle.monto = detalle.precio * detalle.cantidad;
            }
        });
    }else{
        console.error("La lista de detalles está vacía o la cantidad es 1");
    }
}
//ELIMINAR sólo por medio del botón
function eliminarDetalle(numero) {
    const indice = detalles.findIndex(det => det.numero === numero);
    if (indice !== -1) {
        detalles.splice(indice, 1);
    } else {
        console.error(`Detalle con número ${numero} no encontrado.`);
    }
}
//Total de factura
function totalFactura(){
    var total=0;
    if(facturaState.lista_detalles.length === 0) {
        facturaState.lista_detalles.forEach(detalle => {
            total += detalle.monto;
        });
    }
    return total;
}

//Renderización de html
//BUSQUEDA
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
function render_producto(){
    const container_search = document.querySelector('#search-container');
    let productoSearchHTML = `
        <div id = "producto-search" class="section search">
            <h4>Buscar Producto:</h4>
            <td text="HOLA"></td>

            <label text="LABEL"></label><br>
            <form id = "productoForm">
                <input type="text" id = "nombreProducto" name = "nombre" placeholder="Ingrese el nombre del producto">
                <button type="submit">Buscar</button>
            </form>
        </div>
    `;
    container_search.insertAdjacentHTML('beforeend', productoSearchHTML);
    document.querySelector('#productoForm').addEventListener('submit',producto_search);
}

//LISTA DE DETALLES (FACTURA)
function render_list(){
    const lista = document.querySelector('#lista-productos');
    let listaHTML=`
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
            <!-- ForEach de los detalles en el load--->
            <tbody id = "detallesTable">
            </tbody>
        </table>
        <!-- Total (factura), llamar a util -->
        <h4>Total: ... </h4>
        <label id = total></label>
    `;
    lista.insertAdjacentHTML('beforeend',listaHTML);
}

//CARGAR DATOS DESDE EL BACKEND
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
        document.getElementById("identificacion-cliente").ariaPlaceholder = "Ingrese el nombre del cliente";
    }catch(error){
        console.error('Error al buscar cliente:', error);
        document.getElementById('cliente-info').innerText = 'Error al buscar cliente';
    }
}
async function producto_search(event){
    event.preventDefault();
    console.log("ESTOY en ADDProducto");

    const nombreProducto = document.getElementById("nombreProducto").value;
    console.log(nombreProducto);
    const request = new Request(`/api/facturas/searchProducto?nombreProducto=${nombreProducto}`, {
        method: 'GET',
        headers: { }
    });
    try{
        const response = await fetch(request);
        const data = await response.json();
        console.log(data);
        //Data aparece con error..?
        if (data.error) {
            console.error('Error al buscar el producto', data.error);
            document.getElementById('LABEL').innerText = `Error al buscar producto: ${data.error}`;
            return;
        }
        console.log(data);
        agregarDetalle(1,data.nombre, 1,data.codigo, data.precio);
        load_detalles();
    }catch(error){
        console.error('Error al buscar producto:', error);
        document.getElementById('LABEL').innerText = 'Error al buscar producto';
    }
}
function load_detalles(){
    try{
        const detallesTable = document.getElementById('detallesTable');
        detallesTable.innerHTML = ''; // Limpiar la lista anterior
        facturaState.lista_detalles.forEach(detalle=>{
            const row = document .createElement('tr');
            row.innerHTML = `
                <td><!-- Botón de eliminar -->
                    <button id="eliminar"><img src="/images/equis.png" width="20px"/></button>
                </td>
                <td>${detalle.cantidad}</td>
                <td>${detalle.descripcion}</td>
                <td>${detalle.precioProducto}</td>
                <td>${detalle.monto}</td>
                <!-- Botones de suma o resta de la cantidad -->
                <td>
                    <!-- Sumar  -->
                    <button id="sumar"><img src="/images/suma.png" width="20px"/></button>
                    <!-- Restar -->
                    <button id="restar"><img src="/images/resta.png" width="20px"/></button>
                </td>
           `;

            //document.querySelector('#eliminar').addEventListener('click', eliminarDetalle(detalle.numero));
            //document.querySelector('#sumar').addEventListener('click', sumarDetalle(detalle.numero));
            //document.querySelector('#eliminar').addEventListener('click', restarDetalle(detalle.numero));
            detallesTable.appendChild(row);
        });
    }catch(error){
        console.error('Error al cargar clientes:', error);
    }

}

//Implementar un utils para que sume la cantidad de cada detalle
//Si da tiempo implementar el de los botones