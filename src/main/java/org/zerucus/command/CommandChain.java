/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zerucus.command;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.zerucus.command.jaxb.Command;
import org.zerucus.command.jaxb.Param;

/**
 *
 * @author zbigniewk
 */
public class CommandChain {

    org.zerucus.command.jaxb.CommandChain commandChain;
    Properties params;
    public static final String CONSTRUCTOR = "Constructor";

    public CommandChain(org.zerucus.command.jaxb.CommandChain c, Properties p) {
        commandChain = c;
        params = p;
    }

    /**
     * Method run all methods listed on commandChain
     *
     * @throws Exception
     */
    public void execute() throws Exception {
        Map<String, Object> returnObjects = new HashMap<>();
        Map<Command, AccessibleObject> methods = getMethodsToRun();

        Object o;
        for (Command c : commandChain.getCommand()) {
            o = executeMethod(methods.get(c), returnObjects, c, params);
            if (o != null) {
                returnObjects.put(c.getName(), o);
            }
        }

    }

    /**
     * Method execute passed method <code>obj</code> with passed parameters. Method can be any method or constructor.
     *
     * @param obj Method or constructor to execute
     * @param objs map of parameters to pass to execute method.
     * @param comm Command object
     * @param params values of parameters of executed method.
     * @return object, with return executed method or null, if method don't return any object.
     * @throws ClassNotFoundException
     * @throws Exception
     */

    Object executeMethod(AccessibleObject obj, Map<String, Object> objs, Command comm, Properties params) throws ClassNotFoundException, Exception {
        Constructor ctr = null;
        Method m = null;
        // obj is method or constructor
        if (obj instanceof Constructor) {
            ctr = (Constructor) obj;
        } else {
            m = (Method) obj;
        }
        // array contains method (or contructor) parameters.
        Object[] o = new Object[ctr != null ? ctr.getParameterTypes().length : m.getParameterTypes().length];
        int i = 0;
        for (Param par : comm.getParams().getParam()) {
//                Class cl = Class.forName(par.getType()); // klasa parametru metody
            Class cl = ctr != null ? ctr.getParameterTypes()[i] : m.getParameterTypes()[i];
            if (cl.isPrimitive() || "java.lang.String".equals(cl.getName())) {
                o[i++] = getPrimitiveParameter(cl, par.getContent(), (String) params.get(par.getName()));
            } else {
                o[i++] = getObjectParameter(objs, par.getName());
            }
        }
        Object runObj = null;
        String ci = comm.getClassInstance();
        if (ci != null && objs.containsKey(ci)) {
            runObj = objs.get(ci);
        }
        if (ci != null && runObj == null) {
            System.out.println("Object with name " + comm.getClassInstance() + " not found, but it should be.");
        }
        return m != null ? m.invoke(runObj, o) : ctr.newInstance(o);
    }

    private Object getPrimitiveParameter(Class cl, String defaultValue, String paramValue) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object o;
        Constructor cr = TypeWrapper.convert(cl).getConstructor(String.class);
        // jeżeli jest parametr w linii wywołań, to go wrzucamy, jeżeli nie, wówczas bierzemy default.
        if (paramValue != null) {
            o = cr.newInstance(paramValue);
        } else {
            o = cr.newInstance(defaultValue);
        }
        return o;
    }

    private Object getObjectParameter(Map<String, Object> objs, String paramName) {
        // w przypadku paramerów będących obiektami, zakładamy, że obiekty te zostały utworzone
        // w trakcie wywołań wcześniejszych komend chaina. Zakładamy, że nazwa parametru MUSI być taka sama
        // jak nazwa komendy, w trakcie wywołania której został wygenerowany obiekt
        return objs.get(paramName);
    }

    private Map<Command, AccessibleObject> getMethodsToRun() throws Exception {
        Map<Command, AccessibleObject> methods = new LinkedHashMap<Command, AccessibleObject>();
        AccessibleObject ao = null;

        for (Command c : commandChain.getCommand()) {
            ao = null;
            Class cl = Class.forName(c.getClassName());
            if (CONSTRUCTOR.equals(c.getMethod())) {
                for (Constructor ctor : cl.getConstructors()) {
                    ao = check(ctor, c);
                    if (ao != null) {
                        break;
                    }
                }
            } else {
                for (Method m : cl.getDeclaredMethods()) {
                    ao = check(m, c);
                    if (ao != null) {
                        break;
                    }
                }
            }

            if (ao != null) {
                methods.put(c, ao);
            } else {
                if (CONSTRUCTOR.equals(c.getMethod())) {
                    System.out.println("Usage:\n\r" + c.getDescription());
                    throw new Exception("Constructor not found in class " + c.getClassName());
                } else {
                    System.out.println("Usage:\n\r" + c.getDescription());
                    throw new Exception("Method with name " + c.getMethod() + " not found in class " + c.getClassName());
                }
            }

        }
        return methods;
    }

    /**
     * Method checks, if method m is on list of methods in command.
     *
     * @param m method to check
     * @param comm object command, that contais informations about executed command
     * @return m or null, if m is not on list of methods in command.
     * @throws Exception
     */
    private AccessibleObject check(Method m, Command comm) throws Exception {
        List<Param> cParams = comm.getParams().getParam();
        boolean is = true;

        // dla każdej metody znajdujemy właściwe i sprawdzamy dla nich listę parametrów.
        // parametr is jest wprowadzony z tego względu, że może być kilka metod o tej samej nazwie
        // i trzeba sprawdzić wszystkie.
        if (m.getName().equals(comm.getMethod())) {
            is = true;
            for (Class par : m.getParameterTypes()) {
                is = false;
                for (Param tp : cParams) {
                    if (tp.getType().equals(par.getName())) {
                        is = true;
                        break;
                    }
                }
            }
            if (is) {
                return m;
            }
        }
        return null;
    }

    private AccessibleObject check(Constructor m, Command comm) throws Exception {
        List<Param> cParams = comm.getParams().getParam();
        boolean is = true;

        // dla każdej metody znajdujemy właściwe i sprawdzamy dla nich listę parametrów.
        // parametr is jest wprowadzony z tego względu, że może być kilka metod o tej samej nazwie
        // i trzeba sprawdzić wszystkie.
        for (Class par : m.getParameterTypes()) {
            is = false;
            for (Param tp : cParams) {
                if (tp.getType().equals(par.getName())) {
                    is = true;
                    break;
                }
            }
        }
        if (is) {
            return m;
        }
        return null;
    }

}
