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
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Controller("facturas")
@SessionAttributes({"facturas","clienteSearch","proveedor", "productoSearch", "producto"})
public class Controller {
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

    @ModelAttribute("proveedor")
    public Proveedor proveedor(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Optional<Proveedor> prov = service.proveedorRead(usuario.getIdentificacion());
        Proveedor proveedor = new Proveedor();
        if (prov.isPresent()) {
            proveedor = prov.get();
        }
        return proveedor;
    }

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
        /*Usuario usuario = (Usuario) session.getAttribute("usuario");
        proveedor = service.proveedorRead(usuario.getIdentificacion());
        model.addAttribute("proveedor", proveedor);*/
            //cliente = model.getAttribute("clienteFactura")

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("mensaje", "Hubo un error al crear una factura. Por favor, inténtalo de nuevo.");
        }

        return "presentation/facturar/crearFactura";
    }


    // Elimina esta anotación duplicada
    //@GetMapping("/presentation/facturar/crearFactura")
    //public String saveFactura(Proveedor proveedor, Model model){
    //    return "presentation/facturar/crearFactura";
    //}

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
        return "/presentation/facturar/showFacturar";
    }


    @GetMapping("/presentation/facturar/pdf")
    public void pdf(Factura facturanumero, HttpServletResponse response) throws Exception {
        // Obtener la factura según el número proporcionado
        Factura factura = service.facturasSearchById(facturanumero.getNumero());

        // Inicializar el PdfWriter con el flujo de salida del HttpServletResponse
        PdfWriter writer = new PdfWriter(response.getOutputStream());

        // Inicializar el PdfDocument y el Document
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        // Agregar contenido al documento (por ejemplo, un título y texto)
        Paragraph title = new Paragraph("Factura");
        Paragraph content = new Paragraph("Número de factura: " + factura.getNumero() + "\n" +
                "Fecha: " + factura.getFecha() + "\n" +
                "Cliente: " + factura.getClienteByCliente().getNombre());

        // Agregar el contenido al documento
        document.add(title);
        document.add(content);

        // Cerrar el documento al finalizar
        document.close();
    }

    @GetMapping("/presentation/facturar/xml")
    public void xml(Factura facturaNumero, HttpServletResponse response)throws Exception{
        Factura factura = service.facturasSearchById(facturaNumero.getNumero());
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(new AnnotationConfigApplicationContext());
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".xml");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setTemplateMode(TemplateMode.XML);
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        Context ctx = new Context();
        ctx.setVariable("factura", factura);
        String xml = engine.process("presentation/facturar/xmlView", ctx);
        response.setContentType("application/xml");
        PrintWriter writer = response.getWriter();
        writer.print(xml);
        writer.close();
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
                // Si ya hay detalles en la sesión, agregamos el nuevo detalle a la lista existente
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
                // Si no hay detalles en la sesión, creamos una nueva lista y agregamos el nuevo detalle
                detallesEnSesion = new ArrayList<>();
                detallesEnSesion.add(detalle);
            }
            // Establecemos la lista actualizada en la sesión
            session.setAttribute("facturaDetalles", detallesEnSesion);

            factura.setClienteByCliente(cliente); // Asignamos el cliente a la factura
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
        // Aquí irá la lógica para crear la factura
        return "redirect:/creado"; // Redirige a una página de éxito después de crear la factura
    }

    @GetMapping("/creado")
    public String crearFacturaGet(Model model, HttpSession session) {
        // Obtener los detalles de la factura de la sesión
        List<Detalle> detalles = (List<Detalle>) session.getAttribute("facturaDetalles");
        // Asumiendo que tienes una clase Usuario y guardas el usuario en la sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Optional<Proveedor> proveedor = service.proveedorRead(usuario.getIdentificacion());
        Cliente cliente = (Cliente) model.getAttribute("clienteSearch");

        try {
            Factura factura = new Factura();
            if (proveedor.isPresent()){
                factura.setProveedorByProveedor(proveedor.get());
            }

            // Obtener la fecha del sistema
            LocalDate fechaActual = LocalDate.now();
            // Convertir la fecha a un formato de cadena
            String fechaFormateada = fechaActual.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // Establecer la fecha en la factura
            factura.setFecha(fechaFormateada);
            factura.setClienteByCliente(cliente);
            factura.setDetallesByNumero(detalles);

            // Llamar al método en la capa de servicio para crear la factura
            service.facturasCreate(factura);

            // Eliminar los detalles de la factura de la sesión después de crear la factura
            session.removeAttribute("facturaDetalles");

            return "/presentation/registroExitoso"; // Redirige a una página de éxito después de crear la factura
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("mensaje", "Hubo un error al crear una factura. Por favor, inténtalo de nuevo.");
            return "/error"; // Retorna a la página de creación de factura con un mensaje de error
        }
    }
}


    /*
    @GetMapping("/presentation/facturar/pdf")
    public void pdf(Factura facturanumero, HttpServletResponse response) throws Exception {
        Factura factura = service.facturasSearchById(facturanumero.getId());
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
    }
    @GetMapping("/presentation/facturas/xml")
    public void xml(Factura facturaNumero, HttpServletResponse response)throws Exception{
        Factura factura = service.facturasSearchById(facturaNumero.getId());
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(new AnnotationConfigApplicationContext());
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".xml");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setTemplateMode(TemplateMode.XML);
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        Context ctx = new Context();
        ctx.setVariable("factura", factura);
        String xml = engine.process("presentation/facturar/xmlView", ctx);
        response.setContentType("application/xml");
        PrintWriter writer = response.getWriter();
        writer.print(xml);
        writer.close();
    }
    */




