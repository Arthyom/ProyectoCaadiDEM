
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
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;
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
    private List<Students>   stdNoExst, stdExst, stdNoVisi ;
    private String           indxPrf, indxPrd;
    private Teachers         loadedTeacher;
   
    
    private UploadedFile     archivo;
    
    
    @EJB
    private GroupsFacade     fcdGrupos;
    
    @EJB
    private StudentsFacade   fcdEstudints;
    
    @EJB
    private TeachersFacade   fcdProfes;
    
    @EJB
    private PeriodsFacade    fcdPeriods;
    
    @EJB
    private VisitFacade      fcdVisit;
    
    private int grpIndex;
    
    ////////////////////////////////////////////////////////////////////////////
    public String limpiar(){
        this.grpActual = null;
        
        return "/dash?faces-redirect=true";
    }
    
    public List<Groups> listarValidos(){
        
        List <Groups> t = null;
        try{
        t = this.fcdGrupos.getEm().createNamedQuery("Groups.findValidos").getResultList();
        }
        catch(Exception ex){
            ;
        }
        return t;
    }
    
    public List<Groups> listarItems () {
     List <Groups> t = null;
        try{
        t = this.fcdGrupos.getEm().createNamedQuery("Groups.findValidos").getResultList();
        }
        catch(Exception ex){
            ;
        }
        return t;
    }
    
    public List<Students> listarItemsFromActual () {
       
        List<Students> g = new ArrayList();

        if (this.grpActual != null) {
            List<Students> s = (List<Students>) this.grpActual.getStudentsCollection();
            for (Students si : s) {
                if (si.getVisible()) {
                    g.add(si);
                }
            }

            return g;
        }

        return null;
    }
    
    public List<Students> listarStdSinGrupo () {
        
        List<Students> estTotal  = fcdEstudints.findAll();
        List<Students> estLibrs  = fcdEstudints.findAll();
        List<Groups>   grpTotal = fcdGrupos.findAll();               
        
        for( int i = 0 ; i < estTotal.size() ; i ++ ){
            if( !estTotal.get(i).getVisible() ){
                estTotal.remove( estTotal.get(i) );
                estLibrs.remove( estLibrs.get(i) );
            }
        }

        for( Students ei : estTotal )
            for( Groups gi : grpTotal )
                for( Students eii : gi. getStudentsCollection() )
                    if( eii.getNua().equals(ei.getNua()) || !ei.getVisible() )
                        estLibrs.remove(ei);
                return estLibrs;
    }
    
    public int contarStdConGrupo () {
        
        int a = 0;
       List<Students> estTotal  = fcdEstudints.findAll();
        List<Groups>   grpTotal = fcdGrupos.findAll();       
        List<Students> estLibrs  = fcdEstudints.findAll();
        
        for( Students ei : estTotal )
            for( Groups gi : grpTotal )
                for( Students eii : gi.getStudentsCollection() )
                     if( eii.getNua().equals(ei.getNua()) && ei.getVisible() )
                        a++;

        return a;
    }
    
        public List<Students> contarStdValidosConGrupo () {
        
        List<Students> s = (List<Students>) this.grpActual.getStudentsCollection();
        
        for( int i = 0 ; i < s.size() ; i ++ )
            if( !s.get(i).getVisible() )
                s.remove(i);
        
        this.grpActual.setStudentsCollection(stdsSlct);
        this.fcdGrupos.edit(grpActual);
       
        return (List<Students>) grpActual.getStudentsCollection();
    }
    
    public String eliminarStdGrupo ( Students s){
        this.grpActual.getStudentsCollection().remove(s);
        this.fcdGrupos.edit(this.grpActual);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Asignacion", "Se ha Eliminado al estudiante" ));
        return "asignacion?faces-redirect=true";
    }
         
    public String borrarSeleccionado () {
        grpActual.setVisible(Boolean.FALSE);
        grpActual.getStudentsCollection().clear();
        fcdGrupos.edit(grpActual);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
       
    public String borrarSeleccionados () {
        for( Groups si : grpsSeleccionados ){
            si.setVisible(Boolean.FALSE);
            si.getStudentsCollection().clear();
            fcdGrupos.edit(si);
        }
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "listar?faces-redirect=true";
    }
    
    public String guardarItem () {


        Periods   pn = fcdPeriods.find(1);
        Teachers  tn = fcdProfes.find(this.indxPrf);
        List<Students> ncs = new ArrayList <> () ;

        grpNuevo.setVisible(Boolean.TRUE);
        grpNuevo.setEmployeeNumber(tn);
        grpNuevo.setPeriodId(pn);
        
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
            int idp = this.fcdPeriods.conseguirPrdActual().getId();
            String idAlt = this.fcdPeriods.conseguirPrdActual().getIdAlterno();
            if (grp.getPeriodId().getId() == this.fcdPeriods.conseguirPrdActual().getId() || 
                grp.getPeriodId().getIdAlterno().equals(idAlt)
               ) {
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
        }
        return "-";
    }

     public String crearDirectori() throws UnknownHostException {
        String  s= "../../imagenes/";
        File ruta = new File(s);
        if(ruta.exists())
            return s;
        ruta.mkdir();
        return s;
}
    
    public void crearPDF() throws IOException, BadElementException, DocumentException {
        
        if( this.grpActual != null ){
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        String profName = grpActual.getEmployeeNumber().getName();
        String proFFname = grpActual.getEmployeeNumber().getFirstLastName();
        String proFSname = grpActual.getEmployeeNumber().getSecondLastName();
        String grpSlctd = grpActual.getLearningUnit() + " " + grpActual.getLevel() + grpActual.getIdentifier();
        String grpIds =   grpActual.getLevel() +grpActual.getIdentifier();
        int aRegs = grpActual.getStudentsCollection().size();

        List<Students> grs = (List<Students>) grpActual.getStudentsCollection();

        ec.responseReset();
        ec.setResponseContentType("application/pdf");
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "RG" + grpSlctd + profName + proFFname + ".pdf" + "\"");
        OutputStream output = ec.getResponseOutputStream();

        Document nd = new Document(PageSize.LETTER);
        PdfPTable nt = new PdfPTable(5);
        nt.setWidths(new float[]{4,8,20,20,20});
       

        String ruta = this.crearDirectori();
        String sP = ruta + grpIds + this.grpActual.getEmployeeNumber().getName()+  this.grpActual.getEmployeeNumber().getFirstLastName() + this.grpActual.getEmployeeNumber().getSecondLastName() + "grafPie.jpeg";
        String sB = ruta + grpIds + this.grpActual.getEmployeeNumber().getName()+  this.grpActual.getEmployeeNumber().getFirstLastName() + this.grpActual.getEmployeeNumber().getSecondLastName() +"grafBar.jpeg";
        
        Image imgP = Image.getInstance(sP);
        Image imgB = Image.getInstance(sB);
        Font hf = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);
        Font th = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD, BaseColor.BLUE);

        Phrase contenido = new Phrase();

        PdfWriter.getInstance(nd, output);
        nd.open();

        // primer parrafo
        nd.add(new Paragraph("Reporte De Grupo Para: "));
        contenido.setFont(th);
        contenido.add(grpSlctd + ",  Profesor: " + profName + " " + proFFname + " " + proFSname);
        nd.add(contenido);
        
        nd.add( new Paragraph("Alumnos Registrados: " + aRegs));

        nd.add( new Paragraph(" "));

        nt.addCell(new Phrase("#", hf));
        nt.addCell(new Phrase("NUA", hf));
        nt.addCell(new Phrase("Nombre", hf));
        nt.addCell(new Phrase("Apellidos", hf));
        nt.addCell(new Phrase("Total de Horas", hf));

        int i = 1;
        // para cada estudiante en la coleccion 
        for (Students sti : grs) {
            nt.addCell( String.valueOf(i));
            nt.addCell(sti.getNua());
            nt.addCell(sti.getName());
            nt.addCell(sti.getFirstLastName() + " " +sti.getSecondLastName());
            nt.addCell(contarHorasParaStd(sti));
            i++;
        }

        nd.add(nt);
        nd.add(imgP);
        nd.add(imgB);
        nd.close();
        fc.responseComplete();
        }

        
    }
    
    public String contarHorasParaStd ( Students stdGrp ){
        long total = 0, h = 0 , m = 0;
        
        Periods p = this.fcdPeriods.conseguirPrdActual();

       
        List<Visit> vs = this.fcdVisit.getEm().createNamedQuery("Visit.findByNuaByPeriod")
                .setParameter( "stdNua", stdGrp.getNua())
                .setParameter( "idP", p.getId()) 
                .getResultList();
        
        for ( Visit vi : vs )
            total += vi.getEnd().getTime() - vi.getStart().getTime();
            
        h = total / (1000 * 60 * 60);
        total = total % (1000 * 60 * 60);
        m = total / (1000 * 60);

        return h + " Horas, " + m +" Minutos";
    }
    
     public float contarHorasParaStdNum ( Students stdGrp ){
        long total = 0, h = 0 , m = 0;
        
        Periods p = this.fcdPeriods.conseguirPrdActual();

       
        List<Visit> vs = this.fcdVisit.getEm().createNamedQuery("Visit.findByNuaByPeriod")
                .setParameter( "stdNua", stdGrp.getNua())
                .setParameter( "idP", p.getId()) 
                .getResultList();
        
        for ( Visit vi : vs )
            total += vi.getEnd().getTime() - vi.getStart().getTime();
            
        h = total / (1000 * 60 * 60);
        total = total % (1000 * 60 * 60);
        m = total / (1000 * 60);

         if (10 > m) {
             return new Float(h + ".0" + m);
         }
        
        return  new Float (h + "." + m );
    }
   
     public PieChartModel grpPieGraf() throws IOException {
                      int reading = 0, listening = 0, grammar = 0, speaking = 0, a4 = 0, a5 = 0;
             DefaultPieDataset pd = new DefaultPieDataset();
             PieChartModel mdn = new PieChartModel();
         if (this.grpActual != null) {
             
             String sk[] = {"Reading", "Listening", "Grammar", "Speaking","Vocabulary", "Writing"};
             JFreeChart gpPie;
             String ruta = crearDirectori();
              String grpIds = grpActual.getLevel() +grpActual.getIdentifier();
              String nombre = ruta  + grpIds+ this.grpActual.getEmployeeNumber().getName()+ this.grpActual.getEmployeeNumber().getFirstLastName() +  this.grpActual.getEmployeeNumber().getSecondLastName();

             // iterar para todos los estudiantes del grupo
             for (Students st : this.grpActual.getStudentsCollection()) {

                 reading += this.fcdVisit.getEm().createNamedQuery("Visit.findBySkillByNUA").
                         setParameter("nua", st.getNua()).setParameter("skill", sk[0]).getResultList().size();

                 listening += this.fcdVisit.getEm().createNamedQuery("Visit.findBySkillByNUA").
                         setParameter("nua", st.getNua()).setParameter("skill", sk[1]).getResultList().size();

                 grammar += this.fcdVisit.getEm().createNamedQuery("Visit.findBySkillByNUA").
                         setParameter("nua", st.getNua()).setParameter("skill", sk[2]).getResultList().size();

                 speaking += this.fcdVisit.getEm().createNamedQuery("Visit.findBySkillByNUA").
                         setParameter("nua", st.getNua()).setParameter("skill", sk[3]).getResultList().size();
                 
                 a4 += this.fcdVisit.getEm().createNamedQuery("Visit.findBySkillByNUA").
                         setParameter("nua", st.getNua()).setParameter("skill", sk[4]).getResultList().size();

                 a5 += this.fcdVisit.getEm().createNamedQuery("Visit.findBySkillByNUA").
                         setParameter("nua", st.getNua()).setParameter("skill", sk[5]).getResultList().size();

             }

             pd.setValue(sk[0], reading);
             pd.setValue(sk[1], listening);
             pd.setValue(sk[2], grammar);
             pd.setValue(sk[3], speaking);
             pd.setValue(sk[4], a4);
             pd.setValue(sk[5], a5);

             gpPie = ChartFactory.createPieChart(
                     "Habilidades Más Trabajadas", pd, true, true, false);

             PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                     "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));

             PiePlot pPie = (PiePlot) gpPie.getPlot();
             pPie.setCircular(false);
             pPie.setSimpleLabels(true);
             pPie.setLabelGenerator(gen);

             ChartUtils.saveChartAsJPEG(new File( nombre + "grafPie.jpeg"), gpPie, 550, 480);

             mdn.setTitle("Habilidades Más Trabajadas");
             mdn.setLegendPosition("w");
             mdn.setShowDataLabels(true);

             mdn.set(sk[0], reading);
             mdn.set(sk[1], listening);
             mdn.set(sk[2], grammar);
             mdn.set(sk[3], speaking);
             mdn.set(sk[4], a4);
             mdn.set(sk[5], a5);
             
         }
         return mdn;
    }
     
    
    public BarChartModel grpBarGraf() throws IOException {
        String sk[] = {"Reading", "Listening", "Grammar", "Speaking","Vocabulary", "Writing"};
        JFreeChart gpPie, gpBar;
        String ruta = crearDirectori();
        if( this.grpActual != null ){
        String grpIds = grpActual.getLevel() +grpActual.getIdentifier();
        String nombre = ruta  + grpIds + this.grpActual.getEmployeeNumber().getName()+ this.grpActual.getEmployeeNumber().getFirstLastName() +  this.grpActual.getEmployeeNumber().getSecondLastName();

        BarChartModel bm = new BarChartModel();
        bm.getAxis(AxisType.X).setTickAngle(90);
        if (grpActual != null) {

            bm.setTitle("Horas Por Alumno");
            bm.getAxis(AxisType.Y).setLabel("Horas");
            bm.getAxis(AxisType.X).setLabel("Nombre");

            DefaultCategoryDataset dt = new DefaultCategoryDataset();
            for (Students s : grpActual.getStudentsCollection()) 
                dt.addValue(contarHorasParaStdNum(s), "Total Horas",s.getNua());
            
            gpBar = ChartFactory.createBarChart(
                    "Horas Por Alumno",
                    "NUA", "Horas",
                    dt, PlotOrientation.VERTICAL,
                    false, true, false);
            
            gpBar.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.DOWN_90);

            ChartUtils.saveChartAsJPEG(new File( nombre  + "grafBar.jpeg"), gpBar, 550, 480);
            ChartSeries sR = new ChartSeries("Nombre");

            for (Students si : grpActual.getStudentsCollection()) 
                sR.set(si.getNua(), contarHorasParaStdNum(si));
           
            bm.addSeries(sR);
        }
        return bm;
        }
        return null;
    }
    
    public void mensajeCargar () throws IOException, ParseException{
        
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext ct = FacesContext.getCurrentInstance();

            
        try{
            //barrerArchivoXl();
            barrerArchivoXl();
            mostrarPanel("dlgCargar");
        }
           
       catch(Exception exp){
            ct.addMessage(null,
                    new FacesMessage("Error: ", "El archivo no tiene el formato correcto"));
       }
    }
    
      // analizar el archivo json seleccinado
    public void mostrarPanel ( String panel ){
        
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext ct = FacesContext.getCurrentInstance();
        context.execute("PF('"+panel+"').show();");
    }
    
    public void barrerArchivoXl () throws IOException, ParseException{
      
            this.stdExst       = new ArrayList<Students>();
            this.stdNoExst     = new ArrayList<Students>();
            this.stdNoVisi     = new ArrayList<Students>();
            
            XSSFWorkbook nb = new XSSFWorkbook(archivo.getInputstream());
            XSSFSheet nh = nb.getSheetAt(0);

            // conseguir la primera columna para representar al grupo
            
            String gIdTx = nh.getRow(0).getCell(0).getRichStringCellValue().getString();
            String gLuTx = nh.getRow(0).getCell(1).getRichStringCellValue().getString();
            String gLvTx = String.valueOf((int)nh.getRow(0).getCell(2).getNumericCellValue());
            //String gEmTx = String.valueOf((int)nh.getRow(0).getCell(4).getNumericCellValue());
            //int gPId = (int) nh.getRow(0).getCell(5).getNumericCellValue();

            
            // buscar el grupo en la base de datos 
            
            this.grpNuevo = new Groups (0, gLuTx, gLvTx, gIdTx);
            this.grpNuevo.setPeriodId(this.fcdPeriods.conseguirPrdActual());
            this.grpNuevo.setStudentsCollection( new ArrayList<Students>() );
            

            // conseguir datos del profesor
             XSSFRow rowTeacher = nh.getRow(2);
             String teacherNue  = String.valueOf( (int)rowTeacher.getCell(0).getNumericCellValue() );
             String teacherName = rowTeacher.getCell(1).getRichStringCellValue().getString();
             String teacherFLastName = rowTeacher.getCell(2).getRichStringCellValue().getString();
             String teacherSLastName = rowTeacher.getCell(3).getRichStringCellValue().getString();
             String teacherGender    = rowTeacher.getCell(4).getRichStringCellValue().getString();
             String teacherEmail     = rowTeacher.getCell(5).getRichStringCellValue().getString();
             
            loadedTeacher = this.fcdProfes.find(teacherNue);
             if(loadedTeacher == null ){
                 loadedTeacher = new Teachers(teacherNue, teacherName, teacherFLastName, teacherSLastName, teacherGender);
                 loadedTeacher.setVisible(Boolean.TRUE);
                 loadedTeacher.setEmail(teacherEmail);
                 this.fcdProfes.create(loadedTeacher);
             }
             else{
                 loadedTeacher.setEmployeeNumber(teacherNue);
                 loadedTeacher.setName(teacherName);
                 loadedTeacher.setFirstLastName(teacherFLastName);
                 loadedTeacher.setSecondLastName(teacherSLastName);
                 loadedTeacher.setGender(teacherGender);
                 loadedTeacher.setEmail(teacherEmail);
                 loadedTeacher.setVisible(Boolean.TRUE);
                 this.fcdProfes.edit(loadedTeacher);
             }
             
            // saltarse una columna 
            // barrer todos los alumnos 
            for (int nr = 4; nr < nh.getLastRowNum() + 1; nr++) {

                XSSFRow r = nh.getRow(nr);
                XSSFCell cn = r.getCell(0); // nua
                XSSFCell cN = r.getCell(1); // nombre
                XSSFCell cAP = r.getCell(2); // apellido P
                XSSFCell cAM = r.getCell(3); // apellido M
                XSSFCell cG = r.getCell(4); // genero

                String cnV = String.valueOf((int) cn.getNumericCellValue()); 
                String cNv = cN.getRichStringCellValue().getString();
                String cAPv = cAP.getRichStringCellValue().getString();
                String cAMv = cAM.getRichStringCellValue().getString();
                String cGV = cG.getRichStringCellValue().getString();
                Date   cFNac; String cFnacS;
                switch(r.getCell(5).getCellType()){
                    case Cell.CELL_TYPE_NUMERIC:
                        cFNac =  r.getCell(5).getDateCellValue();
                        cFnacS = new SimpleDateFormat("dd/MM/yyyy").format(cFNac);
                        cFNac = new SimpleDateFormat("dd/MM/yyyy").parse(cFnacS);
                    break;
                    
                    default:
                        cFnacS =  r.getCell(5).getRichStringCellValue().getString();                              
                        cFNac = new SimpleDateFormat("dd/MM/yyyy").parse(cFnacS );
                    break;
                }
                String cEmil = r.getCell(6).getRichStringCellValue().getString();
                String cPedu = r.getCell(7).getRichStringCellValue().getString();
                

                // verificar si el estudiante no existe persistirlo y agregarlo a la lista de no existentes
                Students st = this.fcdEstudints.find(cnV);
                if (st == null) {
                    st = new Students(cnV, cNv, cAPv, cAMv, cGV);
                    
                    if(cFNac != null)
                        st.setBirthday(cFNac);
                    
                    st.setEmail(cEmil);
                    st.setProgram(cPedu);
                    st.setVisible(Boolean.TRUE);
                    this.stdNoExst.add(st);
                   

                } else // meterlo en la lista de existentes {
                {
                    st.setName(cNv);
                    st.setFirstLastName(cAPv);
                    st.setSecondLastName(cAMv);
                    st.setGender(cGV);
                    st.setEmail(cEmil);
                    if(cFNac != null)
                        st.setBirthday(cFNac);
                    st.setProgram(cPedu);
                    if( !st.getVisible() )
                    {
                        st.setVisible(Boolean.TRUE);
                        //stdNoVisi.add(st);
                        this.stdExst.add(st);
                    } 
                    else
                        this.stdExst.add(st);
                }
            }
        
          
    }
    
    public boolean grpContStd ( Students st ){
        List<Groups> g = this.listarValidos();
        
        for( Groups gi : g ){
           List<Students> ss = (List<Students>) gi.getStudentsCollection();
           for(Students si : ss)
               if( si.getNua().equals(st.getNua() ) )
                 return true;
        }
        
            return false;
                
    }
    
    public String desconectarTodo (){
        List<Periods> ps = this.fcdPeriods.findAll();
        for(Periods p : ps){
            List<Groups>gs = (List<Groups>) p.getGroupsCollection();
            for( Groups g : gs ){
                g.getStudentsCollection().clear();
                this.fcdGrupos.edit(g);
            }
        }
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext ct = FacesContext.getCurrentInstance();

        ct.addMessage(null,
                new FacesMessage("Agregar: ", "Estudiantes Liberados Correctamente"));
        return "listar?faces-redirect=true";
    }
    
    public void desconectarStdOfGrp(Students std){
        List<Periods> ps = this.fcdPeriods.findAll();
        
        for( int i = 0 ; i < ps.size() ; i ++ ){
            List<Groups> gs = (List<Groups>)ps.get(i).getGroupsCollection();
            for( int j = 0 ; j < gs.size() ; j ++ ){
                List<Students>ss = (List<Students>)gs.get(j).getStudentsCollection();
                for(int k = 0 ; k < ss.size() ; k++){
                    if( ss.get(k).getNua().equals( std.getNua()) ){
                        ss.remove(k);
                        this.fcdGrupos.edit(gs.get(j));
                    }
                }
            }
        } 
    }
    
    public String agregarAutomatico(){

        if( fcdProfes.find(loadedTeacher.getEmployeeNumber()) == null )
            fcdProfes.create(loadedTeacher);
        else
            fcdProfes.edit(loadedTeacher);
        
        for (Students s : stdNoExst) {
            this.desconectarStdOfGrp(s);
            if (!this.grpContStd(s)) {
                fcdEstudints.create(s);
                grpNuevo.getStudentsCollection().add(s);
            }
        }

       

        for (Students s : stdExst){
            this.desconectarStdOfGrp(s);
            if (!this.grpContStd(s)) {
                this.fcdEstudints.edit(s);
                grpNuevo.getStudentsCollection().add(s);
                
            }
        }

       
       
       grpNuevo.setEmployeeNumber(loadedTeacher);
       grpNuevo.setPeriodId(fcdPeriods.conseguirPrdActual());
       fcdGrupos.create(grpNuevo);
       
       
  
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext ct = FacesContext.getCurrentInstance();

        ct.addMessage(null,
                new FacesMessage("Agregar: ", "Estudiantes Agregados Correctamente"));
        return "listar?faces-redirect=true";
        
    }

    public UploadedFile getArchivo() {
        return archivo;
    }

    public void setArchivo(UploadedFile archivo) {
        this.archivo = archivo;
    }
    
    public void cancelarCargaAutomatica(){
        
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

    public List<Students> getStdNoExst() {
        return stdNoExst;
    }

    public void setStdNoExst(List<Students> stdNoExst) {
        this.stdNoExst = stdNoExst;
    }

    public List<Students> getStdExst() {
        return stdExst;
    }

    public void setStdExst(List<Students> stdExst) {
        this.stdExst = stdExst;
    }

    public Teachers getLoadedTeacher() {
        return loadedTeacher;
    }

    public void setLoadedTeacher(Teachers loadedTeacher) {
        this.loadedTeacher = loadedTeacher;
    }
 
    
    
    
   
}
