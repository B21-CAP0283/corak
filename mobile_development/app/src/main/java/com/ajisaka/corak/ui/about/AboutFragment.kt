package com.ajisaka.corak.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ajisaka.corak.R
import com.ajisaka.corak.databinding.FragmentAboutBinding
import com.ajisaka.corak.databinding.FragmentFavoriteBinding
import com.ajisaka.corak.ui.favorite.FavoriteFragmentDirections

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.includeToolbar.btnSetting.setOnClickListener {
            findNavController()
                .navigate(AboutFragmentDirections.actionNavigationAboutToNavigationSetting())
        }

    }
}