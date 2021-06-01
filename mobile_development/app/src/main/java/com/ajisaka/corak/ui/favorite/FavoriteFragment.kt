package com.ajisaka.corak.ui.favorite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajisaka.corak.R
import com.ajisaka.corak.adapters.FavBatikAdapter
import com.ajisaka.corak.application.FavBatikApplication
import com.ajisaka.corak.databinding.FragmentFavoriteBinding
import com.ajisaka.corak.model.entities.FavBatik
import com.ajisaka.corak.viewmodel.FavBatikViewModel
import com.ajisaka.corak.viewmodel.FavBatikViewModelFactory

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val mFavBatikViewModel: FavBatikViewModel by viewModels {
        FavBatikViewModelFactory((requireActivity().application as FavBatikApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvBatikList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val favBatikAdapter = FavBatikAdapter(this@FavoriteFragment)
        binding.rvBatikList.adapter = favBatikAdapter
        mFavBatikViewModel.allBatikList.observe(viewLifecycleOwner) { batik ->
            batik.let {
                Log.d("BatikLIst" , batik.toString())

                if (it.isNotEmpty()) {

                    binding.rvBatikList.visibility = View.VISIBLE
                    binding.tvNoBatikAddedYet.visibility = View.GONE

                    favBatikAdapter.batikList(it)
                } else {

                    binding.rvBatikList.visibility = View.GONE
                    binding.tvNoBatikAddedYet.visibility = View.VISIBLE
                }
            }
        }
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
            mFavBatikViewModel.delete(batik)
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
}