package com.ajisaka.corak.ui.detail

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.ajisaka.corak.R
import com.ajisaka.corak.application.FavBatikApplication
import com.ajisaka.corak.databinding.ActivityDetailBinding
import com.ajisaka.corak.helpers.PercentageHelper
import com.ajisaka.corak.model.entities.FavBatik
import com.ajisaka.corak.tflite.Classifier
import com.ajisaka.corak.utils.Constants
import com.ajisaka.corak.viewmodel.FavBatikViewModel
import com.ajisaka.corak.viewmodel.FavBatikViewModelFactory
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private val mediaPlayer = MediaPlayer()
    private lateinit var binding: ActivityDetailBinding
    private var db = Firebase.firestore
    private var mImagePath: String = ""
    companion object {
        const val EXTRA_NAME = "EXTRA_NAME"
        const val EXTRA_CONFIDENCE = "EXTRA_CONFIDENCE"
        const val EXTRA_ORIGIN = "EXTRA_ORIGIN"
        const val EXTRA_IMAGE = "EXTRA_IMAGE"
        const val EXTRA_AUDIO = "EXTRA_AUDIO"
        const val EXTRA_CHARACTERISTIC = "EXTRA_CHARACTERISTIC"
        const val EXTRA_PHILOSOPHY = "EXTRA_PHILOSOPHY"

        private const val IMAGE_DIRECTORY = "FavBatikImages"
    }
    private val mFavBatikViewModel: FavBatikViewModel by viewModels {
        FavBatikViewModelFactory((application as FavBatikApplication).repository)
    }
    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }
    private var title: String = "Detail Batik"
    private val mInputSize = 224
    private val mModelPath = "corak.tflite"
    private val mLabelPath = "label.txt"
    private lateinit var classifier: Classifier


    @SuppressLint("UseCompatLoadingForDrawables")
    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.navigationIcon = resources.getDrawable(
            R.drawable.ic_baseline_arrow_back_24,
            null
        )
        binding.toolbar.setNavigationOnClickListener { finish() }

        initDataOffline()
        if (intent.getByteArrayExtra("image") != null){
            initClassifier()
            initFireabase()
            val byteArray = intent.getByteArrayExtra("image")
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
            mImagePath = saveImageToInternalStorage(bitmap)
            Log.i("ImagePath", mImagePath)
            // Set Capture Image bitmap to the imageView using Glide
            Glide.with(this@DetailActivity)
                .load(bitmap)
                .centerCrop()
                .into(binding.ivImage)
            // classification with intent
            runClassifier(bitmap)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initDataOffline() {
        binding.batikName.text = intent.getStringExtra(EXTRA_NAME)
        val txtProfile = binding.batikName.text
        title = txtProfile.toString()
        binding.toolbar.title = title
        setActionBarTitle(title)
        val txtAccurate = getString(R.string.txtAccurate)
        val confidence = intent.getStringExtra(EXTRA_CONFIDENCE)
        binding.confidence.text = "$confidence $txtAccurate"
        binding.origin.text = intent.getStringExtra(EXTRA_ORIGIN)
        binding.characteristic.text = intent.getStringExtra(EXTRA_CHARACTERISTIC)
        binding.philosophy.text = intent.getStringExtra(EXTRA_PHILOSOPHY)
        Glide.with(applicationContext)
            .load(intent.getStringExtra(EXTRA_IMAGE))
            .placeholder(R.drawable.img_not_found)
            .into(binding.ivImage)
        binding.fav.visibility = View.GONE
        binding.tbAudio.setOnClickListener {

            val name = intent.getStringExtra(EXTRA_NAME)
            if (!binding.tbAudio.isChecked) {
                binding.tbAudio.isChecked = false
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.setVolume(0F, 0F)
                Toast.makeText(this, "Stopping Audio $name", Toast.LENGTH_SHORT)
                    .show()

            } else {
                if (mediaPlayer.isPlaying) {
                    Log.d("Debug Audio", mediaPlayer.toString())
                    mediaPlayer.reset()
                } else {
                    try {
                        mediaPlayer.setDataSource(intent.getStringExtra(EXTRA_AUDIO))
                        mediaPlayer.prepare()
                        mediaPlayer.isLooping = false
                        mediaPlayer.setVolume(1F, 1F)
                        mediaPlayer.start()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                Toast.makeText(this, "Playing Audio $name", Toast.LENGTH_SHORT)
                    .show()
                binding.tbAudio.isChecked = true
            }

        }

    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {

        // Get the context wrapper instance
        val wrapper = ContextWrapper(applicationContext)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        /**
         * The Mode Private here is
         * File creation mode: the default mode, where the created file can only
         * be accessed by the calling application (or all applications sharing the
         * same user ID).
         */
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)

        // Mention a file name to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException) { // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image absolute path
        return file.absolutePath
    }

    @SuppressLint("SetTextI18n")
    @ExperimentalStdlibApi
    private fun runClassifier(bitmap: Bitmap) {
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
            if (percentConfidence > PercentageHelper.toPercentage(0.80f, 2)) {
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
                                    Toast.makeText(this, "Stopping Audio $name", Toast.LENGTH_SHORT)
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
                                    Toast.makeText(this, "Playing Audio $name", Toast.LENGTH_SHORT)
                                        .show()
                                    binding.tbAudio.isChecked = true
                                }

                            }
                            binding.fav.setOnClickListener {
                                val favBatikDetails = FavBatik(
                                    mImagePath,
                                    Constants.BATIK_IMAGE_SOURCE_LOCAL,
                                    percentConfidence,
                                    audioUrl.toString(),
                                    name.toString(),
                                    origin.toString(),
                                    characteristic.toString(),
                                    philosophy.toString(),
                                    true
                                )
                                Log.d("Data Favorite", favBatikDetails.toString())
                                if(!favBatikDetails.favoriteBatik){
                                    binding.fav.visibility = View.GONE
                                }else{
                                    mFavBatikViewModel.insert(favBatikDetails)
                                }

                                Toast.makeText(
                                    this@DetailActivity,
                                    "You successfully added your favorite dish details.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // You even print the log if Toast is not displayed on emulator
                                Log.e("Insertion", "Success")
                            }

                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "get failed with ", exception)
                        }

                    binding.confidence.text = "$percentConfidence $txtAccurate"
                    binding.notFound.visibility = View.GONE
                    binding.confidence.visibility = View.VISIBLE
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
                    binding.confidence.text = "$percentConfidence $txtAccurate"
                    binding.confidence.visibility = View.VISIBLE
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
                binding.confidence.visibility = View.GONE
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