package com.ajisaka.corak.ui.welcome

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ajisaka.corak.MainActivity
import com.ajisaka.corak.databinding.FragmentWelcomeThreeBinding
import java.io.ByteArrayOutputStream


@Suppress("DEPRECATION")
class WelcomeThreeFragment : Fragment() {
    private val REQUEST_PERMISSION = 100
    val CAMERA_CAPTURE = 1
    val CROP_PIC = 2
    private var picUri: Uri? = null
    private lateinit var binding: FragmentWelcomeThreeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnStart.setOnClickListener {
            if(checkCameraPermission()){
                try {
                    // use standard intent to capture an image
                    val captureIntent = Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE
                    )
                    // we will handle the returned data in onActivityResult
                    startActivityForResult(captureIntent, CAMERA_CAPTURE)
                } catch (anfe: ActivityNotFoundException) {
                    val toast: Toast = Toast.makeText(
                        activity, "This device doesn't support the crop action!",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }

        }
    }
    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }

    private fun checkCameraPermission(): Boolean {
        var b = false
        if (activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
            != PackageManager.PERMISSION_GRANTED) {

            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_PERMISSION
                )
            }
        }else{
            b = true
        }
        return b
    }
    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === RESULT_OK) {
            if (requestCode === CAMERA_CAPTURE) {
                // get the Uri for the captured image
                if (data != null) {
                    picUri = data.data
                }
                performCrop()
            } else if (requestCode === CROP_PIC) {
                // get the returned data
                val bundle: Bundle? = data?.extras
                //Convert to byte array
                val stream = ByteArrayOutputStream()
                bundle?.getParcelable<Bitmap>("data")
                    ?.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray: ByteArray = stream.toByteArray()
                val in1 = Intent(activity, MainActivity::class.java)
                in1.putExtra("image", byteArray)
                startActivity(in1)
            }
        }
    }
    private fun performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            val cropIntent = Intent("com.android.camera.action.CROP")
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*")
            // set crop properties
            cropIntent.putExtra("crop", "true")
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1)
            cropIntent.putExtra("aspectY", 1)
            // indicate output X and Y
            cropIntent.putExtra("outputX", 224)
            cropIntent.putExtra("outputY", 224)
            // retrieve data on return
            cropIntent.putExtra("return-data", true)
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC)
        } // respond to users whose devices do not support the crop action
        catch (anfe: ActivityNotFoundException) {
            val toast: Toast = Toast
                .makeText(
                    activity,
                    "This device doesn't support the crop action!",
                    Toast.LENGTH_SHORT
                )
            toast.show()
        }
    }
}