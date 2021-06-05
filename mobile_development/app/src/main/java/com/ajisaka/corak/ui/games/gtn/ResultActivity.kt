package com.ajisaka.corak.ui.games.gtn

import android.R.id.message
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ajisaka.corak.MainActivity
import com.ajisaka.corak.databinding.ActivityResultBinding
import com.ajisaka.corak.utils.ConstantGame


@Suppress("DEPRECATION")
class ResultActivity : AppCompatActivity() {
    private lateinit var binding : ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        // TODO (STEP 6: Hide the status bar and get the details from intent and set it to the UI. And also add a click event to the finish button.)
        // START
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val userName = intent.getStringExtra(ConstantGame.USER_NAME)
        binding.tvName.text = userName

        val totalQuestions = intent.getIntExtra(ConstantGame.TOTAL_QUESTIONS, 0)
        val correctAnswers = intent.getIntExtra(ConstantGame.CORRECT_ANSWERS, 0)
        val totalScore = intent.getIntExtra(ConstantGame.TOTAL_SCORE.toString(), 0)
        binding.tvScore.text = "Your Question is $correctAnswers out of $totalQuestions."
        binding.tvTotalScore.text = "$totalScore Point"
        binding.btnFinish.setOnClickListener {
            finish()
        }
        // END
    }
}