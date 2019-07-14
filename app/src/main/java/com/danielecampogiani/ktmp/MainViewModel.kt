package com.danielecampogiani.ktmp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val api = Api()

    private val mutableState = MutableLiveData<MainState>()

    init {
        loadData()
    }

    val state: LiveData<MainState>
        get() = mutableState

    private fun loadData() {
        viewModelScope.launch {
            runCatching {
                api.getRandomPerson().execute()
            }.fold(
                { mutableState.value = MainState.Result(it) },
                { mutableState.value = MainState.Error(it.message.orEmpty()) }
            )
        }
    }
}