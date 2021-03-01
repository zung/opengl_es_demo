package com.example.myapplication

import android.annotation.SuppressLint
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.sin


class MainActivity : AppCompatActivity() {
    private var glSurfaceView: GLSurfaceView? = null

    private val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f
    private var previousX: Float = 0f
    private var previousY: Float = 0f
    lateinit var points: Points
    private val vPMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    val myGLRenderer = MyGLRenderer(this)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        glSurfaceView = findViewById(R.id.gl_surface)
        glSurfaceView?.run {
            setEGLContextClientVersion(3)

            setRenderer(myGLRenderer)
            renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        }

        val seekBar = findViewById<SeekBar>(R.id.seek_bar)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                myGLRenderer.mSquare3?.run {
                    mVisible = progress / 100.0f
                    glSurfaceView?.requestRender()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
//        glSurfaceView?.run {
//            cameraTextureListener = object : CameraGLSurfaceView.CameraTextureListener {
//                override fun onCameraViewStarted(width: Int, height: Int) {
//                }
//
//                override fun onCameraViewStopped() {
//                }
//
//                override fun onCameraTexture(
//                    texIn: Int,
//                    texOut: Int,
//                    width: Int,
//                    height: Int
//                ): Boolean {
//                    points = Points()
//                    val scratch = FloatArray(16)
//
//                    // Redraw background color
//                    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
//
//                    // Set the camera position (View matrix)
//                    Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
//
//                    points.draw(viewMatrix)
//                    return false
//                }
//
//            }
//        }
    }
}