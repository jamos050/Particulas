/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;


/**
 *
 * @author Josue Alvarez M
 */
public class ControladorFrames implements Runnable{
    private static ControladorFrames entidad;
    
    public static final int NS_POR_SEGUNDO = 1000000000;
    
    private static int frames;
    double promedio; // promedio de nanosegundos para generar cada frame
    
    int fps;
    
    private ControladorFrames(int frames){
        ControladorFrames.frames = frames;
        
        this.promedio = ControladorFrames.NS_POR_SEGUNDO / frames;
    }
    
    public static ControladorFrames newEntity(int frames){
        if(ControladorFrames.entidad == null){
            ControladorFrames.entidad = new ControladorFrames(frames);
            Thread t = new Thread(ControladorFrames.entidad);
            t.start();
        }
        
        return ControladorFrames.entidad;
    }

    public static int getFrames() {
        return frames;
    }
    
    @Override
    public void run() {
        int fpsAct = 0; // FPS actuales
        
        double espera = 0;
        
        long inicioSegundo = System.nanoTime(); // valor de referencia para el conteo de cada segundo (fps)
        long inicioProceso;
        long inicioEspera = 0;
        
        long finProceso;
        
        long dif;
        try{
            while(true){
                if(espera > 0 && System.nanoTime() - inicioEspera >= espera)
                    espera = 0;
                else if(espera > 0)
                    continue;
                
                
                if(System.nanoTime() - inicioSegundo >= ControladorFrames.NS_POR_SEGUNDO){
                    inicioSegundo = System.nanoTime();
                    this.fps = fpsAct;
                    fpsAct = 0;
                    //System.out.println(this.fps);
                }
                
                inicioProceso = System.nanoTime();
                Juego.actualizarFrame();
                finProceso = System.nanoTime();
                
                fpsAct++;
                
                dif = finProceso - inicioProceso;
                // dif < this.promedio  (le sobró tiempo)
                // dif > this.promedio  (hay retraso)
                espera += this.promedio - dif;
                
                if(espera > 0)
                    inicioEspera = System.nanoTime(); // marca el inicio de la espera
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
}