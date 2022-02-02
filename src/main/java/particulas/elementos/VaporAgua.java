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
public class VaporAgua extends Gaseoso{
    public VaporAgua(Pixel pixel) {
        super(Elemento.VAPOR_AGUA);
        
        this.dencidad = 4;
        setColor(225, 235, 240);
        
        cambiarTono(5,0);
        
        this.pixel = pixel;
        this.pixel.setParticula(this);
    }
    
    @Override
    protected void eliminarParticulaCombustion() {}
}
