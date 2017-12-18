package com.bulingzhuang.opengltest.shape

import android.opengl.GLES20
import android.opengl.Matrix
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
 * 描    述：练习八，圆锥
 * ================================================
 */
class Cone(mView: View) : BaseShape(mView) {

    companion object {
        //        private val vertexShaderCode = "attribute vec4 vPosition;" +
//                "uniform mat4 vMatrix;" +
//                "varying  vec4 vColor;" +
//                "attribute vec4 aColor;" +
//                "void main() {" +
//                "  gl_Position = vMatrix*vPosition;" +
//                "  vColor=aColor;" +
//                "}"
        private val vertexShaderCode = "attribute vec4 vPosition;" +
                "uniform mat4 vMatrix;" +
                "varying  vec4 vColor;" +
                "attribute vec4 aColor;" +
                "void main() {" +
                "  gl_Position = vMatrix*vPosition;" +
                "  if(vPosition.z!=0.0){" +
                "        vColor=vec4(1,0.75686,0.02745,1.0);" +
                "    }else{" +
                "        if(vPosition.x == 0.0 && vPosition.y == 0.0){"+
                "            vColor=vec4(1,0.75686,0.02745,1.0);" +
                "          }else{" +
                "            vColor=vec4(0,0.58823,0.53333,1.0);" +
                "          }" +
                "    }" +
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
    private val vertexBottomBuffer: FloatBuffer
    private val colorBuffer: FloatBuffer
    //    private val indexBuffer: ShortBuffer
    private val mViewMatrix = FloatArray(16)
    private val mProjectMatrix = FloatArray(16)
    private val mMVPMatrix = FloatArray(16)
    private var mProgram = 0
    private var mMatrixHandler = 0
    private var mPositionHandle = 0
    private var mColorHandle = 0

    private lateinit var triangleCoords: FloatArray
    private lateinit var triangleCoordsBottom: FloatArray
    private lateinit var color: FloatArray

    init {
        create(360)
        val bb = ByteBuffer.allocateDirect(triangleCoords.size * 4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer.put(triangleCoords)
        vertexBuffer.position(0)
        val bb2 = ByteBuffer.allocateDirect(triangleCoordsBottom.size * 4)
        bb2.order(ByteOrder.nativeOrder())
        vertexBottomBuffer = bb2.asFloatBuffer()
        vertexBottomBuffer.put(triangleCoordsBottom)
        vertexBottomBuffer.position(0)
//        val cc = ByteBuffer.allocateDirect(index.size * 2)
//        cc.order(ByteOrder.nativeOrder())
//        indexBuffer = cc.asShortBuffer()
//        indexBuffer.put(index)
//        indexBuffer.position(0)
        val dd = ByteBuffer.allocateDirect(color.size * 4)
        dd.order(ByteOrder.nativeOrder())
        colorBuffer = dd.asFloatBuffer()
        colorBuffer.put(color)
        colorBuffer.position(0)
    }

    private fun create(count: Int) {
        val vertexList = ArrayList<Float>()
        val vertexBottomList = ArrayList<Float>()
        vertexList.add(0f)
        vertexList.add(0f)
        vertexList.add(2f)
        vertexBottomList.add(0f)
        vertexBottomList.add(0f)
        vertexBottomList.add(0f)
        val angle = 360f / count
        var i = 0f
        while (i < 360 + angle) {
            vertexList.add(Math.sin(i * Math.PI / 180f).toFloat())
            vertexList.add(Math.cos(i * Math.PI / 180f).toFloat())
            vertexList.add(0.0f)
            vertexBottomList.add(Math.sin(i * Math.PI / 180f).toFloat())
            vertexBottomList.add(Math.cos(i * Math.PI / 180f).toFloat())
            vertexBottomList.add(0.0f)
            i += angle
        }
        triangleCoords = vertexList.toFloatArray()
        triangleCoordsBottom = vertexBottomList.toFloatArray()
        val colorList = ArrayList<Float>()
        colorList.add(0f)
        colorList.add(1f)
        colorList.add(0f)
        colorList.add(1f)
        for (position in 0..count) {
            if (position % 2 == 0) {
                colorList.add(1f)
                colorList.add(0f)
                colorList.add(0f)
                colorList.add(1f)
            } else {
                colorList.add(0f)
                colorList.add(0f)
                colorList.add(1f)
                colorList.add(1f)
            }
        }
        color = colorList.toFloatArray()
    }

    override fun onDrawFrame(gl: GL10?) {
        //清除深度缓存
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT)
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

//        //准备三角形的坐标数据
//        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer)
//        //获取片元着色器的vColor成员句柄
//        mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor")
//        //设置绘制三角形的颜色
//        GLES20.glEnableVertexAttribArray(mColorHandle)
//        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer)
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, triangleCoords.size / 3)

        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, triangleCoords.size / 3)
        //准备三角形的坐标数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBottomBuffer)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, triangleCoordsBottom.size / 3)

        //禁止顶点数组句柄
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
//        //计算宽高比
//        val ratio = width.toFloat() / height
//        //设置透视投影
//        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
//        //设置相机位置
//        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 7f, 0f, 0f, 0f, 0f, 1f, 0f)
//        //计算变换矩阵
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0)

//        //计算宽高比
//        val ratio = width.toFloat() / height
//        //设置透视投影
//        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 20f)
//        //设置相机位置
//        Matrix.setLookAtM(mViewMatrix, 0, 1.0f, -10.0f, -4.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
//        //计算变换矩阵
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0)

        //计算宽高比
        val ratio = width.toFloat() / height
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 20f)
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 3f, -10f, -4f, 0f, 0f, 0f, 0f, 1f, 0f)
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