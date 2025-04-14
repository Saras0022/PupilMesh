package com.saras.pupilmesh.ui.screen.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saras.pupilmesh.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var email = mutableStateOf("")
        private set
    var password = mutableStateOf("")
        private set

    fun onEmailChange(it: String) {
        email.value = it
    }

    fun onPasswordChange(it: String) {
        password.value = it
    }

    fun onSignIn(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = userRepository.signInOrCreateUser(email.value, password.value)

            if (result) {
                onSuccess()
            } else {
                onError("Invalid Credentials")
            }
        }
    }

}