package com.Alkemy.alkemybankbase.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.repository.FakeSendRepository
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class SendViewModelTest{
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    //Needed Rule to test LiveData changes

    private lateinit var viewModel : SendViewModel

    @Before
    fun setup() {
        viewModel = SendViewModel(FakeSendRepository())
    }

    //In the cases of empty fields, regardless of which one is empty, the ViewModel should set
    //isFormValidLiveData's value to false, thus the reason I assert that value in the next tests

    /*******************************************************************************
    IF THE VALIDATEFORM() FROM CHARGEVIEWMODEL GETS IMPLEMENTED CORRECTLY, UNCOMMENT
    THESE TESTS AND RUN THEM
     *******************************************************************************/
    @Test
    fun validateForm_emptyConcept_sendFails(){
        viewModel.validateForm(
            1365, "", 200
        )
        Truth.assertThat(viewModel.isFormValidLiveData.value).isFalse()
    }

//    TEST REMOVED DUE TO API AND VIEWMODEL NOT ACTUALLY USING THE PARAMETERS SHOWN IN THE TICKET
//    @Test
//    fun validateForm_emptyDate_sendFails(){
//        viewModel.validateForm(
//            1, "Pago de honorarios", 200, "", "ARS", "Password1", "Password1"
//        )
//        Truth.assertThat(viewModel.isFormValidLiveData.value).isFalse()
//    }

//    TEST REMOVED DUE TO API AND VIEWMODEL NOT ACTUALLY USING THE PARAMETERS SHOWN IN THE TICKET
//    @Test
//    fun validateForm_emptyCurrency_sendFails(){
//        viewModel.validateForm(
//            1, "Pago de honorarios", 200, "11-11-2022", "", "Password1", "Password1"
//        )
//        Truth.assertThat(viewModel.isFormValidLiveData.value).isFalse()
//    }

//    TEST REMOVED DUE TO API AND VIEWMODEL NOT ACTUALLY USING THE PARAMETERS SHOWN IN THE TICKET
//    @Test
//    fun validateForm_emptyPassword_sendFails(){
//        viewModel.validateForm(
//            1, "Pago de honorarios", 200, "11-11-2022", "ARS", "", "Password1"
//        )
//        Truth.assertThat(viewModel.isFormValidLiveData.value).isFalse()
//    }

//    TEST REMOVED DUE TO API AND VIEWMODEL NOT ACTUALLY USING THE PARAMETERS SHOWN IN THE TICKET
//    @Test
//    fun validateForm_emptyPasswordConfirmation_sendFails(){
//        viewModel.validateForm(
//            1, "Pago de honorarios", 200, "11-11-2022", "ARS",  "Password1", ""
//        )
//        Truth.assertThat(viewModel.isFormValidLiveData.value).isFalse()
//    }

    //In the following tests, isFormValidLiveData's value is false due to specific reasons, hence
    //the need to assert the specific vals with their correspondent string error messages
    @Test
    fun validateForm_negativeAmount_sendFails(){
        viewModel.validateForm(
            1365, "Pago de honorarios", -200
        )
        Truth.assertThat(viewModel.amountErrorResourceLiveData.value).isEqualTo(R.string.amount_error)
    }

//    TEST REMOVED DUE TO API AND VIEWMODEL NOT ACTUALLY USING THE PARAMETERS SHOWN IN THE TICKET
//    @Test
//    fun validateForm_invalidCurrency_sendFails(){
//        viewModel.validateForm(
//            1, "Pago de honorarios", 200, "11-11-2022", "FSADASD", "Password1", "Password1"
//        )
//        Truth.assertThat(viewModel.currencyErrorResourceIdLiveData.value).isEqualTo(R.string.currency_error)
//    }

//    TEST REMOVED DUE TO API AND VIEWMODEL NOT ACTUALLY USING THE PARAMETERS SHOWN IN THE TICKET
//    @Test
//    fun validateForm_differentPasswords_sendFails(){
//        viewModel.validateForm(
//            1, "Pago de honorarios", 200, "11-11-2022", "ARS", "Password1", "Password2"
//        )
//        Truth.assertThat(viewModel.confirmPasswordErrorResourceIdLiveData.value).isEqualTo(R.string.confirm_password_error)
//    }

    @Test
    fun validateForm_negativeDestinationAccount_sendFails(){
        viewModel.validateForm(
            -1365, "Pago de honorarios", 200
        )
        Truth.assertThat(viewModel.toAccountIdErrorResourceLiveData.value).isEqualTo(R.string.toAccountId_error)
    }

    @Test
    fun validateForm_tooLongConcept_expenseFails(){
        viewModel.validateForm(
            1365, "Y aserejé-ja-dejé, De jebe tu de jebere seibiunouva majavi an de bugui an de güididípi", 200
        )
        Truth.assertThat(viewModel.conceptErrorResourceLiveData.value).isEqualTo(R.string.concept_error)
    }

    @Test
    fun validateForm_correctInput_sendSuccess(){
        viewModel.validateForm(
            1365, "Pago de honorarios", 200
        )
        Truth.assertThat(viewModel.isFormValidLiveData.value).isTrue()
    }
}