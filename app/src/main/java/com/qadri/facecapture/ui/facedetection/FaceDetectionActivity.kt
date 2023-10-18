package com.qadri.facecapture.ui.facedetection

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.qadri.facecapture.FaceCaptureTopAppBar
import com.qadri.facecapture.ui.camera.CameraXViewModel
import com.qadri.facecapture.ui.facedetection.ui.theme.FaceCaptureTheme
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
class FaceDetectionActivity : ComponentActivity() {

    private lateinit var imageAnalysis: ImageAnalysis

    private lateinit var faceBoxOverlay: FaceBoxOverlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FaceCaptureTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                    val cameraXViewModel: CameraXViewModel = hiltViewModel()
                    val faceViewModel: FaceViewModel = hiltViewModel()
                    val faceCount = faceViewModel.faceCount.collectAsState()
                    Scaffold(
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        topBar = {
                            FaceCaptureTopAppBar(
                                title = "Face Detection",
                                canNavigateBack = true,
                                navigateUp = { this.finish() },
                                scrollBehavior = scrollBehavior
                            )
                        },
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it),
                            contentAlignment = Alignment.Center
                        ) {
                            AndroidView(
                                factory = { ctx ->
                                    val previewView = PreviewView(ctx)
                                    faceBoxOverlay =
                                        FaceBoxOverlay(this@FaceDetectionActivity, null)
                                    cameraXViewModel.processCameraProvider.observe(this@FaceDetectionActivity) { cameraProvider ->
//                                    val cameraProvider = provider

                                        val cameraPreview = androidx.camera.core.Preview.Builder()
                                            .setTargetRotation(previewView.display.rotation).build()
                                            .also {
                                                it.setSurfaceProvider(previewView.surfaceProvider)
                                            }

                                        val cameraSelector = CameraSelector.Builder()
                                            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                                            .build()

                                        cameraProvider.unbindAll()
                                        cameraProvider.bindToLifecycle(
                                            this@FaceDetectionActivity,
                                            cameraSelector,
                                            cameraPreview
                                        )

                                        // Bind image analysis

                                        //Build Face Detector
                                        val detector = FaceDetection.getClient(
                                            FaceDetectorOptions.Builder()
                                                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                                                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
                                                .build()
                                        )

                                        imageAnalysis = ImageAnalysis.Builder()
                                            .setTargetRotation(previewView.display.rotation)
                                            .build()

                                        val cameraExecutor = Executors.newSingleThreadExecutor()
                                        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                                            processImageProxy(
                                                faceViewModel,
                                                detector,
                                                imageProxy,
                                                faceBoxOverlay
                                            )
                                        }
                                        cameraProvider.bindToLifecycle(
                                            this@FaceDetectionActivity,
                                            cameraSelector,
                                            imageAnalysis
                                        )
                                    }
                                    previewView
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                            AndroidView(
                                factory = { _ ->
                                    faceBoxOverlay
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)) {
                                Row(modifier = Modifier.padding(10.dp)) {
                                    Text(text = "Face Detected", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(text = faceCount.value, fontSize = 20.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(
        viewModel: FaceViewModel,
        detector: FaceDetector,
        imageProxy: ImageProxy,
        faceBoxOverlay: FaceBoxOverlay
    ) {
        val inputImage =
            InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)

        detector.process(inputImage)
            .addOnSuccessListener { faces ->
                viewModel.updateCount(faces.size.toString())
                faceBoxOverlay.clear()
                faces.forEach { face ->
                    val box = FaceBox(faceBoxOverlay, face, imageProxy.cropRect)
                    faceBoxOverlay.add(box)

                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    companion object {
        fun start(context: Context) {
            Intent(context, FaceDetectionActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }


}
