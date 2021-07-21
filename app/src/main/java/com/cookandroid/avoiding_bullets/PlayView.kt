package com.cookandroid.avoiding_bullets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet

class PlayView(context:Context,screenX:Int,screenY:Int) : View(context) {

    var screenX = screenX
    var screenY = screenY
    var picture = BitmapFactory.decodeResource(resources,R.drawable.spaceship)
    var width : Float= picture.width/2f
    var height :Float = picture.height/2f
    var picture2= Bitmap.createScaledBitmap(picture, width.toInt(), height.toInt(),false)
    var picX : Float = (screenX-picture.width)/2f
    var picY : Float =  (screenY-picture.height)/2f
    var touchX : Float = 0f
    var touchY : Float = 0f

    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)
        picX = touchX
        picY = touchY
        canvas.drawBitmap(picture2,picX,picY,null)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_MOVE ->{
                touchX = event.getX()-width/2
                touchY = event.getY()-height/2
                this.invalidate()
            }
        }


        return true
    }

//    fun update(){
//
//    }
//
//    fun sleep(){
//        try{
//            Thread.sleep(17)
//        }catch (e:InterruptedException ){
//            e.printStackTrace()
//        }
//    }

}