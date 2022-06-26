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
    
    public static final GenerarRandom random = GenerarRandom.nuevaEntidad();
    
    private ArrayList<ControladorParticulasThread> hilos;
    
    private static double gravedadX,gravedadY;
    
    public ControladorParticulas(int alto, int ancho, int cantHilos) {
        ControladorParticulas.gravedadX = 0;
        ControladorParticulas.gravedadY = 1;
        
        generarMatrizCasillas(alto, ancho);
        
        ControladorParticulas.posFinArr = 0;
        ControladorParticulas.casArr_tam = (int) ((matrizCasillas.size() * matrizCasillas.get(0).size())*1.5);
        ControladorParticulas.casillasArr = new Casilla[ControladorParticulas.casArr_tam];
        
        ControladorParticulas.random.addLista(cantHilos - 1);
        ControladorParticulas.random.setMax(1000);
        
        generarHilos(cantHilos);
    }
    
    private void generarMatrizCasillas(int alto, int ancho){
        ControladorParticulas.matrizCasillas = new HashMap<>();
        HashMap<Integer, Casilla> fila;
        
        Casilla casilla;
        int incremento = Casilla.size * Particula.getSize();
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
            controladorPT = new ControladorParticulasThread(i, cantHilos, this.casillasArr, this.casArr_tam);
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
        
        Casilla casilla;
        for (int i = 0; i < ControladorParticulas.casArr_tam; i++) {
            casilla = ControladorParticulas.casillasArr[i];
            if(casilla == null)
                break;
            
            if(casilla.isNull() || !casilla.isActiva())
                quitarCasillaArray(i);
        }
    }
    
    public void pintar(){
        for (int i = 0; i < ControladorParticulas.posFinArr; i++) {
            Casilla casilla = ControladorParticulas.casillasArr[i];
            
            casilla.pintar();
            //casilla.quitarBorde();
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
    
    /**
     * Marca como activa y agrega a casillasArray la casilla
     * indicada y sus cercanas
     * @param casilla 
     */
    public static void activar(Casilla casilla){
        casilla.setActiva(true);
        
        int x;
        int y = casilla.getPosY() - 1;
        
        for (int i = 0; i < 3; i++) {
            x = casilla.getPosX() - 1;
            for (int j = 0; j < 3; j++) {
                if(ControladorParticulas.matrizCasillas.containsKey(y) && ControladorParticulas.matrizCasillas.get(y).containsKey(x)){
                    Casilla casillaCercana = ControladorParticulas.matrizCasillas.get(y).get(x);
                    if(!casillaCercana.isEnArrayCasillas()){
                        casillaCercana.setActiva(true);
                        agregarCasillaArray(casillaCercana);
                    }
                }
                x++;
            }
            y++;
        }
        
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
        
        int incremento = Casilla.size * Particula.getSize();
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
    
    public static Casilla getCasillaRelativa(int x, int y, Casilla casilla){
        int posX = casilla.getPosX();
        int posY = casilla.getPosY();
        
        double incX = (double)x / (double)Casilla.size;
        double incY = (double)y / (double)Casilla.size;
        
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
