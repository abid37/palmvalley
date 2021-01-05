/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeks.utils;

import java.util.Random;

/**
 *
 * @author xyz
 */
public class BarCodeGenerator {
    
    public static String generator(){
        String bar="";
        String alphs = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
        char al[] = alphs.toCharArray();
        
        int n1=(int)((Math.random()*100)%26);
        int n2=(int)((Math.random()*100)%26);
        int n3=(int)((Math.random()*100)%26);
        int n4=(int)((Math.random()*100)%10);
        int n5=(int)((Math.random()*100)%10);
        int n6=(int)((Math.random()*100)%10);
       
        bar=al[n1]+""+al[n2]+""+al[n3]+"-"+n4+""+n5+""+n6;
        
        return bar;
    }
}
