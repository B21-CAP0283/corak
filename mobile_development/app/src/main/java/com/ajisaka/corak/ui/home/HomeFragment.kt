package com.ajisaka.corak.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajisaka.corak.MainActivity
import com.ajisaka.corak.R
import com.ajisaka.corak.adapters.FavBatikHomeAdapter
import com.ajisaka.corak.application.FavBatikApplication
import com.ajisaka.corak.databinding.DialogCustomImageSelectionBinding
import com.ajisaka.corak.databinding.FragmentHomeBinding
import com.ajisaka.corak.model.entities.FavBatik
import com.ajisaka.corak.ui.detail.DetailActivity
import com.ajisaka.corak.ui.games.GamesFragment
import com.ajisaka.corak.ui.setting.SettingFragment
import com.ajisaka.corak.viewmodel.FavBatikViewModel
import com.ajisaka.corak.viewmodel.FavBatikViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.*
import java.util.*


@Suppress("DEPRECATION", "UNREACHABLE_CODE")
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val mBatikViewModel: FavBatikViewModel by viewModels {
        FavBatikViewModelFactory((requireActivity().application as FavBatikApplication).repository)
    }
    private var mImagePath: String = ""
    companion object {
        private const val CAMERA = 1

        private const val GALLERY = 2
        private const val IMAGE_DIRECTORY = "FavBatikImages"
    }
    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        (activity as AppCompatActivity?)?.supportActionBar?.setShowHideAnimationEnabled(false)
        binding.includeToolbar.btnSetting.setOnClickListener {
            findNavController()
                .navigate(HomeFragmentDirections.actionNavigationHomeToNavigationSetting())
        }
        binding.btnPlayGames.setOnClickListener {
            findNavController()
                .navigate(HomeFragmentDirections.actionNavigationHomeToNavigationGames())
        }

        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(activity, 2)
        binding.rvList.layoutManager = layoutManager

        val favBatikHomeAdapter = FavBatikHomeAdapter(this)
        Log.d("Batik Home", favBatikHomeAdapter.toString())
        mBatikViewModel.allBatikList.observe(viewLifecycleOwner) { batik ->
            batik.let {


                if (it.isNotEmpty()) {

                    binding.rvList.visibility = View.VISIBLE
                    binding.tvNoBatikAddedYet.visibility = View.GONE
                    favBatikHomeAdapter.batikList(batik)
                    binding.rvList.adapter = favBatikHomeAdapter

                    Log.d("BatikLIst Home", batik.toString())

                } else {
                    binding.tvNoBatikAddedYet.visibility = View.VISIBLE
                    binding.rvList.visibility = View.GONE
                }
            }
        }

        // button Take Camera
        binding.button.setOnClickListener{ customImageSelectionDialog() }
        return binding.root

    }
    override fun onResume() {
        super.onResume()

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }
    fun batikDetails(favBatik: FavBatik){
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }

        findNavController()
            .navigate(HomeFragmentDirections.actionNavigationHomeToNavigationBatikDetails(favBatik))
    }
    fun deleteStudent(batik: FavBatik) {
        val builder = AlertDialog.Builder(requireContext())
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.title_delete_batik))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.msg_delete_batik_dialog, batik.name))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)) { dialogInterface, _ ->
            mBatikViewModel.delete(batik)
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        //performing negative action
        builder.setNegativeButton(resources.getString(R.string.lbl_no)) { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }
    override fun onActivityResult(resultCode: Int, requestCode: Int, data: Intent?) {
        super.onActivityResult(resultCode, requestCode, data)
        Log.d("Image", "$resultCode $requestCode $data")
        if (requestCode == Activity.RESULT_OK) {
            if (resultCode == CAMERA) {

                data?.extras?.let {
                    val thumbnail: Bitmap =
                            data.extras!!.get("data") as Bitmap // Bitmap from camera
                    mImagePath = saveImageToInternalStorage(thumbnail)
                    Log.d("ImagePath", mImagePath)
                    Log.d("Imagess",thumbnail.toString())
                    //Convert to byte array
//                    val stream = ByteArrayOutputStream()
//                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream)
//                    val byteArray: ByteArray = stream.toByteArray()
                    val sendImage = Intent(context, DetailActivity::class.java)
                    sendImage.putExtra("resultImage", mImagePath)
                    startActivity(sendImage)


                }
            } else if (resultCode == GALLERY) {

                data?.let {
                    // Here we will get the select image URI.
                    val uri = data.data
                    Log.d("uriImageBeforSend", uri.toString())

//                    val thumbnail = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
//                    mImagePath = saveImageToInternalStorage(thumbnail)
//                    val stream = ByteArrayOutputStream()
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//                    val byteArray: ByteArray = stream.toByteArray()
                    val sendImage = Intent(context, DetailActivity::class.java)
                    sendImage.putExtra("uriImage", uri.toString())
                    startActivity(sendImage)
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "Cancelled")
        }
    }
    private fun saveImageToInternalStorage(bitmap: Bitmap): String {

        // Get the context wrapper instance
        val wrapper = ContextWrapper(context)

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
    private fun customImageSelectionDialog() {
        val dialog = Dialog(requireContext())

        val binding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {

            Dexter.withContext(context)
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

            Dexter.withContext(context)
                .withPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(object : PermissionListener {

                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                        // Here after all the permission are granted launch the gallery to select and image.
                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )

                        startActivityForResult(galleryIntent, GALLERY)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        Toast.makeText(
                            context,
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

        //Start the dialog and display it on screen.
        dialog.show()
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(requireContext())
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context?.packageName, null)
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

}