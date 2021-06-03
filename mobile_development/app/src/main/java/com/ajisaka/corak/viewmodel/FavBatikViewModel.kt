package com.ajisaka.corak.viewmodel

import androidx.lifecycle.*
import com.ajisaka.corak.model.database.FavBatikRepository
import com.ajisaka.corak.model.entities.FavBatik
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class FavBatikViewModel(private val repository: FavBatikRepository) : ViewModel() {

    fun insert(batik: FavBatik) = viewModelScope.launch {
        repository.insertFavBatikData(batik)
    }

    val allBatikList: LiveData<List<FavBatik>> = repository.allBatikList.asLiveData()
    val favoriteBatik: LiveData<List<FavBatik>> = repository.favoriteBatik.asLiveData()
    fun update(batik: FavBatik) = viewModelScope.launch {
        repository.updateFavBatikData(batik)
    }
    fun delete(batik: FavBatik) = viewModelScope.launch {
        // Call the repository function and pass the details.
        repository.deleteFavBatikData(batik)
    }
}

class FavBatikViewModelFactory(private val repository: FavBatikRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FavBatikViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return FavBatikViewModel(repository) as T
        }
        throw IllegalArgumentException("Unkown ViewModel Class")
    }
}