
package com.ProyectoCaadiDEM.Beans;

import com.ProyectoCaadiDEM.Entidades.Groups;
import com.ProyectoCaadiDEM.Entidades.Periods;
import com.ProyectoCaadiDEM.Entidades.Students;
import com.ProyectoCaadiDEM.Entidades.Teachers;
import com.ProyectoCaadiDEM.Entidades.Visit;
import com.ProyectoCaadiDEM.Fachadas.GroupsFacade;
import com.ProyectoCaadiDEM.Fachadas.PeriodsFacade;
import com.ProyectoCaadiDEM.Fachadas.StudentsFacade;
import com.ProyectoCaadiDEM.Fachadas.TeachersFacade;
import com.ProyectoCaadiDEM.Fachadas.VisitFacade;
import com.ProyectoCaadiDEM.Fachadas.VisitsFacade;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

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
    
    @EJB
    private PeriodsFacade     fcdPeriodos;
    
     @EJB
    private VisitFacade     fcdVisita;

    
    
    
    private UploadedFile     archivo;

    private int              objetivo;
    
    ////////////////////////////////////////////////////////////////////////////
    
    // analizar el archivo json seleccinado
    public void ocultarPanel ( String panel ){
        
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext ct = FacesContext.getCurrentInstance();
        context.execute("PF('"+panel+"').hide();");
        mostrarMensaje();
    }
    
    // analizar el archivo json seleccinado
    public void mostrarPanel ( String panel ){
        
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext ct = FacesContext.getCurrentInstance();
        context.execute("PF('"+panel+"').show();");
    }
    
    public void insertarLineas (String [] lineas){
        DateFormat formateador    = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formateadorIso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        
        ArrayList<String> buffer = new ArrayList();
        
        for (String linea : lineas) {
            String valores[] = linea.split(":");
            if (valores.length == 2) {
                String valor[] = valores[1].split("\\\"");
                buffer.add(valor[1]);
            }
            else{
                
                if(valores.length == 4){
                    String s    = valores[1]+":"+valores[2]+":"+valores[3];
                    String si[] = s.split("\\\"");
                    buffer.add(si[1]);
                }
            }
        }
        
       
        try {
            switch (this.objetivo) {
                //alumnos
                case 1:
                    Students sn = new Students(buffer.get(1), buffer.get(2), buffer.get(3), buffer.get(4), buffer.get(5));
                    sn.setBirthday( formateador.parse(buffer.get(6)) );
                    sn.setProgram(buffer.get(7));
                    sn.setEmail(buffer.get(8));
                    sn.setVisible(Boolean.FALSE);
                    this.fcdEstudiante.create(sn);
                    break;

                //maestros
                case 2:
                    Teachers tn = new Teachers(buffer.get(1), buffer.get(2), buffer.get(3), buffer.get(4), buffer.get(5));
                    tn.setEmail(buffer.get(6));
                    tn.setVisible(Boolean.FALSE);
                    this.fcdMaestros.create(tn);
                    break;
                    
                // periodos
                case 3:
                    Periods pn = new Periods(1, formateador.parse(buffer.get(1) ), formateador.parse(buffer.get(2) ));
                    pn.setDescription(buffer.get(3));
                    pn.setIdAlterno(buffer.get(0));
                    pn.setVisible(Boolean.TRUE);
                    pn.setActual(Boolean.TRUE);
                    fcdPeriodos.create(pn);
                    break;
                    
                // grupos
                case 4:
                    Periods s = fcdPeriodos.conseguirPrdActual();
                   
                   if( s.getIdAlterno().equals( buffer.get(1)) ){
                    Groups gn = new Groups(1,buffer.get(3), buffer.get(4), buffer.get(5));
                    Teachers m = fcdMaestros.find(buffer.get(2));
                    m.setVisible(Boolean.TRUE);
                    fcdMaestros.edit(m);
                    gn.setEmployeeNumber( m);
                    gn.setIdAlterno(buffer.get(0));
                    gn.setVisible(Boolean.TRUE);
                    gn.setPeriodId(fcdPeriodos.conseguirPrdActual());
                    fcdGroups.create(gn);
                   }
                    break;
                    
                // miembros grupo
                case 5:
                      if( fcdPeriodos.conseguirPrdActual().getIdAlterno().equals(buffer.get(1)) ) {
                    List<Groups> ge =  fcdGroups.getEm().createNamedQuery("Groups.findByIdAlterno").
                            setParameter("idAlt", buffer.get(2)).getResultList();
                    
                    Students ss = fcdEstudiante.find(buffer.get(3)) ;
                    ss.setVisible(Boolean.TRUE);
                    fcdEstudiante.edit(ss);
                    
                    ge.get(0).getStudentsCollection().add( ss );
                  
                    fcdGroups.edit(ge.get(0));
                      }
                    break;
                    
                    
                case 6:
                   
                   Periods ms = fcdPeriodos.conseguirPrdActual();
                   
                   if( ms.getIdAlterno().equals( buffer.get(1)) ){
                   Date start = formateadorIso.parse(buffer.get(4));
                   Date end   = formateadorIso.parse(buffer.get(5));
                   Visit vn = new Visit(1);
                   vn.setNua( fcdEstudiante.find(buffer.get(2)));
                   vn.setSkill( buffer.get(3));
                   vn.setStart(start);
                   vn.setEnd(end);
                   vn.setPeriodId( ms );
                   fcdVisita.create(vn);
                   }

                  
                    break;
            }

        } catch (Exception ex) {
            ;
        }
               
    }
    
    public void mostrarMensaje () {

        FacesContext ct = FacesContext.getCurrentInstance();
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        ct.addMessage(null,
                new FacesMessage("Informacion: ", "Transpilacion teminada con exito"));
      
    }
    
    // ejecutar la insercion del archivo json parseado
    public void transpilarArchivo () throws IOException{
        
        if ( archivo != null ){

            StringWriter w = new StringWriter();
            InputStream is = archivo.getInputstream();

            String jsonTxt = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));
            
            String collection [] = jsonTxt.split("\\{");
            
            for( String registro :  collection ){
                String [] lineas = registro.split("\\n");
                this.insertarLineas(lineas);
            }
            
            this.mostrarPanel("status");
        }
        
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
