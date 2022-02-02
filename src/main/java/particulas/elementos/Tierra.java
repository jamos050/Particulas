/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particulas.elementos;

import particulas.Elemento;
import clases.Pixel;
import particulas.Solido;

/**
 *
 * @author Josue Alvarez M
 */
public class Tierra extends Solido{
    public Tierra(Pixel pixel) {
        super(Elemento.TIERRA);
        
        this.temperaturaMin = 900;
        
        this.dencidad = 14;
        this.friccion = 0.40;
        this.resistencia_inercia = 85;
        this.velociadX = 0;
        setColor(160, 125, 90);
        cambiarTono(10,0);
        
        this.pixel = pixel;
        this.pixel.setParticula(this);
    }
    
    @Override
    protected void eliminarParticulaCombustion() {}
}
