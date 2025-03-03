/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paq_biometrico;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;

import jxl.Sheet;
import jxl.Workbook;

import org.primefaces.component.editor.Editor;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;


import paq_gestion.ejb.ServicioEmpleado;
import paq_nomina.ejb.ServicioNomina;
import paq_sistema.aplicacion.Pantalla;
import paq_sistema.ejb.ServicioSeguridad;
import framework.aplicacion.TablaGenerica;
import framework.componentes.AutoCompletar;
import framework.componentes.Boton;
import framework.componentes.Confirmar;
import framework.componentes.Dialogo;
import framework.componentes.Division;
import framework.componentes.Espacio;
import framework.componentes.Etiqueta;
import framework.componentes.Grid;
import framework.componentes.Imagen;
import framework.componentes.PanelTabla;
import framework.componentes.SeleccionTabla;
import framework.componentes.Tabla;
import framework.componentes.Upload;
/**
 *
 * @author DELL-USER
 */
public class pre_calendario_horario_notificacion extends Pantalla {
	@EJB
	private ServicioNomina ser_empleado = (ServicioNomina) utilitario.instanciarEJB(ServicioNomina.class);
	@EJB
	private ServicioSeguridad ser_seguridad = (ServicioSeguridad) utilitario.instanciarEJB(ServicioSeguridad.class);
	@EJB
	private ServicioEmpleado ser_empleado1 = (ServicioEmpleado) utilitario.instanciarEJB(ServicioEmpleado.class);
    private Tabla tab_horario_novedades = new Tabla();
    private Tabla tab_horario_consulta = new Tabla();

    private Tabla tab_horario = new Tabla();
	private AutoCompletar aut_empleado = new AutoCompletar();
	private AutoCompletar aut_anio_mes = new AutoCompletar();
	int ide_gemes=0,ide_geani=0,ultimoDia=0;
	String ide_gtemp="",ide_gtempxx="",tipo_perfil="",area="",ide_asjei="",ide_geare,jefe_padre,cargo_padre,mes="",anio="",meses="",anios="",mesEditar="",anioEditar="",empleado="";
    private StringBuilder str_ide_empleado_mensual=new StringBuilder();
    StringBuilder ide_empleados_crear=new StringBuilder(); 
    StringBuilder ide_empleados=new StringBuilder();
	TablaGenerica tabJefeInmediato=null;
	private Upload upl_archivo = new Upload();
    private SeleccionTabla sel_mes_importarTotales= new SeleccionTabla();
    private SeleccionTabla sel_anio_importarTotales= new SeleccionTabla();
    private SeleccionTabla sel_empleado_importarTotales= new SeleccionTabla();
	private Dialogo dia_importar_total = new Dialogo();
	private Dialogo dia_valida_empleado_total = new Dialogo();
	private Etiqueta eti_num_nomina = new Etiqueta();
	private Editor edi_mensajes = new Editor();
	private Tabla tab_emp_total = new Tabla();
	private List<String[]> lis_importa = null; // Guardo los empleados y el
	private List<String[]> lis_turno = null; // Guardo los empleados y el
	private List<String[]> lis_empleado = null; // Guardo los empleados y el

	private Grid grid_tabla_emp_total = new Grid();
	private List<String[]> lis_importadia1 = null; // Guardo los empleados y el
	private Confirmar con_guardar = new Confirmar();
	private Etiqueta eti_dia = new Etiqueta();
	private Etiqueta eti_turno = new Etiqueta();
	public pre_calendario_horario_notificacion() {
    TablaGenerica  tab_ingresos=null;
    	
		ide_gtempxx = ser_seguridad.getUsuario(utilitario.getVariable("ide_usua")).getValor("ide_gtemp");
    	tabJefeInmediato=utilitario.consultar("SELECT asjei.ide_asjei, asjei.ide_gtemp, asjei.ide_geedp, asjei.tipo_asjei, asjei.ide_geare, asjei.activo_asjei,area.detalle_geare,ide_gtemp_padre_asjei,cargo_padre_asjei  "
    			+ "FROM asi_jefe_inmediato  asjei "
    			+ "left join gen_area area on area.ide_geare=asjei.ide_geare "
    			+ "where ide_gtemp="+ide_gtempxx);
    	
    	if (tabJefeInmediato.getValor("tipo_asjei")==null || tabJefeInmediato.getValor("tipo_asjei").equals("") || tabJefeInmediato.getValor("tipo_asjei").isEmpty()) {
    		utilitario.agregarMensaje("No se puede continuar", "No contiene los permisos necesarios. Pongase en contacto con el Adminisrador");
    		return;
    	}else {
    		tipo_perfil=tabJefeInmediato.getValor("tipo_asjei");
        	area=tabJefeInmediato.getValor("detalle_geare");
        	TablaGenerica tabEmpleadoXJefeInmediato=null;
           	
        	if(tipo_perfil.equals("1")){
        		ide_asjei=""; 
   				TablaGenerica tab_ide_geedp=utilitario.consultar("SELECT asjei.ide_asjei, asjei.ide_gtemp, asjei.ide_geedp, asjei.tipo_asjei, asjei.ide_geare, asjei.activo_asjei,area.detalle_geare  "
   		    			+ "FROM asi_jefe_inmediato  asjei "
   		    			+ "left join gen_area area on area.ide_geare=asjei.ide_geare ");
				if (tab_ide_geedp.getTotalFilas()>0) {
					for (int j = 0; j < tab_ide_geedp.getTotalFilas(); j++) {
						if (j==(tab_ide_geedp.getTotalFilas()-1)) {
							ide_asjei+=tab_ide_geedp.getValor(j,"ide_asjei");
						}else{
							ide_asjei+=tab_ide_geedp.getValor(j,"ide_asjei");
							ide_asjei+=",";

						}
					}
        		
	    	 }
        		ide_geare=tabJefeInmediato.getValor("ide_geare"); 
        		jefe_padre=tabJefeInmediato.getValor("ide_gtemp_padre_asjei");
        		cargo_padre=tabJefeInmediato.getValor("cargo_padre_asjei");
        		
        		
                tabEmpleadoXJefeInmediato=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp "
          				+ "FROM asi_empleado_jefe_inmediato ");
          			//	+ "where ide_asjei="+tabJefeInmediato.getValor("ide_asjei"));
      
                 String int_num_col_idegetempmensual="";
                 for (int i = 0; i < tabEmpleadoXJefeInmediato.getTotalFilas(); i++) {
                	 int_num_col_idegetempmensual=tabEmpleadoXJefeInmediato.getValor(i, "IDE_GTEMP");
                	 if(str_ide_empleado_mensual.toString().isEmpty()==false){
                		 str_ide_empleado_mensual.append(",");
                	 }
                	 str_ide_empleado_mensual.append(int_num_col_idegetempmensual);
                 }
        		
        		
        		
        	}
        	
        	if(tipo_perfil.equals("2")){
        		ide_asjei=tabJefeInmediato.getValor("ide_asjei"); 
        		ide_geare=tabJefeInmediato.getValor("ide_geare"); 
        		jefe_padre=tabJefeInmediato.getValor("ide_gtemp_padre_asjei");
        		cargo_padre=tabJefeInmediato.getValor("cargo_padre_asjei");
        		
        		
        	       tabEmpleadoXJefeInmediato=utilitario.consultar("SELECT ide_emjei, ide_asjei, ide_gtemp "
             				+ "FROM asi_empleado_jefe_inmediato "
             				+ "where ide_asjei="+tabJefeInmediato.getValor("ide_asjei"));
         
                    String int_num_col_idegetempmensual="";
                    for (int i = 0; i < tabEmpleadoXJefeInmediato.getTotalFilas(); i++) {
                   	 int_num_col_idegetempmensual=tabEmpleadoXJefeInmediato.getValor(i, "IDE_GTEMP");
                   	 if(str_ide_empleado_mensual.toString().isEmpty()==false){
                   		 str_ide_empleado_mensual.append(",");
                   	 }
                   	 str_ide_empleado_mensual.append(int_num_col_idegetempmensual);
                    }
            		
        		
	    	 }
        
		
    	}
    		
		Etiqueta eti_anio_mes=new Etiqueta("Seleccione Anio-Mes:");
		bar_botones.agregarComponente(eti_anio_mes);
		aut_anio_mes.setId("aut_anio_mes");
		aut_anio_mes.setAutoCompletar(obtenerPeriodo());		
		aut_anio_mes.setMetodoChange("filtrarHorarioEmpleado");
		bar_botones.agregarComponente(aut_anio_mes);
    	
    	aut_empleado.setId("aut_empleado");
		String str_sql_emp=empleadoNovedadHorario("1",0,0,ide_asjei,tipo_perfil);
		aut_empleado.setAutoCompletar(str_sql_emp);		
		aut_empleado.setMetodoChange("filtrarHorarioEmpleado");

		Etiqueta eti_colaborador=new Etiqueta("Empleado:");
		bar_botones.agregarComponente(eti_colaborador);
		bar_botones.agregarComponente(aut_empleado);


		
		Boton bot_limpiar = new Boton();
		bot_limpiar.setIcon("ui-icon-cancel");
		bot_limpiar.setMetodo("limpiar");
		bar_botones.agregarBoton(bot_limpiar);
		    	

		Boton bot_carga_notificacion = new Boton();
		bot_carga_notificacion.setIcon("ui-icon-cancel");
		bot_carga_notificacion.setValue("Importar Valores");
		bot_carga_notificacion.setTitle("Importar Valores");
		bot_carga_notificacion.setMetodo("abrirDialogoImportar");
		bar_botones.agregarBoton(bot_carga_notificacion);
		
		Boton bot_envio_notificacion = new Boton();
		bot_envio_notificacion.setIcon("ui-icon-cancel");
		bot_envio_notificacion.setValue("Enviar Notificaciones");
		bot_envio_notificacion.setTitle("Enviar Notificaciones");
		bot_envio_notificacion.setMetodo("enviarNotificacion");
		bar_botones.agregarBoton(bot_envio_notificacion);
		    
		
		con_guardar.setId("con_guardar");
		agregarComponente(con_guardar);
    	
    	
		
    	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    	///////////////////////////////IMPORTAR VALORES CALENDARIO//////////////////////////////////////////////////////////////
    	
    	sel_mes_importarTotales.setId("sel_mes_importarTotales");
		sel_mes_importarTotales.setSeleccionTabla("select ide_gemes,detalle_gemes from gen_mes WHERE ide_gemes in(8,9,10,11,12)","IDE_GEMES");
		sel_mes_importarTotales.getTab_seleccion().getColumna("detalle_gemes").setFiltro(true);
		sel_mes_importarTotales.setRadio();
		sel_mes_importarTotales.setTitle("Seleccione Mes Horario");
		gru_pantalla.getChildren().add(sel_mes_importarTotales);
		sel_mes_importarTotales.setWidth("20");
		sel_mes_importarTotales.setHeight("45");
		sel_mes_importarTotales.getBot_aceptar().setMetodo("obtenerMesImportarTotales");
		agregarComponente(sel_mes_importarTotales);
	
		
	 	sel_anio_importarTotales.setId("sel_anio_importarTotales");
    	sel_anio_importarTotales.setSeleccionTabla("select ide_geani,detalle_geani from gen_anio where ide_geani in("+16+")","IDE_GEANI");
    	sel_anio_importarTotales.getTab_seleccion().getColumna("detalle_geani").setFiltro(true);
    	sel_anio_importarTotales.setRadio();
    	sel_anio_importarTotales.setTitle("Seleccione Anio Importar");
		gru_pantalla.getChildren().add(sel_anio_importarTotales);
		sel_anio_importarTotales.setWidth("25");
		sel_anio_importarTotales.setHeight("30");
		sel_anio_importarTotales.getBot_aceptar().setMetodo("obtenerAnioImportarTotales");
		agregarComponente(sel_anio_importarTotales);
	
		sel_empleado_importarTotales.setId("sel_empleado_importarTotales");
		agregarComponente(sel_empleado_importarTotales);
		
		
		
		dia_importar_total.setId("dia_importar_total");
		dia_importar_total.setTitle("VALIDACION TOTAL DE HORAS EXTRA");
		dia_importar_total.setPosition("left");
		dia_importar_total.setHeight("85%");
		dia_importar_total.getBot_aceptar().setRendered(false);
		dia_importar_total.setWidth("50%");
		dia_importar_total.getBot_cancelar().setMetodo("cancelarImportarTotal");
		
								
		Grid gri_cuerpo_total = new Grid();

		Grid gri_impo_total = new Grid();
		gri_impo_total.setColumns(2);

		//gri_impo_total.getChildren().add(new Etiqueta("Todas las Nominas'"));

		Grid gri_tn_total = new Grid();
		gri_tn_total.setColumns(2);

	
		eti_num_nomina.setId("eti_num_nomina");
		eti_num_nomina.setStyle("font-size:8px;");
		gri_tn_total.getChildren().add(eti_num_nomina);
		gri_impo_total.getChildren().add(gri_tn_total);


		gri_impo_total.getChildren().add(new Etiqueta("Seleccione el archivo: "));
		upl_archivo.setId("upl_archivo");
		upl_archivo.setMetodo("validarArchivoTotal");

		upl_archivo.setUpdate("gri_valida_total");
		upl_archivo.setAuto(false);
		upl_archivo.setAllowTypes("/(\\.|\\/)(xls)$/");
		upl_archivo.setUploadLabel("Validar");
		upl_archivo.setCancelLabel("Cancelar Seleccion");

		gri_impo_total.getChildren().add(upl_archivo);
		gri_impo_total.setWidth("100%");

		Grid gri_valida_total = new Grid();
		gri_valida_total.setId("gri_valida_total");
		gri_valida_total.setColumns(3);

		Etiqueta eti_valida_total = new Etiqueta();
		eti_valida_total.setValueExpression("value", "pre_index.clase.upl_archivo.nombreReal");
		eti_valida_total.setValueExpression("rendered", "pre_index.clase.upl_archivo.nombreReal != null");
		gri_valida_total.getChildren().add(eti_valida_total);

		Imagen ima_valida_total = new Imagen();
		ima_valida_total.setWidth("22");
		ima_valida_total.setHeight("22");
		ima_valida_total.setValue("/imagenes/im_excel.gif");
		ima_valida_total.setValueExpression("rendered", "pre_index.clase.upl_archivo.nombreReal != null");
		gri_valida_total.getChildren().add(ima_valida_total);

		edi_mensajes.setControls("");
		edi_mensajes.setId("edi_mensajes");
		edi_mensajes.setStyle("overflow:auto;");
		edi_mensajes.setWidth(dia_importar_total.getAnchoPanel() - 15);
		edi_mensajes.setDisabled(true);
		gri_valida_total.setFooter(edi_mensajes);

		gri_cuerpo_total.setStyle("width:" + (dia_importar_total.getAnchoPanel() - 5) + "px;");
		gri_cuerpo_total.setMensajeInfo("Esta opcion  permite subir valores a un rubro a partir de un archivo xls");
		gri_cuerpo_total.getChildren().add(gri_impo_total);
		gri_cuerpo_total.getChildren().add(gri_valida_total);
		gri_cuerpo_total.getChildren().add(edi_mensajes);
		gri_cuerpo_total.getChildren().add(new Espacio("0", "10"));

		dia_importar_total.setDialogo(gri_cuerpo_total);
		dia_importar_total.setDynamic(false);

		agregarComponente(dia_importar_total);

		
		
		
		dia_valida_empleado_total.setId("dia_valida_empleado_total");
		dia_valida_empleado_total.getBot_aceptar().setMetodo("aceptarImportarTotal");
		dia_valida_empleado_total.getBot_cancelar().setMetodo("cancelarImportarTotal");
		dia_valida_empleado_total.setModal(false);
		dia_valida_empleado_total.setPosition("right");
		dia_valida_empleado_total.setTitle("Colaboradores encontrados en el archivo");
		dia_valida_empleado_total.setWidth("50%");
		dia_valida_empleado_total.setHeight("85%");
		
		
	  tab_emp_total.setId("tab_emp_total");
	  tab_emp_total.setSql("SELECT EMP.ide_gtemp, EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   "
			  		+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			  		+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  "
			  		+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES, "
			  		+ "0 as DIA, "
			  		+ "0 as TURNO "
					+ "FROM gth_empleado EMP  "
			  		+ "where EMP.ide_gtemp=-1");
	  tab_emp_total.setCampoPrimaria("IDE_GTEMP");

	  tab_emp_total.setRows(15);
	  tab_emp_total.setLectura(true);
	  tab_emp_total.dibujar();

		
	  eti_dia.setId("eti_dia1");
	  eti_dia.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	  eti_turno.setId("eti_dia2");
	  eti_turno.setStyle("font-size:10px;font-weight: bold; width: 130px;");
	 	grid_tabla_emp_total.setStyle("width:" + (dia_valida_empleado_total.getAnchoPanel() - 5) + "px; height:" + dia_valida_empleado_total.getAltoPanel() + "px;overflow:auto;display:block;");
		dia_valida_empleado_total.setDialogo(grid_tabla_emp_total);
		dia_valida_empleado_total.setDynamic(false);

		agregarComponente(dia_valida_empleado_total);
		
		
		
		
		
		
		

    	
    	tab_horario_novedades.setId("tab_horario_novedades");
		tab_horario_novedades.setTabla("asi_horario_mes_notificaciones", "ide_ashmn", 1);
		
		tab_horario_novedades.getColumna("ide_ashmn").setNombreVisual("CODIGO");
		tab_horario_novedades.getColumna("IDE_GTEMP").setCombo("select EMP.IDE_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
  			  "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
  			  "EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
  			  "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "+
  			  "FROM GTH_EMPLEADO EMP");
		tab_horario_novedades.getColumna("IDE_GTEMP").setLectura(true);
		
		
		tab_horario_novedades.getColumna("fecha_ashmn").setValorDefecto(utilitario.getFechaActual());
		tab_horario_novedades.getColumna("fecha_ashmn").setNombreVisual("FECHA SOLICITUD");
		tab_horario_novedades.getColumna("fecha_ashmn").setRequerida(true);     
		tab_horario_novedades.getColumna("fecha_ashmn").setLongitud(25);
		tab_horario_novedades.getColumna("fecha_ashmn").setMetodoChange("cambiarFecha");

		tab_horario_novedades.getColumna("ide_astur").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
		tab_horario_novedades.getColumna("ide_astur").setLongitud(200);
		tab_horario_novedades.getColumna("ide_astur").setNombreVisual("TURNO");

		tab_horario_novedades.getColumna("IDE_GEMES").setVisible(false);
		tab_horario_novedades.getColumna("IDE_GEANI").setVisible(false);
		tab_horario_novedades.getColumna("ide_usua").setLectura(true);
		tab_horario_novedades.getColumna("ide_usua").setCombo("select ide_usua,nick_usua  "
               		+ "from sis_usuario  ");
		tab_horario_novedades.getColumna("activo_ashmn").setCheck();
		tab_horario_novedades.getColumna("activo_ashmn").setValorDefecto("true");
		tab_horario_novedades.getColumna("activo_ashmn").setNombreVisual("ACTIVO");
		tab_horario_novedades.getColumna("activo_ashmn").setLectura(true);
		tab_horario_novedades.getColumna("notificacion_ashmn").setCheck();
		tab_horario_novedades.getColumna("notificacion_ashmn").setValorDefecto("false");
		tab_horario_novedades.getColumna("notificacion_ashmn").setLectura(true);
		tab_horario_novedades.getColumna("notificacion_ashmn").setNombreVisual("NOTIFICACION CAMBIO");
		tab_horario_novedades.getColumna("ide_asjei").setVisible(false);
	   	tab_horario_novedades.getGrid().setColumns(4);
	   	tab_horario_novedades.setCondicion("ide_ashmn=-1");
		tab_horario_novedades.dibujar();
		PanelTabla pat_panel=new PanelTabla();
		pat_panel.setPanelTabla(tab_horario_novedades);
		pat_panel.setMensajeWarn("REGISTRO DE NOVEDADES EN HORARIO MENSUAL");
		
		
		
		
		tab_horario_consulta.setId("tab_horario_consulta");
		tab_horario_consulta.setSql("SELECT mes.ide_ashmn,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
  			  "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
  			  "EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
  			  "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, mes.fecha_ashmn, turno.nom_astur || '' || turno.descripcion_astur as descripcion_astur "
				+ "FROM asi_horario_mes_notificaciones mes "
				+ "left join asi_turnos turno on turno.ide_astur=mes.ide_astur "
				+ "left join gth_empleado emp on emp.ide_gtemp=mes.ide_gtemp "
				+ "where mes.ide_ashmn=-1");
		
    			tab_horario_consulta.getColumna("ide_ashmn").setNombreVisual("CODIGO");
    			tab_horario_consulta.getColumna("NOMBRES_APELLIDOS").setNombreVisual("NOMBRES");
    			tab_horario_consulta.getColumna("NOMBRES_APELLIDOS").setLongitud(50);
    			tab_horario_consulta.getColumna("fecha_ashmn").setValorDefecto(utilitario.getFechaActual());
    			tab_horario_consulta.getColumna("fecha_ashmn").setNombreVisual("FECHA SOLICITUD");
    			tab_horario_consulta.getColumna("fecha_ashmn").setLongitud(25);
    			tab_horario_consulta.getColumna("descripcion_astur").setLongitud(150);
    			tab_horario_consulta.getColumna("descripcion_astur").setNombreVisual("TURNO");
    			tab_horario_consulta.getGrid().setColumns(4);
    			tab_horario_consulta.setCondicion("ide_ashmn=-1");
    			tab_horario_consulta.setLectura(true);

    			tab_horario_consulta.dibujar();
		PanelTabla pat_panel_consulta=new PanelTabla();
		pat_panel_consulta.setPanelTabla(tab_horario_consulta);
		pat_panel_consulta.setMensajeWarn("REGISTRO DE NOVEDADES NOTIFICADAS");

		
		tab_horario.setId("tab_horario");
  	    tab_horario.setTabla("asi_horario_mes_empleado", "ide_ashme", 2);
  	    tab_horario.getColumna("ide_ashme").setNombreVisual("COD");
  	    tab_horario.getColumna("ide_ashme").setLongitud(5);
  	    tab_horario.getColumna("ide_ashme").alinearCentro();
  	 
  	    
  	  tab_horario.getColumna("IDE_GTEMP").setCombo("select EMP.IDE_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
  			  "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
  			  "EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
  			  "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "+
  			  "FROM GTH_EMPLEADO EMP ");
  	  	tab_horario.getColumna("IDE_GTEMP").setAutoCompletar();
	    tab_horario.getColumna("IDE_GTEMP").setLectura(true);

  	    
  	    tab_horario.getColumna("IDE_GEMES").setCombo("select ide_gemes,detalle_gemes from gen_mes ");
  	    tab_horario.getColumna("IDE_GEANI").setCombo("select ide_geani,detalle_geani from gen_anio");
  	    tab_horario.getColumna("dia1").setCombo("select ide_astur,  "
       		+ "nom_astur, "
       		+ "descripcion_astur "
       		+ "from asi_turnos ");
  	    tab_horario.getColumna("dia1").setAutoCompletar();
  	    tab_horario.getColumna("dia1").setLongitud(20);
  	    tab_horario.getColumna("dia1").alinearCentro();

      
        tab_horario.getColumna("dia2").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia2").setAutoCompletar();
        tab_horario.getColumna("dia2").setLongitud(20);
        tab_horario.getColumna("dia2").alinearCentro();           
        tab_horario.getColumna("dia3").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia3").setAutoCompletar();
        tab_horario.getColumna("dia3").setLongitud(20);
        tab_horario.getColumna("dia3").alinearCentro();
        tab_horario.getColumna("dia4").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia4").setAutoCompletar();
        tab_horario.getColumna("dia4").setLongitud(20);
        tab_horario.getColumna("dia4").alinearCentro();
        tab_horario.getColumna("dia5").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia5").setAutoCompletar();
        tab_horario.getColumna("dia5").setLongitud(20);
        tab_horario.getColumna("dia5").alinearCentro();    
        tab_horario.getColumna("dia6").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia6").setAutoCompletar();
        tab_horario.getColumna("dia6").setLongitud(20);
        tab_horario.getColumna("dia6").alinearCentro();       
        tab_horario.getColumna("dia7").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia7").setAutoCompletar();
        tab_horario.getColumna("dia7").setLongitud(20);
        tab_horario.getColumna("dia7").alinearCentro();
        tab_horario.getColumna("dia8").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia8").setAutoCompletar();
        tab_horario.getColumna("dia8").setLongitud(20);
        tab_horario.getColumna("dia8").alinearCentro();
        tab_horario.getColumna("dia9").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia9").setAutoCompletar();
        tab_horario.getColumna("dia9").setLongitud(20);
        tab_horario.getColumna("dia9").alinearCentro();     
        tab_horario.getColumna("dia10").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia10").setAutoCompletar();
        tab_horario.getColumna("dia10").setLongitud(20);
        tab_horario.getColumna("dia10").alinearCentro();
        tab_horario.getColumna("dia11").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia11").setAutoCompletar();
        tab_horario.getColumna("dia11").setLongitud(20);
        tab_horario.getColumna("dia11").alinearCentro();
        tab_horario.getColumna("dia12").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia12").setAutoCompletar();
        tab_horario.getColumna("dia12").setLongitud(20);
        tab_horario.getColumna("dia12").alinearCentro();
        tab_horario.getColumna("dia13").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia13").setAutoCompletar();
        tab_horario.getColumna("dia13").setLongitud(20);
        tab_horario.getColumna("dia13").alinearCentro();
        tab_horario.getColumna("dia14").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia14").setAutoCompletar();
        tab_horario.getColumna("dia14").setLongitud(20);
        tab_horario.getColumna("dia14").alinearCentro();
        tab_horario.getColumna("dia15").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia15").setAutoCompletar();
        tab_horario.getColumna("dia15").setLongitud(20);
        tab_horario.getColumna("dia15").alinearCentro();
        tab_horario.getColumna("dia16").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia16").setAutoCompletar();
        tab_horario.getColumna("dia16").setLongitud(20);
        tab_horario.getColumna("dia16").alinearCentro();
        tab_horario.getColumna("dia17").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia17").setAutoCompletar();
        tab_horario.getColumna("dia17").setLongitud(20);
        tab_horario.getColumna("dia17").alinearCentro();

        
        
        
        
        
        
        
        tab_horario.getColumna("dia18").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia18").setAutoCompletar();
        tab_horario.getColumna("dia18").setLongitud(20);
        tab_horario.getColumna("dia18").alinearCentro();

        
        
        
        
        
        

        tab_horario.getColumna("dia19").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia19").setAutoCompletar();
        tab_horario.getColumna("dia19").setLongitud(20);
        tab_horario.getColumna("dia19").alinearCentro();

        
        
        
        
        

        tab_horario.getColumna("dia20").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia20").setAutoCompletar();
        tab_horario.getColumna("dia20").setLongitud(20);
        tab_horario.getColumna("dia20").alinearCentro();

        
        
        
        
        tab_horario.getColumna("dia21").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia21").setAutoCompletar();
        tab_horario.getColumna("dia21").setLongitud(20);
        tab_horario.getColumna("dia21").alinearCentro();

        
        

        tab_horario.getColumna("dia22").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia22").setAutoCompletar();
        tab_horario.getColumna("dia22").setLongitud(20);
        tab_horario.getColumna("dia22").alinearCentro();

        
        
        
        
        
        tab_horario.getColumna("dia23").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia23").setAutoCompletar();
        tab_horario.getColumna("dia23").setLongitud(20);
        tab_horario.getColumna("dia23").alinearCentro();

        
        
        
        tab_horario.getColumna("dia24").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia24").setAutoCompletar();
        tab_horario.getColumna("dia24").setLongitud(20);
        tab_horario.getColumna("dia24").alinearCentro();

        
        
        
        
        tab_horario.getColumna("dia25").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia25").setAutoCompletar();
        tab_horario.getColumna("dia25").setLongitud(20);
        tab_horario.getColumna("dia25").alinearCentro();

        
        
        

        tab_horario.getColumna("dia26").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia26").setAutoCompletar();
        tab_horario.getColumna("dia26").setLongitud(20);
        tab_horario.getColumna("dia26").alinearCentro();

        
        tab_horario.getColumna("dia27").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia27").setAutoCompletar();
        tab_horario.getColumna("dia27").setLongitud(20);
        tab_horario.getColumna("dia27").alinearCentro();

        
        
        
        

        tab_horario.getColumna("dia28").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia28").setAutoCompletar();
        tab_horario.getColumna("dia28").setLongitud(20);
        tab_horario.getColumna("dia28").alinearCentro();

        
        
        
        
        
        tab_horario.getColumna("dia29").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia29").setAutoCompletar();
        tab_horario.getColumna("dia29").setLongitud(20);
        tab_horario.getColumna("dia29").alinearCentro();

        
        
        
        tab_horario.getColumna("dia30").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia30").setAutoCompletar();
        tab_horario.getColumna("dia30").setLongitud(20);
        tab_horario.getColumna("dia30").alinearCentro();

        
        
        
        tab_horario.getColumna("dia31").setCombo("select ide_astur,  "
           		+ "nom_astur, "
           		+ "descripcion_astur "
           		+ "from asi_turnos  ");
        tab_horario.getColumna("dia31").setAutoCompletar();
        tab_horario.getColumna("dia31").setLongitud(20);
        tab_horario.getColumna("dia31").alinearCentro();

        
        
        tab_horario.getColumna("activo_ashme").setNombreVisual("ACTIVO");
        tab_horario.getColumna("activo_ashme").setLongitud(5);

        tab_horario.getColumna("activo_ashme").setCheck();
        tab_horario.getColumna("activo_ashme").setValorDefecto("true");
        tab_horario.getColumna("activo_ashme").alinearCentro();
        
        tab_horario.getColumna("aplica_hora_extra").setNombreVisual("H.EXT");
        tab_horario.getColumna("aplica_hora_extra").setCheck();
        tab_horario.getColumna("aplica_hora_extra").alinearCentro();
        tab_horario.getColumna("aplica_hora_extra").setVisible(false);

        
        tab_horario.getColumna("IDE_SUCURSAL").setCombo("select ide_sucu,nom_sucu from sis_sucursal");
        tab_horario.getColumna("IDE_SUCURSAL").setAutoCompletar();
        tab_horario.getColumna("IDE_SUCURSAL").setNombreVisual("SUCURSAL");
        tab_horario.getColumna("IDE_SUCURSAL").alinearCentro();
        tab_horario.getColumna("IDE_SUCURSAL").setLectura(true);
        tab_horario.getColumna("IDE_SUCURSAL").setVisible(false);

        
     
        tab_horario.getColumna("IDE_GEARE").setCombo("select ide_geare,detalle_geare from gen_area");
        tab_horario.getColumna("IDE_GEARE").setAutoCompletar();
        tab_horario.getColumna("IDE_GEARE").setNombreVisual("AREA");
        tab_horario.getColumna("IDE_GEARE").setLectura(true);
        tab_horario.getColumna("IDE_GEARE").setVisible(false);

        
        
        tab_horario.getColumna("IDE_GEDEP").setCombo("select ide_gedep,detalle_gedep from gen_departamento");
        tab_horario.getColumna("IDE_GEDEP").setAutoCompletar();
        tab_horario.getColumna("IDE_GEDEP").setNombreVisual("DEPARTAMENTO");
        tab_horario.getColumna("IDE_GEDEP").alinearCentro();
        tab_horario.getColumna("IDE_GEDEP").setLectura(true);        
        tab_horario.getColumna("IDE_GEEDP").setCombo("SELECT EPAR.IDE_GEEDP, " +
  				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
  				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
  				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
  				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
  				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
  				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
  				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
  				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
  				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE ");
        tab_horario.getColumna("IDE_GEEDP").setAutoCompletar();
        tab_horario.getColumna("IDE_GEEDP").setLongitud(20);
        tab_horario.getColumna("IDE_GEEDP").setNombreVisual("INGRESO");
        tab_horario.getColumna("IDE_GEEDP").alinearCentro();
        tab_horario.getColumna("IDE_GEEDP").setVisible(false);
        
        tab_horario.getColumna("IDE_GEEDP_CAMBIO").setCombo("SELECT EPAR.IDE_GEEDP, " +
  				"EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
  				"(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
  				"EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
  				"(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS " +
  				"FROM GEN_EMPLEADOS_DEPARTAMENTO_PAR EPAR " +
  				"LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=EPAR.IDE_GTEMP " +
  				"LEFT JOIN SIS_SUCURSAL SUCU ON SUCU.IDE_SUCU=EPAR.IDE_SUCU " +
  				"LEFT JOIN GEN_DEPARTAMENTO DEPA ON DEPA.IDE_GEDEP=EPAR.IDE_GEDEP " +
  				"LEFT JOIN GEN_AREA AREA ON AREA.IDE_GEARE=EPAR.IDE_GEARE ");
        
        tab_horario.getColumna("IDE_GEEDP_CAMBIO").setAutoCompletar();
        tab_horario.getColumna("IDE_GEEDP_CAMBIO").setLongitud(20);
        tab_horario.getColumna("IDE_GEEDP_CAMBIO").setNombreVisual("CAMBIO");
        tab_horario.getColumna("IDE_GEEDP_CAMBIO").alinearCentro();
        tab_horario.getColumna("IDE_GEEDP_CAMBIO").setVisible(false);
        tab_horario.getColumna("IDE_GEANI").setVisible(false);
        tab_horario.getColumna("NUM_EXTRA_ASHEM").setVisible(false);
        tab_horario.getColumna("NUM_SUPLE_ASHEM").setVisible(false);
        tab_horario.getColumna("IDE_ASJEI").setVisible(false);
        tab_horario.getColumna("documento_identidad_gtemp").setVisible(false);
        tab_horario.getColumna("ide_geedp").setVisible(false);
        tab_horario.getColumna("ide_gedep").setVisible(false);
        tab_horario.getColumna("activo_ashme").setVisible(false);
        tab_horario.getColumna("ide_gemes").setVisible(false);

        
        
       tab_horario.setHeader("ASIGNACION TURNOS A EMPLEADOS");
        
        tab_horario.setLectura(true);
        tab_horario.setCondicion("ide_ashme=-1");

        
       tab_horario.dibujar();
       PanelTabla pat_panel1 = new PanelTabla();
       pat_panel1.setPanelTabla(tab_horario);
       pat_panel1.setMensajeWarn("HORARIO MENSUAL ASIGNADO");
       

       Division div_division_notificaciones = new Division();
       div_division_notificaciones.setId("div_division_notificaciones");
       div_division_notificaciones.dividir2(pat_panel, pat_panel_consulta, "60%", "V");
       //agregarComponente(div_division_notificaciones);

        Division div_division = new Division();
        div_division.setId("div_division");
        div_division.dividir2(div_division_notificaciones, pat_panel1, "50%", "H");
        agregarComponente(div_division);
    }

    
    
    
	@Override
	public void insertar() {
		// TODO Auto-generated method stub
		if (tab_horario_novedades.isFocus()){
			if (aut_anio_mes.getValor()!=null){
				if (aut_empleado.getValor()!=null){
		tab_horario_novedades.insertar();
		tab_horario_novedades.setValor("ide_gtemp", aut_empleado.getValor());
		tab_horario_novedades.setValor("ide_gemes", ""+ide_gemes);
		tab_horario_novedades.setValor("ide_geani", ""+ide_geani);
		tab_horario_novedades.setValor("ide_usua", ""+utilitario.getVariable("ide_usua"));
      	
    	if(tipo_perfil.equals("1")){
    		tab_horario_novedades.setValor("ide_asjei", ""+tabJefeInmediato.getValor("ide_asjei"));
    	}else {
    		tab_horario_novedades.setValor("ide_asjei", ""+ide_asjei);
		}

			}else {
			utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Anio-Mes");
			}
			}else {
				utilitario.agregarMensajeInfo("No se puede insertar", "Debe seleccionar el Empleado");
				}		
				
		}
	}




	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		int ide_ashop=0;
		
		if (aut_anio_mes.getValor()==null || aut_anio_mes.getValor().isEmpty() || aut_anio_mes.getValor().equals("")) {
			utilitario.agregarMensaje("Debe seleccionar anio y mes", "");
			return;
			
		}
		
		if (aut_empleado.getValor()==null || aut_empleado.getValor().isEmpty() || aut_empleado.getValor().equals("")) {
			utilitario.agregarMensaje("Debe seleccionar empleado", "");
			return;
			
		}
		
		
		ide_ashop=Integer.parseInt(aut_anio_mes.getValor());
		 TablaGenerica tab_anio_mes=utilitario.consultar("SELECT "
		 			+ "PERIODO.ide_ashop,"
		 			+ "MES.IDE_GEMES,MES.DETALLE_GEMES, "
		 			+ "ANIO.IDE_geani,ANIO.DETALLE_geani "
			  		+ "FROM asi_horario_periodo PERIODO "
			  		+ "LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI "
			  		+ "LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES "
			  		+ "WHERE PERIODO.ide_ashop="+ide_ashop+" "
			  		+ "ORDER BY ANIO.DETALLE_geani ASC,MES.IDE_GEMES ASC ");
		String fecha_inicio="",fecha_fin="";
		int mesValida=0;
		if (Integer.parseInt(tab_anio_mes.getValor("IDE_GEMES"))<10) {
			 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-0"+tab_anio_mes.getValor("IDE_GEMES")+"-01";
			 fecha_fin=tab_anio_mes.getValor("DETALLE_geani")+"-0"+tab_anio_mes.getValor("IDE_GEMES")+"-"+utilitario.getDia(utilitario.getUltimaFechaMes(fecha_inicio));
		}else {
			 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-"+tab_anio_mes.getValor("IDE_GEMES")+"-01";
			 fecha_fin=tab_anio_mes.getValor("DETALLE_geani")+"-"+tab_anio_mes.getValor("IDE_GEMES")+"-"+utilitario.getDia(utilitario.getUltimaFechaMes(fecha_inicio));
		}
		
		 
		 if (tab_horario_novedades.getTotalFilas()>0) {
			 
			 for (int i = 0; i < tab_horario_novedades.getTotalFilas(); i++) {
					if (tab_horario_novedades.getValor("fecha_ashmn") == null) {
						utilitario.agregarMensajeInfo("Campos Invalidos", "Verificar fechas");
						return;
					}
			}
			
		
		for (int i = 0; i < tab_horario_novedades.getTotalFilas(); i++) {
			if (tab_horario_novedades.getValor(i,"fecha_ashmn")==null || tab_horario_novedades.getValor(i,"fecha_ashmn").equals("") || tab_horario_novedades.getValor(i,"fecha_ashmn").isEmpty()) {
				utilitario.agregarMensajeError("Fecha invalida", "Favor revisar fechas registradas como vacio");
				return;
			}
			
			if (tab_horario_novedades.getValor(i,"fecha_ashmn").compareTo(fecha_inicio)>=0 && tab_horario_novedades.getValor(i,"fecha_ashmn").compareTo(fecha_fin)<=0) {
			}else {
				utilitario.agregarMensajeError("Fecha seleccionado", "El mes y anio no corresponde"+tab_horario_novedades.getValor(i,"fecha_ashmn"));
				return;
			}
		}
		 }else {
				//guardarPantalla();
//				utilitario.agregarMensajeError("No existen datos", "Pantalla vacia");
//				return;
		}
		 
			for (int i = 0; i < tab_horario_novedades.getTotalFilas(); i++) {
 
		 TablaGenerica tab_horario_consulta_=utilitario.consultar("SELECT mes.ide_ashmn,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				  "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				  "EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				  "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, mes.fecha_ashmn, turno.nom_astur || '' || turno.descripcion_astur as descripcion_astur "
					+ "FROM asi_horario_mes_notificaciones mes "
					+ "left join asi_turnos turno on turno.ide_astur=mes.ide_astur "
					+ "left join gth_empleado emp on emp.ide_gtemp=mes.ide_gtemp "
				+ "where mes.ide_geani="+ide_geani+" and mes.ide_gemes="+ide_gemes+" and mes.activo_ashmn=true and mes.notificacion_ashmn=true and mes.IDE_GTEMP in("+aut_empleado.getValor()+") and  "
						+ "mes.fecha_ashmn='"+tab_horario_novedades.getValor(i,"fecha_ashmn")+"' "
						+ "order by mes.ide_ashmn desc " );
		if (tab_horario_consulta_.getTotalFilas()>0) {
			tab_horario_novedades.setValor(tab_horario_novedades.getFilaSeleccionada().getIndice(),"fecha_ashmn", "");
			utilitario.addUpdateTabla(tab_horario_novedades, "fecha_ashmn", "");
					utilitario.agregarMensaje("Error al seleccionar", "Ya se encuentra registrada la fecha");
			return;
		}
			}
		
		tab_horario_novedades.guardar();
		guardarPantalla();
	}




	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
		if (tab_horario_novedades.isFocus()) {
			 if (tab_horario_novedades.getTotalFilas()>0) {
					tab_horario_novedades.eliminar();
			 }

		}
	}



	public Tabla getTab_horario() {
		return tab_horario;
	}

	public void setTab_horario(Tabla tab_horario) {
		this.tab_horario = tab_horario;
	}



	public Tabla getTab_horario_consulta() {
		return tab_horario_consulta;
	}




	public void setTab_horario_consulta(Tabla tab_horario_consulta) {
		this.tab_horario_consulta = tab_horario_consulta;
	}




public Tabla getTab_horario_novedades() {
		return tab_horario_novedades;
	}




	public void setTab_horario_novedades(Tabla tab_horario_novedades) {
		this.tab_horario_novedades = tab_horario_novedades;
	}




public AutoCompletar getAut_empleado() {
		return aut_empleado;
	}




	public void setAut_empleado(AutoCompletar aut_empleado) {
		this.aut_empleado = aut_empleado;
	}




	public AutoCompletar getAut_anio_mes() {
		return aut_anio_mes;
	}




	public void setAut_anio_mes(AutoCompletar aut_anio_mes) {
		this.aut_anio_mes = aut_anio_mes;
	}
	
		

	public SeleccionTabla getSel_mes_importarTotales() {
		return sel_mes_importarTotales;
	}




	public void setSel_mes_importarTotales(SeleccionTabla sel_mes_importarTotales) {
		this.sel_mes_importarTotales = sel_mes_importarTotales;
	}




	public SeleccionTabla getSel_anio_importarTotales() {
		return sel_anio_importarTotales;
	}




	public void setSel_anio_importarTotales(SeleccionTabla sel_anio_importarTotales) {
		this.sel_anio_importarTotales = sel_anio_importarTotales;
	}




	public void limpiar() {
		tab_horario_novedades.limpiar();
		tab_horario_consulta.limpiar();
		tab_horario.limpiar();
		aut_empleado.limpiar();
		aut_anio_mes.limpiar();
		utilitario.addUpdate("tab_horario,tab_horario_novedades,aut_empleado,aut_anio_mes");// limpia y refresca el autocompletar
	}



public String empleadoNovedadHorario(String ide_gtemp,int ide_geani,int ide_gemes,String ide_asjei,String perfil){
	
	
	
	String sql="";
			
			if (ide_gtemp=="") {
			sql="SELECT  EMP.IDE_GTEMP,   "
			+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
			+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
			+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
			+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
			+ "FROM  asi_horario_mes_empleado ASHME "
			+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=ASHME.IDE_GTEMP "
			+ "WHERE IDE_GEMES=-1 AND IDE_GEANI=-1  "
			+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP ,EMP.SEGUNDO_NOMBRE_GTEMP ";
			}else{
			
			sql="SELECT   EMP.IDE_GTEMP, "
						+ "EMP.DOCUMENTO_IDENTIDAD_GTEMP, "
						+ "EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  "
						+ "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
						+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' || "
						+ "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS "
						+ "FROM  asi_horario_mes_empleado ASHME "
						+ "LEFT JOIN GTH_EMPLEADO EMP ON EMP.IDE_GTEMP=ASHME.IDE_GTEMP "
						+ "WHERE IDE_GEMES="+ide_gemes+" AND IDE_GEANI="+ide_geani+" ";
							
				if (perfil.equals("1")) {
					//sql+="and ide_asjei in("+ide_asjei+") ";
				}else {
					sql+="and ide_asjei in("+ide_asjei+") ";


				}
				sql+="ORDER BY EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP ,EMP.SEGUNDO_NOMBRE_GTEMP ";
			}
			
	

	return sql;
	
	
}

public void filtrarHorarioEmpleado(SelectEvent evt){
	aut_empleado.onSelect(evt);
	
	if (aut_anio_mes.getValue()==null || aut_anio_mes.getValue().equals("") ) {
		utilitario.agregarMensajeInfo("No se puede realizar la accion", "Debe seleccionar el Año y Mes");
		return;	
	}
	
	
	
	
	
	TablaGenerica tabAnioMes=utilitario.consultar("SELECT MES.IDE_GEMES, ANIO.IDE_geani "
	  		+ "FROM asi_horario_periodo PERIODO "
	  		+ "LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI "
	  		+ "LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES "
	  		+ "WHERE PERIODO.ide_ashop="+aut_anio_mes.getValor());
	String anio="",mes="";
	anio=tabAnioMes.getValor("ide_geani");
	mes=tabAnioMes.getValor("ide_gemes");
	ide_geani=Integer.parseInt(anio);
	ide_gemes=Integer.parseInt(mes);
	
	aut_empleado.setId("aut_empleado");
	String str_sql_emp=empleadoNovedadHorario("1",ide_geani,ide_gemes,ide_asjei,tipo_perfil);
	aut_empleado.setAutoCompletar(str_sql_emp);		
	

	if (aut_empleado.getValue()==null || aut_empleado.getValue().equals("") ) {
		utilitario.agregarMensajeInfo("No se puede realizar la accion", "Debe seleccionar un empleado");
		return;	
	}
	tab_horario_novedades.setCondicion("IDE_GTEMP="+aut_empleado.getValor()+" and IDE_GEANI="+ide_geani+" AND IDE_GEMES="+ide_gemes+" AND notificacion_ashmn=false");
	tab_horario_novedades.ejecutarSql();
	tab_horario_novedades.actualizar();
	tab_horario.setCondicion("IDE_GTEMP="+aut_empleado.getValor()+" and IDE_GEANI="+ide_geani+" AND IDE_GEMES="+ide_gemes);
	tab_horario.ejecutarSql();
	tab_horario.actualizar();
	
	
	tab_horario_consulta.setSql("SELECT mes.ide_ashmn,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
			  "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
			  "EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
			  "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, "
			  + "mes.fecha_ashmn, turno.nom_astur || '' || turno.descripcion_astur as descripcion_astur "
				+ "FROM asi_horario_mes_notificaciones mes "
				+ "left join asi_turnos turno on turno.ide_astur=mes.ide_astur "
				+ "left join gth_empleado emp on emp.ide_gtemp=mes.ide_gtemp "
			+ "where mes.ide_geani="+ide_geani+" and mes.ide_gemes="+ide_gemes+" and mes.notificacion_ashmn=true and mes.IDE_GTEMP="+aut_empleado.getValor()+"" );
	tab_horario_consulta.ejecutarSql();
	tab_horario_consulta.actualizar();
	tab_horario_novedades.setCondicion("IDE_GTEMP="+aut_empleado.getValor()+" and IDE_GEANI="+ide_geani+" AND IDE_GEMES="+ide_gemes+" AND notificacion_ashmn=false AND ACTIVO_ASHMN=true");
	tab_horario_novedades.ejecutarSql();
	tab_horario_novedades.actualizar();
	
	utilitario.addUpdate("tab_horario,tab_horario_novedades");
}

  public String obtenerPeriodo(){
	  String sql="";
	  
	  sql="SELECT PERIODO.ide_ashop,MES.DETALLE_GEMES, ANIO.DETALLE_geani "
	  		+ "FROM asi_horario_periodo PERIODO "
	  		+ "LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI "
	  		+ "LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES "
	  		+ "ORDER BY ANIO.DETALLE_geani ASC,MES.IDE_GEMES ASC ";
	  return sql;
	  
  }
  
  public void abrirDialogoImportar() {
		//tab_planificacion_hxe.setCondicion("ide_cobph=-1");
		//tab_planificacion_hxe.ejecutarSql();
	    //tab_planificacion_hxe_observacion.setCondicion("ide_cobpo=-1");
	    //tab_planificacion_hxe_observacion.ejecutarSql();
		//utilitario.addUpdate("tab_planificacion_hxe,tab_planificacion_hxe_observacion");
			sel_mes_importarTotales.dibujar();
			//valorTotal=false;
		}
  
  
  public void getEmpleadoImportarTotales(){
    	try {
    		empleado=sel_empleado_importarTotales.getSeleccionados();
    		sel_empleado_importarTotales.cerrar();

    		//Validacion si no se escogen datos
    		 if ((mes.isEmpty() || mes.equals("")) || (anio.isEmpty() || anio.equals("")) || (empleado.isEmpty() || empleado.equals(""))) {
    			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "");
    			return;
    		}else {
    			
    		int mesAsignado=Integer.parseInt(mes);
    		int anioAsignado=Integer.parseInt(anio);
  		dia_importar_total.dibujar();
			}
    		
    	} catch (NumberFormatException e) {
    		// TODO Auto-generated catch block

    	System.out.println("ERROR METODO getEmpleado()");
    	
    	}
    }
    
  
  public void obtenerMesImportarTotales(){
    	
	  mes=sel_mes_importarTotales.getValorSeleccionado();
		 if ((mes==null || mes.isEmpty() || mes.equals(""))) {
	 			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado mes");
	 			return;
	 		}else {
				  sel_mes_importarTotales.cerrar();	
				    sel_anio_importarTotales.dibujar();


}

}
  //Metodo distingue si es ingreso o edicion
  public void obtenerAnioImportarTotales(){

      anio=sel_anio_importarTotales.getValorSeleccionado();
      if (anio==null || (anio.isEmpty() || anio.equals(""))) {
  			utilitario.agregarMensajeError("Debe seleccionar los parametros solicitados", "No ha seleccionado anio");
  			return;
  		}else {
  			sel_anio_importarTotales.cerrar();
  			
  			TablaGenerica tabEmpleadoXJefeInmediato=null,tabEmpleadoMensual=null;
  			String sql="";
  			 if (tipo_perfil.equals("1")) {    				
  	
  				 
  				sql="select  emp.ide_gtemp,	emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
  						+ "from  asi_horario_mes_empleado asi "
  						+ "LEFT JOIN GTH_EMPLEADO EMP  on emp.ide_gtemp=asi.ide_gtemp "
  						+ "WHERE emp.ide_gtemp in("+str_ide_empleado_mensual+")  and asi.ide_gemes="+mes+" and ide_geani="+anio+" "
  						+ "GROUP BY  emp.ide_gtemp,emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP   "
  						+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
  						+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
 
				}else {
					
					sql="select  emp.ide_gtemp,	emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
						+ "from  asi_horario_mes_empleado asi "
	  					+ "LEFT JOIN GTH_EMPLEADO EMP  on emp.ide_gtemp=asi.ide_gtemp "
  						+ "WHERE emp.ide_gtemp in("+str_ide_empleado_mensual+")  and asi.ide_gemes="+mes+" and ide_geani="+anio+" "
  						+ "GROUP BY  emp.ide_gtemp,emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP   "
  						+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
  						+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC";
  					 }

  			
  			System.out.println(""+sql);
  			sel_empleado_importarTotales.setSeleccionTabla(sql, "IDE_GTEMP");
  			sel_empleado_importarTotales.getTab_seleccion().getColumna("documento_identidad_gtemp").setFiltro(true);
  			sel_empleado_importarTotales.getTab_seleccion().getColumna("apellido_paterno_gtemp").setFiltro(true);
  			sel_empleado_importarTotales.setTitle("Seleccione Empleados");
  			gru_pantalla.getChildren().add(sel_empleado_importarTotales);
  			sel_empleado_importarTotales.getBot_aceptar().setMetodo("getEmpleadoImportarTotales");
  			sel_empleado_importarTotales.redibujar();
  		   	utilitario.addUpdate("sel_empleado_importarTotales");
	
  }
  
  
  }
  
  
  
  private String getFormatoInformacion(String mensaje) {
		return "<div><font color='#3333ff'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
	}

	private String getFormatoError(String mensaje) {
		return "<div><font color='#ff0000'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
	}
	private String getFormatoAdvertencia(String mensaje) {
		return "<div><font color='#ffcc33'><strong>*&nbsp;</strong>" + mensaje + "</font></div>";
	}  
  

/**
 * Valida el archivo para que pueda importar un rubro a la nomina
 * 
 * @param evt
 */
public void validarArchivoTotal(FileUploadEvent evt) {
//	if (aut_rubros.getValor() != null) {
		// Leer el archivo
		String str_msg_info = "";
		String str_msg_adve = "";
		String str_msg_erro = "";
		double dou_tot_valor_imp = 0;

		boolean bandTabVacia=false;
		int contErrores=0; 
		
		try {
			// V�lido que el rubro seleccionado este configurado en los tipo
			// de nomina
			String tipo_nom = "";
			
			Workbook archivoExcel = Workbook.getWorkbook(evt.getFile().getInputstream());
			Sheet hoja = archivoExcel.getSheet(0);// LEE LA PRIMERA HOJA
			if (hoja == null) {
				utilitario.agregarMensajeError("No existe ninguna hoja en el archivo seleccionado", "");
				return;
			}
			int int_fin = hoja.getRows();
			ide_empleados_crear=new StringBuilder();
			upl_archivo.setNombreReal(evt.getFile().getFileName());
			str_msg_info += getFormatoInformacion("El archivo " + upl_archivo.getNombreReal() + " contiene " + int_fin + " filas");
			
			{
			lis_importa = new ArrayList<String[]>();
			lis_turno = new ArrayList<String[]>();
			lis_empleado = new ArrayList<String[]>();

			int num_seleccionados=sel_empleado_importarTotales.getNumeroSeleccionados();

			
			
			
			
			tab_emp_total.setSql("SELECT IDE_GTEMP, DOCUMENTO_IDENTIDAD_GTEMP AS DOCUMENTO, SEGUNDO_NOMBRE_GTEMP AS NOMBRES , "
							  		+ "0 as DIA, "
							  		+ "0 as TURNO "
									+ "FROM GTH_EMPLEADO WHERE IDE_GTEMP=-1");	
			
			tab_emp_total.ejecutarSql();
			tab_emp_total.setLectura(false);
			tab_emp_total.setDibujo(false);
			int ide_gtemp=0;
			TablaGenerica tab_empleado1=null;
			TablaGenerica tab_hxe=null;
			int x=0;
			double dou_valor = 0d,dou_valor_turno=0d;
			int in_valor=0;
			TablaGenerica tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anio+")");
			int nuevoMes=0;
			nuevoMes=Integer.parseInt(mes);
			String fecha="";
			if (Integer.parseInt(mes)>9) {
				fecha=tabAnio.getValor("detalle_geani")+"-"+nuevoMes+"-01";
			}else {
				fecha=tabAnio.getValor("detalle_geani")+"-0"+nuevoMes+"-01";
			}

			ultimoDia=utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha));
			String fec_limite="",fec_ini="",fec_ini_="";
			fec_ini_=diaSemana(utilitario.getFechaActual());
			fec_ini=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fec_ini_), -Integer.parseInt(utilitario.getVariable("p_dias_registro_novedad"))));
			fec_limite=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fec_ini_), 7));
			
			for (int i = 0; i < int_fin; i++) {
				//Obtengo la cedula de la hoja excel en kla columna 0
				String str_cedula = hoja.getCell(0, i).getContents();
				//Quito los espacios en blanco de la cedula obtenida
				str_cedula = str_cedula.trim();
				//Obtengo los datos del empleado del sistema ERP tabla GTH_EMPLEADO
				//TablaGenerica tab_empleado = ser_empleado.getEmpleado("DOCUMENTO_IDENTIDAD_GTEMP", str_cedula);
				String diaIngreso=hoja.getCell(1, i).getContents();
				//Si existe empleados
				
				TablaGenerica tab_empleado =utilitario.consultar("select  emp.ide_gtemp,	emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,"
						+ "EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP  "
  						+ "from  GTH_EMPLEADO EMP  "
  						+ " left join asi_horario_mes_empleado ashem on emp.ide_gtemp=ashem.ide_gtemp "
  						+ "WHERE emp.ide_gtemp in("+empleado+")  and  emp.documento_identidad_gtemp='"+str_cedula+"' and ashem.activo_ashme=true and "
  						+ "ashem.ide_gemes="+mes+" and ashem.ide_geani="+anio+" "
  						+ "GROUP BY  emp.ide_gtemp,emp.documento_identidad_gtemp , EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP   "
  						+ "ORDER BY EMP.APELLIDO_PATERNO_GTEMP ASC ,EMP.APELLIDO_MATERNO_GTEMP ASC , "
  						+ "EMP.PRIMER_NOMBRE_GTEMP ASC,EMP.SEGUNDO_NOMBRE_GTEMP ASC");
  					//	tab_empleado.imprimirSql();
				 
				
				
				
				
				
				if (tab_empleado.isEmpty()) {
					// No existe el documento en la tabla de empleados
					str_msg_erro += getFormatoError("El documento de Identidad: " + str_cedula + " no se encuentra registrado en la base de datos de horarios, fila " + (i + 1));
				} else {
					
					String fecComparacion="";
					if (Integer.parseInt(mes)>=10) {
						if (Integer.parseInt(diaIngreso)<10) {
							fecComparacion=tabAnio.getValor("detalle_geani")+"-"+nuevoMes+"-0"+diaIngreso+" ";

						}else {
							fecComparacion=tabAnio.getValor("detalle_geani")+"-"+nuevoMes+"-"+diaIngreso+" ";
						}
						
					}else {
						if (Integer.parseInt(diaIngreso)<10) {
							fecComparacion=tabAnio.getValor("detalle_geani")+"-0"+nuevoMes+"-0"+diaIngreso+" ";

						}else {
							fecComparacion=tabAnio.getValor("detalle_geani")+"-0"+nuevoMes+"-"+diaIngreso+" ";
						}
												
					}
					
	
								
					
					
				ide_gtemp=Integer.parseInt(tab_empleado.getValor("IDE_GTEMP"));
				int valorHex=0;
				String ide_cobph="";
				boolean band25=false,bandNoc25=false,band50=false,band60=false,band100=false;
				bandTabVacia=false;
				// Valido que el documento sea correcto
					if (!ser_empleado1.isDocumentoIdentidadValido(utilitario.getVariable("p_gth_tipo_documento_cedula"), str_cedula)) {
						str_msg_adve += getFormatoAdvertencia("El documento de Identidad: " + str_cedula + " no es valido, fila " + (i + 1));
					} else {
						// Valido que el empleado se encuentre en la o las
						// nominas q va a importar
						String str_ide_gtemp = tab_empleado.getValor("IDE_GTEMP");
						
						
						TablaGenerica tabpartda = null;
						tabpartda = ser_empleado1.getPartidaRoles(str_ide_gtemp);
						if (tabpartda.isEmpty() == false) {
							
							
							
							
							//tab_hxe= utilitario.consultar("SELECT ide_cobph,ide_gtemp, ide_asjei "
						//			+ "FROM con_biometrico_plan_hxe ");
								//	+ "where ide_gtemp="+ide_gtemp+" and ide_geani="+anio+" and ide_gemes="+mes+" and ide_asjei="+jefe_inmediato_planificacion+" and ide_gtemp in("+empleado+")");
					//		if (tab_hxe.isEmpty()) {
								// No existe registro del rubro para el
								// empleado
							//	str_msg_adve += getFormatoAdvertencia("No se puede asignar valor al rubro " + ((Object[]) aut_rubros.getValue())[1] + " al n�mero de cedula " + str_cedula + " ya que no existe configuraci�n, fila " + (i + 1));	
						//	}
							//Sin partida Presupuestaria
							}else {
								str_msg_adve += getFormatoAdvertencia("El documento de Identidad: " + str_cedula + " no tiene horario asginado, fila " + (i + 1));
							}				
						
		

				if (fecComparacion.compareTo(fec_ini)>=0 && fecComparacion.compareTo(fec_limite)<=0) {
				}else {
					str_msg_erro += getFormatoError("La fecha: " + fecComparacion  + " esta fuera de rango, fila " + (i + 1));

				}
		
					//VALIDACION DE REGISTROS REPETIDOS	
					TablaGenerica tabHistorial=null;				
					TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("asi_horario_mes_notificaciones_historial", "ide_ashmh"));
					String codigo=tab_codigo.getValor("codigo");
						String valor="INSERT INTO asi_horario_mes_notificaciones_historial( "
								+ "ide_ashmh, ide_gtemp, dia, ide_geani, ide_gemes) "
								+ "VALUES ("+codigo+","
								+ " "+tab_empleado.getValor("ide_gtemp")+","
								+ " "+diaIngreso+","
								+ " "+anio+","
								+ ""+mes+")";
						utilitario.getConexion().ejecutarSql(valor);

								
						tabHistorial=utilitario.consultar("select ide_ashmh,ide_gtemp from asi_horario_mes_notificaciones_historial where ide_gtemp="+tab_empleado.getValor("ide_gtemp")+" "
								+ "and dia="+diaIngreso+" and ide_geani="+anio+" and ide_gemes="+mes);
						
						if (tabHistorial.getTotalFilas()>1) {
							str_msg_erro += getFormatoError("La fecha: " + fecComparacion  + " ya se encuentra repetida en el archivo, fila " + (i + 1));
						}
						
						TablaGenerica tab_horario_consultaHorario=utilitario.consultar("SELECT mes.ide_ashmn, mes.fecha_ashmn, turno.descripcion_astur "
								+ "FROM asi_horario_mes_notificaciones mes "
								+ "left join asi_turnos turno on turno.ide_astur=mes.ide_astur "
								+ "where mes.ide_geani="+anio+" and mes.ide_gemes="+mes+" and mes.notificacion_ashmn=true and mes.IDE_GTEMP="+tab_empleado.getValor("ide_gtemp")+" "
								+ "and mes.fecha_ashmn='"+fecComparacion+"' ");
						
						if (tab_horario_consultaHorario.getTotalFilas()>0) {
							str_msg_erro += getFormatoError("La fecha: " + fecComparacion  + " ya se encuentra registrado en la base de datos de notificaciones, fila " + (i + 1));
						}
						
						//if (fecComparacion.compareTo(fec_limite)<0) {
						//	str_msg_erro += getFormatoError("El cambio de fecha: " + fecComparacion  + " no esta permitida, fila " + (i + 1));

						//}//
						
					}//validacion documento de identidad
					
					//if (int_fin==(i-1)) {
					//	ide_empleados_crear.append(ide_gtemp);	
					//}else {
						ide_empleados_crear.append(ide_gtemp+",");
					//}
					
					
					
				}// tab_emp
				
				
				
				
				
				//for (x = 0; x <= num_seleccionados; x++) {		
			
				String str_valor = hoja.getCell(1, i).getContents();
				String str_valor_turno = hoja.getCell(2, i).getContents();

				str_valor = str_valor.replaceAll(",", ".");
				if (str_valor == null || str_valor.equals("")) {
					str_valor = "0";
				}
				dou_valor = 0.00;
				in_valor=0;
				str_valor_turno = str_valor_turno.replaceAll(",", ".");
				if (str_valor_turno == null || str_valor_turno.equals("")) {
					str_valor_turno = "0";
				}
				dou_valor_turno = 0.00;
				
				
				try {
					// Valida que sea una cantidad numerica
					dou_valor = Integer.parseInt(str_valor);
					dou_valor_turno = Integer.parseInt(str_valor_turno);

					} catch (Exception e) {
					// TODO: handle exception
					str_msg_erro += getFormatoError("Valor numerico no valido , fila " + (i + 1));
				}
				//
				//TablaGenerica tab_emp;
			//	Object[] obj_cumula = getAcumuladoTotal(str_cedula,x);
				//Object[] obj_cumula60 = getAcumuladoTotal(str_cedula,x);
				//Object[] obj_cumula25 = getAcumuladoTotal(str_cedula,x);
				//Object[] obj_cumula50 = getAcumuladoTotal(str_cedula,x);
				//Object[] obj_cumula100 = getAcumuladoTotal(str_cedula,x);
				//Object[] obj_cumula = getAcumulado(str_cedula);

				//if (obj_cumula == null) {
					tab_emp_total.insertar();
					tab_emp_total.setValor("IDE_GTEMP", tab_empleado.getValor("IDE_GTEMP"));
					tab_emp_total.setValor("DOCUMENTO_IDENTIDAD_GTEMP", str_cedula);
					tab_emp_total.setValor(
							"NOMBRES",
							new StringBuilder(tab_empleado.getValor("APELLIDO_PATERNO_GTEMP") == null ? "" : tab_empleado.getValor("APELLIDO_PATERNO_GTEMP")).append(" ")
									.append((tab_empleado.getValor("APELLIDO_MATERNO_GTEMP") == null ? "" : tab_empleado.getValor("APELLIDO_MATERNO_GTEMP"))).append(" ")
									.append((tab_empleado.getValor("PRIMER_NOMBRE_GTEMP") == null ? "" : tab_empleado.getValor("PRIMER_NOMBRE_GTEMP"))).append(" ")
									.append(((tab_empleado.getValor("SEGUNDO_NOMBRE_GTEMP") == null ? "" : tab_empleado.getValor("SEGUNDO_NOMBRE_GTEMP")))).toString());
					lis_importa.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor) });
					lis_turno.add(new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor_turno) });

					tab_emp_total.setValor("DIA", utilitario.getFormatoNumero(dou_valor));
					tab_emp_total.setValor("TURNO", utilitario.getFormatoNumero(dou_valor_turno));
					

					
					
					
				// Acumula el valor
					//		}
				
			/*	try {
					int int_fila = pckUtilidades.CConversion.CInt(String.valueOf(obj_cumula[0]));
					double dou_anterior = Double.parseDouble(String.valueOf(obj_cumula[1]));
					double dou_anterior_turno = Double.parseDouble(String.valueOf(obj_cumula[2]));

					tab_emp_total.setValor(int_fila, "DIA", utilitario.getFormatoNumero(dou_valor + dou_anterior));
					tab_emp_total.setValor(int_fila, "TURNO", utilitario.getFormatoNumero(dou_valor + dou_anterior));

					int int_indice = pckUtilidades.CConversion.CInt(String.valueOf(obj_cumula[2]));
					lis_importa.set(int_indice, new String[] { str_cedula, utilitario.getFormatoNumero(dou_valor + dou_anterior) });
				} catch (Exception e) {
					System.out.println("ERROR " + e.getMessage());
				}*/
				
			}

		}
			tab_emp_total.setLectura(true);
			tab_emp_total.setDibujo(true);
	
			
			archivoExcel.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		String str_resultado = "";
		if (!str_msg_info.isEmpty()) {
			str_resultado = "<strong><font color='#3333ff'>INFORMACION</font></strong>" + str_msg_info;
		}
		if (!str_msg_adve.isEmpty()) {
			str_resultado += "<strong><font color='#ffcc33'>ADVERTENCIAS</font></strong>" + str_msg_adve;
		}
		if (!str_msg_erro.isEmpty()) {
			str_resultado += "<strong><font color='#ff0000'>ERRORES</font></strong>" + str_msg_erro;
		}

		edi_mensajes.setValue(str_resultado);
		grid_tabla_emp_total.getChildren().clear();
	//	grid_tabla_emp_sum_totales.getChildren().clear();

		utilitario.addUpdate("edi_mensajes,eti_tot_val_imp");
		grid_tabla_emp_total.getChildren().add(tab_emp_total);
		dia_valida_empleado_total.dibujar();
		

		
		
}




public TablaGenerica getTabJefeInmediato() {
	return tabJefeInmediato;
}




public void setTabJefeInmediato(TablaGenerica tabJefeInmediato) {
	this.tabJefeInmediato = tabJefeInmediato;
}




public Upload getUpl_archivo() {
	return upl_archivo;
}




public void setUpl_archivo(Upload upl_archivo) {
	this.upl_archivo = upl_archivo;
}




public SeleccionTabla getSel_empleado_importarTotales() {
	return sel_empleado_importarTotales;
}




public void setSel_empleado_importarTotales(
		SeleccionTabla sel_empleado_importarTotales) {
	this.sel_empleado_importarTotales = sel_empleado_importarTotales;
}




public Dialogo getDia_importar_total() {
	return dia_importar_total;
}




public void setDia_importar_total(Dialogo dia_importar_total) {
	this.dia_importar_total = dia_importar_total;
}




public Dialogo getDia_valida_empleado_total() {
	return dia_valida_empleado_total;
}




public void setDia_valida_empleado_total(Dialogo dia_valida_empleado_total) {
	this.dia_valida_empleado_total = dia_valida_empleado_total;
}




public Tabla getTab_emp_total() {
	return tab_emp_total;
}




public void setTab_emp_total(Tabla tab_emp_total) {
	this.tab_emp_total = tab_emp_total;
}




public Grid getGrid_tabla_emp_total() {
	return grid_tabla_emp_total;
}




public void setGrid_tabla_emp_total(Grid grid_tabla_emp_total) {
	this.grid_tabla_emp_total = grid_tabla_emp_total;
}
  
public void cancelarImportarTotal() {
	dia_valida_empleado_total.cerrar();
	dia_importar_total.cerrar();
	upl_archivo.limpiar();
	edi_mensajes.setValue("");
    //tab_planificacion_hxe.setCondicion("ide_cobph=-1");
    //tab_planificacion_hxe_observacion.setCondicion("ide_cobpo=-1");
    //tab_planificacion_hxe.ejecutarSql();
    //tab_planificacion_hxe_observacion.ejecutarSql();
	utilitario.addUpdate("dia_valida_empleado_total,dia_importar_total,edi_mensajes");
	
	String valor="DELETE FROM asi_horario_mes_notificaciones_historial ";
	utilitario.getConexion().ejecutarSql(valor);

}  

  

public void aceptarImportarTotal() {

	if (upl_archivo.getNombreReal() == null) {
		utilitario.agregarMensajeInfo("Debe seleccionar un archivo", "");
		return;
	}
	if (edi_mensajes.getValue() == null || edi_mensajes.getValue().toString().isEmpty()) {
		utilitario.agregarMensajeInfo("Debe validar el archivo", "");
		return;
	} else {
		if (edi_mensajes.getValue().toString().indexOf("#ff0000") > 0) {
			utilitario.agregarMensajeInfo("Debe solucionar los errores del archivo", "");
			return;
		}
	}
	
	String emp="";
	TablaGenerica tab_empleado=null;
	String validacionEmpleado="";
	String valor="";
	/*if (ide_empleados_crear.length()==0) {
		
	}else {
		validacionEmpleado=ide_empleados_crear.substring((ide_empleados_crear.length())-1,ide_empleados_crear.length());
		if (validacionEmpleado.equals(",")) {
			utilitario.getConexion().ejecutarSql("DELETE FROM  asi_horario_mes_empleado WHERE IDE_GEMES="+mes+" AND IDE_GEANI="+anio+" AND IDE_GTEMP IN("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+")");		
			tab_empleado=utilitario.consultar("select ide_gtemp, documento_identidad_gtemp from gth_empleado where ide_gtemp in("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+")");		
		
		}else {
			utilitario.getConexion().ejecutarSql("DELETE FROM  asi_horario_mes_empleado WHERE IDE_GEMES="+mes+" AND IDE_GEANI="+anio+" AND IDE_GTEMP IN("+ide_empleados_crear+")");					
			tab_empleado=utilitario.consultar("select ide_gtemp, documento_identidad_gtemp from gth_empleado where ide_gtemp in("+ide_empleados_crear+")");
		}
	}*/
	
	
	
	
	tab_empleado=utilitario.consultar("select ide_gtemp, documento_identidad_gtemp from gth_empleado where ide_gtemp in("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+")");
for (int i = 0; i < tab_emp_total.getTotalFilas(); i++) {
	 TablaGenerica tab_anio_mes=utilitario.consultar("SELECT "
	 			+ "PERIODO.ide_ashop,"
	 			+ "MES.IDE_GEMES,MES.DETALLE_GEMES, "
	 			+ "ANIO.IDE_geani,ANIO.DETALLE_geani "
		  		+ "FROM asi_horario_periodo PERIODO "
		  		+ "LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI "
		  		+ "LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES "
		  		+ "WHERE ANIO.ide_GEANI="+anio+" AND MES.IDE_GEMES="+mes
		  		+ "ORDER BY ANIO.DETALLE_geani ASC,MES.IDE_GEMES ASC ");
	String fecha_inicio="",fecha_fin="";
	int mesValida=0,diaValida=0;

if (Integer.parseInt(tab_anio_mes.getValor("IDE_GEMES"))<10) {
if ((int)Double.parseDouble(tab_emp_total.getValor(i,"DIA").toString())<10) {
	 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-0"+tab_anio_mes.getValor("IDE_GEMES")+"-0"+(int)Double.parseDouble(tab_emp_total.getValor(i,"DIA"));

}else {
	 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-0"+tab_anio_mes.getValor("IDE_GEMES")+"-"+(int)Double.parseDouble(tab_emp_total.getValor(i,"DIA"));

}
}else {
if ((int)Double.parseDouble(tab_emp_total.getValor(i,"DIA"))<10) {
	 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-"+tab_anio_mes.getValor("IDE_GEMES")+"-0"+(int)Double.parseDouble(tab_emp_total.getValor(i,"DIA"));

}else {
	 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-"+tab_anio_mes.getValor("IDE_GEMES")+"-"+(int)Double.parseDouble(tab_emp_total.getValor(i,"DIA"));

}

}

	utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_notificaciones set activo_ashmn=false WHERE IDE_GTEMP="+tab_emp_total.getValor(i,"ide_gtemp")+" AND fecha_ashmn='"+fecha_inicio+"' and ide_gemes="+mes+" and ide_geani="+anio+" and activo_ashmn=true");				

}

	for (int x = 0; x < tab_emp_total.getTotalFilas(); x++) {
		TablaGenerica tab_codigo = utilitario.consultar(servicioCodigoMaximo("asi_horario_mes_notificaciones", "ide_ashmn"));
		String codigo=tab_codigo.getValor("codigo");
		
	/*	TablaGenerica tablaRegistro=null;
		 tablaRegistro=utilitario.consultar("SELECT ide_ashmn, ide_gtemp, fecha_ashmn, ide_astur, notificacion_ashmn, "
				+ "ide_usua, activo_ashmn, ide_geani, ide_gemes, ide_asjei "
				+ "FROM asi_horario_mes_notificaciones "
				+ "where fecha_ashmn='"+tab_emp_total.getValor("fecha_ashmn")+"' and ide_gtemp="+tab_emp_total.getValor("ide_gtemp")+" and notificacion_ashmn=false and activo_ashmn=true "
				+ "order by ide_ashmn");*/

		 TablaGenerica tab_anio_mes=utilitario.consultar("SELECT "
		 			+ "PERIODO.ide_ashop,"
		 			+ "MES.IDE_GEMES,MES.DETALLE_GEMES, "
		 			+ "ANIO.IDE_geani,ANIO.DETALLE_geani "
			  		+ "FROM asi_horario_periodo PERIODO "
			  		+ "LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI "
			  		+ "LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES "
			  		+ "WHERE ANIO.ide_GEANI="+anio+" AND MES.IDE_GEMES="+mes
			  		+ "ORDER BY ANIO.DETALLE_geani ASC,MES.IDE_GEMES ASC ");
		String fecha_inicio="",fecha_fin="";
		int mesValida=0,diaValida=0;

	if (Integer.parseInt(tab_anio_mes.getValor("IDE_GEMES"))<10) {
	if ((int)Double.parseDouble(tab_emp_total.getValor(x,"DIA").toString())<10) {
		 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-0"+tab_anio_mes.getValor("IDE_GEMES")+"-0"+(int)Double.parseDouble(tab_emp_total.getValor(x,"DIA"));

	}else {
		 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-0"+tab_anio_mes.getValor("IDE_GEMES")+"-"+(int)Double.parseDouble(tab_emp_total.getValor(x,"DIA"));

	}
	}else {
	if ((int)Double.parseDouble(tab_emp_total.getValor(x,"DIA"))<10) {
		 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-"+tab_anio_mes.getValor("IDE_GEMES")+"-0"+(int)Double.parseDouble(tab_emp_total.getValor(x,"DIA"));

	}else {
		 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-"+tab_anio_mes.getValor("IDE_GEMES")+"-"+(int)Double.parseDouble(tab_emp_total.getValor(x,"DIA"));

	}

	}
		
		
		
	valor="INSERT INTO asi_horario_mes_notificaciones( "
			+ "ide_ashmn, ide_gtemp, fecha_ashmn, ide_astur, notificacion_ashmn, "
			+ "ide_usua, activo_ashmn, usuario_ingre, fecha_ingre, usuario_actua, " 
			+ "fecha_actua, hora_ingre, hora_actua, ide_geani, ide_gemes, ide_asjei)   "
			+ "VALUES ("+codigo+", "+tab_emp_total.getValor(x,"ide_gtemp")+", '"+fecha_inicio+"', "+0+", "+false+", "
			+ "'"+utilitario.getVariable("ide_usua")+"', "+true+", '"+utilitario.getVariable("ide_usua")+"', '"+utilitario.getFechaActual()+"', "+null+", ";
			if (tipo_perfil.equals("1")) {
				valor+=	""+null+", '"+utilitario.getHoraActual()+"', "+null+", "+anio+", "+mes+", "+tabJefeInmediato.getValor("ide_asjei")+")";
			}else {
				valor+=	""+null+", '"+utilitario.getHoraActual()+"', "+null+", "+anio+", "+mes+", "+ide_asjei+")";
			}
			ide_empleados.append(codigo+",");

	utilitario.getConexion().ejecutarSql(valor);
	}

	
	
	
	
	TablaGenerica tablaRegistro=utilitario.consultar("SELECT ide_ashmn, ide_gtemp, fecha_ashmn, ide_astur, notificacion_ashmn, "
			+ "ide_usua, activo_ashmn, ide_geani, ide_gemes, ide_asjei "
			+ "FROM asi_horario_mes_notificaciones "
			+ "where ide_ashmn in("+ide_empleados.substring(0,(ide_empleados.length()-1))+") and notificacion_ashmn=false "
			+ "order by ide_ashmn");
	
	
	if (tablaRegistro.getTotalFilas()>0) {
		for (int i = 0; i < tablaRegistro.getTotalFilas(); i++) {
		//	eliminarNotificacion(tablaRegistro.getValor(i,"ide_gtemp"), tablaRegistro.getValor(i,"fecha_ashmn"), ide_gemes, ide_geani);	
			importarValoresRubro(lis_importa,1,i,tablaRegistro.getValor(i,"ide_gtemp"),tablaRegistro.getValor(i,"ide_ashmn"));
			importarValoresRubro(lis_turno,2,i,tablaRegistro.getValor(i,"ide_gtemp"),tablaRegistro.getValor(i,"ide_ashmn"));
			}
		
	}
	

	utilitario.getConexion().ejecutarSql("DELETE FROM asi_horario_mes_notificaciones_historial ");
	validacionEmpleado=ide_empleados_crear.substring((ide_empleados_crear.length())-1,ide_empleados_crear.length());

	tab_horario_novedades.setCondicion("IDE_GTEMP in("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+") and IDE_GEANI="+anio+" AND IDE_GEMES="+mes+" AND notificacion_ashmn=false and activo_ashmn=true "
			+ "and ide_ashmn in("+ide_empleados.substring(0,(ide_empleados.length()-1))+")");
	tab_horario_novedades.ejecutarSql();
	tab_horario_novedades.actualizar();
	tab_horario.setCondicion("IDE_GTEMP in("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+") and IDE_GEANI="+anio+" AND IDE_GEMES="+mes);
	tab_horario.ejecutarSql();
	tab_horario.actualizar();
	utilitario.addUpdate("tab_horario,tab_horario_novedades");
	dia_importar_total.cerrar();
	dia_valida_empleado_total.cerrar();
	utilitario.agregarMensaje("Valores Importados con exito", "Se ha importado los valores correctamente");

}
  




public boolean importarValoresRubro(List lis_importa,int tipo_hora_extra,int i,String ide_gtemp,String ide_ashmn){

	
	//	eliminarNotificacion(tablaRegistro.getValor(i,"ide_gtemp"), tablaRegistro.getValor(i,"fecha_ashmn"), ide_gemes, ide_geani);	

	
	String str_sql="";
	str_sql="SELECT cbph.ide_ashmn,  EMP.DOCUMENTO_IDENTIDAD_GTEMP,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||   (case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' ||  "
			+ "EMP.PRIMER_NOMBRE_GTEMP || ' ' ||  (case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS,   anio.detalle_geani,mes.detalle_gemes  " //,cbph.ide_asjei
			+ "FROM asi_horario_mes_notificaciones cbph  "
			+ "left join gth_empleado emp on emp.ide_gtemp=cbph.ide_gtemp   "
			+ "left join gen_anio anio on anio.ide_geani=cbph.ide_geani  "
			+ "left join gen_mes mes on mes.ide_gemes=cbph.ide_gemes  "
			+ "where mes.ide_gemes="+mes+" and anio.ide_geani="+anio+" and cbph.ide_gtemp in("+ide_gtemp+") and cbph.ide_ashmn in("+ide_ashmn+") " 
			+ " order by EMP.APELLIDO_PATERNO_GTEMP,EMP.APELLIDO_MATERNO_GTEMP,EMP.PRIMER_NOMBRE_GTEMP,EMP.SEGUNDO_NOMBRE_GTEMP";
			//ide_empleados_crear.substring((ide_empleados_crear.length())-1,ide_empleados_crear.length()
	TablaGenerica tab_emp_dep=utilitario.consultar(str_sql);
	//jefe_inmediato_planificacion
	for (int j = 0; j < tab_emp_dep.getTotalFilas(); j++) {	
		String str_documento=tab_emp_dep.getValor(j, "DOCUMENTO_IDENTIDAD_GTEMP");
		String str_valor=null;
		for (int k = 0; k < lis_importa.size(); k++) {						
			//busco el valor
			if(str_documento.equalsIgnoreCase(((String[])lis_importa.get(k))[0])){
				str_valor=((String[])lis_importa.get(k))[1];
				lis_importa.remove(k);
				break;
			}
		}			
		//Metodo Insertar Resumen
		if(str_valor!=null){
			String ide_cobrh="";
			ide_cobrh=tab_emp_dep.getValor(j, "ide_ashmn");
			if (str_valor.equals("0") || str_valor.equals("0.0") || str_valor.equals("0.00")) {
				str_valor=null;
			}
			 TablaGenerica tab_anio_mes=utilitario.consultar("SELECT "
			 			+ "PERIODO.ide_ashop,"
			 			+ "MES.IDE_GEMES,MES.DETALLE_GEMES, "
			 			+ "ANIO.IDE_geani,ANIO.DETALLE_geani "
				  		+ "FROM asi_horario_periodo PERIODO "
				  		+ "LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI "
				  		+ "LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES "
				  		+ "WHERE ANIO.ide_GEANI="+anio+" AND MES.IDE_GEMES="+mes
				  		+ "ORDER BY ANIO.DETALLE_geani ASC,MES.IDE_GEMES ASC ");
			String fecha_inicio="",fecha_fin="";
			int mesValida=0,diaValida=0;
			if (Integer.parseInt(tab_anio_mes.getValor("IDE_GEMES"))<10) {
				if ((int)Double.parseDouble(str_valor.toString())<10) {
					 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-0"+tab_anio_mes.getValor("IDE_GEMES")+"-0"+(int)Double.parseDouble(str_valor.toString());

				}else {
					 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-0"+tab_anio_mes.getValor("IDE_GEMES")+"-"+(int)Double.parseDouble(str_valor.toString());

				}
			}else {
				if ((int)Double.parseDouble(str_valor.toString())<10) {
					 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-"+tab_anio_mes.getValor("IDE_GEMES")+"-0"+(int)Double.parseDouble(str_valor.toString());

				}else {
					 fecha_inicio=tab_anio_mes.getValor("DETALLE_geani")+"-"+tab_anio_mes.getValor("IDE_GEMES")+"-"+(int)Double.parseDouble(str_valor.toString());
	
				}

			}
			if (tipo_hora_extra==1) {
			    	utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_notificaciones set fecha_ashmn='"+fecha_inicio+"' where ide_ashmn=" + ide_cobrh);				
			}else {
				utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_notificaciones set ide_astur="+str_valor+" where ide_ashmn=" + ide_cobrh);				

			}
			
		}
	}
	String str_msg=utilitario.getConexion().ejecutarListaSql();
	if (str_msg.isEmpty()){
		return true;
	}
	return false;

}


private Object[] getAcumulado(String documento) {
	for (int i = 0; i < tab_emp_total.getTotalFilas(); i++) {
		if (tab_emp_total.getValor(i, "DOCUMENTO_IDENTIDAD_GTEMP").equalsIgnoreCase(documento)) {
			Object[] obj = new Object[5];
			obj[0] = i;
			obj[1] = tab_emp_total.getValor(i, "DIA");
			obj[2] = tab_emp_total.getValor(i, "TURNO");

			for (int k = 0; k < lis_importa.size(); k++) {
				// busco posicion en la lista
				if (documento.equalsIgnoreCase(((String[]) lis_importa.get(k))[0])) {
					obj[3] = k;
					break;
				}
			}
			
			for (int k = 0; k < lis_turno.size(); k++) {
				// busco posicion en la lista
				if (documento.equalsIgnoreCase(((String[]) lis_turno.get(k))[0])) {
					obj[4] = k;
					break;
				}
			}

			return obj;
		}
	}
	return null;
}


 	public String servicioCodigoMaximo(String tabla,String ide_primario){
 		
 		String maximo="Select 1 as ide,(case when max("+ide_primario+") is null then 0 else max("+ide_primario+") end) + 1 as codigo from "+tabla;
 		return maximo;
 	}

 	
	public void enviarNotificacion(){	
		
		
		//ide_geedp_activo=ser_gestion.getIdeContratoActivo(aut_empleado.getValor());

				if(tab_horario_novedades.getTotalFilas()>0){
					TablaGenerica tabAnio=null;
					if (anio==null || anio.equals("") || anio.isEmpty()) {
						tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+ide_geani+")");
					}else {
						tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anio+")");
					}
					int nuevoMes=0;
					if (mes==null || mes.equals("") || mes.isEmpty()) {
						nuevoMes=ide_gemes;

					}else {
						nuevoMes=Integer.parseInt(mes);

					}
					
							
					String fecha="",fecFin="";
				if (nuevoMes>9) {
								fecha=tabAnio.getValor("detalle_geani")+"-"+nuevoMes+"-01";
								fecFin=tabAnio.getValor("detalle_geani")+"-"+nuevoMes+"-"+utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha));
							}else {
								fecha=tabAnio.getValor("detalle_geani")+"-0"+nuevoMes+"-01";
								fecFin=tabAnio.getValor("detalle_geani")+"-0"+nuevoMes+"-"+utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha));
						}

							//ultimoDia=utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha));
							String fec_limite="",fec_ini="",fec_ini_="";
							//fec_ini_=diaSemana(utilitario.getFechaActual());
							//fec_ini=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fec_ini_), -7));
							fec_limite=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fecFin), 5));
							String fecComparacion="";
							//fecComparacion=tab_horario_novedades.getValor("fecha_ashmn");
									

				//	if (utilitario.getFechaActual().compareTo(fecha)>=0 && utilitario.getFechaActual().compareTo(fec_limite)<=0) {
					//			}else {
					//				utilitario.agregarMensaje("No se puede cambiar de estado","Fuera de Rango de fechas");
					//				return;/

								//}			
					
					
					
					
					for (int i = 0; i < tab_horario_novedades.getTotalFilas(); i++) {

						if (tab_horario_novedades.getValor(i,"ide_ashmn")==null || tab_horario_novedades.getValor(i,"ide_ashmn").isEmpty() || tab_horario_novedades.getValor(i,"ide_ashmn").equals("")) {
							utilitario.agregarMensajeError("No se puede actualizar", "Debe guardar el registro");
							return;
						}
						}
					con_guardar.setMessage("Esta Seguro de Enviar Notificacion");
					con_guardar.setTitle("CONFIRMACION ENVIO DE NOTIFICACION");
					con_guardar.getBot_aceptar().setMetodo("aceptarEnviarNotificacion");
					con_guardar.dibujar();
					utilitario.addUpdate("con_guardar");
				}else{
					utilitario.agregarMensajeInfo("No se puede Aprobar ", "No contiene Solicitudes Pendientes");
					return;
				}		
	
	}
	
	
	public void aceptarEnviarNotificacion(){		

	try {
	    StringBuilder ide_empleados_crear=new StringBuilder(); 			
	    int dia=0;
		if(tab_horario_novedades.getTotalFilas()>0){
			for (int i = 0; i < tab_horario_novedades.getTotalFilas(); i++) {
				TablaGenerica tabHorarioAsignacion=null;
				dia=utilitario.getDia(tab_horario_novedades.getValor(i,"fecha_ashmn"));
				tabHorarioAsignacion=utilitario.consultar("select ide_ashme,ide_gtemp,ide_gemes,dia"+dia+" "
									+ "from asi_horario_mes_empleado "
									+ "where ide_gtemp in("+tab_horario_novedades.getValor("ide_gtemp")+") and ide_geani="+tab_horario_novedades.getValor(i,"ide_geani")+" and ide_gemes in("+tab_horario_novedades.getValor(i,"ide_gemes")+")");
							if (tabHorarioAsignacion.getTotalFilas()>0) {
								utilitario.getConexion().ejecutarSql("UPDATE asi_horario_mes_empleado SET dia"+dia+"="+tab_horario_novedades.getValor(i,"ide_astur")+" WHERE ide_ashme="+tabHorarioAsignacion.getValor("ide_ashme"));
							}	
					}
				
			
			

		for (int i = 0; i < tab_horario_novedades.getTotalFilas(); i++) {
			utilitario.getConexion().agregarSqlPantalla("update asi_horario_mes_notificaciones set notificacion_ashmn=true where ide_ashmn="+tab_horario_novedades.getValor(i,"ide_ashmn"));
			ide_empleados_crear.append(tab_horario_novedades.getValor(i,"ide_gtemp")+",");
		}
		}
		guardarPantalla();
		String mes_="",anio_="";
		if (ide_geani==0) {
			mes_=mes;
			anio_=anio;
		}else {
			mes_=""+ide_gemes;
			anio_=""+ide_geani;
		}
		
		tab_horario_novedades.setCondicion("IDE_GTEMP in("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+") and IDE_GEANI="+anio_+" AND IDE_GEMES="+mes_+" AND notificacion_ashmn=false and activo_ashmn=true");
		tab_horario_novedades.ejecutarSql();
		tab_horario_novedades.actualizar();
		tab_horario.setCondicion("IDE_GTEMP in("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+") and IDE_GEANI="+anio_+" AND IDE_GEMES="+mes_);
		tab_horario.ejecutarSql();
		tab_horario.actualizar();
		
		
		
		tab_horario_consulta.setSql("SELECT mes.ide_ashmn,EMP.APELLIDO_PATERNO_GTEMP || ' ' ||  " +
				  "(case when EMP.APELLIDO_MATERNO_GTEMP is null then '' else EMP.APELLIDO_MATERNO_GTEMP end) || ' ' || " +
				  "EMP.PRIMER_NOMBRE_GTEMP || ' ' || " +
				  "(case when EMP.SEGUNDO_NOMBRE_GTEMP is null then '' else EMP.SEGUNDO_NOMBRE_GTEMP end) AS NOMBRES_APELLIDOS, mes.fecha_ashmn, turno.nom_astur || '' || turno.descripcion_astur as descripcion_astur "
					+ "FROM asi_horario_mes_notificaciones mes "
					+ "left join asi_turnos turno on turno.ide_astur=mes.ide_astur "
					+ "left join gth_empleado emp on emp.ide_gtemp=mes.ide_gtemp "
				+ "where mes.ide_geani="+anio_+" and mes.ide_gemes="+mes_+" and mes.notificacion_ashmn=true and mes.IDE_GTEMP in("+ide_empleados_crear.substring(0,(ide_empleados_crear.length()-1))+") "
						+ "order by mes.ide_ashmn desc " );
		tab_horario_consulta.ejecutarSql();
		tab_horario_consulta.actualizar();
		
		con_guardar.cerrar();		
 	
		
	} catch (Exception e) {
		System.out.println("Error en  aprobar solicitud aceptarAprobarSolicitud:  "+e.getMessage());
	}
	
 
}




	public Confirmar getCon_guardar() {
		return con_guardar;
	}




	public void setCon_guardar(Confirmar con_guardar) {
		this.con_guardar = con_guardar;
	}




	public StringBuilder getIde_empleados() {
		return ide_empleados;
	}




	public void setIde_empleados(StringBuilder ide_empleados) {
		this.ide_empleados = ide_empleados;
	}
		
	
	
	 public void eliminarNotificacion(String ide_gtemp,String fecha, int ide_gemes,int ide_geani){
		 
utilitario.getConexion().ejecutarSql("DELETE FROM asi_horario_mes_notificaciones WHERE IDE_GTEMP="+ide_gtemp+" AND fecha_ashmn='"+fecha+"' and ide_gemes="+ide_gemes+" and ide_geani="+ide_geani);				

		 
	 }
	 
	 
		public String diaSemana (String fecha)
	    {
		 
		 int dia=utilitario.getDia(fecha);
		 int mes=utilitario.getMes(fecha);
		 int anioEscogido=utilitario.getAnio(fecha);
	    	String mesMenor="";
	        int Valor_dia=0;
	        String diaMenor="";
	        String retornoDia="";
	        if (dia<10) {
				diaMenor="0";
			}else {
				diaMenor="";
			}
	        
	        if (mes<10) {
				mesMenor="0";
			}else {
				mesMenor="";
			}
	        

			String fechaHoraBiometrico=anioEscogido+"-"+mesMenor+mes+"-"+diaMenor+dia;
		    //Calendario 
			Calendar c = Calendar.getInstance();
		    //Setteo la fecha y hora timbrada en el biometrico de tipo calendario
			c.setTime(getFechaAsyyyyMMdd(fechaHoraBiometrico)); 
	    	
	    	int diaSemana = c.get(Calendar.DAY_OF_WEEK);
	        if (diaSemana == 1) {
	           // Valor_dia = "Domingo";
	            Valor_dia = 1;
	            retornoDia=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(utilitario.getFechaActual()), -6));

	        } else if (diaSemana == 2) {
	           // Valor_dia = "Lunes";
	            Valor_dia = 2;
	            retornoDia=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(utilitario.getFechaActual()), -0));

	        } else if (diaSemana == 3) {
	           // Valor_dia = "Martes";
	            Valor_dia = 3;
	            retornoDia=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(utilitario.getFechaActual()), -1));

	        } else if (diaSemana == 4) {
	           // Valor_dia = "Miercoles";
	            Valor_dia = 4;
	            retornoDia=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(utilitario.getFechaActual()), -2));
 
	        } else if (diaSemana == 5) {
	           // Valor_dia = "Jueves";
	            Valor_dia = 5;
	            retornoDia=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(utilitario.getFechaActual()), -3));

	        } else if (diaSemana == 6) {
	          // Valor_dia = "Viernes";
	            Valor_dia = 6;
	            retornoDia=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(utilitario.getFechaActual()), -4));

	        } else if (diaSemana == 7) {
	            //Valor_dia = "Sabado";
	            Valor_dia = 7;
	            retornoDia=	utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(utilitario.getFechaActual()), -5));

	        }
	        return retornoDia;
	    }
	  
	 
	 
	 
	 
		public void cambiarFecha() {
			//tab_horario_novedades.modificar(evt);
			if (tab_horario_novedades.getValor("fecha_ashmn") == null) {
				return;
			}
			
			if (aut_empleado.getValor() == null) {
				return;
			}
			
			TablaGenerica tabAnioMes=utilitario.consultar("SELECT MES.IDE_GEMES, ANIO.IDE_geani "
			  		+ "FROM asi_horario_periodo PERIODO "
			  		+ "LEFT JOIN GEN_ANIO ANIO ON ANIO.IDE_GEANI=PERIODO.IDE_GEANI "
			  		+ "LEFT JOIN GEN_MES MES ON MES.IDE_GEMES=PERIODO.IDE_GEMES "
			  		+ "WHERE PERIODO.ide_ashop="+aut_anio_mes.getValor());
			String anio="",mes="";
			anio=tabAnioMes.getValor("ide_geani");
			mes=tabAnioMes.getValor("ide_gemes");
			ide_geani=Integer.parseInt(anio);
			ide_gemes=Integer.parseInt(mes);
			
			

			TablaGenerica tabAnio=utilitario.consultar("select ide_geani,detalle_geani from gen_anio where ide_geani in("+anio+")");
			int nuevoMes=0;
			nuevoMes=Integer.parseInt(mes);
			String fecha="";
		if (Integer.parseInt(mes)>9) {
						fecha=tabAnio.getValor("detalle_geani")+"-"+nuevoMes+"-01";
					}else {
						fecha=tabAnio.getValor("detalle_geani")+"-0"+nuevoMes+"-01";
					}

					ultimoDia=utilitario.getDia(utilitario.getUltimoDiaMesFecha(fecha));
					String fec_limite="",fec_ini="",fec_ini_="";
					fec_ini_=diaSemana(utilitario.getFechaActual());
					fec_ini=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fec_ini_), -7));
					fec_limite=utilitario.DeDateAString(utilitario.sumarDiasFecha(utilitario.DeStringADate(fec_ini_), 7));
					String fecComparacion="";
					fecComparacion=tab_horario_novedades.getValor("fecha_ashmn");
							

			if (fecComparacion.compareTo(fec_ini)>=0 && fecComparacion.compareTo(fec_limite)<=0) {
						}else {
							utilitario.agregarMensaje("La fecha: " + fecComparacion  + " esta fuera de rango: "+fec_ini+" y "+fec_limite,"");
							tab_horario_novedades.setValor("fecha_ashmn", null);
							utilitario.addUpdateTabla(tab_horario_novedades, "fecha_ashmn", null);
							utilitario.addUpdate("tab_horario_novedades");
							return;

						}			
			
			

		}
	 
		 private Date getFechaAsyyyyMMdd(String fecha){
			    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    Date fechaDate = new Date();
			    try {	
			    	fechaDate = df.parse(fecha);
			    	return fechaDate;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    return null;

		    } 
	
}
