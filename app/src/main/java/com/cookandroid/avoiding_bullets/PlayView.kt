package com.cookandroid.avoiding_bullets

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.view.SurfaceView
import android.view.View

class PlayView(context:Context,screenX:Int,screenY:Int) : View(context) {

    var screenX = screenX
    var screenY = screenY

    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)
        var picture = BitmapFactory.decodeResource(resources,R.drawable.spaceship)
        var picX = (screenX/2f)
        var picY = (screenY/2f)
        canvas.drawBitmap(picture,picX,picY,null)
    }
}