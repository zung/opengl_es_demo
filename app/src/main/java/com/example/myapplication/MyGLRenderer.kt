package com.example.myapplication

import android.content.Context
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class MyGLRenderer : GLSurfaceView.Renderer {
    private lateinit var mTriangle: Triangle
    private lateinit var mSquare: Square2
    lateinit var points: Points
    lateinit var mTriangle2: Triangle2
    var mTriangle3: Triangle3? = null
    var shape: Shape? = null
    var mContext: Context? = null
    var mType: Any?

    constructor(context: Context, clazz: Any) {
        mContext = context
        mType = clazz
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        //打开深度检测
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        //打开背面裁剪
//        GLES30.glEnable(GLES30.GL_CULL_FACE)

        // initialize a triangle
//        mTriangle = Triangle()
        // initialize a square
//        mSquare = Square2()
//        points = Points()
//        mTriangle2 = Triangle2()
        shape = ShapeFactory(mContext!!).createShape(mType!!)
    }

    override fun onDrawFrame(unused: GL10) {
        shape?.draw()

    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }


}
