package com.app.listdatawithapi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(private val authRepository: DataRepository) :
    ViewModel() {
    //object declaration
    private val _dataResult = MutableLiveData<Result<ArrayList<DataList>>>()
    val dataResult: LiveData<Result<ArrayList<DataList>>> get() = _dataResult


    fun data() {
        viewModelScope.launch {
            _dataResult.value = authRepository.data()
        }

    }


}