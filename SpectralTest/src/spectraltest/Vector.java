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
 * Class that supports manipulation of BigInteger vectors
 */
public class Vector {

    private final int dimension;
    private final BigInteger components[];

    /**
     * Creates an empty vector of a specific dimension
     * @param dimension The dimension of the vector to be created
     */
    public Vector(int dimension) {
        this.dimension = dimension;
        this.components = new BigInteger[dimension];
        for (int i = 0; i < components.length; i++) {
            components[i]= BigInteger.ZERO;
        }
    }

    /**
     * Takes an array and creates a vector out of it
     * @param array The input array
     */
    public Vector(BigInteger array[]) {
        dimension = array.length;
        components = array;
    }

    /**
     * Creates an array representation of a vector
     * @return An array representation of a vector
    */
    public BigInteger[] toArray() {
        return components;
    }

    /**
     * Multiplies a vector with a scalar value and returns the resulting vector
     * @param s The scalar value to be multiplied with this vector
     * @return Product of multiplying the scalar with this vector
    */
    public Vector multiplyScalar(BigInteger s) {
        int length = this.dimension;
        BigInteger v1Array[] = this.components;

        Vector result = new Vector(this.dimension);
        for (int i = 0; i < length; i++) {
            result.components[i] = s.multiply(v1Array[i]);
        }
        return result;
    }

    /**
     * Multiplies this vector with another vector and returns the resulting scalar product
     * @param v The vector to be multiplied with this vector
     * @return A scalar product of two vectors
     */
    public BigInteger multiply(Vector v) {
        int length = this.dimension;
        BigInteger v1Array[] = this.components;
        BigInteger v2Array[] = v.components;

        BigInteger scalarProduct = BigInteger.ZERO;
        for (int i = 0; i < length; i++) {
            scalarProduct = scalarProduct.add(v1Array[i].multiply(v2Array[i]));
        }
        return scalarProduct;
    }

    /**
     * Multiplies this vector with itself and returns the resulting scalar product
     * @return A scalar self-product of this vector
     */
    public BigInteger selfProduct() {
        return this.multiply(this);
    }

    /**
     * Adds this vector with another vector and returns the resulting sum vector
     * @param v The vector to be added with this vector
     * @return A vector sum of the two vectors
     */
    public Vector add(Vector v) {
        int length = this.dimension;
        BigInteger v1Array[] = this.components;
        BigInteger v2Array[] = v.components;

        Vector result = new Vector(this.dimension);
        for (int i = 0; i < length; i++) {
            result.components[i] = v1Array[i].add(v2Array[i]);
        }
        return result;
    }

    /**
     * Subtracts a vector from this vector and returns the resulting difference vector
     * @param v The vector to be subtracted from this vector
     * @return A vector denoting difference of the two vectors
     */
    public Vector subtract(Vector v) {
        int length = this.dimension;
        BigInteger v1Array[] = this.components;
        BigInteger v2Array[] = v.components;

        Vector result = new Vector(this.dimension);
        for (int i = 0; i < length; i++) {
            result.components[i] = v1Array[i].subtract(v2Array[i]);
        }
        return result;
    }
}
