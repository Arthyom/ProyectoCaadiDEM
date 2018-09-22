
package com.ProyectoCaadiDEM.Beans;

import com.ProyectoCaadiDEM.Entidades.Periods;
import com.ProyectoCaadiDEM.Fachadas.PeriodsFacade;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

@Named(value = "beanPeriodos")
@SessionScoped
public class BeanPeriodos implements Serializable {

    
    @EJB
    private PeriodsFacade  fcdPeriodos;
    private Periods        prdActual;
    private Periods        prdNuevo;
    private List<Periods>  prdSeleccionados;
    private List<Periods>  prdFiltrados;
        
    
    
    ////////////////////////////////////////////////////////////////////////////
    public List<Periods> listarItems () {        
        return fcdPeriodos.findAll();
    }
        
    public String borrarSeleccionado () {
        fcdPeriodos.remove(prdActual);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
       
    public String borrarSeleccionados () {
        for( Periods si : prdSeleccionados )
            fcdPeriodos.remove(si);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
    
    public String guardarItem () {
        fcdPeriodos.create(prdNuevo);
     
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
    
     public String editarItem () {
        fcdPeriodos.edit(prdActual);
     
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
     
     public void crearNuevoItem () {
        prdNuevo = new Periods();
    }
    ////////////////////////////////////////////////////////////////////////////
     
     
     
    
    
    public BeanPeriodos() {
    }

    public Periods getPrdActual() {
        return prdActual;
    }

    public void setPrdActual(Periods prdActual) {
        this.prdActual = prdActual;
    }

    public Periods getPrdNuevo() {
        return prdNuevo;
    }

    public void setPrdNuevo(Periods prdNuevo) {
        this.prdNuevo = prdNuevo;
    }

    public List<Periods> getPrdSeleccionados() {
        return prdSeleccionados;
    }

    public void setPrdSeleccionados(List<Periods> prdSeleccionados) {
        this.prdSeleccionados = prdSeleccionados;
    }

    public List<Periods> getPrdFiltrados() {
        return prdFiltrados;
    }

    public void setPrdFiltrados(List<Periods> prdFiltrados) {
        this.prdFiltrados = prdFiltrados;
    }
    
    
}
