package com.example.san_lim.screens.trekking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.san_lim.R
import com.example.san_lim.screens.home.DropdownMenuItem
import com.example.san_lim.ui.theme.ColorPalette
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng

val mountainRegions = mapOf(
    "서울" to listOf("관악산", "북한산"),
    "경기" to listOf("감악산", "관악산", "명지산", "삼악산", "소요산", "운악산", "유명산", "축령산", "화악산"),
    "강원" to listOf("가리산", "가리왕산", "계방산", "공작산", "덕유산", "대암산", "백덕산", "백운산(정선)", "백운산(포천)", "방태산", "설악산", "오대산", "치악산", "태백산", "점봉산"),
    "충청" to listOf("계룡산", "대둔산", "덕유산", "덕항산", "서대산", "월악산", "천태산", "청량산", "칠갑산"),
    "전라" to listOf("강천산", "마이산", "모악산", "변산", "방장산", "서대산", "선운산", "월출산", "추월산", "내장산", "백암산", "지리산"),
    "경상" to listOf("가야산", "가지산", "금정산", "남산", "내연산", "덕유산", "무등산", "무학산", "미륵산", "성인봉", "운문산", "주왕산", "주흘산", "지리산", "지리산(통영)", "청량산"),
    "제주" to listOf("한라산")
)

@Composable
fun TrekkingScreen() {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle()
    val trekkingRoutes = remember { mutableStateOf<List<MountainRoute>>(emptyList()) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val currentLocation = remember { mutableStateOf<LatLng?>(null) }
    var selectedMountain by remember { mutableStateOf<MountainRoute?>(null) }

    // 필터링을 위한 상태 관리
    val regions = listOf("전체", "서울", "경기", "강원", "충청", "전라", "경상", "제주")
    var selectedRegion by remember { mutableStateOf("전체") }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        getCurrentLocation(context, fusedLocationClient) { location ->
            currentLocation.value = location
            trekkingRoutes.value = readJsonFiles(context, location)
        }
    }

    // 필터된 산 목록 계산
    val filteredMountains = if (selectedRegion == "전체") {
        trekkingRoutes.value
    } else {
        trekkingRoutes.value.filter { mountain ->
            mountainRegions[selectedRegion]?.contains(mountain.name) == true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPalette.softWhite)  // 화면 전체 배경색 설정
    ) {
        // 지도 부분
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // 지도의 고정된 높이 설정
        ) {

            // MapView
            AndroidView({ mapView }, modifier = Modifier.fillMaxSize()) { mapView ->
                mapView.getMapAsync { googleMap ->
                    googleMap.uiSettings.isZoomControlsEnabled = true
                    currentLocation.value?.let { location ->
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
                        trekkingRoutes.value.forEach { mountainRoute ->
                            drawPolylines(googleMap, mountainRoute.routes)
                        }
                    } ?: run {
                        // Set initial camera position to South Korea
                        val southKoreaLatLng = LatLng(36.0, 127.5)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(southKoreaLatLng, 7f))
                    }
                }
            }

            // South Korea Move Icon
            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.ic_map), // 사용하려는 아이콘 리소스 ID로 변경
                contentDescription = "Move to South Korea",
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd)
                    .clickable {
                        mapView.getMapAsync { googleMap ->
                            val southKoreaLatLng = LatLng(36.0, 127.5)
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(southKoreaLatLng, 7f))
                        }
                    }
            )
        }

        // 필터 선택 UI
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                onClick = { expanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorPalette.earthyDarkGreen,  // backgroundColor 대신 containerColor 사용
                    contentColor = ColorPalette.softWhite
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home), // 필터 아이콘
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "지역 필터: $selectedRegion")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                regions.forEach { region ->
                    DropdownMenuItem(onClick = {
                        selectedRegion = region
                        expanded = false
                    }) {
                        Text(text = region)
                    }
                }
            }
        }

        // 리스트 부분
        TrekkingRouteList(
            mountains = filteredMountains,  // 필터된 산 목록 사용
            selectedMountain = selectedMountain,
            onMountainSelected = { mountain ->
                selectedMountain = mountain
                mapView.getMapAsync { googleMap ->
                    googleMap.clear()
                    mountain!!.routes.forEach { route ->
                        drawPolylines(googleMap, listOf(route))
                    }
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mountain.routes.first().latLngList.first(), 10f))
                }
            },
            onRouteSelected = { selectedRoute ->
                mapView.getMapAsync { googleMap ->
                    googleMap.clear()
                    drawPolylines(googleMap, listOf(selectedRoute))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedRoute.latLngList.first(), 10f))
                }
            },
            onBackClicked = {
                selectedMountain = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // 나머지 공간을 차지하도록 설정
        )
    }
}

@Composable
fun TrekkingRouteList(
    mountains: List<MountainRoute>,
    selectedMountain: MountainRoute?,
    onMountainSelected: (MountainRoute?) -> Unit,
    onRouteSelected: (Route) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPalette.softWhite) // padding 너머의 배경색 설정
    ) {
        if (selectedMountain == null) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp) // 내부 패딩 적용
            ) {
                items(mountains) { mountain ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onMountainSelected(mountain) },
                        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = ColorPalette.earthyDarkMoss) // 카드 배경색 설정
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = mountain.name,
                                style = MaterialTheme.typography.headlineSmall,
                                color = ColorPalette.softWhite // 텍스트 색상 설정
                            )
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp) // 내부 패딩 적용
            ) {
                Button(onClick = onBackClicked, modifier = Modifier.padding(bottom = 8.dp)) {
                    Text("Back to Mountains")
                }
                Text(
                    text = selectedMountain.name,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyColumn {
                    items(selectedMountain.routes) { route ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable { onRouteSelected(route) },
                            colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = ColorPalette.earthyDarkMoss) // 카드 배경색 설정
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Distance: %.2f km".format(route.totalDistance / 1000),
                                    color = ColorPalette.softWhite // 텍스트 색상 설정
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
