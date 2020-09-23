package main.java.tutorial_jni;

import java.util.Arrays;

public class TestNativePoint {

    private static native int  adjustMemory(int input);
    private        native void adjustArrayValue(double[] input);

    static { System.loadLibrary("MemoryWriter"); }

    public static void main(String[] args) {

        /*
        System.out.println("----");
        int input = 10;
        int output = adjustMemory(input);
        System.out.println("Output in Java: " + output);
        System.out.println("----");
        */

        TestNativePoint tnp = new TestNativePoint();

        double values[]  = new double[]{ 1.0, 2.0, 3.0 };
        Arrays.stream(values).forEach((v) -> System.out.println("In Java before change: " + v));
        tnp.adjustArrayValue(values);
        Arrays.stream(values).forEach((v) -> System.out.println("Output in Java: " + v));
    }
}
