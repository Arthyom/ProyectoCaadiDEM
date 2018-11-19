
package com.ProyectoCaadiDEM.Beans;

import com.ProyectoCaadiDEM.Entidades.Usuarios;
import com.ProyectoCaadiDEM.Fachadas.UsuariosFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


@Named(value = "beanUsuarios")
@ApplicationScoped
public class BeanUsuarios {
    @EJB
    private UsuariosFacade fcdUsuario;
            
    
    String usr, pass;
    
    Usuarios usrActual;
    Usuarios ursNuevo;
    Usuarios usrEditado;
    List<Usuarios> usrFiltrados, usrSeleccionados;
    
    
    public String buscarUsuario (){
        this.usrActual = this.fcdUsuario.buscarPorNombreYPass(this.usr, this.pass);

        if(this.usrActual != null){
            if("admin".equals(this.usrActual.getNombre())){
                mensajeAceptarAdmin();
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("admin", "admin");
                return "dash?faces-redirect=true";
            }
            else{
                if("students".equals(this.usrActual.getNombre())){
                    mensajeAceptarStd();
                    return "/Visitas/LogInSession?faces-redirect=true";
                }
            }
        }
       mensajeRechazarAdmin();
        return "";            
    }
    
    public void mensajeRechazarAdmin () {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Rechazadoo",  "Credenciales invalidas") );   
    }
    
    public void mensajeAceptarStd () {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Bienvenido",  "Estudiante") );   
    }
    
    public void mensajeAceptarAdmin () {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Aceptado",  "Bienvenido Administrador") );   
    }
    
    
    public BeanUsuarios() {
    }

    public Usuarios getUsrActual() {
        return usrActual;
    }

    public void setUsrActual(Usuarios usrActual) {
        this.usrActual = usrActual;
    }

    public Usuarios getUrsNuevo() {
        return ursNuevo;
    }

    public void setUrsNuevo(Usuarios ursNuevo) {
        this.ursNuevo = ursNuevo;
    }

    public Usuarios getUsrEditado() {
        return usrEditado;
    }

    public void setUsrEditado(Usuarios usrEditado) {
        this.usrEditado = usrEditado;
    }

    public List<Usuarios> getUsrFiltrados() {
        return usrFiltrados;
    }

    public void setUsrFiltrados(List<Usuarios> usrFiltrados) {
        this.usrFiltrados = usrFiltrados;
    }

    public List<Usuarios> getUsrSeleccionados() {
        return usrSeleccionados;
    }

    public void setUsrSeleccionados(List<Usuarios> usrSeleccionados) {
        this.usrSeleccionados = usrSeleccionados;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    
    
    
    
}
