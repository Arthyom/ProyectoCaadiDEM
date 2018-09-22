
package com.ProyectoCaadiDEM.Beans;

import com.ProyectoCaadiDEM.Entidades.Teachers;
import com.ProyectoCaadiDEM.Fachadas.TeachersFacade;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;


@Named(value = "beanMaestros")
@SessionScoped
public class BeanMaestros implements Serializable {

    @EJB
    private TeachersFacade fcdMaestros;
    
    private Teachers        mtsActual;
    private Teachers        mtsNuevo;
    private List<Teachers>  mtsSeleccionados;
    private List<Teachers>  mtsFiltrados;
    
    
    
    ////////////////////////////////////////////////////////////////////////////
    public List<Teachers> listarItems () {        
        return fcdMaestros.findAll();
    }
        
    public String borrarSeleccionado () {
        fcdMaestros.remove(mtsActual);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
       
    public String borrarSeleccionados () {
        for( Teachers si : mtsSeleccionados )
            fcdMaestros.remove(si);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
    
    public String guardarItem () {
        fcdMaestros.create(mtsNuevo);
     
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
    ////////////////////////////////////////////////////////////////////////////
     
     
     
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
}
