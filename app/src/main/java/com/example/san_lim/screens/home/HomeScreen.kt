package com.example.san_lim.screens.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants.IterateForever
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.san_lim.R // 추가된 import 문

//HomeScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
                .padding(bottom = 16.dp)
                .clickable { navController.navigate("select_screen") },
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFD0F0C0)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.home_hue_select),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(alpha = 0.3f)
                )
                Text(
                    text = "휴양림 추천",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Text(
            text = "Quick Access",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        QuickAccessGrid(navController, modifier = Modifier.weight(1f))
    }
}

@Composable
fun QuickAccessItem(
    title: String,
    navController: NavController,
    destination: String,
    isExternalLink: Boolean,
    lottieFileName: String, // Change the parameter to the Lottie animation file name
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .clickable {
                if (isExternalLink) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(destination))
                    context.startActivity(intent)
                } else {
                    navController.navigate(destination)
                }
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.Asset(lottieFileName))
            LottieAnimation(
                composition = composition,
                iterations = IterateForever,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .graphicsLayer(alpha = 0.8f)
            )
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun QuickAccessGrid(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuickAccessItem(
                title = "산림청",
                navController = navController,
                destination = "https://www.forest.go.kr",
                isExternalLink = true,
                lottieFileName = "sanlim_lottie.json", // Replace with your Lottie animation file name in assets
                modifier = Modifier.weight(1f)
            )
            QuickAccessItem(
                title = "트레킹 코스",
                navController = navController,
                destination = "trekking_screen",
                isExternalLink = false,
                lottieFileName = "trekking_lottie.json", // Replace with your Lottie animation file name in assets
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuickAccessItem(
                title = "지역별 휴양림 정보",
                navController = navController,
                destination = "forest_info_screen",
                isExternalLink = false,
                lottieFileName = "huelim_lottie.json", // Replace with your Lottie animation file name in assets
                modifier = Modifier.weight(1f)
            )
            QuickAccessItem(
                title = "봉사 알림",
                navController = navController,
                destination = "https://www.1365.go.kr/vols/1572247904127/partcptn/timeCptn.do",
                isExternalLink = true,
                lottieFileName = "volunteer_lottie.json", // Replace with your Lottie animation file name in assets
                modifier = Modifier.weight(1f)
            )
        }
    }
}
