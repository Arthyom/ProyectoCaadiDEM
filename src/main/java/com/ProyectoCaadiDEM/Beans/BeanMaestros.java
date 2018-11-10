
package com.ProyectoCaadiDEM.Beans;


import com.ProyectoCaadiDEM.Entidades.Groups;
import com.ProyectoCaadiDEM.Entidades.Teachers;
import com.ProyectoCaadiDEM.Fachadas.TeachersFacade;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;


@Named(value = "beanMaestros")
@SessionScoped
public class BeanMaestros implements Serializable {

    @EJB
    private TeachersFacade fcdMaestros;
    
    private Teachers        mtsActual;
    private Teachers        mtsNuevo;
    private List<Teachers>  mtsSeleccionados,
                            mtsNoExist = new ArrayList(),
                            mtsExist   = new ArrayList();
    
    private UploadedFile    archivo;
    private String          nue;
    
    private List<Teachers>  mtsFiltrados;
    
   
    
    
    ////////////////////////////////////////////////////////////////////////////
    public List<Teachers> listarValidos (){
        List<Teachers> t = this.fcdMaestros.getEm().createNamedQuery("Teachers.findValidos").getResultList();
        return t;
        
    }
    
    public List<Teachers> listarItems () {        
        List<Teachers> t = this.fcdMaestros.getEm().createNamedQuery("Teachers.findValidos").getResultList();
        return t;

    }
       
    
    public Collection<Groups> listarGruposXprof(){
        return this.mtsActual.getGroupsCollection();
    }
    public String borrarSeleccionado () {
        mtsActual.setVisible(Boolean.FALSE);
        fcdMaestros.edit(mtsActual);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
       
    public String borrarSeleccionados () {
        for( Teachers si : mtsSeleccionados ){
            si.setVisible(Boolean.FALSE);
            fcdMaestros.edit(si);
        }
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
    
    public String guardarItem () {
        
        if (fcdMaestros.find(mtsNuevo.getEmployeeNumber()) == null) {
            mtsNuevo.setVisible(Boolean.TRUE);
            fcdMaestros.create(mtsNuevo);
        } else {
            mtsNuevo.setVisible(Boolean.TRUE);
            fcdMaestros.edit(mtsNuevo);
        }
     
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
    
     public String editarItem () {
        fcdMaestros.edit(mtsActual);
     
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
     
    public void crearNuevoItem () {
        mtsNuevo = new Teachers();
    }
    
    
      public void cancelarCargaAutomatica (){
        this.mtsExist.clear(); this.mtsNoExist.clear();
    }
    
    public void mensajeCargar () throws IOException{
        
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext ct = FacesContext.getCurrentInstance();
            
        
        if (this.archivo != null) // si es un archivo xl
        {
            if (this.archivo.getContentType().contains("xlsx")) {
                barrerArchivoXl();
                context.execute("PF('dlgCargar').show();");
                return;
            } 
        }
        ct.addMessage(null,
                new FacesMessage("Error: ", "El archivo no tiene el formato correcto"));
    }
    
    public void barrerArchivoXl () throws IOException{
        
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
           
           Teachers nt = this.fcdMaestros.find( String.valueOf(cnV) );
           
           if(nt != null)
                // si el maestro ya existe se agraga a la lista de nuevo  
                this.mtsExist.add(nt);
           else{
               // si el maestro no existe en la base de datos
               Teachers t = new Teachers( String.valueOf(cnV), cAPv, cAMv, cNv, cGV);
               t.setVisible(Boolean.TRUE);
               this.mtsNoExist.add(t);
           }
           
       }
    }
    public String agregarAutomatico(){
        for (Teachers ti : this.mtsNoExist) 
            this.fcdMaestros.create(ti);

        this.mtsExist.clear();
        this.mtsNoExist.clear();
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext ct = FacesContext.getCurrentInstance();

        ct.addMessage(null,
                new FacesMessage("Agregar: ", "Estudiantes Agregados Correctamente"));
        return "listar?faces-redirect=true";
        
    }
    
    public void buscarProfesor () {
        this.mtsActual = this.fcdMaestros.find(this.nue );
    }
    
    public String comprobarProfesor() {

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext ct = FacesContext.getCurrentInstance();
        if (this.mtsActual == null) {

            ct.addMessage(null,
                    new FacesMessage("Error", "No se encontro profesor"));
            return null;
        }
        return "Grupos/reportes_out.xhtml?faces-redirect=true";
    }

    ////////////////////////////////////////////////////////////////////////////

    public String getNue() {
        return nue;
    }

    public void setNue(String nue) {
        this.nue = nue;
    }
     
     
     
    public BeanMaestros() {
    }

    public Teachers getMtsActual() {
        return mtsActual;
    }

    public void setMtsActual(Teachers mtsActual) {
        this.mtsActual = mtsActual;
    }

    public Teachers getMtsNuevo() {
        return mtsNuevo;
    }

    public void setMtsNuevo(Teachers mtsNuevo) {
        this.mtsNuevo = mtsNuevo;
    }

    public List<Teachers> getMtsSeleccionados() {
        return mtsSeleccionados;
    }

    public void setMtsSeleccionados(List<Teachers> mtsSeleccionados) {
        this.mtsSeleccionados = mtsSeleccionados;
    }

    public List<Teachers> getMtsFiltrados() {
        return mtsFiltrados;
    }

    public void setMtsFiltrados(List<Teachers> mtsFiltrados) {
        this.mtsFiltrados = mtsFiltrados;
    } 

    public List<Teachers> getMtsNoExist() {
        return mtsNoExist;
    }

    public void setMtsNoExist(List<Teachers> mtsNoExist) {
        this.mtsNoExist = mtsNoExist;
    }

    public List<Teachers> getMtsExist() {
        return mtsExist;
    }

    public void setMtsExist(List<Teachers> mtsExist) {
        this.mtsExist = mtsExist;
    }

    public UploadedFile getArchivo() {
        return archivo;
    }

    public void setArchivo(UploadedFile archivo) {
        this.archivo = archivo;
    }
    
    
    
    
}
