//package com.example.san_lim.viewmodels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.san_lim.models.WeedData
//import com.example.san_lim.utils.fetchDataFromFirebase
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//class BookViewModel : ViewModel() {
//
//    // _weedDataList는 내부에서 변경 가능
//    private val _weedDataList = MutableStateFlow<List<WeedData>>(emptyList())
//    // 외부에서는 읽기만 가능하도록 StateFlow로 변환
//    val weedDataList: StateFlow<List<WeedData>> = _weedDataList
//
//    // fetchError와 dataFetched 상태 관리
//    private val _fetchError = MutableStateFlow<Exception?>(null)
//    val fetchError: StateFlow<Exception?> = _fetchError
//
//    private val _dataFetched = MutableStateFlow(false)
//    val dataFetched: StateFlow<Boolean> = _dataFetched
//
//    init {
//        fetchWeedData()
//    }
//
//    // Firebase에서 데이터를 가져오는 함수
//    fun fetchWeedData() {
//        viewModelScope.launch {
//            fetchDataFromFirebase(
//                onDataLoaded = { data ->
//                    _weedDataList.value = data
//                    _dataFetched.value = true
//                },
//                onFailure = { exception ->
//                    _fetchError.value = exception
//                }
//            )
//        }
//    }
//}
