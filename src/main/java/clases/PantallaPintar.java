/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Josue Alvarez M
 */
public class PantallaPintar implements Runnable{
    private boolean terminado = true;
    private boolean iniciar = false;
    private final int hilo;
    private final int cantHilos;
    
    private Graphics2D g2d;

    public PantallaPintar(int hilo, int cantHilos) {
        this.hilo = hilo;
        this.cantHilos = cantHilos;
    }
    
    //is
    public boolean isTerminado() {
        return terminado;
    }

    public boolean isIniciar() {
        return iniciar;
    }
    
    
    //set
    public void setTerminado(boolean terminado) {
        this.terminado = terminado;
    }

    public void setIniciar(boolean iniciar) {
        this.iniciar = iniciar;
    }

    public void setG2d(Graphics2D g2d) {
        this.g2d = g2d;
    }
    
    
    @Override
    public void run() {
        ArrayList<Pixel> fila;
        while(true){
            if(this.iniciar){
                this.iniciar = false;
                this.terminado = false;
                
                for (int i = this.hilo; i < Pantalla.pixeles.size(); i+= this.cantHilos) {
                    fila = Pantalla.pixeles.get(i);
                    for (Pixel p : fila)
                        if(p.isActualizar())
                            p.actualizarColorPantalla(this.g2d);
                    
                }
                
                this.terminado = true;
            }
            try {
                Thread.sleep(0, 1);
            } catch (Exception e) {
            }
        }
    }

    void iniciar() {
        this.iniciar = true;
    }
}
