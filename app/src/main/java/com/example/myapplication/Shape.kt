package com.example.myapplication

import android.opengl.GLES30
import android.renderscript.Float3

/**
 *
 * @ClassName:      com.example.myapplication.Shape
 * @Description:    类作用描述
 * @Author:         czg
 * @CreateDate:     2021/3/10 10:35
 */
open class Shape {
    var mFovy: Float? = 45.0f
    var mVisible: Float? = 0.2f
    var camera: Camera = Camera(Float3(0.0f, 0.0f, 3.0f))

    open fun draw() {}
}