/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.awt.Color;
import particulas.Particula;

/**
 *
 * @author Josue Alvarez M
 */
public class Casilla {
    public static final int size = 64;
    
    private final Particula[][] matriz; 
    
    private final Particula[] particulasArr;
    private final int partArr_tam; // tamaño del arreglo de partículas
    private int posFinArr; // ultima posición disponible del array
    
    // posición en la que se ubica en pantalla
    private int xIni;
    private int yIni;
    private int xFin;
    private int yFin;
    
    // posición en matrizCasillas
    private int posX;
    private int posY;
    
    public Casilla(int xIni, int yIni, int xFin, int yFin, int posX, int posY) {
        this.xIni = xIni;
        this.yIni = yIni;
        
        this.xFin = xFin;
        this.yFin = yFin;
        
        this.posX = posX;
        this.posY = posY;
        
        this.matriz = new Particula[Casilla.size][Casilla.size];
        
        this.partArr_tam = Casilla.size * Casilla.size;
        this.particulasArr = new Particula[this.partArr_tam];
        this.posFinArr = 0;
    }
    
    public void actualizar(int hilo){
        int pos;
        
        Particula particula;
        for (int i = this.posFinArr; i > 0; i--) {
            pos = (int) ControladorParticulas.random.getNum(hilo, i);
            
            particula = this.particulasArr[pos];
            
            // Pasa la partícula al final del array
            particula.setPosArr(i - 1);
            this.particulasArr[i - 1].setPosArr(pos);
            this.particulasArr[pos] = this.particulasArr[i - 1];
            this.particulasArr[i - 1] = particula;
            
            particula.actualizar(hilo);
            
        }
    }
    
    public void pintar() {
        // coordenadas en pantalla
        int xAnt, yAnt;
        int x, y;
        
        Casilla casillaAnt;
        Casilla casilla;
        
        for (Particula particula : particulasArr){
            if(particula == null)
                continue;
            
            xAnt = particula.getXAntPantalla();
            yAnt = particula.getYAntPantalla();
            casillaAnt = particula.getCasillaAnt();
            
            x = particula.getXPantalla();
            y = particula.getYPantalla();
            casilla = particula.getCasilla();
            
            if(xAnt != x || yAnt != y || casillaAnt != casilla)
                Pantalla.getG2d().clearRect(xAnt, yAnt, Particula.getSize(), Particula.getSize());
        }
        
        for (Particula particula : particulasArr){
            if(particula == null)
                continue;
            
            x = particula.getXPantalla();
            y = particula.getYPantalla();
            
            Pantalla.getG2d().setColor(particula.getColor());
            Pantalla.getG2d().fillRect(x, y, Particula.getSize(), Particula.getSize());
            
            // actualizar posición anterior
            particula.reiniciarPosAnt();
        }
    }
    
    public void pintarBorde() {
        int dim = Particula.getSize() * Casilla.size;
        
        Pantalla.getG2d().setColor(Color.BLACK);
        Pantalla.getG2d().drawRect(this.xIni + 1, this.yIni + 1, dim - 1, dim - 1);
    }
    public void quitarBorde() {
        int dim = Particula.getSize() * Casilla.size;
        
        Pantalla.getG2d().setColor(Pantalla.getG2d().getBackground());
        Pantalla.getG2d().drawRect(this.xIni + 1, this.yIni + 1, dim - 1, dim - 1);
    }
    
    public void generarParticula(int x, int y){
        int particulaTam = Particula.getSize();
        
        x /= particulaTam;
        y /= particulaTam;
        
        setParticula(x, y, new Particula(this, x, y, this.posFinArr));
    }
    
    public void borrarParticula(int x, int y){
        Particula particula = this.matriz[y][x];
        if(particula != null){
            this.matriz[y][x] = null;
            
            int pos = particula.getPosArr();
            // pasa la ultima partícula a la posición de la partícula eliminada
            this.particulasArr[this.posFinArr - 1].setPosArr(pos);
            this.particulasArr[pos] = this.particulasArr[this.posFinArr - 1];
            // borra la ultima partícula
            this.particulasArr[this.posFinArr - 1] = null;
            
            this.posFinArr--;
        }
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
                borrarParticula(particula.getX(), particula.getY());
                
                casilla.setParticula(x2, y2, particula);
                
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
        return this.posFinArr == 0;
    }
    
    public void setParticula(int x, int y, Particula particula){
        if(x < Casilla.size && y < Casilla.size){
            borrarParticula(x, y);
            
            particula.setCasilla(this);
            particula.setX(x);
            particula.setY(y);
            particula.setPosArr(this.posFinArr);
            
            this.particulasArr[this.posFinArr] = particula;
            this.matriz[y][x] = particula;
            
            this.posFinArr++;
        }
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
