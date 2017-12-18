package com.bulingzhuang.opengltest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import com.bulingzhuang.opengltest.shape.Cone
import com.bulingzhuang.opengltest.shape.Cube
import com.bulingzhuang.opengltest.shape.Cylinder
import com.bulingzhuang.opengltest.shape.Sphere
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //按下的时候记录的x、y值
    private var mMoveX: Float = 0.0f
    private var mMoveY: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        glv.setShape(Cylinder::class.java)
//        glv.setOnTouchListener { view, event ->
//            return@setOnTouchListener when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
////                    Log.e("blz", "down，x = ${event.x}，y = ${event.y}")
//                    mMoveX = event.x
//                    mMoveY = event.y
//                    true
//                }
//                MotionEvent.ACTION_MOVE -> {
////                    Log.e("blz", "move，x = ${event.x}，y = ${event.y}")
//                    val offsetX = event.x - mMoveX
//                    val offsetY = event.y - mMoveY
//                    if (offsetX > 2 && offsetY > 2) {
//                        if (view is GLViewM) {
//                            view.offset(offsetX, offsetY)
//                        }
//                    }
//                    mMoveX = event.x
//                    mMoveY = event.y
//                    true
//                }
//                MotionEvent.ACTION_UP -> {
////                    Log.e("blz", "up，x = ${event.x}，y = ${event.y}")
//                    true
//                }
//                else -> false
//            }
//        }
    }
}
