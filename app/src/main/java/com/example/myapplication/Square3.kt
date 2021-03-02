package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.Matrix
import android.renderscript.Float3
import android.util.Log
import java.nio.*
import kotlin.collections.ArrayList
import kotlin.math.cos
import kotlin.math.sin


class Square3(mContext: Context?) {
    var mFovy: Float? = 45.0f
    var mAngle: Float? = 0.0f

    private val vertexShaderCode =
        "#version 300 es\n" +
                "layout(location=0) in vec3 aPos;\n" +
                "layout (location = 1) in vec3 aColor;\n" +
                "layout (location = 2) in vec2 aTexCoord;\n" +
                "out vec4 ourColor;\n" +
                "out vec2 TexCoord;\n" +
                "uniform mat4 model;\n" +
                "uniform mat4 view;\n" +
                "uniform mat4 projection;\n" +
                "void main() {\n" +
                "   gl_Position = projection * view * model * vec4(aPos, 1.0);\n" +
                "   ourColor = vec4(aColor, 1.0);\n" +
                "   TexCoord = aTexCoord;\n" +
                "}\n"

    private val fragmentShaderCode =
        "#version 300 es\n" +
                "out vec4 FragColor;\n" +
                "in vec4 ourColor;\n" +
                "in vec2 TexCoord;\n" +
                "uniform sampler2D texture1;\n" +
                "uniform sampler2D texture2;\n" +
                "uniform float mVisible;\n" +
                "void main() {\n" +
                "    FragColor = mix(texture(texture1, TexCoord), \n" +
                "texture(texture2, TexCoord), mVisible);\n" +
                "}\n"

    private var vertices = floatArrayOf(
        //位置            //颜色            //纹理坐标(0.0, 0.0) -(2.0, 2.0）
//        0.5f, 0.5f, 0.0f,   1.0f, 0.0f, 0.0f, 1.0f, 1.0f,      // top right
//        0.5f, -0.5f, 0.0f,  0.0f, 1.0f, 0.0f, 1.0f, 0.0f,    // bottom right
//        -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,   // bottom left
//        -0.5f, 0.5f, 0.0f,  1.0f, 1.0f, 0.0f, 0.0f,1.0f,     // top left

        -0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f,  0.0f, 0.0f,
        0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 0.0f,
        0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
        0.5f,  0.5f, -0.5f,  1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
        -0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f,

        -0.5f, -0.5f,  0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
        0.5f, -0.5f,  0.5f,  0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
        0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
        0.5f,  0.5f,  0.5f,  1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
        -0.5f,  0.5f,  0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,

        -0.5f,  0.5f,  0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
        -0.5f,  0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
        -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f,
        -0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f,

        0.5f,  0.5f,  0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
        0.5f,  0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
        0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
        0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
        0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f,
        0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f,

        -0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
        0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
        0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
        0.5f, -0.5f,  0.5f,  1.0f, 1.0f, 0.0f, 1.0f, 0.0f,
        -0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f,
        -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,

        -0.5f,  0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
        0.5f,  0.5f, -0.5f,  0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
        0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
        0.5f,  0.5f,  0.5f,  1.0f, 1.0f, 0.0f, 1.0f, 0.0f,
        -0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f,
        -0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f
    )

    val cubePositions: ArrayList<FloatArray> = ArrayList()

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
    var TEX = IntArray(2)

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
        GLES30.glBufferData(
            GLES30.GL_ARRAY_BUFFER,
            vertices.size * 4,
            vertexBuffer,
            GLES30.GL_STATIC_DRAW
        )

        //bind ebo
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, EBO[0])
        GLES30.glBufferData(
            GLES30.GL_ELEMENT_ARRAY_BUFFER,
            indices.size * 4,
            indicesBuffer,
            GLES30.GL_STATIC_DRAW
        )

        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 8 * 4, 0)
        GLES30.glEnableVertexAttribArray(0)

        //颜色
        GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false, 8 * 4, 3 * 4)
        GLES30.glEnableVertexAttribArray(1)

        GLES30.glVertexAttribPointer(2, 2, GLES30.GL_FLOAT, false, 8 * 4, 6 * 4)
        GLES30.glEnableVertexAttribArray(2)

        val bitmap = getBitmap(mContext!!, R.drawable.tttt)
        val data = ByteBuffer.allocate(bitmap?.byteCount!!)
        data.order(ByteOrder.nativeOrder())
        bitmap.copyPixelsToBuffer(data)
        data.position(0)
        GLES30.glGenTextures(2, TEX, 0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, TEX[0])
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)

        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA, bitmap.width, bitmap.height, 0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, data)
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D)
        bitmap.recycle()

        val bm2 = getBitmap(mContext!!, R.drawable.face_black_face3)
        val d2 = ByteBuffer.allocate(bm2?.byteCount!!)
        d2.order(ByteOrder.nativeOrder())
        bm2.copyPixelsToBuffer(d2)
        d2.position(0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, TEX[1])
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA, bm2.width, bm2.height, 0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, d2)
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D)
        bm2.recycle()

        //unbind
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
        GLES30.glBindVertexArray(0)


        cubePositions.add(floatArrayOf(0.0f, 0.0f, 0.0f))
        cubePositions.add(floatArrayOf(2.0f, 5.0f, -15.0f))
        cubePositions.add(floatArrayOf(-1.5f, -2.2f, -2.5f))
        cubePositions.add(floatArrayOf(-3.8f, -2.0f, -12.3f))
        cubePositions.add(floatArrayOf(2.4f, -0.4f, -3.5f))
        cubePositions.add(floatArrayOf(-1.7f, 3.0f, -7.5f))
        cubePositions.add(floatArrayOf(1.3f, -2.0f, -2.5f))
        cubePositions.add(floatArrayOf(1.5f, 2.0f, -2.5f))
        cubePositions.add(floatArrayOf(1.5f, 0.2f, -1.5f))
        cubePositions.add(floatArrayOf(-1.3f, 1.0f, -1.5f))
    }

    fun getBitmap(context: Context, vectorDrawableId: Int): Bitmap? {
        var bitmap: Bitmap? = null
        val vectorDrawable = context.getDrawable(vectorDrawableId)

        bitmap = Bitmap.createBitmap(
            vectorDrawable!!.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)

        val matrix = android.graphics.Matrix()
        matrix.setScale(1.0f, -1.0f)//垂直翻转
//        matrix.setScale(-1.0f, 1.0f)//水平翻转
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        return bitmap
    }

    var mVisible: Float? = 0.2f
    var cpos = Float3(0.0f, 0.0f, 3.0f)
    var cfront = Float3(0.0f, 0.0f, -1.0f)
    var up = Float3(0.0f, 1.0f, 0.0f)

    fun draw() {

        // Add program to OpenGL ES environment
        GLES30.glUseProgram(mProgram)
        GLES30.glUniform1i(GLES30.glGetUniformLocation(mProgram, "texture1"), 0)
        GLES30.glUniform1i(GLES30.glGetUniformLocation(mProgram, "texture2"), 1)
        GLES30.glUniform1f(GLES30.glGetUniformLocation(mProgram, "mVisible"), mVisible!!)

        //

        val radius = 10.0f
        val cX = sin(mAngle!!) * radius
        val cZ = cos(mAngle!!) * radius

        val center = VectorUtils.add(cpos, cfront)

        val view = FloatArray(16)
        Matrix.setIdentityM(view, 0)
        Matrix.setLookAtM(view, 0,
            cpos.x, cpos.y, cpos.z,
            center.x, center.y, center.z,
            up.x, up.y, up.z)

        val projection = FloatArray(16)
        Matrix.setIdentityM(projection, 0)
        Matrix.perspectiveM(projection, 0, mFovy!!, 4.0f / 3.0f, 0.1f, 100.0f)

        GLES30.glGetUniformLocation(mProgram, "view").also {
            GLES30.glUniformMatrix4fv(it, 1, false, view, 0)
        }
        GLES30.glGetUniformLocation(mProgram, "projection").also {
            GLES30.glUniformMatrix4fv(it, 1, false, projection, 0)
        }
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, TEX[0])
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, TEX[1])

        GLES30.glBindVertexArray(VAO[0])
        cubePositions.forEachIndexed { index, floats ->
            val model = FloatArray(16)
            Matrix.setIdentityM(model, 0)
            Matrix.translateM(model, 0, floats[0], floats[1], floats[2])
            Matrix.rotateM(model, 0, model, 0, 20.0f * index, 1.0f, 0.3f, 0.5f)

            GLES30.glGetUniformLocation(mProgram, "model").also {
                GLES30.glUniformMatrix4fv(it, 1, false, model, 0)
            }

            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36)
        }

//        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, 0)

//        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(mProgram, "transform"), 1, false, trans2.array, 0 )
//        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, 0)
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
