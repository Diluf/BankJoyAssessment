package com.dsg.bankjoyassessment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _validationState = MutableLiveData<ValidationState>()
    val validationState: LiveData<ValidationState> get() = _validationState

    // Hard-coded username and password
    private val correctUsername = "admin"
    private val correctPassword = "password123"

    fun validateCredentials(username: String, password: String) {
        when {
            username.isBlank() -> _validationState.value = ValidationState.Invalid("Username cannot be empty")
            password.isBlank() -> _validationState.value = ValidationState.Invalid("Password cannot be empty")
            //Validate password in a simple manner. Still possible to toughen up the validation based on special characters etc.
            password.length < 6 -> _validationState.value = ValidationState.Invalid("Password must be at least 6 characters")
            (username != correctUsername || password != correctPassword) -> _validationState.value = ValidationState.Invalid("Your username or password is incorrect. Please, try again.")
            else -> _validationState.value = ValidationState.Valid
        }
    }
}

sealed class ValidationState {
    data object Valid : ValidationState()
    data class Invalid(val message: String) : ValidationState()
}