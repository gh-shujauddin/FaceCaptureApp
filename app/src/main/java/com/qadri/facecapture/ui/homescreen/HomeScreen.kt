@file:OptIn(ExperimentalMaterial3Api::class)

package com.qadri.facecapture.ui.homescreen

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qadri.facecapture.FaceCaptureTopAppBar
import com.qadri.facecapture.R
import com.qadri.facecapture.data.Constant.cameraPermission
import com.qadri.facecapture.ui.camera.ScannerActivity
import com.qadri.facecapture.ui.facedetection.FaceDetectionActivity
import com.qadri.facecapture.ui.login.LoginViewModel
import com.qadri.facecapture.ui.navigation.NavigationDestination
import com.qadri.facecapture.util.cameraPermissionRequest
import com.qadri.facecapture.util.isPermissionGranted
import com.qadri.facecapture.util.openPermissionSetting

object HomeScreenDestination : NavigationDestination {
    override val route: String = "home"
    override val titleRes: Int = R.string.app_name
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    canNavigateBack: Boolean = false
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FaceCaptureTopAppBar(
                title = stringResource(HomeScreenDestination.titleRes),
                canNavigateBack = canNavigateBack,
                scrollBehavior = scrollBehavior
            )
        },
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            val requestPermissionLauncher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        ScannerActivity.startScanner(context)
                    }
                }

            fun requestCameraPermission() {
                when {
                    (context as Activity).shouldShowRequestPermissionRationale(cameraPermission) -> {
                        context.cameraPermissionRequest {
                            context.openPermissionSetting()
                        }
                    }

                    else -> {
                        requestPermissionLauncher.launch(cameraPermission)
                    }
                }
            }

            fun requestPermissionAndStartScanner(context: Context) {
                if (context.isPermissionGranted(cameraPermission)) {
//            startScanner(context, viewModel)
                    ScannerActivity.startScanner(context)
                } else {
                    requestCameraPermission()
                }
            }

            fun requestPermissionAndStartFaceDetection(context: Context) {
                if (context.isPermissionGranted(cameraPermission)) {
                    FaceDetectionActivity.start(context)
                } else {
                    requestCameraPermission()
                }
            }
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                Button(onClick = onLogin, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Login")
                }

                Button(onClick = onRegister, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Register")
                }

                Button(
                    onClick = { requestPermissionAndStartScanner(context) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Open QR Scanner")
                }

                Button(
                    onClick = { requestPermissionAndStartFaceDetection(context) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Face Detection")
                }
            }
        }
    }
}