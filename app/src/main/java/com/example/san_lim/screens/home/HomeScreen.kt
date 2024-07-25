package com.example.san_lim.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
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
            RegionSelection { region = it }

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

@Composable
fun RegionSelection(onSelect: (String) -> Unit) {
    val regions = listOf("경상남도", "전라남도", "경상북도", "기타")
    var selectedRegion by remember { mutableStateOf("") }
    var otherRegion by remember { mutableStateOf(TextFieldValue("")) }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        regions.forEach { region ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        selectedRegion = region
                        onSelect(region)
                    }
                    .padding(8.dp)
            ) {
                val icon = when (region) {
                    "경상남도" -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                    "전라남도" -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                    "경상북도" -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                    else -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                }
                Icon(
                    imageVector = icon,
                    contentDescription = region,
                    tint = if (selectedRegion == region) Color.Blue else Color.Gray,
                    modifier = Modifier.size(48.dp)
                )
                Text(region, fontSize = 16.sp)
            }
        }
    }
    if (selectedRegion == "기타") {
        BasicTextField(
            value = otherRegion,
            onValueChange = { otherRegion = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(8.dp)
        )
        onSelect(otherRegion.text)
    }
}

@Composable
fun CompanionsSelection(onSelect: (String) -> Unit) {
    val options = listOf("혼자", "연인이나 친구(2~3인)", "가족(4인 이상)")
    var selectedOption by remember { mutableStateOf("") }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
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
                    "혼자" -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                    "연인이나 친구(2~3인)" -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                    "가족(4인 이상)" -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                    else -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                }
                Icon(
                    imageVector = icon,
                    contentDescription = option,
                    tint = if (selectedOption == option) Color.Blue else Color.Gray,
                    modifier = Modifier.size(48.dp)
                )
                Text(option, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun AccommodationSelection(onSelect: (String) -> Unit) {
    val options = listOf("예", "아니오")
    var selectedOption by remember { mutableStateOf("") }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
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
                    "예" -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                    "아니오" -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                    else -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                }
                Icon(
                    imageVector = icon,
                    contentDescription = option,
                    tint = if (selectedOption == option) Color.Blue else Color.Gray,
                    modifier = Modifier.size(48.dp)
                )
                Text(option, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun FacilitiesSelection(onSelect: (String) -> Unit) {
    val options = listOf("자연휴양관", "캠핑장", "산책로", "운동 시설")
    var selectedOption by remember { mutableStateOf("") }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
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
                    "자연휴양관" -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                    "캠핑장" -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                    "산책로" -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                    else -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                }
                Icon(
                    imageVector = icon,
                    contentDescription = option,
                    tint = if (selectedOption == option) Color.Blue else Color.Gray,
                    modifier = Modifier.size(48.dp)
                )
                Text(option, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ActivitiesSelection(onSelect: (String) -> Unit) {
    val options = listOf("산책", "등산", "휴식", "기타")
    var selectedOption by remember { mutableStateOf("") }
    var otherOption by remember { mutableStateOf(TextFieldValue("")) }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
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
                    "산책" -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                    "등산" -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                    "휴식" -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                    else -> ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground)
                }
                Icon(
                    imageVector = icon,
                    contentDescription = option,
                    tint = if (selectedOption == option) Color.Blue else Color.Gray,
                    modifier = Modifier.size(48.dp)
                )
                Text(option, fontSize = 16.sp)
            }
        }
    }
    if (selectedOption == "기타") {
        BasicTextField(
            value = otherOption,
            onValueChange = { otherOption = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(8.dp)
        )
        onSelect(otherOption.text)
    }
}
