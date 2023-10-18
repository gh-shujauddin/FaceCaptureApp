package com.qadri.facecapture.ui.camera

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.qadri.facecapture.FaceCaptureTopAppBar
import com.qadri.facecapture.ui.camera.ui.theme.FaceCaptureTheme
import com.qadri.facecapture.ui.register.RegisterDestination
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
class ScannerActivity : ComponentActivity() {
    private val lifecycleOwner = this
    private lateinit var imageAnalysis: ImageAnalysis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FaceCaptureTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(this) }
                    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                    val cameraXViewModel: CameraXViewModel = hiltViewModel()
                    val scanQRViewModel: ScanQRViewModel = hiltViewModel()
                    val state = scanQRViewModel.state.collectAsState().value
                    val qrType = state.qrType
                    var qrContent = state.qrContent
                    Scaffold(
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        topBar = {
                            FaceCaptureTopAppBar(
                                title = "Scan QR",
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
//                                cameraProviderFuture.addListener(
//                                    {
//                                        val cameraProvider = cameraProviderFuture.get()
                                    cameraXViewModel.processCameraProvider.observe(this@ScannerActivity) { cameraProvider ->
//                                    val cameraProvider = provider

                                        val cameraPreview = Preview.Builder()
                                            .setTargetRotation(previewView.display.rotation).build()
                                            .also {
                                                it.setSurfaceProvider(previewView.surfaceProvider)
                                            }

                                        val cameraSelector = CameraSelector.Builder()
                                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                            .build()

                                        cameraProvider.unbindAll()
                                        cameraProvider.bindToLifecycle(
                                            lifecycleOwner,
                                            cameraSelector,
                                            cameraPreview
                                        )
                                        // Bind image analysis
                                        val barcodeScanner = BarcodeScanning.getClient(
                                            BarcodeScannerOptions.Builder()
                                                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                                                .build()
                                        )
                                        imageAnalysis = ImageAnalysis.Builder()
                                            .setTargetRotation(previewView.display.rotation)
                                            .build()

                                        val cameraExecutor = Executors.newSingleThreadExecutor()
                                        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                                            processImageProxy(
                                                scanQRViewModel,
                                                barcodeScanner,
                                                imageProxy
                                            )
                                        }
                                        cameraProvider.bindToLifecycle(
                                            lifecycleOwner,
                                            cameraSelector,
                                            imageAnalysis
                                        )

                                    }
                                    previewView
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = "QR Type", fontSize = 18.sp)
                                    Text(
                                        text = qrType,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(text = "QR Content", fontSize = 18.sp)
                                    if (qrType == "URL") {
                                        TextButton(onClick = {
                                            if (!qrContent.startsWith("http://") && !qrContent.startsWith(
                                                    "https://"
                                                )
                                            ) {
                                                qrContent = "http://$qrContent"
                                            }
                                            val browserIntent =
                                                Intent(Intent.ACTION_VIEW, Uri.parse(qrContent))
                                            ContextCompat.startActivity(
                                                this@ScannerActivity,
                                                browserIntent,
                                                savedInstanceState
                                            )
                                        }) {
                                            Text(
                                                text = qrContent,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    } else {
                                        Text(
                                            text = qrContent,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
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
        viewModel: ScanQRViewModel,
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy
    ) {
        val inputImage =
            InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)

        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty()) {
                    showBarcodeInfo(viewModel, barcodes.first())
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun showBarcodeInfo(viewModel: ScanQRViewModel, barcode: Barcode) {
        when (barcode.valueType) {
            Barcode.TYPE_URL -> {
                viewModel.updateState(qrType = "URL", qrContent = barcode.url?.url.toString())
            }

            Barcode.TYPE_CONTACT_INFO -> {
                viewModel.updateState(
                    qrType = "Contact",
                    qrContent = barcode.contactInfo.toString()
                )
            }

            else -> {
                viewModel.updateState(
                    qrType = "Others",
                    qrContent = barcode.rawValue.toString()
                )
            }
        }
    }

    companion object {
        fun startScanner(context: Context) {
            Intent(context, ScannerActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }
}