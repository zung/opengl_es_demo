package com.example.myapplication.lighting

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.Matrix
import android.renderscript.Float3
import android.util.Log
import com.example.myapplication.Camera
import com.example.myapplication.ShaderUtils
import java.nio.*


class TestLighting(mContext: Context?) {
    var mFovy: Float? = 90.0f

    var camera: Camera = Camera(Float3(0.0f, 0.0f, 3.0f))
    var lightPos = floatArrayOf(1.2f, 1.0f, 2.0f)

    private var vertices = floatArrayOf(
        -0.5f, -0.5f, -0.5f,
        0.5f, -0.5f, -0.5f,
        0.5f,  0.5f, -0.5f,
        0.5f,  0.5f, -0.5f,
        -0.5f,  0.5f, -0.5f,
        -0.5f, -0.5f, -0.5f,

        -0.5f, -0.5f,  0.5f,
        0.5f, -0.5f,  0.5f,
        0.5f,  0.5f,  0.5f,
        0.5f,  0.5f,  0.5f,
        -0.5f,  0.5f,  0.5f,
        -0.5f, -0.5f,  0.5f,

        -0.5f,  0.5f,  0.5f,
        -0.5f,  0.5f, -0.5f,
        -0.5f, -0.5f, -0.5f,
        -0.5f, -0.5f, -0.5f,
        -0.5f, -0.5f,  0.5f,
        -0.5f,  0.5f,  0.5f,

        0.5f,  0.5f,  0.5f,
        0.5f,  0.5f, -0.5f,
        0.5f, -0.5f, -0.5f,
        0.5f, -0.5f, -0.5f,
        0.5f, -0.5f,  0.5f,
        0.5f,  0.5f,  0.5f,

        -0.5f, -0.5f, -0.5f,
        0.5f, -0.5f, -0.5f,
        0.5f, -0.5f,  0.5f,
        0.5f, -0.5f,  0.5f,
        -0.5f, -0.5f,  0.5f,
        -0.5f, -0.5f, -0.5f,

        -0.5f,  0.5f, -0.5f,
        0.5f,  0.5f, -0.5f,
        0.5f,  0.5f,  0.5f,
        0.5f,  0.5f,  0.5f,
        -0.5f,  0.5f,  0.5f,
        -0.5f,  0.5f, -0.5f
    )

    //EBO
    private var indices = intArrayOf(
        0, 1, 3,
        1, 2, 3
    )

    var vertexBuffer: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(vertices.size * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(vertices)
                // set the buffer to read the first coordinate
                position(0)
            }
        }
    var objectShader: ShaderUtils? = null
    var lightShader: ShaderUtils? = null

    var VAO = IntArray(1)
    var VBO = IntArray(1)
    var lightVAO = IntArray(1)

    init {
        objectShader = ShaderUtils(mContext!!).also {
            it.loadShaderSource("objectVertexShader.vs", "objectFragmentShader.fs")
        }

        lightShader = ShaderUtils((mContext)).also {
            it.loadShaderSource("lightVertexShader.vs", "lightFragmentShader.fs")
        }

        GLES30.glGenVertexArrays(1, VAO, 0)

        GLES30.glGenBuffers(1, VBO, 0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[0])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, 4 * vertices.size, vertexBuffer, GLES30.GL_STATIC_DRAW)

        GLES30.glBindVertexArray(VAO[0])

        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 12, 0)
        GLES30.glEnableVertexAttribArray(0)

        //light
        GLES30.glGenVertexArrays(1, lightVAO, 0)
        GLES30.glBindVertexArray(lightVAO[0])

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[0])

        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 12, 0)
        GLES30.glEnableVertexAttribArray(0)

        //unbind
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
        GLES30.glBindVertexArray(0)

    }

    fun draw() {
        // Add program to OpenGL ES environment
        objectShader?.use()

        val projection = FloatArray(16)
        Matrix.setIdentityM(projection, 0)
        Matrix.perspectiveM(projection, 0, mFovy!!, 4.0f / 3.0f, 0.1f, 100.0f)

        val model = FloatArray(16)
        Matrix.setIdentityM(model, 0)

        objectShader?.setMatrix4f("projection", projection)
        objectShader?.setMatrix4f("view", camera.getView())
        objectShader?.setMatrix4f("model", model)
        objectShader?.setVec3("objectColor", 1.0f, 0.5f, 0.31f)
        objectShader?.setVec3("lightColor", 1.0f, 1.0f, 1.0f)

        GLES30.glBindVertexArray(VAO[0])
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36)

        //light
        lightShader?.run {
            use()
            setMatrix4f("projection", projection)
            setMatrix4f("view", camera.getView())
            Matrix.translateM(model, 0, lightPos[0], lightPos[1], lightPos[2])
            Matrix.scaleM(model, 0, 0.2f, 0.2f, 0.2f)
            setMatrix4f("model", model)
        }

        GLES30.glBindVertexArray(lightVAO[0])
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36)
    }
}
