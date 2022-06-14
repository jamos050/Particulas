/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Josue Alvarez M
 */
public class GenerarRandom implements Runnable{
    private static GenerarRandom entidad = null;
    
    private ArrayList<ConcurrentLinkedQueue<Double>> listNum;
    
    private int max; // Cntidad máxima de números por sub lista
    private int listCant; // Cantidad de subListas a generar
    
    
    private final Random random = new Random();
    
    private GenerarRandom(){
        this.max = 100;
        this.listCant = 1;
        
        iniciar();
    }
    
    private GenerarRandom(int listCant, int max){
        this.max = 100;
        if(max > 0)
            this.max = max;
            
        this.listCant = 1;
        if(listCant > 1)
            this.listCant = listCant;
        
        iniciar();
    }
    
    public static GenerarRandom nuevaEntidad(){
        if(GenerarRandom.entidad == null)
            GenerarRandom.entidad = new GenerarRandom();
        
        return GenerarRandom.entidad;
    }
    
    public static GenerarRandom nuevaEntidad(int listCant, int max){
        if(GenerarRandom.entidad == null)
            GenerarRandom.entidad = new GenerarRandom(listCant, max);
        
        return GenerarRandom.entidad;
    }
    
    
    private void iniciar(){
        this.listNum = new ArrayList<>();
        for (int i = 0; i < this.listCant; i++)
            this.listNum.add(new ConcurrentLinkedQueue<>());
        
        Thread t = new Thread(this);
        t.start();
    }
    
    private void generar(){
        for (int i = 0; i < this.listCant; i++) {
            ConcurrentLinkedQueue<Double> L = this.listNum.get(i);
            
            if(L.size() >= this.max)
                continue;
            
            L.add(this.random.nextDouble());
        }
    }
    
    public void addLista(int cant){
        for (int i = 0; i < cant; i++){
            this.listNum.add(new ConcurrentLinkedQueue<Double>());
            this.listCant++;
        }
    }
    
    /**
     * 
     * @param posList
     * Posición de sub lista a sacar el num
     * 
     * @return 
     * Retorna un número entre 0 y 1, excluyendo el 1.
     */
    public double getNum(int posList){
        if(posList > this.listCant - 1)
            posList = 0;
        
        double num;
        if(!this.listNum.get(posList).isEmpty()) // si se ha generado un número lo retorna
            num = this.listNum.get(posList).poll();
        else
            num = this.random.nextDouble(); // si no, lo genera
            
        return num;
    }
    
    /**
     * 
     * @param posList
     * Posición de sub lista a sacar el num
     * 
     * @param max
     * Valor máximo del rango
     * 
     * @return 
     * Retorna un número entre 0 y max, excluyendo max.
     */
    public double getNum(int posList, double max){
        return getNum(posList) * max;
    }
    
    /**
     * 
     * @param posList
     * Posición de sub lista a sacar el num
     * 
     * @param max
     * Valor máximo del rango
     * @param min
     * Valor mínimo del rango
     * 
     * @return 
     * Retorna un número entre min y max, excluyendo max.
     */
    public double getNum(int posList, double max, double min){
        return (getNum(posList) * (max - min)) + min;
    }

    public void setMax(int max) {
        this.max = max;
    }
    
    @Override
    public void run() {
        while(true){
            generar();
        }
    }
    
}
