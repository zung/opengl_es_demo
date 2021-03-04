package com.example.myapplication

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES30
import android.util.Log
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.microedition.khronos.opengles.GL

/**
 *
 * @ClassName:      ShaderUtils
 * @Description:    类作用描述
 * @Author:         czg
 * @CreateDate:     2021/3/3 8:32
 */
class ShaderUtils(val context: Context?) {
    val TAG = "ShaderUtils"

    var program: Int = 0

    fun loadShaderSource(vertexPath: String?, fragmentPath: String?) {

        val vertexCode = readFile(vertexPath!!)
        val fragmentCode = readFile(fragmentPath!!)

        val vShader = createShader(GLES30.GL_VERTEX_SHADER, vertexCode)
        val fShader = createShader(GLES30.GL_FRAGMENT_SHADER, fragmentCode)

        program = GLES30.glCreateProgram().also {
            GLES30.glAttachShader(it, vShader)
            GLES30.glAttachShader(it, fShader)
            GLES30.glLinkProgram(it)
            checkCompileErrors(it, "PROGRAM")
        }

        GLES30.glDeleteShader(vShader)
        GLES30.glDeleteShader(fShader)
    }

    fun use() {
        GLES30.glUseProgram(program)
    }

    fun setInt(name: String, value: Int) {
        GLES30.glGetUniformLocation(program, name).also {
            GLES30.glUniform1i(it, value)
        }
    }

    fun setBoolean(name: String, value: Boolean) {
        GLES30.glGetUniformLocation(program, name).also {
            GLES30.glUniform1i(it, if (value) 1 else 0)
        }
    }

    fun setFloat(name: String, value: Float) {
        GLES30.glGetUniformLocation(program, name).also {
            GLES30.glUniform1f(it, value)
        }
    }

    fun setVec2(name: String, value: FloatArray) {
        GLES30.glGetUniformLocation(program, name).also {
            GLES30.glUniform2fv(it, 1, value, 0)
        }
    }

    fun setVec2(name: String, x: Float, y: Float) {
        GLES30.glGetUniformLocation(program, name).also {
            GLES30.glUniform2f(it, x, y)
        }
    }

    fun setVec3(name: String, value: FloatArray) {
        GLES30.glGetUniformLocation(program, name).also {
            GLES30.glUniform3fv(it, 1, value, 0)
        }
    }

    fun setVec3(name: String, x: Float, y: Float, z: Float) {
        GLES30.glGetUniformLocation(program, name).also {
            GLES30.glUniform3f(it, x, y, z)
        }
    }

    fun setVec4(name: String, value: FloatArray) {
        GLES30.glGetUniformLocation(program, name).also {
            GLES30.glUniform4fv(it, 1, value, 0)
        }
    }

    fun setVec4(name: String, x: Float, y: Float, z: Float, w: Float) {
        GLES30.glGetUniformLocation(program, name).also {
            GLES30.glUniform4f(it, x, y, z, w)
        }
    }

    fun setMatrix2f(name: String, value: FloatArray) {
        GLES30.glGetUniformLocation(program, name).also {
            GLES30.glUniformMatrix2fv(it, 1, false, value, 0)
        }
    }

    fun setMatrix3f(name: String, value: FloatArray) {
        GLES30.glGetUniformLocation(program, name).also {
            GLES30.glUniformMatrix3fv(it, 1, false, value, 0)
        }
    }

    fun setMatrix4f(name: String, value: FloatArray) {
        GLES30.glGetUniformLocation(program, name).also {
            GLES30.glUniformMatrix4fv(it, 1, false, value, 0)
        }
    }

    private fun readFile(path: String): String {
        val result = StringBuffer()

        try {
            val inputStream = context?.assets?.open(path)
            val br = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (br.readLine().also { line = it } != null) {
                result.append(line).append("\n")
            }
            br.close()
            inputStream?.close()
        } catch (e: Exception) {
            Log.e(TAG, "readFile error:${e.message}")
        }

        return result.toString()
    }

    private fun createShader(type: Int, shaderCode: String): Int {

        // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
        return GLES30.glCreateShader(type).also { shader ->

            // add the source code to the shader and compile it
            GLES30.glShaderSource(shader, shaderCode)
            GLES30.glCompileShader(shader)
            checkCompileErrors(shader, if (type == GLES30.GL_VERTEX_SHADER) "VERTEX" else "FRAGMENT")
        }
    }

    private fun checkCompileErrors(shader: Int, type: String) {
        val status = IntArray(1)

        if (type != "PROGRAM") {
            GLES30.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0)
            if (status[0] == 0) {
                Log.e(TAG, "Error: shader_compilation_error of type: ${type},\n ${GLES30.glGetShaderInfoLog(shader)}")
            }
        } else {
            GLES30.glGetProgramiv(shader, GLES20.GL_LINK_STATUS, status, 0)
            if (status[0] == 0) {
                Log.e(TAG, "Error: program_linking_error of type: ${type},\n ${GLES30.glGetProgramInfoLog(shader)}")
            }
        }
    }
}