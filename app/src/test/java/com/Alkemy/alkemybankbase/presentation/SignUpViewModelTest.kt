package com.Alkemy.alkemybankbase.presentation

import com.Alkemy.alkemybankbase.R
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.Alkemy.alkemybankbase.repository.FakeSignUpRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import org.junit.rules.TestRule

class SignUpViewModelTest {
    @get:Rule var rule: TestRule = InstantTaskExecutorRule()
    //Needed Rule to test LiveData changes

    private lateinit var viewModel : SignUpViewModel

    @Before
    fun setup() {
        viewModel = SignUpViewModel(FakeSignUpRepository())
    }

    //In the cases of empty fields, regardless of which one is empty, the ViewModel should set
    //isFormValidLiveData's value to false, thus the reason I assert that value in the next tests

    @Test
    fun validateForm_emptyFirstName_signupFails(){
        viewModel.validateForm(
            "", "Clivio", "example@gmail.com", "Password1", "Password1"
        )
        assertThat(viewModel.isFormValidLiveData.value).isFalse()
    }

    @Test
    fun validateForm_emptyLastName_signupFails(){
        viewModel.validateForm(
            "Marcelo", "", "example@gmail.com", "Password1", "Password1"
        )
    }

    @Test
    fun validateForm_emptyEmail_signupFails(){
        viewModel.validateForm(
            "Marcelo", "Clivio", "", "Password1", "Password1"
        )
        assertThat(viewModel.isFormValidLiveData.value).isFalse()
    }

    @Test
    fun validateForm_emptyPassword_signupFails(){
        viewModel.validateForm(
            "Marcelo", "Clivio", "example@gmail.com", "", "Password1"
        )
        assertThat(viewModel.isFormValidLiveData.value).isFalse()
    }

    @Test
    fun validateForm_emptyPasswordConfirmation_signupFails(){
        viewModel.validateForm(
            "Marcelo", "Clivio", "example@gmail.com", "Password1", ""
        )
        assertThat(viewModel.isFormValidLiveData.value).isFalse()
    }

    @Test
    fun validateForm_emptyFields_signupFails(){
        viewModel.validateForm(
            "", "", "", "", ""
        )
        assertThat(viewModel.isFormValidLiveData.value).isFalse()
    }

    //In the following tests, isFormValidLiveData's value is false due to specific reasons, hence
    //the need to assert the specific vals with their correspondent string error messages
    @Test
    fun validateForm_invalidFirstName_signupFails(){
        viewModel.validateForm(
            "999999999999", "Clivio", "example@gmail.com", "Password1", "Password1"
        )
        assertThat(viewModel.firstnameErrorResourceLiveData.value).isEqualTo(R.string.firstname_error)
    }

    @Test
    fun validateForm_invalidLastName_signupFails(){
        viewModel.validateForm(
            "Marcelo", "999999999999", "example@gmail.com", "Password1", "Password1"
        )
        assertThat(viewModel.lastnameErrorResourceLiveData.value).isEqualTo(R.string.lastname_error)
    }

    @Test
    fun validateForm_shortPassword_signupFails(){
        viewModel.validateForm(
            "Marcelo", "Clivio", "example@gmail.com", "Pass1", "Pass1"
        )
        assertThat(viewModel.passwordErrorResourceIdLiveData.value).isEqualTo(R.string.password_error)
    }

    @Test
    fun validateForm_passwordWithoutNumber_signupFails(){
        viewModel.validateForm(
            "Marcelo", "Clivio", "example@gmail.com", "Password", "Password"
        )
        assertThat(viewModel.passwordErrorResourceIdLiveData.value).isEqualTo(R.string.password_error)
    }

    @Test
    fun validateForm_passwordWithoutUppercase_signupFails(){
        viewModel.validateForm(
            "Marcelo", "Clivio", "example@gmail.com", "password1", "password1"
        )
        assertThat(viewModel.passwordErrorResourceIdLiveData.value).isEqualTo(R.string.password_error)
    }

    @Test
    fun validateForm_differentPasswords_signupFails(){
        viewModel.validateForm(
            "Marcelo", "Clivio", "example@gmail.com", "Password1", "Password2"
        )
        assertThat(viewModel.confirmPasswordErrorResourceIdLiveData.value).isEqualTo(R.string.confirm_password_error)
    }

    @Test
    fun validateForm_invalidEmail_signupFails(){
        viewModel.validateForm(
            "Marcelo", "Clivio", "examplegmail.com", "Password1", "Password1"
        )
        assertThat(viewModel.emailErrorResourceIdLiveData.value).isEqualTo(R.string.email_error)
    }

    //A succesful sign-up is the only situation in which isFormValidLiveData's value should be true
    @Test
    fun validateForm_correctInput_signupSuccess(){
        viewModel.validateForm(
            "Marcelo", "Clivio", "example@gmail.com", "Password1", "Password1"
        )
        assertThat(viewModel.isFormValidLiveData.value).isTrue()
    }
}