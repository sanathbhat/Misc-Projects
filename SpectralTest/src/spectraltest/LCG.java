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
 * Class that represents a Linear Congruential Generator(LCG) of the form x(n) = ax(n-1)+c mod m
 */
public class LCG {

    private BigInteger a;
    private BigInteger c;
    private BigInteger m;

    /**
     * Creates a LCG from BigIneger representations of a, c and m
     * @param a a value of LCG
     * @param c c value of LCG
     * @param m m value of LCG
     */
    public LCG(BigInteger a, BigInteger c, BigInteger m) {
        this.a = a;
        this.c = c;
        this.m = m;
    }
    
    /**
     * Creates a LCG from long representations of a, c and m
     * @param a a value of LCG
     * @param c c value of LCG
     * @param m m value of LCG
     */
    public LCG(long a, long c, long m) {
        this.a = new BigInteger(String.valueOf(a));
        this.c = new BigInteger(String.valueOf(c));
        this.m = new BigInteger(String.valueOf(m));
    }

    /**
     * @return a
     */
    public BigInteger getA() {
        return a;
    }

    /**
     * @return c
     */
    public BigInteger getC() {
        return c;
    }

    /**
     * @return m
     */
    public BigInteger getM() {
        return m;
    }
}
