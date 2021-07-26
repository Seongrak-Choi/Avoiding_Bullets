package com.cookandroid.avoiding_bullets

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect

class SpaceShip(var image: Bitmap) {
    var x: Int = 0
    var y: Int = 0
    private val w: Int
    private val h: Int
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    init {
        w = image.width
        h = image.height

        x = screenWidth / 2-w
        y = screenHeight/2-h
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

    fun updateTouch(touch_x: Int, touch_y: Int) {
        var touch2_x = touch_x
        var touch2_y = touch_y
        if(touch2_x>screenWidth-w)
            touch2_x=(screenWidth-w)
        if(touch2_x<2)
            touch2_x=2
        if(touch2_y>screenHeight-h)
            touch2_y=(screenHeight-h)
        if(touch2_y<2)
            touch2_y=2

        x = touch2_x.toInt()
        y = touch2_y.toInt()
    }

    fun getCollisionShape() : Rect {
        return Rect(x,y,x+(w/10*8),y+(h/10*8))
    }
}