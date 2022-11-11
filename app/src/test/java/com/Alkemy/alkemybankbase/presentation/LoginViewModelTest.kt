package com.Alkemy.alkemybankbase.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.repository.FakeLoginRepository
import com.google.common.truth.Truth
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class LoginViewModelTest{
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    //Needed Rule to test LiveData changes

    private lateinit var viewModel : LoginViewModel

    @Before
    fun setup() {
        viewModel = LoginViewModel(FakeLoginRepository())
    }

    //In the cases of empty fields, regardless of which one is empty, the ViewModel should set
    //isFormValidLiveData's value to false, thus the reason I assert that value in the next tests
    @Test
    fun validateForm_emptyEmail_loginFails(){
        viewModel.validateForm(
            "", "Password1"
        )
        Truth.assertThat(viewModel.isFormValidLiveData.value).isFalse()
    }

    @Test
    fun validateForm_emptyPassword_loginFails(){
        viewModel.validateForm(
            "example@gmail.com", ""
        )
        Truth.assertThat(viewModel.isFormValidLiveData.value).isFalse()
    }

    @Test
    fun validateForm_emptyFields_loginFails(){
        viewModel.validateForm(
            "", ""
        )
        Truth.assertThat(viewModel.isFormValidLiveData.value).isFalse()
    }

    //In the following tests, isFormValidLiveData's value is false due to specific reasons, hence
    //the need to assert the specific vals with their correspondent string error messages
    @Test
    fun validateForm_invalidEmail_loginFails(){
        viewModel.validateForm(
            "examplegmail.com", "Password1"
        )
        Truth.assertThat(viewModel.emailErrorResourceIdLiveData.value).isEqualTo(R.string.email_error)
    }

    @Test
    fun validateForm_shortPassword_loginFails(){
        viewModel.validateForm(
            "example@gmail.com", "Pass1"
        )
        Truth.assertThat(viewModel.passwordErrorResourceIdLiveData.value).isEqualTo(R.string.password_error)
    }

    @Test
    fun validateForm_passwordWithoutNumber_loginFails(){
        viewModel.validateForm(
            "example@gmail.com", "Password"
        )
        Truth.assertThat(viewModel.passwordErrorResourceIdLiveData.value).isEqualTo(R.string.password_error)
    }

    @Test
    fun validateForm_passwordWithoutUppercase_loginFails(){
        viewModel.validateForm(
            "example@gmail.com", "password1"
        )
        Truth.assertThat(viewModel.passwordErrorResourceIdLiveData.value).isEqualTo(R.string.password_error)
    }

    @Test
    fun validateForm_correctInput_loginSuccess(){
        viewModel.validateForm(
            "example@gmail.com", "Password1"
        )
        Truth.assertThat(viewModel.isFormValidLiveData.value).isTrue()
    }
}