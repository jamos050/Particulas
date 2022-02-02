/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particulas.elementos;

import particulas.Elemento;
import clases.Pixel;
import particulas.Liquido;

/**
 *
 * @author Josue Alvarez M
 */
public class Lava extends Liquido{
    public Lava(Pixel pixel) {
        super(Elemento.LAVA);
        
        this.generar = Elemento.FUEGO;
        this.generar_prob = 1;
        
        this.fuenteTemp = true;
        this.temperatura = 20;
        this.temperaturaMin = 999;
        this.conductividad = 0;
        
        this.cambiarColor = 1;
        this.velociadMax = 1;
        this.velociadX = 1;
        this.velociadY = 1;
        setColor(255, 50, 0);
        cambiarTono(70,0);
        
        this.pixel = pixel;
        this.pixel.setParticula(this);
    }

    @Override
    protected void eliminarParticulaCombustion() {}
}
