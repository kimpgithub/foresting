package com.example.san_lim.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var region by remember { mutableStateOf("") }
    var companions by remember { mutableStateOf("") }
    var accommodation by remember { mutableStateOf("") }
    var facilities by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
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
            Text("혼자 여행하시나요 아니면 여러 사람과 함께 가시나요?", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            CompanionsSelection { companions = it }

            Spacer(modifier = Modifier.height(16.dp))
            Text("숙박을 원하시나요?", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            AccommodationSelection { accommodation = it }

            Spacer(modifier = Modifier.height(16.dp))
            Text("어떤 시설을 이용하고 싶으신가요?", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            FacilitiesSelection { facilities = it }

            Spacer(modifier = Modifier.height(16.dp))
            Text("예산은 얼마 정도로 생각하시나요?", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            BudgetSelection { budget = it }

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

    Column {
        regions.forEach { region ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedRegion = region
                        if (region != "기타") onSelect(region)
                    }
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = selectedRegion == region,
                    onClick = {
                        selectedRegion = region
                        if (region != "기타") onSelect(region)
                    }
                )
                Text(region, fontSize = 16.sp)
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
}

@Composable
fun CompanionsSelection(onSelect: (String) -> Unit) {
    val options = listOf("혼자", "여러 사람")
    var selectedOption by remember { mutableStateOf("") }

    Column {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedOption = option; onSelect(option) }
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = { selectedOption = option; onSelect(option) }
                )
                Text(option, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun AccommodationSelection(onSelect: (String) -> Unit) {
    val options = listOf("숲속의 집", "캠핑장", "기타")
    var selectedOption by remember { mutableStateOf("") }
    var otherOption by remember { mutableStateOf(TextFieldValue("")) }

    Column {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedOption = option
                        if (option != "기타") onSelect(option)
                    }
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = {
                        selectedOption = option
                        if (option != "기타") onSelect(option)
                    }
                )
                Text(option, fontSize = 16.sp)
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
}

@Composable
fun FacilitiesSelection(onSelect: (String) -> Unit) {
    val options = listOf("글램핑장", "야영장", "세미나실", "기타")
    var selectedOption by remember { mutableStateOf("") }
    var otherOption by remember { mutableStateOf(TextFieldValue("")) }

    Column {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedOption = option
                        if (option != "기타") onSelect(option)
                    }
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = {
                        selectedOption = option
                        if (option != "기타") onSelect(option)
                    }
                )
                Text(option, fontSize = 16.sp)
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
}

@Composable
fun BudgetSelection(onSelect: (String) -> Unit) {
    val options = listOf("5,000원 이하", "10,000원 이하", "15,000원 이하", "기타")
    var selectedOption by remember { mutableStateOf("") }
    var otherOption by remember { mutableStateOf(TextFieldValue("")) }

    Column {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedOption = option
                        if (option != "기타") onSelect(option)
                    }
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = {
                        selectedOption = option
                        if (option != "기타") onSelect(option)
                    }
                )
                Text(option, fontSize = 16.sp)
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
}

@Composable
fun ActivitiesSelection(onSelect: (String) -> Unit) {
    val options = listOf("산책", "등산", "휴식", "기타")
    var selectedOption by remember { mutableStateOf("") }
    var otherOption by remember { mutableStateOf(TextFieldValue("")) }

    Column {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedOption = option
                        if (option != "기타") onSelect(option)
                    }
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = {
                        selectedOption = option
                        if (option != "기타") onSelect(option)
                    }
                )
                Text(option, fontSize = 16.sp)
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
}

