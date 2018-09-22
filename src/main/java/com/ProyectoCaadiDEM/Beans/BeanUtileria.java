
package com.ProyectoCaadiDEM.Beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;


@Named(value = "beanUtileria")
@SessionScoped
public class BeanUtileria implements Serializable {
    
     public void mensajeBorrdo (){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Borrar",  "Registros borrados con exito") );
    }
    
    
    public void mensajeAgregado () {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Agregar",  "Registros Agregados con exito") );   
    }
    
     public void mensajeEditado () {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Editar",  "Registro Editado con exito") );   
    }
    
     
    public void mostrarDialogoBorrarMulti ( String objetivo ) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('"+objetivo+"').show();");
    }
    
    public void mostrarDialogoBorrarSingle ( String objetivo ) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('"+objetivo +"').show();");
    }
    
    
 
    public BeanUtileria() {
    }
    
}
