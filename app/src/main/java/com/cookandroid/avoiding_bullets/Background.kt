package com.cookandroid.avoiding_bullets

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas

class Background(var res : Resources) {
    var x =0f
    var y =0f
    var backgroundBitmap = BitmapFactory.decodeResource(res,R.drawable.galaxy)
    private var screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private var screenHeight = Resources.getSystem().displayMetrics.heightPixels

    init {
        backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap,screenWidth,screenHeight,false)
    }

}