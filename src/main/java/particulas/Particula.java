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
    private static int size = 4;
    
    private int x, y;           // Posición en la matriz casilla
    private Casilla casilla;    // Casilla en la que está la partícula
    
    private Color color;
    
    private boolean actualizada = false;
    
    private double friccion;
    private double aceleracion;
    
    public static final Color COLORPRED = Color.BLACK;

    public Particula(Casilla casilla, int x, int y) {
        this.color = Particula.COLORPRED;
        
        this.casilla = casilla;
        
        this.x = x;
        this.y = y;
    }

    public Particula(Casilla casilla, int x, int y, Color color) {
        this.color = color;
        
        this.casilla = casilla;
        
        this.x = x;
        this.y = y;
    }
    
    public void actualizar(int hilo){
        this.color = new Color((int) ControladorParticulas.random.getNum(hilo, 256)
                                , (int) ControladorParticulas.random.getNum(hilo, 256)
                                , (int) ControladorParticulas.random.getNum(hilo, 256));
        
        this.casilla.moverParticula(getXPantalla(), getYPantalla() + Particula.getSize(), this, false);
        
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
