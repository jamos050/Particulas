/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Josue Alvarez M
 */
public class Juego{
    private static Pantalla pantalla;
    private static ControladorParticulas controladorP;
    private ControladorFrames controladorF;
    
    public static final GenerarRandom random = GenerarRandom.nuevaEntidad();
    
    public Juego() throws IOException {
        Juego.pantalla = new Pantalla();
        
        int cantHilos = 1;
        Juego.controladorP = new ControladorParticulas(Juego.pantalla.getVentana_alto(), Juego.pantalla.getVentana_ancho(), cantHilos);
        Juego.random.addLista(cantHilos - 1);
        Juego.random.setMax(1000);
        
        this.controladorF = ControladorFrames.getTiempo(144);
    }

    private static void raton(){
        ArrayList<int[]> posiciones = Juego.pantalla.raton();
        for (int[] pos : posiciones) {
            Juego.controladorP.generarParticula(pos[0], pos[1]);
        }
    }
    
    public static void actualizarFrame() throws InterruptedException, IOException{
        Juego.controladorP.actualizar();
        Juego.pantalla.pintar(Juego.controladorP);

        raton();
    }
}
