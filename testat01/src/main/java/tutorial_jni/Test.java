package main.java.tutorial_jni;

// javac -h ../../../target/Headers tutorial_jni.Test.java
// to create header files in 'target/Headers'.

// In run configuration: VM options:
// -Djava.library.path="D:\00_fhnw\repo_prcpp\vs_projekte\testat01\PRCPP\x64\Debug"


public class Test {

    public static native void display();

    public static native int increment(int value);

    static {
        System.loadLibrary("NativeFunctions");
    }

    public static void main(String[] args) {
        display();
        System.out.println(increment(10));
    }

}