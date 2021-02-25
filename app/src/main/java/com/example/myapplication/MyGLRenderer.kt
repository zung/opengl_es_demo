package com.example.myapplication

import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL
import javax.microedition.khronos.opengles.GL10


class MyGLRenderer : GLSurfaceView.Renderer {
    private lateinit var mTriangle: Triangle
    private lateinit var mSquare: Square2
    lateinit var points: Points
    lateinit var mTriangle2: Triangle2
    lateinit var mTriangle3: Triangle3
    lateinit var mSquare3: Square3
    @Volatile
    var angle: Float = 0f


    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        //打开深度检测
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        //打开背面裁剪
//        GLES30.glEnable(GLES30.GL_CULL_FACE)

        // initialize a triangle
        mTriangle = Triangle()
        // initialize a square
//        mSquare = Square2()
//        points = Points()
//        mTriangle2 = Triangle2()
        mTriangle3 = Triangle3()
        mSquare3 = Square3()
    }

    override fun onDrawFrame(unused: GL10) {
        val scratch = FloatArray(16)
        GLES30.glClearColor(1.0f, 1.0f, 1.0f,1.0f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
//        mTriangle3.draw()
        mSquare3.draw()

//        // Redraw background color
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
//
//        // Set the camera position (View matrix)
//        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
//
//        // Calculate the projection and view transformation
//        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
//
////        mSquare.draw(scratch)
//
//        // Create a rotation transformation for the triangle
////        val time = SystemClock.uptimeMillis() % 4000L
////        val angle = 0.090f * time.toInt()
//        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
//
//        // Combine the rotation matrix with the projection and camera view
//        // Note that the vPMatrix factor *must be first* in order
//        // for the matrix multiplication product to be correct.
//        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)

//        points.draw(scratch)
//        mTriangle.draw(scratch)
//        mSquare.draw(scratch)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }


}
