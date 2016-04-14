/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zerucus.command;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zbigniewk
 */
public class TypeWrapper {
    static final Map<Class, Class> privToObj = new HashMap<Class, Class>();
    Class outClass;

    public TypeWrapper(Class c) {
        outClass = privToObj.get(c);
    }

    public static Class convert(Class c) {
        Class cc = privToObj.get(c);
        if (cc == null)
            return c;
        return cc;
    }

    static {
        privToObj.put(int.class, Integer.class);
        privToObj.put(long.class, Long.class);
        privToObj.put(float.class, Float.class);
        privToObj.put(double.class, Double.class);
        privToObj.put(boolean.class, Boolean.class);
        privToObj.put(char.class, Character.class);
        privToObj.put(byte.class, Byte.class);
    }
}
