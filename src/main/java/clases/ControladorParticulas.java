/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.util.ArrayList;
import java.util.HashMap;
import particulas.Particula;

/**
 *
 * @author Josue Alvarez M
 */
public class ControladorParticulas implements Runnable{
    private static HashMap<Integer, HashMap<Integer, Casilla>> matrizCasillas;
    
    public static final GenerarRandom random = GenerarRandom.nuevaEntidad();
    
    private ArrayList<Thread> hilos;
    
    public ControladorParticulas(int alto, int ancho, int cantHilos) {
        generarMatrizCasillas(alto, ancho);
        
        ControladorParticulas.random.addLista(cantHilos - 1);
        ControladorParticulas.random.setMax(1000);
        
        generarHilos(cantHilos);
    }
    
    private void generarMatrizCasillas(int alto, int ancho){
        ControladorParticulas.matrizCasillas = new HashMap<>();
        HashMap<Integer, Casilla> fila;
        
        Casilla casilla;
        int incremento = Casilla.size * Particula.getSize();
        int posX;
        int posY = 0;
        for (int y = 0; y < alto; y += incremento) {
            posX = 0;
            fila = new HashMap<>();
            for (int x = 0; x < ancho; x += incremento) {
                casilla = new Casilla(x, y, x + incremento, y + incremento, posX, posY);
                
                fila.put(posX, casilla);
                
                posX++;
            }
            
            ControladorParticulas.matrizCasillas.put(posY, fila);
            
            posY++;
        }
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
    }
    
    public void pintar(){
        int cantFilas = ControladorParticulas.matrizCasillas.size();
        int cantColum = ControladorParticulas.matrizCasillas.get(0).size();
        for (int i = 0; i < cantFilas; i++) {
            for (int j = 0; j < cantColum; j++) {
                ControladorParticulas.matrizCasillas.get(i).get(j).pintar();
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
            
            int cantFilas = ControladorParticulas.matrizCasillas.size();
            int cantColum = ControladorParticulas.matrizCasillas.get(0).size();
            for (int j = 0; j < cantFilas; j++) {
                if(posFila == posFilaAct){
                    for (int k = 0; k < cantColum; k++) {
                        ControladorParticulas.matrizCasillas.get(j).get(k).actualizar(posHilo, cantHilos);
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
            
            x -= casilla.getXIni();
            y -= casilla.getYIni();
            
            casilla.generarParticula(x, y);
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
    public static Casilla getCasillaRango(int x, int y){
        int cantFilas = ControladorParticulas.matrizCasillas.size();
        int cantColum = ControladorParticulas.matrizCasillas.get(0).size();
        
        Casilla casillaIni = ControladorParticulas.matrizCasillas.get(0).get(0);
        Casilla casillaFin = ControladorParticulas.matrizCasillas.get(cantFilas-1).get(cantColum-1);
        
        int incremento = Casilla.size * Particula.getSize();
        int posX, posY;
        if(x < casillaIni.getXIni() || x >= casillaFin.getXFin())
            return null;
        else
            posX = ((x - casillaIni.getXIni()) / incremento) + casillaIni.getPosX();
        
        if(y < casillaIni.getYIni() || y >= casillaFin.getYFin())
            return null;
        else
            posY = ((y - casillaIni.getYIni()) / incremento) +  casillaIni.getPosY();
        
        Casilla casilla = ControladorParticulas.matrizCasillas.get(posY).get(posX);
        
        return casilla;
    }
    
    
    @Override
    public void run() {
        actualizarParticula();
    }
    
    
}
