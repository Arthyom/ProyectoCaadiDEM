
package com.ProyectoCaadiDEM.Beans;

import com.ProyectoCaadiDEM.Entidades.Students;
import com.ProyectoCaadiDEM.Fachadas.StudentsFacade;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.UploadedFile;


@Named(value = "beanEstudiantes")
@SessionScoped
public class BeanEstudiantes implements Serializable {


    @EJB
    private StudentsFacade   fcdEstudiante;
    
    private Students         stdActual;
    private Students         stdNuevo ;
    private List<Students>   stdSeleccionados;
    private List<Students>   stdFiltrados;
    
    private String           nua;
    private UploadedFile     archivo;
    
    
    ////////////////////////////////////////////////////////////////////////////
    public String buscarEstudiante ( ){
        this.stdActual = this.fcdEstudiante.find(this.nua);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext ct =  FacesContext.getCurrentInstance();
        
        if(this.stdActual != null ){
            ct.addMessage(null, 
              new FacesMessage("Deslogueado", "Hasta luego" ));
            return "/Estudiantes/estatus?faces-redirect=true";
        }
        ct.addMessage(null, 
              new FacesMessage("Error", "No se ha encontrado el NUA" ));
        return ""; 
    }
    
    public List<Students> listarItems () {        
        return fcdEstudiante.findAll();
    }
        
    public String borrarSeleccionado () {
        fcdEstudiante.remove(stdActual);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
       
    public String borrarSeleccionados () {
        for( Students si : stdSeleccionados )
            fcdEstudiante.remove(si);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
    
    public String guardarItem () {
        fcdEstudiante.create(stdNuevo);
     
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
    
     public String editarItem () {
        fcdEstudiante.edit(stdActual);
     
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
     
    public void crearNuevoItem () {
        stdNuevo = new Students();
    }
    
    public void mensajeCargar () throws IOException{
        if( this.archivo != null ){
            FacesMessage message = new FacesMessage("Succesful", this.archivo.getContentType() +" "+ this.archivo.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            barrerArchivo();
        }
    }
    
    public void barrerArchivo () throws IOException{
       InputStreamReader in = new InputStreamReader(archivo.getInputstream(), "UTF-8");
       BufferedReader    bf = new BufferedReader(in);
       XSSFWorkbook      nb = new XSSFWorkbook( archivo.getInputstream() );
       XSSFSheet         nh = nb.getSheetAt(0);
       
       for( int nr = nh.getFirstRowNum(); nr<nh.getLastRowNum()+1 ; nr++){

           XSSFRow   r = nh.getRow(nr);
           XSSFCell  cn = r.getCell(0);
           XSSFCell  cN = r.getCell(1);
           XSSFCell  cA = r.getCell(2);
           XSSFCell  cP = r.getCell(3);
           
          
           XSSFRichTextString  cNv = cN.getRichStringCellValue();
           XSSFRichTextString  cAv = cA.getRichStringCellValue();
           XSSFRichTextString  cPv = cP.getRichStringCellValue();
           
           
       }
       
       String line;
       
     
    }
    
    ////////////////////////////////////////////////////////////////////////////

    public String getNua() {
        return nua;
    }

    public void setNua(String nua) {
        this.nua = nua;
    }

    public BeanEstudiantes() {
    }

    public Students getStdActual() {
        return stdActual;
    }

    public void setStdActual(Students stdActual) {
        this.stdActual = stdActual;
    }

    public List<Students> getStdSeleccionados() {
        return stdSeleccionados;
    }

    public void setStdSeleccionados(List<Students> stdSeleccionados) {
        this.stdSeleccionados = stdSeleccionados;
    }

    public List<Students> getStdFiltrados() {
        return stdFiltrados;
    }

    public void setStdFiltrados(List<Students> stdFiltrados) {
        this.stdFiltrados = stdFiltrados;
    }

    public Students getStdNuevo() {
        return stdNuevo;
    }

    public void setStdNuevo(Students stdNuevo) {
        this.stdNuevo = stdNuevo;
    }

    public UploadedFile getArchivo() {
        return archivo;
    }

    public void setArchivo(UploadedFile archivo) {
        this.archivo = archivo;
    }
    
    
    

}
