package com.cookandroid.avoiding_bullets

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View
import com.cookandroid.avoiding_bullets.databinding.ActivityPlayBinding

class PlayView(activity: PlayActivity, context: Context, isMute: Boolean) : SurfaceView(context),
    Runnable {
    companion object {
        var point: Int = 0
    }

    private lateinit var thread: Thread
    private var spaceShip: SpaceShip? = null
    private var background1: Background? = null
    private var background2: Background? = null
    private var bullets: ArrayList<Bullets>
    var isGameOver = false
    private var isPlaying = false
    private var am = context.assets
    private var activity: PlayActivity
    private var bgmPlayer: MediaPlayer
    private var isMute: Boolean
    var isRestart = false
    var newBulletCount = 0
    var bulletsBitmap = BitmapFactory.decodeResource(resources, R.drawable.bullets)
    var spaceBitmap = BitmapFactory.decodeResource(resources, R.drawable.spaceship)
    private var yourRank : Int = 0

    private var screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private var screenHeight = Resources.getSystem().displayMetrics.heightPixels

    private var paint: Paint
    private var paintCount: Paint
    private var oldTime: Int = 0

    private var touched: Boolean = false
    private var touched_x: Int = 0
    private var touched_y: Int = 0

    var prevX: Float = 0f
    var prevY: Float = 0f

    private lateinit var binding : ActivityPlayBinding

    init {
        point = 0
        this.activity = activity
        bgmPlayer = MediaPlayer.create(context, R.raw.electron_energy)
        this.isMute = isMute

        spaceBitmap = Bitmap.createScaledBitmap(
            spaceBitmap,
            spaceBitmap.width / 3,
            spaceBitmap.height / 3,
            false
        )
        spaceShip = SpaceShip(spaceBitmap)

        bulletsBitmap = Bitmap.createScaledBitmap(
            bulletsBitmap,
            bulletsBitmap.width / 4,
            bulletsBitmap.height / 4,
            false
        )
        bullets = ArrayList()

        for (i in 0..4) {
            bullets.add(Bullets(bulletsBitmap, i + 1))
        }

        background1 = Background(resources)
        background2 = Background(resources)

        background2!!.y = -screenHeight.toFloat()

        oldTime = (System.currentTimeMillis() / 10).toInt()

        paint = Paint()
        paintCount = Paint()
        paint.textSize = 70f
        paint.setColor(Color.WHITE)
        paint.textAlign = Paint.Align.CENTER

        binding= ActivityPlayBinding.inflate(activity.layoutInflater)
    }

    override fun run() {
        while (isPlaying) {
            update()
            draw()
            sleep()
        }
        while (isRestart) {
            draw()
            sleep()
        }
    }

    fun update() {
        background1!!.y += 5
        background2!!.y += 5

        if (background1!!.y > screenHeight)
            background1!!.y = -screenHeight.toFloat()

        if (background2!!.y > screenHeight)
            background2!!.y = -screenHeight.toFloat()

        if (point - newBulletCount == 20) {
            if (bullets.size < 16) {
                var rand = (1..4).random()
                bullets.add(Bullets(bulletsBitmap, rand))
                newBulletCount = point
            }
        }

        for (i in bullets.indices) {
            var rand = (1..4).random()
            bullets[i]!!.update()
            if (bullets[i].getfinish()) {
                bullets[i] = Bullets(bulletsBitmap, rand)
            }
            if (Rect.intersects(bullets[i].getCollisionShape(), spaceShip!!.getCollisionShape())) {
                isGameOver = true
            }
        }
        if (touched) {
            spaceShip!!.updateTouch(spaceShip!!.x + touched_x, spaceShip!!.y + touched_y)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                prevX = event.x
                prevY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                touched = true

                touched_x = event.x.toInt() - prevX.toInt()
                touched_y = event.y.toInt() - prevY.toInt()

                prevX = event.x
                prevY = event.y
            }
            MotionEvent.ACTION_UP -> touched = false
            MotionEvent.ACTION_CANCEL -> touched = false
            MotionEvent.ACTION_OUTSIDE -> touched = false
        }
        return true
    }


    fun draw() {
        if (holder.surface.isValid) {
            var canvas = holder.lockCanvas()
            canvas.drawBitmap(
                background1!!.backgroundBitmap,
                background1!!.x,
                background1!!.y,
                paint
            )
            canvas.drawBitmap(
                background2!!.backgroundBitmap,
                background2!!.x,
                background2!!.y,
                paint
            )
            spaceShip!!.draw(canvas)

            for (i in bullets)
                i!!.draw(canvas)

            if (isGameOver) {
                isPlaying = false

                yourRank = saveHighScore()
                paint.textSize = 150f
                paint.textAlign = Paint.Align.CENTER
                paint.typeface = Typeface.createFromAsset(am, "samliphopangche_outline.ttf")
                canvas.drawText(
                    "GAME OVER",
                    (screenWidth / 2).toFloat(),
                    (screenHeight / 5 * 2).toFloat(),
                    paint
                )
                paint.textSize = 70f
                paint.typeface = Typeface.createFromAsset(am, "nanumgothicbold.ttf")
                canvas.drawText(
                    "YOUR SCORE: " + point,
                    (screenWidth / 2).toFloat(),
                    (screenHeight / 5 * 3).toFloat(),
                    paint
                )

                paint.textSize = 60f

                when(yourRank){
                    1->{
                        canvas.drawText("Congrautration!! you are 1st",(screenWidth/2).toFloat(),(screenHeight/2).toFloat(),paint)
                    }
                    2->{
                        canvas.drawText("Congrautration!! you are 2nd",(screenWidth/2).toFloat(),(screenHeight/2).toFloat(),paint)
                    }
                    3->{
                        canvas.drawText("Congrautration!! you are 3rd",(screenWidth/2).toFloat(),(screenHeight/2).toFloat(),paint)

                    }
                }

                holder.unlockCanvasAndPost(canvas)
                bgmPlayer.pause()
                waitBeforeExiting()

                return

            } else {
                canvas.drawText(point.toString(), (screenWidth / 2).toFloat(), 100f, paint)
            }
            holder.unlockCanvasAndPost(canvas)
        }
    }


    fun saveHighScore() : Int {
        var sp = context.getSharedPreferences("score", 0)
        var editor = sp.edit()

        var score_1st = sp.getInt("Score_1st", 0)
        var score_2st = sp.getInt("Score_2st", 0)
        var score_3st = sp.getInt("Score_3st", 0)

        if (score_3st < point) {
            if(score_2st <point){
                if (score_1st < point){
                    editor.putInt("Score_1st", point)
                    editor.commit()
                    return 1
                }else{
                    editor.putInt("Score_2st", point)
                    editor.commit()
                    return 2
                }
            }else{
                editor.putInt("Score_3st", point)
                editor.commit()
                return 3
            }
        }
        return 0
    }

    private fun waitBeforeExiting() {
        try {
            Thread.sleep(2000)
            activity.finish()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun onResume() {
        if (!isRestart) {
            bgmPlayer = MediaPlayer.create(context, R.raw.electron_energy)
            isPlaying = true
            if (isMute) {
                bgmPlayer.start()
            }
            thread = Thread(this)
            thread.start()
        } else {
            thread = Thread(this)
            thread.start()
        }
    }

    fun makeThread() {
        isPlaying = true
        if (isMute) {
            bgmPlayer.start()
        }
        thread = Thread(this)
        thread.start()
    }


    fun onPause() {
        try {
            isPlaying = false
            if (bgmPlayer.isPlaying)
                bgmPlayer.pause()
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun sleep() {
        try {
            Thread.sleep(1)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


}