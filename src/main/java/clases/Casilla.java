/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.util.ArrayList;
import particulas.Particula;

/**
 *
 * @author Josue Alvarez M
 */
public class Casilla {
    public static final int size = 64;
    
    private Particula[][] matriz; 
    private Integer posiciones[];
    
    // posición en la que se ubica en pantalla
    private int xIni;
    private int yIni;
    private int xFin;
    private int yFin;
    
    // posición en matrizCasillas
    private int posX;
    private int posY;
    
    private int cantParticulas;
    
    public Casilla(int xIni, int yIni, int xFin, int yFin, int posX, int posY) {
        this.xIni = xIni;
        this.yIni = yIni;
        
        this.xFin = xFin;
        this.yFin = yFin;
        
        this.posX = posX;
        this.posY = posY;
        
        this.matriz = new Particula[Casilla.size][Casilla.size];
        
        definirPosicionesArray();
    }
    
    private void definirPosicionesArray(){
        this.posiciones = new Integer[Casilla.size];
        
        for (int i = 0; i < Casilla.size; i++)
            this.posiciones[i] = i;
    }
    
    public void actualizar(int hilo, int cantHilos){
        int pos;
        int posVal; // valor guardado en pos
        
        Particula particula;
        for (int i = Casilla.size - 1; i >= 0; i--) {
            for (int tam = Casilla.size; tam > 0; tam--) {
                pos = (int) ControladorParticulas.random.getNum(hilo, tam);

                particula = this.matriz[i][this.posiciones[pos]];
                if(particula != null && !particula.isActualizada())
                    particula.actualizar(hilo);
                
                // pasa el número seleccionado a la ultima posición
                posVal = this.posiciones[pos];
                this.posiciones[pos] = this.posiciones[tam - 1];
                this.posiciones[tam - 1] = posVal;

            }
        }
        
        for (Particula[] fila : matriz) {
            for (Particula p : fila) {
                if(p != null){
                    p.setActualizada(false);
                }
            }
        }
    }
    
    public void pintar() {
        for (Particula[] fila : matriz) {
            for (Particula p : fila) {
                if(p == null)
                    continue;
                
                Pantalla.g2d.setColor(p.getColor());
                Pantalla.g2d.fillRect(p.getXPantalla(), p.getYPantalla(), Particula.getSize(), Particula.getSize());
            }
        }
    }
    
    public void generarParticula(int x, int y){
        int particulaTam = Particula.getSize();
        
        x /= particulaTam;
        y /= particulaTam;
        
        if(x < Casilla.size && y < Casilla.size){
            this.matriz[y][x] = new Particula(this, x, y);
            this.cantParticulas++;
        }
    }
    
    public void borrarParticula(int x, int y){
        this.matriz[y][x] = null;
        this.cantParticulas--;
    }
    
    /**
     * Mueve la particula a la coordenada de la pantalla indicada.
     * @param x
     * @param y
     * @param particula
     * Particula a mover
     * @param remplazar 
     */
    public boolean moverParticula(int x, int y, Particula particula, boolean remplazar){
        Casilla casilla = ControladorParticulas.getCasillaRango(x, y);
        
        if(casilla != null){
            // Coordanada en la matriz
            int x2 = (x - casilla.xIni) / Particula.getSize();
            int y2 = (y - casilla.yIni) / Particula.getSize();

            if(remplazar || casilla.matriz[y2][x2] == null){
                particula.getCasilla().borrarParticula(particula.getX(), particula.getY());
                
                casilla.cantParticulas++;
                particula.setCasilla(casilla);
                particula.setX(x2);
                particula.setY(y2);
                casilla.matriz[y2][x2] = particula;
                
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Retorna true si la cantidad de particulas
     * contenidas en la casilla es 0.
     * @return 
     */
    public boolean isNull(){
        return this.cantParticulas == 0;
    }
    
    public Particula getParticula(int x, int y){
        int particulaTam = Particula.getSize();
        
        x /= particulaTam;
        y /= particulaTam;
        
        if(x < Casilla.size && y < Casilla.size)
            return this.matriz[y][x];
        
        return null;
    }
    
    public int getXIni() {
        return xIni;
    }

    public int getYIni() {
        return yIni;
    }
    
    public int getXFin() {
        return xFin;
    }

    public int getYFin() {
        return yFin;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
    
    
}
