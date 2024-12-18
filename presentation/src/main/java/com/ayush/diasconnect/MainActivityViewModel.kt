package com.ayush.diasconnect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.data.datastore.UserPreferences
import com.ayush.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch {
            val data = userPreferences.getUserData()
            _uiState.value = if (data.token.isNotEmpty() && data.id.toLong() != -1L) {
                UiState.LoggedIn(User(token = data.token, id = data.id , name = data.name , email = data.email , created = data.created , updated = data.updated , cartId = data.cartId))
            } else {
                UiState.LoggedOut
            }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class LoggedIn(val user: User) : UiState()
        object LoggedOut : UiState()
    }

}