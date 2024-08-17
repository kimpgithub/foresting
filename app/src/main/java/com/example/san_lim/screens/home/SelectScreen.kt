package com.example.san_lim.screens.home

//SelectScreen.kt
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.san_lim.R
import com.example.san_lim.network.RecommendationRequest
import com.example.san_lim.network.RetrofitClient
import com.example.san_lim.ui.theme.ColorPalette
import com.example.san_lim.widgets.AutoRecommendButton
import com.example.san_lim.widgets.RegionSelectButton
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

    val allSelected = region.isNotEmpty() && companions.isNotEmpty() &&
            accommodation.isNotEmpty() && facilities.isNotEmpty() &&
            activities.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPalette.earthyLightGreen)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pagerState.currentPage > 0) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp))
            }

            Text(
                "(${pagerState.currentPage + 1}/5)",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ColorPalette.earthyDarkMoss
            )

            if (pagerState.currentPage < 4) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp))
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = false
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    when (page) {
                        0 -> "어느 지역의 휴양림을 원하시나요?"
                        1 -> "몇 명과 함께 방문하실 계획인가요?"
                        2 -> "숙박을 계획하고 계신가요?"
                        3 -> "어떤 시설을 중요하게 생각하시나요?"
                        4 -> "어떤 활동을 선호하시나요?"
                        else -> ""
                    },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorPalette.darkCharcoal
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (page) {
                    0 -> RegionSelection(region) { selectedRegions ->
                        region = selectedRegions
                    }
                    1 -> CompanionsSelection(companions) { selectedCompanions ->
                        companions = selectedCompanions
                    }
                    2 -> AccommodationSelection(accommodation) { selectedAccommodation ->
                        accommodation = selectedAccommodation
                    }
                    3 -> FacilitiesSelection(facilities) { selectedFacilities ->
                        facilities = selectedFacilities
                    }
                    4 -> ActivitiesSelection(activities) { selectedActivities ->
                        activities = selectedActivities
                    }
                }
            }
        }

        if (pagerState.currentPage == 4) {
            AutoRecommendButton(
                onClick = {
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
                },
                isSelected = allSelected,  // 5개 항목이 모두 선택되었을 때 true
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun RegionSelection(selectedRegions: List<String>, onSelect: (List<String>) -> Unit) {
    val regions = listOf(
        "강원도", "경기도", "경상남도", "경상북도", "대구", "대전",
        "부산", "울산", "인천", "전라남도", "전라북도", "제주도",
        "충청남도", "충청북도"
    )
    val allRegions = regions + "전체"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        allRegions.chunked(3).forEach { rowRegions ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowRegions.forEach { region ->
                    val isSelected = selectedRegions.contains(region)
                    RegionSelectButton(
                        region = region,
                        isSelected = isSelected,  // 여기서 isSelected 상태를 전달합니다.
                        onClick = {
                            val newSelection = when {
                                region == "전체" && !isSelected -> allRegions
                                region == "전체" && isSelected -> emptyList()
                                isSelected -> selectedRegions - region - "전체"
                                else -> (selectedRegions + region).filter { it != "전체" }
                            }
                            onSelect(newSelection)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun CompanionsSelection(companions: String, onSelect: (String) -> Unit) {
    val options = listOf("혼자", "2 ~ 3인", "4인 이상")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        options.forEach { option ->
            val isSelected = companions == option
            val icon: Painter = when (option) {
                "혼자" -> painterResource(id = R.drawable.comp_person_1)
                "2 ~ 3인" -> painterResource(id = R.drawable.comp_person_2)
                "4인 이상" -> painterResource(id = R.drawable.comp_person_4)
                else -> painterResource(id = R.drawable.ic_launcher_foreground)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(option) }
                    .background(
                        color = if (isSelected) ColorPalette.earthyDarkMoss else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (isSelected) Color.White else ColorPalette.softWhite,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = icon,
                        contentDescription = option,
                        modifier = Modifier.size(48.dp),
                        colorFilter = if (isSelected) ColorFilter.tint(ColorPalette.earthyDarkMoss) else ColorFilter.tint(ColorPalette.warmGray)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = option,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else ColorPalette.softWhite
                )
            }
        }
    }
}

@Composable
fun AccommodationSelection(accommodation: String, onSelect: (String) -> Unit) {
    val options = listOf("예", "아니오")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        options.forEach { option ->
            val isSelected = accommodation == option
            val icon: Painter = when (option) {
                "예" -> painterResource(id = R.drawable.accom_sukbak)
                "아니오" -> painterResource(id = R.drawable.accom_dangil)
                else -> painterResource(id = R.drawable.ic_launcher_foreground)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(option) }
                    .background(
                        color = if (isSelected) ColorPalette.earthyDarkMoss else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (isSelected) Color.White else ColorPalette.softWhite,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = icon,
                        contentDescription = option,
                        modifier = Modifier.size(48.dp),
                        colorFilter = if (isSelected) ColorFilter.tint(ColorPalette.earthyDarkMoss) else ColorFilter.tint(ColorPalette.warmGray)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = option,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else ColorPalette.softWhite
                )
            }
        }
    }
}

@Composable
fun FacilitiesSelection(facilities: List<String>, onSelect: (List<String>) -> Unit) {
    val options = listOf(
        "숙박시설",
        "체험 및 교육 시설",
        "편의시설",
        "레저 및 놀이 시설",
        "자연경관 및 명소",
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        options.forEach { option ->
            val isSelected = facilities.contains(option)
            val icon = when (option) {
                "숙박시설" -> painterResource(id = R.drawable.fac_wood_cabin)
                "체험 및 교육 시설" -> painterResource(id = R.drawable.fac_exp)
                "편의시설" -> painterResource(id = R.drawable.fac_amenities)
                "레저 및 놀이 시설" -> painterResource(id = R.drawable.fac_lesuire)
                "자연경관 및 명소" -> painterResource(id = R.drawable.fac_nature)
                else -> painterResource(id = R.drawable.ic_launcher_foreground)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSelect(if (isSelected) facilities - option else facilities + option)
                    }
                    .background(
                        color = if (isSelected) ColorPalette.earthyDarkMoss else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (isSelected) Color.White else ColorPalette.softWhite,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = icon,
                        contentDescription = option,
                        modifier = Modifier.size(48.dp),
                        colorFilter = if (isSelected) ColorFilter.tint(ColorPalette.earthyDarkMoss) else ColorFilter.tint(ColorPalette.warmGray)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = option,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else ColorPalette.softWhite
                )
            }
        }
    }
}

@Composable
fun ActivitiesSelection(activities: List<String>, onSelect: (List<String>) -> Unit) {
    val options = listOf("야영", "등산", "래프팅", "명소탐방", "산책", "풍경감상", "소풍")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        options.forEach { option ->
            val isSelected = activities.contains(option)
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSelect(if (isSelected) activities - option else activities + option)
                    }
                    .background(
                        color = if (isSelected) ColorPalette.earthyDarkMoss else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (isSelected) Color.White else ColorPalette.softWhite,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = icon,
                        contentDescription = option,
                        modifier = Modifier.size(48.dp),
                        colorFilter = if (isSelected) ColorFilter.tint(ColorPalette.earthyDarkMoss) else ColorFilter.tint(ColorPalette.warmGray)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = option,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else ColorPalette.softWhite
                )
            }
        }
    }
}