/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package particulas.elementos;

import clases.Pixel;
import particulas.Elemento;
import particulas.Solido;

/**
 *
 * @author Josue Alvarez M
 */
public class Hierro extends Solido{
    public Hierro(Pixel pixel) {
        super(Elemento.HIERRO);
        
        this.particulaExp = false;
        
        this.estatico = true;
        
        this.temperaturaMin = 2000;
        this.conductividad = 35;
        
        this.dencidad = 16;
        this.resistencia_inercia = 95;
        this.velociadX = 0;
        setColor(210, 210, 210);
        
        cambiarTono(10,0);
        
        this.pixel = pixel;
        this.pixel.setParticula(this);
    }
    
    @Override
    protected void eliminarParticulaCombustion() {}
}
