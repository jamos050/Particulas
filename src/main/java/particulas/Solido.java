/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package particulas;

/**
 *
 * @author Josue Alvarez M
 */
public abstract class Solido extends Elemento{
    protected boolean estatico = false; // si es estatico la particula no se moverá
    
    public Solido(int tipo) {
        super(tipo, Elemento.ESTADO_SOLIDO);
        
        this.dencidad = 15;
    }
    
    @Override
    public void comportamiento() {
        if(!this.estatico){
            boolean completado = moverY();
            if(!completado)
                moverDiagonal();

            moverX();
        }
        intercambiarColor();
        
        generar();
        combustion();
    }

    public boolean isEstatico() {
        return estatico;
    }

    public void setEstatico(boolean estatico) {
        this.estatico = estatico;
    }
    
    
    
}
