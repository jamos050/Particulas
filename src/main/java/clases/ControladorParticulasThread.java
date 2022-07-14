/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 *
 * @author Josue Alvarez M
 */
public class ControladorParticulasThread implements Runnable{
    private volatile boolean actualizar;
    private volatile boolean finalizado;
    
    private final int id;
    private final int cantidad; // cantidad total de hilos (ControladorParticulasThread)
    
    private final Casilla[] casillasArray;
    private final int casArr_tam;

    public ControladorParticulasThread(int id, int cantidad, Casilla[] casillasArr, int tam) {
        this.id = id;
        this.cantidad = cantidad;
        
        this.casillasArray = casillasArr;
        this.casArr_tam = tam;
        
        this.actualizar = false;
        this.finalizado = false;
        
        iniciarThread();
    }

    private void actualizarParticula(){
        Casilla casilla;
        for (int i = this.id; i < this.casArr_tam; i += this.cantidad) {
            casilla = this.casillasArray[i];
            
            if(casilla == null)
                break;
            
            casilla.actualizar(this.id);
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
