package com.example.friendly_words.therapist.ui.materials.list


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MaterialsListViewModel : ViewModel() {

    var uiState = mutableStateOf(MaterialsListState())
        private set

    fun onEvent(event: MaterialsListEvent) {
        when (event) {
            is MaterialsListEvent.SelectMaterial -> {
                uiState.value = uiState.value.copy(selectedIndex = event.index)
            }

            is MaterialsListEvent.RequestDelete -> {
                uiState.value = uiState.value.copy(
                    showDeleteDialog = true,
                    materialToDelete = event.index to event.name
                )
            }

            MaterialsListEvent.ConfirmDelete -> {
                val index = uiState.value.materialToDelete?.first
                if (index != null) {
                    val newMaterials = uiState.value.materials.toMutableList().apply {
                        removeAt(index)
                    }

                    val newSelected = when {
                        newMaterials.isEmpty() -> null
                        uiState.value.selectedIndex == index -> 0
                        uiState.value.selectedIndex != null && uiState.value.selectedIndex!! > index -> uiState.value.selectedIndex!! - 1
                        else -> uiState.value.selectedIndex
                    }

                    uiState.value = uiState.value.copy(
                        materials = newMaterials,
                        selectedIndex = newSelected,
                        showDeleteDialog = false,
                        materialToDelete = null
                    )
                }
            }

            MaterialsListEvent.DismissDeleteDialog -> {
                uiState.value = uiState.value.copy(
                    showDeleteDialog = false,
                    materialToDelete = null
                )
            }
        }
    }
}
