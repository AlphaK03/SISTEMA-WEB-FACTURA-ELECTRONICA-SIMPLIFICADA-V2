package com.example.proyecto_i.presentation.factura;

import com.example.proyecto_i.logic.*;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {
    @Autowired
    private Service service;

    @PostMapping("/crearFactura")
    public ResponseEntity<Map<String, Object>> crearFactura(@RequestBody Factura factura, Authentication authentication, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("Factura recibida: " + factura);
            System.out.println("Detalles recibidos: " + factura.getDetallesByNumero());

            factura.setClienteByCliente(service.clientesSearch(factura.getClienteByCliente().getIdentificacion()));
            factura.setProveedorByProveedor(factura.getClienteByCliente().getProveedorByProveedor());
            List<Detalle> lista = (List<Detalle>) factura.getDetallesByNumero();
            factura.setDetallesByNumero(null);
            // Guarda la factura en la base de datos
            service.facturasCreate(factura);
            // Aquí podrías imprimir los detalles individuales para verificar que están siendo deserializados correctamente
            lista.forEach(detalle -> {
                detalle.setFacturaByNumerofactura(service.facturasGetAll().getFirst());
                detalle.getProductoByCodigoproducto().setProveedorByProveedor(factura.getProveedorByProveedor());

                try {
                    service.detalleCreate(detalle);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });





            response.put("message", "Factura creada exitosamente");
            response.put("numero", factura.getNumero());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error al crear la factura: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

/*
    @GetMapping("/presentation/facturar/showFacturar")
    public String search(Model model, HttpSession session, @ModelAttribute("proveedor") Proveedor proveedor, @ModelAttribute("facturas") List<Factura> facturas) {
        try {
            facturas = service.facturasGetAll();
            List<Factura> listaFiltrada = new ArrayList<>();

            for(Factura factura : facturas){
                if(Objects.equals(factura.getProveedorByProveedor().getIdentificacion(), proveedor.getIdentificacion())){
                    listaFiltrada.add(factura);
                }
            }

            model.addAttribute("facturas", listaFiltrada);
            return "/presentation/facturar/showFacturar";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("mensaje", "Hubo un error al crear una factura. Por favor, inténtalo de nuevo.");
        }
        return "/pages/facturar/showFacturar";
    }


    @GetMapping("/presentation/facturar/pdf")
    public void pdf(Factura facturanumero, HttpServletResponse response) throws Exception {
        Factura factura = service.facturasSearchById(facturanumero.getNumero());

        PdfWriter writer = new PdfWriter(response.getOutputStream());

        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        Paragraph title = new Paragraph("Factura");
        Paragraph content = new Paragraph("Número de factura: " + factura.getNumero() + "\n" +
                "Fecha: " + factura.getFecha() + "\n" +
                "Cliente: " + factura.getClienteByCliente().getNombre());


        document.add(title);
        document.add(content);

        document.close();
    }

    @GetMapping("/presentation/facturar/xml")
    public void xml(Factura facturaNumero, HttpServletResponse response)throws Exception{

    }

    @GetMapping("agregarDetalle")
    public String agregarDetalle(HttpSession session, Model model, @ModelAttribute("detalle") Detalle detalle, @ModelAttribute("detalles") Iterable<Detalle> detalles, @ModelAttribute("productoSearch") Producto producto, @ModelAttribute("factura") Factura factura) {
        Detalle nuevo = new Detalle();
        nuevo.setCantidad(1);
        nuevo.setProductoByCodigoproducto(productoSearch());
        nuevo.setDescripcion(productoSearch().getNombre());
        nuevo.setFacturaByNumerofactura(factura);
        return "redirect:/crearFactura";
    }


    @PostMapping("/facturaProducto")
    public String facturaProducto(Model model, HttpSession session, @RequestParam String codigo, @ModelAttribute("producto") Producto producto, @ModelAttribute("clienteSearch") Cliente cliente, @ModelAttribute("proveedor") Proveedor proveedor, @ModelAttribute("factura") Factura factura) throws Exception {
        Optional<Producto> addProducto = service.productosSearch(proveedor, codigo);

        if (addProducto.isPresent()) {
            Detalle detalle = new Detalle();
            detalle.setProductoByCodigoproducto(addProducto.get());
            detalle.setDescripcion(addProducto.get().getNombre());
            detalle.setCantidad(1);

            List<Detalle> detalles = (List<Detalle>) model.getAttribute("detalles");
            detalles.add(detalle); // Agregamos el nuevo detalle a la lista existente en el modelo

            // Verificar si ya existe el atributo en la sesión
            List<Detalle> detallesEnSesion = (List<Detalle>) session.getAttribute("facturaDetalles");
            if (detallesEnSesion != null) {

                boolean exist = false;
                for (Detalle det : detallesEnSesion){
                    if (Objects.equals(det.getDescripcion(), detalle.getDescripcion())) {
                        exist = true;
                        detalle.setCantidad(det.getCantidad());
                        break;
                    }

                }
                if(exist){
                    detallesEnSesion.remove(detalle);
                    detalle.setCantidad(detalle.getCantidad() + 1);
                }
                detallesEnSesion.add(detalle);
            } else {
                detallesEnSesion = new ArrayList<>();
                detallesEnSesion.add(detalle);
            }
            session.setAttribute("facturaDetalles", detallesEnSesion);

            factura.setClienteByCliente(cliente);
            factura.setDetallesByNumero(detalles);
            model.addAttribute("detalles", detalles);
            return "redirect:/crearFactura";
        } else {
            model.addAttribute("mensaje", "Producto no encontrado");
            return "redirect:/crearFactura";
        }
    }

    @PostMapping("/facturaFinal")
    public String crearFactura() {
        return "redirect:/creado";
    }

    @GetMapping("/creado")
    public String crearFacturaGet(Model model, HttpSession session) {
        List<Detalle> detalles = (List<Detalle>) session.getAttribute("facturaDetalles");
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Optional<Proveedor> proveedor = service.proveedorRead(usuario.getIdentificacion());
        Cliente cliente = (Cliente) model.getAttribute("clienteSearch");

        try {
            Factura factura = new Factura();
            if (proveedor.isPresent()){
                factura.setProveedorByProveedor(proveedor.get());
            }

            LocalDateTime fechaActual = LocalDateTime.now();
            String fechaFormateada = fechaActual.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            factura.setFecha(fechaFormateada);
            factura.setClienteByCliente(cliente);
           // factura.setDetallesByNumero(detalles);

            service.facturasCreate(factura);
            for(Factura factura1 : service.facturasGetAll()){
                if(Objects.equals(factura1.getFecha(), factura.getFecha())){
                    factura = factura1;
                    break;
                }
            }

            for(Detalle detalle: detalles){
                detalle.setFacturaByNumerofactura(factura);
                service.detalleCreate(detalle);
            }


            session.removeAttribute("facturaDetalles");

            return "/pages/otros/registroExitoso";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("mensaje", "Hubo un error al crear una factura. Por favor, inténtalo de nuevo.");
            return "pages/error";
        }
    }*/



    //ACTUALIZACIÓN-----------------------

    @GetMapping("/proveedorID")
    public Proveedor obtenerProveedor(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user authenticated");
        }
        try {
            String username = authentication.getName();
            Optional<Proveedor> proveedorOpt = service.proveedorRead(username);
            if (proveedorOpt.isPresent()) {
                return proveedorOpt.get();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado");
            }
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado");
        }
    }
    @GetMapping("/listar-facturas")
    public List<FacturaSimpleDTO> listarFacturasPorProveedor(Authentication authentication) {
        return service.facturasByProveedor(authentication.getName());
    }
    @GetMapping("/listar")
    public List<FacturaSimpleDTO> listarFacturas(Authentication authentication) throws Exception {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user authenticated");
        }
        try {
            String username = authentication.getName();
            Optional<Proveedor> proveedorOpt = service.proveedorRead(username);
            if (proveedorOpt.isPresent()) {
                Proveedor proveedor = proveedorOpt.get();
                return service.facturasByProveedor(proveedor.getIdentificacion());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado");
            }
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado");
        }
    }
    @GetMapping("/searchCliente")
    public Optional<Cliente> searchCliente(@RequestParam String identificacionCliente, Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user authenticated");
        }
        try {
            String username = authentication.getName();
            Optional<Proveedor> proveedorOpt = service.proveedorRead(username);
            if (proveedorOpt.isPresent()) {
                Proveedor proveedor = proveedorOpt.get();
                Cliente cliente = service.findClienteByProveedor(proveedor.getIdentificacion(), identificacionCliente);
                if (cliente != null) {
                    return Optional.of(cliente);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        }
    }
    @GetMapping("/pdf")
    public void pdf(@RequestParam String numero, HttpServletResponse response) throws Exception {
        Factura factura = service.facturasSearchById(Integer.parseInt(numero));

        // Establecer el tipo de contenido de la respuesta
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=factura.pdf");

        // Crear el PDF y escribirlo en el output stream de la respuesta
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        // Crear contenido del PDF
        Paragraph title = new Paragraph("Factura").setBold().setFontSize(20);
        Paragraph content = new Paragraph("Número de factura: " + factura.getNumero() + "\n" +
                "Fecha: " + factura.getFecha() + "\n" +
                "Cliente: " + factura.getClienteByCliente().getNombre());

        // Agregar contenido al documento
        document.add(title);
        document.add(content);

        // Cerrar el documento
        document.close();
    }

    @GetMapping("/searchProducto")
    public Optional<Producto> agregarProducto(@RequestParam String nombreProducto, Authentication authentication){
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user authenticated");
        }
        try {
            String username = authentication.getName();
            Optional<Proveedor> proveedorOpt = service.proveedorRead(username);
            if (proveedorOpt.isPresent()) {
                Proveedor proveedor = proveedorOpt.get();
                Producto producto = service.productoByProveedor(proveedor.getIdentificacion(),nombreProducto);
                if (producto != null) {
                    return Optional.of(producto);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
        }
    }

}



