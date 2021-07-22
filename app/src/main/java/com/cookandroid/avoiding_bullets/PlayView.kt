package com.cookandroid.avoiding_bullets

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class PlayView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes), SurfaceHolder.Callback {
    private val thread: PlayThread
    private var spaceShip: SpaceShip? = null
    private var background1 : Background? = null
    private var background2 : Background? = null
    private lateinit var bullets: ArrayList<Bullets>
    private var isGameOver = false
    private var am = context.assets
    private lateinit var activity : PlayActivity

    var bulletsBitmap = BitmapFactory.decodeResource(resources,R.drawable.bullets)
    var spaceBitmap = BitmapFactory.decodeResource(resources, R.drawable.spaceship)

    private var screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private var screenHeight = Resources.getSystem().displayMetrics.heightPixels

    private lateinit var paint : Paint
    private var oldTime : Int = 0

    private var touched:Boolean = false
    private var touched_x: Int =0
    private var touched_y: Int =0


    init {
        holder.addCallback(this)
        thread = PlayThread(holder, this)
    }


    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {

        spaceBitmap = Bitmap.createScaledBitmap(spaceBitmap, spaceBitmap.width / 3, spaceBitmap.height / 3, false)
        spaceShip = SpaceShip(spaceBitmap)

        bulletsBitmap = Bitmap.createScaledBitmap(bulletsBitmap,bulletsBitmap.width/4,bulletsBitmap.height/4,false)
        bullets = ArrayList()
        for(i in 0..9){
            bullets.add(Bullets(bulletsBitmap,i+1))
        }

        background1 = Background(resources)
        background2 = Background(resources)

        background2!!.y=-screenHeight.toFloat()


        oldTime= (System.currentTimeMillis()/10).toInt()

        paint =Paint()
        paint.textSize=70f
        paint.setColor(Color.WHITE)
        paint.textAlign=Paint.Align.CENTER

        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        var retry = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            retry = false
        }
    }

    fun update() {

        background1!!.y += 5
        background2!!.y += 5

        if(background1!!.y > screenHeight)
            background1!!.y=-screenHeight.toFloat()

        if(background2!!.y > screenHeight)
            background2!!.y=-screenHeight.toFloat()

        for(i in bullets.indices){
            var rand = (1..4).random()
            bullets[i]!!.update()
            if (bullets[i].getfinish()){
                bullets[i]=Bullets(bulletsBitmap,rand)
            }
            if (Rect.intersects(bullets[i].getCollisionShape(),spaceShip!!.getCollisionShape())) {
                isGameOver=true
            }
        }
        if(touched){
            spaceShip!!.updateTouch(touched_x,touched_y)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        touched_x = event.x.toInt()
        touched_y = event.y.toInt()

        val action = event.action
        when(action){
            MotionEvent.ACTION_DOWN -> touched =true
            MotionEvent.ACTION_MOVE -> touched = true
            MotionEvent.ACTION_UP -> touched = false
            MotionEvent.ACTION_CANCEL -> touched = false
            MotionEvent.ACTION_OUTSIDE -> touched = false
        }
        return true
    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawBitmap(background1!!.backgroundBitmap , background1!!.x , background1!!.y , paint)
        canvas.drawBitmap(background2!!.backgroundBitmap , background2!!.x , background2!!.y , paint)
        spaceShip!!.draw(canvas)

        for (i in bullets)
            i!!.draw(canvas)

        if (isGameOver){
            var afterScore=saveHighScore()

            paint.textSize=150f
            paint.textAlign=Paint.Align.CENTER
            paint.typeface=Typeface.createFromAsset(am,"samliphopangche_outline.ttf")
            canvas.drawText("GAME OVER",(screenWidth/2).toFloat(),(screenHeight/5*2).toFloat(),paint)

            paint.textSize=70f
            paint.typeface=Typeface.createFromAsset(am,"nanumgothicbold.ttf")
            canvas.drawText("HIGH SCORE : "+afterScore,(screenWidth/2).toFloat(),(screenHeight/5*3).toFloat(),paint)

            thread.setRunning(false)
        }else{
            canvas.drawText(stopWatch(),(screenWidth/2).toFloat(),50f,paint)
        }
    }

    fun stopWatch():String{
        var cTime = (System.currentTimeMillis()/10).toInt()
        var rTime = cTime-oldTime
        val mm1 = (rTime % 100)
        val mm = (rTime / 100)
        val ss = mm % 60
        val MM = mm / 60 % 60
        var time : String = mm.toString()+":"+ss+"."+mm1
        return mm.toString()
    }

    fun saveHighScore():String{
        var sp=context.getSharedPreferences("rank",0)
        var afterScore = sp.getInt("highScore",0)
        if(sp.getInt("highScore",0)<stopWatch().toInt()){
            var editor= sp.edit()
            editor.putInt("highScore",stopWatch().toInt())
            editor.commit()
            afterScore=stopWatch().toInt()
        }
        return afterScore.toString()
    }

}