package com.Alkemy.alkemybankbase.repository

import com.Alkemy.alkemybankbase.data.model.expense.ExpenseInput
import com.Alkemy.alkemybankbase.data.model.expense.ExpenseResponse
import com.Alkemy.alkemybankbase.repository.expense.ExpensesRepository
import com.Alkemy.alkemybankbase.utils.Resource

class FakeExpensesRepository : ExpensesRepository {
    /*
    THE ONLY FUNCTION THAT WILL BE TESTED IS A FORM VALIDATION.
    THE IMPLEMENTATION OF THE FUNCTIONS IN THIS CLASS WILL HAVE
    NO RELATION WITH THE TESTS AS THEY WILL NEVER BE CALLED
     */
    override suspend fun addExpense(auth: String, expense: ExpenseInput): Resource<ExpenseResponse> {
        return Resource.Success(ExpenseResponse())
    }
}