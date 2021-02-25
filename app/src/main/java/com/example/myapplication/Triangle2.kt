package com.example.myapplication

import android.opengl.GLES20
import android.opengl.GLES30
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import javax.microedition.khronos.opengles.GL

class Triangle2 {

    private val fragmentShaderCode =
            "out vec4 FragColor;\n" +
            "in vec3 ourColor;\n" +
            "void main() {\n" +
            "    FragColor = vec4(ourColor, 1.0f);\n" +
            "}"

    private val vertexShaderCode =
            "layout (location = 0) in vec3 aPos;\n" +
            "layout (location = 1) in vec3 aColor;\n" +
            "out vec3 ourColor;\n" +
            "void main() {\n" +
            "   gl_Position = vec4(aPos, 1.0f);\n" +
            "   ourColor = aColor;\n" +
            "}"

    private var mProgram: Int
    private lateinit var vertices: FloatArray
    var VAO = IntArray(1)
    var VBO = IntArray(1)

    var vaoBuffer = IntBuffer.allocate(100)
    var vboBuffer = IntBuffer.allocate(100)
    var va : Int = 0
    var vb : Int = 0

    init {
        val vertexShader: Int = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES30.glCreateProgram().also {

            // add the vertex shader to program
            GLES30.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES30.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES30.glLinkProgram(it)
        }
        GLES30.glDeleteShader(vertexShader)
        GLES30.glDeleteShader(fragmentShader)
        vertices = floatArrayOf(
            0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f,      // bottom right
            -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f,    // bottom left
            0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f     // top
        )
        GLES30.glGenVertexArrays(1, vaoBuffer)
        GLES30.glGenBuffers(1, vboBuffer)

        va = vaoBuffer.get()
        vb = vboBuffer.get()
        GLES30.glBindVertexArray(va)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vb)
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.size, FloatBuffer.wrap(vertices), GLES30.GL_STATIC_DRAW)

        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 6 * 4, 0)
        GLES30.glEnableVertexAttribArray(0)

        GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false, 6 * 4, 3 * 4)
        GLES30.glEnableVertexAttribArray(1)
    }

    fun draw() {



        // Add program to OpenGL ES environment
        GLES30.glUseProgram(mProgram)
        GLES30.glBindVertexArray(va)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3)
    }

    fun loadShader(type: Int, shaderCode: String): Int {

        // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
        return GLES30.glCreateShader(type).also { shader ->

            // add the source code to the shader and compile it
            GLES30.glShaderSource(shader, shaderCode)
            GLES30.glCompileShader(shader)
        }
    }

}
