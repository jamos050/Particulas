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
    private static int size = 3;
    
    private int posArr;
    
    private int xAnt, yAnt;     // Posición anterior mostrada en pantalla, en la matriz casilla
    private Casilla casillaAnt;    // Casilla en la que estaba la partícula
    
    private int x, y;           // Posición en la matriz casilla
    private Casilla casilla;    // Casilla en la que está la partícula
    
    private Color color;
    
    private double friccion;
    private double aceleracion;
    
    private double movimientoX, movimientoY;
    
    public static final Color COLOR_PRED = Color.BLACK;

    public Particula(Casilla casilla, int x, int y, int posArr) {
        this.color = Particula.COLOR_PRED;
        
        this.casillaAnt = casilla;
        this.xAnt = x;
        this.yAnt = y;
        
        this.casilla = casilla;
        this.x = x;
        this.y = y;
        
        
        this.posArr = posArr;
        
        setValorBasicos();
    }

    public Particula(Casilla casilla, int x, int y, int posArr, Color color) {
        this.color = color;
        
        this.casillaAnt = casilla;
        this.xAnt = x;
        this.yAnt = y;
        
        this.casilla = casilla;
        this.x = x;
        this.y = y;
        
        this.posArr = posArr;
        
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
        int xPantalla = getXPantalla();
        int yPantalla = getYPantalla();
        
        double gravedadX = ControladorParticulas.getGravedadX();
        double gravedadY = ControladorParticulas.getGravedadY();
        
        if(gravedadX != 0){
            int signoX = (int) (Math.abs(gravedadX) / gravedadX);
            int incrementoX = (Particula.getSize() * signoX);
            if(ControladorParticulas.particulaIsNull(xPantalla + incrementoX, yPantalla))
                this.movimientoX += this.aceleracion * gravedadX;
        }
        if(gravedadY != 0){
            int signoY = (int) (Math.abs(gravedadY) / gravedadY);
            int incrementoY = (Particula.getSize() * signoY);
            if(ControladorParticulas.particulaIsNull(xPantalla, yPantalla + incrementoY))
                this.movimientoY += this.aceleracion * gravedadY;
        }
    }
    
    public void actualizar(int hilo){
        this.color = new Color((int) ControladorParticulas.random.getNum(hilo, 256)
                                , (int) ControladorParticulas.random.getNum(hilo, 256)
                                , (int) ControladorParticulas.random.getNum(hilo, 256));
        
        accelerar();
        
        // El valor de movimiento debe ser minimo 1 para poder moverse
        boolean movX = Math.abs(this.movimientoX) - 1 >= 0;
        boolean movY = Math.abs(this.movimientoY) - 1 >= 0;
        
        while(movX || movY){
            int signoX = 0;
            int signoY = 0;
            int incrementoX = 0;
            int incrementoY = 0;
            
            // Define los incrementos a la coordenada para la siguiente posición
            if(movY){
                signoY = (int) (Math.abs(this.movimientoY) / this.movimientoY);
                incrementoY = (Particula.getSize() * signoY);
            }
            if(movX){
                signoX = (int) (Math.abs(this.movimientoX) / this.movimientoX);
                incrementoX = (Particula.getSize() * signoX);
            }
            
            int xPantalla = getXPantalla();
            int yPantalla = getYPantalla();
            
            // Si la particula en dicha posición está libre
            if(ControladorParticulas.particulaIsNull(xPantalla + incrementoX, yPantalla + incrementoY)){
                this.casilla.moverParticula(xPantalla + incrementoX, yPantalla + incrementoY, this, false);
                this.movimientoY -= signoY;
                this.movimientoX -= signoX;
            }
            else
                break;
            movX = Math.abs(this.movimientoX) - 1 >= 0;
            movY = Math.abs(this.movimientoY) - 1 >= 0;
        }
    }
    
    /**
     * Define como posición anterior la posicion actual
     */
    public void reiniciarPosAnt(){
        this.xAnt = this.x;
        this.yAnt = this.y;
        this.casillaAnt = this.casilla;
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
    
    public int getXAntPantalla() {
        return (xAnt * Particula.getSize()) + this.casillaAnt.getXIni();
    }

    public int getYAntPantalla() {
        return (yAnt * Particula.getSize()) + this.casillaAnt.getYIni();
    }

    public Casilla getCasillaAnt() {
        return casillaAnt;
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

    public int getPosArr() {
        return posArr;
    }

    
    public void setPosArr(int posArr) {
        this.posArr = posArr;
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
