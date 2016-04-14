/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.zerucus.command.samples;

/**
 *
 * @author zbigniewk
 */
public class Sample {
    String t = null;

    public Sample (String t) {
        this. t = t;
        
    }

    public Sample objMethod(String t) {
        System.out.println("objMethod: "+t);
        return new Sample(t);
    }

    public void objMethod1(Sample t) {
        System.out.println("objMethod1 with Sample Class: "+t.t);
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
