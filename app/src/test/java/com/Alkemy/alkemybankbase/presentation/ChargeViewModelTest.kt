package com.Alkemy.alkemybankbase.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.repository.FakeChargeRepository
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ChargeViewModelTest{
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    //Needed Rule to test LiveData changes

    private lateinit var viewModel : ChargeViewModel

    @Before
    fun setup() {
        viewModel = ChargeViewModel(FakeChargeRepository())
    }
    //(amount:Int,concept:String,date:String,currency:String){
    //In the cases of empty fields, regardless of which one is empty, the ViewModel should set
    //isFormValidLiveData's value to false, thus the reason I assert that value in the next tests

    /*******************************************************************************
     IF THE VALIDATEFORM() FROM CHARGEVIEWMODEL GETS IMPLEMENTED CORRECTLY, UNCOMMENT
    THESE TESTS AND RUN THEM
     *******************************************************************************/

    @Test
    fun validateForm_emptyConcept_chargeFails(){
        viewModel.validateForm(
            200, ""
        )
        Truth.assertThat(viewModel.isFormValidLiveData.value).isFalse()
    }

//    TEST REMOVED DUE TO API AND VIEWMODEL NOT ACTUALLY USING THE PARAMETERS SHOWN IN THE TICKET
//    @Test
//    fun validateForm_emptyCurrency_chargeFails(){
//        viewModel.validateForm(
//            200, "Deposito", "10-11-2022", ""
//        )
//        Truth.assertThat(viewModel.isFormValidLiveData.value).isFalse()
//    }

//    TEST REMOVED DUE TO API AND VIEWMODEL NOT ACTUALLY USING THE PARAMETERS SHOWN IN THE TICKET
//    @Test
//    fun validateForm_emptyDate_chargeFails(){
//        viewModel.validateForm(
//            200, "Deposito", "", "ARS"
//        )
//        Truth.assertThat(viewModel.isFormValidLiveData.value).isFalse()
//    }

    //In the following tests, isFormValidLiveData's value is false due to specific reasons, hence
    //the need to assert the specific vals with their correspondent string error messages
    @Test
    fun validateForm_invalidAmount_chargeFails(){
        viewModel.validateForm(
            -1, "Deposito"
        )
        Truth.assertThat(viewModel.amountErrorResourceLiveData.value).isEqualTo(R.string.amount_error)
    }

//    TEST REMOVED DUE TO API AND VIEWMODEL NOT ACTUALLY USING THE PARAMETERS SHOWN IN THE TICKET
//    @Test
//    fun validateForm_invalidCurrency_chargeFails(){
//        viewModel.validateForm(
//            200, "Deposito", "10-11-2022", "¯\\_(ツ)_/¯"
//        )
//        Truth.assertThat(viewModel.currencyErrorResourceIdLiveData.value).isEqualTo(R.string.currency_error)
//    }

    @Test
    fun validateForm_correctInput_chargeSuccess(){
        viewModel.validateForm(
            200, "Deposito"
        )
        Truth.assertThat(viewModel.isFormValidLiveData.value).isTrue()
    }
}