@file:OptIn(ExperimentalMaterial3Api::class)

package com.qadri.facecapture.ui.register

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.qadri.facecapture.FaceCaptureTopAppBar
import com.qadri.facecapture.R
import com.qadri.facecapture.ui.navigation.NavigationDestination
import com.qadri.facecapture.ui.theme.AppBlackColor
import com.qadri.facecapture.ui.theme.AppBlueColor
import com.qadri.facecapture.ui.theme.AppFocusColor
import com.qadri.facecapture.ui.theme.AppUnFocusedColor
import com.qadri.facecapture.ui.theme.BackgroundColor
import com.qadri.facecapture.ui.theme.FacebookButtonColor
import com.qadri.facecapture.ui.theme.GoogleButtonColor
import com.qadri.facecapture.ui.theme.RegularFont
import kotlinx.coroutines.launch

object RegisterDestination : NavigationDestination {
    override val route = "register"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
    onSubmit: () -> Unit,
    onGoogleSignInButtonClicked: () -> Unit,
    onLogin: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true
) {

    val registerUiState = viewModel.registerState.collectAsState()
    val state = viewModel.signUpState.collectAsState(initial = null)
    var passwordVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }


    val onFacebookSignInButtonClicked: () -> Unit = {}
    val onSignUpButtonClicked: () -> Unit = {}
    val onForgortPasswordButtonClicked: () -> Unit = {}
    val onSignInButtonClicked: (String, String) -> Unit = { email, password ->
        Log.i("LoginScreen", "Email: $email, Password: $password")
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FaceCaptureTopAppBar(
                title = stringResource(RegisterDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp,
                scrollBehavior = scrollBehavior
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(BackgroundColor)
        ) {
            Image(
                painter = painterResource(id = R.drawable.cirlce_ring),
                contentDescription = "Login ring",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(100.dp)
                    .offset(x = 10.dp, y = (-5).dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.login_logo),
                    contentDescription = "Sample Logo",
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Text(
                    text = stringResource(id = R.string.sign_up),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    fontSize = 28.sp,
                    color = AppBlackColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.sign_up_info),
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    color = AppBlackColor
                )

                Spacer(modifier = Modifier.height(12.dp))
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = registerUiState.value.email,
                    onValueChange = {
                        viewModel.updateUiState(registerUiState.value.copy(email = it))
                    },
                    label = { Text(text = stringResource(id = R.string.label_email_address)) },
                    placeholder = { Text(text = stringResource(id = R.string.hint_enter_email)) },
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = AppFocusColor,
                        unfocusedBorderColor = AppUnFocusedColor
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email Icon",
                            tint = AppFocusColor
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(12.dp))

                val visibilityIcon = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"

                OutlinedTextField(
                    value = registerUiState.value.password,
                    onValueChange = {
                        viewModel.updateUiState(registerUiState.value.copy(password = it))
                    },
                    label = { Text(text = stringResource(id = R.string.label_password)) },
                    placeholder = { Text(text = stringResource(id = R.string.hint_enter_password)) },
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = AppFocusColor,
                        unfocusedBorderColor = AppUnFocusedColor
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock Icon",
                            tint = AppFocusColor
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(text = stringResource(id = R.string.confirm_password_label)) },
                    placeholder = { Text(text = stringResource(id = R.string.hint_confirm_password)) },
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 1,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = if (confirmPassword.text == registerUiState.value.password) AppFocusColor else MaterialTheme.colorScheme.error,
                        unfocusedBorderColor = AppUnFocusedColor
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock Icon",
                            tint = AppFocusColor
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = visibilityIcon, description)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        scope.launch {
                            viewModel.registerUser(
                                registerUiState.value.email,
                                registerUiState.value.password
                            )
                            println(registerUiState.value)
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(AppBlueColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_up),
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (state.value?.isLoading == true) {
                        CircularProgressIndicator()
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Divider(Modifier.weight(0.5f))
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = stringResource(id = R.string.or_continue),
                        style = TextStyle(
                            color = AppFocusColor,
                            fontSize = 12.sp,
                        )
                    )
                    Spacer(Modifier.width(12.dp))
                    Divider(Modifier.weight(0.5f))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onGoogleSignInButtonClicked() },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(GoogleButtonColor),
                        modifier = Modifier
                            .height(45.dp)
                            .fillMaxWidth()
                            .weight(0.5f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_google),
                                contentDescription = "Google icon",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Google",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 12.sp,
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { onFacebookSignInButtonClicked() },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(FacebookButtonColor),
                        modifier = Modifier
                            .height(45.dp)
                            .fillMaxWidth()
                            .weight(0.5f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_fb),
                                contentDescription = "Facebook icon",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Facebook",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 12.sp,
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.already_have_account),
                        color = AppBlackColor,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = stringResource(id = R.string.sign_in),
                        color = AppBlueColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onLogin() }
                    )
                }

                LaunchedEffect(key1 = state.value?.isSuccess) {
                    scope.launch {
                        if (state.value?.isSuccess?.isNotEmpty() == true) {
                            val success = state.value?.isSuccess
                            Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                LaunchedEffect(key1 = state.value?.isError) {
                    scope.launch {
                        if (state.value?.isError?.isNotEmpty() == true) {
                            val error = state.value?.isError
                            Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}