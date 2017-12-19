package com.bulingzhuang.opengltest.base;

import android.opengl.GLES20;
import android.view.View;

import com.bulingzhuang.opengltest.shape.MyTriangle;

import java.lang.reflect.Constructor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * ================================================
 * 作    者：bulingzhuang
 * 版    本：1.0
 * 邮    箱：bulingzhuang@foxmail.com
 * 创建日期：2017/12/13
 * 描    述：Shape装载器，本身是一个壳，根据传入的class文件反射出内容
 * ================================================
 */
public class MyGLRender extends BaseShape {

    private BaseShape shape;
    private Class<? extends BaseShape> clazz= MyTriangle.class;

    public MyGLRender(View mView) {
        super(mView);
    }

    public void setShape(Class<? extends BaseShape> shape){
        this.clazz=shape;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
            Constructor constructor=clazz.getDeclaredConstructor(View.class);
            constructor.setAccessible(true);
            shape= (BaseShape) constructor.newInstance(getMView());
        } catch (Exception e) {
            e.printStackTrace();
            shape=new MyTriangle(getMView());
        }
        shape.onSurfaceCreated(gl,config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);

        shape.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        shape.onDrawFrame(gl);
    }

}
