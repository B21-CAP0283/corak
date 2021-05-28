package com.ajisaka.corak.ui.welcome

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ajisaka.corak.MainActivity
import com.ajisaka.corak.R
import com.ajisaka.corak.databinding.ActivityWelcomeBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
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
//        val mImageUri_gallery = data!!.data
//        val mImageUri_camera = data.data
//        if (requestCode == 100 && resultCode == RESULT_OK) {
//            CropImage.activity(mImageUri_gallery)
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .start(this)
//            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//                val result = CropImage.getActivityResult(data)
//                if (resultCode == RESULT_OK) {
//                    val resultUriGallery: Uri = result.uri
//                    val intentGallery = Intent(this@WelcomeActivity, MainActivity::class.java)
//                    intentGallery.putExtra("image-uri", resultUriGallery.toString())
//                    startActivity(intentGallery)
//                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    Log.d("Error",result.error.toString())
//                }
//            }
//        } else if (resultCode != RESULT_CANCELED) {
//            if (requestCode == 200 && resultCode == RESULT_OK) {
//                CropImage.activity(mImageUri_camera)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .start(this)
//                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//                    val result = CropImage.getActivityResult(data)
//                    if (resultCode == RESULT_OK) {
//                        val resultUriCamera: Uri = result.uri
//                        val intentCamera = Intent(this@WelcomeActivity, MainActivity::class.java)
//                        intentCamera.putExtra("image-uri", resultUriCamera.toString())
//                        startActivity(intentCamera)
//                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                         Log.d("Error",result.error.toString())
//                    }
//                }
//            }
//        }
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