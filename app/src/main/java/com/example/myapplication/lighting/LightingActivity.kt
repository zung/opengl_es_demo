package com.example.myapplication.lighting

import android.annotation.SuppressLint
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.SeekBar
import com.example.myapplication.Direction
import com.example.myapplication.MyGLRenderer
import com.example.myapplication.R
import java.util.*

class LightingActivity : AppCompatActivity() {
    private var glSurfaceView: GLSurfaceView? = null

    val myGLRenderer = MyGLRenderer(this, 3)
    lateinit var timer1: Timer
    lateinit var timer2: Timer
    lateinit var timer3: Timer
    lateinit var timer4: Timer

    var lastX: Float = 0.0f
    var lastY: Float = 0.0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lighting)
        glSurfaceView = findViewById(R.id.gl_surface)

        glSurfaceView?.run {
            setEGLContextClientVersion(3)

            setRenderer(myGLRenderer)
            renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        }

        val seekBar = findViewById<SeekBar>(R.id.seek_bar)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                myGLRenderer.shape?.run {
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
                myGLRenderer.shape?.run {
                    camera.zoom = progress.toFloat()
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
                        myGLRenderer.shape?.run {
                            camera.processButtonPress(Direction.UP)
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
                        myGLRenderer.shape?.run {
                            camera.processButtonPress(Direction.DOWN)
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
                            myGLRenderer.shape?.run {
                                camera.processButtonPress(Direction.LEFT)
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
                            myGLRenderer.shape?.run {
                                camera.processButtonPress(Direction.RIGHT)
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
//                myGLRenderer.shape?.run {
//                    camera.mAngle = rot
//                    glSurfaceView?.requestRender()
//                }
//                rot += 1f
//            }
//
//        }, 100, 10)

        glSurfaceView?.setOnTouchListener { v, e ->
            when(e.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = e.x
                    lastY = e.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val xoffset = e.x - lastX
                    val yoffset = lastY - e.y
                    lastX = e.x
                    lastY = e.y
                    myGLRenderer.shape?.run {
                        camera.procesMove(xoffset, yoffset)
                        glSurfaceView?.requestRender()
                    }
                }

                MotionEvent.ACTION_UP -> {
                    lastX = 0.0f
                    lastY = 0.0f
                }
            }

            false
        }
    }
}