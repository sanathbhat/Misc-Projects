/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spectraltest;

import java.math.BigInteger;

/**
 *
 * @author Bhat, Sanath
 */
public class SpectralTestDriver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpectralTest t1 = new SpectralTest(new LCG(23,                       1, 100000001), 6);   //works
        SpectralTest t2 = new SpectralTest(new LCG((long)Math.pow(2, 7)+1,   1, (long)Math.pow(2, 35)), 6);  //works
        SpectralTest t3 = new SpectralTest(new LCG((long)Math.pow(2, 18)+1,  1, (long)Math.pow(2, 35)), 6);  //works
        SpectralTest t4 = new SpectralTest(new LCG(3141592653L,              1, (long)Math.pow(2, 35)), 6);  //works
        SpectralTest t5 = new SpectralTest(new LCG(137,                      1, 256), 6);    //works
        SpectralTest t6 = new SpectralTest(new LCG(3141592621L,              1, (long)Math.pow(10, 10)), 6); //works
        SpectralTest t7 = new SpectralTest(new LCG(3141592221L,              1, (long)Math.pow(10, 10)), 6); //works
        SpectralTest t8 = new SpectralTest(new LCG(4219755981L,              1, (long)Math.pow(10, 10)), 6); //works
        SpectralTest t12 = new SpectralTest(new LCG((long)Math.pow(5, 13),   1, (long)Math.pow(2, 35)), 6);  //works
        SpectralTest t18 = new SpectralTest(new LCG((long)Math.pow(2, 24)+(long)Math.pow(2, 13)+5, 1, (long)Math.pow(2, 35)), 6);   //works
        SpectralTest t22 = new SpectralTest(new LCG((long)Math.pow(2, 16)+3, 1, (long)Math.pow(2, 29)), 6);  //works
        SpectralTest t24 = new SpectralTest(new LCG(1566083941,              1, (long)Math.pow(2, 32)), 6);    //works
        SpectralTest t25 = new SpectralTest(new LCG(69069,                   1, (long)Math.pow(2, 32)), 6);    //works
        SpectralTest t26 = new SpectralTest(new LCG(1664525,                 1, (long)Math.pow(2, 32)), 6);    //works
        
        SpectralTest tHuge = new SpectralTest(new LCG(new BigInteger("6364136223846793005"), BigInteger.ONE, new BigInteger("18446744073709551616")), 16);
        
        SpectralTest t[] = {t1, t2, t3, t4, t5, t6, t7, t8, t12, t18, t22, t24, t25, t26};
        for (int i = 0; i < t.length; i++) {
            t[i].test();
            System.out.println("\n\n");
        }
//        tHuge.test();      
    }
}