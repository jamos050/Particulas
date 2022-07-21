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
    public static final int SIZE = 32;
    
    private volatile Particula[][] matriz; 
    
    private final Particula[][] matrizOrdFila;
    private final int[] info_matrizOrdFila;
    private final Particula[][] matrizOrdColumna;
    private final int[] info_matrizOrdColumna;
    
    private volatile boolean enArrayCasillas; // indica si la casilla se encuentra en el array de casillas
    
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
        
        this.matriz = new Particula[Casilla.SIZE][Casilla.SIZE];
        
        this.matrizOrdFila = new Particula[Casilla.SIZE][Casilla.SIZE];
        this.info_matrizOrdFila = new int[Casilla.SIZE];
        this.matrizOrdColumna = new Particula[Casilla.SIZE][Casilla.SIZE];
        this.info_matrizOrdColumna = new int[Casilla.SIZE];
        
        this.enArrayCasillas = false;
    }
    
    public synchronized void actualizar(int hilo){
        double gravedadY = ControladorParticulas.getGravedadY();
        double gravedadX = ControladorParticulas.getGravedadX();
        
        int pos;
        Particula particula;
        if(Math.abs(gravedadY) > Math.abs(gravedadX)){
            if(gravedadY > 0){
                for (int i = Casilla.SIZE-1; i >= 0; i--) {
                    if(this.info_matrizOrdFila[i] == 0)
                        continue;

                    for (int tam = this.info_matrizOrdFila[i]; tam > 0; tam--) {
                        pos = (int) Juego.random.getNum(hilo, tam);

                        particula = this.matrizOrdFila[i][pos];

                        // Pasa la partícula al final
                        particula.setPosMatrizOrdFila(tam - 1);
                        this.matrizOrdFila[i][tam - 1].setPosMatrizOrdFila(pos);
                        this.matrizOrdFila[i][pos] = this.matrizOrdFila[i][tam - 1];
                        this.matrizOrdFila[i][tam - 1] = particula;

                        if(!particula.isActualizada()){
                            particula.actualizar(hilo);
                            particula.setActualizada(true);
                        }
                        if(!particula.isActiva())
                            quitarParticulaMatrizOrd(particula);
                    }
                }
            }
            else{
                for (int i = 0; i < Casilla.SIZE; i++) {
                    if(this.info_matrizOrdFila[i] == 0)
                        continue;

                    for (int tam = this.info_matrizOrdFila[i]; tam > 0; tam--) {
                        pos = (int) Juego.random.getNum(hilo, tam);

                        particula = this.matrizOrdFila[i][pos];

                        // Pasa la partícula al final
                        particula.setPosMatrizOrdFila(tam - 1);
                        this.matrizOrdFila[i][tam - 1].setPosMatrizOrdFila(pos);
                        this.matrizOrdFila[i][pos] = this.matrizOrdFila[i][tam - 1];
                        this.matrizOrdFila[i][tam - 1] = particula;

                        if(!particula.isActualizada()){
                            particula.actualizar(hilo);
                            particula.setActualizada(true);
                        }
                        if(!particula.isActiva())
                            quitarParticulaMatrizOrd(particula);
                    }
                }
            }
        }
        else{
            if(gravedadX > 0){
                for (int i = Casilla.SIZE-1; i >= 0; i--) {
                    if(this.info_matrizOrdColumna[i] == 0)
                        continue;

                    for (int tam = this.info_matrizOrdColumna[i]; tam > 0; tam--) {
                        pos = (int) Juego.random.getNum(hilo, tam);

                        particula = this.matrizOrdColumna[i][pos];

                        // Pasa la partícula al final
                        particula.setPosMatrizOrdColumna(tam - 1);
                        this.matrizOrdColumna[i][tam - 1].setPosMatrizOrdColumna(pos);
                        this.matrizOrdColumna[i][pos] = this.matrizOrdColumna[i][tam - 1];
                        this.matrizOrdColumna[i][tam - 1] = particula;
                        
                        if(!particula.isActualizada()){
                            particula.actualizar(hilo);
                            particula.setActualizada(true);
                        }
                        if(!particula.isActiva())
                            quitarParticulaMatrizOrd(particula);
                    }
                }
            }
            else{
                for (int i = 0; i < Casilla.SIZE; i++) {
                    if(this.info_matrizOrdColumna[i] == 0)
                        continue;

                    for (int tam = this.info_matrizOrdColumna[i]; tam > 0; tam--) {
                        pos = (int) Juego.random.getNum(hilo, tam);

                        particula = this.matrizOrdColumna[i][pos];

                        // Pasa la partícula al final
                        particula.setPosMatrizOrdColumna(tam - 1);
                        this.matrizOrdColumna[i][tam - 1].setPosMatrizOrdColumna(pos);
                        this.matrizOrdColumna[i][pos] = this.matrizOrdColumna[i][tam - 1];
                        this.matrizOrdColumna[i][tam - 1] = particula;

                        if(!particula.isActualizada()){
                            particula.actualizar(hilo);
                            particula.setActualizada(true);
                        }
                        if(!particula.isActiva())
                            quitarParticulaMatrizOrd(particula);
                    }
                }
            }
        }
    }
    
    public synchronized void activarCasilla(){
        Particula particula;
        int tam;
        for (int i = 0; i < Casilla.SIZE; i++) {
            for (int j = 0; j < Casilla.SIZE; j++) {
                particula = this.matriz[i][j];
                if(particula != null && !particula.isActiva()){
                    particula.setActiva(true);
                    particula.getCasilla().agregarParticulaMatrizOrd(particula);
                }
            }
        }
    }
    /**
     * Agrega a las matrizOrdFila y matrizOrdColumna de su correspondiente casilla la
     * partícula indicada y sus cercanas. 
     * @param particula
     */
    public synchronized void activar(Particula particula){
        int rango = 2;
        
        for (int i = -rango; i <= rango; i++) {
            for (int j = -rango; j <= rango; j++) {
                Particula particulaCercana = getParticula(particula.getX() + j, particula.getY() + i);
                
                if(particulaCercana != null && !particulaCercana.isActiva()){
                    particulaCercana.setActiva(true);
                    particulaCercana.getCasilla().agregarParticulaMatrizOrd(particulaCercana);
                }
            }
        }
    }
    
    public synchronized void agregarParticulaMatrizOrd(Particula particula){
        // matrizOrdFila
        int posMatrizOrdFila = this.info_matrizOrdFila[particula.getY()];
        particula.setPosMatrizOrdFila(posMatrizOrdFila);
        this.matrizOrdFila[particula.getY()][posMatrizOrdFila] = particula;
        this.info_matrizOrdFila[particula.getY()]++;
        
        // matrizOrdColumna
        int posMatrizOrdColumna = this.info_matrizOrdColumna[particula.getX()];
        particula.setPosMatrizOrdColumna(posMatrizOrdColumna);
        this.matrizOrdColumna[particula.getX()][posMatrizOrdColumna] = particula;
        this.info_matrizOrdColumna[particula.getX()]++;
    }
    
    public synchronized void quitarParticulaMatrizOrd(Particula particula){
        particula.setActiva(false);
        this.matrizOrdFila[particula.getY()][this.info_matrizOrdFila[particula.getY()] - 1].setPosMatrizOrdFila(particula.getPosMatrizOrdFila());
        this.matrizOrdColumna[particula.getX()][this.info_matrizOrdColumna[particula.getX()] - 1].setPosMatrizOrdColumna(particula.getPosMatrizOrdColumna());
        
        // matrizOrdFila
        this.matrizOrdFila[particula.getY()][particula.getPosMatrizOrdFila()] = this.matrizOrdFila[particula.getY()][this.info_matrizOrdFila[particula.getY()] - 1];
        this.matrizOrdFila[particula.getY()][this.info_matrizOrdFila[particula.getY()] - 1] = null;
        this.info_matrizOrdFila[particula.getY()]--;
        
        // matrizOrdColumna
        this.matrizOrdColumna[particula.getX()][particula.getPosMatrizOrdColumna()] = this.matrizOrdColumna[particula.getX()][this.info_matrizOrdColumna[particula.getX()] - 1];
        this.matrizOrdColumna[particula.getX()][this.info_matrizOrdColumna[particula.getX()] - 1] = null;
        this.info_matrizOrdColumna[particula.getX()]--;
    }
    
    public void pintar() {
        // coordenadas en pantalla
        int xAnt, yAnt;
        int x, y;
        
        Casilla casillaAnt;
        Casilla casilla;
        
        // Borra la posición anterior de la partícula
        Particula particula;
        int tam;
        for (int i = 0; i < Casilla.SIZE; i++) {
            tam = this.info_matrizOrdFila[i];
            if(tam == 0)
                continue;
            
            for (int j = 0; j < tam; j++) {
                particula = this.matrizOrdFila[i][j];
                
                xAnt = particula.getXAntPantalla();
                yAnt = particula.getYAntPantalla();
                casillaAnt = particula.getCasillaAnt();

                x = particula.getXPantalla();
                y = particula.getYPantalla();
                casilla = particula.getCasilla();
                
                if((xAnt != x || yAnt != y || casillaAnt != casilla) 
                    && casillaAnt.isParticulaNull(particula.getXAnt(), particula.getYAnt())){
                    
                    Pantalla.getG2d().clearRect(xAnt, yAnt, Particula.getSize(), Particula.getSize());
                }
            }
        }
        
        // pinta la posición actual de la partícula
        for (int i = 0; i < Casilla.SIZE; i++) {
            tam = this.info_matrizOrdFila[i];
            if(tam == 0)
                continue;
            
            for (int j = 0; j < tam; j++) {
                particula = this.matrizOrdFila[i][j];
                
                x = particula.getXPantalla();
                y = particula.getYPantalla();

                Pantalla.getG2d().setColor(particula.getColor());
                Pantalla.getG2d().fillRect(x, y, Particula.getSize(), Particula.getSize());

                // actualizar posición anterior
                particula.reiniciarPosAnt();
            }
        }
    }
    
    public void pintarBorde() {
        int dim = Particula.getSize() * Casilla.SIZE;
        
        Pantalla.getG2d().setColor(Color.BLACK);
        Pantalla.getG2d().drawRect(this.xIni + 1, this.yIni + 1, dim - 1, dim - 1);
    }
    public void quitarBorde() {
        int dim = Particula.getSize() * Casilla.SIZE;
        
        Pantalla.getG2d().setColor(Pantalla.getG2d().getBackground());
        Pantalla.getG2d().drawRect(this.xIni + 1, this.yIni + 1, dim - 1, dim - 1);
    }
    
    public void generarParticula(int x, int y){
        int particulaTam = Particula.getSize();
        
        x /= particulaTam;
        y /= particulaTam;
        
        if(isParticulaNull(x, y))
            setParticula(x, y, new Particula(this, x, y, this.info_matrizOrdFila[y], this.info_matrizOrdColumna[x]));
    }
    
    public synchronized void borrarParticula(int x, int y){
        Particula particula = this.matriz[y][x];
        if(particula != null){
            this.matriz[y][x] = null;
            
            if(particula.isActiva())
                quitarParticulaMatrizOrd(particula);
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
        if(x < Casilla.SIZE && y < Casilla.SIZE){
            if(!this.enArrayCasillas)
                ControladorParticulas.agregarCasillaArray(this);
            
            borrarParticula(x, y);
            
            this.matriz[y][x] = particula;
            particula.setCasilla(this);
            particula.setX(x);
            particula.setY(y);
            
            activar(particula);
        }
    }
    
    public synchronized void setEnArrayCasillas(boolean enArrayCasillas) {
        this.enArrayCasillas = enArrayCasillas;
    }

    public boolean isEnArrayCasillas() {
        return enArrayCasillas;
    }
    
    /**
     * Retorna true si la cantidad de particulas
     * contenidas en la casilla es 0.
     * @return 
     */
    public synchronized boolean isNull(){
        for (int i = 0; i < Casilla.SIZE; i++) {
            if(this.info_matrizOrdFila[i] != 0)
                return false;
        }
        
        return true;
    }
    
    /**
     * Retorna true si la partícula en coordenada 
     * matriz casilla es null.
     * @param x
     * @param y
     * @return 
     */
    public synchronized boolean isParticulaNull(int x, int y){
        return getParticula(x, y) == null;
    }
    
    /**
     * Retorna la partícula en la coordenada de matriz casilla
     * @param x
     * @param y
     * @return 
     */
    public synchronized Particula getParticula(int x, int y){
        if(x >= 0 && y >= 0 && x < Casilla.SIZE && y < Casilla.SIZE)
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
    
    public synchronized void setActualizada(boolean actualizada){
        int tam;
        Particula particula;
        for (int i = 0; i < Casilla.SIZE; i++) {
            tam = this.info_matrizOrdFila[i];
            if(tam == 0)
                continue;
            
            for (int j = 0; j < tam; j++) {
                particula = this.matrizOrdFila[i][j];
                
                particula.setActualizada(actualizada);
            }
        }
    }
}