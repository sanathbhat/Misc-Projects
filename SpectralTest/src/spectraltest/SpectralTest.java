/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spectraltest;

import java.math.BigInteger;

/**
 * Class that helps to perform the spectral test on a LCG
 * @author Bhat, Sanath
 */
public class SpectralTest {
    private static final double SQRTPI = Math.sqrt(Math.PI);
    private final double nu[];
    private final double mu[];
    private final int T;
    private final LCG generator;

    public SpectralTest(LCG generator, int T) {
        this.nu = new double[T];
        this.mu = new double[T];
        this.T = T;
        this.generator = generator;
    }
    
    /**
     * @return the nu. nu(t) is the shortest vector length that defines a family of hyperplanes containing the elements of the lattice L(t) formed by the LCG in t-dimensional space
     */
    public double[] getNu() {
        return nu;
    }

    /**
     * @return the mu. mu(t) is the figure of merit given by the volume of an ellipsoid in t-space defined by the LCG parameters
     */
    public double[] getMu() {
        return mu;
    }
    /**
     * Performs the Spectral Test on a LCG. Results are stored in nu[]
     * @return 
     */
    public double[] test() {
        BigInteger a = generator.getA();
        BigInteger m = generator.getM();
        BigInteger h, hPrime, p, pPrime;
        BigInteger r, s;
        BigInteger q;
        BigInteger U[][], V[][];
        int t;

        //Step 1: Initialization
        h = a;
        hPrime = m;
        p = BigInteger.ONE;
        pPrime = BigInteger.ZERO;
        r = a;
        s = a.multiply(a).add(BigInteger.ONE);
        t = 1;
        //End of Step 1: Initialization

        //Step 2: Euclidean step to handle the case when t=2.
        U = new BigInteger[t][t];
        V = new BigInteger[t][t];
        //In this step, U & V have only one element
        do {
            q = hPrime.divide(h);
            U[0][0] = hPrime.subtract(q.multiply(h));         //Same as hPrime % h?
            V[0][0] = pPrime.subtract(q.multiply(p));
            BigInteger temp = U[0][0].multiply(U[0][0]).add(V[0][0].multiply(V[0][0]));
            if (temp.compareTo(s) >= 0) {                   //if temp>=s
                break;
            }
            //else if u2+v2<s do the following and loop back
            s = temp;
            hPrime = h;
            h = U[0][0];
            pPrime = p;
            p = V[0][0];
        } while (true);
        //End of Step 2: Euclidean step for t=2

        //Step 3: Compute nu2
        U[0][0] = U[0][0].subtract(h);
        V[0][0] = V[0][0].subtract(p);
        if (U[0][0].multiply(U[0][0]).add(V[0][0].multiply(V[0][0])).compareTo(s)==-1) {
            s = U[0][0].multiply(U[0][0]).add(V[0][0].multiply(V[0][0]));
            hPrime = U[0][0];
            pPrime = V[0][0];
        }
        t++;                    //t=2
        nu[t - 1] = s.doubleValue();          //nu2

        //Setting up the matrices
        U = new BigInteger[t][t];
        V = new BigInteger[t][t];

        //U:
        U[0][0] = h.negate();
        U[0][1] = p;
        U[1][0] = hPrime.negate();
        U[1][1] = pPrime;

        //V:
        V[0][0] = pPrime;
        V[0][1] = hPrime;
        V[1][0] = p.negate();
        V[1][1] = h.negate();

        if (pPrime.compareTo(BigInteger.ZERO) > 0) //if pPrime>0 then V = -V
        {
            for (int i = 0; i < t; i++) {
                for (int j = 0; j < t; j++) {
                    V[i][j] = V[i][j].negate();
                }
            }
        }
        //End of Step 3: Compute nu2

        //Step 4: For values 2<t<=T
        while (t < T) {
            t++;                                //3, 4,..., T
            //System.out.println("t="+t);
            
            r = a.multiply(r).mod(m);

            BigInteger uIncreased[][] = new BigInteger[t][t];
            BigInteger vIncreased[][] = new BigInteger[t][t];

            for (int i = 0; i < t; i++) {
                for (int j = 0; j < t; j++) {
                    uIncreased[i][j] = vIncreased[i][j] = BigInteger.ZERO;
                }
            }
            
            //setting Ut and Vt i.e. last rows of the new U and V
            uIncreased[t - 1][0] = r.negate();
            vIncreased[t - 1][0] = BigInteger.ZERO;
//            for (int i = 1; i < t - 1; i++) {
//                uIncreased[t - 1][i] = vIncreased[t - 1][i] = BigInteger.ZERO;
//            }
            uIncreased[t - 1][t - 1] = BigInteger.ONE;
            vIncreased[t - 1][t - 1] = m;

            //copy u & v to uIncreased & vIncreased
            for (int i = 0; i < U.length; i++) {
                int j;
                for (j = 0; j < U[i].length; j++) {
                    uIncreased[i][j] = U[i][j];
                    vIncreased[i][j] = V[i][j];
                }
                //Uit = 0 , Vit = Vi1*r - q*m where q = round(vi1*r/m) for 1<=i<t. Here j=t now.
//                uIncreased[i][j] = 0;
                //q = Math.round(vIncreased[i][1] * r / (double)m);           //casting to prevent precision loss by division
                //q = vIncreased[i][1].multiply(r).divide(m);
                q = divideAndRound(vIncreased[i][0].multiply(r), m);
                vIncreased[i][j] = vIncreased[i][0].multiply(r).subtract(q.multiply(m));

                //Ut = Ut + q*Ui
                uIncreased[t - 1] = (new Vector(uIncreased[t - 1]).add(new Vector(uIncreased[i]).multiplyScalar(q))).toArray();
            }
            
            U = uIncreased;
            V = vIncreased;

            BigInteger utSq = new Vector(U[t - 1]).selfProduct();
            if (utSq.compareTo(s) < 0) {
                s = utSq;
            }

            int k = t - 1;      //last index where the transformation shortened atleast one of the Vi
            int j = 0;      //current row index for transformation(Knuth's algo is 1-based, arrays here are 0-based)

            //Step 7: Advance j. Starts elsewhere according to Knuth's algo. Modified to prevent gotos.
            do {
                //Step 5: Transform
                for (int i = 0; i < t; i++) {
                    Vector vI = new Vector(V[i]);
                    Vector vJ = new Vector(V[j]);
                    BigInteger vIDotJ = vI.multiply(vJ);
                    BigInteger vJDotJ = vJ.selfProduct();
                    //System.out.println(vIDotJ.multiply(new BigInteger("2"))+" & "+vJDotJ+" i= "+i+" j= "+j);
                    if (i != j && vIDotJ.abs().multiply(new BigInteger("2")).compareTo(vJDotJ) > -1) {
                        q = divideAndRound(vIDotJ, vJDotJ);            //casting to prevent precision loss by division
                        //Vi = Vi - q*Vj
                        V[i] = vI.subtract(vJ.multiplyScalar(q)).toArray();
                        //Uj = Uj + q*Ui
                        U[j] = new Vector(U[j]).add(new Vector(U[i]).multiplyScalar(q)).toArray();
                        k = j;
                    }
                }
                //End of Step 5: Transform

                //Step 6: Examine new bound
                if (k == j) {
                    BigInteger ujSq = new Vector(U[j]).selfProduct();
                    if (ujSq.compareTo(s) < 0) {
                        s = ujSq;
                    }
                }
                //End of Step 6: Examine new bound
                j = (j + 1) % t;
            } while (j != k);
            //System.out.println("Ended loop of step 7");
            //End of Step 7: Advance j

            //Step 8: Prepare for search
            BigInteger X[] = new BigInteger[t];
            BigInteger Y[] = new BigInteger[t];
            BigInteger Z[] = new BigInteger[t];
            //extra overhead of initializing BigInteger arrays :(
            for (int i = 0; i < t; i++) {
                X[i] = Y[i] = Z[i] = BigInteger.ZERO;
            }
            
            k = t - 1;
            //Setting Z
            for (int i = 0; i < t; i++) {
                Vector vI = new Vector(V[i]);
                //Z[i] = (BigInteger) Math.floor(Math.sqrt(Math.floor((vI.selfProduct() * s / (double)m)/ m)));     //If m is too big m^2 becomes inf, 
                //so divide by m twice instead of dividing by m^2
                //Exploiting the fact that floor(a/b^2) = floor(floor(a/b)/b)
                //The BigIntegers are actually small enough before taking square roots hence sqrt(double) works fine enough
                String temp = String.valueOf(Math.floor(Math.sqrt(Math.floor(
                        vI.selfProduct().multiply(s).divide(m).divide(m).doubleValue()))));
                Z[i] = new BigInteger(temp.substring(0, temp.length()-2));
                //----------------------------------------------------------------------------------------------------
            }
            //End of Step 8: Prepare for search

            //Step 9: Advance Xk
            do {
                if (X[k].compareTo(Z[k])!=0) {                                 //X[k] != Z[k]
                    X[k] = X[k].add(BigInteger.ONE);
                    Y = new Vector(Y).add(new Vector(U[k])).toArray();

                    //Step 10: Advance k
                    do {
                        ++k;
                        if (k <= t - 1) {
                            X[k] = Z[k].negate();
                            Y = new Vector(Y).subtract(new Vector(U[k]).multiplyScalar(Z[k].multiply(new BigInteger("2")))).toArray();
                        } else {
                            BigInteger ySq = new Vector(Y).selfProduct();
                            if (ySq.compareTo(s) < 0) {
                                s = ySq;
                            }
                            break;
                        }
                    } while (true);
                    //End of Step 10: Advance k
                }
                //End of Step 9: Advance Xk
                //Step 11: Decrease k
                --k;
                //System.out.println("Stuck at k="+k);
            } while (k >= 0);
            nu[t - 1] = s.doubleValue();
            //End of Step 11: Decrease k
        }
        //End of Step 4: For values 2<t<=T
        
        //Calculating mu
        calculateMu();
        //
        //Show a comprehensive summary of the test results
        showSummary();
        return getNu();
    }
    
    private void calculateMu()
    {
        for (int t = 1; t < T; t++) {
            mu[t] = (Math.pow(SQRTPI*Math.sqrt(getNu()[t]), t+1))/factorial((t+1)/(double)2)/generator.getM().doubleValue();
        }
    }
    
    /**
     * Displays the values of (nu(t))^2 and nu(t) on the console for all t between 2 and T.
     */
    public void showAllNu()
    {
        for (int i = 1; i < T; i++) {
            System.out.println("(nu("+(i+1)+"))^2 : "+getNu()[i]+"\t\t| nu("+(i+1)+") : "+Math.sqrt(getNu()[i]));
        }
    }
    
    /**
     * Displays the values of mu(t) on the console for all t between 2 and T.
     */
    public void showAllMu()
    {
        for (int i = 1; i < T; i++) {
            System.out.println("mu["+(i+1)+"]^2 : "+getNu()[i]+"\t\t| nu["+(i+1)+"] : "+Math.sqrt(getNu()[i]));
        }
    }
    
    /**
     * Shows a summary of results of the spectral test applied to the LCG
     */
    private void showSummary() {
        System.out.println("LCG: a="+generator.getA()+", m="+generator.getM()+", c="+generator.getC());
        for (int i = 1; i < T; i++) {
            System.out.println("nu("+(i+1)+") : "+Math.sqrt(getNu()[i])+"\t\t| mu("+(i+1)+") : "+getMu()[i]);
        }
        boolean goodMultiplier=true, greatMultiplier=true, sufficientNu = true;
        
        System.out.println("\nSummary comments: ");
        for (int i = 1; i < 6; i++) {
            if(mu[i]<0.1) {
                goodMultiplier = false;
                System.out.println("mu["+(i+1)+"] is less than 0.1");
            }
            if(mu[i]<1)
                greatMultiplier = false;
        }
        
        if(greatMultiplier)
            System.out.println("The multiplier is a really good one.");
        else if(goodMultiplier)
            System.out.println("The multiplier meets minimum requirements");
        else
            System.out.println("The multiplier is not good enough");
        
        
        for (int i = 1; i < 6; i++) {
            if(nu[i]<Math.pow(2, 30/(i+1)))
                sufficientNu = false;
        }
        
        if(sufficientNu)
            System.out.println("nu values for dimensions 2 through 6 are quite good for most applications.");
        else
            System.out.println("nu values for some dimensions are low. LCG may not be suitable for your specific application");
            
    }
    
    /**
     * Divides and rounds the quotient of the operation a divided by b, when b is positive
     * If a and b were ints/longs this would be equivalent to Math.round((double)a/b)
     * @param a Dividend
     * @param b Divisor b>0
     * @return BigInteger representing the rounded quotient
     */
    private static BigInteger divideAndRound(BigInteger a, BigInteger b) {
        BigInteger result = a.divide(b);
        //Case 1: Integer part of the quotient is positive, a%b*2>=b, then the float part of the quotient>=.5, therefore add 1 to quotient
        if(result.signum()>0 && a.mod(b).multiply(new BigInteger("2")).compareTo(b)>=0)
            result = result.add(BigInteger.ONE);
        //Case 2: Integer part of the quotient is negative, a%b*2<=b but a%b<>0 , 
        //then the float part of the quotient<=-0.5, therefore subtract 1 from quotient
        else if(result.signum()<0 && a.mod(b).multiply(new BigInteger("2")).compareTo(b)<=0 && !a.mod(b).equals(BigInteger.ZERO))
            result = result.subtract(BigInteger.ONE);
        //Case 3: Integer part of the quotient is zero then two cases arise: a>0 or a<0
        else if(result.signum()==0)
        {   
            //Case 1.1: a>0.5 then result=1
            if(a.compareTo(BigInteger.ZERO)>0 && a.mod(b).multiply(new BigInteger("2")).compareTo(b)>=0)
                result = BigInteger.ONE;
            //Case 1.1: a<-0.5 then result=-1
            else if(a.compareTo(BigInteger.ZERO)<0 && a.mod(b).multiply(new BigInteger("2")).compareTo(b)<=0)
                result = BigInteger.ZERO.subtract(BigInteger.ONE);  //-1 :)
        }
        return result;
    }
    
    /**
     * Calculates factorial of whole numbers and multiples of 1/2
     * @param x
     * @return the factorial
     */
    private double factorial(double x) {
        double fact=1;
        for (double i = x; i > 0; i=i-1) {
            fact = fact*i;
        }
        if(((int)x)!=x) {
            fact=fact*SQRTPI;
        }
        return fact;
    }

}
