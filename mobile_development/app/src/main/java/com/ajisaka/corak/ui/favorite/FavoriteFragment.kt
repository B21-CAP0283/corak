package com.ajisaka.corak.ui.favorite

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajisaka.corak.MainActivity
import com.ajisaka.corak.R
import com.ajisaka.corak.adapters.FavBatikAdapter
import com.ajisaka.corak.application.FavBatikApplication
import com.ajisaka.corak.databinding.FragmentFavoriteBinding
import com.ajisaka.corak.model.entities.FavBatik
import com.ajisaka.corak.ui.home.HomeFragmentDirections
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

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        (activity as AppCompatActivity?)?.supportActionBar?.setShowHideAnimationEnabled(false)
        binding.includeToolbar.btnSetting.setOnClickListener {
            findNavController()
                .navigate(FavoriteFragmentDirections.actionNavigationFavoriteToNavigationSetting())
        }
        mFavBatikViewModel.favoriteBatik.observe(viewLifecycleOwner) { batik ->
            batik.let {
                Log.d("BatikLIst" , batik.toString())
                binding.rvBatikList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                val favBatikAdapter = FavBatikAdapter(this@FavoriteFragment)
                binding.rvBatikList.adapter = favBatikAdapter
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
    fun batikDetails(favBatik: FavBatik){
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }

        findNavController()
            .navigate(FavoriteFragmentDirections.actionNavigationFavoriteToNavigationBatikDetails(favBatik))
    }

    override fun onResume() {
        super.onResume()

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }

}