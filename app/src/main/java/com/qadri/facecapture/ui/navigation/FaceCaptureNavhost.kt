package com.qadri.facecapture.ui.navigation

import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.identity.Identity
import com.qadri.facecapture.ui.homescreen.HomeScreen
import com.qadri.facecapture.ui.homescreen.HomeScreenDestination
import com.qadri.facecapture.ui.login.GoogleAuthUiClient
import com.qadri.facecapture.ui.login.LoginDestination
import com.qadri.facecapture.ui.login.LoginScreen
import com.qadri.facecapture.ui.login.LoginViewModel
import com.qadri.facecapture.ui.profile.ProfileDestination
import com.qadri.facecapture.ui.profile.ProfileScreen
import com.qadri.facecapture.ui.register.RegisterDestination
import com.qadri.facecapture.ui.register.RegisterScreen
import com.qadri.facecapture.ui.register.RegisterViewModel
import kotlinx.coroutines.launch

@Composable
fun FaceCaptureNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    context: Context
) {
    val scope = rememberCoroutineScope()
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }
    NavHost(
        navController = navController,
        startDestination = HomeScreenDestination.route,
        modifier = modifier
    ) {
        composable(HomeScreenDestination.route) {
            HomeScreen(
                onLogin = { navController.navigate(LoginDestination.route) },
                onRegister = { navController.navigate(RegisterDestination.route) }
            )
        }
        composable(LoginDestination.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = Unit) {
                if (googleAuthUiClient.getSignedInUser() != null) {
                    navController.navigate(ProfileDestination.route)
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        scope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInWithGoogleResult(signInResult)
                        }
                    }
                }
            )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(
                        context,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(ProfileDestination.route)
                    viewModel.resetState()
                }
            }
            LoginScreen(
                viewModel = viewModel,
                onRegister = { navController.navigate(RegisterDestination.route) },
                onGoogleButtonClick = {
                    scope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(RegisterDestination.route) {
            val viewModel: RegisterViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = Unit) {
                if (googleAuthUiClient.getSignedInUser() != null) {
                    navController.navigate(ProfileDestination.route)
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        scope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInWithGoogleResult(signInResult)
                        }
                    }
                }
            )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    Toast.makeText(
                        context,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(ProfileDestination.route)
                    viewModel.resetState()
                }
            }
            RegisterScreen(
                onNavigateUp = { navController.navigateUp() },
                onLogin = { navController.navigate(LoginDestination.route) },
                onGoogleSignInButtonClicked = {
                    scope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )

                    }
                }
            )
        }
        composable(ProfileDestination.route) {
            ProfileScreen(
                userData = googleAuthUiClient.getSignedInUser(),
                onSignOut = {
                    scope.launch {
                        googleAuthUiClient.signOut()
                        Toast.makeText(context, "Signed Out", Toast.LENGTH_LONG).show()
                    }
                    navController.navigateUp()
                }
            )
        }
    }
}
