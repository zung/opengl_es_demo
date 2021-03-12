package com.example.myapplication.testdepth

import android.content.Context
import android.opengl.GLES30
import android.opengl.Matrix
import android.renderscript.Float3
import com.example.myapplication.*

class TestDepthShape(context: Context?) : Shape() {
    var shader: ShaderUtils? = null

    var cubeVertices = floatArrayOf(
        // positions          // texture Coords
        -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
         0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
         0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
         0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
        -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

        -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
         0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
         0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
         0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
        -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

        -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
        -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
        -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

         0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
         0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
         0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
         0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
         0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
         0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
         0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
         0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
         0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
        -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

        -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
         0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
         0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
         0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
        -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
        -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
    )

    // texture Coords (note we set these higher than 1 (together with
    // L_REPEAT as texture wrapping mode). this will cause the floor texture to repeat)
    // 翻译：纹理坐标（请注意，我们将其设置为大于1（与
    //L\u作为纹理包装模式重复）。这将导致地板纹理重复）
    var planeVertices = floatArrayOf(
        // positions
         5.0f, -0.5f,  5.0f,  2.0f, 0.0f,
        -5.0f, -0.5f,  5.0f,  0.0f, 0.0f,
        -5.0f, -0.5f, -5.0f,  0.0f, 2.0f,

         5.0f, -0.5f,  5.0f,  2.0f, 0.0f,
        -5.0f, -0.5f, -5.0f,  0.0f, 2.0f,
         5.0f, -0.5f, -5.0f,  2.0f, 2.0f
    )

    var cubeVerticesBuffer = GlUtil.createFloatBuffer(cubeVertices)
    var planeVerticesBuffer = GlUtil.createFloatBuffer(planeVertices)

    var cubeVAO = IntArray(1)
    var cubeVBO = IntArray(1)

    var planeVAO = IntArray(1)
    var planeVBO = IntArray(1)

    var cubeTexture: Int? = 0
    var floorTexture: Int? = 0

    init {
        GLES30.glEnable(GLES30.GL_DEPTH)
        GLES30.glDepthFunc(GLES30.GL_ALWAYS)
        shader = ShaderUtils(context)
        shader?.loadShaderSource("depth/depth.vs", "depth/depth.fs")

        //cube VAO
        GLES30.glGenVertexArrays(1, cubeVAO, 0)
        GLES30.glGenBuffers(1, cubeVBO, 0)
        GLES30.glBindVertexArray(cubeVAO[0])
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, cubeVBO[0])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, 4 * cubeVertices.size, cubeVerticesBuffer, GLES30.GL_STATIC_DRAW)
        GLES30.glEnableVertexAttribArray(0)
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 5 * 4, 0)
        GLES30.glEnableVertexAttribArray(1)
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT, false, 5 * 4, 3 * 4)
        GLES30.glBindVertexArray(0)

        //plan VAO
        GLES30.glGenVertexArrays(1, planeVAO, 0)
        GLES30.glGenBuffers(1, planeVBO, 0)
        GLES30.glBindVertexArray(planeVAO[0])
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, planeVBO[0])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, 4 * planeVertices.size, planeVerticesBuffer, GLES30.GL_STATIC_DRAW)
        GLES30.glEnableVertexAttribArray(0)
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 5 * 4, 0)
        GLES30.glEnableVertexAttribArray(1)
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT, false, 5 * 4, 3 * 4)
        GLES30.glBindVertexArray(0)

        //load textures
        cubeTexture = loadTexture(context!!, R.drawable.marble)
        floorTexture = loadTexture(context, R.drawable.metal)
        shader?.use()
        shader?.setInt("texture1", 0)
    }

    override fun draw() {
        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 1.0f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        shader?.run {
            use()

            val view = camera.getView()
            val projection = FloatArray(16)
            Matrix.setIdentityM(projection, 0)
            Matrix.perspectiveM(projection, 0, camera.zoom * 2, 4.0f/3.0f, 0.1f, 100.0f)
            setMatrix4f("view", view)
            setMatrix4f("projection", projection)

            //cubes
            GLES30.glBindVertexArray(cubeVAO[0])
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, cubeTexture!!)

            val model = FloatArray(16)
            Matrix.setIdentityM(model, 0)
            Matrix.translateM(model, 0, -1.0f, 0.0f, -1.0f)
            setMatrix4f("model", model)
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36)

            val resModel = FloatArray(16)
            Matrix.setIdentityM(resModel, 0)
            Matrix.translateM(resModel, 0, -0.1f, 0.0f, 0.0f)
            setMatrix4f("model", resModel)
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36)

            //floor
            GLES30.glBindVertexArray(planeVAO[0])
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, floorTexture!!)
            val floorModel = FloatArray(16)
            Matrix.setIdentityM(floorModel, 0)
            setMatrix4f("model", floorModel)
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 6)
            GLES30.glBindVertexArray(0)
        }

    }
}
