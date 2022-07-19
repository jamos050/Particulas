/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particulas;

import clases.Casilla;
import clases.ControladorFrames;
import clases.ControladorParticulas;
import clases.Juego;
import java.awt.Color;


/**
 *
 * @author Josue Alvarez M
 */
public class Particula {
    private static int size = 3;
    
    private boolean actualizada = false;
    
    private boolean activa = false;
    private int posMatrizOrdFila;
    private int posMatrizOrdColumna;
    
    private int xAnt, yAnt;     // Posición anterior mostrada en pantalla, en la matriz casilla
    private Casilla casillaAnt;    // Casilla en la que estaba la partícula
    
    private int x, y;           // Posición en la matriz casilla
    private Casilla casilla;    // Casilla en la que está la partícula
    
    private Color color;
    
    private double porc_rebote;
    private double porc_inercia;
    
    private double aceleracionBase;
    private double aceleracionX;
    private double aceleracionY;
    
    private double movimientoX, movimientoY;
    public static final Color COLOR_PRED = Color.BLACK;

    public Particula(Casilla casilla, int x, int y, int posMatrizOrdFila, int posMatrizOrdColumna) {
        this.color = Particula.COLOR_PRED;
        
        this.casillaAnt = casilla;
        this.xAnt = x;
        this.yAnt = y;
        
        this.casilla = casilla;
        this.x = x;
        this.y = y;
        
        
        this.posMatrizOrdFila = posMatrizOrdFila;
        this.posMatrizOrdColumna = posMatrizOrdColumna;
        
        setValorBasicos();
    }

    public Particula(Casilla casilla, int x, int y, int posMatrizOrdFila, int posMatrizOrdColumna, Color color) {
        this.color = color;
        
        this.casillaAnt = casilla;
        this.xAnt = x;
        this.yAnt = y;
        
        this.casilla = casilla;
        this.x = x;
        this.y = y;
        
        this.posMatrizOrdFila = posMatrizOrdFila;
        this.posMatrizOrdColumna = posMatrizOrdColumna;
        
        setValorBasicos();
    }
    
    private void setValorBasicos(){
        this.porc_rebote = 0.40;
        this.porc_inercia = 0.60;
        
        this.aceleracionBase = 175;
        this.aceleracionX = 0;
        this.aceleracionY = 0;
        
        this.movimientoX = 0;
        this.movimientoY = 0;
    }
    
    private boolean acelerar(){
        double gravedadX = ControladorParticulas.getGravedadX();
        double gravedadY = ControladorParticulas.getGravedadY();
        
        int frames = ControladorFrames.getFrames();
        
        // gravedad con valores negativos no está funcionando
        
        // Acelera si no hay colisión
        
        double incrementoX = 0;
        double incrementoY = 0;
        
        // Define los incrementos a la coordenada para la siguiente posición según la aceleración
        if(this.aceleracionX != 0 || this.aceleracionY != 0){
            if(this.aceleracionX != 0)
                incrementoX = (int) (Math.abs(this.aceleracionX) / this.aceleracionX);
            if(this.aceleracionY != 0)
                incrementoY = (int) (Math.abs(this.aceleracionY) / this.aceleracionY);
        }
        else{
            if(gravedadX != 0)
                incrementoX = (int) (Math.abs(gravedadX) / gravedadX);
            if(gravedadY != 0)
                incrementoY = (int) (Math.abs(gravedadY) / gravedadY);
        }
        
        double aceleracionXTemp = this.aceleracionX;
        double aceleracionYTemp = this.aceleracionY;
        if((gravedadX < 0 && this.aceleracionX > this.aceleracionBase * gravedadX) || (gravedadX > 0 && this.aceleracionX < this.aceleracionBase * gravedadX))
            aceleracionXTemp += ((this.aceleracionBase * gravedadX) / frames);
        if((gravedadY < 0 && this.aceleracionY > this.aceleracionBase * gravedadY) || (gravedadY > 0 && this.aceleracionY < this.aceleracionBase * gravedadY))
            aceleracionYTemp += ((this.aceleracionBase * gravedadY) / frames);
        
        Particula particula = this.casilla.getParticula(this.x + (int)incrementoX, this.y + (int)incrementoY);
        boolean acelerar = false;
        
        if(particula == null && ControladorParticulas.getCasillaRelativa(this.x + (int)incrementoX, this.y + (int)incrementoY, this.casilla) != null)
            acelerar = true;
        
        else if(particula != null){
            if(particula.getAceleracionX() == 0 && particula.getAceleracionX() == aceleracionXTemp 
                &&  particula.getAceleracionY() < 0 && particula.getAceleracionY() <= aceleracionYTemp){
                
                acelerar = true;
            }
            else if(particula.getAceleracionX() == 0 && particula.getAceleracionX() == aceleracionXTemp 
                &&  particula.getAceleracionY() > 0 && particula.getAceleracionY() >= aceleracionYTemp){
                
                acelerar = true;
            }
            else if(particula.getAceleracionX() < 0 && particula.getAceleracionX() <= aceleracionXTemp 
                &&  particula.getAceleracionY() == 0 && particula.getAceleracionY() == aceleracionYTemp){
                
                acelerar = true;
            }
            else if(particula.getAceleracionX() > 0 && particula.getAceleracionX() >= aceleracionXTemp 
                &&  particula.getAceleracionY() == 0 && particula.getAceleracionY() == aceleracionYTemp){
                
                acelerar = true;
            }
            else if(particula.getAceleracionX() < 0 && particula.getAceleracionX() <= aceleracionXTemp 
                &&  particula.getAceleracionY() > 0 && particula.getAceleracionY() >= aceleracionYTemp){
                
                acelerar = true;
            }
            else if(particula.getAceleracionX() > 0 && particula.getAceleracionX() >= aceleracionXTemp 
                &&  particula.getAceleracionY() < 0 && particula.getAceleracionY() <= aceleracionYTemp){
                
                acelerar = true;
            }
            else if(particula.getAceleracionX() > 0 && particula.getAceleracionX() >= aceleracionXTemp 
                &&  particula.getAceleracionY() > 0 && particula.getAceleracionY() >= aceleracionYTemp){
                
                acelerar = true;
            }
            else if(particula.getAceleracionX() < 0 && particula.getAceleracionX() <= aceleracionXTemp 
                &&  particula.getAceleracionY() < 0 && particula.getAceleracionY() <= aceleracionYTemp){
                
                acelerar = true;
            }
        }
        if(acelerar){
            this.aceleracionX = aceleracionXTemp;
            this.aceleracionY = aceleracionYTemp;
            incrementoX = this.aceleracionX / frames;
            incrementoY = this.aceleracionY / frames;
            
            if(Math.abs(this.aceleracionX) < incrementoX)
                this.aceleracionX = 0;
            if(Math.abs(this.aceleracionY) < incrementoY)
                this.aceleracionY = 0;
            
            this.movimientoX += incrementoX;
            this.movimientoY += incrementoY;
            
            return true;
        }
        
        return false;
    }
    
    private boolean rebote(int hilo){
        int incrementoX = 0;
        int incrementoY = 0;
        
        // Define los incrementos a la coordenada para la siguiente posición
        if(Math.abs(this.movimientoX) - 1 >= 0)
            incrementoX = (int) (Math.abs(this.movimientoX) / this.movimientoX);
        if(Math.abs(this.movimientoY) - 1 >= 0)
            incrementoY = (int) (Math.abs(this.movimientoY) / this.movimientoY);
        
        if(incrementoX == 0 && incrementoY == 0){
            if(this.aceleracionX != 0)
                incrementoX = (int) (Math.abs(this.aceleracionX) / this.aceleracionX);
            if(this.aceleracionY != 0)
                incrementoY = (int) (Math.abs(this.aceleracionY) / this.aceleracionY);
            
            if(incrementoX == 0 && incrementoY == 0)
                return false;
        }
        
        // Si hay colision con partícula o borde de pantalla
        Particula particula = this.casilla.getParticula(this.x + incrementoX, this.y + incrementoY);
        if((particula != null
            || ControladorParticulas.getCasillaRelativa(this.x + incrementoX, this.y + incrementoY, this.casilla) == null)){
            
            // Inercia
            if(particula != null && !particula.isRodeada()){
                if(incrementoX != 0)
                    particula.aceleracionX += this.aceleracionX * this.porc_inercia;
                if(incrementoY != 0)
                    particula.aceleracionY += this.aceleracionY * this.porc_inercia;
            }
            
            if(incrementoX == 0){ // si no se estaba moviendo en x
                this.aceleracionY *= -1;

                this.aceleracionX += this.aceleracionY * Juego.random.getNum(hilo, 2, -2) * (Juego.random.getNum(hilo, 15)/100);
            }
            else if(incrementoY == 0){ // si no se estaba moviendo en y
                this.aceleracionX *= -1;

                this.aceleracionY += this.aceleracionX * Juego.random.getNum(hilo, 2, -2) * (Juego.random.getNum(hilo, 15)/100);
            }
            
            else{
                if(this.casilla.isParticulaNull(this.x + incrementoX, this.y) 
                    && ControladorParticulas.getCasillaRelativa(this.x + incrementoX, this.y, this.casilla) != null){
                    
                    if(this.casilla.isParticulaNull(this.x, this.y + incrementoY)
                        && ControladorParticulas.getCasillaRelativa(this.x, this.y + incrementoY, this.casilla) != null){
                        
                        int rand = (int) Juego.random.getNum(hilo, 100);
                        if(rand  < 66){
                            if(rand < 33)
                                this.aceleracionX *= -1;
                            else
                                this.aceleracionY *= -1;
                        }
                        else{
                            this.aceleracionX *= -1;
                            this.aceleracionY *= -1;
                        }
                    }
                    else
                        this.aceleracionY *= -1;
                }
                else{
                    if(!this.casilla.isParticulaNull(this.x, this.y + incrementoY)
                        || ControladorParticulas.getCasillaRelativa(this.x, this.y + incrementoY, this.casilla) == null){
                        
                        this.aceleracionX *= -1;
                        this.aceleracionY *= -1;
                    }
                    else
                        this.aceleracionX *= -1;
                }
            }
            
            // Disminuye la aceleración dependiendo de el porcentaje de rebote
            if(incrementoX != 0)
                this.aceleracionX *= this.porc_rebote;
            if(incrementoY != 0)
                this.aceleracionY *= this.porc_rebote;
            
            if(Math.abs(this.aceleracionX) < 1)
                this.aceleracionX = 0;
            if(Math.abs(this.aceleracionY) < 1)
                this.aceleracionY = 0;
            
            this.movimientoX = 0;
            this.movimientoY = 0;
            
            return true;
        }
        
        return false;
    }
    
    public void actualizar(int hilo){
        this.color = new Color((int) Juego.random.getNum(hilo, 256)
                                , (int) Juego.random.getNum(hilo, 256)
                                , (int) Juego.random.getNum(hilo, 256));
        
        boolean acel = acelerar();
        
        boolean movX = Math.abs(this.movimientoX) - 1 >= 0;
        boolean movY = Math.abs(this.movimientoY) - 1 >= 0;
        
        if(acel || movX || movY){
            // El valor de movimiento debe ser minimo 1 para poder moverse
            
            int incrementoX;
            int incrementoY;
            
            while(movX || movY){
                incrementoX = 0;
                incrementoY = 0;
                
                // Define los incrementos a la coordenada para la siguiente posición
                if(movX)
                    incrementoX = (int) (Math.abs(this.movimientoX) / this.movimientoX);
                if(movY)
                    incrementoY = (int) (Math.abs(this.movimientoY) / this.movimientoY);
                
                if(rebote(hilo))
                    break;
                
                // Si la particula en dicha posición está libre
                if(this.casilla.isParticulaNull(this.x + incrementoX, this.y + incrementoY)){
                    this.casilla.moverParticula(this.x + incrementoX, this.y + incrementoY, this, false);
                    this.movimientoX -= incrementoX;
                    this.movimientoY -= incrementoY;
                }
                
                movX = Math.abs(this.movimientoX) - 1 >= 0;
                movY = Math.abs(this.movimientoY) - 1 >= 0;
            }
            
            this.activa = true;
        }
        else
            this.activa = rebote(hilo);
        
    }
    
    /**
     * Define como posición anterior la posicion actual
     */
    public void reiniciarPosAnt(){
        this.xAnt = this.x;
        this.yAnt = this.y;
        this.casillaAnt = this.casilla;
    }

    public boolean isActiva() {
        return activa;
    }
    
    public boolean isRodeada(){
        for (int i = -1; i <= -1; i++) {
            for (int j = -1; j <= -1; j++) {
                if(this.casilla.isParticulaNull(this.x + j, this.y + i) 
                    && ControladorParticulas.getCasillaRelativa(this.x + j, this.y + i, this.casilla) != null){
                    
                    return false;
                }
            }
        }
        return true;
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

    public int getXAnt() {
        return xAnt;
    }

    public int getYAnt() {
        return yAnt;
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

    public int getPosMatrizOrdFila() {
        return posMatrizOrdFila;
    }

    public int getPosMatrizOrdColumna() {
        return posMatrizOrdColumna;
    }

    public double getAceleracionX() {
        return aceleracionX;
    }

    public double getAceleracionY() {
        return aceleracionY;
    }

    public void setActualizada(boolean actualizada) {
        this.actualizada = actualizada;
    }
    
    public void setAceleracionX(double aceleracionX) {
        this.aceleracionX = aceleracionX;
    }

    public void setAceleracionY(double aceleracionY) {
        this.aceleracionY = aceleracionY;
    }

    public void setPosMatrizOrdFila(int posMatrizOrdFila) {
        this.posMatrizOrdFila = posMatrizOrdFila;
    }

    public void setPosMatrizOrdColumna(int posMatrizOrdColumna) {
        this.posMatrizOrdColumna = posMatrizOrdColumna;
    }

    
    public void setActiva(boolean activa) {
        this.activa = activa;
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
    
    public static int cordRelativaAEx(int cord){
        if(cord < 0){
            cord *= -1;
            double porc = (double)cord / (double)Casilla.SIZE;
            return (int)(Casilla.SIZE - ((porc - ((int) porc)) * Casilla.SIZE));
        }
        if(cord >= Casilla.SIZE)
            return ((cord / Casilla.SIZE) - ((int) (cord / Casilla.SIZE))) * Casilla.SIZE;
        
        return cord;
    }
}