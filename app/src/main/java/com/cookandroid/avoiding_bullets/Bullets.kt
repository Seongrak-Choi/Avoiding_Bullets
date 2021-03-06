package com.cookandroid.avoiding_bullets

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import com.cookandroid.avoiding_bullets.PlayView.Companion.point
import kotlin.random.Random

class Bullets(private val image: Bitmap,private val num:Int) {
    var w: Int = 0
    var h: Int = 0
    var startX : Int = 0
    var startY : Int = 0
    var finish : Boolean = false
    private var xVelocity = 5
    private var yVelocity = 5
    private var screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private var screenHeight = Resources.getSystem().displayMetrics.heightPixels


    init {
        w = image.width
        h = image.height
        var randX = (1..10).random()
        var randY = (1..20).random()
        var randSpeed = (5..10).random()

        when(num%5){
            1-> {
                startX=screenWidth/10*randX
                startY=h

                yVelocity=randSpeed
                if(startX>screenWidth/2){
                    xVelocity=-randSpeed
                }else{
                    xVelocity=randSpeed
                }
            }
            2-> {
                startX=screenWidth-w
                startY=screenHeight/20*randY
                xVelocity=-randSpeed
                if(startY>screenHeight/2){
                    yVelocity=-randSpeed
                }else{
                    yVelocity=randSpeed
                }
            }
            3-> {
                startX=screenWidth/10*randX
                startY=screenHeight-(2*h)
                yVelocity=-randSpeed
                if(startX>screenWidth/2){
                    xVelocity=-randSpeed
                }else{
                    xVelocity=randSpeed
                }
            }
            4->{
                startX=w
                startY=screenHeight/20*randY
                xVelocity=randSpeed
                if(startY>screenHeight/2){
                    yVelocity=-randSpeed
                }else{
                    yVelocity=randSpeed
                }
            }
        }
    }

    fun draw(canvas: Canvas){
        canvas.drawBitmap(image, startX.toFloat(), startY.toFloat(),null)
    }

    fun update(){
        if (startX > screenWidth - w || startX<0){
            point++
            finish=true
        }
        if(startY>screenHeight - h || startY<image.height-h){
            point++
            finish=true
        }
        startX += (xVelocity)
        startY += (yVelocity)
    }

    fun getCollisionShape() : Rect {
        return Rect(startX,startY,startX+(w/2),startY+(h/2))
    }

    fun getfinish():Boolean{
        return finish
    }

}