package com.santrikoding.newsapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santrikoding.newsapp.data.Repository
import com.santrikoding.newsapp.data.remote.model.Sliders
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: Repository
) : ViewModel() {
    private var _sliders = MutableLiveData<List<Sliders>>()
    val slider: LiveData<List<Sliders>> get() = _sliders

    init {
        getSliders()
    }

    private fun getSliders() {
        viewModelScope.launch {
            try {
                val remote = repository.getSlider().data
                Log.d("TAG", "Received sliders: $remote")
                _sliders.postValue(remote)
            } catch (e: Exception) {
                Log.d("TAG", "Error Slider $e")
            }
        }
    }
}
