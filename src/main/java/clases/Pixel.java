/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author Josue Alvarez M
 */
import java.awt.Color;
import java.awt.Graphics2D;
import particulas.Particula;

public class Pixel{
    private boolean actualizar = false;
    private Particula particula = null;
    
    private final int fila;
    private final int columna;
    
    private int x;
    private int y;
    private int tam;
    
    private int rojo, verde, azul;
    private Color color = Color.WHITE;
    
    public static final int ROJO_PRED = 255;
    public static final int VERDE_PRED = 255;
    public static final int AZUL_PRED = 255;
    
    protected final Color color_pred = new Color(this.ROJO_PRED, this.VERDE_PRED, this.AZUL_PRED);
    
    public int bandera;
    
    protected static final int luz_intencidadPred = 125;
    protected int luz_intencidad = Pixel.luz_intencidadPred;
    protected int luz_rojo = 0;
    protected int luz_verde = 0;
    protected int luz_azul = 0;
    private Color luz_color = Color.BLACK;
    
    public Pixel(int x, int y, int tam, int fila, int columna) {
        setColor(this.ROJO_PRED, this.VERDE_PRED, this.AZUL_PRED);
        
        this.x = x;
        this.y = y;
        this.tam = tam;
        
        this.fila = fila;
        this.columna = columna;
    }
    
    public void actualizarColor(){
        // se definen los colores basicos de la particula
        if(this.particula != null)
            this.rojo = this.particula.getRojo();
        if(this.particula != null)
            this.verde = this.particula.getVerde();
        if(this.particula != null)
            this.azul = this.particula.getAzul();
        
        if(this.rojo < 0)
            this.rojo = 0;
        if(this.verde < 0)
            this.verde = 0;
        if(this.azul < 0)
            this.azul = 0;

        if(this.rojo > 255)
            this.rojo = 255;
        if(this.verde > 255)
            this.verde = 255;
        if(this.azul > 255)
            this.azul = 255;
        
        this.color =  new Color(this.rojo, this.verde, this.azul);
        
        // se verifica que la intencidad no esté fuera del limite
        if(this.luz_intencidad < 0)
            this.luz_intencidad = 0;
        
        // oscurece los colores de la particula
        int combioColor = 255 - this.luz_intencidad;
        
        this.luz_rojo += this.rojo - combioColor;
        this.luz_verde += this.verde - combioColor;
        this.luz_azul += this.azul - combioColor;
        
        if(this.luz_rojo < 0)
            this.luz_rojo = 0;
        if(this.luz_verde < 0)
            this.luz_verde = 0;
        if(this.luz_azul < 0)
            this.luz_azul = 0;
        
        if(this.luz_rojo > 255)
            this.luz_rojo = 255;
        if(this.luz_verde > 255)
            this.luz_verde = 255;
        if(this.luz_azul > 255)
            this.luz_azul = 255;
        
        this.luz_color =  new Color(this.luz_rojo, this.luz_verde, this.luz_azul);
    }
    
    public void actualizarColorPantalla(Graphics2D g){
        this.actualizar = false;
        
        actualizarColor();
        
        g.setColor(this.luz_color);
        g.fillRect(this.x, this.y, this.tam, this.tam);
    }
    
    public void eliminarParticula(){
        if(this.particula != null){
            this.particula = null;
            setColor(this.ROJO_PRED, this.VERDE_PRED, this.AZUL_PRED);
            
            setLuzColor(0, 0, 0);
            this.actualizar = true;
        }
    }
    
    public void limpiarLuz(){
        this.luz_rojo = 0;
        this.luz_verde = 0;
        this.luz_azul = 0;
        this.luz_intencidad = Pixel.luz_intencidadPred;
    }
    
    //is
    public boolean isActualizar() {
        return actualizar;
    }
    
    //set
    public void setLuz_intencidad(int luz_intencidad){
        this.luz_intencidad = luz_intencidad;
    }

    public void setParticula(Particula particula) {
        this.particula = particula;
        this.actualizar = true;
    }

    public void setLuzColor(int luz_rojo, int luz_verde, int luz_azul) {
        this.luz_rojo = luz_rojo;
        this.luz_verde = luz_verde;
        this.luz_azul = luz_azul;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    private void setColor(int rojo, int verde, int azul) {
        this.rojo = rojo;
        this.verde = verde;
        this.azul = azul;
    }

    public void setActualizar(boolean actualizar) {
        this.actualizar = actualizar;
    }
    
    
    //get
    public int getLuz_intencidad(){
        return luz_intencidad;
    }

    public static Color getColorPred() {
        int combioColor = 255 - Pixel.luz_intencidadPred;
        int r = Pixel.ROJO_PRED - combioColor;
        int v = Pixel.VERDE_PRED - combioColor;
        int a = Pixel.AZUL_PRED - combioColor;
        
        if(r < 0)
            r = 0;
        if(v < 0)
            v = 0;
        if(a < 0)
            a = 0;
        if(r > 255)
            r = 255;
        if(v > 255)
            v = 255;
        if(a > 255)
            a = 255;
        
        return new Color(r, v, a);
    }
    
    public Particula getParticula() {
        return particula;
    }

    public int getLuz_rojo() {
        return luz_rojo;
    }

    public int getLuz_verde() {
        return luz_verde;
    }

    public int getLuz_azul() {
        return luz_azul;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTam() {
        return tam;
    }

    public int getRojo() {
        return rojo;
    }

    public int getVerde() {
        return verde;
    }

    public int getAzul() {
        return azul;
    }

    public Color getColor() {
        return color;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
    
}