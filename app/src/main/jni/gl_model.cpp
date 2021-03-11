//
// Created by Administrator on 2021/3/10.
//

#include "gl_model.h"
#include "opencv2/opencv.hpp"

extern "C"
JNIEXPORT jint JNICALL
Java_com_example_myapplication_lighting_ModelUtil_loadModel(JNIEnv
* env,
jobject thiz
) {
    return 2323;
}

extern "C"

JNIEXPORT jstring JNICALL
Java_com_example_myapplication_lighting_ModelUtil_opencvVersion(JNIEnv *env, jobject thiz) {

    return env->NewStringUTF(CV_VERSION);
}