/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Josue Alvarez M
 */
public class ControladorParticulasThread implements Runnable{
    private volatile boolean actualizar;
    private volatile boolean finalizado;
    
    private final int id;
    private final int cantidad; // cantidad total de hilos (ControladorParticulasThread)
    
    private final HashMap<Integer, HashMap<Integer, Casilla>> matrizCasillas;
    private final ArrayList<Integer> posicionesFila;
    private final ArrayList<Integer> posicionesColumna;
    
    public ControladorParticulasThread(int id, int cantidad, HashMap<Integer, HashMap<Integer, Casilla>> matrizCasillas) {
        this.id = id;
        this.cantidad = cantidad;
        
        this.matrizCasillas = matrizCasillas;
        
        this.actualizar = false;
        this.finalizado = false;
        
        this.posicionesFila = new ArrayList<>();
        this.posicionesColumna = new ArrayList<>();
        definirPosiciones();
                
        iniciarThread();
    }
    
    private void definirPosiciones(){
        int cantFilas = this.matrizCasillas.size();
        int cantColumnas = this.matrizCasillas.get(0).size();
        
        for (int i = 0; i < cantColumnas; i++) {
            this.posicionesFila.add(i);
        }
        for (int i = 0; i < cantFilas; i++) {
            this.posicionesColumna.add(i);
        }
    }
    
    private void actualizarParticula(){
        int cantFilas = this.matrizCasillas.size();
        int cantColumnas = this.matrizCasillas.get(0).size();
        
        // Redefine las posiciones de ser necesario
        if(cantColumnas != this.posicionesFila.size() || cantFilas != this.posicionesColumna.size()){
            this.posicionesFila.clear();
            this.posicionesColumna.clear();
            definirPosiciones();
        }
        
        Casilla casilla;
        int pos, posVal;
        for (int i = cantFilas - 1; i >= 0; i--) {
            for (int tam = cantColumnas; tam > 0; tam--) {
                pos = (int) Juego.random.getNum(this.id, tam);
                posVal = this.posicionesFila.get(pos);
                
                casilla = this.matrizCasillas.get(i).get(posVal);

                // Pasa la casilla al final
                this.posicionesFila.set(pos, this.posicionesFila.get(tam-1));
                this.posicionesFila.set(tam-1, posVal);

                if(!casilla.isNull()){
                    casilla.pintarBorde();
                    casilla.actualizar(this.id);
                }
                else
                    casilla.quitarBorde();
            }
        }
        
        for (int i = 0; i < cantFilas; i++) {
            for (int j = 0; j < cantColumnas; j++) {
                casilla = this.matrizCasillas.get(i).get(j);
                
                casilla.setActualizadaFalse();
            }
        }
    }
    
    private void iniciarThread(){
        Thread t = new Thread(this);
        t.start();
    }
    
    public void actualizar(){
        this.finalizado = false;
        this.actualizar = true;
    }
    
    public boolean isFinalizado() {
        return finalizado;
    }

    @Override
    public void run() {
        while(true){
            if(this.actualizar){
                this.actualizar = false;
                actualizarParticula();
                this.finalizado = true;
            }
        }
    }
    
}