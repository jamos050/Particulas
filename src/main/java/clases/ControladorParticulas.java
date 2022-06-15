/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.util.ArrayList;

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
        for (Thread h : hilos)
            h.start();
        
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
        
        generarHilos(this.hilos.size());
        
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
        
        int posFila;
        int posFilaAct; // posición de la fila a actualizar
        int cantHilos = this.hilos.size();
        for (int i = 0; i < 2; i++) {
            posFila = 0;
            posFilaAct = posHilo*2;
            
            if(i == 1)
                posFilaAct++;
            
            for (Casilla casillaFila = this.casillaInicio; casillaFila != null; casillaFila = casillaFila.getCasillaAbj()) {
                if(posFila == posFilaAct){
                    for (Casilla casillaCol = casillaFila; casillaCol != null; casillaCol = casillaCol.getCasillaDer()) {
                        casillaCol.actualizar(posHilo, cantHilos);
                    }
                    posFilaAct += cantHilos*2;
                }
                posFila++;
            }
        }
    }
    
    public void generarParticula(int x, int y){
        if(x >= 0 && y >= 0){
            Casilla casilla = getCasillaRango(x, y);
            if(casilla != null){
                x -= casilla.getX();
                y -= casilla.getY();
                casilla.generarParticula(x, y);
            }
        }
    }
    
    /**
     * Busca la casilla que contenga en su rango la coordenada indicada.
     * La coordenada corresponde a la de una particula en 
     * en el plano carteciano de la pantalla.
     * 
     * @param x
     * @param y
     * @return 
     */
    public Casilla getCasillaRango(int x, int y){
        for (Casilla casillaFila = this.casillaInicio; casillaFila != null; casillaFila = casillaFila.getCasillaAbj()) {
            for (Casilla casillaCol = casillaFila; casillaCol != null; casillaCol = casillaCol.getCasillaDer()) {
                if(casillaCol.getX() <= x && x < casillaCol.getXFin()
                && casillaCol.getY() <= y && y < casillaCol.getYFin())
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
