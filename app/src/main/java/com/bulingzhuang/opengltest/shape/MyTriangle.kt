package com.bulingzhuang.opengltest.shape

import android.opengl.GLES20
import android.view.View
import com.bulingzhuang.opengltest.base.BaseShape
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * ================================================
 * 作    者：bulingzhuang
 * 版    本：1.0
 * 邮    箱：bulingzhuang@foxmail.com
 * 创建日期：2017/12/13
 * 描    述：练习一，简单三角形
 * ================================================
 */
class MyTriangle(mView: View) : BaseShape(mView) {

    companion object {

        private val vertexShaderCode = "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = vPosition;" +
                "}"

        private val fragmentShaderCode = (
                "precision mediump float;" +
                        "uniform vec4 vColor;" +
                        "void main() {" +
                        "  gl_FragColor = vColor;" +
                        "}")

        val COORDS_PER_VERTEX = 3

        private val triangleCoords = floatArrayOf(
                0.5f, 0.5f, 0.0f, // top
                -0.5f, -0.5f, 0.0f, // bottom left
                0.5f, -0.5f, 0.0f  // bottom right
        )

        private val color = floatArrayOf(1f, 1f, 1f, 1f)//白色
    }

    private var mProgram = 0
    private var mPositionHandle = 0
    private var mColorHandle = 0
    private lateinit var mBuffer: FloatBuffer
    //顶点个数
    private val vertexCount = triangleCoords.size / COORDS_PER_VERTEX
    //顶点之间的偏移量
    private val vertexStride = COORDS_PER_VERTEX * 4 // 每个顶点四个字节


    override fun onDrawFrame(gl: GL10?) {
        //将程序加入到OpenGL ES 2.0环境
        GLES20.glUseProgram(mProgram)
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, mBuffer)
        //获取片元着色器的vColor成员句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor")
        //设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0)
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //将背景设置为灰色
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1f)
        val bb = ByteBuffer.allocateDirect(triangleCoords.size * 4)
        bb.order(ByteOrder.nativeOrder())
        //将坐标数据转换为FloatBuffer，用以传入给OpenGL ES程序
        mBuffer = bb.asFloatBuffer()
        mBuffer.put(triangleCoords)
        mBuffer.position(0)
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        //创建一个空的OpenGL ES程序
        mProgram = GLES20.glCreateProgram()
        //将顶点着色器加入程序
        GLES20.glAttachShader(mProgram, vertexShader)
        //将片元着色器加入程序
        GLES20.glAttachShader(mProgram, fragmentShader)
        //连接到着色器程序
        GLES20.glLinkProgram(mProgram)
    }
}