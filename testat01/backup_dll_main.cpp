// dllmain.cpp : Defines the entry point for the DLL application.
#include "pch.h"
#include "matrix_multiplication_Matrix.h"
#include <iostream>

// Output of .dll files: D:\00_fhnw\repo_prcpp\vs_projekte\testat01\PRCPP\x64

using namespace std;

BOOL APIENTRY DllMain(HMODULE hModule, DWORD ul_reason_for_call, LPVOID lpReserved) {
    switch (ul_reason_for_call)
    {
    case DLL_PROCESS_ATTACH:
    case DLL_THREAD_ATTACH:
    case DLL_THREAD_DETACH:
    case DLL_PROCESS_DETACH:
        break;
    }
    return TRUE;
}

JNIEXPORT void JNICALL Java_matrix_1multiplication_Matrix_multiplyC(JNIEnv* env, jobject,
    jdoubleArray aValues, jdoubleArray bValues, jdoubleArray result,
    jint           aRows, jint           aCols, jint          bCols) {

    // 'env' allows access to the JVM, allows to call special JNI functions.
    // 'jobject' or 'jclass' represents the caller of the native method.
    double* pa = env->GetDoubleArrayElements(aValues, JNI_FALSE); // Pointer to aValues[0]
    double* pb = env->GetDoubleArrayElements(bValues, JNI_FALSE); // Pointer to bValues[0]
    double* pr = env->GetDoubleArrayElements(result,  JNI_FALSE);  // Pointer to result[0]

    for (size_t r=0; r<aRows; r++) {
        for (size_t c = 0; c < bCols; c++) {
            // double tmp = 0;
            for (size_t k = 0; k<aCols; k++) {
                *(pr + r * bCols + c) += *(pa + r * aCols + k) * *(pb + k * bCols + c);
            }
        }
    }

    env->ReleaseDoubleArrayElements(aValues, pa, 0);
    env->ReleaseDoubleArrayElements(bValues, pb, 0);
    env->ReleaseDoubleArrayElements(result, pr, 0);
}


JNIEXPORT void JNICALL Java_matrix_1multiplication_Matrix_powerC(JNIEnv* env, jobject o, jdoubleArray vals, jdoubleArray res, jint i) {
    cout << "Hello from powerCxxx" << endl;
}
