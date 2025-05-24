//package com.example.friendly_words.therapist.ui.configuration.list
//
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//
//class ConfigurationsListViewModel : ViewModel() {
//    private val _state = mutableStateOf(ConfigurationsListState())
//    val state: Thread.State<ConfigurationsListState> = _state
//
//    var activeConfiguration by mutableStateOf<Pair<String, String>?>(null)
//        private set
//
//    fun onEvent(event: ConfigurationsListEvent) {
//        when (event) {
//            is ConfigurationsListEvent.OnSearchQueryChange -> {
//                _state.value = _state.value.copy(searchQuery = event.query)
//            }
//            is ConfigurationsListEvent.RequestDelete -> {
//                _state.value = _state.value.copy(showDeleteDialogFor = event.config)
//            }
//            is ConfigurationsListEvent.ConfirmDelete -> {
//                _state.value = _state.value.copy(
//                    configurations = _state.value.configurations.filter { it != event.config },
//                    showDeleteDialogFor = null
//                )
//                if (activeConfiguration?.first == event.config) {
//                    activeConfiguration = "1 konfiguracja NA STAŁE" to "uczenie"
//                }
//            }
//            ConfigurationsListEvent.DismissDeleteDialog -> {
//                _state.value = _state.value.copy(showDeleteDialogFor = null)
//            }
//            is ConfigurationsListEvent.RequestActivate -> {
//                _state.value = _state.value.copy(showActivateDialogFor = event.config)
//            }
//            is ConfigurationsListEvent.ConfirmActivate -> {
//                activeConfiguration = event.config to "uczenie"
//                _state.value = _state.value.copy(showActivateDialogFor = null)
//            }
//            ConfigurationsListEvent.DismissActivateDialog -> {
//                _state.value = _state.value.copy(showActivateDialogFor = null)
//            }
//            is ConfigurationsListEvent.CopyConfiguration -> {
//                val newName = generateCopyName(event.config, _state.value.configurations)
//                _state.value = _state.value.copy(
//                    configurations = _state.value.configurations + newName
//                )
//            }
//        }
//    }
//
//    fun onSwitchModeChange(mode: String) {
//        activeConfiguration = activeConfiguration?.first?.let { it to mode }
//    }
//}
