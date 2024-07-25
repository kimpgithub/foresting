package com.example.san_lim.screens.home

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.san_lim.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var region by remember { mutableStateOf("") }
    var companions by remember { mutableStateOf("") }
    var accommodation by remember { mutableStateOf("") }
    var facilities by remember { mutableStateOf("") }
    var activities by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text("어느 지역의 휴양림을 원하시나요?", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            RegionSelection { region = it.toString() }

            Spacer(modifier = Modifier.height(16.dp))
            Text("몇 명과 함께 방문하실 계획인가요?", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            CompanionsSelection { companions = it }

            Spacer(modifier = Modifier.height(16.dp))
            Text("숙박을 계획하고 계신가요?", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            AccommodationSelection { accommodation = it }

            Spacer(modifier = Modifier.height(16.dp))
            Text("어떤 시설을 중요하게 생각하시나요?", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            FacilitiesSelection { facilities = it }

            Spacer(modifier = Modifier.height(16.dp))
            Text("어떤 활동을 선호하시나요?", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            ActivitiesSelection { activities = it }

            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { /* TODO: navigate to recommendation screen */ }) {
                Text("자동추천")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionSelection(onSelect: (List<String>) -> Unit) {
    val regions = listOf(
        "강원도",
        "경기도",
        "경상남도",
        "경상북도",
        "대구광역시",
        "대전광역시",
        "부산광역시",
        "서울특별시",
        "울산광역시",
        "인천광역시",
        "전라남도",
        "전라북도",
        "제주특별자치도",
        "충청남도",
        "충청북도"
    ).sorted() + "전체" // 가나다 순으로 정렬하고 "전체" 추가

    var selectedRegions by remember { mutableStateOf(emptyList<String>()) }

    fun toggleRegion(region: String) {
        selectedRegions = if (region == "전체") {
            if (selectedRegions.contains("전체")) {
                emptyList()
            } else {
                regions
            }
        } else {
            if (selectedRegions.contains(region)) {
                selectedRegions - region
            } else {
                selectedRegions + region
            }
        }
        onSelect(selectedRegions)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        regions.chunked(3).forEach { rowRegions ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowRegions.forEach { region ->
                    Button(
                        onClick = {
                            toggleRegion(region)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedRegions.contains(region)) Color.Blue else Color.LightGray,
                            contentColor = if (selectedRegions.contains(region)) Color.White else Color.Black
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) {
                        Text(
                            text = region,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanionsSelection(onSelect: (String) -> Unit) {
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
                "혼자" -> painterResource(id = R.drawable.person_1)
                "2 ~ 3인" -> painterResource(id = R.drawable.person_2)
                "4인 이상" -> painterResource(id = R.drawable.person_4)
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
fun AccommodationSelection(onSelect: (String) -> Unit) {
    val options = listOf("예", "아니오")
    var selectedOption by remember { mutableStateOf("") }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
            val isSelected = selectedOption == option
            val icon: Painter = when (option) {
                "예" -> painterResource(id = R.drawable.sukbak)
                "아니오" -> painterResource(id = R.drawable.dangil)
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

@Composable
fun FacilitiesSelection(onSelect: (String) -> Unit) {
    val options = listOf(
        "숙박시설",
        "체험 및 교육 시설",
        "편의시설",
        "레저 및 놀이 시설",
        "자연경관 및 명소",
    )
    var selectedOption by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        // Split options into rows of 2 and 3 items
        val rows = listOf(
            options.take(2),
            options.drop(2).take(3)
        )

        rows.forEach { rowOptions ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                rowOptions.forEach { option ->
                    val isSelected = selectedOption == option
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                selectedOption = option
                                onSelect(option)
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
fun ActivitiesSelection(onSelect: (String) -> Unit) {
    val options = listOf("야영", "등산", "래프팅", "명소탐방", "산책", "풍경감상", "소풍")
    var selectedOption by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
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
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                rowOptions.forEach { option ->
                    val isSelected = selectedOption == option
                    val icon = when (option) {
                        "야영" -> painterResource(id = R.drawable.act_camping)
                        "등산" -> painterResource(id = R.drawable.act_hiking)
                        "래프팅" -> painterResource(id = R.drawable.act_rafting)
                        "명소탐방" -> painterResource(id = R.drawable.act_seeing)
                        "산책" -> painterResource(id = R.drawable.act_sanwalk)
                        "풍경감상" -> painterResource(id = R.drawable.act_seeing)
                        "소풍" -> painterResource(id = R.drawable.act_picnic)
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
    }
}
