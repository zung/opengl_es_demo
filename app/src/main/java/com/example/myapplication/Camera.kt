package com.example.myapplication

import android.opengl.Matrix
import android.renderscript.Float3
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.cosh
import kotlin.math.sin

/**
 *
 * @ClassName:      Camera
 * @Description:    类作用描述
 * @Author:         czg
 * @CreateDate:     2021/3/2 15:34
 */

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

val YAW = -90.0f
val PITCH = 0.0f
val SENSITIVITY = 0.1f
val ZOOM = 45.0f

class Camera {

    var mAngle: Float? = 0.0f

    var cpos: Float3
    var cfront: Float3
    lateinit var up: Float3
    val speed = 0.05f
    lateinit var right: Float3
    val worldUp: Float3

    var yaw: Float
    var pitch: Float
    var movementSpeed: Float = 0.0f
    var sensitivity: Float
    var zoom: Float = ZOOM

    constructor(position: Float3 = Float3(0.0f, 0.0f, 0.0f),
        cup: Float3 = Float3(0.0f, 1.0f, 0.0f),
        front: Float3 = Float3(0.0f, 0.0f, -1.0f),
        yaw: Float = YAW, pitch: Float = PITCH, sensitivity: Float = SENSITIVITY) {
        cpos = position
        worldUp = cup
        cfront = front

        this.yaw = yaw
        this.pitch = pitch
        this.sensitivity = sensitivity
        updateCameraVectors()
    }

    fun getRorateVec3() : Float3 {
        val radius = 10.0f
        val cX = sin(mAngle!!) * radius
        val cZ = cos(mAngle!!) * radius

        return Float3(cX, 0.0f, cZ)
    }

    fun getView(): FloatArray {

        val center = VectorUtils.add(cpos, cfront)

        val view = FloatArray(16)
        Matrix.setIdentityM(view, 0)

        Matrix.setLookAtM(view, 0,
            cpos.x, cpos.y, cpos.z,
            center.x, center.y, center.z,
            up.x, up.y, up.z)

        return view
    }

    fun processButtonPress(direction: Direction) {
        when(direction) {
            Direction.UP -> {
                cpos = VectorUtils.add(cpos, VectorUtils.mul(cfront, speed))
            }

            Direction.DOWN -> {
                cpos = VectorUtils.sub(cpos, VectorUtils.mul(cfront, speed))
            }

            Direction.LEFT -> {
                cpos = VectorUtils.sub(
                    cpos,
                    VectorUtils.mul(
                        VectorUtils.normalize(
                            VectorUtils.cross(
                                cfront,
                                up
                            )
                        ),
                        speed
                    )
                )
            }

            Direction.RIGHT -> {
                cpos = VectorUtils.add(
                    cpos,
                    VectorUtils.mul(
                        VectorUtils.normalize(
                            VectorUtils.cross(cfront, up)
                        ),
                        speed
                    )
                )
            }
        }
    }

    fun procesMove(xoffset: Float, yoffset: Float, constrainPitch: Boolean = true) {
        xoffset.also {
            yaw += it * sensitivity
        }

        yoffset.also {
            pitch += it * sensitivity
        }

        if (constrainPitch) {
            if (pitch > 89.0f) pitch = 89.0f
            if (pitch < -89.0f) pitch = -89.0f
        }

        updateCameraVectors()
    }

    fun updateCameraVectors() {
        val front = Float3()
        front.x = cos(radians(yaw)) * cos(radians(pitch))
        front.y = sin(radians(pitch))
        front.z = sin(radians(yaw)) * cos(radians(pitch))
        cfront = VectorUtils.normalize(front)
        right = VectorUtils.normalize(VectorUtils.cross(cfront, worldUp))
        this.up = VectorUtils.normalize(VectorUtils.cross(right, cfront))
    }

    fun radians(angle: Float) : Float {
        return (angle * PI / 180.0f).toFloat()
    }
}