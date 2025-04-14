package com.saras.pupilmesh.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saras.pupilmesh.data.local.entity.NetworkDataEntity
import com.saras.pupilmesh.data.repository.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val mangaRepository: MangaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchMangaDataById(id: String) {
        _uiState.update { DetailUiState.Loading }
        viewModelScope.launch {
            val data = mangaRepository.getMangaDataById(id)
            _uiState.update { DetailUiState.Success(data) }
        }
    }
}

sealed interface DetailUiState {
    data class Success(val manga: NetworkDataEntity) : DetailUiState
    object Loading : DetailUiState
}