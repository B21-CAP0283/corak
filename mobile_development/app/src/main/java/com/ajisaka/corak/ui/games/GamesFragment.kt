package com.ajisaka.corak.ui.games

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.ajisaka.corak.databinding.FragmentGamesBinding
import com.ajisaka.corak.ui.home.HomeFragmentDirections

class GamesFragment : Fragment() {
    private lateinit var binding: FragmentGamesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        (activity as AppCompatActivity?)?.supportActionBar?.setShowHideAnimationEnabled(false)
        binding.includeToolbar.btnSetting.setOnClickListener {
            findNavController()
                .navigate(GamesFragmentDirections.actionNavigationGamesToNavigationSetting())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentGamesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }
}