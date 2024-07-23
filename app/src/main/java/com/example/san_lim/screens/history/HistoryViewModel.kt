package com.example.san_lim.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.san_lim.models.WeedData
import com.example.san_lim.utils.fetchDataFromFirebase
import com.example.san_lim.utils.uploadDataToFirebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val _weedDataList = MutableStateFlow<List<WeedData>>(emptyList())
    val weedDataList: StateFlow<List<WeedData>> = _weedDataList

    private val _fetchError = MutableStateFlow<Exception?>(null)
    val fetchError: StateFlow<Exception?> = _fetchError

    private val _dataFetched = MutableStateFlow(false)
    val dataFetched: StateFlow<Boolean> = _dataFetched

    private val _uploadSuccess = MutableStateFlow(false)
    val uploadSuccess: StateFlow<Boolean> = _uploadSuccess

    private val _uploadFailure = MutableStateFlow<Exception?>(null)
    val uploadFailure: StateFlow<Exception?> = _uploadFailure

    init {
        fetchWeedData()
    }

    fun fetchWeedData() {
        viewModelScope.launch {
            fetchDataFromFirebase(
                onDataLoaded = { data ->
                    _weedDataList.value = data
                    _dataFetched.value = true
                },
                onFailure = { exception ->
                    _fetchError.value = exception
                }
            )
        }
    }

    fun uploadWeedData(testImageUri: Uri) {
        viewModelScope.launch {
            uploadDataToFirebase(
                name = "산",
                latitude = 37.5665,
                longitude = 126.9780,
                date = "2023-07-22",
                weedType = "잡초 종류",
                comment = "한줄 코멘트",
                imageUri = testImageUri,
                onSuccess = {
                    _uploadSuccess.value = true
                    _dataFetched.value = false
                    fetchWeedData()
                },
                onFailure = { exception ->
                    _uploadFailure.value = exception
                }
            )
        }
    }
}
