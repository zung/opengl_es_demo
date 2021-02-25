package com.example.myapplication;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @ClassName: PointRenderer
 * @Description: 类作用描述
 * @Author: czg
 * @CreateDate: 2021/1/15 17:32
 */
class PointRenderer implements GLSurfaceView.Renderer {

    private void checkError(String message) {
        int err = GLES20.glGetError();
        if (err != GLES20.GL_NO_ERROR) {
            throw new RuntimeException(message + " -> " + err);
        }
    }

    private int createShader(int type, String source) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, source);
        checkError("glShaderSource");
        GLES20.glCompileShader(shader);
        checkError("glCompileShader");
        return shader;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        int vertex = createShader(GLES20.GL_VERTEX_SHADER, "" +
                "void main() {" +
                "   gl_Position = vec4(0, 0, 0, 1);" +
                "   gl_PointSize = 100.0;" +
                "}");
        int fragment = createShader(GLES20.GL_FRAGMENT_SHADER, "" +
                "precision mediump float;" +
                "void main() {" +
                "   gl_FragColor = vec4(1, 0, 1, 1);" +
                "}");
        program = GLES20.glCreateProgram();
        checkError("glCreateProgram");
        GLES20.glAttachShader(program, vertex);
        checkError("glAttachShader(vertex)");
        GLES20.glAttachShader(program, fragment);
        checkError("glAttachShader(fragment)");
        GLES20.glLinkProgram(program);
        checkError("glLinkProgram");
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(program);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
    }

    private int program;

}