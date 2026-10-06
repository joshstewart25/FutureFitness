package co.future.exerciseprogress.ui.clientdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import co.future.exerciseprogress.data.ClientsRepository
import co.future.exerciseprogress.ui.navigation.ClientDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ClientDetailUiState(
    val clientName: String = ""
)

@HiltViewModel
class ClientDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    clientsRepository: ClientsRepository
) : ViewModel() {

    private val clientID = savedStateHandle.toRoute<ClientDetail>().clientID

    private val _uiState = MutableStateFlow(
        ClientDetailUiState(clientName = clientsRepository.getClient(clientID)?.fullName.orEmpty())
    )
    val uiState: StateFlow<ClientDetailUiState> = _uiState.asStateFlow()
}
