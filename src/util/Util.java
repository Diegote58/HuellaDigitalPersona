/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author ASUS555
 */
public class Util {
    
    //Cambiamos Caracteres para POST/GET
    public String hardCodeCaracteres( String valor){
        valor=valor.replace("á","a");valor=valor.replace("Á","a");  
        valor=valor.replace("é","e");valor=valor.replace("É", "e");
        valor=valor.replace("í","i");valor=valor.replace("Í","i");
        valor=valor.replace("ó","o");valor=valor.replace("Ó","o");
        valor=valor.replace("ú","u");valor=valor.replace("Ú","u");
        valor=valor.replace( "ñ", "n");valor=valor.replace( "Ñ", "n");

        return valor;
    }
    
}
