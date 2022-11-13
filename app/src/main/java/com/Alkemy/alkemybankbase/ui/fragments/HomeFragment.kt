package com.Alkemy.alkemybankbase.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.Alkemy.alkemybankbase.data.local.SessionManager
import com.Alkemy.alkemybankbase.data.model.Transaction
import com.Alkemy.alkemybankbase.databinding.FragmentHomeBinding
import com.Alkemy.alkemybankbase.presentation.MovementViewModel
import com.Alkemy.alkemybankbase.ui.adapters.TransactionAdapter
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.utils.Constants.TYPE_PAYMENT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovementViewModel by viewModels()
    private lateinit var auth: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         auth = "${SessionManager.getToken(requireContext())}"
        viewModel.getAllAccounts(auth)
        initRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun initRecyclerView() {
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupObservers() {
        viewModel.isLoadingLiveData.observe(viewLifecycleOwner) {
            // show loading and hide ui
            binding.progressBar.isVisible = it
            binding.clHome.isVisible = !it
            if (it) {
                binding.tvError.isVisible = !it
                binding.btnRetry.isVisible = !it
            }
        }
        viewModel.allTransactionLiveData.observe(viewLifecycleOwner) { transactionList ->
            //show list
            binding.rvTransactions.adapter =
                TransactionAdapter(transactionList.take(5)) { transaction ->
                    onTransactionSelected(transaction)
                }
            binding.tvEmptyTransaction.isVisible = transactionList.isEmpty()
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) { resId ->
            //show error message
            binding.clHome.isVisible = false
            binding.tvError.isVisible = true
            binding.btnRetry.isVisible = true
            binding.tvError.text = getString(resId)

        }
        viewModel.balance.observe(viewLifecycleOwner) { balance ->
            binding.tvAmount.text = "$".plus(balance)
        }
    }

    private fun onTransactionSelected(transaction: Transaction) {
        // todo navigate to the details screen
        Toast.makeText(requireContext(), "${transaction.id}", Toast.LENGTH_SHORT).show()

    }

    private fun setupListeners() {
        binding.btnVerMas.setOnClickListener {
            setCurrentFragment(MovementFragment())
        }
        binding.btnRetry.setOnClickListener {
            viewModel.retry(auth)
        }
    }

    //use parent fragment manager to navigate to other fragment
    private fun setCurrentFragment(fragment: Fragment) =
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

}