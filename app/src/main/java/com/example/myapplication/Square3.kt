package com.example.myapplication

import android.opengl.GLES10
import android.opengl.GLES20
import android.opengl.GLES30
import android.util.Log
import java.nio.*
import javax.microedition.khronos.opengles.GL

class Square3 {
    private val vertexShaderCode =
        "#version 300 es\n" +
        "layout(location=0) in vec3 aPos;\n" +
        "void main() {\n" +
        "   gl_Position = vec4(aPos, 1.0);\n" +
        "}\n"

    private val fragmentShaderCode =
        "#version 300 es\n" +
        "out vec4 FragColor;\n" +
        "uniform vec4 ourColor;\n" +
        "void main() {\n" +
        "    FragColor = ourColor;\n" +
        "}\n"

    private var vertices = floatArrayOf(
        0.5f, 0.5f, 0.0f,       // top right
        0.5f, -0.5f, 0.0f,      // bottom right
        -0.5f, -0.5f, 0.0f,     // bottom left
        -0.5f, 0.5f, 0.0f       // top left
    )
    //EBO
    private var indices = intArrayOf(
        0, 1, 3,
        1, 2, 3
    )

    var green: Float = 0.0f

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
    var indicesBuffer : IntBuffer =
        ByteBuffer.allocateDirect(indices.size * 4).run {
            order(ByteOrder.nativeOrder())
            asIntBuffer().apply {
                put(indices)
                position(0)
            }
        }
    private var mProgram: Int

    var VAO = IntArray(1)
    var VBO = IntArray(1)
    var EBO = IntArray(1)

    init {
        val vertexShader: Int = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES30.glCreateProgram().also {

            // add the vertex shader to program
            GLES30.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES30.glAttachShader(it, fragmentShader)
//            GLES30.glBindAttribLocation(it, 0, "aPos")
            // creates OpenGL ES program executables
            GLES30.glLinkProgram(it)
        }

        GLES30.glGenVertexArrays(1, VAO, 0)
        GLES30.glGenBuffers(1, VBO, 0)
        GLES30.glGenBuffers(1, EBO, 0)

        GLES30.glBindVertexArray(VAO[0])

        //bind vbo
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[0])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.size * 4, vertexBuffer, GLES30.GL_STATIC_DRAW)

        //bind ebo
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, EBO[0])
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, indices.size * 4, indicesBuffer, GLES30.GL_STATIC_DRAW)

        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, 0)
        GLES30.glEnableVertexAttribArray(0)

        //unbind
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
        GLES30.glBindVertexArray(0)

    }

    fun draw() {

        // Add program to OpenGL ES environment
        GLES30.glUseProgram(mProgram)

        GLES30.glGetUniformLocation(mProgram, "ourColor").also {
            GLES30.glUniform4f(it, 0.0f, green, 0.0f, 1.0f)
        }

        GLES30.glBindVertexArray(VAO[0])
//        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6)
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, 0)
//        GLES30.glDrawElements(GLES30.GL_LINE_LOOP, 6, GLES30.GL_UNSIGNED_INT, 0)

    }

    fun loadShader(type: Int, shaderCode: String): Int {

        // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
        return GLES30.glCreateShader(type).also { shader ->

            // add the source code to the shader and compile it
            GLES30.glShaderSource(shader, shaderCode)
            GLES30.glCompileShader(shader)
            val status = IntArray(1)
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0)
            if (status[0] == 0) {
                Log.e("Triangle3", "Could not compile shader: " + GLES20.glGetShaderInfoLog(shader))
                GLES20.glDeleteShader(shader)
            }
        }
    }

}
