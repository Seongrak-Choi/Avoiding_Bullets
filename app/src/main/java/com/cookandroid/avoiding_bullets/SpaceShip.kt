package com.cookandroid.avoiding_bullets

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class SpaceShip(screenX:Int,screenY:Int,res:Resources) {
    var width = 0
    var height = 0
    var spaceShip : Bitmap = BitmapFactory.decodeResource(res,R.drawable.spaceship)

    init {
        width = spaceShip.width/2
        height = spaceShip.height/2

        spaceShip = Bitmap.createScaledBitmap(spaceShip,width,height,false)
    }

    fun getShip() : Bitmap{
        return spaceShip
    }

//    fun getCollisionShape() : Rect{
//        return Rect()
//    }
}