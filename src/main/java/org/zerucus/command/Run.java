/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zerucus.command;
import java.util.*;
import org.zerucus.command.jaxb.CommandChain;


/**
 *
 * @author zbigniewk
 */
public class Run {

    static CommandChain currentCommand;
    public static void main(String[] p) {
        
 // 1. odczytanie z listy poleceń parametrów programu i powiązanie ich w pary (parametr=wartość)
        Properties params = Utils.parseParameters(p);
//2. odczytanie xml'a
        if (p.length == 0) {
            System.out.println("Usage: Command commands\n\rcommand - path with file name to xml file with commands definition. ");
        }
        try {
        currentCommand = Utils.getCurrentCommand(params, p[0]);
            org.zerucus.command.CommandChain cc = new org.zerucus.command.CommandChain(currentCommand, params);
        cc.execute();
        }
        catch (Exception e) {e.printStackTrace();}
 

//wykonujemy wszystkie command w kolejno�ci ich wyst�powania w pliku XML.
}
            
    }

