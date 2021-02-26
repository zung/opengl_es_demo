package com.example.myapplication

import android.opengl.GLES20
import android.opengl.GLES30
import android.util.Log
import java.nio.*
import javax.microedition.khronos.opengles.GL

class Triangle3 {
    private val vertexShaderCode =
        "#version 300 es\n" +
                "layout (location = 0) in vec3 aPos;\n" +
                "layout (location = 1) in vec3 aColor;\n" +
                "out vec3 ourColor;\n" +
                "void main() {\n" +
                "   gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0f);\n" +
                "   ourColor = aColor;\n" +
                "}\n"

    private val fragmentShaderCode =
        "#version 300 es\n" +
            "out vec4 FragColor;\n" +
            "in vec3 ourColor;\n" +
            "void main() {\n" +
            "    FragColor = vec4(ourColor, 1.0f);\n" +
            "}\n"

    private var vertices = floatArrayOf(
        //位置                  //颜色
         0.9f,  -0.5f,  0.0f,  0.5f, -0.5f, 0.0f,    // bottom right
        -0.0f,  -0.5f,  0.0f,  0.5f, -0.5f, 0.0f,   // bottom left
         -0.45f,   0.5f,  0.0f,  0.0f, 0.5f, 0.0f,   // top
    )

    private var vertices2 = floatArrayOf(
        0.0f,  -0.5f,  0.0f,  0.5f, -0.5f, 0.0f,    // bottom right
        0.9f,  -0.5f,  0.0f,  0.5f, -0.5f, 0.0f,   // bottom left
        0.45f,   0.5f,  0.0f,  0.0f, 0.5f, 0.0f,   // top
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

    var vertexBuffer2: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(vertices2.size * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                // add the coordinates to the FloatBuffer
                put(vertices2)
                // set the buffer to read the first coordinate
                position(0)
            }
        }
    private var mProgram: Int

    var VAO = IntArray(2)
    var VBO = IntArray(2)

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

        GLES30.glGenVertexArrays(2, VAO, 0)
        GLES30.glGenBuffers(2, VBO, 0)
        GLES30.glBindVertexArray(VAO[0])

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[0])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.size * 4, vertexBuffer, GLES30.GL_STATIC_DRAW)

        //位置属性
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 6 * 4, 0)
        GLES30.glEnableVertexAttribArray(0)

        //颜色属性
        GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false, 6 * 4, 3 * 4)
        GLES30.glEnableVertexAttribArray(1)

        //第二个
        GLES30.glBindVertexArray(VAO[1])
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[1])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices2.size * 4, vertexBuffer2, GLES30.GL_STATIC_DRAW)
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 24, 0)
        GLES30.glEnableVertexAttribArray(0)
        GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false, 24, 12)
        GLES30.glEnableVertexAttribArray(1)

    }

    fun draw() {

        // Add program to OpenGL ES environment
        GLES30.glUseProgram(mProgram)
        GLES30.glBindVertexArray(VAO[0])
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3)

        GLES30.glBindVertexArray(VAO[1])
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3)
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
