package com.ajisaka.corak.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ajisaka.corak.R
import com.ajisaka.corak.databinding.ActivityWelcomeBinding
import kotlin.system.exitProcess

@Suppress("DEPRECATION")
class WelcomeActivity : AppCompatActivity() {
    private var time: Long = 0
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        val twoSecond = 2000
        val strExit: String = getString(R.string.exit)
        if (time.plus(twoSecond) > System.currentTimeMillis()) {
            super.onBackPressed()
            moveTaskToBack(true)
            finish()
            exitProcess(0)
        } else {
            Toast.makeText(this, strExit, Toast.LENGTH_SHORT)
                .show()
        }
        time = System.currentTimeMillis()
    }
}