package com.qadri.facecapture.ui.camera

import android.app.Application
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.ExecutionException
import javax.inject.Inject

@HiltViewModel
class CameraXViewModel @Inject constructor(application: Application): AndroidViewModel(application) {

    private val cameraProviderLiveData: MutableLiveData<ProcessCameraProvider> = MutableLiveData()
    val processCameraProvider: LiveData<ProcessCameraProvider>
        get() {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(getApplication())
            cameraProviderFuture.addListener(
                {
                    try {
                        cameraProviderLiveData.setValue(cameraProviderFuture.get())
                    } catch (e: ExecutionException) {
                        e.printStackTrace()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                },
                ContextCompat.getMainExecutor(getApplication())
            )
            return cameraProviderLiveData
        }
}