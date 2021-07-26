package com.cookandroid.avoiding_bullets

import android.os.*
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.cookandroid.avoiding_bullets.databinding.ActivityPlayBinding
import java.lang.String

class PlayActivity : AppCompatActivity() {
    private var isMute = true
    private lateinit var playView: PlayView
    private lateinit var binding: ActivityPlayBinding
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        handler = Handler()

        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        isMute = intent.getBooleanExtra("isMute", true)


        playView = PlayView(this, this, isMute)
        binding.playFrameLayout.addView(playView)

        binding.playBtnAgain.setOnClickListener {
            var timer = 3
            binding.playBtnAgain.visibility = View.INVISIBLE
            binding.playTxCountdown.visibility = View.VISIBLE

            object : CountDownTimer(3000, 1000) {
                override fun onTick(p0: Long) {
                    binding.playTxCountdown.text = timer.toString()
                    timer--
                }

                override fun onFinish() {
                    binding.playTxCountdown.visibility = View.INVISIBLE
                    playView.isRestart = false
                    playView.makeThread()
                }
            }.start()
        }

    setContentView(binding.root)
}


override fun onPause() {
    super.onPause()
    playView.onPause()
}

override fun onRestart() {
    super.onRestart()
    playView.isRestart = true
    binding.playBtnAgain.visibility = View.VISIBLE

}

override fun onResume() {
    super.onResume()
    playView.onResume()
}

}