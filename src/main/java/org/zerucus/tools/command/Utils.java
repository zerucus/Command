/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zerucus.tools.command;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.zerucus.command.jaxb.CommandChain;
import org.zerucus.command.jaxb.Commands;

/**
 *
 * @author zbigniewk
 */
public class Utils {
    public static Map<Class, Class> privToObj = new HashMap<Class, Class>();

    public static Integer convert(int a) {
        return a;
    }
    public static Long convert(long a) {
        return a;
    }

    public static Boolean convert(boolean a) {
        return a;
    }

    public static Float convert(float a) {
        return a;
    }

    public static Double convert(double a) {
        return a;
    }

    public static Character convet(char a) {
        return a;
    }

    public static Properties parseParameters(String[] params) {
        Properties p = new Properties();
        for (String param:params) {
            String[] tab = param.split("=");// może być parametr typu np. generate=plik
            if (tab.length==2)
                p.put(tab[0], tab[1]);
            else
                p.put(tab[0], "");
        }
        return p;
    }

    static CommandChain getCurrentCommand(Properties params, String string) throws Exception {
        CommandChain currentCommand = null;
        JAXBContext jc = JAXBContext.newInstance("org.zerucus.command.jaxb");
        Unmarshaller u =   jc.createUnmarshaller();
        Commands com = (Commands)u.unmarshal( new FileInputStream(string));
        List<CommandChain> lcc = com.getCommandChain();
        for (CommandChain c : lcc)
            if (params.containsKey(c.getName()))
                currentCommand = c;
        if (currentCommand == null)
            throw new Exception ("Command not found");
        return currentCommand;
    }
}
