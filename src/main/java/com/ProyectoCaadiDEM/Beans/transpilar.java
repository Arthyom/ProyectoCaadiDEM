
package com.ProyectoCaadiDEM.Beans;

import com.ProyectoCaadiDEM.Fachadas.GroupsFacade;
import com.ProyectoCaadiDEM.Fachadas.StudentsFacade;
import com.ProyectoCaadiDEM.Fachadas.TeachersFacade;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import org.primefaces.model.UploadedFile;

@Named(value = "transpilar")
@SessionScoped
public class transpilar implements Serializable {

    @EJB
    private StudentsFacade   fcdEstudiante;
    
    @EJB
    private TeachersFacade fcdMaestros;
    
    @EJB
    private GroupsFacade     fcdGroups;
    
    private UploadedFile     archivo;

    private int              objetivo;
    
    ////////////////////////////////////////////////////////////////////////////
    
    // analizar el archivo json seleccinado
    public void barrerJson(){
        
    }
    
    // ejecutar la insercion del archivo json parseado
    public void transpilarArchivo (){
        
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    
    public transpilar() {
    }

    public UploadedFile getArchivo() {
        return archivo;
    }

    public void setArchivo(UploadedFile archivo) {
        this.archivo = archivo;
    }

    public int getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(int objetivo) {
        this.objetivo = objetivo;
    }
    
    
    
    
}
