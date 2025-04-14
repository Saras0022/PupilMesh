package com.saras.pupilmesh.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saras.pupilmesh.data.local.entity.NetworkDataEntity
import com.saras.pupilmesh.data.repository.MangaRepository
import com.saras.pupilmesh.utils.ConnectionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val mangaRepository: MangaRepository,
    private val connectionManager: ConnectionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _isConnected = MutableStateFlow(true)
    val isConnected = _isConnected.asStateFlow()

    var pageNumber = 1
        private set

//    fun fetchMangaNetwork(page: Int = 1) {
//        viewModelScope.launch {
//            mangaRepository.fetchMangaDataAndCache(page)
//        }
//    }

    private var wasPreviouslyDisconnected = false

    fun fetchManga(page: Int = 1) {
        pageNumber = page
        viewModelScope.launch {
            connectionManager.isConnected().collect { isConnected ->
                _isConnected.update { isConnected }

                if (isConnected && wasPreviouslyDisconnected) {
                    _uiState.update { HomeUiState.Loading }
                    val result = mangaRepository.fetchData(true)
                    _uiState.update { HomeUiState.Success(result) }
                }

                wasPreviouslyDisconnected = !isConnected

                val result = mangaRepository.fetchData(isConnected, pageNumber)

                _uiState.update { HomeUiState.Success(result) }
            }
        }
    }
}

sealed interface HomeUiState {
    data class Success(val mangaList: List<NetworkDataEntity> = emptyList()) : HomeUiState
    object Loading : HomeUiState
}