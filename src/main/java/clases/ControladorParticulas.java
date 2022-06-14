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
public class ControladorParticulas implements Runnable{
    private Casilla casillaInicio;
    
    public static final GenerarRandom random = GenerarRandom.nuevaEntidad();
    
    private ArrayList<Thread> hilos;
    
    public ControladorParticulas(int alto, int ancho, int cantHilos) {
        this.casillaInicio = Casilla.nuevaCasillaInicio(alto, ancho);
        
        ControladorParticulas.random.addLista(cantHilos - 1);
        ControladorParticulas.random.setMax(1000);
        
        generarHilos(cantHilos);
    }
    
    private void generarHilos(int cantHilos){
        this.hilos = new ArrayList<>();
        
        Thread hilo;
        for (int i = 0; i < cantHilos; i++) {
            hilo = new Thread(this);
            this.hilos.add(hilo);
        }
    }
    
    public void actualizar() throws InterruptedException{
        boolean continuar = false;
        while(!continuar){
            continuar = true;
            for (Thread h : hilos) {
                if(h.isAlive()){
                    continuar = false;
                    break;
                }
            }
        }
        
        for (Casilla casillaFila = this.casillaInicio; casillaFila != null; casillaFila = casillaFila.getCasillaAbj()) {
            for (Casilla casillaCol = casillaFila; casillaCol != null; casillaCol = casillaCol.getCasillaDer()) {
                casillaCol.pintar();
            }
        }
    }
    
    private void actualizarParticula(){
        int posHilo = 0;
        for (int i = 0; i < this.hilos.size(); i++) {
            if(this.hilos.get(i).getId() == Thread.currentThread().getId()){
                posHilo = i;
                break;
            }
        }
        
        this.casillaInicio.actualizar(posHilo, this.hilos.size());
    }
    
    public void generarParticula(int x, int y){
        if(x >= 0 && y >= 0){
            Casilla casilla = getCasilla(x, y);
            if(casilla != null){
                x -= casilla.getX();
                y -= casilla.getY();
                casilla.generarParticula(x, y);
            }
        }
    }
    
    /**
     * Busca la casilla que contenga la coordenada indicada.
     * La coordenada corresponde a la de una particula en 
     * en el plano carteciano general de la matriz.
     * 
     * @param x
     * @param y
     * @return 
     */
    public Casilla getCasilla(int x, int y){
        int posX, posY;
        
        for (Casilla casillaFila = this.casillaInicio; casillaFila != null; casillaFila = casillaFila.getCasillaAbj()) {
            for (Casilla casillaCol = casillaFila; casillaCol != null; casillaCol = casillaCol.getCasillaDer()) {
                posX = casillaCol.getX();
                posY = casillaCol.getY();
                if(posX <= x && x < posX + Casilla.size * Particula.getSize() 
                && posY <= y && y < posY + Casilla.size * Particula.getSize())
                    return casillaCol;
            }
        }
        
        return null;
    }
    
    @Override
    public void run() {
        actualizarParticula();
    }
    
    
}
