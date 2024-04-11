package com.example.proyecto_i.presentation.factura;

import com.example.proyecto_i.logic.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Controller("facturas")
@SessionAttributes({"facturas","clienteSearch","proveedor", "productoSearch"})
public class Controller {
    @Autowired private Service service;

    @ModelAttribute("facturas")
    public List<Factura> facturas(){
        return new ArrayList<Factura>();
    }

    @ModelAttribute("clienteSearch")
    public Cliente clienteSearch (){
        Cliente cliente = new Cliente();
        cliente.setNombre("...");
        return cliente;
    }

    @ModelAttribute("productoSearch")
    public Producto productoSearch(){
        return new Producto();
    }

    @ModelAttribute("proveedor")
    public Proveedor proveedor (HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Optional<Proveedor> prov = service.proveedorRead(usuario.getIdentificacion());
        Proveedor proveedor = new Proveedor();
        if(prov.isPresent()){
            proveedor =  prov.get();
        }
        return proveedor;
    }

    @ModelAttribute("detalles")
    public Iterable<Detalle> detalles(){
        return new ArrayList<Detalle>();
    }

    @ModelAttribute("detalle")
    public Detalle detalle(){
        return new Detalle();
    }

    @ModelAttribute("factura")
    public Factura factura(){
        return new Factura();
    }

    @GetMapping("/crearFactura")
    public String crearFactura(@ModelAttribute("clienteSearch") Cliente cliente, @ModelAttribute("proveedor") Proveedor proveedor, Model model, HttpSession session, Factura factura){
        try{
            /*Usuario usuario = (Usuario) session.getAttribute("usuario");
            proveedor = service.proveedorRead(usuario.getIdentificacion());
            model.addAttribute("proveedor", proveedor);*/
            //cliente = model.getAttribute("clienteFactura")

        }catch(Exception e){
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

    @GetMapping("/presentation/facturar/show")
    public String search(Model model, HttpSession session, @ModelAttribute("proveedor") Proveedor proveedor, @ModelAttribute("facturas") List<Factura> facturas){
        try {
            facturas = service.facturasSearchByProveedor(proveedor.getIdentificacion());
            model.addAttribute("facturas", facturas);
            return "presentation/facturar/show";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("mensaje", "Hubo un error al crear una factura. Por favor, inténtalo de nuevo.");
        }
        return "/presentation/facturar/crearFactura";
    }

    @GetMapping("agregarDetalle")
    public String agregarDetalle(HttpSession session, Model model, @ModelAttribute("detalle") Detalle detalle, @ModelAttribute("detalles") Iterable<Detalle> detalles, @ModelAttribute("productoSearch") Producto producto, @ModelAttribute("factura") Factura factura){
        Detalle nuevo = new Detalle();
        nuevo.setCantidad(1);
        nuevo.setProductoByCodigoproducto(productoSearch());
        nuevo.setDescripcion(productoSearch().getNombre());
        nuevo.setFacturaByNumerofactura(factura);
        return "redirect:/crearFactura";
    }

    @GetMapping("/facturaProducto")
    public String facturaProducto(Model model, @ModelAttribute("producto") Producto producto, @ModelAttribute("factura") Factura factura, HttpSession session){
        return "redirect:/crearFactura";
    }

    @PostMapping("/facturaCliente")
    public String facturaCliente(Model model, HttpSession session, @RequestParam String identificacion, @ModelAttribute("factura") Factura factura) throws Exception {
        Factura facturaNueva = new Factura();
        Cliente addCliente = service.clientesSearch(identificacion);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        try{
            Proveedor proveedor = new Proveedor();
            Optional<Proveedor> prov = service.proveedorRead(usuario.getIdentificacion());
            if(prov.isPresent() && addCliente != null){
                facturaNueva.setClienteByCliente(addCliente);
                model.addAttribute("clienteSearch", addCliente);
                model.addAttribute("factura", facturaNueva);
                proveedor = prov.get();
                model.addAttribute("proveedor", proveedor);
            }
            return "redirect:/crearFactura";
        }catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("mensaje", "Hubo un error al crear una factura. Por favor, inténtalo de nuevo.");
            return "redirect:/crearFactura";
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


