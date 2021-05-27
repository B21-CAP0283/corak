package com.ajisaka.corak.ui.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ajisaka.corak.R
import com.ajisaka.corak.databinding.FragmentWelcomeOneBinding

@Suppress("DEPRECATION")
class WelcomeOneFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeOneBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeOneBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnNext.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_welcome_two)
        }
    }


}