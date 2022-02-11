/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package particulas;

import clases.Pixel;

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
        /*
        boolean completado = moverY();
        if(!completado){
            completado = moverDiagonal();
            if(!completado)
                moverX();
        }
        
        intercambiarColor();
        
        generar();
        combustion();
        */
        
        boolean completado = moverY();
        
        Pixel p;
        if(!completado){
            boolean continuar = true;
            
            cederCantidadYDiagonal();
            cederCantidadX();
            
            continuar = cederCantidadY();
            
            if(continuar){
                completado = moverDiagonal();
                if(!completado)
                    moverX();
            }
        }
        
        cederCantidad();
        
        intercambiarColor();
        
        generar();
        combustion();
    }
    
}
