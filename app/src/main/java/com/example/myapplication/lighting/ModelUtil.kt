package com.example.myapplication.lighting

/**
 *
 * @ClassName:      ModelUtil
 * @Description:    类作用描述
 * @Author:         czg
 * @CreateDate:     2021/3/10 15:40
 */
class ModelUtil {
    init {
        System.loadLibrary("glmodel")
    }

    external fun loadModel(): Int

    external fun opencvVersion():String
}