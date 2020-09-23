package matrix_multiplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {
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
    void testPowerOwnImplementation() {
        assertEquals(p.power(0), d);
        assertEquals(k.power(1), new Matrix(2, 2, new double[]{1, 2, 3, 4}));
        assertEquals(k.power(2), new Matrix(2, 2, new double[]{7, 10, 15, 22}));
        assertEquals(p.power(7), q);
        assertEquals(d.power(2), d);
        assertEquals(d.power(111), d);
    }

    @Test
    void testPowerCppOwnImplementation() {
        assertEquals(p.powerCpp(0), d);
        assertEquals(p.powerCpp(1), p);
        assertEquals(k.powerCpp(2),  new Matrix(2, 2, new double[]{7, 10, 15, 22}));
        assertEquals(p.powerCpp(7), q);
        assertEquals(d.powerCpp(2), d);
        assertEquals(d.powerCpp(111), d);
    }

    @Test
    void testMultiplyRef() {
        Matrix res = a.multiply(b);
        System.out.println(res.getValues());
        System.out.println(c.getValues());
        assertEquals(a.multiply(b), c);
    }

    @Test
    void multiply() {
        Matrix A = new Matrix(2, 3, new double[]{2, 3, 5, 1, 2, 3});
        Matrix B = new Matrix(3, 2, new double[]{2, 3, 4, 6, 1, 1});
        Matrix result = A.multiply(B);
        // Arrays.stream(result.getValues()).forEach((v) -> System.out.println(v));
        Matrix resultShouldBe = new Matrix(2, 2, new double[]{21, 29, 13, 18});
        // Arrays.stream(resultShouldBe.getValues()).forEach((v) -> System.out.println(v));
        assertTrue(result.equals(resultShouldBe));
    }

    @Test
    void multiplyNative() {
        Matrix A = new Matrix(2, 3, new double[]{2, 3, 5, 1, 2, 3});
        Matrix B = new Matrix(3, 2, new double[]{2, 3, 4, 6, 1, 1});
        Matrix result = A.multiplyCpp(B);
        // Arrays.stream(result.getValues()).forEach((v) -> System.out.println(v));
        Matrix resultShouldBe = new Matrix(2, 2, new double[]{21, 29, 13, 18});
        // Arrays.stream(resultShouldBe.getValues()).forEach((v) -> System.out.println(v));
        assertTrue(result.equals(resultShouldBe));
    }

    @Test
    void multiplyLarge() {
        Matrix A = new Matrix(500, 6000);
        Matrix B = new Matrix(6000, 400);

        long initJava = System.currentTimeMillis();
        Matrix resultJava = A.multiply(B);
        long finJava  = System.currentTimeMillis();

        long initCpp = System.currentTimeMillis();
        Matrix resultCpp = A.multiplyCpp(B);
        long finCpp = System.currentTimeMillis();

        System.out.println("Java: " + (finJava - initJava) + " seconds");
        System.out.println("C++:  " + (finCpp - initCpp)   + " seconds");

        assertTrue(resultJava.equals(resultCpp));
    }

    @Test
    void testDistance() {
        System.out.println(abs(1.0749095452714203E19 - 1.0749095452714207E19)  < 0.000_000_001);
    }

    @Test
    void testM250Power93() {
        int POW = 2;
        int SIZE = 2;
        Matrix A1 = new Matrix(SIZE, SIZE, new double[]{6, 2, 3, 4});

        /*
        Reference values:
        POW=10, SIZE=2:
        { (4783807  | 6972050
           10458075 | 15241882) }
        */

        long initJava = System.currentTimeMillis();
        Matrix resultJava = A1.power(POW);
        long finJava  = System.currentTimeMillis();

        long initCpp = System.currentTimeMillis();
        Matrix resultCpp = A1.powerCpp(POW);
        long finCpp = System.currentTimeMillis();

        // System.out.println("Java: " + (finJava - initJava) + " Milliseconds");
        System.out.println("C++:  " + (finCpp - initCpp)   + " Milliseconds");
        System.out.println();

        Arrays.stream(resultCpp.getValues()).forEach(v -> System.out.println(v));

        // assertEquals(resultJava, resultCpp);
    }

    @Test
    void testPowerLargeCppCustoms() {
        /*
        Sample output for POW=3, SIZE=30:
        Java: 3 Milliseconds
        C++:  0 Milliseconds

        The differences are microscopic but this is the reason why the matrices are not equal.
        -1.4210854715202004E-14
        -1.4210854715202004E-14
        0.0
        -1.4210854715202004E-14
         */

        int POW = 3;
        int SIZE = 30;

        Matrix A1 = new Matrix(SIZE, SIZE);

        long initJava = System.currentTimeMillis();
        Matrix resultJava = A1.power(POW);
        long finJava  = System.currentTimeMillis();

        long initCpp = System.currentTimeMillis();
        Matrix resultCpp = A1.powerCpp(POW);
        long finCpp = System.currentTimeMillis();

        System.out.println("Java: " + (finJava - initJava) + " Milliseconds");
        System.out.println("C++:  " + (finCpp - initCpp)   + " Milliseconds");
        System.out.println();

        for(int i = 0; i<resultJava.getValues().length; i++) {
            System.out.println(resultJava.getValues()[i]-resultCpp.getValues()[i]);
        }

        assertEquals(resultJava, resultCpp);

    }
}