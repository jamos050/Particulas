/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import particulas.Particula;

/**
 *
 * @author Josue Alvarez M
 */
public class Casilla {
    public static final int size = 64;
    
    private Particula[][] matriz; 
    private Integer posiciones[];
    
    // Casillas continuas
    private Casilla casillaIzq = null;
    private Casilla casillaDer = null;
    private Casilla casillaArb = null;
    private Casilla casillaAbj = null;
    
    // posición en la que se ubica en pantalla
    private int x;
    private int y;
    private int xFin;
    private int yFin;
    
    public Casilla() {
        this.matriz = new Particula[Casilla.size][Casilla.size];
        
        this.x = 0;
        this.y = 0;
        
        definirPosicionesArray();
    }
    
    public static Casilla nuevaCasillaInicio(int alto, int ancho){
        int incremento = Casilla.size * Particula.getSize();
        
        Casilla casillaInicio = new Casilla();
        casillaInicio.xFin = incremento;
        casillaInicio.yFin = incremento;
        
        Casilla casillaIteraColFilaAnt = null; // Casilla de iteración de la fila anterior
        Casilla casillaIteraCol = casillaInicio;
        Casilla casillaIteraFila = casillaInicio;
        
        alto += incremento;
        ancho += incremento;
        for (int posYIni = 0; posYIni < alto; posYIni += incremento) {
            for (int posXIni = 0; posXIni + incremento < ancho; posXIni += incremento) {
                // Define la casilla derecha
                casillaIteraCol.casillaDer = new Casilla();
                // Define la casilla izquierda
                casillaIteraCol.casillaDer.casillaIzq = casillaIteraCol;
                
                // actualiza la posición de la casilla
                casillaIteraCol.casillaDer.x += casillaIteraCol.xFin;
                casillaIteraCol.casillaDer.y += casillaIteraCol.y;
                casillaIteraCol.casillaDer.xFin += casillaIteraCol.xFin + incremento;
                casillaIteraCol.casillaDer.yFin += casillaIteraCol.yFin;
                
                if(casillaIteraColFilaAnt != null){
                    // Pasa a la siguiente casilla ya que se definió 
                    // la casilla derecha de la fila actual
                    casillaIteraColFilaAnt = casillaIteraColFilaAnt.casillaDer;
                    
                    casillaIteraCol.getCasillaDer().casillaArb = casillaIteraColFilaAnt;
                }
                
                // Cambia la casilla de iteración a la siguiente
                casillaIteraCol = casillaIteraCol.casillaDer;
            }
            if(posYIni + incremento < alto){
                // Define la casilla de abajo
                casillaIteraFila.casillaAbj = new Casilla();
                // Define la casilla de arriba
                casillaIteraFila.casillaAbj.casillaArb = casillaIteraFila;
                
                // actualiza la posición de la casilla
                casillaIteraFila.casillaAbj.x += casillaIteraFila.x;
                casillaIteraFila.casillaAbj.y += casillaIteraFila.yFin;
                casillaIteraFila.casillaAbj.xFin += casillaIteraFila.xFin;
                casillaIteraFila.casillaAbj.yFin += casillaIteraFila.yFin + incremento;
                
                // La casilla iteración columna fila anterior
                casillaIteraColFilaAnt = casillaIteraFila; 

                // Cambia las casillas de iteración
                casillaIteraFila = casillaIteraFila.casillaAbj;
                casillaIteraCol = casillaIteraFila;
            }
        }
        
        return casillaInicio;
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
        for (int i = 0; i < Casilla.size; i++) {
            for (int tam = Casilla.size; tam > 0; tam--) {
                pos = (int) ControladorParticulas.random.getNum(hilo, tam);

                particula = this.matriz[i][this.posiciones[pos]];
                if(particula != null)
                    particula.actualizar(hilo);

                // pasa el número seleccionado a la ultima posición
                posVal = this.posiciones[pos];
                this.posiciones[pos] = this.posiciones[tam - 1];
                this.posiciones[tam - 1] = posVal;

            }
        }
    }
    
    void pintar() {
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
        
        if(x < Casilla.size && y < Casilla.size && this.matriz[y][x] == null)
            this.matriz[y][x] = new Particula(this, x, y);
    }
    
    /**
     * Retorna la partícula en la posición indicada,
     * de no encontrarla retorna null.
     * 
     * @param x
     * Coordenada x en casilla
     * @param y
     * Coordenada y en casilla
     * @return 
     * Partícula en coordenada indicada o null.
     */
    public Particula getParticula(int x, int y){
        Particula particula = null;
        
        if(x < Casilla.size && x >= 0 && y < Casilla.size && y >= 0)
            particula = this.matriz[x][y];
        else{
            Casilla casilla = this;
            
            if(x < 0 && this.casillaIzq != null){
                casilla =  this.casillaIzq;
                x = Casilla.size + x;
            }
            else if(x >= Casilla.size && this.casillaDer != null){
                casilla =  this.casillaDer;
                x = Casilla.size - x;
            }
            
            if(y < 0 && casilla.casillaArb != null){
                casilla = casilla.casillaArb;
                y = Casilla.size + y;
            }
            else if(y >= Casilla.size && casilla.casillaAbj != null){
                casilla = casilla.casillaAbj;
                y = Casilla.size - y;
            }
            
            if(casilla != this)
                particula = casilla.getParticula(x, y);
        }
        
        return particula;
    }
    
    public Casilla getCasillaIzq() {
        return casillaIzq;
    }

    public Casilla getCasillaDer() {
        return casillaDer;
    }

    public Casilla getCasillaArb() {
        return casillaArb;
    }

    public Casilla getCasillaAbj() {
        return casillaAbj;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getXFin() {
        return xFin;
    }

    public int getYFin() {
        return yFin;
    }
    
}
