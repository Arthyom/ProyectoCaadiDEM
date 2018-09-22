
package com.ProyectoCaadiDEM.Beans;

import com.ProyectoCaadiDEM.Entidades.Students;
import com.ProyectoCaadiDEM.Fachadas.StudentsFacade;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;


@Named(value = "beanEstudiantes")
@SessionScoped
public class BeanEstudiantes implements Serializable {


    @EJB
    private StudentsFacade   fcdEstudiante;
    
    private Students         stdActual;
    private Students         stdNuevo ;
    private List<Students>   stdSeleccionados;
    private List<Students>   stdFiltrados;
    
    
    
    ////////////////////////////////////////////////////////////////////////////
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
    ////////////////////////////////////////////////////////////////////////////
    
    
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
    
    

}
