package com.ajisaka.corak.ui.detail

import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ajisaka.corak.R
import com.ajisaka.corak.application.FavBatikApplication
import com.ajisaka.corak.databinding.FragmentBatikDetailsBinding
import com.ajisaka.corak.viewmodel.FavBatikViewModel
import com.ajisaka.corak.viewmodel.FavBatikViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import java.io.IOException

class BatikDetailsFragment : Fragment() {
    private var mBinding: FragmentBatikDetailsBinding? = null
    private val mediaPlayer = MediaPlayer()
    private val mFavBatikViewModel: FavBatikViewModel by viewModels {
        FavBatikViewModelFactory((requireActivity().application as FavBatikApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentBatikDetailsBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        val args: BatikDetailsFragmentArgs by navArgs()
        args.let {
            try {
                // Load the dish image in the ImageView.
                Glide.with(requireActivity())
                    .load(it.batikDetails.image)
                    .centerCrop()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            @Nullable e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            // log exception
                            Log.e("TAG", "Error loading image", e)
                            return false // important to return false so the error placeholder can be placed
                        }
                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
//                            Palette.from(resource.toBitmap())
//                                .generate { palette ->
//                                    val intColor = palette?.vibrantSwatch?.rgb ?: 0
//                                    mBinding!!.rlDishDetailMain.setBackgroundColor(intColor)
//                                }
                            return false
                        }
                    })
                    .into(mBinding!!.ivImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mBinding!!.batikName.text = it.batikDetails.name
            mBinding!!.confidence.text = it.batikDetails.confidence // Used to make first letter capital
            mBinding!!.origin.text = it.batikDetails.origin
            mBinding!!.characteristic.text = it.batikDetails.characteristic
            mBinding!!.philosophy.text = it.batikDetails.philosophy
            mBinding!!.fav.visibility = View.GONE
            val name = it.batikDetails.name
            val audioUrl = it.batikDetails.audio
            mBinding!!.tbAudio.setOnClickListener {
                if (!mBinding!!.tbAudio.isChecked) {
                    mBinding!!.tbAudio.isChecked = false
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                    mediaPlayer.setVolume(0F, 0F)
                    val stop = getString(R.string.stopping_audio)
                    activity?.let { it1 ->
                        Snackbar.make(it1.findViewById(android.R.id.content), "$stop $name", Snackbar.LENGTH_SHORT)
                            .show()
                    }
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
                    activity?.let { it1 ->
                        Snackbar.make(it1.findViewById(android.R.id.content), "$play $name", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                    mBinding!!.tbAudio.isChecked = true
                }
            }
            if (args.batikDetails.favoriteBatik) {
                mBinding!!.ivFavoriteBatik.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite
                    )
                )
            } else {
                mBinding!!.ivFavoriteBatik.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_border
                    )
                )
            }
        }
        mBinding!!.ivFavoriteBatik.setOnClickListener {
            args.batikDetails.favoriteBatik = !args.batikDetails.favoriteBatik
            mFavBatikViewModel.update(args.batikDetails)
            if (args.batikDetails.favoriteBatik) {
                mBinding!!.ivFavoriteBatik.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite
                    )
                )
                activity?.let { it1 ->
                    Snackbar.make(it1.findViewById(android.R.id.content), R.string.msg_added_to_favorites, Snackbar.LENGTH_SHORT)
                        .show()
                }
            } else {
                mBinding!!.ivFavoriteBatik.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_border
                    )
                )
                activity?.let { it1 ->
                    Snackbar.make(it1.findViewById(android.R.id.content), R.string.msg_removed_from_favorite, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.supportActionBar!!.show()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}