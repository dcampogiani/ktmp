package com.danielecampogiani.ktmp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielecampogiani.ktmp.sample.Api
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
                { response ->
                    mutableState.value = response.fold(
                        { error -> MainState.Error(error.errorBody) },
                        { success -> MainState.Result(success.body) }
                    )
                },
                { mutableState.value = MainState.Error(it.message.orEmpty()) }
            )
        }
    }
}