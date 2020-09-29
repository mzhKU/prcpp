package matrix_multiplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.*;

class MatrixPerformanceTests {
    Matrix a, b, c, d, p, q, k;

    @BeforeEach
    void setUp() throws Exception {
        k = new Matrix(2, 2, new double[] {1, 2, 3, 4});
        a = new Matrix(3, 5, new double[] {1, 2, 3, 4, 5, 2, 3, 4, 5, 6, 3, 4, 5, 6, 7});
        b = new Matrix(5, 2, new double[] {1,2,3,4,5,6,7,8,9,0});
        c = new Matrix(3, 2, new double[] {95,60,120,80,145,100});
        d = new Matrix(3, 3, new double[] {1,0,0,0,1,0,0,0,1});
        p = new Matrix(3, 3, new double[] {1,2,3,4,5,6,7,8,9});
        q = new Matrix(3, 3, new double[] {31644432,38881944,46119456,71662158,88052265,104442372,111679884,137222586,162765288});
    }

    @Test
    void testEquals() {
        Matrix r1 = new Matrix(3,3);
        Matrix r2 = new Matrix(3,3);
        Matrix zeros = new Matrix(5,5,0);
        Matrix ones = new Matrix(5,5,1);

        assertTrue(a.equals(a));
        assertTrue(p.equals(new Matrix(3,3, new double[] {1,2,3,4,5,6,7,8,9})));
        assertFalse(a.equals(b));
        assertFalse(a.equals(null));
        assertFalse(r1.equals(r2));

        assertFalse(ones.equals(zeros));
    }

    @Test
    void multiply500x400() {
        Matrix A = new Matrix(500, 6000);
        Matrix B = new Matrix(6000, 400);

        long initJava = System.currentTimeMillis();
        Matrix resultJava = A.multiply(B);
        long finJava  = System.currentTimeMillis();

        long initCpp = System.currentTimeMillis();
        Matrix resultCpp = A.multiplyCpp(B);
        long finCpp = System.currentTimeMillis();

        System.out.println("Java: " + (finJava - initJava) + " milliseconds");
        System.out.println("C++:  " + (finCpp - initCpp)   + " milliseconds");

        assertTrue(resultJava.equals(resultCpp));

        /*
         * Performance metrics for Debug version DLL
         * (VM options "-Djava.library.path="D:\00_fhnw\repo_prcpp\vs_projekte\testat01\PRCPP\x64\Debug")
         * - Java:  6880 milliseconds
         * - C++:  15986 milliseconds
         *
         * Performance metrics for Release version DLL
         * (VM options "-Djava.library.path="D:\00_fhnw\repo_prcpp\vs_projekte\testat01\PRCPP\x64\Release")
         * - Java: 6488 seconds
         * - C++:  5947 seconds
         */
    }

    @Test
    void testM250Power93() {
        int POW = 93; int SIZE = 250;
        // Matrix A1 = new Matrix(SIZE, SIZE, new double[]{1, 2, 3, 4.0, 5, 6, 7, 8, 9});
        Matrix A1 = new Matrix(SIZE, SIZE);

        // Make sure both matrices have the same values.
        double a2Values[] = new double[SIZE*SIZE];
        for(int i=0; i<a2Values.length; i++) { a2Values[i] = A1.getValues()[i]; }
        Matrix A2 = new Matrix(SIZE, SIZE, a2Values);

        long initJava = System.currentTimeMillis();
        Matrix resultJava = A1.power(POW);
        long finJava  = System.currentTimeMillis();

        long initCpp = System.currentTimeMillis();
        Matrix resultCpp = A2.powerCpp(POW);
        long finCpp = System.currentTimeMillis();

        System.out.println("Java: " + (finJava - initJava) + " Milliseconds");
        System.out.println("C++:  " + (finCpp - initCpp)   + " Milliseconds");
        System.out.println();

        // printArrayOf(A1); printArrayOf(A2);

        /*
         * Performance metrics for Release version DLL
         * (VM options "-Djava.library.path="D:\00_fhnw\repo_prcpp\vs_projekte\testat01\PRCPP\x64\Debug")
         * - Java:  xx seconds
         * - C++:   xx seconds
         *
         * Performance metrics for Debug version DLL
         * (VM options "-Djava.library.path="D:\00_fhnw\repo_prcpp\vs_projekte\testat01\PRCPP\x64\Release")
         * - Java: 2828 Milliseconds
         * - C++:  1873 Milliseconds
         *
         */

        assertEquals(resultJava, resultCpp);
    }

    private void printArrayOf(Matrix m) {
        Arrays.stream(m.getValues()).forEach(v -> System.out.println(v));
    }
}