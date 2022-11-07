package com.Alkemy.alkemybankbase.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.Alkemy.alkemybankbase.databinding.FragmentMovementBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovementFragment : Fragment() {
    private var _binding : FragmentMovementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentMovementBinding.inflate(inflater,container,false)
        return binding.root
    }
}