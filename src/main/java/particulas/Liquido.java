/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package particulas;

import clases.Pantalla;
import clases.Pixel;
import java.util.Random;

/**
 *
 * @author Josue Alvarez M
 */
public abstract class Liquido extends Elemento{

    public Liquido(int tipo) {
        super(tipo, Elemento.ESTADO_LIQUIDO);
        
        this.inercia = false;
        this.dencidad = 10;
        this.friccion = 0;
        this.velociadX = 15;
    }
    
    @Override
    public void comportamiento() {
        
        boolean completado = moverY();
        if(!completado){
            completado = moverDiagonal();
            if(!completado)
                moverX();
        }
        
        intercambiarColor();
        
        generar();
        combustion();
        
        /*
        int fila = this.pixel.getFila();
        int columna = this.pixel.getColumna();
        
        boolean completado = moverY();
        
        Pixel p;
        if(!completado){
            boolean continuar = true;
            
            cederCantidadDiagonal();
            cederCantidadX();
            continuar = cederCantidadY();
            
            if(continuar){
                completado = moverDiagonal();
                if(!completado)
                    moverX();
            }
        }
        
        int limite = 1;
        if(this.cantidad > limite && fila - 1 >= 0){
            p = Pantalla.pixeles.get(fila - 1).get(columna);
            if(p.getParticula() == null){
                Pantalla.fabricaP.generarParticula(p, this.tipo, true);
                p.getParticula().agregarCantidad(this.cantidad - limite - 1);
                //p.getParticula().comportamiento();
            }
            else if(p.getParticula().isElemento() && ((Elemento)p.getParticula()).getTipo() == this.tipo)
                p.getParticula().agregarCantidad(this.cantidad - limite);
            this.cantidad = limite;
        }
        */
        intercambiarColor();
        
        generar();
        combustion();
    }
    
}
