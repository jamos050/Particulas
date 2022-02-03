/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.util.ArrayList;
import particulas.Elemento;


/**
 *
 * @author Josue Alvarez M
 */
public class ControladorLuz implements Runnable{
    
    @Override
    public void run() {
        Pixel p;
        double intencidad;
        
        double r;
        double v;
        double a;
        
        double absorcion = .005f;
        
        while(true){
            for (int i = 0; i < Pantalla.ancho; i++) {
                r = 0;
                v = 0;
                a = 0;

                intencidad = 255 - Pixel.luz_intencidadPred;
                for (int j = 0; j < Pantalla.alto; j++) {
                    p = Pantalla.pixeles.get(j).get(i);

                    p.limpiarLuz();
                    p.setLuz_intencidad((int) (p.getLuz_intencidad() + intencidad));

                    if(p.getParticula() != null){
                        int limite = 50;
                        if(r <= limite && v <= limite && a <= limite){
                            r += p.getRojo() * absorcion;
                            v += p.getVerde() * absorcion;
                            a += p.getAzul() * absorcion;
                        }
                    }
                    p.setLuzColor((int)r, (int)v, (int)a);
                    p.setActualizar(true);

                    if(intencidad == 0)
                        continue;

                    int degradado = 2;
                    try{
                        if(p.getParticula() != null && p.getParticula().isElemento() && !((Elemento)p.getParticula()).isGaseoso()){
                            if(intencidad > 0){
                                intencidad -= degradado;
                                if(intencidad < 0)
                                    intencidad = 0;
                            }
                            if(intencidad < 0){
                                intencidad += degradado;
                                if(intencidad > 0)
                                    intencidad = 0;
                            }
                        }
                    }catch(Exception e){}
                }
            }
        }
    }
    
}
