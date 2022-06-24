/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.io.IOException;

/**
 *
 * @author Josue Alvarez M
 */
public class Main{
    public static void main(String[] args) throws IOException, InterruptedException {
        Pantalla pantalla = new Pantalla();
        pantalla.loop();
    }
}
