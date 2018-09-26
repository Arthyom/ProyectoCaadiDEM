
package com.ProyectoCaadiDEM.Beans;

import com.ProyectoCaadiDEM.Entidades.Groups;
import com.ProyectoCaadiDEM.Entidades.Periods;
import com.ProyectoCaadiDEM.Entidades.Students;
import com.ProyectoCaadiDEM.Entidades.Teachers;
import com.ProyectoCaadiDEM.Fachadas.GroupsFacade;
import com.ProyectoCaadiDEM.Fachadas.PeriodsFacade;
import com.ProyectoCaadiDEM.Fachadas.StudentsFacade;
import com.ProyectoCaadiDEM.Fachadas.TeachersFacade;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


@Named(value = "beanGrupos")
@SessionScoped
public class BeanGrupos implements Serializable {
    
    private Groups           grpNuevo;
    private Groups           grpActual;
    private List<Groups>     grpsSeleccionados;
    private List<Groups>     grpsFiltrados;
    private List<Students>   stdsSlct, stdsFlt;
    private List<Students>   stdsSlctG, stdsFltG;
    private String           indxPrf, indxPrd;
    
    
    
    @EJB
    private GroupsFacade     fcdGrupos;
    
    @EJB
    private StudentsFacade   fcdEstudints;
    
    @EJB
    private TeachersFacade   fcdProfes;
    
    @EJB
    private PeriodsFacade    fcdPeriods;
    
    private int grpIndex;
    
    ////////////////////////////////////////////////////////////////////////////
    public List<Groups> listarItems () {
        return this.fcdGrupos.findAll();
    }
    
    public List<Students> listarStdSinGrupo () {
        
        List<Students> estTotal  = fcdEstudints.findAll();
        List<Students> estLibrs  = fcdEstudints.findAll();
        List<Groups>   grpTotal  = fcdGrupos.findAll();
        
        for( Students ei : estTotal ){
            for( Groups gi : grpTotal )
                for( Students eii : gi.getStudentsCollection() )
                    if( eii.getNua().equals(ei.getNua()) )
                        estLibrs.remove(ei);
        }
        return estLibrs;
    }
    
    public int contarStdConGrupo () {
        
        int a = 0;
        
        List<Groups>   grpTotal  = fcdGrupos.findAll();
        
        for( Groups gi : grpTotal )
            a += gi.getStudentsCollection().size();
            
        return a;
    }
    
    public String eliminarStdGrupo ( Students s){
        this.grpActual.getStudentsCollection().remove(s);
        this.fcdGrupos.edit(this.grpActual);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Asignacion", "Se ha Eliminado al estudiante" ));
        return "asignacion?faces-redirect=true";
    }
         
    public String borrarSeleccionado () {
        fcdGrupos.remove(grpActual);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
       
    public String borrarSeleccionados () {
        for( Groups si : grpsSeleccionados )
            fcdGrupos.remove(si);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
    
    public String guardarItem () {
/*
        Periods   pn = fcdPeriods.find(1);
        Teachers  tn = fcdProfes.find("210123");
        Students  sn = fcdEstudints.find("102345");
        List<Students> ncs = new ArrayList <> () ;
        ncs.add(sn);
        
        
        grpNuevo.setEmployeeNumber(tn);
        grpNuevo.setPeriodId(pn);
        grpNuevo.setStudentsCollection(ncs);
*/

        Periods   pn = fcdPeriods.find(1);
        Teachers  tn = fcdProfes.find(this.indxPrf);
      //  Students  sn = new Students ();
        List<Students> ncs = new ArrayList <> () ;

        
        
        grpNuevo.setEmployeeNumber(tn);
        grpNuevo.setPeriodId(pn);
        //grpNuevo.setStudentsCollection(ncs);

        fcdGrupos.create(grpNuevo);
     
     
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
    
     public String editarItem () {
        grpActual.setEmployeeNumber( fcdProfes.find(this.indxPrf) );
        fcdGrupos.edit(grpActual);
     
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
     
    public void crearNuevoItem () {
        grpNuevo = new Groups();
    }
    
    public void buscarGrupo () {
        this.grpActual = fcdGrupos.find(this.grpIndex);
    }
    
    public String asignarME() {
        for(Students si : this.stdsSlct )
            this.grpActual.getStudentsCollection().add(si);
        
        //this.grpActual.setEmployeeNumber( this.fcdProfes.find(this.indxPrf) );
        this.fcdGrupos.edit(this.grpActual);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Asignacion", "Se han asigando los estudiantes" ));
        return "asignacion?faces-redirect=true";
    }
    
    public String eliminarSeleccionados (){
        for(Students si : this.stdsSlctG)
            this.grpActual.getStudentsCollection().remove(si);
        this.fcdGrupos.edit(grpActual);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Asignacion", "Se han Borrado los estudiantes" ));
        return "asignacion?faces-redirect=true";
    }

    ////////////////////////////////////////////////////////////////////////////
    
    
    
    public BeanGrupos() {
    }

    public int getGrpIndex() {
        return grpIndex;
    }

    public void setGrpIndex(int grpIndex) {
        this.grpIndex = grpIndex;
    }    

    public Groups getGrpNuevo() {
        return grpNuevo;
    }

    public void setGrpNuevo(Groups grpNuevo) {
        this.grpNuevo = grpNuevo;
    }

    public Groups getGrpActual() {
        return grpActual;
    }

    public void setGrpActual(Groups grpActual) {
        this.grpActual = grpActual;
    }

    public List<Groups> getGrpsSeleccionados() {
        return grpsSeleccionados;
    }

    public void setGrpsSeleccionados(List<Groups> grpsSeleccionados) {
        this.grpsSeleccionados = grpsSeleccionados;
    }

    public List<Groups> getGrpsFiltrados() {
        return grpsFiltrados;
    }

    public void setGrpsFiltrados(List<Groups> grpsFiltrados) {
        this.grpsFiltrados = grpsFiltrados;
    }

    public List<Students> getStdsSlct() {
        return stdsSlct;
    }

    public void setStdsSlct(List<Students> stdsSlct) {
        this.stdsSlct = stdsSlct;
    }

    public List<Students> getStdsFlt() {
        return stdsFlt;
    }

    public void setStdsFlt(List<Students> stdsFlt) {
        this.stdsFlt = stdsFlt;
    }

    public String getIndxPrf() {
        return indxPrf;
    }

    public void setIndxPrf(String indxPrf) {
        this.indxPrf = indxPrf;
    }

    public String getIndxPrd() {
        return indxPrd;
    }

    public void setIndxPrd(String indxPrd) {
        this.indxPrd = indxPrd;
    }

    public List<Students> getStdsSlctG() {
        return stdsSlctG;
    }

    public void setStdsSlctG(List<Students> stdsSlctG) {
        this.stdsSlctG = stdsSlctG;
    }

    public List<Students> getStdsFltG() {
        return stdsFltG;
    }

    public void setStdsFltG(List<Students> stdsFltG) {
        this.stdsFltG = stdsFltG;
    }
 
    
    
   
}
