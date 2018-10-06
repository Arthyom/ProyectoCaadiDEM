
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
import com.itextpdf.layout.element.Cell;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPHeaderCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.OutputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;


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
    
    public String contarHorasGrupo ( Groups grp ){
        long total = 0, h = 0, m = 0;

        if (grp != null) {
            // contar las horas que cada estudiante ha realizaro
            for (Students si : grp.getStudentsCollection()) {
                for (Visit vi : si.getVisitCollection()) {
                    total += vi.getEnd().getTime() - vi.getStart().getTime();
                }
            }

            h = total / (1000 * 60 * 60);
            total = total % (1000 * 60 * 60);
            m = total / (1000 * 60);

            return h + " Horas, " + m + " Minutos";
        }
        return "-";
    }

    public void crearPDF() throws IOException, BadElementException, DocumentException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        String profName = grpActual.getEmployeeNumber().getName();
        String proFFname = grpActual.getEmployeeNumber().getFirstLastName();
        String proFSname = grpActual.getEmployeeNumber().getSecondLastName();
        String grpSlctd = grpActual.getLearningUnit() + " " + grpActual.getLevel();
        String grpIds = grpActual.getId() + grpActual.getIdentifier();
        int aRegs = grpActual.getStudentsCollection().size();

        List<Students> grs = (List<Students>) grpActual.getStudentsCollection();

        ec.responseReset();
        ec.setResponseContentType("application/pdf");
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "RP" + grpSlctd + profName + proFFname + ".pdf" + "\"");
        OutputStream output = ec.getResponseOutputStream();

        Document nd = new Document(PageSize.LETTER);
        PdfPTable nt = new PdfPTable(5);

        // Image imgP = Image.getInstance("/home/frodo/images/" + grpIds + "grafPie.jpeg");
        // Image imgB = Image.getInstance("/home/frodo/images/" + grpIds + "grafBar.jpeg");
        Font hf = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
        Font th = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD, BaseColor.BLUE);

        Phrase contenido = new Phrase();

        PdfWriter.getInstance(nd, output);
        nd.open();

        // primer parrafo
        nd.add(new Paragraph("Reporte De Grupo Para: "));
        contenido.setFont(th);
        contenido.add(grpSlctd + ",  Profesor: " + proFFname + " " + proFFname + " " + proFSname + ", Alumnos Registrados: " + aRegs);
        nd.add(contenido);

        // encabezad de la tabla 
        nt.addCell(new Phrase("NUA", hf));
        nt.addCell(new Phrase("Nombre", hf));
        nt.addCell(new Phrase("Apellido Paterno", hf));
        nt.addCell(new Phrase("Apellido Materno", hf));
        nt.addCell(new Phrase("Total de Horas", hf));

        // para cada estudiante en la coleccion 
        for (Students sti : grs) {
            nt.addCell(sti.getNua());
            nt.addCell(sti.getName());
            nt.addCell(sti.getFirstLastName());
            nt.addCell(sti.getSecondLastName());
            nt.addCell(contarHorasParaStd(sti));
        }

        nd.add(nt);
        // nd.add(imgP);
        // nd.add(imgB);
        nd.close();
        fc.responseComplete();

        
    }
    
    public String contarHorasParaStd ( Students stdGrp ){
        long total = 0, h = 0 , m = 0;
        
        for ( Visit vi : stdGrp.getVisitCollection() )
            total += vi.getEnd().getTime() - vi.getStart().getTime();
            
        h = total / (1000 * 60 * 60);
        h = h % (1000 * 60 * 60);
        m = h / (1000 * 60);

        return h + " Horas, " + m +" Minutos";
    }
    
     public float contarHorasParaStdNum ( Students stdGrp ){
        long total = 0, h = 0 , m = 0;
        
        for ( Visit vi : stdGrp.getVisitCollection() )
            total += vi.getEnd().getTime() - vi.getStart().getTime();
            
        h = total / (1000 * 60 * 60);
        total = total % (1000 * 60 * 60);
        m = total / (1000 * 60);

         if (10 > m) {
             return new Float(h + ".0" + m);
         }
        
        return  new Float (h + "." + m );
    }
   
    
    public BarChartModel grpBarGraf (){
        
        BarChartModel bm = new BarChartModel();
        if (grpActual != null) {

            bm.setTitle("Horas Por Alumno");
            bm.getAxis(AxisType.Y).setLabel("Horas");
            bm.getAxis(AxisType.X).setLabel("Nombre");

            ChartSeries sR = new ChartSeries("Nombre");

            for (Students si : grpActual.getStudentsCollection()) 
                sR.set(si.getName(), contarHorasParaStdNum(si));
            
             bm.addSeries(sR);
        }
        
        return bm;
        
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
