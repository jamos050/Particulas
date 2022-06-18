/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particulas;

import clases.Casilla;
import clases.ControladorParticulas;
import java.awt.Color;


/**
 *
 * @author Josue Alvarez M
 */
public class Particula {
    private static int size = 2;
    
    private int x, y;           // Posición en la matriz casilla
    private Casilla casilla;    // Casilla en la que está la partícula
    
    private Color color;
    
    private boolean actualizada = false;
    
    private double friccion;
    private double aceleracion;
    
    private double movimientoX, movimientoY;
    
    public static final Color COLOR_FONDO = Color.WHITE;
    public static final Color COLOR_PRED = Color.BLACK;

    public Particula(Casilla casilla, int x, int y) {
        this.color = Particula.COLOR_PRED;
        
        this.casilla = casilla;
        
        this.x = x;
        this.y = y;
        
        setValorBasicos();
    }

    public Particula(Casilla casilla, int x, int y, Color color) {
        this.color = color;
        
        this.casilla = casilla;
        
        this.x = x;
        this.y = y;
        
        setValorBasicos();
    }
    
    private void setValorBasicos(){
        this.friccion = 0;
        this.aceleracion = 1;
        
        this.movimientoX = 0;
        this.movimientoY = 0;
    }
    
    private void accelerar(){
        // coordenadas en pantalla
        int x = getXPantalla();
        int y = getYPantalla();
        
        double gravedadX = ControladorParticulas.getGravedadX();
        double gravedadY = ControladorParticulas.getGravedadY();
        
        if(gravedadY != 0){
            int incrementoY = (int) (Math.abs(gravedadY) / gravedadY);
            if(ControladorParticulas.particulaIsNull(x, y + (Particula.getSize() * incrementoY)))
                this.movimientoY += this.aceleracion * gravedadY;
        }
        
        if(gravedadX != 0){
            int incrementoX = (int) (Math.abs(gravedadX) / gravedadX);
            if(ControladorParticulas.particulaIsNull(x + (Particula.getSize() * incrementoX), y))
                this.movimientoX += this.aceleracion * gravedadX;
        }
    }
    
    public void actualizar(int hilo){
        this.color = new Color((int) ControladorParticulas.random.getNum(hilo, 256)
                                , (int) ControladorParticulas.random.getNum(hilo, 256)
                                , (int) ControladorParticulas.random.getNum(hilo, 256));
        
        accelerar();
        
        boolean movX = Math.abs(this.movimientoX) - 1 >= 0;
        boolean movY = Math.abs(this.movimientoY) - 1 >= 0;
        while(movX || movY){
            int incrementoY = 0;
            int incrementoX = 0;
            
            if(movY){
                incrementoY = (int) (Math.abs(this.movimientoY) / this.movimientoY);
                this.movimientoY -= incrementoY;
            }
            
            if(movX){
                incrementoX = (int) (Math.abs(this.movimientoX) / this.movimientoX);
                this.movimientoX -= incrementoX;
            }
            
            this.casilla.moverParticula(getXPantalla() + (Particula.getSize() * incrementoX), getYPantalla() + (Particula.getSize() * incrementoY), this, false);
            
            movX = Math.abs(this.movimientoX) - 1 >= 0;
            movY = Math.abs(this.movimientoY) - 1 >= 0;
        }
        
        this.actualizada = true;
    }

    public boolean isActualizada() {
        return actualizada;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getXPantalla() {
        return (x * Particula.getSize()) + this.casilla.getXIni();
    }

    public int getYPantalla() {
        return (y * Particula.getSize()) + this.casilla.getYIni();
    }

    public Color getColor() {
        return color;
    }

    public Casilla getCasilla() {
        return casilla;
    }
    
    public static int getSize() {
        return size;
    }

    public void setActualizada(boolean actualizada) {
        this.actualizada = actualizada;
    }
    
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCasilla(Casilla casilla) {
        this.casilla = casilla;
    }
    
    
    
}
