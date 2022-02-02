/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import particulas.Particula;
import java.util.Random;
import java.util.stream.IntStream;

/**
 *
 * @author Josue Alvarez M
 */

public class ControladorParticulas implements Runnable{
    
    /**
     * Intercambia dos posiciones del array.
     * ...
     */
    protected void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Mescla un array con Knut's algorithm.
     * ...
     */
    public void shuffle(int[] array) {
        Random random = new Random();
        int i = 0;
        for (int j : random.ints(array.length, 0, array.length).toArray()) {
            swap(array, i++, j);
        }
    }
    
    private void actualizarParticulas(){
        Particula particula;
        
        int posFila = Pantalla.alto - 1;
        
        int[] columna = IntStream.rangeClosed(0, Pantalla.ancho - 1).toArray();
        
        while(posFila >= 0){
            shuffle(columna);
            
            for (int i : columna) {
                particula = Pantalla.pixeles.get(posFila).get(i).getParticula();
                if(particula != null){
                    if(particula.isActualizar() || (!particula.isElementoGaseoso() && !particula.isParticulaExplocion())){
                        particula.comportamiento();
                        particula.setActualizar(false);
                    }
                    else if(particula.isElementoGaseoso() || particula.isParticulaExplocion())
                        particula.setActualizar(true);
                    
                }
            }
            posFila--;
        }
    }
    
    @Override
    public void run() {
        while(true){
            actualizarParticulas();
            
            try{
                Thread.sleep(25);
            }catch(Throwable e){}
        }
    }
}
