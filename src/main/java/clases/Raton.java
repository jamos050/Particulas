/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import particulas.Particula;

/**
 *
 * @author Josue Alvarez M
 */
public class Raton implements MouseListener, MouseWheelListener{
    
    private int x, y;
    
    private boolean presionando = false;
    private int tipo;
    
    
    public void actualizarPosicion(){
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        
        x = (int) b.getX();
        y = (int) (b.getY() - 23);
    }
    
    // is
    public boolean isPresionando() {
        return presionando;
    }
    
    // get
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
        
    }
}
