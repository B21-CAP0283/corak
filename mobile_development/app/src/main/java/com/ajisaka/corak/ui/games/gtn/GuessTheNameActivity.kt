package com.ajisaka.corak.ui.games.gtn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ajisaka.corak.R
import com.ajisaka.corak.databinding.ActivityDetailBinding.inflate
import com.ajisaka.corak.databinding.ActivityGuessTheNameBinding
import com.ajisaka.corak.utils.ConstantGame

@Suppress("DEPRECATION")
class GuessTheNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuessTheNameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuessTheNameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        // To hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        binding.btnStart.setOnClickListener {

            if (binding.etName.text.toString().isEmpty()) {

                Toast.makeText(this@GuessTheNameActivity, R.string.please_enter_your_name, Toast.LENGTH_SHORT)
                    .show()
            } else {

                val intent = Intent(this@GuessTheNameActivity, QuizQuestionActivity::class.java)
                // TODO (STEP 2: Pass the name through intent using the constant variable which we have created.)
                // START
                intent.putExtra(ConstantGame.USER_NAME, binding.etName.text.toString())
                // END
                startActivity(intent)
                finish()
            }
        }
    }
}