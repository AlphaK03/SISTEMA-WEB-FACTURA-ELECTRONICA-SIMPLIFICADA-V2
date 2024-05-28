document.addEventListener("DOMContentLoaded", loaded);

async function loaded(event) {
    try {
        load_facturas();
        render_list();
    } catch (error) {
        console.error('Error:', error);
    }
}

var state = {
    current: { fecha: new Date(), numero: "", idCliente: "" },
    listaFacturas: []
};

async function load_facturas() {
    console.log("cargando las facturas");
    try {
        await search_lista();
        const facturasTable = document.getElementById('facturas-body');
        facturasTable.innerHTML = ''; // Limpiar la lista anterior
        state.listaFacturas.forEach(factura => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${factura.fecha}</td>
                <td>${factura.numero}</td>
                <td>${factura.clienteIdentificacion}</td>
                <td>
                    <button class="pdf" data-numero="${factura.numero}">PDF</button>
                    <button class="xml" data-numero="${factura.numero}">XML</button>
                </td>
            `;
            facturasTable.appendChild(row);
        });

        // Agregar event listeners para los botones
        console.log("Creando botones");
        document.querySelectorAll('.pdf').forEach(button => {
            button.addEventListener('click', (e) => {
                const numero = e.target.closest('button').dataset.numero;
                generarPDF(numero);
            });
        });

        document.querySelectorAll('.xml').forEach(button => {
            button.addEventListener('click', (e) => {
                const numero = e.target.closest('button').dataset.numero;
                render_xml(numero);
            });
        });
    } catch (error) {
        console.error('Error al cargar facturas:', error);
    }
}

async function search_lista() {
    console.log("ESTOY en search listas de facturas");

    const request = new Request('/api/facturas/listar-facturas', {
        method: 'GET',
        headers: {}
    });
    try {
        const response = await fetch(request);
        if (!response.ok) {
            console.error(response.error)
        }
        state.listaFacturas = await response.json();
        console.log('Facturas obtenidas:', state.listaFacturas);
    } catch (error) {
        console.error('Error al buscar producto:', error);
    }
}

function render_list() {
    const lista_facturas = document.querySelector('#lista-facturas');
    let lista_facturas_HTML = `
        <table class="invoice-table">
        <thead>
        <tr>
            <th>Fecha</th>
            <th>Número</th>
            <th>Cliente</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody id="facturas-body">
        <!-- Aquí se insertarán las filas de las facturas -->
        </tbody>
        </table>
    `;
    lista_facturas.innerHTML = lista_facturas_HTML;
}


function findFacturaByIndex(index) {
    if (index > 0){
        return state.listaFacturas[index-1];
    }

}

function render_xml(index) {
    const factura = findFacturaByIndex(index);
    if (!factura) {
        console.error('Factura no encontrada:', index);
        return;
    }

    const contenido = `
    <FacturaElectronica>
        <Clave>24560683106815061216505160306</Clave>
        <CodigoActividad>4654684</CodigoActividad>
        <NumeroConsecutivo>43131664414</NumeroConsecutivo>
        <FechaEmision>${factura.fecha}</FechaEmision>
        <Emisor>
            <Identificacion>
                <Tipo>01</Tipo>
                <Numero>${factura.proveedorIdentificacion}</Numero>
            </Identificacion>
            <NombreComercial>${factura.clienteIdentificacion}</NombreComercial>
        </Emisor>
        <Receptor>
            <!-- Información del receptor -->
        </Receptor>
        <Detalle>
            <!-- Detalle de la factura -->
        </Detalle>
        <ResumenFactura>
            <!-- Resumen de la factura -->
        </ResumenFactura>
    </FacturaElectronica>
    `;

    const blob = new Blob([contenido], { type: 'text/xml' });
    const url = URL.createObjectURL(blob);
    window.open(url, '_blank');
}

function generarPDF(index) {
    const factura = findFacturaByIndex(index);
    if (!factura) {
        console.error('Factura no encontrada:', index);
        return;
    }

    // Construir la URL con el parámetro de la factura
    const url = `/api/facturas/pdf?numero=${encodeURIComponent(factura.numero)}`;

    // Abrir la URL en una nueva pestaña
    window.open(url, '_blank');
}

