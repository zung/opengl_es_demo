package com.example.myapplication.lighting

import android.content.Context
import android.opengl.GLES30
import android.opengl.Matrix
import com.example.myapplication.*
import java.nio.*
import kotlin.math.PI
import kotlin.math.cos


class MultipleLighting(mContext: Context?): Shape() {
    var lightPos = floatArrayOf(1.2f, 1.0f, 2.0f)
    val cubePositions: ArrayList<FloatArray> = ArrayList()
    val pointLightPositions: ArrayList<FloatArray> = ArrayList()

    private var vertices = floatArrayOf(
        //position            //normals(法向量)     //texture coords
        -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f, 0.0f,
         0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f, 0.0f,
         0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f, 1.0f,
         0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f, 1.0f,
        -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f, 0.0f,

        -0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   0.0f, 0.0f,
         0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   1.0f, 0.0f,
         0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   1.0f, 1.0f,
         0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   1.0f, 1.0f,
        -0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   0.0f, 0.0f,

        -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f, 0.0f,
        -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  1.0f, 1.0f,
        -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  0.0f, 0.0f,
        -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f, 0.0f,

         0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  1.0f, 0.0f,
         0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  1.0f, 1.0f,
         0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
         0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
         0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  0.0f, 0.0f,
         0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  1.0f, 0.0f,

        -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f, 1.0f,
         0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  1.0f, 1.0f,
         0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f, 0.0f,
         0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f, 0.0f,
        -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  0.0f, 0.0f,
        -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f, 1.0f,

        -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f, 1.0f,
         0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  1.0f, 1.0f,
         0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f, 0.0f,
         0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f, 0.0f,
        -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  0.0f, 0.0f,
        -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f, 1.0f,
    )

    //EBO
    private var indices = intArrayOf(
        0, 1, 3,
        1, 2, 3
    )

    var vertexBuffer: FloatBuffer = GlUtil.createFloatBuffer(vertices)
    var objectShader: ShaderUtils? = null
    var lightShader: ShaderUtils? = null

    var VAO = IntArray(1)
    var VBO = IntArray(1)
    var lightVAO = IntArray(1)
    var diffuseMap: Int? = 0    //漫反射贴图
    var specularMap: Int? = 0   //镜面光贴图

    init {
        objectShader = ShaderUtils(mContext!!).also {
            it.loadShaderSource("multiple_lights.vs", "multiple_lights.fs")
        }

        lightShader = ShaderUtils((mContext)).also {
            it.loadShaderSource("lightVertexShader.vs", "lightFragmentShader.fs")
        }

        GLES30.glGenVertexArrays(1, VAO, 0)
        GLES30.glGenBuffers(1, VBO, 0)

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[0])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, 4 * vertices.size, vertexBuffer, GLES30.GL_STATIC_DRAW)

        GLES30.glBindVertexArray(VAO[0])
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 8 * 4, 0)
        GLES30.glEnableVertexAttribArray(0)
        GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false, 8 * 4, 3 * 4)
        GLES30.glEnableVertexAttribArray(1)
        GLES30.glVertexAttribPointer(2, 2, GLES30.GL_FLOAT, false, 8 * 4, 6 * 4)
        GLES30.glEnableVertexAttribArray(2)

        //light
        GLES30.glGenVertexArrays(1, lightVAO, 0)
        GLES30.glBindVertexArray(lightVAO[0])

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[0])
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 8 * 4, 0)
        GLES30.glEnableVertexAttribArray(0)

        //texture
        diffuseMap = loadTexture(mContext, R.drawable.container2)
        specularMap = loadTexture(mContext, R.drawable.container2_specular)
        objectShader?.use()
        objectShader?.setInt("material.diffuse", 0)
        objectShader?.setInt("material.specular", 1)

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

        pointLightPositions.add(floatArrayOf(0.7f, 0.2f, 2.0f))
        pointLightPositions.add(floatArrayOf(2.3f, -3.3f, -4.0f))
        pointLightPositions.add(floatArrayOf(-4.0f, 2.0f, -12.0f))
        pointLightPositions.add(floatArrayOf(0.0f, 0.0f, -3.0f))
    }

    override fun draw() {
        GLES30.glClearColor(0.9f, 0.9f, 0.9f,1.0f)
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
        val projection = FloatArray(16)
        Matrix.setIdentityM(projection, 0)
        Matrix.perspectiveM(projection, 0, camera.zoom * 2, 4.0f / 3.0f, 0.1f, 100.0f)

        // Add program to OpenGL ES environment
        objectShader?.run {
            use()
            setMatrix4f("projection", projection)
            setMatrix4f("view", camera.getView())

            //定向光
            setVec3("dirLight.direction", -0.2f, -1.0f, -0.3f)
            setVec3("dirLight.ambient", 0.5f, 0.5f, 0.5f)
            setVec3("dirLight.diffuse", 1.0f, 1.0f, 1.0f)
            setVec3("dirLight.specular", 1.0f, 1.0f, 1.0f)
            //点光源
            pointLightPositions.forEachIndexed { index, floats ->
                val namePre = "pointLights[${index}]."
                setVec3("${namePre}position", floats)
                setVec3("${namePre}ambient", 0.04f, 0.07f, 0.01f)
                setVec3("${namePre}diffuse", 0.4f, 0.7f, 0.1f)
                setVec3("${namePre}specular", 0.4f, 0.7f, 0.1f)
                setFloat("${namePre}constant", 1.0f)
                setFloat("${namePre}linear", 0.07f)
                setFloat("${namePre}quadratic", 0.017f)
            }
            //聚光
            setVec3("spotLight.position", camera.cpos.x, camera.cpos.y, camera.cpos.z)
            setVec3("spotLight.direction", camera.cfront.x, camera.cfront.y, camera.cfront.z)
            setVec3("spotLight.ambient", 0.0f, 0.0f, 0.0f)
            setVec3("spotLight.diffuse", 0.0f, 1.0f, 0.0f)
            setVec3("spotLight.specular", 0.0f, 1.0f, 0.0f)
            setFloat("spotLight.constant", 1.0f)
            setFloat("spotLight.linear", 0.07f)
            setFloat("spotLight.quadratic", 0.017f)
            setFloat("spotLight.cutOff", cos(radians(7.0f)))
            setFloat("spotLight.outerCutOff", cos(radians((10.0f))))

            setVec3("viewPos", camera.cpos.x, camera.cpos.y, camera.cpos.z)

            //设置材质
            setFloat("material.shininess", 32.0f)
        }

        //texture
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, diffuseMap!!)
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, specularMap!!)

        //renderer cube
        GLES30.glBindVertexArray(VAO[0])

        cubePositions.forEachIndexed { index, floats ->
            val model = FloatArray(16)
            Matrix.setIdentityM(model, 0)
            Matrix.translateM(model, 0, floats[0], floats[1], floats[2])
            Matrix.rotateM(model, 0, radians(20.0f * index), 1.0f, 0.3f, 0.5f)
            objectShader?.setMatrix4f("model", model)

            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36)
        }

        //light
        lightShader?.run {
            use()
            setMatrix4f("projection", projection)
            setMatrix4f("view", camera.getView())

            GLES30.glBindVertexArray(lightVAO[0])
            pointLightPositions.forEachIndexed { index, floats ->
                val model = FloatArray(16)
                Matrix.setIdentityM(model, 0)
                Matrix.translateM(model, 0, floats[0], floats[1], floats[2])
                Matrix.scaleM(model, 0, 0.2f, 0.2f, 0.2f)
                objectShader?.setMatrix4f("model", model)

                GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36)
            }
        }
    }
}
