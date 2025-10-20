package com.example.friendly_words.therapist.ui.materials.list

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.data.entities.Image
import kotlinx.coroutines.flow.filterNotNull

import com.example.shared.data.entities.Resource
import com.example.shared.data.repositories.ImageRepository
import com.example.shared.data.repositories.ResourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaterialsListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resourceRepository: ResourceRepository,
    private val imageRepository: ImageRepository,
) : ViewModel() {

//    var uiState = mutableStateOf(MaterialsListState())
    private val _uiState = MutableStateFlow(MaterialsListState())
    val uiState: StateFlow<MaterialsListState> = _uiState


    init {

        viewModelScope.launch {
            savedStateHandle
                .getStateFlow<Long?>("newlySavedResourceId", null)
                .filterNotNull()            // odfiltrowujemy pierwsze null-emisje
                .collect { newId ->
                    Log.d("MaterialsListVM", "⬅️ really collected newlySavedResourceId = $newId")
                    _uiState.update { it.copy(pendingSelectId = newId) }
                    savedStateHandle["newlySavedResourceId"] = null
                }
        }
        // 2. Nasłuchiwanie zmian w bazie zasobów
        viewModelScope.launch {
            resourceRepository.getAll().collect { rawResources ->
                if (rawResources.isEmpty()) {
                    insertExampleMaterialsWithImages()
                }

                val resources = rawResources.sortedBy { it.name.lowercase() }
                val imagesById = buildMap {
                    for (resource in resources) {
                        put(resource.id, imageRepository.getByResourceId(resource.id))
                    }
                }

                // Jeśli jest ID zasobu do zaznaczenia, szukamy jego indeksu
                val pendingId = _uiState.value.pendingSelectId
                val indexToSelect = pendingId?.let { id ->
                    resources.indexOfFirst { it.id == id }.takeIf { it != -1 }
                }

                _uiState.update {
                    it.copy(
                        materials = resources,
                        imagesForSelected = imagesById,
                        selectedIndex = indexToSelect ?: it.selectedIndex?.coerceAtMost(resources.lastIndex),
                        pendingSelectId = null // Resetujemy
                    )
                }
            }
        }
    }

    private fun normalizeName(name: String): String {
        val map = mapOf(
            'ą' to 'a', 'ć' to 'c', 'ę' to 'e', 'ł' to 'l',
            'ń' to 'n', 'ó' to 'o', 'ś' to 's', 'ź' to 'z', 'ż' to 'z',
            'Ą' to 'A', 'Ć' to 'C', 'Ę' to 'E', 'Ł' to 'L',
            'Ń' to 'N', 'Ó' to 'O', 'Ś' to 'S', 'Ź' to 'Z', 'Ż' to 'Z'
        )
        return name.map { map[it] ?: it }.joinToString("")
    }


    private suspend fun insertExampleMaterialsWithImages()
    {
        val exampleResources = listOf(
            Resource(name = "Balonik", learnedWord = "Balonik", category = "zabawki", isExample = true),
            Resource(name = "Banan", learnedWord = "Banan", category = "jedzenie", isExample = true),
            Resource(name= "Bluzka", learnedWord = "Bluzka", category = "ubrania", isExample = true),
            Resource(name= "Buty", learnedWord = "Buty", category = "ubrania", isExample = true),
            Resource(name = "Bułka", learnedWord = "Bułka", category = "jedzenie", isExample = true),
            Resource(name = "Ciastko", learnedWord = "Ciastko", category = "jedzenie", isExample = true),
            Resource(name = "Ciężarówka", learnedWord = "Ciężarówka", category = "pojazdy", isExample = true),
            Resource(name = "Czapka", learnedWord = "Czapka", category = "ubrania", isExample = true),
            Resource(name = "Czesać", learnedWord = "Czesać", category = "czynności", isExample = true),
            Resource(name = "Gotować", learnedWord = "Gotować", category = "czynności", isExample = true),
            Resource(name = "Huśtać", learnedWord = "Huśtać", category = "czynności", isExample = true),
            Resource(name = "Jabłko", learnedWord = "Jabłko", category = "jedzenie", isExample = true),
            Resource(name = "Jajko", learnedWord = "Jajko", category = "jedzenie", isExample = true),
            Resource(name = "Kąpać się", learnedWord = "Kąpać się", category = "czynności", isExample = true),
            Resource(name = "Klocki", learnedWord = "Klocki", category = "zabawki", isExample = true),
            Resource(name = "Koń", learnedWord = "Koń", category = "zwierzęta", isExample = true),
            Resource(name = "Kot", learnedWord = "Kot", category = "zwierzęta", isExample = true),
            Resource(name = "Kredki", learnedWord = "Kredki", category = "zabawki", isExample = true),
            Resource(name = "Krowa", learnedWord = "Krowa", category = "zwierzęta", isExample = true),
            Resource(name = "Książka", learnedWord = "Książka", category = "zabawki", isExample = true),
            Resource(name = "Kubek", learnedWord = "Kubek", category = "przedmioty", isExample = true),
            Resource(name = "Kuchnia", learnedWord = "Kuchnia", category = "pomieszczenia", isExample = true),
            Resource(name = "Kura", learnedWord = "Kura", category = "zwierzęta", isExample = true),
            Resource(name = "Lalka", learnedWord = "Lalka", category = "zabawki", isExample = true),
            Resource(name = "Las", learnedWord = "Las", category = "miejsca", isExample = true),
            Resource(name = "Łazienka", learnedWord = "Łazienka", category = "pomieszczenia", isExample = true),
            Resource(name = "Lew", learnedWord = "Lew", category = "zwierzęta", isExample = true),
            Resource(name = "Lodówka", learnedWord = "Lodówka", category = "sprzęty", isExample = true),
            Resource(name = "Lody", learnedWord = "Lody", category = "jedzenie", isExample = true),
            Resource(name = "Morze", learnedWord = "Morze", category = "miejsca", isExample = true),
            Resource(name = "Ogórek", learnedWord = "Ogórek", category = "jedzenie", isExample = true),
            Resource(name = "Ołówek", learnedWord = "Ołówek", category = "zabawki", isExample = true),
            Resource(name = "Pies", learnedWord = "Pies", category = "zwierzęta", isExample = true),
            Resource(name = "Piłka", learnedWord = "Piłka", category = "zabawki", isExample = true),
            Resource(name = "Pociąg", learnedWord = "Pociąg", category = "pojazdy", isExample = true),
            Resource(name = "Pokój", learnedWord = "Pokój", category = "pomieszczenie", isExample = true),
            Resource(name = "Prasować", learnedWord = "Prasować", category = "czynności", isExample = true),
            Resource(name = "Rękawiczki", learnedWord = "Rękawiczki", category = "ubrania", isExample = true),
            Resource(name = "Rower", learnedWord = "Rower", category = "pojazdy", isExample = true),
            Resource(name = "Ryba", learnedWord = "Ryba", category = "zwierzęta", isExample = true),
            Resource(name = "Samochód", learnedWord = "Samochód", category = "pojazdy", isExample = true),
            Resource(name = "Samolot", learnedWord = "Samolot", category = "pojazdy", isExample = true),
            Resource(name = "Sklep", learnedWord = "Sklep", category = "miejsca", isExample = true),
            Resource(name = "Słoń", learnedWord = "Słoń", category = "zwierzęta", isExample = true),
            Resource(name = "Spać", learnedWord = "Spać", category = "czynności", isExample = true),
            Resource(name = "Spodnie", learnedWord = "Spodnie", category = "ubrania", isExample = true),
            Resource(name = "Stół", learnedWord = "Stół", category = "meble", isExample = true),
            Resource(name = "Telewizor", learnedWord = "Telewizor", category = "sprzęty", isExample = true),
            Resource(name = "Woda", learnedWord = "Woda", category = "napoje", isExample = true),
            Resource(name = "Żaba", learnedWord = "Żaba", category = "zwierzęta", isExample = true)
            )

        for (resource in exampleResources)
        {
            val resourceId = resourceRepository.insert(resource)

            val baseName = normalizeName(resource.name.lowercase())
            val imagePaths = (1..3).map { index ->
                "file:///android_asset/exemplary_photos/${baseName}_${index}.png"
            }

            val images = imagePaths.map { path -> Image(path = path) }
            val insertedImageIds = images.map { imageRepository.insert(it) }
            imageRepository.linkImagesToResource(resourceId, insertedImageIds)

            imageRepository.insertMany(images)
        }
    }

    fun onEvent(event: MaterialsListEvent) {
        when (event) {
            is MaterialsListEvent.ClearInfoMessage -> {
                _uiState.update { it.copy(infoMessage = null) }
            }
            is MaterialsListEvent.SelectMaterial -> {
                val selected = event.index
                val selectedResource = _uiState.value.materials.getOrNull(selected) ?: return

                viewModelScope.launch {
                    //val allImages = imageRepository.getAll()
                    val relatedImages = imageRepository.getByResourceId(selectedResource.id)

                    val updatedMap = _uiState.value.imagesForSelected.toMutableMap().apply {
                        this[selectedResource.id] = relatedImages
                    }

                    _uiState.update  {it.copy(
                        selectedIndex = selected,
                        imagesForSelected = updatedMap
                    )}


                }
            }
            is MaterialsListEvent.ToggleHideExamples -> {
                _uiState.update { it.copy(hideExamples = event.hide) }
            }
            is MaterialsListEvent.RequestDelete -> {
                _uiState.update {it.copy(
                    showDeleteDialog = true,
                    materialToDelete = event.index to event.resource
                )}
            }
            is MaterialsListEvent.SelectByResourceId -> {
                // znajdź index po resourceId
                val idx = _uiState.value.materials.indexOfFirst { it.id == event.resourceId }
                if (idx != -1) {
                    // zaktualizuj selectedIndex i wczytaj obrazki tak jak przy manualnym kliknięciu
                    onEvent(MaterialsListEvent.SelectMaterial(idx))
                }
            }
            is MaterialsListEvent.ConfirmDelete -> {
                val (index, resource) = _uiState.value.materialToDelete ?: return
                val currentSelected = _uiState.value.selectedIndex
                viewModelScope.launch {
                    resourceRepository.delete(resource)

                    val updatedMaterials = _uiState.value.materials.filter { it.id != resource.id }

                    val newSelectedIndex = when {
                        currentSelected == index -> updatedMaterials.indices.firstOrNull()
                        currentSelected != null && currentSelected > index -> currentSelected - 1
                        else -> currentSelected
                    }

                    _uiState.update {
                        it.copy(
                        selectedIndex = newSelectedIndex,
                        showDeleteDialog = false,
                        materialToDelete = null,
                        infoMessage = "Pomyślnie usunięto materiał"
                    )}
                }

            }
            is MaterialsListEvent.DismissDeleteDialog -> {
                _uiState.update {it.copy(
                    showDeleteDialog = false,
                    materialToDelete = null
                )}
            }
            is MaterialsListEvent.CopyRequested -> {
                _uiState.update { it.copy(showCopyDialogFor = event.resource) }
            }
            is MaterialsListEvent.ConfirmCopy -> {
                viewModelScope.launch {
                    val original = event.resource
                    val allNames = _uiState.value.materials.map { it.name }
                    val newName = generateCopyName(original.name, allNames)

                    val newId = resourceRepository.insert(
                        original.copy(
                            id = 0,
                            name = newName,
                            isExample = false
                        )
                    )

                    val images = imageRepository.getByResourceId(original.id)
                    val imageIds = images.map { it.id }
                    imageRepository.linkImagesToResource(newId, imageIds)

                    _uiState.update {
                        it.copy(
                            showCopyDialogFor = null,
                            infoMessage = "Skopiowano materiał: ${original.name}"
                        )
                    }
                }
            }
            is MaterialsListEvent.DismissCopyDialog -> {
                _uiState.update {
                    it.copy(showCopyDialogFor = null)
                }
            }


        }
    }
    private fun generateCopyName(original: String, existing: List<String>): String {
        if (original !in existing) return original

        var index = 1
        var newName: String
        do {
            newName = "${original}_$index"
            index++
        } while (newName in existing)
        return newName
    }
}