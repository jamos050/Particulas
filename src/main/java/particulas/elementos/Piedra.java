/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particulas.elementos;

import clases.Pantalla;
import particulas.Elemento;
import clases.Pixel;
import java.util.Random;
import particulas.Solido;

/**
 *
 * @author Josue Alvarez M
 */
public class Piedra extends Solido{
    public Piedra(Pixel pixel) {
        super(Elemento.PIEDRA);
        
        this.temperaturaMin = 1500;
        
        this.resistencia_inercia = 90;
        this.velociadX = 0;
        setColor(160, 160, 160);
        
        cambiarTono(15,0);
        
        this.pixel = pixel;
        this.pixel.setParticula(this);
    }
    
    @Override
    protected void eliminarParticulaCombustion() {
        Random rand = new Random();
        
        if(rand.nextInt(100) > 50){
            Pantalla.fabricaP.generarParticula(this.pixel, Elemento.LAVA, true);
        }
    }
}