package com.bulingzhuang.opengltest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bulingzhuang.opengltest.shape.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        glv.setShape(Cube::class.java)
    }
}
