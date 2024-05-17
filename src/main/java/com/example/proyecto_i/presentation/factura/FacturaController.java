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
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@RestController
@RequestMapping("/api/facturas")
@SessionAttributes({"facturas","clienteSearch","proveedor", "productoSearch", "producto"})
public class FacturaController {
    @Autowired
    private Service service;

    @ModelAttribute("facturas")
    public List<Factura> facturas() {
        return new ArrayList<Factura>();
    }

    @ModelAttribute("clienteSearch")
    public Cliente clienteSearch() {
        Cliente cliente = new Cliente();
        cliente.setNombre("...");
        return cliente;
    }

    @ModelAttribute("productoSearch")
    public Producto productoSearch() {
        return new Producto();
    }

  /*
  *   @ModelAttribute("proveedor")
    public Proveedor proveedor(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Optional<Proveedor> prov = service.proveedorRead(usuario.getIdentificacion());
        Proveedor proveedor = new Proveedor();
        if (prov.isPresent()) {
            proveedor = prov.get();
        }
        return proveedor;
    }*/

    @ModelAttribute("detalles")
    public Iterable<Detalle> detalles() {
        return new ArrayList<Detalle>();
    }

    @ModelAttribute("detalle")
    public Detalle detalle() {
        return new Detalle();
    }

    @ModelAttribute("factura")
    public Factura factura() {
        return new Factura();
    }

    @ModelAttribute("producto")
    public Producto producto() {
        return new Producto();
    }

    @GetMapping("/crearFactura")
    public String crearFactura(@ModelAttribute("clienteSearch") Cliente cliente, @ModelAttribute("proveedor") Proveedor proveedor, Model model, HttpSession session, @ModelAttribute("factura") Factura factura) {
        try {

            List<Detalle> detalles = (List<Detalle>) session.getAttribute("facturaDetalles");

            model.addAttribute("detalles", detalles);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("mensaje", "Hubo un error al crear una factura. Por favor, inténtalo de nuevo.");
        }

        return "pages/facturar/crearFactura";
    }



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

    @GetMapping("/facturaProducto")
    public String facturaProducto(Model model, @ModelAttribute("producto") Producto producto, @ModelAttribute("factura") Factura factura, HttpSession session) {
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




    @PostMapping("/facturaCliente")
    public String facturaCliente(Model model, HttpSession session, @RequestParam String identificacion, @ModelAttribute("factura") Factura factura) throws Exception {
        Factura facturaNueva = new Factura();
        Cliente addCliente = service.clientesSearch(identificacion);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Detalle> detalles = (List<Detalle>) session.getAttribute("detalles");
        try{
            Optional<Proveedor> proveedor = service.proveedorRead(usuario.getIdentificacion());
            if(proveedor.isPresent() && addCliente != null){
                facturaNueva.setClienteByCliente(addCliente);
                facturaNueva.setProveedorByProveedor((Proveedor) model.getAttribute("proveedor"));
                model.addAttribute("clienteSearch", addCliente);
                model.addAttribute("factura", facturaNueva);
            }
            return "redirect:/crearFactura";
        }catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("mensaje", "Hubo un error al crear una factura. Por favor, inténtalo de nuevo.");
        }
        return "redirect:/crearFactura";
    }
    @PostMapping ("/deleteDetalle")
    public String deleteDetalle(Model model, @ModelAttribute("factura") Factura f, @ModelAttribute("detalles") List<Detalle> detalles){

        return "redirect:/crearFactura";
    }
    @PostMapping ("/sumaDetalle")
    public String sumaDetalle(Model model, @ModelAttribute("factura") Factura f, @ModelAttribute("detalles") List<Detalle> detalles){

        return "redirect:/crearFactura";
    }
    @PostMapping ("/restaDetalle")
    public String restaDetalle(Model model, @ModelAttribute("factura") Factura f, @ModelAttribute("detalles") List<Detalle> detalles){

        return "redirect:/crearFactura";
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
    }



    //ACTUALIZACIÓN-----------------------

    @GetMapping("/proveedorID")
    public Proveedor obtenerProveedorID(Authentication authentication) {
        String username = authentication.getName();
        return service.proveedorRead(username).orElse(null);
    }
    @GetMapping("/listar-facturas")
    public List<FacturaSimpleDTO> listarFacturasPorProveedor(Authentication authentication) {
        return service.facturasByProveedor(authentication.getName());
    }

}



