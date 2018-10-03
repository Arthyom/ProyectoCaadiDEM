
package com.ProyectoCaadiDEM.Beans;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.servlet.http.HttpSession;

@Named(value = "beanIndex")
@SessionScoped
public class BeanIndex implements Serializable {

    public String cerrarSesion (){
       FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
       FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("admin");
       
       
        return "/index?faces-redirect=true";
    }
    
    public void  comprobarSesion () throws IOException{
       String ad = (String)  FacesContext.getCurrentInstance().getExternalContext()
               .getSessionMap().get("admin");
       if(!"admin".equals(ad))
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
    }
 
    public BeanIndex() {
    }
    
}
