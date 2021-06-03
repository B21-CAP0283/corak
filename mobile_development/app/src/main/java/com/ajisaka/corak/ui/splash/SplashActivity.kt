package com.ajisaka.corak.ui.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ajisaka.corak.MainActivity
import com.ajisaka.corak.R
import com.ajisaka.corak.databinding.ActivitySplashBinding
import com.ajisaka.corak.ui.welcome.ViewsSliderActivity
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashBinding: ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
        if (restorePrefData()) {
            val i = Intent(applicationContext, MainActivity::class.java)
            val timer: Thread = object : Thread() {
                override fun run() {
                    try {
                        sleep(3000L)
                    } catch (var4: InterruptedException) {
                        var4.printStackTrace()
                    } finally {
                        this@SplashActivity.startActivity(i)
                    }
                }
            }
            timer.start()
        } else {

            val i = Intent(applicationContext, ViewsSliderActivity::class.java)
            val timer: Thread = object : Thread() {
                override fun run() {
                    try {
                        sleep(3000L)
                    } catch (var4: InterruptedException) {
                        var4.printStackTrace()
                    } finally {
                        this@SplashActivity.startActivity(i)
                    }
                }
            }
            timer.start()
        }

    }

    private fun restorePrefData(): Boolean {
        val pref = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        return pref.getBoolean("isIntroOpnend", false)
    }
}