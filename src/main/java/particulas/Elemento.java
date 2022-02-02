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
public abstract class Elemento extends Particula{
    protected int tipo;
    public static final int AGUA = 0;
    public static final int ARENA = 1;
    public static final int PIEDRA = 2;
    public static final int TIERRA = 3;
    public static final int LAVA = 4;
    public static final int HUMO = 5;
    public static final int VAPOR_AGUA = 6;
    public static final int FUEGO = 7;
    public static final int HIERRO = 8;
    
    protected int estado;
    public static final int ESTADO_SOLIDO = 0;
    public static final int ESTADO_LIQUIDO = 2;
    public static final int ESTADO_GASEOSO = 3;
    
    public Elemento(int tipo, int estado) {
        this.tipo = tipo;
        super.tipo = Particula.ELEMENTO;
        this.estado = estado;
    }
    
    public void intercambiarColor(){
        Random rand = new Random();
        if(rand.nextInt(100) < this.cambiarColor){
            int fila = this.pixel.getFila();
            int columna = this.pixel.getColumna();

            Pixel p = null;
            Pixel p2 = null;
            if(fila - 1 >= 0)
                p = Pantalla.pixeles.get(fila - 1).get(columna);
            if(columna - 1 >= 0)
                p2 = Pantalla.pixeles.get(fila).get(columna - 1);

            Particula particula2 = null;

            if(rand.nextInt(100) > 50 && p != null)
                particula2 = p.getParticula();

            else if(p2 != null)
                particula2 = p2.getParticula();

            if(particula2 != null && particula2.isElemento() && this.tipo == ((Elemento)particula2).getTipo())
                intercambiarColor(particula2);
        }
    }
    
    //get
    public int getEstado() {
        return estado;
    }
    
    public int getTipo() {
        return tipo;
    }

    @Override
    protected boolean reaccionar(Particula particula) {
        Random rand = new Random();
        
        if(particula == null)
            return false;
        
        if(isElemento() && particula.isElemento()){
            Elemento elem2 = (Elemento) particula;
            
            if(this.tipo == Elemento.AGUA && elem2.getTipo() == Elemento.LAVA){
                this.pixel.eliminarParticula();
                elem2.getPixel().eliminarParticula();
                if(rand.nextInt(100) > 50)
                    Pantalla.fabricaP.generarParticula(this.pixel, Elemento.VAPOR_AGUA, true);
                else
                    Pantalla.fabricaP.generarParticula(elem2.getPixel(), Elemento.PIEDRA, true);
                
                return true;
            }
            if(this.tipo == Elemento.LAVA && elem2.getTipo() == Elemento.AGUA){
                this.pixel.eliminarParticula();
                elem2.getPixel().eliminarParticula();
                
                if(rand.nextInt(100) > 50)
                    Pantalla.fabricaP.generarParticula(elem2.getPixel(), Elemento.VAPOR_AGUA, true);
                else
                    Pantalla.fabricaP.generarParticula(this.pixel, Elemento.PIEDRA, true);
                
                return true;
            }
        }
        return false;
    }

    //is
    public boolean isSolido() {
        return this.estado == Elemento.ESTADO_SOLIDO;
    }
    public boolean isLiquido() {
        return this.estado == Elemento.ESTADO_LIQUIDO;
    }
    public boolean isGaseoso() {
        return this.estado == Elemento.ESTADO_GASEOSO;
    }
    
}
