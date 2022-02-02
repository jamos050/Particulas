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
public class Arena extends Solido{
    public Arena(Pixel pixel) {
        super(Elemento.ARENA);
        
        this.temperaturaMin = 800;
        
        this.dencidad = 13;
        this.friccion = 0.25;
        this.resistencia_inercia = 0;
        setColor(220, 190, 150);
        
        cambiarTono(20,0);
        
        this.pixel = pixel;
        this.pixel.setParticula(this);
    }

    @Override
    protected void eliminarParticulaCombustion() {}
}
