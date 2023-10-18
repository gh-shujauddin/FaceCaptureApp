package com.qadri.facecapture.ui.facedetection

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FaceViewModel @Inject constructor(): ViewModel() {
    private val _faceCount = MutableStateFlow("")
    val faceCount = _faceCount.asStateFlow()

    fun updateCount(count: String) {
        _faceCount.value = count
    }
}