package com.example.codingchallenge.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codingchallenge.repository.HomeRepository
import com.example.codingchallenge.repository.Result
import com.example.codingchallenge.response.Cards
import com.example.codingchallenge.response.Event
import kotlinx.coroutines.launch

class HomeViewModel(val repo: HomeRepository) : ViewModel() {

    private var cardsList: MutableLiveData<List<Cards>> = MutableLiveData()

    private val event: MutableLiveData<Event<ViewEvent>> by lazy {
        MutableLiveData<Event<ViewEvent>>()
    }

    fun getHomePageCards(): LiveData<List<Cards>> = cardsList

    fun getEvents(): LiveData<Event<ViewEvent>> {
        return event
    }

    fun loadHomePageCards() {
        viewModelScope.launch {
            when (val result = repo.fetchHomePageResponse()) {
                is Result.Success -> {
                    result.data.let {
                        cardsList.value = it.toList()
                        event.value = Event(ViewEvent.FinishedLoading)
                    }
                }
                is Result.Error -> {
                    event.value = Event(ViewEvent.ShowError(result.message))
                }
            }
        }
    }

    sealed class ViewEvent {
        data class ShowError(val errorMsg: String) : ViewEvent()
        object FinishedLoading : ViewEvent()
    }
}