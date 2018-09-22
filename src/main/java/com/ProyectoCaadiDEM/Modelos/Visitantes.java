
package com.ProyectoCaadiDEM.Modelos;
import com.ProyectoCaadiDEM.Entidades.Visit;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;


@Named(value = "visitantes")
@SessionScoped
public class Visitantes implements Serializable {

    
    private Visit       visita;
    
    
    
    
    public Visitantes() {
    }


    public Visit getVisita() {
        return visita;
    }

    public void setVisita(Visit visita) {
        this.visita = visita;
    }
    
    
    
}
