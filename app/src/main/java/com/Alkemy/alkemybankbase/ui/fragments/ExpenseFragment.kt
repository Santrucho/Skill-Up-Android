package com.Alkemy.alkemybankbase.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.Alkemy.alkemybankbase.databinding.FragmentExpenseBinding
import com.Alkemy.alkemybankbase.presentation.ExpensesViewModel
import com.Alkemy.alkemybankbase.utils.afterTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpenseFragment : Fragment() {
    private var _binding: FragmentExpenseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExpensesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            etAmount.afterTextChanged {
                viewModel.validateForm(
                    etConcept.text.toString(),
                    etAmount.text.toString().toIntOrNull() ?: 0
                )
            }
            etConcept.afterTextChanged {
                viewModel.validateForm(
                    etConcept.text.toString(),
                    etAmount.text.toString().toIntOrNull() ?: 0
                )
            }
            btnAddExpense.isEnabled = false
        }
    }

    private fun setupObservers() {
        viewModel.isFormValidLiveData.observe(viewLifecycleOwner) {
            binding.btnAddExpense.isEnabled = it
        }

        viewModel.conceptErrorLiveData.observe(viewLifecycleOwner) {
            binding.etConcept.error = it
        }
        viewModel.amountErrorResourceLiveData.observe(viewLifecycleOwner) {
            binding.etAmount.error = it
        }
    }


}