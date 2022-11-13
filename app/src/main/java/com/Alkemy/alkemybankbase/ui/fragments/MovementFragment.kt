package com.Alkemy.alkemybankbase.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.Alkemy.alkemybankbase.data.local.SessionManager
import com.Alkemy.alkemybankbase.databinding.FragmentMovementBinding
import com.Alkemy.alkemybankbase.presentation.MovementViewModel
import com.Alkemy.alkemybankbase.ui.adapters.TransactionAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovementFragment : Fragment() {
    private var _binding: FragmentMovementBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovementViewModel by viewModels()
    private lateinit var auth: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentMovementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = "${SessionManager.getToken(requireContext())}"
        viewModel.getAllAccounts(auth)
        initRecyclerView()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
            binding.rvTransactions.isVisible = !it
        }
        viewModel.allTransactionLiveData.observe(viewLifecycleOwner) { transactionList ->
            binding.rvTransactions.adapter = TransactionAdapter(transactionList) {}

        }
    }

    private fun initRecyclerView() {
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
    }
}