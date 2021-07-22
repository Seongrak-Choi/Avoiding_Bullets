package com.cookandroid.avoiding_bullets

import android.graphics.Canvas
import android.view.SurfaceHolder

class PlayThread(private val surfaceHolder: SurfaceHolder, private val playView: PlayView) : Thread() {
    private var running: Boolean = false

    private val targetFPS = 165

    fun setRunning(isRunning: Boolean){
        this.running=isRunning
    }

    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        var targetTime = (1000/targetFPS).toLong()

        while (running){
            startTime = System.nanoTime()
            canvas = null

            try{
                canvas = this.surfaceHolder.lockCanvas()
                synchronized(surfaceHolder){
                    this.playView.update()
                    this.playView.draw(canvas!!)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    }catch (e:java.lang.Exception){
                        e.printStackTrace()
                    }
                }
            }
            timeMillis = (System.nanoTime()-startTime)/1000000
            waitTime = targetTime - timeMillis

            try{
                sleep(waitTime)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    companion object{
        private var canvas: Canvas? = null
    }
}