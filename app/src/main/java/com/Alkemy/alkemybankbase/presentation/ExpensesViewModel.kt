package com.Alkemy.alkemybankbase.presentation

import androidx.lifecycle.ViewModel
import com.Alkemy.alkemybankbase.repository.expense.ExpensesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(private val expensesRepo : ExpensesRepository) : ViewModel() {

    fun validateForm(concept:String,amount:Int,date:String,currency:String){

    }
}
