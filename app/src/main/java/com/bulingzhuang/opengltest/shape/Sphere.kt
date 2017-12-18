package com.bulingzhuang.opengltest.shape

import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import android.view.View
import com.bulingzhuang.opengltest.shape.base.BaseShape
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
 * 描    述：练习十，球
 * ================================================
 */
class Sphere(mView: View) : BaseShape(mView) {

    companion object {
        private val vertexShaderCode = "attribute vec4 vPosition;" +
                "uniform mat4 vMatrix;" +
                "varying  vec4 vColor;" +
                "attribute vec4 aColor;" +
                "void main() {" +
                "  gl_Position = vMatrix*vPosition;" +
                "  float color;" +
                "  if(vPosition.z>0.0){" +
                "        color=vPosition.z;" +
                "    }else{" +
                "        color=-vPosition.z;" +
                "    }" +
                "    vColor=vec4(color,0.58823+color*0.16863,0.53333-color*0.50583,1.0);" +
                "}"
        private val fragmentShaderCode = "precision mediump float;" +
                "varying vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

        private val COORDS_PER_VERTEX = 3
        //顶点之间的偏移量
        private val vertexStride = COORDS_PER_VERTEX * 4 // 每个顶点四个字节
    }

    private val vertexBuffer: FloatBuffer
    private val mViewMatrix = FloatArray(16)
    private val mProjectMatrix = FloatArray(16)
    private val mMVPMatrix = FloatArray(16)
    private var mProgram = 0
    private var mMatrixHandler = 0
    private var mPositionHandle = 0

    private var mEyeX = 0f
    private var mEyeY = 0f
    private var mWidth = 1
    private var mHeight = 1

    fun offset(offsetX: Float, offsetY: Float) {
        mEyeX += offsetX
        mEyeY += offsetY
        Log.e("blz", "内部刷新")
    }

    private lateinit var triangleCoords: FloatArray

    init {
        create(360)
        val bb = ByteBuffer.allocateDirect(triangleCoords.size * 4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer.put(triangleCoords)
        vertexBuffer.position(0)
    }

    private fun create(count: Int) {
        val vertexList = ArrayList<Float>()
        val angle = 360f / count
        var i = -90f
        var r1: Float
        var r2: Float
        var h1: Float
        var h2: Float
        var sin: Float
        var cos: Float
        while (i < 90 + angle) {
            h1 = Math.sin(i * Math.PI / 180f).toFloat()
            h2 = Math.sin((i + angle) * Math.PI / 180f).toFloat()
            r1 = Math.cos(i * Math.PI / 180f).toFloat()
            r2 = Math.cos((i + angle) * Math.PI / 180f).toFloat()
            //固定纬度，360度旋转遍历一条纬线
            val angleD = angle * 2
            var j = 0f
            while (j < 360 + angle) {
                cos = Math.cos(j * Math.PI / 180).toFloat()
                sin = -Math.sin(j * Math.PI / 180).toFloat()
                vertexList.add(r2 * cos)
                vertexList.add(h2)
                vertexList.add(r2 * sin)
                vertexList.add(r1 * cos)
                vertexList.add(h1)
                vertexList.add(r1 * sin)
                j += angleD
            }
            i += angle
        }
        triangleCoords = vertexList.toFloatArray()
    }

    override fun onDrawFrame(gl: GL10?) {
//        Log.e("blz", "eyeX = ${mEyeX / mWidth}，eyeY = ${mEyeY / mHeight}")
//        //设置透视投影
//        Matrix.frustumM(mProjectMatrix, 0, -mRatio, mRatio, -1f, 1f, 3f, 20f)
//        //设置相机位置
////        Matrix.setLookAtM(mViewMatrix, 0, 3f, -10f, -4f, 0f, 0f, 0f, 0f, 1f, 0f)
//        Matrix.setLookAtM(mViewMatrix, 0, mEyeX / mWidth, mEyeY / mHeight, 13f, 0f, 0f, 0f, 0f, 1f, 0f)
//        //计算变换矩阵
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0)

        Log.e("blz", "onDrawFrame")
        //清除深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        //将程序加入到OpenGLES 2.0环境
        GLES20.glUseProgram(mProgram)
        //获取变换矩阵vMatrix成员句柄
        mMatrixHandler = GLES20.glGetUniformLocation(mProgram, "vMatrix")
        //指定vMatrix的值
        GLES20.glUniformMatrix4fv(mMatrixHandler, 1, false, mMVPMatrix, 0)
        //获取顶点着色器的vPosition成员句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        //启用三角形顶点的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, triangleCoords.size / 3)
        //禁止顶点数组句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }

    var mRatio = 0f
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        Log.e("blz", "onSurfaceChanged")
        GLES20.glViewport(0, 0, width, height)
        mWidth = width
        mHeight = height
        Log.e("BLZ", "width = $width，height = $height")
        //计算宽高比
        mRatio = width.toFloat() / height

        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -mRatio, mRatio, -1f, 1f, 3f, 20f)
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 3f, -10f, -4f, 0f, 0f, 0f, 0f, 1f, 0f)
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        Log.e("blz", "onSurfaceCreated")
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