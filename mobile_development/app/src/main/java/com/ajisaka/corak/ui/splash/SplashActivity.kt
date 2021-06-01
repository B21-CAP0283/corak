package com.ajisaka.corak.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ajisaka.corak.MainActivity
import com.ajisaka.corak.R
import com.ajisaka.corak.ui.welcome.ViewsSliderActivity
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        val i = Intent(this, MainActivity::class.java)
//        val timer: Thread = object : Thread() {
//            override fun run() {
//                try {
//                    sleep(3000)
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                } finally {
//                    startActivity(i)
//                }
//            }
//        }
//        timer.start()

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