package com.bulingzhuang.opengltest.base

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.view.View

/**
 * ================================================
 * 作    者：bulingzhuang
 * 邮    箱：bulingzhuang@foxmail.com
 * 创建日期：2017/12/7
 * 描    述：
 * ================================================
 */
abstract class BaseShape(val mView: View) : GLSurfaceView.Renderer {
    fun loadShader(type: Int, shaderCode: String): Int {
        //根据type创建顶点着色器或片元着色器
        val shader = GLES20.glCreateShader(type)
        //将资源加入着色器中，并编译
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }
}