package com.qadri.facecapture.ui.camera

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ScanState(
    val qrType: String = "[TYPE]",
    val qrContent: String = "[CONTENT]"
)

@HiltViewModel
class ScanQRViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ScanState())
    val state = _state.asStateFlow()

    fun updateState(qrType: String, qrContent: String) {
        _state.update {
            it.copy(qrType = qrType, qrContent = qrContent)
        }
    }
}