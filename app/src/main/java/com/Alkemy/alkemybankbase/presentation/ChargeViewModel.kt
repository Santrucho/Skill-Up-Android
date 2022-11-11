package com.Alkemy.alkemybankbase.presentation

import androidx.lifecycle.ViewModel
import com.Alkemy.alkemybankbase.repository.charge.ChargeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChargeViewModel @Inject constructor(private val chargeRepo : ChargeRepository) : ViewModel() {

    //Just putting it here in case anybody needs it
    //LocalDate localDate = LocalDate.now(); Output is 2022-10-11. Now change format.
    //String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd, mm, yyyy"))
    //Output is now 10-11-2022
    fun validateForm(amount:Int,concept:String,date:String,currency:String){

    }
}
