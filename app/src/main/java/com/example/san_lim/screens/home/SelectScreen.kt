package com.example.san_lim.screens.home

//SelectScreen.kt
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.san_lim.R
import com.example.san_lim.network.RecommendationRequest
import com.example.san_lim.network.RetrofitClient
import com.example.san_lim.ui.theme.ColorPalette
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectScreen(navController: NavHostController) {
    var region by rememberSaveable { mutableStateOf(listOf<String>()) }
    var companions by rememberSaveable { mutableStateOf("") }
    var accommodation by rememberSaveable { mutableStateOf("") }
    var facilities by rememberSaveable { mutableStateOf(listOf<String>()) }
    var activities by rememberSaveable { mutableStateOf(listOf<String>()) }

    val pagerState = rememberPagerState(pageCount = { 5 })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = false
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (page) {
                    0 -> {
                        Text("어느 지역의 휴양림을 원하시나요?", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        RegionSelection(region) { selectedRegions ->
                            region = selectedRegions
                        }
                    }
                    1 -> {
                        Text("몇 명과 함께 방문하실 계획인가요?", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        CompanionsSelection(companions) { selectedCompanions ->
                            companions = selectedCompanions
                        }
                    }
                    2 -> {
                        Text("숙박을 계획하고 계신가요?", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        AccommodationSelection(accommodation) { selectedAccommodation ->
                            accommodation = selectedAccommodation
                        }
                    }
                    3 -> {
                        Text("어떤 시설을 중요하게 생각하시나요?", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        FacilitiesSelection(facilities) { selectedFacilities ->
                            facilities = selectedFacilities
                        }
                    }
                    4 -> {
                        Text("어떤 활동을 선호하시나요?", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        ActivitiesSelection(activities) { selectedActivities ->
                            activities = selectedActivities
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage > 0) {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                },
                enabled = pagerState.currentPage > 0
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
            }

            if (pagerState.currentPage == 4) {
                Button(onClick = {
                    val apiService = RetrofitClient.instance
                    val request = RecommendationRequest(
                        user_id = "test@intel.com",
                        region = region.joinToString(", "),
                        activities = activities,
                        facilities = facilities
                    )

                    apiService.getRecommendations(request).enqueue(object : Callback<List<String>> {
                        override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                            if (response.isSuccessful) {
                                val recommendations = response.body()
                                if (recommendations != null) {
                                    navController.navigate("info_screen/${recommendations.joinToString(",")}")
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<String>>, t: Throwable) {
                            // 에러 처리
                        }
                    })
                }) {
                    Text("자동추천")
                }
            } else {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            if (pagerState.currentPage < 4) {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    enabled = pagerState.currentPage < 4
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                }
            }
        }
    }
}
@Composable
fun RegionSelection(selectedRegions: List<String>, onSelect: (List<String>) -> Unit) {
    val regions = listOf(
        "강원도", "경기도", "경상남도", "경상북도", "대구", "대전",
        "부산", "울산", "인천", "전라남도", "전라북도", "제주도",
        "충청남도", "충청북도", "전체"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        regions.chunked(3).forEach { rowRegions ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowRegions.forEach { region ->
                    val isSelected = selectedRegions.contains(region)
                    OutlinedButton(
                        onClick = {
                            val newSelection = if (isSelected) {
                                selectedRegions - region
                            } else {
                                selectedRegions + region
                            }
                            onSelect(newSelection)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = region,
                            fontSize = 14.sp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.DarkGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun CompanionsSelection(companions: String, onSelect: (String) -> Unit) {
    val options = listOf("혼자", "2 ~ 3인", "4인 이상")
    var selectedOption by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            val isSelected = selectedOption == option
            val icon: Painter = when (option) {
                "혼자" -> painterResource(id = R.drawable.comp_person_1)
                "2 ~ 3인" -> painterResource(id = R.drawable.comp_person_2)
                "4인 이상" -> painterResource(id = R.drawable.comp_person_4)
                else -> painterResource(id = R.drawable.ic_launcher_foreground)
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable {
                        selectedOption = option
                        onSelect(option)
                    }
                    .padding(8.dp)
                    .background(
                        color = if (isSelected) Color.LightGray else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = icon,
                        contentDescription = option,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = option,
                        fontSize = 16.sp,
                        color = if (isSelected) ColorPalette.primaryGreen else Color.Black
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccommodationSelection(accommodation: String, onSelect: (String) -> Unit) {
    val options = listOf("예", "아니오")
    var selectedOption by remember { mutableStateOf("") }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
            val isSelected = selectedOption == option
            val icon: Painter = when (option) {
                "예" -> painterResource(id = R.drawable.accom_sukbak)
                "아니오" -> painterResource(id = R.drawable.accom_dangil)
                else -> painterResource(id = R.drawable.ic_launcher_foreground)
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable {
                        selectedOption = option
                        onSelect(option)
                    }
                    .padding(8.dp)
                    .background(
                        color = if (isSelected) Color(0xFFCCFF90) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = icon,
                        contentDescription = option,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = option,
                        fontSize = 16.sp,
                        color = if (isSelected) Color.Blue else Color.Black
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacilitiesSelection(facilities: List<String>, onSelect: (List<String>) -> Unit) {
    val options = listOf(
        "숙박시설",
        "체험 및 교육 시설",
        "편의시설",
        "레저 및 놀이 시설",
        "자연경관 및 명소",
    )
    var selectedOptions by remember { mutableStateOf(listOf<String>()) }

    fun toggleOption(option: String) {
        selectedOptions = if (selectedOptions.contains(option)) {
            selectedOptions - option
        } else {
            selectedOptions + option
        }
        onSelect(selectedOptions)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Split options into rows of 2 and 3 items
        val rows = listOf(
            options.take(2),
            options.drop(2).take(3)
        )

        rows.forEach { rowOptions ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                rowOptions.forEach { option ->
                    val isSelected = selectedOptions.contains(option)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                toggleOption(option)
                            }
                            .padding(8.dp)
                    ) {
                        val icon = when (option) {
                            "숙박시설" -> painterResource(id = R.drawable.fac_wood_cabin)
                            "체험 및 교육 시설" -> painterResource(id = R.drawable.fac_exp)
                            "편의시설" -> painterResource(id = R.drawable.fac_amenities)
                            "레저 및 놀이 시설" -> painterResource(id = R.drawable.fac_lesuire)
                            "자연경관 및 명소" -> painterResource(id = R.drawable.fac_nature)
                            else -> painterResource(id = R.drawable.ic_launcher_foreground)
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = if (isSelected) Color(0xFFCCFF90) else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                        ) {
                            Image(
                                painter = icon,
                                contentDescription = option,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                        Text(
                            text = option,
                            fontSize = 16.sp,
                            color = if (isSelected) Color.Blue else Color.Black
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesSelection(activities: List<String>, onSelect: (List<String>) -> Unit) {
    val options = listOf("야영", "등산", "래프팅", "명소탐방", "산책", "풍경감상", "소풍")
    var selectedOptions by remember { mutableStateOf(listOf<String>()) }

    fun toggleOption(option: String) {
        selectedOptions = if (selectedOptions.contains(option)) {
            selectedOptions - option
        } else {
            selectedOptions + option
        }
        onSelect(selectedOptions)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Split options into rows of 3 items, with the last row having 1 item
        val rows = listOf(
            options.take(3),
            options.drop(3).take(3),
            options.drop(6)
        )

        rows.forEach { rowOptions ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                rowOptions.forEach { option ->
                    val isSelected = selectedOptions.contains(option)
                    val icon = when (option) {
                        "야영" -> painterResource(id = R.drawable.act_camping)
                        "등산" -> painterResource(id = R.drawable.act_hiking)
                        "래프팅" -> painterResource(id = R.drawable.act_rafting)
                        "명소탐방" -> painterResource(id = R.drawable.act_travel)
                        "산책" -> painterResource(id = R.drawable.act_sanwalk)
                        "풍경감상" -> painterResource(id = R.drawable.act_seeing)
                        "소풍" -> painterResource(id = R.drawable.act_picnic)
                        else -> painterResource(id = R.drawable.ic_launcher_foreground)
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clickable {
                                toggleOption(option)
                            }
                            .padding(8.dp)
                            .background(
                                color = if (isSelected) Color(0xFFCCFF90) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = icon,
                                contentDescription = option,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = option,
                                fontSize = 16.sp,
                                color = if (isSelected) Color.Blue else Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
