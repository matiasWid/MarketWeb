package servlet;

import controladores.ControladorUsuarios;
import controladores.Controladorjuegos;
import dominio.Categoria;
import dominio.Desarrollador;
import dominio.Juego;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author BlackArmor
 */
@WebServlet(name = "AltaJuego", urlPatterns = {"/AltaJuego"})
public class AltaJuego extends HttpServlet {

    private Controladorjuegos cj = Controladorjuegos.getInstancia();
    private ControladorUsuarios cu = ControladorUsuarios.getInstancia();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        PrintWriter out = response.getWriter();
        
        String nombre = request.getParameter("nombre");
        String desc = request.getParameter("desc");
        Double precio = Double.parseDouble(request.getParameter("precio"));
        String version = request.getParameter("version");
        String cats[] = request.getParameterValues("cats");
        
        
        
        
        if (cats != null){
            System.out.println("tam"+cats.length);
        }
        Juego j = new Juego();
        
        
                
        /* Obtener ID Desarrollador */
        HttpSession s = request.getSession(true);
        if(s.getAttribute("usuario") != null){
        Desarrollador des = new Desarrollador();
        try {
            //obtener usuario desarrollador
            String usuariologueado = s.getAttribute("usuario").toString();
            //obtener desarrollador
            des = (Desarrollador)cu.find(usuariologueado);
        } catch (SQLException ex) {
            Logger.getLogger(AltaJuego.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ArrayList categorias = new ArrayList();
        
        int i=0;
        while(i < cats.length){
            Categoria c = new Categoria();
            c.setId(Integer.valueOf(cats[i]));
            categorias.add(c);
            i++;
        }
        
        System.out.println(nombre + desc + precio + version+"i: "+i);
        
        j.setNombre(nombre);
        j.setDescripcion(desc);
        j.setPrecio(precio);
        j.setCategorias(categorias);
        j.setDes(des);
  

        try {        
            cj.altaJuego(j, categorias);
            System.out.println("Juego Creado exitosamente");
            
        } catch (Exception ex) {
            Logger.getLogger(Registro.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            out.flush();
            out.close();
        }
    }
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
