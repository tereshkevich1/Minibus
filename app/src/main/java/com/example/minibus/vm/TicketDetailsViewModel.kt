package com.example.minibus.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.minibus.models.Transport
import com.example.minibus.network.MinibusApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TicketDetailsViewModel(minibusId: Int) : ViewModel() {
    private val _bus = MutableStateFlow<Transport?>(null)
    val bus = _bus.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        loadTransportInfo(minibusId)
    }

    private fun loadTransportInfo(minibusId: Int) {
        viewModelScope.launch {
            _bus.value = MinibusApi.retrofitService.getTransport(minibusId)
            _isLoading.value = false
        }
    }


    //обработать ошибки
    fun deleteOrder(orderId: Int) {
        viewModelScope.launch {
            MinibusApi.retrofitService.deleteOrder(orderId)
        }
    }
}

class TicketDetailsViewModelFactory(private val minibusId: Int) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TicketDetailsViewModel(minibusId) as T

}