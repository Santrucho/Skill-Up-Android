package com.Alkemy.alkemybankbase.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.Alkemy.alkemybankbase.repository.FakeExpensesRepository
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule

class ExpensesViewModelTest{
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    //Needed Rule to test LiveData changes

    private lateinit var viewModel : ExpensesViewModel

    @Before
    fun setup() {
        viewModel = ExpensesViewModel(FakeExpensesRepository())
    }
}