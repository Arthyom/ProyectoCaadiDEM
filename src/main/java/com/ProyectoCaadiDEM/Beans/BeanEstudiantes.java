
package com.ProyectoCaadiDEM.Beans;

import com.ProyectoCaadiDEM.Entidades.Groups;
import com.ProyectoCaadiDEM.Entidades.Students;
import com.ProyectoCaadiDEM.Fachadas.GroupsFacade;
import com.ProyectoCaadiDEM.Fachadas.StudentsFacade;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;


@Named(value = "beanEstudiantes")
@SessionScoped
public class BeanEstudiantes implements Serializable {


    @EJB
    private StudentsFacade   fcdEstudiante;
    
    @EJB
    private GroupsFacade     fcdGroups;
    
    private Students         stdActual;
    private Students         stdNuevo ;
    private List<Students>   stdSeleccionados, stdExst = new ArrayList(), stdNoExst = new ArrayList();
    private List<Students>   stdFiltrados;
    
    private String           nua;
    private UploadedFile     archivo;
    
    
    ////////////////////////////////////////////////////////////////////////////
    public String limpiar(){      
        this.nua = null;
        this.stdActual = null;
        
        return "/Estudiantes/listar?faces-redirect=true";
    }
    
     public String limpiarOut(){
          FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext ct =  FacesContext.getCurrentInstance();
        ct.addMessage(null, new FacesMessage("Deslogueado", "Hasta luego" ));
        this.nua = null;
        this.stdActual = null;
        
        return "/Students?faces-redirect=true";
    }
    
    public String buscarEstudiante ( ){
        this.stdActual = this.fcdEstudiante.find(this.nua);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext ct =  FacesContext.getCurrentInstance();
        
        if(this.stdActual != null && this.stdActual.getVisible()){
            ct.addMessage(null, 
              new FacesMessage("Deslogueado", "Hasta luego" ));
            return "/StudentsReport?faces-redirect=true";
        }
        ct.addMessage(null, 
              new FacesMessage("Error", "No se ha encontrado el NUA" ));
        return ""; 
    }
    
    public String comprobarEstudianteExterno ( ){
        this.stdActual = this.fcdEstudiante.find(this.nua);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext ct =  FacesContext.getCurrentInstance();
        
        if(this.stdActual != null && this.stdActual.getVisible()){
            ct.addMessage(null, 
              new FacesMessage("Aceptado", "Bienvenido(a): "+this.stdActual.getName() + " "+ this.stdActual.getFirstLastName() + " "+ this.stdActual.getSecondLastName() ));
            return "/StudentsReport?faces-redirect=true";
        }
        ct.addMessage(null, 
              new FacesMessage("Error", "No se ha encontrado el NUA" ));
        return ""; 
    }
    
    public List<Students> listarValidos (){
        List<Students> t = null;
        
        try{
        t = this.fcdEstudiante.getEm().createNamedQuery("Students.findValidos").getResultList();
        }catch(Exception ex){
            ;
        }
        return t;
    }
    
    public List<Students> listarItems () {   
       List<Students> t = null;
        
        try{
        t = this.fcdEstudiante.getEm().createNamedQuery("Students.findValidos").getResultList();
        }catch(Exception ex){
            ;
        }
        return t;
    }
        
    public String borrarSeleccionado () {
        stdActual.setVisible(Boolean.FALSE);
        fcdEstudiante.edit(stdActual);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
       
    public String borrarSeleccionados () {
        for (Students si : stdSeleccionados) {
            si.setVisible(Boolean.FALSE);
            fcdEstudiante.edit(si);
        }
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
    
    public String guardarItem () {
        // ver si el estudiante no existe o solo no esta visible
        if (fcdEstudiante.find(stdNuevo.getNua()) == null) {
            stdNuevo.setVisible(Boolean.TRUE);
            fcdEstudiante.create(stdNuevo);
        } else {
            stdNuevo.setVisible(Boolean.TRUE);
            fcdEstudiante.edit(stdNuevo);
        }
     
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
    
    public void cancelarCargaAutomatica (){
        this.stdExst.clear(); 
        
        this.stdNoExst.clear();
    }
    
    public void mensajeCargar () throws IOException, ParseException{
        
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext ct = FacesContext.getCurrentInstance();
            
        
        try{
                barrerArchivoXl();
                mostrarPanel("dlgCargar");
        }
        catch(Exception exp){
        ct.addMessage(null,
                new FacesMessage("Error: ", "El archivo no tiene el formato correcto"));
        }
    }

    // analizar el archivo json seleccinado
    public void mostrarPanel ( String panel ){
        
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext ct = FacesContext.getCurrentInstance();
        context.execute("PF('"+panel+"').show();");
    }
    
    public void barrerArchivoXl () throws IOException, ParseException{
        
       XSSFWorkbook      nb = new XSSFWorkbook( archivo.getInputstream() );
       XSSFSheet         nh = nb.getSheetAt(0);
       
       for( int nr = nh.getFirstRowNum(); nr<nh.getLastRowNum()+1 ; nr++){

           XSSFRow   r   = nh.getRow(nr);
           XSSFCell  cn  = r.getCell(0);
           XSSFCell  cN  = r.getCell(1);
           XSSFCell  cAP = r.getCell(2);
           XSSFCell  cAM = r.getCell(3);
           XSSFCell  cG  = r.getCell(4);
           
           int     cnV = (int)cn.getNumericCellValue();
           String  cNv = cN.getRichStringCellValue().getString();
           String  cAPv = cAP.getRichStringCellValue().getString();
           String  cAMv = cAM.getRichStringCellValue().getString();
           String  cGV  = cG.getRichStringCellValue().getString();
           
           String  cEmil = r.getCell(6).getRichStringCellValue().getString();
           String  cPedu = r.getCell(7).getRichStringCellValue().getString();

           Date   cFNac; String cFnacS;
                switch(r.getCell(5).getCellType()){
                    case Cell.CELL_TYPE_NUMERIC:
                        cFNac =  r.getCell(5).getDateCellValue();
                        cFnacS = new SimpleDateFormat("dd/MM/yyyy").format(cFNac);
                        cFNac = new SimpleDateFormat("dd/MM/yyyy").parse(cFnacS);
                    break;
                    
                    default:
                        cFnacS =  r.getCell(5).getRichStringCellValue().getString();                              
                        cFNac = new SimpleDateFormat("dd/MM/yyyy").parse(cFnacS );
                    break;
                }
           
           
           Students ns = this.fcdEstudiante.find( String.valueOf(cnV) );
           
           if(ns != null)
                // si el estudiante ya existe se agraga a la lista de nuevo  
                this.stdExst.add(ns);
           else{
               // si el estudiante no existe en la base de datos
              Students s = new Students( String.valueOf(cnV), cAPv, cAMv, cNv, cGV);
              if(cFNac != null)
                s.setBirthday( cFNac );
              
              s.setEmail(cEmil);
              s.setProgram(cPedu);
              s.setVisible(Boolean.TRUE);
              this.stdNoExst.add(s);
           }
       }
    }
    public String agregarAutomatico(){
        for (Students si : this.stdNoExst) 
            this.fcdEstudiante.create(si);
        

        this.stdExst.clear();
        this.stdNoExst.clear();
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext ct = FacesContext.getCurrentInstance();

        ct.addMessage(null,
                new FacesMessage("Agregar: ", "Estudiantes Agregados Correctamente"));
        return "listar?faces-redirect=true";
        
    }
    
    
    public void barrerArchivoTxt (){
        return;
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

    public List<Students> getStdExst() {
        return stdExst;
    }

    public void setStdExst(List<Students> stdExst) {
        this.stdExst = stdExst;
    }

    public List<Students> getStdNoExst() {
        return stdNoExst;
    }

    public void setStdNoExst(List<Students> stdNoExst) {
        this.stdNoExst = stdNoExst;
    }
    
    
    
    

}
