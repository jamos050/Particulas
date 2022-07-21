/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.util.ArrayList;
import java.util.HashMap;
import particulas.Particula;

/**
 *
 * @author Josue Alvarez M
 */
public class ControladorParticulas{
    private static HashMap<Integer, HashMap<Integer, Casilla>> matrizCasillas;
    
    private static Casilla[] casillasArr;
    private static int casArr_tam; // tamaño del arreglo de casillas
    private static int posFinArr; // ultima posición disponible del array
    
    private ArrayList<ControladorParticulasThread> hilos;
    
    private static double gravedadX, gravedadY;
    
    public ControladorParticulas(int alto, int ancho, int cantHilos) {
        ControladorParticulas.gravedadX = 0;
        ControladorParticulas.gravedadY = 1;
        
        generarMatrizCasillas(alto, ancho);
        
        ControladorParticulas.posFinArr = 0;
        ControladorParticulas.casArr_tam = (int) ((matrizCasillas.size() * matrizCasillas.get(0).size())*1.5);
        ControladorParticulas.casillasArr = new Casilla[ControladorParticulas.casArr_tam];
        
        generarHilos(cantHilos);
    }
    
    private void generarMatrizCasillas(int alto, int ancho){
        ControladorParticulas.matrizCasillas = new HashMap<>();
        HashMap<Integer, Casilla> fila;
        
        Casilla casilla;
        int incremento = Casilla.SIZE * Particula.getSize();
        int posX;
        int posY = 0;
        for (int y = 0; y < alto; y += incremento) {
            posX = 0;
            fila = new HashMap<>();
            for (int x = 0; x < ancho; x += incremento) {
                casilla = new Casilla(x, y, x + incremento, y + incremento, posX, posY);
                
                fila.put(posX, casilla);
                
                posX++;
            }
            
            ControladorParticulas.matrizCasillas.put(posY, fila);
            
            posY++;
        }
    }
    
    private void generarHilos(int cantHilos){
        this.hilos = new ArrayList<>();
        
        ControladorParticulasThread controladorPT;
        for (int i = 0; i < cantHilos; i++) {
            controladorPT = new ControladorParticulasThread(i, cantHilos, this.matrizCasillas);
            this.hilos.add(controladorPT);
        }
    }
    
    public void actualizar() throws InterruptedException{
        for (ControladorParticulasThread controladorPT : hilos)
            controladorPT.actualizar();
        
        boolean finalizado = false; // si ya se actualizó todo
        while(!finalizado){
            finalizado = true;
            for (ControladorParticulasThread h : hilos) {
                if(!h.isFinalizado())
                    finalizado = false;
            }
            
        }
        
        // Recorre el array casillas y remueve las vacias o no activas
        Casilla casilla;
        for (int i = 0; i < ControladorParticulas.casArr_tam; i++) {
            casilla = ControladorParticulas.casillasArr[i];
            if(casilla == null)
                break;
            
            if(casilla.isNull())
                quitarCasillaArray(i);
        }
    }
    
    public void pintar(){
        for (int i = 0; i < ControladorParticulas.posFinArr; i++) {
            Casilla casilla = ControladorParticulas.casillasArr[i];
            
            casilla.pintar();
        }
    }
    
    private void rotarGravedadY(double velocidad, double max){
        int signo = (int) (Math.abs(ControladorParticulas.gravedadY) / ControladorParticulas.gravedadY);
        double incremento = (velocidad / ((double)ControladorFrames.getFrames()));

        if((Math.abs(max) / max) != signo){
            ControladorParticulas.gravedadY -= (double)signo * incremento;
            if(ControladorParticulas.gravedadY == 0)
                ControladorParticulas.gravedadY = -((double)signo) * incremento;
        }
        else{
            ControladorParticulas.gravedadY += (double)signo * incremento;
            if(ControladorParticulas.gravedadY > max)
                ControladorParticulas.gravedadY = max;
        }
    }
    private void rotarGravedadX(double velocidad, double max){
        int signo = (int) (Math.abs(ControladorParticulas.gravedadX) / ControladorParticulas.gravedadX);
        double incremento = (velocidad / ((double)ControladorFrames.getFrames()));

        if((Math.abs(max) / max) != signo){
            ControladorParticulas.gravedadX -= (double)signo * incremento;
            if(ControladorParticulas.gravedadX == 0)
                ControladorParticulas.gravedadX = -((double)signo) * incremento;
        }
        else{
            ControladorParticulas.gravedadX += (double)signo * incremento;
            if(ControladorParticulas.gravedadX > max)
                ControladorParticulas.gravedadX = max;
        }
    }
    public void rotarGravedad(){
        double velocidad = 0.25;
        double max = 1;
        
        if(ControladorParticulas.gravedadX == 0)
            ControladorParticulas.gravedadX = max;
        if(ControladorParticulas.gravedadY == 0)
            ControladorParticulas.gravedadY = max;
        
        if(ControladorParticulas.gravedadX == max && ControladorParticulas.gravedadY != max)
            rotarGravedadY(velocidad, max);
        else if(ControladorParticulas.gravedadX != -max && ControladorParticulas.gravedadY == max)
            rotarGravedadX(velocidad, -max);
        else if(ControladorParticulas.gravedadX == -max && ControladorParticulas.gravedadY != -max)
            rotarGravedadY(velocidad, -max);
        else if(ControladorParticulas.gravedadX != max && ControladorParticulas.gravedadY == -max)
            rotarGravedadX(velocidad, max);
        
        int cantFilas = ControladorParticulas.matrizCasillas.size();
        int cantColumnas = ControladorParticulas.matrizCasillas.get(0).size();
        for (int i = 0; i < cantFilas; i++) {
            for (int j = 0; j < cantColumnas; j++) {
                Casilla casilla = ControladorParticulas.matrizCasillas.get(i).get(j);
                
                casilla.activarCasilla();
            }
        }
    }
    
    private void quitarCasillaArray(int pos){
        ControladorParticulas.casillasArr[pos].setEnArrayCasillas(false);
        ControladorParticulas.casillasArr[pos] = ControladorParticulas.casillasArr[ControladorParticulas.posFinArr - 1];
        ControladorParticulas.casillasArr[ControladorParticulas.posFinArr - 1] = null;
        ControladorParticulas.posFinArr--;
    }
    
    public void generarParticula(int x, int y){
        if(x >= 0 && y >= 0){
            Casilla casilla = getCasillaRango(x, y);
            
            if(casilla != null){
                x -= casilla.getXIni();
                y -= casilla.getYIni();

                casilla.generarParticula(x, y);
            }
        }
    }
    
    public synchronized static boolean particulaIsNull(int x, int y){
        if(x >= 0 && y >= 0){
            Casilla casilla = getCasillaRango(x, y);
            
            if(casilla == null)
                return false;
            
            x -= casilla.getXIni();
            y -= casilla.getYIni();
            
            return casilla.getParticula(x, y) == null;
        }
        
        return false;
    }
    
    public synchronized static void agregarCasillaArray(Casilla casilla){
        casilla.setEnArrayCasillas(true);
        ControladorParticulas.casillasArr[ControladorParticulas.posFinArr] = casilla;
        ControladorParticulas.posFinArr++;
    }
    
    
    /**
     * Busca la casilla que contenga en su rango la coordenada indicada.
     * La coordenada corresponde a la de una particula en 
     * en el plano carteciano de la pantalla.
     * 
     * @param x
     * @param y
     * @return 
     */
    public static Casilla getCasillaRango(int x, int y){
        int cantFilas = ControladorParticulas.matrizCasillas.size();
        int cantColum = ControladorParticulas.matrizCasillas.get(0).size();
        
        Casilla casillaIni = ControladorParticulas.matrizCasillas.get(0).get(0);
        Casilla casillaFin = ControladorParticulas.matrizCasillas.get(cantFilas-1).get(cantColum-1);
        
        int incremento = Casilla.SIZE * Particula.getSize();
        int posX, posY;
        if(x < casillaIni.getXIni() || x >= casillaFin.getXFin())
            return null;
        else
            posX = ((x - casillaIni.getXIni()) / incremento) + casillaIni.getPosX();
        
        if(y < casillaIni.getYIni() || y >= casillaFin.getYFin())
            return null;
        else
            posY = ((y - casillaIni.getYIni()) / incremento) +  casillaIni.getPosY();
        
        Casilla casilla = ControladorParticulas.matrizCasillas.get(posY).get(posX);
        
        return casilla;
    }
    
    /**
     * Busca y retorna la casilla donde se encuentra la particula
     * indicada por X y Y matrizCasilla relativa a la casilla indicada.
     * @param x
     * @param y
     * @param casilla
     * @return 
     */
    public static Casilla getCasillaRelativa(int x, int y, Casilla casilla){
        int posX = casilla.getPosX();
        int posY = casilla.getPosY();
        
        double incX = (double)x / (double)Casilla.SIZE;
        double incY = (double)y / (double)Casilla.SIZE;
        
        if(incX < 0)
            incX--;
        if(incY < 0)
            incY--;
        
        if(ControladorParticulas.matrizCasillas.containsKey(posY + (int)incY) 
        && ControladorParticulas.matrizCasillas.get(posY + (int)incY).containsKey(posX + (int)incX)){
            return ControladorParticulas.matrizCasillas.get(posY + (int)incY).get(posX + (int)incX);
        }
        
        return null;
    }

    public static double getGravedadX() {
        return gravedadX;
    }

    public static double getGravedadY() {
        return gravedadY;
    }
}