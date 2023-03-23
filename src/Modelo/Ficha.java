package Modelo;


public class Ficha {
    public static int ESTADO_VACIO = 0;
    public static int ESTADO_OCUPADO = 1;
    public static int ESTADO_GOLPEADO = 2;
    public static int ESTADO_OCULTO = -1;
    
    private int id_barcos;
    private int fila;
    private int columna;
    private int orientacion;
    private int estado;

    public Ficha(int fila, int columna, int estado) {
        this.fila = fila;
        this.columna = columna;
        this.estado = estado;
    }

    public int getId_barcos() {
        return id_barcos;
    }

    public void setId_barcos(int id_barcos) {
        this.id_barcos = id_barcos;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public int getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(int orientacion) {
        this.orientacion = orientacion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        if (estado == ESTADO_OCUPADO) {
            return "B" + id_barcos;
        }else if(estado == ESTADO_VACIO){
            return "??";
        }else if(estado == ESTADO_OCULTO){
            return "--";
        }else if(estado == ESTADO_GOLPEADO){
            return "XX";
        }
        return "";
    }
    
    
    
}
