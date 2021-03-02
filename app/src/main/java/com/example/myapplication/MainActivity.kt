package com.example.myapplication

import android.annotation.SuppressLint
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.renderscript.Matrix4f
import android.view.MotionEvent
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import java.util.*
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
    lateinit var timer1: Timer
    lateinit var timer2: Timer
    lateinit var timer3: Timer
    lateinit var timer4: Timer

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

        val seekFov = findViewById<SeekBar>(R.id.seek_fov)
        seekFov.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                myGLRenderer.mSquare3?.run {
                    mFovy = progress.toFloat()
                    glSurfaceView?.requestRender()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        val btnUp = findViewById<Button>(R.id.btn_up)
        val btnLeft = findViewById<Button>(R.id.btn_left)
        val btnRight = findViewById<Button>(R.id.btn_right)
        val btnDown = findViewById<Button>(R.id.btn_down)

        btnUp.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                timer1 = Timer()
                timer1.schedule(object : TimerTask() {
                    override fun run() {
                        myGLRenderer.mSquare3?.run {
                            cpos = VectorUtils.add(cpos, VectorUtils.mul(cfront, 0.05f))
                            glSurfaceView?.requestRender()
                        }
                    }
                }, 0, 20)
            } else if (event.action == MotionEvent.ACTION_UP) {
                timer1.cancel()
            }
            false
        }
        btnDown.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                timer2 = Timer()
                timer2.schedule(object : TimerTask() {
                    override fun run() {
                        myGLRenderer.mSquare3?.run {
                            cpos = VectorUtils.sub(cpos, VectorUtils.mul(cfront, 0.05f))
                            glSurfaceView?.requestRender()
                        }
                    }
                }, 0, 20)

            } else if (event.action == MotionEvent.ACTION_UP) {
                timer2.cancel()
            }
            false
        }

        btnLeft.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    timer3 = Timer()
                    timer3.schedule(object : TimerTask() {
                        override fun run() {
                            myGLRenderer.mSquare3?.run {
                                cpos = VectorUtils.sub(cpos, VectorUtils.mul(VectorUtils.normalize(VectorUtils.cross(cfront, up)), 0.05f))
                                glSurfaceView?.requestRender()
                            }
                        }
                    }, 0, 20)
                }
                MotionEvent.ACTION_UP -> {
                    timer3.cancel()
                }
            }
            false
        }

        btnRight.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    timer4 = Timer()
                    timer4.schedule(object : TimerTask() {
                        override fun run() {
                            myGLRenderer.mSquare3?.run {
                                cpos = VectorUtils.add(cpos, VectorUtils.mul(VectorUtils.normalize(VectorUtils.cross(cfront, up)), 0.05f))
                                glSurfaceView?.requestRender()
                            }
                        }
                    }, 0, 20)
                }
                MotionEvent.ACTION_UP -> {
                    timer4.cancel()
                }
            }
            false
        }

        var rot = 0.0f
        var rot2 = 0.0f
//        Timer().schedule(object : TimerTask() {
//            override fun run() {
//                myGLRenderer.mSquare3?.run {
//                    mAngle = rot
//                    glSurfaceView?.requestRender()
//                }
//                rot += 0.01f
//            }
//
//        }, 100, 10)
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