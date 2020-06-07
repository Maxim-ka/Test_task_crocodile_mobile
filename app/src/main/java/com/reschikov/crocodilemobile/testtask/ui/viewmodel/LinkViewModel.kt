package com.reschikov.crocodilemobile.testtask.ui.viewmodel

import androidx.lifecycle.*
import com.yandex.metrica.YandexMetrica
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LinkViewModel(private val derivable: Derivable) : ViewModel() {

    private val urlLiveData = MutableLiveData<String?>()
    private val isBreakLoadLiveData = MutableLiveData<Boolean>()
    private val errorMessage = MutableLiveData<String?>()

    fun setLink(url : String) { urlLiveData.value = url }

    fun getLink() : LiveData<String?> = urlLiveData
    fun isBreakLoad() : LiveData<Boolean> = isBreakLoadLiveData
    fun getErrorMessage() : LiveData<String?> = errorMessage

    fun requestUserLink(url : String){
        isBreakLoadLiveData.postValue(true)
        viewModelScope.launch(Dispatchers.IO + Job()) {
            try {
                val body = derivable.getResponse(url)
                if (body.isNullOrEmpty()) urlLiveData.postValue(null)
                else  urlLiveData.postValue(body)
            } catch (e: Throwable) {
                errorMessage.postValue(e.message)
                YandexMetrica.reportError("error", e)
            }finally {
                isBreakLoadLiveData.postValue(false)
            }
        }
    }
}