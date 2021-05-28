package com.ajisaka.corak.ui.welcome

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ajisaka.corak.MainActivity
import com.ajisaka.corak.databinding.FragmentWelcomeThreeBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
import com.theartofdev.edmodo.cropper.CropImage.getPickImageChooserIntent
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class WelcomeThreeFragment : Fragment() {
    val CAMERA_CAPTURE = 1
    val CROP_PIC = 2
    private var picUri: Uri? = null
    private var cropImageUri: Uri? = null
    private lateinit var currentPhotoPath: String
    val TAKE_PICTURE = 0
    private val REQUEST_IMAGE_CAPTURE = 100
    private lateinit var binding: FragmentWelcomeThreeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeThreeBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun startPickImageActivity(activity: Activity) {
        activity.startActivityForResult(
            getPickImageChooserIntent(activity),
            PICK_IMAGE_CHOOSER_REQUEST_CODE
        )
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnStart.setOnClickListener {

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

//            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            // ******** code for crop image
//            // ******** code for crop image
//            i.putExtra("crop", "true")
//            i.putExtra("aspectX", 100)
//            i.putExtra("aspectY", 100)
//            i.putExtra("outputX", 256)
//            i.putExtra("outputY", 356)
//
//            try {
//                i.putExtra("return-data", true)
//                startActivityForResult(
//                    Intent.createChooser(i, "Select Picture"), 0
//                )
//            } catch (ex: ActivityNotFoundException) {
//                ex.printStackTrace()
//            }
////            Intent(activity, MainActivity::class.java).also { intent ->
////                activity?.let { it1 ->
////                    intent.resolveActivity(it1.packageManager).also {
////                    intent.putExtra(MediaStore.ACTION_IMAGE_CAPTURE,"")
////                        val photoFile: File? = try {
////                            createCapturedPhoto()
////                        } catch (ex: IOException) {
////                            // If there is error while creating the File, it will be null
////                            null
////                        }
////
////                        photoFile?.also {
////                            val photoURI = FileProvider.getUriForFile(
////                                requireActivity(),
////                                "${BuildConfig.APPLICATION_ID}.fileprovider",
////                                it
////                            )
////
////                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
////                            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
////                        }
////                    }
////                }
////            }
        }
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
//                val extras: Bundle? = data?.extras
                // get the cropped bitmap
//                val thePic = extras?.getParcelable<Bitmap>("data")
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
//        if (requestCode === 0 && resultCode === RESULT_OK) {
//            try {
//                val bundle: Bundle? = data?.extras
//                //Convert to byte array
//                val stream = ByteArrayOutputStream()
//                bundle?.getParcelable<Bitmap>("data")
//                    ?.compress(Bitmap.CompressFormat.PNG, 100, stream)
//                val byteArray: ByteArray = stream.toByteArray()
//
//                val in1 = Intent(activity, MainActivity::class.java)
//                in1.putExtra("image", byteArray)
//                startActivity(in1)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        when (requestCode) {
//            PICK_IMAGE_CHOOSER_REQUEST_CODE -> if (resultCode === RESULT_OK) {
//                val imageUri = CropImage.getPickImageResultUri(requireContext(), data)
//
//                // For API >= 23 we need to check specifically that we have permissions to read external storage.
//                if (CropImage.isReadExternalStoragePermissionsRequired(
//                        requireContext(),
//                        imageUri
//                    )
//                ) {
//                    // request permissions and handle the result in onRequestPermissionsResult()
//                    cropImageUri = imageUri
//                    requestPermissions(
//                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                        CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE
//                    )
//                } else {
//                    // no permissions required or already grunted, can start crop image activity
//                    cropImage(imageUri)
//                }
//            }
//            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
//                val result = CropImage.getActivityResult(data)
//                if (resultCode === RESULT_OK) {
//                    val resultUri = result.uri
//                    try {
//                        val imageStream: InputStream? =
//                            requireActivity().contentResolver.openInputStream(resultUri)
//                        val selectedImage = BitmapFactory.decodeStream(imageStream)
////Convert to byte array
//
//                        //Convert to byte array
//                        val stream = ByteArrayOutputStream()
//                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
//                        val byteArray: ByteArray = stream.toByteArray()
//
//                        val in1 = Intent(activity, MainActivity::class.java)
//                        in1.putExtra("image", byteArray)
//                        // Use Picasso or Universal Image Loader:       compile 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'
////                        Picasso.with(context)
////                            .load(R.drawable.ic_profile)
////                            .error(R.drawable.ic_profile)
////                            .fit()
////                            .centerCrop()
////                            .into(imageProfile)
//
//                        // or
//
////                         imageProfile.setImageBitmap(selectedImage);
//                    } catch (e: FileNotFoundException) {
//                        e.printStackTrace()
//                    }
//                } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    Log.d(TAG, "onActivityResult: Error in cropping")
//                }
//            }
//        }

//        if (requestCode === TAKE_PICTURE && resultCode === RESULT_OK) {
//            val u: Uri? = data?.data
//            val selfiSrc = Intent(activity, MainActivity::class.java)
//            selfiSrc.putExtra("imgurl", u)
//            startActivity(selfiSrc)
//        }
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
    private fun cropImage(imageUri: Uri) {
        CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(requireContext(), this)
    }

    @Throws(IOException::class)
    private fun createCapturedPhoto(): File {
        val timestamp: String = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date())
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("PHOTO_${timestamp}", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

}