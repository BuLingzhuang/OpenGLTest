package com.bulingzhuang.opengltest

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.bulingzhuang.opengltest.shape.base.MyGLRender
import com.bulingzhuang.opengltest.shape.base.BaseShape

/**
 * ================================================
 * 作    者：bulingzhuang
 * 邮    箱：bulingzhuang@foxmail.com
 * 创建日期：2017/12/7
 * 描    述：展示用View
 * ================================================
 */
class GLView : GLSurfaceView {
    private val mRender = MyGLRender(this)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        setEGLContextClientVersion(2)
        setRenderer(mRender)
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    fun setShape(clazz: Class<out BaseShape>) {
        mRender.setShape(clazz)
    }
}