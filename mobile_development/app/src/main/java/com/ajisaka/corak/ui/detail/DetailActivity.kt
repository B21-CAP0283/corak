package com.ajisaka.corak.ui.detail

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.*
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.ajisaka.corak.MainActivity
import com.ajisaka.corak.R
import com.ajisaka.corak.application.FavBatikApplication
import com.ajisaka.corak.databinding.ActivityDetailBinding
import com.ajisaka.corak.databinding.DialogCustomImageSelectionBinding
import com.ajisaka.corak.helpers.PercentageHelper
import com.ajisaka.corak.model.entities.FavBatik
import com.ajisaka.corak.tflite.Classifier
import com.ajisaka.corak.utils.Constants
import com.ajisaka.corak.viewmodel.FavBatikViewModel
import com.ajisaka.corak.viewmodel.FavBatikViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URI
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private val mediaPlayer = MediaPlayer()
    private lateinit var binding: ActivityDetailBinding
    private var db = Firebase.firestore

    private var pathImage: String? = ""
    private val mFavBatikViewModel: FavBatikViewModel by viewModels {
        FavBatikViewModelFactory((application as FavBatikApplication).repository)
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }
    companion object {
        private const val CAMERA = 1

        private const val GALLERY = 2
        private const val IMAGE_DIRECTORY = "FavBatikImages"
    }
    private var mImagePath: String = ""

    private var title: String = "Detail Batik"
    private val mInputSize = 224
    private val mModelPath = "corak_15_motives.tflite"
    private val mLabelPath = "label.txt"
    private lateinit var classifier: Classifier


    @SuppressLint("UseCompatLoadingForDrawables")
    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fabCamera.setOnClickListener{ customImageSelectionDialog() }
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
        initClassifier()
        initFireabase()
        binding.includeToolbar.btnBack.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        if (intent.getStringExtra("resultImage") != null){
            pathImage = intent.getStringExtra("resultImage")
            Log.d("Get Data",pathImage.toString())
            val bitmap = BitmapFactory.decodeFile(pathImage)

            runClassifier(bitmap)
        }else {
            Log.d("Error no data", "Else")
        }
        if(intent.getStringExtra("uriImage") != null){
            val uriImageFromGalery = Uri.parse(intent.getStringExtra("uriImage"))
            pathImage = getRealPathFromDocumentUri(applicationContext, uriImageFromGalery)
            val bitmap = BitmapFactory.decodeFile(pathImage)
            runClassifier(bitmap)
        }


    }
    private fun getRealPathFromDocumentUri(context: Context, uri: Uri): String {
    var filePath = ""
    val p: Pattern = Pattern.compile("(\\d+)$")
    val m: Matcher = p.matcher(uri.toString())
    if (!m.find()) {
        Log.e(
            DetailActivity::class.java.simpleName,
            "ID for requested image not found: $uri"
        )
        return filePath
    }
    val imgId: String = m.group()
    val column = arrayOf(MediaStore.Images.Media.DATA)
    val sel = MediaStore.Images.Media._ID + "=?"
    val cursor: Cursor? = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        column, sel, arrayOf(imgId), null
    )
    val columnIndex: Int? = cursor?.getColumnIndex(column[0])
    if (cursor != null) {
        if (cursor.moveToFirst()) {
            filePath = columnIndex?.let { cursor.getString(it) }.toString()
        }
    }
    cursor?.close()
    return filePath
}
    private fun customImageSelectionDialog() {
        val dialog = Dialog(this@DetailActivity)
        val binding: DialogCustomImageSelectionBinding =
                DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        binding.tvCamera.setOnClickListener {
            Dexter.withContext(this@DetailActivity)
                    .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                            report?.let {
                                // Here after all the permission are granted launch the CAMERA to capture an image.
                                if (report.areAllPermissionsGranted()) {

                                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                    startActivityForResult(intent, CAMERA)
                                }
                            }
                        }
                        override fun onPermissionRationaleShouldBeShown(
                                permission: MutableList<PermissionRequest>?,
                                token: PermissionToken?
                        ) {
                            showRationalDialogForPermissions()
                        }
                    }).onSameThread()
                    .check()

            dialog.dismiss()
        }
        binding.tvGallery.setOnClickListener {
            Dexter.withContext(this@DetailActivity)
                    .withPermission(
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            val galleryIntent = Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                            startActivityForResult(galleryIntent, GALLERY)
                        }
                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                            Toast.makeText(
                                    this@DetailActivity,
                                    "You have denied the storage permission to select image.",
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                        override fun onPermissionRationaleShouldBeShown(
                                permission: PermissionRequest?,
                                token: PermissionToken?
                        ) {
                            showRationalDialogForPermissions()
                        }
                    }).onSameThread()
                    .check()

            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
                .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
                .setPositiveButton(
                        "GO TO SETTINGS"
                ) { _, _ ->
                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }.show()
    }

    @ExperimentalStdlibApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //This code is to pass the value to Fragment
        Log.d("Image", "$resultCode $requestCode $data")
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA) {

                data?.extras?.let {
                    val thumbnail: Bitmap =
                            data.extras!!.get("data") as Bitmap // Bitmap from camera
                    mImagePath = saveImageToInternalStorage(thumbnail)
                    Log.d("ImagePath", mImagePath)
                    Log.d("Imagess",thumbnail.toString())
                    runClassifier(thumbnail)

                }
            } else if (requestCode == GALLERY) {

                data?.let {
                    // Here we will get the select image URI.
                    val uri = data.data
                    Log.d("uriImageBeforSend", uri.toString())

                    val thumbnail = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    runClassifier(thumbnail)
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "Cancelled")
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    @SuppressLint("SetTextI18n")
    @ExperimentalStdlibApi
    private fun runClassifier(bitmap: Bitmap) {
        runOnUiThread {
            Glide.with(this@DetailActivity)
                    .load(bitmap)
                    .centerCrop()
                    .apply( RequestOptions().override(224, 224))
                    .into(binding.ivImage)
        }
        Log.d("bitmap classifier", bitmap.toString())
        val results: List<Classifier.Recognition> =
            classifier.recognizeImage(bitmap)
        val result = classifier.recognizeImage(bitmap)
        Log.d("results", results.toString())
        Log.d("result", result.toString())
        val txtAccurate = getString(R.string.txtAccurate)
        try {
            val percentage = result[0].confidence
            val percentConfidence = PercentageHelper.toPercentage(percentage, 2)
            if (percentConfidence > PercentageHelper.toPercentage(0.70f, 2)) {
                binding.fabCamera.visibility = View.GONE
                binding.tbAudio.visibility = View.VISIBLE
                runOnUiThread {
                    val title = result[0].title
                    val trim = replaceSpace(title).lowercase()
                    Log.d("trim", trim)
                    val docRef = db.collection(getString(R.string.collection_corak)).document(trim)
                    docRef.get()
                        .addOnSuccessListener { document ->
                            Log.d("ada", "DocumentSnapshot data: ${document.data}")
                            val name = document.getString("name")
                            val origin = document.getString("origin")
                            val characteristic = document.getString("history")
                            val philosophy = document.getString("philosophy")
                            binding.origin.text = origin
                            binding.characteristic.text = characteristic
                            binding.philosophy.text = philosophy
                            binding.batikName.text = name
                            val audioUrl = document.getString("audio")

                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                            binding.tbAudio.setOnClickListener {

                                val name = document.getString("name")
                                if (!binding.tbAudio.isChecked) {
                                    binding.tbAudio.isChecked = false
                                    mediaPlayer.stop()
                                    mediaPlayer.reset()
                                    mediaPlayer.setVolume(0F, 0F)
                                    val stop = getString(R.string.stopping_audio)
                                    Snackbar.make(
                                        window.decorView.findViewById(android.R.id.content),
                                        "$stop $name",
                                        Snackbar.LENGTH_SHORT
                                    )
                                        .show()

                                } else {
                                    if (mediaPlayer.isPlaying) {
                                        Log.d("Debug Audio", mediaPlayer.toString())
                                        mediaPlayer.reset()
                                    } else {
                                        try {
                                            mediaPlayer.setDataSource(audioUrl)
                                            mediaPlayer.prepare()
                                            mediaPlayer.isLooping = false
                                            mediaPlayer.setVolume(1F, 1F)
                                            mediaPlayer.start()
                                        } catch (e: IOException) {
                                            e.printStackTrace()
                                        }
                                    }
                                    val play = getString(R.string.playing_audio)
                                    Snackbar.make(
                                        window.decorView.findViewById(android.R.id.content),
                                        "$play $name",
                                        Snackbar.LENGTH_SHORT
                                    )
                                        .show()
                                    binding.tbAudio.isChecked = true
                                }

                            }
                            binding.fav.setOnClickListener {
                                val favBatikDetails = FavBatik(
                                    pathImage.toString(),
                                    Constants.BATIK_IMAGE_SOURCE_LOCAL,
                                    percentConfidence,
                                    audioUrl.toString(),
                                    name.toString(),
                                    origin.toString(),
                                    characteristic.toString(),
                                    philosophy.toString(),
                                    false
                                )
                                Log.d("Data Favorite", favBatikDetails.toString())
                                if(!favBatikDetails.favoriteBatik){
                                    mFavBatikViewModel.insert(favBatikDetails)
                                    binding.fav.visibility = View.GONE

                                }
                                val save_batik = getString(R.string.save_batik)

                                Toast.makeText(
                                    this@DetailActivity,
                                    "$save_batik $name",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // You even print the log if Toast is not displayed on emulator
                                Log.e("Insertion", "Success")
                            }

                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "get failed with ", exception)
                        }

//                    binding.confidence.text = "$percentConfidence $txtAccurate"
                    binding.notFound.visibility = View.GONE
//                    binding.confidence.visibility = View.VISIBLE
                    binding.txtName.visibility = View.VISIBLE
                    binding.batikName.visibility = View.VISIBLE
                    binding.txtOrigin.visibility = View.VISIBLE
                    binding.origin.visibility = View.VISIBLE
                    binding.txtCharacteristic.visibility = View.VISIBLE
                    binding.characteristic.visibility = View.VISIBLE
                    binding.txtPhilosophy.visibility = View.VISIBLE
                    binding.philosophy.visibility = View.VISIBLE
                    binding.fav.visibility = View.VISIBLE
                    binding.tbAudio.visibility = View.VISIBLE


                }
            } else {
                runOnUiThread {
                    binding.notFound.visibility = View.VISIBLE
//                    binding.confidence.text = "$percentConfidence $txtAccurate"
//                    binding.confidence.visibility = View.VISIBLE
                    binding.txtName.visibility = View.GONE
                    binding.batikName.visibility = View.GONE
                    binding.txtOrigin.visibility = View.GONE
                    binding.origin.visibility = View.GONE
                    binding.txtCharacteristic.visibility = View.GONE
                    binding.characteristic.visibility = View.GONE
                    binding.txtPhilosophy.visibility = View.GONE
                    binding.philosophy.visibility = View.GONE
                    binding.fav.visibility = View.GONE
                    binding.tbAudio.visibility = View.GONE
                    binding.linearAudio.visibility = View.GONE
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            Log.e(ContentValues.TAG, "ArrayIndexOutOfBoundsException: " + e.message)
//                Toast.makeText(
//                        this, "Error : " + e.message, Toast.LENGTH_LONG
//                )
//                    .show()
            runOnUiThread {
                binding.notFound.visibility = View.VISIBLE
//                binding.confidence.visibility = View.GONE
                binding.txtName.visibility = View.GONE
                binding.batikName.visibility = View.GONE
                binding.txtOrigin.visibility = View.GONE
                binding.origin.visibility = View.GONE
                binding.txtCharacteristic.visibility = View.GONE
                binding.characteristic.visibility = View.GONE
                binding.txtPhilosophy.visibility = View.GONE
                binding.philosophy.visibility = View.GONE
                binding.fav.visibility = View.GONE
                binding.tbAudio.visibility = View.GONE
                binding.linearAudio.visibility = View.GONE
            }
        }

    }

    private fun initFireabase() {
        FirebaseApp.initializeApp(this)
        db.collection("corak")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("dapet", "${document.id} => ${document.data}")
                    document.getString("batik-parang")

                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    private fun initClassifier() {
        classifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)
    }
    private fun replaceSpace(str: String): String {
        var s = ""
        for (i in str.indices) {
            s += if (str[i] == ' ') '-' else str[i]
        }
        return s
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.tbAudio.isChecked = false
        mediaPlayer.reset()
    }


}