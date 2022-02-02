/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package particulas.elementos;

import clases.Pixel;
import particulas.Elemento;
import particulas.Gaseoso;

/**
 *
 * @author Josue Alvarez M
 */
public class Fuego extends Gaseoso{
    public Fuego(Pixel pixel) {
        super(Elemento.FUEGO);
        
        this.vida = 40;
        this.fuenteTemp = true;
        this.temperatura = 15;
        this.temperaturaMin = 0;
        this.conductividad = 0;
        
        this.resistencia_fuego = 0;
        this.dencidad = 3;
        this.cambiarColor = 5;
        setColor(255, 75, 35);
        
        cambiarTono(10,0);
        
        this.pixel = pixel;
        this.pixel.setParticula(this);
    }

    @Override
    protected void eliminarParticulaCombustion() {}
}
