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
import particulas.Liquido;

/**
 *
 * @author Josue Alvarez M
 */
public class Agua extends Liquido{
    public Agua(Pixel pixel) {
        super(Elemento.AGUA);
        
        this.temperatura = -150;
        this.temperaturaMin = 500;
        this.conductividad = 20;
        
        this.dencidad = 9;
        this.cambiarColor = 20;
        setColor(100, 175, 255);
        cambiarTono(4,0);
        
        this.pixel = pixel;
        this.pixel.setParticula(this);
    }

    @Override
    protected void eliminarParticulaCombustion() {
        Random rand = new Random();
        
        if(rand.nextInt(100) > 25)
            Pantalla.fabricaP.generarParticula(this.pixel, Elemento.VAPOR_AGUA, true);
    }
}
