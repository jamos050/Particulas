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
public class Pantalla extends JFrame implements Runnable{
    public static final ArrayList<ArrayList<Pixel>> pixeles = new ArrayList<>();
    
    private BufferedImage bufferedImage;
    private BufferedImage bufferedImageGUI;
    public static Graphics2D g2d;
    public static Graphics2D g2dGUI;
    
    private JLabel imagen;
    private JLabel imagenGUI;
    
    public static int alto;
    public static int ancho;
    
    private int pixelTam;
    
    private int ventana_ancho;
    private int ventana_alto;
    
    private Raton raton;
    
    private ControladorParticulas controladorP;
    public static final FabricaParticulas fabricaP = new FabricaParticulas();
    
    private boolean iniciar = false;
    
    private final ControladorLuz controladorL;
    
    public static final GenerarRandom random = GenerarRandom.nuevaEntidad(1, 1000);
    
    public Pantalla(){
        iniciarVentana();
        
        this.raton = new Raton(this);
        addMouseListener(this.raton);
        addMouseWheelListener(this.raton);
        
        this.pixelTam = 4;
        Pantalla.alto = this.ventana_alto / this.pixelTam;
        Pantalla.ancho = this.ventana_ancho / this.pixelTam;
        
        this.controladorP = new ControladorParticulas();
        this.controladorL = new ControladorLuz();
        
        iniciarPantalla();
        iniciarImagen();
        
        Thread t1 = new Thread(this.controladorP);
        t1.start();
        Thread t2 = new Thread(this.controladorL);
        t2.start();
        Thread t3 = new Thread(this.raton);
        t3.start();
        
        this.iniciar = true;
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
        g2d.setBackground(Pixel.getColorPred());
        g2d.setColor(g2d.getBackground());
        g2d.fillRect(0, 0, this.ventana_ancho, this.ventana_alto);
        
        Pantalla.g2dGUI = bufferedImageGUI.createGraphics();
        
        Pantalla.g2dGUI.setBackground(new Color(0f, 0f, 0f, 0f));
        Pantalla.g2dGUI.setColor(new Color(0f,0f,0f,0f));
        Pantalla.g2dGUI.fillRect(0, 0, this.ventana_ancho, this.ventana_alto);
        
        int x;
        int y = 0;
        ArrayList<Pixel> fila;
        for (int i = 0; i < Pantalla.alto; i++) {
            x = 0;
            fila = new ArrayList<>();
            for (int j = 0; j < Pantalla.ancho; j++) {
                Pixel p = new Pixel(x, y, this.pixelTam, i, j);
                
                fila.add(p);
                x += this.pixelTam;
            }
            Pantalla.pixeles.add(fila);
            y += this.pixelTam;
        }
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
    
    private void generarFrame() throws IOException{
        this.imagen.setIcon(new ImageIcon(this.bufferedImage));
        this.imagenGUI.setIcon(new ImageIcon(this.bufferedImageGUI));
    }
    
    public void limpiarGUI(){
        Pantalla.g2dGUI.clearRect(0, 0, this.ventana_ancho, this.ventana_alto);
        Pantalla.g2dGUI.setColor(Pantalla.g2dGUI.getBackground());
        Pantalla.g2dGUI.fillRect(0, 0, this.ventana_ancho, this.ventana_alto);
    }
    
    //get
    private void getPixelesCercanos_aux(int fila, int columna, int r, ArrayList<Pixel> pixelesR, boolean esquinas){
        if(!(fila < 0 || columna < 0 || fila >= Pantalla.alto || columna >= Pantalla.ancho || r == 0)){
            Pixel p = Pantalla.pixeles.get(fila).get(columna);
            if(p.bandera < r){
                p.bandera = r;
                
                if(!pixelesR.contains(p))
                    pixelesR.add(p);
                
                r--;
                getPixelesCercanos_aux(fila - 1, columna, r, pixelesR, esquinas);
                getPixelesCercanos_aux(fila + 1, columna, r, pixelesR, esquinas);
                getPixelesCercanos_aux(fila, columna - 1, r, pixelesR, esquinas);
                getPixelesCercanos_aux(fila, columna + 1, r, pixelesR, esquinas);
                
                if(esquinas){
                    getPixelesCercanos_aux(fila - 1, columna - 1, r, pixelesR, esquinas);
                    getPixelesCercanos_aux(fila - 1, columna + 1, r, pixelesR, esquinas);
                    getPixelesCercanos_aux(fila + 1, columna - 1, r, pixelesR, esquinas);
                    getPixelesCercanos_aux(fila + 1, columna + 1, r, pixelesR, esquinas);
                }
            }
        }
    }
    
    public ArrayList<Pixel> getPixelesCercanos(int fila, int columna, int r, boolean esquinas, boolean quitarBanderas){
        if(r >= 1){
            ArrayList<Pixel> pixelesR = new ArrayList<>();
            getPixelesCercanos_aux(fila, columna, r, pixelesR, esquinas);
            
            if(quitarBanderas)
                for (Pixel p : pixelesR)
                    p.bandera = 0;
            
            return pixelesR;
        }
        return null;
    }
    
    
    private double distancia(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    public ArrayList<Pixel> getPixelsCirculo(int fila, int columna, int r){
        ArrayList<Pixel> pixelesCirculo = new ArrayList<>();
        
        int x1 = columna - r;
        if(x1 < 0)
            x1 = 0;
        
        int x2;
        int y2 = fila - r;
        if(y2 < 0)
            y2 = 0;
        
        Pixel p;
        for (int i = 0; i < r*2; i++) {
            x2 = x1;
            for (int j = 0; j < r*2; j++) {
                p = Pantalla.pixeles.get(y2).get(x2);
                if(distancia(columna, fila, p.getColumna(), p.getFila()) <= r)
                    pixelesCirculo.add(p);
                
                x2++;
                if(x2 >= Pantalla.ancho)
                    break;
            }
            y2++;
            if(y2 >= Pantalla.alto)
                break;
        }
        
        return pixelesCirculo;
    }
    
    
    public ArrayList<Pixel> getPixelsCirculoRectas(int fila, int columna, int r, boolean explocion){
        ArrayList<Pixel> pixelesCirculo = new ArrayList<>();
        ArrayList<Pixel> pixelesRecta;
        
        int x1 = columna - r;
        int y1 = fila - r;
        if(x1 < 0)
            x1 = 0;
        if(y1 < 0)
            y1 = 0;
        
        int x2 = columna + r;
        int y2 = fila + r;
        if(x2 >= Pantalla.ancho)
            x2 = Pantalla.ancho - 1;
        if(y2 >= Pantalla.alto)
            y2 = Pantalla.alto - 1;
        
        int x = x1;
        int y = y1;
        
        int cont = 0;
        
        Pixel p1;
        Pixel p2;
        while(cont < 4){
            pixelesRecta = getPixelesRecta(columna, fila, x, y);
            
            p1 = pixelesRecta.get(0);
            p2 = pixelesRecta.get(pixelesRecta.size()-1);
            if(distancia(columna, fila, p1.getColumna(), p1.getFila()) < distancia(columna, fila, p2.getColumna(), p2.getFila()))
                for (Pixel p : pixelesRecta) {
                    if(p.getParticula() != null && (explocion && !p.getParticula().isParticulaExp() || !explocion))
                        break;
                    if(distancia(columna, fila, p.getColumna(), p.getFila()) <= r)
                        pixelesCirculo.add(p);
                }
            else
                for (int i = pixelesRecta.size()-1; i >= 0; i--) {
                    Pixel p = pixelesRecta.get(i);
                    if(p.getParticula() != null && (explocion && !p.getParticula().isParticulaExp() || !explocion))
                        break;
                    if(distancia(columna, fila, p.getColumna(), p.getFila()) <= r)
                        pixelesCirculo.add(p);
                }
                
            switch (cont) {
                case 0:
                    x++;
                    if(x == x2)
                        cont++;
                    break;
                case 1:
                    y++;
                    if(y == y2)
                        cont++;
                    break;
                case 2:
                    x--;
                    if(x == x1)
                        cont++;
                    break;
                case 3:
                    y--;
                    if(y == y1)
                        cont++;
                    break;
                default:
                    break;
            }
        }
        
        
        return pixelesCirculo;
    }
    
    
    /*
        Trasa una recta y retorna los pixeles que le conforman
    */
    public ArrayList<Pixel> getPixelesRecta(int x1, int y1, int x2, int y2){
        // y = mx + b
        ArrayList<Pixel> pixelesRecta = new ArrayList<>();
        
        int x3, y3;
        if(x1 == x2){
            if(y1 > y2){
                y3 = y2;
                y2 = y1;
                y1 = y3;
            }
            
            for (int i = y1; i <= y2; i++)
                pixelesRecta.add(Pantalla.pixeles.get(i).get(x1));
            
            return pixelesRecta;
        }
        
        double m = (((double)y2) - ((double)y1)) / (((double)x2) - ((double)x1));
        double b = (((double)-m)*((double)x1)) + ((double)y1);
        
        if(Math.abs(x1 - x2) > Math.abs(y1 - y2)){
            if(x1 > x2){
                x3 = x2;
                x2 = x1;
                x1 = x3;
            }
            int y;
            
            for (int i = x1; i <= x2; i++){
                y = Math.abs((int) ((m*((double)i)) + b));
                pixelesRecta.add(Pantalla.pixeles.get(y).get(i));
            }
        }
        else{
            if(y1 > y2){
                y3 = y2;
                y2 = y1;
                y1 = y3;
            }
            int x;
            for (int i = y1; i <= y2; i++){
                x = Math.abs((int) ((((double)i) - b) / m));
                pixelesRecta.add(Pantalla.pixeles.get(i).get(x));
            }
        }
        
        return pixelesRecta;
    }
    
    
    private void getParticulasRadio_aux(int fila, int columna, int r, ArrayList<Particula> particulasR, boolean esquinas){
        if(!(fila < 0 || columna < 0 || fila >= Pantalla.alto || columna >= Pantalla.ancho || r == 0)){
            Particula p = Pantalla.pixeles.get(fila).get(columna).getParticula();
            if(p != null)
                if(p.getPixel().bandera < r){
                    p.getPixel().bandera = r;

                    if(!particulasR.contains(p))
                        particulasR.add(p);

                    r--;
                    getParticulasRadio_aux(fila - 1, columna, r, particulasR, esquinas);
                    getParticulasRadio_aux(fila + 1, columna, r, particulasR, esquinas);
                    getParticulasRadio_aux(fila, columna - 1, r, particulasR, esquinas);
                    getParticulasRadio_aux(fila, columna + 1, r, particulasR, esquinas);

                    if(esquinas){
                        getParticulasRadio_aux(fila - 1, columna - 1, r, particulasR, esquinas);
                        getParticulasRadio_aux(fila - 1, columna + 1, r, particulasR, esquinas);
                        getParticulasRadio_aux(fila + 1, columna - 1, r, particulasR, esquinas);
                        getParticulasRadio_aux(fila + 1, columna + 1, r, particulasR, esquinas);
                    }
                }
        }
    }
    
    public ArrayList<Particula> getParticulasRadio(int fila, int columna, int r, boolean esquinas, boolean quitarBanderas){
        if(r >= 1){
            ArrayList<Particula> particulasR = new ArrayList<>();
            getParticulasRadio_aux(fila, columna, r, particulasR, esquinas);
            
            if(quitarBanderas)
                for (Particula p : particulasR)
                    p.getPixel().bandera = 0;
            
            return particulasR;
        }
        return null;
    }
    
    
    
    public ArrayList<ArrayList<Pixel>> getPixeles() {
        return pixeles;
    }

    public int getPixelTam() {
        return pixelTam;
    }

    public ControladorParticulas getControladorP() {
        return controladorP;
    }

    public int getAlto() {
        return alto;
    }

    public int getAncho() {
        return ancho;
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
            
            int r = this.raton.getRadio() * 2 * this.pixelTam;
            Pantalla.g2dGUI.setColor(Color.BLACK);
            Pantalla.g2dGUI.drawOval(this.raton.getX() - r/2, this.raton.getY() - 23 - r/2, r, r);
            
        }catch(Exception e){
            
        }
    } 

    @Override
    public void run() {
        while(true){
            if(iniciar){
                repaint();
                
                /*
                for (ArrayList<Pixel> fila : pixeles) {
                    for (Pixel p : fila){
                        p.actualizarColorPantalla();
                    }
                }*/
                
                try{
                    generarFrame();
                }catch(Throwable e){
                    System.out.println(e);
                }
            }
        }
    }
    
}
