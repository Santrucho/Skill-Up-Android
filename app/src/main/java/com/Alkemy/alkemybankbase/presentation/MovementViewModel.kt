package com.Alkemy.alkemybankbase.presentation

import androidx.lifecycle.ViewModel
import com.Alkemy.alkemybankbase.repository.charge.ChargeRepository
import com.Alkemy.alkemybankbase.repository.movement.MovementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovementViewModel @Inject constructor(private val movementRepo : MovementRepository) : ViewModel() {

}