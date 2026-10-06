package co.future.exerciseprogress.ui.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.future.exerciseprogress.R
import co.future.exerciseprogress.ui.components.withExtraPadding

// TODO: Replace this hardcoded name with real user data once a user model exists.
private const val PLACEHOLDER_USER_NAME = "Josh"

@Composable
fun WelcomeScreen(
    contentPadding: PaddingValues,
    onClientClick: (clientID: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WelcomeContent(
        uiState = uiState,
        contentPadding = contentPadding,
        onClientClick = onClientClick,
        modifier = modifier
    )
}

@Composable
private fun WelcomeContent(
    uiState: WelcomeUiState,
    contentPadding: PaddingValues,
    onClientClick: (clientID: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding.withExtraPadding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.welcome_message, PLACEHOLDER_USER_NAME),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(uiState.clients, key = { it.id }) { client ->
            ClientCard(
                client = client,
                onClick = { onClientClick(client.id) }
            )
        }
    }
}
