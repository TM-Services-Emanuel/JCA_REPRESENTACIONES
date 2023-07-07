package com.example.latinodistribuidora.Modelos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fecha {
    
    public static String fechaFormulario(){
        String fechas = null;
        try {
            Date hoy = new Date();
            SimpleDateFormat fe = new SimpleDateFormat("dd/MM/yyyy");
            fechas = fe.format(hoy);
        } catch (Exception e) {
            System.out.println("Error al ver la fecha");
        }
        return fechas;
    }
    
    public static String fechaCFormulario(String fecha){
        String fechas = null;
        try {
            SimpleDateFormat fe = new SimpleDateFormat("dd/MM/yy");
            fechas = fe.format(fe.parse(fecha));
        } catch (ParseException e) {
            System.out.println("No se pudo convertir la fecha completa"+e.getMessage());
        }
        return fechas;
    }

    public static String formatoFecha(String fecha) {
        String fechas = null;
        try {
            SimpleDateFormat fe = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat myFecha = new SimpleDateFormat("20" + "yy-MM-dd");

            fechas = myFecha.format(fe.parse(fecha));
        } catch (ParseException e) {
            System.out.println("No se pudo convertir");
        }
        return fechas;
    }
    
    public static String formatoFechaVM(String fecha) {
        String fechas = null;
        try {
            SimpleDateFormat fe = new SimpleDateFormat("dd/M/yy");
            SimpleDateFormat myFecha = new SimpleDateFormat("dd/MM/yyyy");

            fechas = myFecha.format(fe.parse(fecha));
        } catch (ParseException e) {
            System.out.println("No se pudo convertir");
        }
        return fechas;
    }
    public static String formatoFechaVMR(String fecha) {
        String fechas = null;
        try {
            SimpleDateFormat fe = new SimpleDateFormat("dd/M/yy");
            SimpleDateFormat myFecha = new SimpleDateFormat("yyyy/MM/dd");

            fechas = myFecha.format(fe.parse(fecha));
        } catch (ParseException e) {
            System.out.println("No se pudo convertir");
        }
        return fechas;
    }
    
    public static String formatoFechaVMRInverter(String fecha) {
        String fechas = null;
        try {
            SimpleDateFormat fe = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat myFecha = new SimpleDateFormat("d/M/yyyy");

            fechas = myFecha.format(fe.parse(fecha));
        } catch (ParseException e) {
            System.out.println("No se pudo convertir");
        }
        return fechas;
    }
    public static String formatoFechaN(String fecha) {
        String fechas = null;
        try {
            SimpleDateFormat fe = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat myFecha = new SimpleDateFormat("dd/MM/20"+"yy");

            fechas = myFecha.format(fe.parse(fecha));
        } catch (ParseException e) {
            System.out.println("No se pudo convertir");
        }
        return fechas;
    }
    public static String formatoFechaD(Date fecha) {
        String fechas = null;
        try {
            SimpleDateFormat fe = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat myFecha = new SimpleDateFormat("yyyy-MM-dd");

            fechas = myFecha.format((fecha));
        }catch (Exception e) {
            System.out.println("No se pudo convertir");
        }
        return fechas;
    }
    
    public static String formatoFechaF(Date fecha) {
        String fechas = null;
        try {
            SimpleDateFormat fe = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat myFecha = new SimpleDateFormat("dd/MM/20"+"yy");

            fechas = myFecha.format((fecha));
        }catch (Exception e) {
            System.out.println("No se pudo convertir");
        }
        return fechas;
    }
    
    public static String formatoFechaFF(String fecha) {
        String fechas = null;
        try {
            SimpleDateFormat fe = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat myFecha = new SimpleDateFormat("dd/MM/20"+"yy");

            fechas = myFecha.format(fe.parse(fecha));
        }catch (Exception e) {
            System.out.println("No se pudo convertir");
        }
        return fechas;
    }
    
    public static String fechaCompleta(String fecha){
        String fechas = null;
        String horas = null;
        String horaCompleta = null;
        try {
            Date hoy = new Date();
            SimpleDateFormat df = new SimpleDateFormat("H:mm:ss");
            SimpleDateFormat fe = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat myFecha = new SimpleDateFormat("20" + "yy-MM-dd");
            fechas = myFecha.format(fe.parse(fecha));
            horas = df.format(hoy);
            horaCompleta = fechas + " " + horas;
        } catch (ParseException e) {
            System.out.println("No se pudo convertir la fecha completa");
        }
        return horaCompleta;
    }
    
    public static String fechaReporte(){
        String fechas = null;
        try {
            Date hoy = new Date();
            SimpleDateFormat fe = new SimpleDateFormat("20" + "yy-MM-dd");
            fechas = fe.format(hoy);
        } catch (Exception e) {
            System.out.println("Error al ver la fecha");
        }
        return fechas;
    }
    
    public static String fechaCorrecta(){
        String fechas = null;
        try {
            Date hoy = new Date();
            SimpleDateFormat fe = new SimpleDateFormat("yyyy-MM-dd");
            fechas = fe.format(hoy);
        } catch (Exception e) {
            System.out.println("Error al ver la fecha");
        }
        return fechas;
    }
    
    public static String fechaCorrectaFachada(){
        String fechas = null;
        try {
            Date hoy = new Date();
            SimpleDateFormat fe = new SimpleDateFormat("dd/MM/yyyy");
            fechas = fe.format(hoy);
        } catch (Exception e) {
            System.out.println("Error al ver la fecha");
        }
        return fechas;
    }
    
    public static String darHora(){
        String hora = null;
        try {
            Date hoy = new Date();
            SimpleDateFormat df =  new SimpleDateFormat("H:mm:ss");
            hora = df.format(hoy);
            System.out.println("La hora es "+hora);
        } catch (Exception e) {
            System.out.println("No se pudo mostrar la hora");
        }
        return hora;
    }
    
    public static String darHoraSinSS(){
        String hora = null;
        try {
            Date hoy = new Date();
            SimpleDateFormat df =  new SimpleDateFormat("HH:mm");
            hora = df.format(hoy);
            System.out.println("La hora es "+hora);
        } catch (Exception e) {
            System.out.println("No se pudo mostrar la hora");
        }
        return hora;
    }
    
    public static String FormatoHoraSinSS(Date hor){
        String hora = null;
        try {
            SimpleDateFormat df =  new SimpleDateFormat("HH:mm");
            hora = df.format(hor);
        } catch (Exception e) {
            System.out.println("No se pudo mostrar la hora "+e.getMessage());
        }
        return hora;
    }

    
    public static String FormatoHoraSinSSString(String hor){
        String hora = null;
        try {
            SimpleDateFormat dfs =  new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat df =  new SimpleDateFormat("HH:mm");
            hora = df.format(dfs.parse(hor));
        } catch (Exception e) {
            System.out.println("No se pudo mostrar la hora "+e.getMessage());
        }
        return hora;
    }
}
