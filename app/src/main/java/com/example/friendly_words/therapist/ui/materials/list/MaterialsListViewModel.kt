package com.example.friendly_words.therapist.ui.materials.list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.friendly_words.data.entities.Resource
import com.example.friendly_words.data.repositories.ResourceRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MaterialsListViewModel @Inject constructor(
    private val repository: ResourceRepository
) : ViewModel() {

    var uiState = mutableStateOf(MaterialsListState())
        private set

    init {
        loadResources()
    }

    private fun loadResources() {
        viewModelScope.launch {
            val resources = repository.getAll()
            uiState.value = uiState.value.copy(materials = resources)
        }
    }

    fun onEvent(event: MaterialsListEvent) {
        when (event) {
            is MaterialsListEvent.SelectMaterial -> {
                uiState.value = uiState.value.copy(selectedIndex = event.index)
            }

            is MaterialsListEvent.RequestDelete -> {
                uiState.value = uiState.value.copy(
                    showDeleteDialog = true,
                    materialToDelete = event.index to event.resource
                )
            }

            MaterialsListEvent.ConfirmDelete -> {
                val (index, resource) = uiState.value.materialToDelete ?: return
                viewModelScope.launch {
                    repository.delete(resource)

                    val updatedList = uiState.value.materials.toMutableList().apply {
                        removeAt(index)
                    }

                    val newSelected = when {
                        updatedList.isEmpty() -> null
                        uiState.value.selectedIndex == index -> 0
                        uiState.value.selectedIndex != null && uiState.value.selectedIndex!! > index -> uiState.value.selectedIndex!! - 1
                        else -> uiState.value.selectedIndex
                    }

                    uiState.value = uiState.value.copy(
                        materials = updatedList,
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