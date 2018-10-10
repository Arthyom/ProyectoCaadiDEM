 
package com.ProyectoCaadiDEM.Beans;

import com.ProyectoCaadiDEM.Entidades.Periods;
import com.ProyectoCaadiDEM.Entidades.Students;
import com.ProyectoCaadiDEM.Entidades.Visit;
import com.ProyectoCaadiDEM.Fachadas.PeriodsFacade;
import com.ProyectoCaadiDEM.Fachadas.StudentsFacade;
import com.ProyectoCaadiDEM.Fachadas.VisitFacade;
import com.ProyectoCaadiDEM.Modelos.Visitantes;
import com.itextpdf.awt.DefaultFontMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.inject.Default;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;



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
    
    private JFreeChart      gpPie, gpBar;
    
    private DateFormat      formateador = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss");
    
   
    
    
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
           this.prdActual = fcdPeriodo.conseguirPrdActual();
           this.vstActual.setNua(st);
           this.vstActual.setPeriodId(prdActual);
           
           return;
       }
       
       this.stdActual = null;
       
       
       
    }
   public String verificar () {
      Map<String, Object> mv = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
       if( this.prdActual != null && this.stdActual != null && !mv.containsKey(this.nua)){
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
    
    public String contarHoras(Students stdCont) throws ParseException {
        String total;
        long   tTtl = 0, h= 0, m = 0;

        for (Visit vi : stdCont.getVisitCollection()) 
            if (vi.getEnd() != null && vi.getStart() != null) 
                tTtl += vi.getEnd().getTime() - vi.getStart().getTime();
                
        h = tTtl/(1000*60*60);
        tTtl = tTtl%(1000*60*60);
        m = tTtl/(1000*60);
        
        total = h+" Horas, " + m + " Minutos";
        return total;
    }
    
    public String contarLapso(Visit vstActl) throws ParseException {
  
        long   h= 0, m = 0;

        long delta = vstActl.getEnd().getTime() - vstActl.getStart().getTime();

        h = delta/(1000*60*60);
        delta = delta%(1000*60*60);
        m = delta/(1000*60);
        
        
        return h+" Horas, " + m + " Minutos";
        
    }
    
    
    
    public List<Visit> listarItems2Std ( String nua ) {
        List<Visit> s = this.fcdVisita.visitasParaStd(nua);    
        return s;
    } 
    
    public float contarHorasLista ( List<Visit> listaVisitas ){
        long k = 0, h= 0, m = 0;
        
        for( Visit vi : listaVisitas )
            k += vi.getEnd().getTime() - vi.getStart().getTime();
        
        h = k/(1000*60*60);
        k = k%(1000*60*60);
        m = k/(1000*60);
        
        if(10 > m)
            return new Float(h+".0"+m);
        
        return new Float(h+"."+m);
    }
    
    public PieChartModel crearVisitPieParaStd ( String NUA ) throws IOException{
        String  sk [] = {"Reading", "Listening", "Grammar", "Speaking"};
        
        int rd = this.fcdVisita.visitasParaHblParaStd(sk[0], NUA).size();
        int ls = this.fcdVisita.visitasParaHblParaStd(sk[1], NUA).size();
        int gr = this.fcdVisita.visitasParaHblParaStd(sk[2], NUA).size();
        int sp = this.fcdVisita.visitasParaHblParaStd(sk[3], NUA).size();
        
 
        
        DefaultPieDataset pd = new DefaultPieDataset();      
        PieChartModel mdn = new PieChartModel ();
        
           
        pd.setValue(sk[0], rd);
        pd.setValue(sk[1], ls);
        pd.setValue(sk[2], gr);
        pd.setValue(sk[3], sp);
        
        this.gpPie = ChartFactory.createPieChart(
                "Visitas por Habilidad",  pd, true, true, false );
        
         PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
            "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));

        
        PiePlot pPie = (PiePlot) this.gpPie.getPlot();
        pPie.setCircular(false);
        pPie.setSimpleLabels(true);
        pPie.setLabelGenerator(gen);
        
        ChartUtils.saveChartAsJPEG(new File("/home/frodo/images/"+NUA+"grafPie.jpeg"), gpPie, 360, 450);
        
        mdn.setTitle("Visitas por Habilidad");
        mdn.setLegendPosition("w");
        mdn.setShowDataLabels(true);
     
        
        mdn.set(sk[0], rd);
        mdn.set(sk[1], ls);
        mdn.set(sk[2], gr);
        mdn.set(sk[3], sp);
        
        return mdn;
    }
    
    public BarChartModel crearVisitBarParaStd ( String NUA ) throws IOException{
        String  sk [] = {"Reading", "Listening", "Grammar", "Speaking"};
        
        BarChartModel bm    = new BarChartModel();
    
        bm.setExtender("ext");
        bm.setTitle("Horas Por Habilidad");
        bm.getAxis(AxisType.Y).setLabel("Horas");
        bm.getAxis(AxisType.X).setLabel("Habilidad");
        
        
        ChartSeries sR = new ChartSeries(sk[0]); 

        List<Visit> rd = this.fcdVisita.visitasParaHblParaStd(sk[0], NUA);
        List<Visit> ls = this.fcdVisita.visitasParaHblParaStd(sk[1], NUA);
        List<Visit> gr = this.fcdVisita.visitasParaHblParaStd(sk[2], NUA);
        List<Visit> sp = this.fcdVisita.visitasParaHblParaStd(sk[3], NUA);
        
       
        sR.set(sk[0], contarHorasLista(rd) );
        sR.set(sk[1], contarHorasLista(ls));
        sR.set(sk[2], contarHorasLista(gr));
        sR.set(sk[3], contarHorasLista(sp));
        
        DefaultCategoryDataset dt = new DefaultCategoryDataset( );
        dt.addValue(contarHorasLista(rd), "Habilidad", sk[0]);
        dt.addValue(contarHorasLista(ls), "Habilidad", sk[1]);
        dt.addValue(contarHorasLista(gr), "Habilidad", sk[2]);
        dt.addValue(contarHorasLista(sp), "Habilidad", sk[3]);
     

      this.gpBar = ChartFactory.createBarChart(
         "Horas Por Habilidad", 
         "Habilidad", "Horas", 
         dt, PlotOrientation.VERTICAL,
         false, true, false);

        ChartUtils.saveChartAsJPEG(new File("/home/frodo/images/"+NUA+"grafBar.jpeg"), gpBar, 390, 480);

        bm.addSeries(sR);

        return bm;
    }
    
    public void crearPdfParaStd ( String nombre, String Nua) throws DocumentException, FileNotFoundException, IOException, ParseException{
        
        
        
         FacesContext fc = FacesContext.getCurrentInstance();
         ExternalContext ec = fc.getExternalContext();

         List<Visit> lv = this.listarItems2Std(Nua);
    ec.responseReset(); 
    ec.setResponseContentType("application/pdf"); 
    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "RI"+Nua+nombre+".pdf" + "\""); 

    OutputStream output = ec.getResponseOutputStream();
    
    Document nd = new Document(PageSize.LETTER);
    
    PdfPTable nt = new PdfPTable(4);
 
    Image imgP = Image.getInstance("/home/frodo/images/"+Nua+"grafPie.jpeg");
    Image imgB = Image.getInstance("/home/frodo/images/"+Nua+"grafBar.jpeg");
    
    
    Font hf = new Font( Font.FontFamily.HELVETICA, 15, Font.BOLD);
    Font th = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD, BaseColor.BLUE);
    
        nt.addCell( new Phrase("Inicio",hf) ); nt.addCell(new Phrase("Termino",hf) ); 
        nt.addCell(new Phrase("Duracion",hf) );nt.addCell(new Phrase("Habilidad",hf) );
        
        for (Visit vi : lv) {
            nt.addCell(this.formateador.format(vi.getStart()));
            nt.addCell(this.formateador.format(vi.getEnd()));
            nt.addCell(this.contarLapso(vi));
            nt.addCell(vi.getSkill());
        }
        PdfWriter.getInstance(nd,  output );
        nd.open();
        nd.add( new Paragraph("Reporte Individual para: "));
        nd.add(new Phrase(  Nua +" " + nombre + " "   + lv.get(0).getNua().getFirstLastName() 
                +" "+lv.get(0).getNua().getSecondLastName() +"       Tiempo Total: "+ this.contarHoras( fcdEstudiante.find(Nua)) , th));
        nd.add(nt);
        nd.add(imgP);
        nd.add(imgB);
        nd.close();
      
    fc.responseComplete();
        
    }
    
    public String verificarIp() throws IOException {
        HttpServletRequest req = (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();

        String ip = req.getRemoteAddr();
        if (!"0:0:0:0:0:0:0:1".equals(ip)) {
            return "Visitas/OutLogIn.xhtml";
        }

        return "";
    }
    
    public void pieGrafPdf (){
       
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
