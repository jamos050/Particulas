/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package particulas;

/**
 *
 * @author Josue Alvarez M
 */
public abstract class Gaseoso extends Elemento{

    public Gaseoso(int tipo) {
        super(tipo, Elemento.ESTADO_GASEOSO);
        
        this.temperaturaMin = 750;
        this.cambiarColor = 3;
        this.dencidad = 5;
        this.friccion = 0;
        this.velociadY = -3;
        this.velociadX = 3;
    }
    
    @Override
    public void comportamiento() {
        boolean completado = moverYDiagonal();
        if(!completado)
            moverX();
        
        intercambiarColor();
        
        generar();
        combustion();
    }
    
}
