
package com.ProyectoCaadiDEM.Beans;

import com.ProyectoCaadiDEM.Entidades.Periods;
import com.ProyectoCaadiDEM.Entidades.Students;
import com.ProyectoCaadiDEM.Entidades.Visit;
import com.ProyectoCaadiDEM.Fachadas.PeriodsFacade;
import com.ProyectoCaadiDEM.Fachadas.StudentsFacade;
import com.ProyectoCaadiDEM.Fachadas.VisitFacade;
import com.ProyectoCaadiDEM.Modelos.Visitantes;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;


@Named(value = "beanVisit")
@SessionScoped
public class BeanVisit implements Serializable {


    
    @EJB 
    private VisitFacade     fcdVisita;
    @EJB
    private StudentsFacade  fcdEstudiante;
    @EJB
    private PeriodsFacade   fcdPeriodo;
    
    private Visit           vstActual;
    
    private Students        stdActual;
    
    private Periods         prdActual;
    
    private String          nua;
    
    private Visitantes      vistActual;
    
   
    
    
    public BeanVisit() {
    }

    public Visit getVstActual() {
        return vstActual;
    }

    public void setVstActual(Visit vstActual) {
        this.vstActual = vstActual;
    }
    
    public void crearVisita () {

        // buscar el NUA y regresar al estudiante
       Students st = fcdEstudiante.find(this.nua);
       
       if ( st != null ){
           
           // crear una nueva visita, buscar al periodo actual
           this.vstActual = new Visit(2, new Date() );
           this.stdActual = st;
           this.prdActual = fcdPeriodo.find(1);
           this.vstActual.setNua(st);
           this.vstActual.setPeriodId(prdActual);
           
           return;
       }
       
       this.stdActual = null;
       
       
       
    }
   public String verificar () {
      Map<String, Object> mv = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
       if( this.prdActual != null && this.stdActual != null && !mv.containsKey(this.nua) ){
           // crear un visitante
           Visitantes visitante =  new Visitantes();
           visitante.setVisita(this.vstActual);
        
           
           FacesContext.getCurrentInstance().getExternalContext().
                  getSessionMap().put( this.nua, visitante );
           
           this.nua = null;
           FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
           FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Aceptado", "Bienvenido: " + this.stdActual.getName()));
           return "LogInSession?faces-redirect=true";
       }
       else{
           this.vistActual = (Visitantes) mv.get(this.nua);
           RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('logInConfirm').hide();");
            context.execute("PF('logOutConfirm').show();");
            
       }
       
       return null;
   }

   public String logOutVisitante ( String habilidad ){
      RequestContext context = RequestContext.getCurrentInstance();
      this.vistActual.getVisita().setEnd( new Date() );
      this.vistActual.getVisita().setSkill(habilidad);
      this.fcdVisita.create(this.vistActual.getVisita());
      Map<String, Object> mv = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
      mv.remove(this.nua);
      this.vstActual = null;
      this.vistActual = null;
      this.stdActual  = null;
      this.nua = null;
      context.execute("PF('logOutConfirm').hide();");
      
      FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
      FacesContext.getCurrentInstance().addMessage(null, 
              new FacesMessage("Deslogueado", "Hasta luego" ));
      return "LogInSession?faces-redirect=true";
   }
   
   public List<Visitantes> verHash () {
       
      Map<String, Object> mv = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
      
      List<Visitantes> s = new ArrayList<> ();
      
      for ( String clave : mv.keySet() )
          if( clave.length() == 6 )
            s.add((Visitantes) mv.get(clave));
          
       return s;
   }
   
    public void mensajeErrorLog() {
        if (this.stdActual != null) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('logInConfirm').show();");
        } else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error", "No se ha encontrado el NUA: " + this.nua));
            this.nua = null;
        }
    }
    
    public void cancelarLogin() {
        this.stdActual = null;
        this.nua = null;
        

    }

    public String desconectar ( String nua ) {
        
        Map<String, Object> mv = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        mv.remove(nua);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().
                addMessage(null, 
                        new FacesMessage("Desconectado", "El Estudiante ha Sido Desconectado" ));
        return "registrados?faces-redirect=true";
    }
    
    public String contarHoras(Students stdCont) {
        String total = "0 Horas 0 Minutos";
        long   tTtl = 0;

        for (Visit vi : stdCont.getVisitCollection()) {
            if (vi.getEnd() != null && vi.getStart() != null) {
                tTtl += vi.getEnd().getTime() - vi.getStart().getTime();
                Date delta = new Date(tTtl);
                total = "Horas: " + delta.getHours() + " Minutos: " + delta.getMinutes() + " Segundos: " + delta.getSeconds();
            }
        }
        return total;
    }
    ////////////////////////////////////////////////////////////////////////////
    
    
    public String getNua() {
        return nua;
    }

    public void setNua(String nua) {
        this.nua = nua;
    }

    public Students getStdActual() {
        return stdActual;
    }

    public void setStdActual(Students stdActual) {
        this.stdActual = stdActual;
    }

    public Periods getPrdActual() {
        return prdActual;
    }

    public void setPrdActual(Periods prdActual) {
        this.prdActual = prdActual;
    }

   
   
   
}
