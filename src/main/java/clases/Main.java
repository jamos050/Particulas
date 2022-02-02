/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author Josue Alvarez M
 */
public class Main{
    public static void main(String[] args) {
        Pantalla pantalla = new Pantalla();
        Thread t1 = new Thread(pantalla);
        t1.start();
    }
}
