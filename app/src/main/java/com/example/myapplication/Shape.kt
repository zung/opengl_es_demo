package com.example.myapplication

import android.content.Context
import android.opengl.GLES30
import android.renderscript.Float3
import kotlin.math.PI

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
    var camera: Camera = Camera(Float3(0.0f, 0.0f, 2.0f))

    open fun draw() {}

    fun radians(angle: Float) : Float {
        return (angle * PI / 180.0f).toFloat()
    }

    fun loadTexture(context: Context, resId: Int): Int {
        val TBO = IntArray(1)
        val bitmap = BitmapUtils.getBitmap(context, resId)
        TBO[0] = GlUtil.createImageTexture(bitmap)

        bitmap?.recycle()
        return TBO[0]
    }
}