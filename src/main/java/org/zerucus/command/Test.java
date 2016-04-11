/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zerucus.command;

/**
 *
 * @author zbigniewk
 */
public class Test {
    String t = null;

    public Test (String t) {
        this. t = t;
        
    }

    public Test objMethod(String t) {
        System.out.println("objMethod: "+t);
        return new Test(t);
    }

    public void objMethod1(Test t) {
        System.out.println("objMethod1 with class Test: "+t.t);
    }
    public void method1(int a) {
        System.out.println("Method1a for class "+t+" : "+a);
    }
    public void method1(String b) {
        System.out.println("Method1b: "+b);
    }

    public void method2(String a) {
        System.out.println("Method2 for class "+t+" : "+a);
    }

    public void method3(boolean x) {
        System.out.println("Method3: "+x);
    }

    public void method4(Integer a) {
        System.out.println("Method4:"+a);
    }

    public void method5(int a, int b, String c) {
        System.out.println("Method5: "+a+", "+b+", "+c);
    }
}
