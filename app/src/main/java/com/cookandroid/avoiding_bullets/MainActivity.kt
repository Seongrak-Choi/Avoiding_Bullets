package com.cookandroid.avoiding_bullets

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.cookandroid.avoiding_bullets.databinding.ActivityMainBinding
import com.cookandroid.avoiding_bullets.databinding.DialogRankBinding
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var dialog_binding : DialogRankBinding
    private var isMute = true
    private lateinit var bgmPlayer : MediaPlayer
    private lateinit var sp : SharedPreferences
    private lateinit var dialog_layout : Dialog
    var score_1st  = 0
    var score_2st  = 0
    var score_3st  = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        dialog_binding = DialogRankBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sp = getSharedPreferences("score",0)
        var editor = sp.edit()


        score_1st = sp.getInt("Score_1st", 0)
        score_2st = sp.getInt("Score_2st", 0)
        score_3st = sp.getInt("Score_3st", 0)

        dialog_layout = Dialog(this)
        dialog_layout.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog_layout.setContentView(R.layout.dialog_rank)


        isMute = sp.getBoolean("isMute",true)



        bgmPlayer = MediaPlayer.create(this,R.raw.on_fire)


        binding.mainBtnStart.setOnClickListener {
            var intent = Intent(this,PlayActivity::class.java)
            intent.putExtra("isMute",isMute)
            startActivity(intent)
        }

        binding.mainBtnSound.setOnClickListener {
            if(isMute==true){
                isMute=false
                binding.mainBtnSound.setImageResource(R.drawable.ic_baseline_volume_off_24)
                    bgmPlayer.pause()
                editor.putBoolean("isMute",isMute)
                editor.commit()
            }else{
                isMute=true
                binding.mainBtnSound.setImageResource(R.drawable.ic_baseline_volume_up_24)
                    bgmPlayer.start()
                editor.putBoolean("isMute",isMute)
                editor.commit()
            }
        }
        binding.mainBtnRank.setOnClickListener {
            dialog_layout.findViewById<TextView>(R.id.dialog_tx_1st_score).text=score_1st.toString()
            dialog_layout.findViewById<TextView>(R.id.dialog_tx_2st_score).text=score_2st.toString()
            dialog_layout.findViewById<TextView>(R.id.dialog_tx_3st_score).text=score_3st.toString()
            dialog_layout.show()

            dialog_layout.findViewById<Button>(R.id.dialog_btn_ok).setOnClickListener{
                dialog_layout.dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(isMute){
            bgmPlayer.start()
        }
        else
            binding.mainBtnSound.setImageResource(R.drawable.ic_baseline_volume_off_24)

        sp = getSharedPreferences("score",0)
        score_1st = sp.getInt("Score_1st", 0)
        score_2st = sp.getInt("Score_2st", 0)
        score_3st = sp.getInt("Score_3st", 0)
    }

    override fun onPause() {
        super.onPause()
        if(bgmPlayer.isPlaying)
            bgmPlayer.pause()
    }

}