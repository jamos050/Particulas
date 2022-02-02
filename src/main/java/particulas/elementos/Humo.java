/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package particulas.elementos;

import clases.Pixel;
import particulas.Elemento;
import particulas.Gaseoso;
import particulas.Particula;

/**
 *
 * @author Josue Alvarez M
 */
public class Humo extends Gaseoso{
    public Humo(Pixel pixel) {
        super(Elemento.HUMO);
        
        setColor(80, 80, 80);
        
        cambiarTono(10,0);
        
        this.pixel = pixel;
        this.pixel.setParticula(this);
    }
    
    @Override
    protected void eliminarParticulaCombustion() {}
}
