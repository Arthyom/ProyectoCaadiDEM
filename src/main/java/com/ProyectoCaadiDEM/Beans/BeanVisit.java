 
package com.ProyectoCaadiDEM.Beans;

import com.ProyectoCaadiDEM.Entidades.Periods;
import com.ProyectoCaadiDEM.Entidades.Students;
import com.ProyectoCaadiDEM.Entidades.Visit;
import com.ProyectoCaadiDEM.Entidades.Visits;
import com.ProyectoCaadiDEM.Fachadas.PeriodsFacade;
import com.ProyectoCaadiDEM.Fachadas.StudentsFacade;
import com.ProyectoCaadiDEM.Fachadas.VisitFacade;
import com.ProyectoCaadiDEM.Modelos.Visitantes;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Collections.list;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;


import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.primefaces.component.importconstants.ConstantsHashMap;

import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;
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
    
     private DateFormat     formateadorHora = new SimpleDateFormat( "HH:mm");
    
    private UploadedFile    archivo;
 
    private List<Visit>     VisitasCargadas = new ArrayList();
    
    private HashMap<String, Date> hashVisits =  new HashMap<String, Date>();
    
    
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
        this.stdActual = fcdEstudiante.find(this.nua);

        if (stdActual != null && stdActual.getVisible()) {
            // crear una nueva visita, buscar al periodo actual
            this.vstActual = new Visit(2, new Date());
            this.prdActual = fcdPeriodo.conseguirPrdActual();
            this.vstActual.setNua(stdActual);
            this.vstActual.setPeriodId(prdActual);
        } else 
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
            context.addMessage(null, new FacesMessage("Aceptado", "Bienvenid@: " + this.stdActual.getName()));
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
            context.addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR, "Error", "No se ha encontrado el NUA: " + this.nua));
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
        Periods p = this.fcdPeriodo.conseguirPrdActual();

       
        List<Visit> vs = this.fcdVisita.getEm().createNamedQuery("Visit.findByNuaByPeriod")
                .setParameter( "stdNua", stdCont.getNua())
                .setParameter( "idP", p.getId()) 
                .getResultList();
        
        for (Visit vi : vs) 
            if (vi.getEnd() != null && vi.getStart() != null) 
                tTtl += vi.getEnd().getTime() - vi.getStart().getTime();
                
        h = tTtl/(1000*60*60);
        tTtl = tTtl%(1000*60*60);
        m = tTtl/(1000*60);
        
        total = h+" Hora(s), " + m + " Minuto(s)";
        return total;
    }
    
    public String contarLapso(Visit vstActl) throws ParseException {
  
        long   h= 0, m = 0;

        long delta = vstActl.getEnd().getTime() - vstActl.getStart().getTime();

        h = delta/(1000*60*60);
        delta = delta%(1000*60*60);
        m = delta/(1000*60);
        
        
        return h+" Hora(s), " + m + " Minuto(s)";
        
    }
    
    
    
    public List<Visit> listarItems2Std ( String nua ) {
        Periods p = this.fcdPeriodo.conseguirPrdActual();
     
        List<Visit> vs = this.fcdVisita.getEm().createNamedQuery("Visit.findByNuaByPeriod")
                .setParameter( "stdNua", nua )
                .setParameter( "idP", p.getId()) 
                .getResultList();
        return vs;
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
    
    public String crearDirectori() throws UnknownHostException {
        String  s= "../../imagenes/";
        File ruta = new File(s);
        if(ruta.exists())
            return s;
        ruta.mkdir();
        return s;
}

    public PieChartModel crearVisitPieParaStd ( String NUA ) throws IOException{
        String  sk [] = {"Reading", "Listening", "Grammar", "Speaking","Vocabulary", "Writing"};
        
        String ruta = crearDirectori();
        
        Students stN = this.fcdEstudiante.find(NUA);
        
        
        int pid = this.fcdPeriodo.conseguirPrdActual().getId();
        String pidAlt = this.fcdPeriodo.conseguirPrdActual().getIdAlterno();
        
        int rd = this.fcdVisita.visitasParaHblParaStd(sk[0], NUA, pid, pidAlt).size();
        int ls = this.fcdVisita.visitasParaHblParaStd(sk[1], NUA, pid, pidAlt).size();
        int gr = this.fcdVisita.visitasParaHblParaStd(sk[2], NUA, pid, pidAlt).size();
        int sp = this.fcdVisita.visitasParaHblParaStd(sk[3], NUA, pid, pidAlt).size();
        int sr = this.fcdVisita.visitasParaHblParaStd(sk[4], NUA, pid, pidAlt).size();
        int ss = this.fcdVisita.visitasParaHblParaStd(sk[5], NUA, pid, pidAlt).size();
        
 
        
        DefaultPieDataset pd = new DefaultPieDataset();      
        PieChartModel mdn = new PieChartModel ();
        
           
        pd.setValue(sk[0], rd);
        pd.setValue(sk[1], ls);
        pd.setValue(sk[2], gr);
        pd.setValue(sk[3], sp);
        pd.setValue(sk[4], sr);
        pd.setValue(sk[5], ss);
        
        this.gpPie = ChartFactory.createPieChart(
                "Visitas por Habilidad",  pd, true, true, false );
        
         PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
            "{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));

        
        PiePlot pPie = (PiePlot) this.gpPie.getPlot();
        pPie.setCircular(false);
        pPie.setSimpleLabels(true);
        pPie.setLabelGenerator(gen);
        
        ChartUtils.saveChartAsJPEG(new File(ruta+NUA+"grafPie.jpeg"), gpPie, 500, 500);
        
        mdn.setTitle("Visitas por Habilidad");
        mdn.setLegendPosition("w");
        mdn.setShowDataLabels(true);
     
        
        mdn.set(sk[0], rd);
        mdn.set(sk[1], ls);
        mdn.set(sk[2], gr);
        mdn.set(sk[3], sp);
        mdn.set(sk[4], sr);
        mdn.set(sk[5], ss);
        
        return mdn;
    }
    
    public BarChartModel crearVisitBarParaStd ( String NUA ) throws IOException{
        String  sk [] = {"Reading", "Listening", "Grammar", "Speaking","Vocabulary", "Writing"};
        
        BarChartModel bm    = new BarChartModel();
        
        String ruta = crearDirectori();
        
    
        bm.setExtender("ext");
        bm.setTitle("Horas Por Habilidad");
        bm.getAxis(AxisType.Y).setLabel("Horas");
        bm.getAxis(AxisType.X).setLabel("Habilidad");
        
        
        ChartSeries sR = new ChartSeries(sk[0]); 
        
        int pid = this.fcdPeriodo.conseguirPrdActual().getId();
        String pidAlt = this.fcdPeriodo.conseguirPrdActual().getIdAlterno();

        List<Visit> rd = this.fcdVisita.visitasParaHblParaStd(sk[0], NUA, pid, pidAlt);
        List<Visit> ls = this.fcdVisita.visitasParaHblParaStd(sk[1], NUA, pid, pidAlt);
        List<Visit> gr = this.fcdVisita.visitasParaHblParaStd(sk[2], NUA, pid, pidAlt);
        List<Visit> sp = this.fcdVisita.visitasParaHblParaStd(sk[3], NUA, pid, pidAlt);
        List<Visit> sr = this.fcdVisita.visitasParaHblParaStd(sk[4], NUA, pid, pidAlt);
        List<Visit> ssp = this.fcdVisita.visitasParaHblParaStd(sk[5], NUA, pid, pidAlt);
        
       
        sR.set(sk[0], contarHorasLista(rd) );
        sR.set(sk[1], contarHorasLista(ls));
        sR.set(sk[2], contarHorasLista(gr));
        sR.set(sk[3], contarHorasLista(sp));
        sR.set(sk[4], contarHorasLista(sr));
        sR.set(sk[5], contarHorasLista(ssp));
        
        DefaultCategoryDataset dt = new DefaultCategoryDataset( );
        dt.addValue(contarHorasLista(rd), "Habilidad", sk[0]);
        dt.addValue(contarHorasLista(ls), "Habilidad", sk[1]);
        dt.addValue(contarHorasLista(gr), "Habilidad", sk[2]);
        dt.addValue(contarHorasLista(sp), "Habilidad", sk[3]);
        dt.addValue(contarHorasLista(sr), "Habilidad", sk[4]);
        dt.addValue(contarHorasLista(ssp), "Habilidad", sk[5]);
     

      this.gpBar = ChartFactory.createBarChart(
         "Horas Por Habilidad", 
         "Habilidad", "Horas", 
         dt, PlotOrientation.VERTICAL,
         false, true, false);

        ChartUtils.saveChartAsJPEG(new File(ruta+NUA+"grafBar.jpeg"), gpBar, 550, 500);

        bm.addSeries(sR);

        return bm;
    }
    
    public void crearPdfParaStd ( String nombre, String Nua) throws DocumentException, FileNotFoundException, IOException, ParseException{
        
        String ruta = crearDirectori();
        
        
        
         FacesContext fc = FacesContext.getCurrentInstance();
         ExternalContext ec = fc.getExternalContext();

         List<Visit> lv = this.listarItems2Std(Nua);
    ec.responseReset(); 
    ec.setResponseContentType("application/pdf"); 
    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + "RI"+Nua+nombre+".pdf" + "\""); 

    OutputStream output = ec.getResponseOutputStream();
    
    Document nd = new Document(PageSize.LETTER);
    
    PdfPTable nt = new PdfPTable(4);
 
    Image imgP = Image.getInstance(ruta+Nua+"grafPie.jpeg");
    Image imgB = Image.getInstance(ruta+Nua+"grafBar.jpeg");
    
    
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
    
    public void verificarIp() throws IOException {
        final DatagramSocket socket = new DatagramSocket();
        socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
        String ips = socket.getLocalAddress().getHostAddress();
            
        
        HttpServletRequest req = (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        
        String [] p = req.getRequestURL().toString().split("/");
        String s = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() 
                    + "/Students.xhtml";

        String ip = req.getRemoteAddr();
        
        System.out.println("----------------------" +ip);
        System.out.println("----------------------" +ips);

        //|| !"0:0:0:0:0:0:0:1".equals(ip)
        
        switch (ip) {
            case "0:0:0:0:0:0:0:1":case "127.0.0.1":return;
            
            default:
                if (!ips.equals(ip)) 
                    if( p.length < 5 )
                        FacesContext.getCurrentInstance().getExternalContext().redirect(s);
                    else
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/ProyectoCaadiDEM/404.xhtml");
            break;
        }  
    }
    
    public void pieGrafPdf (){
       
    }
    
    public void mensajeCargar () throws IOException, ParseException{
        
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext ct = FacesContext.getCurrentInstance();
            
        try{
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
       
       XSSFWorkbook      nb = new XSSFWorkbook( archivo.getInputstream() );
       XSSFSheet         nh = nb.getSheetAt(0);
       
       // comenzar una transaccion 
       
       for( int nr = nh.getFirstRowNum(); nr<nh.getLastRowNum()+1 ; nr++){
           
           long h = 0, m = 0;
           Visit nv = new Visit();
           // cnseguir celdas 
           XSSFRow   r            = nh.getRow(nr);
           XSSFCell  cNua         = r.getCell(0);
           XSSFCell  cSkill       = r.getCell(1);
           XSSFCell  cLapso       = r.getCell(2);
           XSSFCell  cTotal       = r.getCell(3);
           
           // extraer valores de las celdas
           String nua   = String.valueOf( (int)cNua.getNumericCellValue() );
           String skill = cSkill.getRichStringCellValue().getString();
           
          
           // extraer los valores de los lapsos de tiempo o del tiempo absoluto
           // buscar al estudiante en la base de datos y regresarlo para insertar las horas cargadas
           Students ns = this.fcdEstudiante.find(nua);
           if(ns != null){
               // crear una nueva visita e insertarla
               nv.setVisible(Boolean.TRUE);
               nv.setPeriodId(this.fcdPeriodo.conseguirPrdActual());
               nv.setId(0);
               nv.setNua(ns);
               nv.setSkill(skill);
               if(cLapso != null ){
                     /// conseguir los valores del lapso
                    String fechaTemporal = cLapso.getRichStringCellValue().getString();
                    fechaTemporal = fechaTemporal.replace(" – ", "–");
                    
                    String fechaCortada [] = fechaTemporal.split("–");
                    nv.setStart( this.formateadorHora.parse(fechaCortada[0]) );
                    nv.setEnd(   this.formateadorHora.parse(fechaCortada[1]) );
                }
               else{
                   /// conseguir los valores del total
                   Date valorFechaCelda = cTotal.getDateCellValue();
                   nv.setStart( this.formateadorHora.parse("00:00") );
                   nv.setEnd(   this.formateador.parse("01/01/1970 "+valorFechaCelda.getHours()+":"+ valorFechaCelda.getMinutes()+":00") );                
                }
               
                // guardar la visita para persistirla cuando el usuario acepte el metodo
                this.VisitasCargadas.add(nv);
           }
       }
    }
    
     public String agregarAutomatico(){
         
        // persistir cada una de las visitas 
        for(Visit vsI : this.VisitasCargadas )
            this.fcdVisita.create(vsI);
       
        this.VisitasCargadas.clear();
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext ct = FacesContext.getCurrentInstance();

        ct.addMessage(null,
                new FacesMessage("Agregar: ", "Visitas Cargadas Correctamente"));
        return "/Estudiantes/listar?faces-redirect=true";     
    }
     
    public void cancelarCargaAutomatica (){
        this.VisitasCargadas.clear();
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

    public UploadedFile getArchivo() {
        return archivo;
    }

    public void setArchivo(UploadedFile archivo) {
        this.archivo = archivo;
    }

    public List<Visit> getVisitasCargadas() {
        return VisitasCargadas;
    }

    public void setVisitasCargadas(List<Visit> VisitasCargadas) {
        this.VisitasCargadas = VisitasCargadas;
    }

    public HashMap<String, Date> getHashVisits() {
        return hashVisits;
    }

    public void setHashVisits(HashMap<String, Date> hashVisits) {
        this.hashVisits = hashVisits;
    }
    

    
   
   
   
}
