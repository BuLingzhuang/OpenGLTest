package com.bulingzhuang.opengltest.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.Matrix
import android.view.View
import com.bulingzhuang.opengltest.R
import com.bulingzhuang.opengltest.base.BaseShape
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 * ================================================
 * 作    者：bulingzhuang
 * 邮    箱：bulingzhuang@foxmail.com
 * 创建日期：2017/12/13
 * 描    述：练习九，圆柱
 * ================================================
 */
class AFilter(mView: View) : BaseShape(mView) {

    companion object {
        private val vertexShaderCode = "attribute vec4 vPosition;" +
                "attribute vec2 vCoordinate" +
                "uniform mat4 vMatrix;" +
                "varying vec2 aCoordinate;" +
                "void main(){" +
                "  gl_Position=vMatrix*vPosition;" +
                "  aCoordinate=vCoordinate;" +
                "}"
        private val fragmentShaderCode = "precision mediump float;" +
                "uniform sampler2D vTexture;" +
                "varying vec2 aCoordinate;" +
                "void main() {" +
                "  gl_FragColor = texture2D(vTexture,aCoordinate);" +
                "}"

        private val sPos = floatArrayOf(
                -1.0f, 1.0f, //左上角
                -1.0f, -1.0f, //左下角
                1.0f, 1.0f, //右上角
                1.0f, -1.0f     //右下角
        )

        private val sCoord = floatArrayOf(
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f)
    }

    private val bPos: FloatBuffer
    private val bCoord: FloatBuffer

    private val mBitmap: Bitmap = BitmapFactory.decodeResource(mView.resources, R.mipmap.ic_launcher)

    init {
        val bb = ByteBuffer.allocateDirect(sPos.size * 4)
        bb.order(ByteOrder.nativeOrder())
        bPos = bb.asFloatBuffer()
        bPos.put(sPos)
        bPos.position(0)
        val cc = ByteBuffer.allocateDirect(sCoord.size * 4)
        cc.order(ByteOrder.nativeOrder())
        bCoord = cc.asFloatBuffer()
        bCoord.put(sCoord)
        bCoord.position(0)
    }


    private val mViewMatrix = FloatArray(16)
    private val mProjectMatrix = FloatArray(16)
    private val mMVPMatrix = FloatArray(16)
    private var mProgram = 0

    override fun onDrawFrame(gl: GL10?) {
        //清除深度缓存
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        //将程序加入到OpenGLES 2.0环境
        GLES20.glUseProgram(mProgram)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //计算宽高比
        val ratio = width.toFloat() / height

        val ratioB = mBitmap.width.toFloat() / mBitmap.height
        if (width > height) {
            if (ratioB > ratio) {
                Matrix.orthoM(mProjectMatrix, 0, -ratio * ratioB, ratio * ratioB, -1f, 1f, 3f, 7f)
            } else {
                Matrix.orthoM(mProjectMatrix, 0, -ratio / ratioB, ratio / ratioB, -1f, 1f, 3f, 7f)
            }
        } else {
            if (ratioB > ratio) {
                Matrix.orthoM(mProjectMatrix, 0, -1f, 1f, -1f / ratio * ratioB, 1f / ratio * ratioB, 3f, 7f)
            } else {
                Matrix.orthoM(mProjectMatrix, 0, -1f, 1f, -ratioB / ratio, ratioB / ratio, 3f, 7f)
            }
        }

        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 20f)
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 7f, 0f, 0f, 0f, 0f, 1f, 0f)
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //将背景设置为灰色
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1f)
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        //创建一个空的OpenGLES程序
        mProgram = GLES20.glCreateProgram()
        //将顶点着色器加入程序
        GLES20.glAttachShader(mProgram, vertexShader)
        //将片元着色器加入程序
        GLES20.glAttachShader(mProgram, fragmentShader)
        //连接到着色器程序
        GLES20.glLinkProgram(mProgram)
        //开启深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
    }
}