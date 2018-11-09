
package com.ProyectoCaadiDEM.Beans;

import com.ProyectoCaadiDEM.Entidades.Groups;
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
    public List<Periods> listarValidos (){
        List<Periods> t = this.fcdPeriodos.getEm().createNamedQuery("Periods.findValidos").getResultList();
        return t;
    }
    
    public List<Periods> listarItems () {  
        List<Periods> t = this.fcdPeriodos.getEm().createNamedQuery("Periods.findValidos").getResultList();
        return t;

    }
        
    public String borrarSeleccionado () {
        prdActual.setVisible(Boolean.FALSE);
        prdActual.setActual(Boolean.FALSE);
        fcdPeriodos.edit(prdActual);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
       
    public String borrarSeleccionados () {
        for( Periods si : prdSeleccionados ){
            si.setActual(Boolean.FALSE);
            si.setVisible(false);
            fcdPeriodos.edit(si);
        }
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
    
    public String guardarItem () {
        prdNuevo.setVisible(Boolean.TRUE);
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
     
     public void fijarComoActual (){
         
         List<Periods> ls = this.fcdPeriodos.findAll();
         for(Periods pi : ls){
             pi.setActual(Boolean.FALSE);
             this.fcdPeriodos.edit(pi);
         }

         this.prdActual.setActual(Boolean.TRUE);
         this.fcdPeriodos.edit(this.prdActual); 
     }
     
     
     public Periods conseguirActual(){
         Periods p = this.fcdPeriodos.conseguirPrdActual();
         return p;
     }
     
     public List<Groups> conseguirGruposParaAtual () {
         Periods p = this.conseguirActual();
         List<Groups> gs = (List<Groups>) p.getGroupsCollection();

         return gs;
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
