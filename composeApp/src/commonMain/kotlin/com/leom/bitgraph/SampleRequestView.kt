package com.leom.bitgraph

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.leom.shared.network.NetworkUiState
import com.leom.shared.network.SampleRequestViewModel
import org.koin.compose.koinInject

/**
 * A screen that demonstrates making network API calls using proper ViewModel approach
 * with good coding practices:
 * - Separation of concerns (UI and business logic)
 * - State management via ViewModel
 * - Proper error handling
 * - Loading state feedback
 */
@Composable
fun SampleRequestView(
    viewModel: SampleRequestViewModel = koinInject<SampleRequestViewModel>(),
    platform: String
) {
    // Collect the ViewModel state
    val uiState by viewModel.uiState.collectAsState()

    // Local UI state
    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hello, $platform!", style = MaterialTheme.typography.h6)
        Text("API Request Example", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(16.dp))

        // GET Button
        Button(
            onClick = { viewModel.fetchData() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("GET Sample Data")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Query Input
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Query") },
            placeholder = { Text("Enter search query") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // POST Button
        Button(
            onClick = { viewModel.sendData(query) },
            modifier = Modifier.fillMaxWidth(),
            enabled = query.isNotBlank()
        ) {
            Text("POST Data")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display network state
        NetworkStateDisplay(uiState)
    }
}

/**
 * Component to display the current network state
 */
@Composable
private fun NetworkStateDisplay(state: NetworkUiState) {
    when (state) {
        is NetworkUiState.Idle -> {
            Text("Press a button to make an API request")
        }

        is NetworkUiState.Loading -> {
            CircularProgressIndicator()
        }

        is NetworkUiState.Success -> {
            Text(
                text = state.message,
                color = MaterialTheme.colors.primary
            )
        }

        is NetworkUiState.Error -> {
            Text(
                text = state.message,
                color = MaterialTheme.colors.error
            )
        }
    }
}