/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clases;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import particulas.Particula;

/**
 *
 * @author Josue Alvarez M
 */
public class Pantalla extends JFrame{
    private BufferedImage bufferedImage;
    private BufferedImage bufferedImageGUI;
    private static Graphics2D g2d;
    private static Graphics2D g2dGUI;
    
    private JLabel imagen;
    private JLabel imagenGUI;
    
    private int ventana_ancho;
    private int ventana_alto;
    
    private final Raton raton;
    public Pantalla() throws IOException{
        iniciarVentana();
        
        this.raton = new Raton();
        addMouseListener(this.raton);
        addMouseWheelListener(this.raton);
        
        iniciarPantalla();
        iniciarImagen();
        
        definirImagenBuffer();
    }
    
    private void iniciarVentana(){
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.ventana_alto = (int) screenSize.getHeight() - 24;
        this.ventana_ancho = (int) screenSize.getWidth();
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void iniciarPantalla(){
        this.bufferedImage = new BufferedImage(this.ventana_ancho, this.ventana_alto, BufferedImage.TYPE_INT_ARGB);
        this.bufferedImageGUI = new BufferedImage(this.ventana_ancho, this.ventana_alto, BufferedImage.TYPE_INT_ARGB);
        
        g2d = bufferedImage.createGraphics();
        g2d.setBackground(Color.WHITE);
        g2d.setColor(g2d.getBackground());
        g2d.fillRect(0, 0, this.ventana_ancho, this.ventana_alto);
        
        Pantalla.g2dGUI = bufferedImageGUI.createGraphics();
        
        Pantalla.g2dGUI.setBackground(new Color(0f, 0f, 0f, 0f));
        Pantalla.g2dGUI.setColor(new Color(0f,0f,0f,0f));
        Pantalla.g2dGUI.fillRect(0, 0, this.ventana_ancho, this.ventana_alto);
    }
    private void iniciarImagen(){
        this.imagen = new JLabel();
        this.imagen.setSize(this.ventana_ancho, this.ventana_alto);
        this.imagen.setVisible(true);
        
        this.imagenGUI = new JLabel();
        this.imagenGUI.setSize(this.ventana_ancho, this.ventana_alto);
        this.imagenGUI.setVisible(true);
        
        this.imagen.add(this.imagenGUI);
        
        add(this.imagen);
    }
    
    private void definirImagenBuffer() throws IOException{
        this.imagen.setIcon(new ImageIcon(this.bufferedImage));
        this.imagenGUI.setIcon(new ImageIcon(this.bufferedImageGUI));
    }
    
    
    public ArrayList<int[]> raton(){
        ArrayList<int[]> posiciones = new ArrayList<>();
        
        this.raton.actualizarPosicion();
        
        int rango = 25 * Particula.getSize();
        int xMax = this.raton.getX() + rango;
        int yMax = this.raton.getY() + rango;
        
        for (int i = this.raton.getY() - rango; i <= yMax; i++) {
            for (int j = this.raton.getX() - rango; j <= xMax; j++) {
                if(this.raton.isPresionando()){
                    posiciones.add(new int[]{j, i});
                }
            }
        }
        
        return posiciones;
    }
    
    public void limpiarGUI(){
        Pantalla.g2dGUI.clearRect(0, 0, this.ventana_ancho, this.ventana_alto);
        Pantalla.g2dGUI.setColor(Pantalla.g2dGUI.getBackground());
        Pantalla.g2dGUI.fillRect(0, 0, this.ventana_ancho, this.ventana_alto);
    }

    public int getVentana_ancho() {
        return ventana_ancho;
    }

    public int getVentana_alto() {
        return ventana_alto;
    }

    @Override
    public void paint(Graphics g) {
        try{
            super.paint(g);
            
            limpiarGUI();
            
            //int r = this.raton.getRadio() * 2 * this.pixelTam;
            //Pantalla.g2dGUI.setColor(Color.BLACK);
            //Pantalla.g2dGUI.drawOval(this.raton.getX() - r/2, this.raton.getY() - 23 - r/2, r, r);
            
        }catch(Exception e){
            System.out.println(e);
        }
    } 

    public void pintar(ControladorParticulas controladorP) throws IOException, InterruptedException {
        repaint();
        controladorP.pintar();
    }

    public static Graphics2D getG2d() {
        return g2d;
    }
    
}
