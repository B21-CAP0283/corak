package com.ajisaka.corak

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ajisaka.corak.databinding.ActivityMainBinding
import com.ajisaka.corak.helpers.PercentageHelper
import com.ajisaka.corak.tflite.Classifier
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var db = Firebase.firestore
    private val mInputSize = 224
    private val mModelPath = "corak.tflite"
    private val mLabelPath = "label.txt"
    private lateinit var classifier: Classifier
    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFireabase()
        initClassifier()
        val byteArray = intent.getByteArrayExtra("image")

        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
        binding.ivImage.setImageBitmap(bitmap)
//        val bit = ((binding.ivImage).drawable as BitmapDrawable).bitmap
        val result = classifier.recognizeImage(bitmap)
        Log.d("result", result.toString())
        try {
            val ta = "Akurasi : "
            val percentage = result[0].confidence
            val ok = PercentageHelper.toPercentage(percentage, 2)
            if (ok > PercentageHelper.toPercentage(0.80f, 2)) {
                val title = result[0].title
                val trim = replaceSpace(title).lowercase()
                Log.d("trim", trim)
                val docRef = db.collection("corak").document(trim)
                docRef.get()
                    .addOnSuccessListener { document ->
                        Log.d("ada", "DocumentSnapshot data: ${document.data}")
                        binding.origin.text = document.getString("origin")
                        binding.characteristic.text = document.getString("history")
                        binding.philosophy.text = document.getString("philosophy")
                        binding.batikName.text = document.getString("name")

                        binding.btnAudio.setOnClickListener{
                            val audioUrl = document.getString("audio")
                            val mediaPlayer = MediaPlayer()

                            if (mediaPlayer.isPlaying) {
                                mediaPlayer.stop()
                                mediaPlayer.prepareAsync()
                                mediaPlayer.seekTo(0)
                                binding.btnAudio.text = "Play"
                            } else {
                                mediaPlayer.start()
                                binding.btnAudio.text = "Pause"
                            }

                            @Suppress("DEPRECATION")
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                            try {
                                mediaPlayer.setDataSource(audioUrl)
                                mediaPlayer.prepare()
                                binding.btnAudio.text = "Pause"

                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "get failed with ", exception)
                    }


                binding.confidence.text = "$ok Accurate with"
            } else {
                binding.notFound.visibility = View.VISIBLE
                binding.confidence.text = "$ok Accurate with"
                binding.txtName.visibility = View.GONE
                binding.batikName.visibility = View.GONE
                binding.txtOrigin.visibility = View.GONE
                binding.origin.visibility = View.GONE
                binding.txtCharacteristic.visibility = View.GONE
                binding.characteristic.visibility = View.GONE
                binding.txtPhilosophy.visibility = View.GONE
                binding.philosophy.visibility = View.GONE
                binding.fav.visibility = View.GONE
                binding.btnAudio.visibility = View.GONE
            }
        }catch (e: IndexOutOfBoundsException) {
            Log.e(ContentValues.TAG, "ArrayIndexOutOfBoundsException: " + e.message)
            Toast.makeText(
                this, "Error : " + e.message, Toast.LENGTH_LONG)
                .show()
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
            binding.btnAudio.visibility = View.GONE

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

}