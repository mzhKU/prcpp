package matrix_multiplication;


import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import static java.lang.Math.abs;

// Generate header files:
// <path>/<to>/<Matrix.java> $ javac -h <path>/<to>/<output_dir_for_headers>

// In IntelliJ under "Edit Configurations" under VM Options enter:
// -Djava.library.path="D:\00_fhnw\repo_prcpp\vs_projekte\testat01\PRCPP\x64\Release"

public class Matrix {

    private int       nRows;
    private int       nCols;
    private double[] values;

    static { System.loadLibrary("MatrixMultiplication"); }

    native void multiplyC(double[] a, double[] b, double[] r, int m, int n, int o);
    native void powerC(double[] vs, double[] m, int r, int k);

    // Test functions
    native void iterateArrayC(double[] toIterate);
    private void iterateArrayCCaller() {
        double tmp[] = new double[]{4.0, 1.0, 5.0};
        iterateArrayC(tmp);
    }



    /* -------------------------------------------------------- */
    /* ------------ CONSTRUCTORS ------------------------------ */
    /* -------------------------------------------------------- */
    public Matrix(int nRows, int nCols) {
        Random r = new Random();
        this.nRows = nRows;
        this.nCols = nCols;
        this.values = new double[nRows*nCols];
        for (int i=0; i<nRows*nCols; i++) {
            this.values[i] = r.nextDouble();
        }
    }
    public Matrix(int nRows, int nCols, double defaultValue) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.values = new double[nRows*nCols];
        for (int i=0; i<nRows*nCols; i++) {
            this.values[i] = defaultValue;
        }
    }
    public Matrix(int nRows, int nCols, double[] defaultMatrix) {
        this.nRows = nRows;
        this.nCols = nCols;
        this.values = defaultMatrix;
    }
    /* -------------------------------------------------------- */



    /* -------------------------------------------------------- */
    /* ------------ MULTIPLICATION JAVA ----------------------- */
    /* -------------------------------------------------------- */
    // m[i][j] = values[i*m + j]
    public Matrix multiply(Matrix m) {
        double[] resultValues = new double[this.nRows*m.nCols];
        for (int r=0; r<nRows; r++) {
            for (int c=0; c<m.nCols; c++) {
                for (int k=0; k<m.nRows; k++) {
                    resultValues[r*m.nCols+c] += values[r*nCols+k]*m.values[k*m.nCols+c];
                }
            }
        }
        return new Matrix(this.nRows, m.nCols, resultValues);
    }

    public Matrix power(int i) throws IllegalArgumentException {
        if (i == 0) { return new Matrix(this.nRows, this.nCols, setUnitMatrix(this)); }
        if (i == 1) { return this; }
        if (i>1) {
            Matrix originalMatrix = new Matrix(this.nRows, this.nCols, new double[this.values.length]);
            for(int s=0; s<this.values.length; s++) { originalMatrix.values[s] = this.values[s]; }

            Matrix tmp = new Matrix(this.nRows, this.nCols, new double[this.values.length]);
            for(int s=0; s<this.values.length; s++) { tmp.values[s] = this.values[s]; }

            // Starts with k=1 because for POWER=2 only one multiplication is done.
            for (int k=1; k < i; k++) {
                multiplyInPlace(this, originalMatrix, tmp);

                // Swap primitives.
                for (int l=0; l<this.values.length; l++) { this.values[l] = tmp.values[l]; }
            }
            return new Matrix(this.nRows, this.nCols, this.values);
        }
        else {
            // What if i<0?
            throw new IllegalArgumentException("Exponent not allowed negative.");
        }
    }

    private void multiplyInPlace(Matrix a, Matrix b, Matrix accumulated) {
        for (int r=0; r<a.nRows; r++) {
            for (int c=0; c<b.nCols; c++) {
                double cell = 0.0;
                for (int k=0; k<a.nCols; k++) {
                     cell += a.values[r*nCols+k] * b.values[k*b.nCols+c];
                }
                accumulated.values[r*accumulated.nCols+c] = cell;
            }
        }
    }
    /* -------------------------------------------------------- */



    /* -------------------------------------------------------- */
    /* ------------ C++ --------------------------------------- */
    /* -------------------------------------------------------- */
    public Matrix multiplyCpp(Matrix m) {
        // m: Rows, n: Cols
        double[] resultValues = new double[this.nRows*m.nCols];
        for (int i=0; i<resultValues.length; i++) {
            resultValues[i] = 0.0;
        }
        this.multiplyC(this.values, m.values, resultValues, this.nRows, this.nCols, m.nCols);
        return new Matrix(this.nRows, m.nCols, resultValues);
    }

    public Matrix powerCpp(int i) {
        if (i == 0) {
            // Power 0 returns unit matrix.
            return new Matrix(this.nRows, this.nCols, setUnitMatrix(this));
        }
        if (i == 1) {
            return this;
        }
        if (i>1) {
            double[] resultValues = new double[this.values.length];

            this.powerC(this.values, resultValues, this.nRows, i);

            return new Matrix(this.nRows, this.nCols, resultValues);
        } else {
            // What if i<0? Assumption: return current matrix.
            return this;
        }
    }
    /* -------------------------------------------------------- */



    /* -------------------------------------------------------- */
    /* ------------ UTILITIES  -------------------------------- */
    /* -------------------------------------------------------- */
    public double[] getValues() {
        return this.values;
    }
    private double[] setUnitMatrix(Matrix m) {
        double[] tmp = new double[m.values.length];
        for (int r=0; r<this.nRows; r++) {
            for (int c=0; c<this.nCols; c++) {
                if(r==c) {
                    tmp[r*this.nCols+c] = 1.0;
                } else {
                    tmp[r*this.nCols+c] = 0.0;
                }
            }
        }
        return tmp;
    }
    private void printArrayOf(Matrix m) {
        Arrays.stream(m.getValues()).forEach(v -> System.out.println(v));
    }
    /* -------------------------------------------------------- */



    /* -------------------------------------------------------- */
    /* ------------ EQUALS & HASHCODE ------------------------- */
    /* -------------------------------------------------------- */
    @Override
    public boolean equals(Object o) {
        if (this == o)                               { return true;  }
        if (o == null || getClass() != o.getClass()) { return false; }
        Matrix matrix = (Matrix) o;
        for (int i=0; i<this.values.length; i++) {
            if (this.values[i] != matrix.values[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(nRows, nCols);
        result = 31 * result + Arrays.hashCode(getValues());
        return result;
    }
    /* -------------------------------------------------------- */


    public static void main(String[] args) {
        /*
        Matrix A = new Matrix(2, 3, new double[]{2, 3, 5, 1, 2, 3});
        Matrix B = new Matrix(3, 2, new double[]{2, 3, 4, 6, 1, 1});
        // Matrix result = A.multiply(B);
        // Matrix resultShouldBe = new Matrix(2, 2, new double[]{21, 29, 13, 18});
        // System.out.println(A.equals(A));
        A.powerCpp(5);
        */
        Matrix A = new Matrix(2, 3, new double[]{2, 3, 5, 1, 2, 3});
        A.iterateArrayCCaller();
    }
}
