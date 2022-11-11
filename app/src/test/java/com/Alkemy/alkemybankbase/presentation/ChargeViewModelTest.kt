package com.Alkemy.alkemybankbase.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.Alkemy.alkemybankbase.repository.FakeChargeRepository
import org.junit.Before
import org.junit.Rule
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
}