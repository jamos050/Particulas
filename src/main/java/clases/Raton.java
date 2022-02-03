/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.awt.Color;
import particulas.Elemento;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import particulas.Particula;

/**
 *
 * @author Josue Alvarez M
 */
public class Raton implements MouseListener, Runnable, MouseWheelListener{
    private Pantalla pantalla; // pantalla en la que está el mouse
    
    private int x_ant = 0;
    private int y_ant = 0;
    private int x, y;
    
    private boolean presionando = false;
    private int tipo;
    private int radio = 5;
    
    public Raton(Pantalla pantalla) {
        this.pantalla = pantalla;
    }
    
    private void actualizarPosicion(){
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        x = (int) b.getX();
        y = (int) b.getY();
    }
    
    public Pixel getPixel(){
        Pixel p = null;
        while(p == null){
            actualizarPosicion();
            try{
                p = pantalla.getPixeles().get((this.y / pantalla.getPixelTam())-23/pantalla.getPixelTam()).get((this.x / pantalla.getPixelTam()));
            }catch(Exception e){}
        }
        return p;
    }
    
    private void generarParticula(Pixel pixel){
        ArrayList<Pixel> pixeles;
        
        switch (this.tipo) {
            case 5:
                pixeles = this.pantalla.getPixelsCirculoRectas(pixel.getFila(), pixel.getColumna(), this.radio, true);
                break;
            default:
                pixeles = this.pantalla.getPixelsCirculo(pixel.getFila(), pixel.getColumna(), this.radio);
                break;
        }
        
        for (Pixel p2 : pixeles) {
            switch (this.tipo) {
                case 1:
                    Pantalla.fabricaP.generarParticula(p2, Elemento.PIEDRA, true);
                    break;
                case 2:
                    Pantalla.fabricaP.generarParticula(p2, Elemento.ARENA, true);
                    break;
                case 3:
                    Pantalla.fabricaP.generarParticula(p2, Elemento.AGUA, true);
                    break;
                case 4:
                    Pantalla.fabricaP.generarParticula(p2, Elemento.HIERRO, true);
                    break;
                case 5:
                    Pantalla.fabricaP.generarParticula(p2, Particula.PARTICULA_EXPLOCION, false);
                    break;
                default:
                    break;
            }
        }
    }
    
    //get
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadio() {
        return radio;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.tipo = e.getButton();
        this.presionando = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.presionando = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        this.radio += e.getWheelRotation();
        if(this.radio < 1)
            this.radio = 1;
    }
    
    @Override
    public void run() {
        Pixel p = getPixel();
        ArrayList<Pixel> pixelesRecta;
        
        int r;
        while(true){
            actualizarPosicion();
            
            if(presionando){
                p = getPixel();
                
                if((this.x_ant != this.y_ant || this.x_ant != 0) && (this.x_ant != this.x || this.y_ant != this.y))
                    pixelesRecta = this.pantalla.getPixelesRecta(this.x_ant, this.y_ant, p.getColumna(), p.getFila());
                else{
                    pixelesRecta = new ArrayList<>();
                    pixelesRecta.add(p);
                }
                
                for (Pixel pixel : pixelesRecta) {
                    generarParticula(pixel);
                }
                this.x_ant = p.getColumna();
                this.y_ant = p.getFila();
            }
            else{
                this.x_ant = 0;
                this.y_ant = 0;
            }
            
            try{
                Thread.sleep(20);
            }catch(Throwable e){}
        }
    }
}
