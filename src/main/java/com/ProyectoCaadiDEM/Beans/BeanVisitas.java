
package com.ProyectoCaadiDEM.Beans;

import com.ProyectoCaadiDEM.Entidades.Visits;
import com.ProyectoCaadiDEM.Fachadas.VisitsFacade;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;


@Named(value = "beanVisitas")
@SessionScoped
public class BeanVisitas implements Serializable {

    
    @EJB
    private VisitsFacade    fcdVisitas;
    
    private Visits          vstActual;
    
    
    
    
    public BeanVisitas() {
    }

    public Visits getVstActual() {
        return vstActual;
    }

    public void setVstActual(Visits vstActual) {
        this.vstActual = vstActual;
    }
    
    
}
