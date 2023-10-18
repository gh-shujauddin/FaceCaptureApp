@file:OptIn(ExperimentalMaterial3Api::class)

package com.qadri.facecapture.ui.apiscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.qadri.facecapture.FaceCaptureTopAppBar
import com.qadri.facecapture.R
import com.qadri.facecapture.model.LoginResponse
import com.qadri.facecapture.ui.login.LoginViewModel
import com.qadri.facecapture.ui.navigation.NavigationDestination

object ApiScreenDestination : NavigationDestination {
    override val route = "apiScreen"
    override val titleRes = R.string.apiScreen
}

@Composable
fun ApiScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = true
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FaceCaptureTopAppBar(
                title = stringResource(ApiScreenDestination.titleRes),
                canNavigateBack = canNavigateBack,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        },
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
//            viewModel.fetchToken()
//            Text(text = responseState.value.toString())
        }
    }


}