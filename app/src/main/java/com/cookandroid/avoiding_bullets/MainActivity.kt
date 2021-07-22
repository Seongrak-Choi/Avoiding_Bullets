package com.cookandroid.avoiding_bullets

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.cookandroid.avoiding_bullets.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var isMute = true
    private lateinit var bgmPlayer : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bgmPlayer = MediaPlayer.create(this,R.raw.on_fire)
        bgmPlayer.start()

        binding.mainBtnStart.setOnClickListener {
            var intent = Intent(this,PlayActivity::class.java)
            startActivity(intent)
        }

        binding.mainBtnSound.setOnClickListener {
            if(isMute==true){
                isMute=false
                binding.mainBtnSound.setImageResource(R.drawable.ic_baseline_volume_off_24)
                bgmPlayer.pause()
            }else{
                isMute=true
                binding.mainBtnSound.setImageResource(R.drawable.ic_baseline_volume_up_24)
                bgmPlayer.start()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        bgmPlayer.pause()
    }

    override fun onRestart() {
        super.onRestart()
        bgmPlayer.start()
    }
}