/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import particulas.Elemento;
import particulas.Particula;
import particulas.ParticulaExplocion;
import particulas.elementos.Arena;
import particulas.elementos.Agua;
import particulas.elementos.Fuego;
import particulas.elementos.Hierro;
import particulas.elementos.Humo;
import particulas.elementos.Piedra;
import particulas.elementos.Lava;
import particulas.elementos.Tierra;
import particulas.elementos.VaporAgua;

/**
 *
 * @author Josue Alvarez M
 */
public class FabricaParticulas {

    public FabricaParticulas() {
    }
    
    public Particula generarParticula(Pixel p, int tipo, boolean elemento){
        try{
            if(p.getParticula() == null && elemento){
                if(tipo == Elemento.AGUA)
                    return new Agua(p);
                else if(tipo == Elemento.ARENA)
                    return new Arena(p);
                else if(tipo == Elemento.PIEDRA)
                    return new Piedra(p);
                else if(tipo == Elemento.TIERRA)
                    return new Tierra(p);
                else if(tipo == Elemento.LAVA)
                    return new Lava(p);
                else if(tipo == Elemento.HUMO)
                    return new Humo(p);
                else if(tipo == Elemento.VAPOR_AGUA)
                    return new VaporAgua(p);
                else if(tipo == Elemento.FUEGO)
                    return new Fuego(p);
                else if(tipo == Elemento.HIERRO)
                    return new Hierro(p);
            }
            if(!elemento){
                if(tipo == Particula.PARTICULA_EXPLOCION && (p.getParticula() == null || p.getParticula() != null && !p.getParticula().isParticulaExplocion() && p.getParticula().isParticulaExp()))
                    return new ParticulaExplocion(p);
            }
        }catch(Exception e){}
        
        return null;
    }
}
