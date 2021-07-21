package com.cookandroid.avoiding_bullets

import android.graphics.Point
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class PlayActivity : AppCompatActivity() {
    private lateinit var playView : PlayView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        var point = Point()
        windowManager.defaultDisplay.getSize(point)
        playView = PlayView(this,point.x,point.y)

        setContentView(playView)
    }
}