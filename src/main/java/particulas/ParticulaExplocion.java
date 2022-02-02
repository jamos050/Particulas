/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package particulas;

import clases.Pantalla;
import clases.Pixel;
import java.util.Random;

/**
 *
 * @author Josue Alvarez M
 */
public class ParticulaExplocion extends Particula{
    private Particula particula = null;
    private int intencidad = 7;
    
    public ParticulaExplocion(Pixel pixel) {
        this.tipo = Particula.PARTICULA_EXPLOCION;
        
        this.velociadY = -15;
        
        this.pixel = pixel;
        iniciarParticula();
        
    }
    
    private void iniciarParticula(){
        this.particula = this.pixel.getParticula();
        this.pixel.eliminarParticula();
        
        if(this.particula != null){
            if(!this.particula.isElementoGaseoso()){
                setColor(this.particula.getColor());
                this.pixel.setParticula(this);
            }
        }
        else{
            Random rand = new Random();
            if(rand.nextInt(100) < 10){
                if(rand.nextInt(100) < 50){
                    this.particula = Pantalla.fabricaP.generarParticula(this.pixel, Elemento.FUEGO, true);
                    if(this.particula == null && this.pixel != null)
                        this.pixel.eliminarParticula();
                }
                else{
                    this.particula = Pantalla.fabricaP.generarParticula(this.pixel, Elemento.HUMO, true);
                    if(this.particula == null){
                        if(this.pixel != null)
                            this.pixel.eliminarParticula();
                    }
                    else{
                        this.particula.setCombustion(true);
                        this.particula.setVida(40);
                    }
                }
            }
        }
        
    }
    
    @Override
    protected void eliminarParticulaCombustion() {}

    @Override
    protected boolean reaccionar(Particula particula) {
        return false;
    }

    @Override
    public void comportamiento() {
        int fila = this.pixel.getFila();
        int columna = this.pixel.getColumna();

        Pixel p;
        boolean continuar = moverY();
        this.velociadY = -15;
        
        if(!continuar){
            moverYDiagonal();
            moverX();
            if(fila - 1 >= 0){
                p = Pantalla.pixeles.get(fila - 1).get(columna);
                if(p.getParticula() != null && !p.getParticula().isParticulaExplocion()){
                    this.pixel.eliminarParticula();
                    if(this.particula != null)
                        this.particula.moverParticula(this.pixel);
                }
            }
            else{
                this.pixel.eliminarParticula();
                if(this.particula != null)
                    this.particula.moverParticula(this.pixel);
            }
        }
        
        this.intencidad--;
        if(this.intencidad <= 0){
            this.pixel.eliminarParticula();
            if(this.particula != null)
                this.particula.moverParticula(this.pixel);
        }
    }
}
