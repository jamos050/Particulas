/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package particulas;

import clases.Pantalla;
import clases.Pixel;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Josue Alvarez M
 */
public abstract class Particula {
    protected boolean actualizar = true;
    
    protected int cantidad = 1;
    
    protected Pixel pixel;
    protected double velociadMax = 5;
    protected double velociadX = 5;
    protected double velociadY = 5;
    protected double vida = 100;
    
    protected boolean cayendo = false;
    protected boolean inercia = true; // si está en true el proceso de inercia se activará
    protected boolean activar_inercia = false; // indica si se debe de ejecutar la inercia
    
    protected double friccion = 1; // cantidad a reducir la velocidadX con cada movimiento
    protected double dencidad = 0; // las desplazan solo a las de menor dencidad
    protected int resistencia_inercia = 100; // probabilidad de que no se ejecute inercia
    protected int resistencia_fuego = 75; // porcentaje que disminuye el daño por combustión
    
    protected boolean fuenteTemp = false; // si la partícula proboca un cambio de temperatura
    protected boolean combustion = false; // indica si la particula estará en el efecto de combustión al generarce
    protected double temperatura = 0; // temperatura en la que se encuentra (0 ambiente)
    protected double temperaturaMin = 1000; // temperatura minima para la combustión
    
    protected double conductividad = 15; // porcentaje de temperatura que  recibe
    
    protected int generar = -1; // particula a generar (-1 no generará nada)
    protected int generar_prob = 0; // probabilidad de que se genera la partícula
    
    protected int rojo = 0;
    protected int verde = 0;
    protected int azul = 0;
    protected Color color = Color.BLACK;
    
    protected int cambiarColor = 0; // Porcentaje de intercambio de color
    
    protected int tipo;
    public static final int ELEMENTO = 0;
    public static final int PARTICULA_EXPLOCION = 1;
    
    protected boolean particulaExp = true; // si una particulaExplacion puede tomar esta particula
    
    public void intercambiarColor(Particula particula2){
        Color c = this.color;
        setColor(particula2.getColor());
        
        particula2.setColor(c);
        
        this.pixel.setActualizar(false);
        particula2.getPixel().setActualizar(false);
    }
    
    protected void moverParticula(Pixel p){
        if(this.pixel != null)
            this.pixel.eliminarParticula();
        
        p.setParticula(this);
        
        setPixel(p);
        this.pixel.setActualizar(false);
    }
    
    private void desplazar(Particula particula){
        int fila = this.pixel.getFila();
        int columna = this.pixel.getColumna();
        
        boolean continuar = true;
        
        Pixel pixel;
        if(columna - 1 > 0){
            pixel = Pantalla.pixeles.get(fila).get(columna - 1);
            if(pixel.getParticula() == null){
                particula.moverParticula(pixel);
                continuar = false;
            }
        }
        if(continuar && columna + 1 < Pantalla.ancho){
            pixel = Pantalla.pixeles.get(fila).get(columna + 1);
            if(pixel.getParticula() == null){
                particula.moverParticula(pixel);
                continuar = false;
            }
        }
        if(continuar){
            Pixel p = particula.getPixel();
            p.eliminarParticula();
            particula.setPixel(null);
            
            this.pixel.eliminarParticula();
            
            particula.moverParticula(this.pixel);
            
            this.pixel = null;
            moverParticula(p);
        }
    }
    
    protected boolean desplazarValidar(Particula particula){
        if(particula.getDencidad() < this.dencidad && !particula.isParticulaExplocion()){
            desplazar(particula);
            
            if(this.velociadY > 0){
                this.velociadY -= particula.getFriccion();
                if(this.velociadY < 0)
                    this.velociadY = 0;
            }
            else if(this.velociadY < 0){
                this.velociadY += particula.getFriccion();
                if(this.velociadY > 0)
                    this.velociadY = 0;
            }
            
            return true;
        }
        return false;
    }
    
    protected void inercia(){
        Random rand = new Random();
        int fila = this.pixel.getFila();
        int columna = this.pixel.getColumna();

        Pixel p = null;
        Pixel p2 = null;

        if(columna - 1 >= 0)
            p = Pantalla.pixeles.get(fila).get(columna - 1);
        if(columna + 1 < Pantalla.ancho)
            p2 = Pantalla.pixeles.get(fila).get(columna + 1);
        
        try{
            if(p != null && p.getParticula() != null)
                if(rand.nextInt(100) > p.getParticula().getResistenciaInercia()){
                    p.getParticula().setActivar_inercia(true);
                    if(p.getParticula().getVelociadX() == 0)
                        p.getParticula().setVelociadX(1);
                }
            if(p2 != null && p2.getParticula() != null)
                if(rand.nextInt(100) > p2.getParticula().getResistenciaInercia()){
                    p2.getParticula().setActivar_inercia(true);
                    if(p2.getParticula().getVelociadX() == 0)
                        p2.getParticula().setVelociadX(1);
                }
        }catch(Exception e){}
    }
    
    protected abstract void eliminarParticulaCombustion();
    
    private void actualizarTemperatura(Particula particula){
        if(particula != null && particula.getConductividad() > 0 && !particula.fuenteTemp){
            double temp = particula.getTemperatura();
            temp += this.temperatura * (particula.getConductividad() / 100);
            
            if(!this.fuenteTemp){
                if(this.temperatura > 0 && temp < this.temperatura)
                    particula.setTemperatura(temp);
                if(this.temperatura < 0 && temp > this.temperatura)
                    particula.setTemperatura(temp);
            }
            else
                particula.setTemperatura(temp);
        }
    }
    
    protected void combustion(){
        int fila = this.pixel.getFila();
        int columna = this.pixel.getColumna();
        
        Pixel p;
        
        Random rand = new Random();
        
        if(!this.fuenteTemp){
            double tempRest = Math.abs(this.temperatura) * 0.0025;
            if(this.temperatura >= 0f){
                if(this.temperatura >= tempRest)
                    this.temperatura -= tempRest;
                else
                    this.temperatura = 0;
            }
            else{
                if(this.temperatura <= -tempRest)
                    this.temperatura += tempRest;
                else
                    this.temperatura = 0;
            }
        }
        
        if(this.temperatura != 0){
            if(fila - 1 >= 0){
                p = Pantalla.pixeles.get(fila - 1).get(columna);
                actualizarTemperatura(p.getParticula());
            }
            if(fila + 1 < Pantalla.alto){
                p = Pantalla.pixeles.get(fila + 1).get(columna);
                actualizarTemperatura(p.getParticula());
            }
            if(columna - 1 >= 0){
                p = Pantalla.pixeles.get(fila).get(columna - 1);
                actualizarTemperatura(p.getParticula());
            }
            if(columna + 1 < Pantalla.ancho){
                p = Pantalla.pixeles.get(fila).get(columna + 1);
                actualizarTemperatura(p.getParticula());
            }
        }
        
        if(this.temperatura >= this.temperaturaMin || this.combustion){
            double vidaQuitar = rand.nextInt(5);
            vidaQuitar -= vidaQuitar*(this.resistencia_fuego/100);
            this.vida -= vidaQuitar;
            if(this.vida <= 0){
                this.pixel.eliminarParticula();
                eliminarParticulaCombustion();
            }
        }
    }
    
    protected void generar(){
        if(this.generar != -1){
            Random rand = new Random();
            if(rand.nextInt(100) < this.generar_prob){
                ArrayList<Pixel> cercanos = new ArrayList<>();

                int fila = this.pixel.getFila();
                int columna = this.pixel.getColumna();

                Pixel p;
                if(fila - 1 >= 0){
                    p = Pantalla.pixeles.get(fila - 1).get(columna);
                    if(p.getParticula() == null)
                        cercanos.add(p);
                }
                if(fila + 1 < Pantalla.alto){
                    p = Pantalla.pixeles.get(fila + 1).get(columna);
                    if(p.getParticula() == null)
                        cercanos.add(p);
                }
                if(columna - 1 >= 0){
                    p = Pantalla.pixeles.get(fila).get(columna - 1);
                    if(p.getParticula() == null)
                        cercanos.add(p);
                }
                if(columna + 1 < Pantalla.ancho){
                    p = Pantalla.pixeles.get(fila).get(columna + 1);
                    if(p.getParticula() == null)
                        cercanos.add(p);
                }
                int tam = cercanos.size();
                if(tam > 0)
                    Pantalla.fabricaP.generarParticula(cercanos.get(rand.nextInt(tam)), this.generar, true);
                
            }
        }
    }
    
    /**
     * Reacciona a la particula entrante modificando atributos o el estado de laguna de las particulas.
     *
     * @param particula
     * Particula a le cual reaccionará
     * @return 
     * Retorna true si la reacción se elimino a si misma o cambio su estado (partícula), 
     * con lo que los procesos de la particula deben terminar.
     */
    protected abstract boolean reaccionar(Particula particula);
    
    protected void cederCantidadDiagonal(){
        int fila = this.pixel.getFila();
        int columna = this.pixel.getColumna();
        
        int incrementoY;
        if(this.velociadY > 0)
            incrementoY = 1;
        else
            incrementoY = -1;
        
        if(fila + incrementoY < Pantalla.alto && fila + incrementoY >= 0){
            Random rand = new Random();
            
            Pixel p = null;
            Pixel p2 = null;
            Pixel p3;
            Pixel p4 = Pantalla.pixeles.get(fila + incrementoY).get(columna);
            if(p4.getParticula() != null && !(p4.getParticula().isElemento() && ((Elemento)p4.getParticula()).getTipo() == this.tipo)){
                if(columna - 1 >= 0)
                    p = Pantalla.pixeles.get(fila + incrementoY).get(columna - 1);
                if(columna + 1 < Pantalla.ancho)
                    p2 = Pantalla.pixeles.get(fila + incrementoY).get(columna + 1);

                if(this.cantidad > 1){
                    if(this.cantidad == 2){
                        if(rand.nextInt(100) < 50)
                            p3 = p;
                        else
                            p3 = p2;

                        if(p3 != null && p3.getParticula() == null){
                            this.cantidad--;
                            Pantalla.fabricaP.generarParticula(p3, this.tipo, true);
                            p3.getParticula().comportamiento();
                        }

                    }
                    else{
                        if(p != null && p.getParticula() == null){
                            this.cantidad--;
                            Pantalla.fabricaP.generarParticula(p, this.tipo, true);
                            p.getParticula().comportamiento();
                        }

                        if(p2 != null && p2.getParticula() == null){
                            this.cantidad--;
                            Pantalla.fabricaP.generarParticula(p2, this.tipo, true);
                            p2.getParticula().comportamiento();
                        }

                    }
                }
            }
        }
    }
    
    protected void cederCantidadX(){
        int fila = this.pixel.getFila();
        int columna = this.pixel.getColumna();
        
        Random rand = new Random();
        
        Pixel p = null;
        Pixel p2 = null;
        
        if(columna - 1 >= 0)
            p = Pantalla.pixeles.get(fila).get(columna - 1);
        
        if(columna + 1 < Pantalla.ancho)
            p2 = Pantalla.pixeles.get(fila).get(columna + 1);

        if(this.cantidad == 2){
            if(rand.nextInt(100) < 50){
                if(p != null && p.getParticula() == null){
                    this.cantidad--;
                    Pantalla.fabricaP.generarParticula(p, this.tipo, true);
                    p.getParticula().comportamiento();
                }
                else if(p2 != null && p2.getParticula() == null){
                    this.cantidad--;
                    Pantalla.fabricaP.generarParticula(p2, this.tipo, true);
                    p2.getParticula().comportamiento();
                }
            }
            else{
                if(p2 != null && p2.getParticula() == null){
                    this.cantidad--;
                    Pantalla.fabricaP.generarParticula(p2, this.tipo, true);
                    p2.getParticula().comportamiento();
                }
                else if(p != null && p.getParticula() == null){
                    this.cantidad--;
                    Pantalla.fabricaP.generarParticula(p, this.tipo, true);
                    p.getParticula().comportamiento();
                }
            }
        }
        else if(this.cantidad > 2){
            int cantAgregar = 1;
            if(p != null && p.getParticula() == null){
                this.cantidad -= 1;
                Pantalla.fabricaP.generarParticula(p, this.tipo, true);
                p.getParticula().comportamiento();
            }
            else
                cantAgregar++;
            if(p2 != null && p2.getParticula() == null){
                this.cantidad -= cantAgregar;
                Pantalla.fabricaP.generarParticula(p2, this.tipo, true);
                p2.getParticula().comportamiento();
            }
        }
    }
    
    /**
     * Cede 1 de la cantidad de la particula actual a la de arriba/abajo
     * @return 
     * Retorna true si la particula actual no se eliminó
     */
    protected boolean cederCantidadY(){
        int fila = this.pixel.getFila();
        int columna = this.pixel.getColumna();
        
        int incrementoY;
            if(this.velociadY > 0)
                incrementoY = 1;
            else
                incrementoY = -1;
        
        if(fila + incrementoY < Pantalla.alto && fila + incrementoY >= 0){
            Pixel p = Pantalla.pixeles.get(fila + incrementoY).get(columna);

            if(p.getParticula() != null && p.getParticula().isElemento() && ((Elemento)p.getParticula()).getTipo() == this.tipo){
                this.cantidad--;
                p.getParticula().agregarCantidad(1);
                if(this.cantidad == 0){
                    this.pixel.eliminarParticula();
                    return false;
                }
            }
        }
        return true;
    }
    
    protected boolean moverDiagonal(){
        if(this.activar_inercia || !this.inercia){
            this.activar_inercia = false;
            
            int fila = this.pixel.getFila();
            int columna = this.pixel.getColumna();

            int incrementoY;
            if(this.velociadY > 0)
                incrementoY = 1;
            else
                incrementoY = -1;

            Pixel p;
            int dir = 0;
            
            for (int i = 0; i < Math.abs(this.velociadX); i++) {
                if(fila + incrementoY < Pantalla.alto && fila + incrementoY >= 0){
                    
                    p = Pantalla.pixeles.get(fila + incrementoY).get(columna);
                    if(p.getParticula() == null || (p.getParticula() != null && p.getParticula().getDencidad() < this.dencidad))
                        return false;
                    
                    this.velociadX -= this.friccion;
                    if(this.velociadX < 0)
                        this.velociadX = 0;
                    
                    if(dir == 0){
                        if(columna - 1 >= 0){
                            p = Pantalla.pixeles.get(fila + incrementoY).get(columna - 1);
                            if(reaccionar(p.getParticula()))
                                return true;
                            
                            if(p.getParticula() == null){
                                moverParticula(p);
                                inercia();
                                fila += incrementoY;
                                columna--;
                                continue;
                            }
                            else if(desplazarValidar(p.getParticula())){
                                inercia();
                                fila += incrementoY;
                                columna--;
                                continue;
                            }
                        }
                        if(columna + 1 < Pantalla.ancho){
                            p = Pantalla.pixeles.get(fila + incrementoY).get(columna + 1);
                            if(reaccionar(p.getParticula()))
                                return true;
                            
                            if(p.getParticula() == null){
                                dir = 1;
                                moverParticula(p);
                                inercia();
                                fila += incrementoY;
                                columna++;
                                continue;
                            }
                            else if(desplazarValidar(p.getParticula())){
                                inercia();
                                fila += incrementoY;
                                columna++;
                                continue;
                            }
                        }
                    }
                    else{
                        if(columna + 1 < Pantalla.ancho){
                            p = Pantalla.pixeles.get(fila + incrementoY).get(columna + 1);
                            if(reaccionar(p.getParticula()))
                                return true;
                            
                            if(p.getParticula() == null){
                                moverParticula(p);
                                inercia();
                                fila += incrementoY;
                                columna++;
                                continue;
                            }
                            else if(desplazarValidar(p.getParticula())){
                                inercia();
                                fila += incrementoY;
                                columna++;
                                continue;
                            }
                        }
                        if(columna - 1 >= 0){
                            p = Pantalla.pixeles.get(fila + incrementoY).get(columna - 1);
                            if(reaccionar(p.getParticula()))
                                return true;
                            
                            if(p.getParticula() == null){
                                dir = 0;
                                moverParticula(p);
                                inercia();
                                fila += incrementoY;
                                columna--;
                                continue;
                            }
                            else if(desplazarValidar(p.getParticula())){
                                inercia();
                                fila += incrementoY;
                                columna--;
                                continue;
                            }
                        }
                    }
                }
                return false;
            }
            return true;
        }
        return false;
    }
    
    protected boolean moverYDiagonal(){
        int incrementoY;
        if(this.velociadY > 0)
            incrementoY = 1;
        else
            incrementoY = -1;

        int fila = this.pixel.getFila();
        int columna = this.pixel.getColumna();

        Pixel p;
        
        if(this.velociadY == 0 && this.velociadMax > 0 && fila + 1 < Pantalla.alto){
            p = Pantalla.pixeles.get(fila + 1).get(columna);
            if(p.getParticula() == null)
                this.velociadY = 1;
        }
        
        Random rand = new Random();
        int dir;
        ArrayList<Pixel> movimiento;
        
        boolean continuar;
        for (int i = 0; i < Math.abs(this.velociadY); i++) {
            movimiento = new ArrayList<>();
            continuar = false;
            if(fila + incrementoY < Pantalla.alto && fila + incrementoY >= 0){
                p = Pantalla.pixeles.get(fila + incrementoY).get(columna);
                
                movimiento.add(p);
                if(columna - 1 >= 0)
                    p = Pantalla.pixeles.get(fila + incrementoY).get(columna - 1);
                movimiento.add(p);
                if(columna + 1 < Pantalla.ancho)
                    p = Pantalla.pixeles.get(fila + incrementoY).get(columna + 1);
                movimiento.add(p);
                
                for (int j = 0; j < 3; j++) {
                    dir = rand.nextInt(movimiento.size());
                    p = movimiento.get(dir);
                    
                    if(reaccionar(p.getParticula()))
                        return true;
                    
                    if(p != null){
                        if(p.getParticula() == null){
                            fila = p.getFila();
                            columna = p.getColumna();
                            moverParticula(p);
                            inercia();

                            continuar = true;
                            break;
                        }
                        else if(desplazarValidar(p.getParticula())){
                            fila = p.getFila();
                            columna = p.getColumna();
                            inercia();

                            continuar = true;
                            break;
                        }
                    }
                    movimiento.remove(dir);
                }
                
                if(continuar)
                    continue;
            }
            
            if(this.cayendo && this.friccion != 0)
                this.velociadX += Math.abs(this.velociadY);
            
            
            this.cayendo = false;
            
            return false;
        }
        
        this.cayendo = true;
        return true;
    }
    
    protected boolean moverY(){
        int incrementoY;
        if(this.velociadY > 0)
            incrementoY = 1;
        else
            incrementoY = -1;

        int fila = this.pixel.getFila();
        int columna = this.pixel.getColumna();

        Pixel p;
        
        if(this.velociadY == 0 && this.velociadMax > 0 && fila + 1 < Pantalla.alto){
            p = Pantalla.pixeles.get(fila + 1).get(columna);
            if(p.getParticula() == null)
                this.velociadY = 1;
        }
        
        for (int i = 0; i < Math.abs(this.velociadY); i++) {
            if(fila + incrementoY < Pantalla.alto && fila + incrementoY >= 0){
                p = Pantalla.pixeles.get(fila + incrementoY).get(columna);
                if(reaccionar(p.getParticula()))
                    return true;
                
                if(p.getParticula() == null){
                    if(this.velociadY > 0 && this.velociadY < this.velociadMax)
                        this.velociadY++;
                    
                    moverParticula(p);
                    inercia();
                    fila += incrementoY;
                    continue;
                }
                else if(desplazarValidar(p.getParticula())){
                    inercia();
                    fila += incrementoY;
                    continue;
                }
            }
            
            if(this.cayendo && this.friccion != 0)
                this.velociadX += Math.abs(this.velociadY);
            
            this.cayendo = false;
            
            return false;
        }
        
        this.cayendo = true;
        return true;
    }
    
    protected boolean moverX(){
        if(this.velociadX == 0)
            return true;
        
        int incrementoY;
        if(this.velociadY > 0)
            incrementoY = 1;
        else
            incrementoY = -1;
        
        int fila = this.pixel.getFila();
        int columna = this.pixel.getColumna();
        
        Pixel p;
        int dir = 0;
        for (int i = 0; i < Math.abs(this.velociadX); i++) {
            for (int j = 1; j <= 5; j++) {
                if(fila + incrementoY*j < Pantalla.alto && fila + incrementoY*j >= 0){
                    p = Pantalla.pixeles.get(fila + incrementoY*j).get(columna);
                    
                    if(p.getParticula() == null)
                        return false;
                    else if(p.getParticula().getDencidad() < this.dencidad)
                        return false;
                    else if(p.getParticula().isElementoSolido() && ((Solido)p.getParticula()).isEstatico())
                        break;
                }
                else
                    break;
            }
            
            this.velociadX -= this.friccion;
            if(this.velociadX < 0)
                this.velociadX = 0;
            
            
            if(dir == 0){
                if(columna - 1 >= 0){
                    p = Pantalla.pixeles.get(fila).get(columna - 1);
                    if(reaccionar(p.getParticula()))
                        return true;
                    
                    if(p.getParticula() == null){
                        moverParticula(p);
                        columna--;
                        continue;
                    }
                    else if(desplazarValidar(p.getParticula())){
                        columna--;
                        continue;
                    }
                }
                if(columna + 1 < Pantalla.ancho){
                    p = Pantalla.pixeles.get(fila).get(columna + 1);
                    if(reaccionar(p.getParticula()))
                        return true;
                    
                    if(p.getParticula() == null){
                        dir = 1;
                        moverParticula(p);
                        columna++;
                        continue;
                    }
                    else if(desplazarValidar(p.getParticula())){
                        columna++;
                        continue;
                    }
                }
            }
            else{
                if(columna + 1 < Pantalla.ancho){
                    p = Pantalla.pixeles.get(fila).get(columna + 1);
                    if(reaccionar(p.getParticula()))
                        return true;
                    
                    if(p.getParticula() == null){
                        moverParticula(p);
                        columna++;
                        continue;
                    }
                    else if(desplazarValidar(p.getParticula())){
                        columna++;
                        continue;
                    }
                }
                if(columna - 1 >= 0){
                    p = Pantalla.pixeles.get(fila).get(columna - 1);
                    if(reaccionar(p.getParticula()))
                        return true;
                    
                    if(p.getParticula() == null){
                        dir = 0;
                        moverParticula(p);
                        columna--;
                        continue;
                    }
                    else if(desplazarValidar(p.getParticula())){
                        columna--;
                        continue;
                    }
                }
            }
            
            return false;
        }
        return true;
    }
    
    
    public abstract void comportamiento();
    
    public void actualizarColor(){
        if(this.rojo < 0)
            this.rojo = 0;
        if(this.verde < 0)
            this.verde = 0;
        if(this.azul < 0)
            this.azul = 0;
        
        if(this.rojo > 255)
            this.rojo = 255;
        if(this.verde > 255)
            this.verde = 255;
        if(this.azul > 255)
            this.azul = 255;
        
        this.color = new Color(this.rojo, this.verde, this.azul);
        if(this.pixel != null)
            this.pixel.setActualizar(true);
        
    }
    
    public void cambiarTono(int max, int min){
        int dif = (int) ((Math.random() * (max - min)) + min);
        
        if(Math.random() > 0.5)
            setColor(this.rojo + dif, this.verde+ dif, this.azul+ dif);
        else
            setColor(this.rojo - dif, this.verde- dif, this.azul- dif);
        
    }
    
    public void agregarCantidad(int cantidad){
        this.cantidad += cantidad;
    }
    public void quitarCantidad(int cantidad){
        this.cantidad -= cantidad;
    }
    
    //is
    public boolean isCombustion(){
        return combustion;
    }

    public boolean isParticulaExp() {
        return particulaExp;
    }

    public boolean isCayendo() {
        return cayendo;
    }

    public boolean isActualizar() {
        return actualizar;
    }

    public boolean isElemento() {
        return this.tipo == Particula.ELEMENTO;
    }
    
    public boolean isParticulaExplocion() {
        return this.tipo == Particula.PARTICULA_EXPLOCION;
    }
    
    public boolean isElementoGaseoso(){
        if(isElemento())
            return ((Elemento)this).isGaseoso();
        return false;
    }
    
    public boolean isElementoSolido(){
        if(isElemento())
            return ((Elemento)this).isSolido();
        return false;
    }
    
    //set
    public void setCombustion(boolean combustion) {    
        this.combustion = combustion;
    }

    public void setParticulaExp(boolean particulaExp) {
        this.particulaExp = particulaExp;
    }

    public void setTemperaturaMin(double temperaturaMin) {
        this.temperaturaMin = temperaturaMin;
    }
    
    public void setVida(double vida) {
        this.vida = vida;
    }

    public void setDencidad(double dencidad) {
        this.dencidad = dencidad;
    }
    
    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }
    
    public void setVelociadX(int velociadX) {
        this.velociadX = velociadX;
    }

    public void setVelociadY(int velociadY) {
        this.velociadY = velociadY;
    }

    public void setActivar_inercia(boolean activar_inercia) {
        this.activar_inercia = activar_inercia;
    }

    public void setActualizar(boolean actualizar) {
        this.actualizar = actualizar;
    }

    public void setPixel(Pixel pixel) {
        this.pixel = pixel;
    }
    
    public void setColor(Color color){
        this.rojo = color.getRed();
        this.verde = color.getGreen();
        this.azul = color.getBlue();
        
        actualizarColor();
    }
    
    public void setColor(int rojo, int verde, int azul) {
        this.rojo = rojo;
        this.verde = verde;
        this.azul = azul;
        
        actualizarColor();
    }
    
    public void setRojo(int rojo) {
        this.rojo = rojo;
        actualizarColor();
    }

    public void setVerde(int verde) {
        this.verde = verde;
        actualizarColor();
    }

    public void setAzul(int azul) {
        this.azul = azul;
        actualizarColor();
    }
    
    
    //get
    public int getCantidad() {
        return cantidad;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public double getTemperaturaMin() {    
        return temperaturaMin;
    }

    public double getConductividad() {
        return conductividad;
    }

    public double getVelociadX() {
        return velociadX;
    }

    public double getVelociadY() {    
        return velociadY;
    }

    public double getFriccion() {
        return friccion;
    }

    public double getDencidad() {
        return dencidad;
    }

    public int getResistenciaInercia() {
        return resistencia_inercia;
    }

    public Pixel getPixel() {
        return pixel;
    }

    public int getRojo() {
        return rojo;
    }

    public int getVerde() {
        return verde;
    }

    public int getAzul() {
        return azul;
    }

    public Color getColor() {
        return color;
    }

    public double getVida() {
        return vida;
    }
    
}
