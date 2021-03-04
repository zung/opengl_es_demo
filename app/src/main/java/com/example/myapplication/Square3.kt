package com.example.myapplication

import android.content.Context
import android.opengl.GLES30
import android.opengl.Matrix
import android.renderscript.Float3
import java.nio.*
import kotlin.collections.ArrayList

class Square3(mContext: Context?) {
    var mFovy: Float? = 45.0f

    private var vertices = floatArrayOf(
        //位置            //颜色            //纹理坐标(0.0, 0.0) -(2.0, 2.0）
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
    private var shader: ShaderUtils? = null

    var VAO = IntArray(1)
    var VBO = IntArray(1)
    var EBO = IntArray(1)
    var TEX = IntArray(2)

    init {
        shader = ShaderUtils(mContext)
        shader?.loadShaderSource("square3_vs.vs", "square3_fs.fs")

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

        val bitmap = BitmapUtils.getBitmap(mContext!!, R.drawable.tttt)
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

        val bm2 = BitmapUtils.getBitmap(mContext, R.drawable.face_black_face3)
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

    var mVisible: Float? = 0.2f
    var camera: Camera = Camera(Float3(0.0f, 0.0f, 3.0f))

    fun draw() {

        // Add program to OpenGL ES environment
        shader?.run {
            use()
            setInt("texture1", 0)
            setInt("texture2", 1)
            setFloat("mVisible", mVisible!!)

            val projection = FloatArray(16)
            Matrix.setIdentityM(projection, 0)
            Matrix.perspectiveM(projection, 0, mFovy!!, 4.0f / 3.0f, 0.1f, 100.0f)
            setMatrix4f("projection", projection)
            setMatrix4f("view", camera.getView())

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

            if (index % 3 == 0) {
                Matrix.rotateM(model, 0, model, 0, camera.mAngle!!, 1.0f, 0.3f, 0.5f)
            } else {
                Matrix.rotateM(model, 0, model, 0, 20.0f * index, 1.0f, 0.3f, 0.5f)
            }

            shader?.setMatrix4f("model", model)
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36)
        }

//        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, 0)

//        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(mProgram, "transform"), 1, false, trans2.array, 0 )
//        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, 0)
    }
}
