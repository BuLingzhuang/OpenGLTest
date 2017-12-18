package com.bulingzhuang.opengltest

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import com.bulingzhuang.opengltest.shape.Sphere
import com.bulingzhuang.opengltest.shape.base.MyGLRender
import com.bulingzhuang.opengltest.shape.base.BaseShape

/**
 * ================================================
 * 作    者：bulingzhuang
 * 邮    箱：bulingzhuang@foxmail.com
 * 创建日期：2017/12/7
 * 描    述：展示用View(包含滑动)
 * ================================================
 */
class GLViewM : GLSurfaceView {
    private val mRender = Sphere(this)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        setEGLContextClientVersion(2)
        setRenderer(mRender)
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    fun offset(offsetX: Float, offsetY: Float) {
//        Log.e("blz","刷新")
        mRender.offset(offsetX, offsetY)
        requestRender()
    }
}