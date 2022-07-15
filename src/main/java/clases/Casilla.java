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
    public static final int size = 2;
    
    private volatile Particula[][] matriz; 
    
    private final Particula[] particulasArr;
    private final int partArr_tam; // tamaño del arreglo de partículas
    private int posFinArr; // ultima posición disponible del array
    
    private volatile boolean enArrayCasillas; // indica si la casilla se encuentra en el array de casillas
    private volatile boolean activa; // indica si la casilla está activa (si hay partículas que actualizar)
    
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
        
        this.enArrayCasillas = false;
    }
    
    public synchronized void actualizar(int hilo){
        int pos;
        
        Particula particula;
        this.activa = false;
        for (int i = this.posFinArr; i > 0; i--) {
            pos = (int) Juego.random.getNum(hilo, i);
            
            particula = this.particulasArr[pos];
            
            // Pasa la partícula al final del array
            particula.setPosArr(i - 1);
            this.particulasArr[i - 1].setPosArr(pos);
            this.particulasArr[pos] = this.particulasArr[i - 1];
            this.particulasArr[i - 1] = particula;
            
            // si se actualizó mínimo una particula
            if(particula.actualizar(hilo))
                this.activa = true;
            
        }
    }
    
    public void pintar() {
        // coordenadas en pantalla
        int xAnt, yAnt;
        int x, y;
        
        Casilla casillaAnt;
        Casilla casilla;
        
        // Borra la posición anterior de la partícula
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
        
        // pinta la posición actual de la partícula
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
        
        if(isParticulaNull(x, y))
            setParticula(x, y, new Particula(this, x, y, this.posFinArr));
    }
    
    public synchronized void borrarParticula(int x, int y){
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
     * Mueve la particula a la coordenada de la matriz casilla indicada.
     * @param x
     * @param y
     * @param particula
     * Particula a mover
     * @param remplazar 
     * @return  
     */
    public synchronized boolean moverParticula(int x, int y, Particula particula, boolean remplazar){
        Casilla casilla = ControladorParticulas.getCasillaRelativa(x, y, this);
        
        x = Particula.cordRelativaAEx(x);
        y = Particula.cordRelativaAEx(y);
        
        if(casilla != null){
            // Coordanada en la matriz

            if(remplazar || casilla.isParticulaNull(x, y)){
                borrarParticula(particula.getX(), particula.getY());
                
                casilla.setParticula(x, y, particula);
                
                return true;
            }
        }
        
        return false;
    }
    
    public synchronized void setParticula(int x, int y, Particula particula){
        if(x < Casilla.size && y < Casilla.size){
            if(!this.enArrayCasillas)
                ControladorParticulas.agregarCasillaArray(this);
            
            borrarParticula(x, y);
            
            ControladorParticulas.activar(this);
            
            particula.setCasilla(this);
            particula.setX(x);
            particula.setY(y);
            particula.setPosArr(this.posFinArr);
            
            this.particulasArr[this.posFinArr] = particula;
            this.matriz[y][x] = particula;
            
            this.posFinArr++;
        }
    }
    
    public void setEnArrayCasillas(boolean enArrayCasillas) {
        this.enArrayCasillas = enArrayCasillas;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    
    public boolean isActiva() {
        return activa;
    }

    public boolean isEnArrayCasillas() {
        return enArrayCasillas;
    }
    
    /**
     * Retorna true si la cantidad de particulas
     * contenidas en la casilla es 0.
     * @return 
     */
    public boolean isNull(){
        return this.posFinArr == 0;
    }
    
    /**
     * Retorna true si la partícula en coordenada 
     * matriz casilla es null.
     * @param x
     * @param y
     * @return 
     */
    public boolean isParticulaNull(int x, int y){
        return getParticula(x, y) == null;
    }
    
    /**
     * Retorna la partícula en la coordenada de matriz casilla
     * @param x
     * @param y
     * @return 
     */
    public Particula getParticula(int x, int y){
        if(x >= 0 && y >= 0 && x < Casilla.size && y < Casilla.size)
            return this.matriz[y][x];
        
        Casilla casilla = ControladorParticulas.getCasillaRelativa(x, y, this);
        
        x = Particula.cordRelativaAEx(x);
        y = Particula.cordRelativaAEx(y);
        
        if(casilla != null)
            return casilla.matriz[y][x];
        
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
